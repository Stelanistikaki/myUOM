package com.example.admin.myuom.Settings;

class Student {
    private String last_name, first_name, department, direction;
    private int semester;

    //class for the student values

    public void setLastname(String last_name) {
        this.last_name = last_name;
    }

    public void setFirstname(String first_name) {
        this.first_name = first_name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getDepartment() {
        return department;
    }

    public String getDirection() {
        return direction;
    }

    public int getSemester() {
        return semester;
    }
}
