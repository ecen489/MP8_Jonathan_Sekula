package com.example.jonathan.mp8_jonathan_sekula;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PullActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference root_reference;

    FirebaseAuth mAuth;
    FirebaseUser user = null;

    EditText editText_student_id;
    ListView list;

    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> query1_items = new ArrayList<>();

    String course_name;
    String grd;

    int j = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull);

        database = FirebaseDatabase.getInstance();
        root_reference = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        editText_student_id = findViewById(R.id.student_id);
        list = findViewById(R.id.list);
    }

    public void Query1(View view) {
        user = mAuth.getCurrentUser();

        if (user != null) {
            DatabaseReference grade_reference = root_reference.child("simpsons/grades/");

            if (IsInteger(editText_student_id.getText().toString()) && !editText_student_id.getText().toString().isEmpty()) {
                int student_id = Integer.parseInt(editText_student_id.getText().toString());
                Query query = grade_reference.orderByChild("student_id").equalTo(student_id);

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<String> items = new ArrayList<>();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Grade grade = snapshot.getValue(Grade.class);
                                items.add(grade.getcourse_name() + ", " + grade.getgrade());
                            }

                            String[] items_array = new String[items.size()];
                            items.toArray(items_array);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
                            list.setAdapter(adapter);

                        } else {
                            list.setAdapter(null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };

                query.addListenerForSingleValueEvent(valueEventListener);
            }

            else {
                Toast.makeText(this, "Enter an Integer...", Toast.LENGTH_SHORT).show();
            }

        }

        else {
            Toast.makeText(this, "Please Login...", Toast.LENGTH_SHORT).show();
        }

    }

    public void Query2(View view) {
        user = mAuth.getCurrentUser();

        if (user != null) {
            DatabaseReference grade_reference = root_reference.child("simpsons/grades/");
            final DatabaseReference students_reference = root_reference.child("simpsons/students");

            if (IsInteger(editText_student_id.getText().toString()) && !editText_student_id.getText().toString().isEmpty()) {
                int student_id = Integer.parseInt(editText_student_id.getText().toString());
                Query query = grade_reference.orderByChild("student_id").startAt(student_id);

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@ NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Grade grade = snapshot.getValue(Grade.class);
                                int id = grade.getstudent_id();
                                course_name = grade.getcourse_name();
                                grd = grade.getgrade();
                                query1_items.add(course_name + ", " + grd);


                                Query subquery = students_reference.orderByChild("id").equalTo(id);

                                ValueEventListener sub_valueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {

                                            Student student = dataSnapshot.getChildren().iterator().next().getValue(Student.class);
                                            items.add(student.getname() + ", " + query1_items.get(j));
                                            ++j;

                                            String[] items_array = new String[items.size()];
                                            items.toArray(items_array);

                                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, items);

                                            list.setAdapter(adapter);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                };

                                subquery.addListenerForSingleValueEvent(sub_valueEventListener);
                                subquery.removeEventListener(sub_valueEventListener);

                            }

                        }

                        else {
                            list.setAdapter(null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                };

                query.addListenerForSingleValueEvent(valueEventListener);
                items.clear();
                query1_items.clear();
                j = 0;
            }

            else {
                Toast.makeText(this, "Enter an Integer...", Toast.LENGTH_SHORT).show();
            }

        }

        else {
            Toast.makeText(this, "Please Login...", Toast.LENGTH_SHORT).show();
        }

    }

    public void SignOut(View view) {
        mAuth.signOut();
        user = null;
        Toast.makeText(this, "User Logged Out!", Toast.LENGTH_SHORT).show();
    }

    public void Push(View view) {
        Intent intent = new Intent(this, PushActivity.class);
        this.startActivity(intent);
    }

    public boolean IsInteger(String text) {

        boolean is_integer = true;

        for (int i = 0; i < text.length(); ++i) {
            if (!Character.isDigit(text.charAt(i)))
                is_integer = false;
        }

        return is_integer;
    }

}
