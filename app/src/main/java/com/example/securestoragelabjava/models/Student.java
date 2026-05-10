package com.example.securestoragelabjava.models;

public class Student {
    private int id;
    private String fullName;
    private int studentAge;

    public Student(int id, String fullName, int studentAge) {
        this.id = id;
        this.fullName = fullName;
        this.studentAge = studentAge;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public int getStudentAge() { return studentAge; }

    @Override
    public String toString() {
        return "ID: " + id + " | Nom: " + fullName + " | Age: " + studentAge;
    }
}