package com.example.jonathan.mp8_jonathan_sekula;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PushActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference root_reference;

    FirebaseAuth mAuth;
    FirebaseUser user = null;

    EditText course_id;
    EditText course_name;
    EditText grade;

    int student_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        database = FirebaseDatabase.getInstance();
        root_reference = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        course_id = findViewById(R.id.course_id);
        course_name = findViewById(R.id.course_name);
        grade = findViewById(R.id.grade);
    }

    public void RadioClick(View view) {

        switch(view.getId()) {
            case R.id.bart:
                student_id = 123;
                break;
            case R.id.lisa:
                student_id = 888;
                break;
            case R.id.milhouse:
                student_id = 456;
                break;
            case R.id.ralph:
                student_id = 404;
                break;
        }

    }

    public void PushData(View view) {

        if (student_id != 0) {

            user = mAuth.getCurrentUser();
            if (user != null) {

                if (!(course_id.getText().toString().equals("") || course_name.getText().toString().equals("")
                        || grade.getText().toString().equals(""))) {

                    DatabaseReference grades_reference = root_reference.child("simpsons/grades/");
                    Grade grade_object = new Grade(Integer.parseInt(course_id.getText().toString()),
                            course_name.getText().toString(), grade.getText().toString(), student_id);
                    grades_reference.push().setValue(grade_object);

                    Toast.makeText(this, "Data Successfully Pushed!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, PullActivity.class);
                    this.startActivity(intent);

                }

                else {
                    Toast.makeText(this, "Please Fill Out All Fields...", Toast.LENGTH_SHORT).show();
                }
            }

            else {
                Toast.makeText(this, "Please Login...", Toast.LENGTH_SHORT).show();
            }

        }

        else {
            Toast.makeText(this, "Please Select a Student...", Toast.LENGTH_SHORT).show();
        }

    }
}
