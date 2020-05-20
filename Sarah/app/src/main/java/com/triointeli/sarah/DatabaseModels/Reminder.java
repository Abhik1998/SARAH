// made by Abhik
package com.triointeli.sarah.DatabaseModels;

import io.realm.RealmObject;

public class Reminder extends RealmObject {
    private String reminderContent;
    private String dateTime;
    private boolean done;
    private String placeOnEnter;
    private String placeOnLeave;

    public Reminder() {

    }

    public Reminder(String reminderContent, String dateTime, boolean done, String placeOnEnter, String placeOnLeave) {
        this.reminderContent = reminderContent;
        this.dateTime = dateTime;
        this.done = done;
        this.placeOnEnter = placeOnEnter;
        this.placeOnLeave = placeOnLeave;
    }

    public String getReminderContent() {
        return reminderContent;
    }

    public void setReminderContent(String reminderContent) {
        this.reminderContent = reminderContent;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getPlaceOnEnter() {
        return placeOnEnter;
    }

    public void setPlaceOnEnter(String placeOnEnter) {
        this.placeOnEnter = placeOnEnter;
    }

    public String getPlaceOnLeave() {
        return placeOnLeave;
    }

    public void setPlaceOnLeave(String placeOnLeave) {
        this.placeOnLeave = placeOnLeave;
    }
}
