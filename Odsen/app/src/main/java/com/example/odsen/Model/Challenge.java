package com.example.odsen.Model;

import android.util.Log;

import com.example.odsen.Tags.LogTags;

import java.util.Date;

public class Challenge implements Comparable<Challenge> {
    private String text;
    private String completedBy;
    private Date dateCompleted;


    public Challenge(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }


    public void complete(String user)  {
        if (completedBy == null && dateCompleted == null) {
            completedBy = user;
            dateCompleted = new Date();
        } else if (completedBy != null && dateCompleted != null) {
            Log.e(LogTags.ILLEGAL_INPUT, "Challenge: kunne ikke fullføre challenge");
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

    @Override
    public int compareTo(Challenge challenge) {
        if (this.getCompletedBy() == null) {
            if (challenge.getCompletedBy() == null) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (challenge.getCompletedBy() == null) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
