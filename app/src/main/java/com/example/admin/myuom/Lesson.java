package com.example.admin.myuom;

public class Lesson {

    //class for Lesson values
    private String name, id_lesson;
    private int semester;

    //getters and setters for the parameters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id_lesson;
    }

    public void setId(String id) {
        this.id_lesson = id;
    }

    public int getSemester() {
        return semester;
    }
    public void setSemester(int semester) {
        this.semester = semester;
    }
}
