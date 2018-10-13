package com.example.memolist;

public class data_model {
    private String importance;
    private String message;
    private String dateEdited;

    public data_model(String importance, String message, String dateEdited) {
        this.importance = importance;
        this.message = message;
        this.dateEdited = dateEdited;
    }

    public void setImportance(String importance) {this.importance = importance; }

    public void setMessage(String message) {this.message = message; }

    public void setDateEdited(String dateEdited) {this.dateEdited = dateEdited; }

    public String getImportance() {return importance; }

    public String getMessage() {return message; }

    public String getDateEdited() {return dateEdited; }
}
