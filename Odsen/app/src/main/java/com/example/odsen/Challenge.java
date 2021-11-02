package com.example.odsen;

import java.util.Date;

public class Challenge {
    private String text;
    private String completedBy;
    private Date dateCompleted;


    public Challenge(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }


    public void complete(String user) throws Exception {
        if (completedBy == null && dateCompleted == null) {
            completedBy = user;
            dateCompleted = new Date();
        } else if (completedBy != null && dateCompleted != null) {
            throw new Exception("Challenge already completed by another user");
        }
    }

    public Challenge() {
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }
}
