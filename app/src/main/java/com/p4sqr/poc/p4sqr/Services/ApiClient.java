package com.p4sqr.poc.p4sqr.Services;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.p4sqr.poc.p4sqr.app.Constants;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;
    private static int REQUEST_TIMEOUT = 60;
    private static OkHttpClient okHttpClient;
    private static final String APIKEY= "";

    public static Retrofit getClient(Context context) {
        if (okHttpClient == null) {
            initOkHttp(context);
        }

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return retrofit;
    }

    private static void initOkHttp(final Context context) {
            OkHttpClient.Builder httpClient =new OkHttpClient.Builder()
                                                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                                                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                                                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);



        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Interceptor headAuthorizationIntercepter = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();

                // Adding Authorization token (API Key)
                // Requests will be denied without API key

                    requestBuilder.addHeader("key", APIKEY);


                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };

//        httpClient.addInterceptor(headAuthorizationIntercepter);
        httpClient.addInterceptor(interceptor);

        okHttpClient = httpClient.build();
    }

}
