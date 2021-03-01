package com.harshit.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Main";
    public static final String SHARED = "users";
    RecyclerView recyclerView;
    ArrayList<MyList> lists = new ArrayList<>();
    ListAdapter listAdapter;
    EditText itemEt;
    Button itemBt;

    DatabaseReference databaseReference;
    ProgressDialog pbar;

    FirebaseUser mUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycle);
        itemBt = findViewById(R.id.addBt);
        itemEt = findViewById(R.id.addEt);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser == null) {
            Intent it = new Intent(getApplicationContext(), Login.class);
            startActivity(it);
            return;
        }

        databaseReference =  FirebaseDatabase.getInstance().getReference().child("users");


        //progress dialogue
        pbar = new ProgressDialog(this);
        pbar.setMessage("Please Wait...");
        pbar.setCanceledOnTouchOutside(false);
        pbar.show();



        listAdapter = new ListAdapter(getApplicationContext(), lists, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);

        //filter
        itemEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                String changed = editable.toString();
                ArrayList<MyList> myNewList = new ArrayList<>();
                for(int i = 0; i<lists.size(); i++ ) {
                    if(lists.get(i).getText().contains(changed)) {
                     myNewList.add(lists.get(i));
                    }
                }
                listAdapter.renewItem(myNewList);
            }
        });

        //list => String , Integer list.contains(
        //list => MyList-> string )

        itemBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = itemEt.getText().toString();
                if(value.isEmpty()){
                    itemEt.setError("Field can't be empty");
                    itemEt.requestFocus();
                    return;
                }
                //current times in mili - use as id
                String id = System.currentTimeMillis()+"";

                //ussing id
                databaseReference.child(id).setValue(value)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                               addToRecyclerView(value,id);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });



            }
        });






        /************************************************/

//        FirebaseDatabase mRef = FirebaseDatabase.getInstance();
//        DatabaseReference dRef = mRef.getReference("status");

//        dRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot data) {
//
//                Log.d(TAG, data.getValue().toString());
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d(TAG, "Error" +error.toString());
//            }
//        });
//        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot data) {
//
//                String key = data.getKey();
//                Log.d(TAG, "Key =  " +key);
//                Log.d(TAG, data.getValue().toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


//        dRef.child("123456").child("age").setValue("21");
//        dRef.child("123456").child("name").setValue("Harshit");
//        dRef.child("123456").child("gender").setValue("M");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                if(data.exists()){
                    lists.clear();
                    for (DataSnapshot myData: data.getChildren()) {
                        // TODO: handle the post
                        String key = myData.getKey().toString();
                        String value = myData.getValue().toString();
                        lists.add(new MyList(value,key));
                    }
                    listAdapter.renewItem(lists);
                    Log.d(TAG, lists.size()+" ");
                }
                pbar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pbar.dismiss();
            }
        });

    }

    public void addToRecyclerView(String value, String id) {
        lists.add(new MyList(value,id));
        listAdapter.renewItem(lists);
        itemEt.setText("");
        Log.d(TAG,lists.toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
//        SharedPreferences.Editor editor =  getSharedPreferences(SHARED,MODE_PRIVATE).edit();
//        editor.putString("list",lists.toString());
//        editor.apply();




    }

    @Override
    protected void onStart() {
        super.onStart();
         SharedPreferences s = getSharedPreferences(SHARED,MODE_PRIVATE);
         String myList = s.getString("list","-1");
         //left------------------------------------------------------------------------
    }

    public void editView( String text) {
        itemEt.setText(text);

    }


    public void removeItem(int pos) {
        String id = lists.get(pos).getId();
        databaseReference.child(id).removeValue();
        lists.remove(pos);

    }




}