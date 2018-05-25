package com.solution.tecno.realtimedata;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity{
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button btn_new_val;
    EditText txt_new_val;
    TextView tv_user;
    ProgressBar progressBar;

    private RecyclerView recyclerView;
    private DeviceAdapter adapter;
    List<DataSnapshot> l=new ArrayList<>();

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String email="rogger.aburto@gmail.com";
        String password="*45139300*";
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Login incorrecto",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Login correcto",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                // Name, email address, and profile photo Url
                                System.out.println(user);
                                String name = user.getDisplayName();
                                String phone=user.getPhoneNumber();
                                String email = user.getEmail();
                                Toast.makeText(MainActivity.this, name+"-"+email+"-"+phone, Toast.LENGTH_SHORT).show();
                                System.out.println(name+"-"+email);
                                Uri photoUrl = user.getPhotoUrl();

                                // The user's ID, unique to the Firebase project. Do NOT use this value to
                                // authenticate with your backend server, if you have one. Use
                                // FirebaseUser.getToken() instead.
                                String uid = user.getUid();
                            }
                        }
                    }
                });

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("", "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "Credenciales Incorrectas",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Login correcto",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                // Name, email address, and profile photo Url
                                System.out.println(user);
                                String name = user.getDisplayName();
                                String email = user.getEmail();
                                String phone=user.getPhoneNumber();
                                Toast.makeText(MainActivity.this, name+"-"+email+"-"+phone, Toast.LENGTH_SHORT).show();
                                System.out.println(name+"-"+email);
                                Uri photoUrl = user.getPhotoUrl();

                                // The user's ID, unique to the Firebase project. Do NOT use this value to
                                // authenticate with your backend server, if you have one. Use
                                // FirebaseUser.getToken() instead.
                                String uid = user.getUid();
                            }
                        }

                        // ...
                    }
                });

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        btn_new_val=findViewById(R.id.btn_new_val);
        txt_new_val=findViewById(R.id.txt_new_val);
        tv_user=findViewById(R.id.bd_users);
        progressBar=findViewById(R.id.progress_bar);

        recyclerView=findViewById(R.id.device_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new DeviceAdapter(l);
        recyclerView.setAdapter(adapter);

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
                l.clear();
                for (DataSnapshot values: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    l.add(values);
                }
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
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

    public void deleteElement(String key){
        myRef.child(key).removeValue();
    }

    public void updateElement(String key,String val){
        myRef.child(key).child("name").setValue(val);
    }
}
