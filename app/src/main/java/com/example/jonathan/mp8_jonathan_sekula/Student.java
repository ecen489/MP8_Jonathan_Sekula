package com.example.jonathan.mp8_jonathan_sekula;

import java.io.Serializable;



public class Student implements Serializable {
    String email;
    int id;
    String name;
    String password;

    public Student() {}
    public Student(String em, int idd, String nm, String psw){
        email = em;
        id = idd;
        name = nm;
        password = psw;
    }

    public String getemail() {
        return email;
    }
    public int getid() {
        return id;
    }
    public String getname() {
        return name;
    }
    public String getpassword() {
        return password;
    }
}
