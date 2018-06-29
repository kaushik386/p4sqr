package com.p4sqr.poc.p4sqr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.p4sqr.poc.p4sqr.Model.SectionModel;

import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionHolder> {

    Context mContext;
   List<SectionModel> mSections;
    ClickEvent mClickEvent;

    public SectionAdapter(Context context, ClickEvent clickEvent,List<SectionModel> list) {

        mContext = context;
        mClickEvent =clickEvent;
        mSections = list;
    }

    @NonNull
    @Override
    public SectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.section_card,parent,false);

        return new SectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionHolder holder, int position) {
    holder.checkBox.setText(mSections.get(position).getmName());
    holder.checkBox.setChecked(mSections.get(position).isChecked());

    }

    @Override
    public int getItemCount() {
        return mSections.size();
    }

    public void swap(List<SectionModel> section) {

        this.mSections = section;
        notifyDataSetChanged();
    }


    interface ClickEvent
    {
        void sendBack(int adapterPosition);
    }

    public class SectionHolder extends RecyclerView.ViewHolder implements  CompoundButton.OnCheckedChangeListener {


        CheckBox checkBox;


        public SectionHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox2);
            checkBox.setOnCheckedChangeListener(this);

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(buttonView.isChecked()) {
                mClickEvent.sendBack(getAdapterPosition());
            }
        }
    }
}
