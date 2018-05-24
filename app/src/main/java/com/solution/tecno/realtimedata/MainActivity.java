package com.solution.tecno.realtimedata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity{
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button btn_new_val;
    EditText txt_new_val;
    TextView tv_user;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        btn_new_val=findViewById(R.id.btn_new_val);
        txt_new_val=findViewById(R.id.txt_new_val);
        tv_user=findViewById(R.id.bd_users);
        progressBar=findViewById(R.id.progress_bar);
        btn_new_val.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String val=txt_new_val.getText().toString();
                addNewValue(val);
                txt_new_val.setText("");
            }
        });

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int cont=1;
                String list="";
                for (DataSnapshot values: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    HashMap<String,String> devices=(HashMap<String,String>)values.getValue();
                    list+=cont+" "+devices.get("name")+"\n";
                    cont++;
                }
                progressBar.setVisibility(View.GONE);
                tv_user.setText(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to read value."+ error.toException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addNewValue(String value){
        myRef.push().child("name").setValue(value);
    }
}
