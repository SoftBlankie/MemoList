package com.example.memolist;

/**
 * Created by Kinred on 4/16/18.
 */

public class data_model {
    String importance;
    String message;
    String dateEdited;
    String alarm;
    String notification;

    public data_model(String importance, String message, String dateEdited, String alarm, String notification) {
        this.importance = importance;
        this.message = message;
        this.dateEdited = dateEdited;
        this.alarm = alarm;
        this.notification = notification;
    }

    public void setImportance(String importance) {this.importance = importance; }

    public void setMessage(String message) {this.message = message; }

    public void setDateEdited(String dateEdited) {this.dateEdited = dateEdited; }

    public void setAlarm(String alarm) {this.alarm = alarm; }

    public void setNotification(String notification) {this.notification = notification; }

    public String getImportance() {return importance; }

    public String getMessage() {return message; }

    public String getDateEdited() {return dateEdited; }

    public String getAlarm() {return alarm; }

    public String getNotification() {return notification; }
}
