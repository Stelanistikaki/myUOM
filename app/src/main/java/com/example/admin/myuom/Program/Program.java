package com.example.admin.myuom.Program;

import android.util.Log;

class Program {
    private String title, time, classroom;

    public void setTime(String time) {
        String theTime[] = time.split(" -");
        this.time = theTime[0]+ ":00";
    }

    public void setTitle(String title) {
        String theTitle[] = title.split("-");
        setClassroom(theTitle[3]);
        this.title = theTitle[0];
    }
    public void setClassroom(String classroom){
        this.classroom = classroom;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getClassroom() {
        return classroom;
    }
}