package com.triointeli.sarah;
import android.support.v7.widget.RecyclerView;

import com.triointeli.sarah.DatabaseModels.Reminder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.triointeli.sarah.DatabaseModels.Reminder;
import com.triointeli.sarah.R;

import java.util.ArrayList;

import com.triointeli.sarah.DatabaseModels.Reminder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.triointeli.sarah.DatabaseModels.Reminder;
import com.triointeli.sarah.R;

import java.util.ArrayList;


public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    private ArrayList<Reminder> reminders;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView remainder,placeIn,placeout;
        TextView dateTime;
        CheckBox mCheckbox;

        public ViewHolder(View view) {
            super(view);
            remainder = (TextView) view.findViewById(R.id.reminder);
            dateTime = (TextView) view.findViewById(R.id.dateTime);
//            mCheckbox = (CheckBox) view.findViewById(R.id.checkbox);
            placeIn=(TextView)view.findViewById(R.id.placeIn);
            placeout=(TextView)view.findViewById(R.id.placeOut);
        }
    }

    public ReminderAdapter(ArrayList<Reminder> reminders) {
        this.reminders = reminders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);

        holder.remainder.setText(reminder.getReminderContent());
        holder.dateTime.setText(reminder.getDateTime());
//        holder.mCheckbox.setChecked(reminder.isDone());
        holder.placeIn.setText(reminder.getPlaceOnEnter());
        holder.placeout.setText(reminder.getPlaceOnLeave());

    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }


}