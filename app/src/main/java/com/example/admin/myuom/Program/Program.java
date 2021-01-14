package com.example.admin.myuom.Program;

class Program {

    //class to set the Program values

    private String title, time, classroom;
    //the time should start from the first hour
    //it is given like: 9-11 -> "9:00"
    public void setTime(String time) {
        String theTime[] = time.split(" -");
        this.time = theTime[0]+ ":00";
    }

    public void setTitle(String title) {
        String theTitle[] = title.split("/");
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
