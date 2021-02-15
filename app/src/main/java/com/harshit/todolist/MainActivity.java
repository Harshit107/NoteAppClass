package com.harshit.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Main";
    RecyclerView recyclerView;
    ArrayList<MyList> lists = new ArrayList<>();
    ListAdapter listAdapter;
    EditText itemEt;
    Button itemBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycle);
        itemBt = findViewById(R.id.addBt);
        itemEt = findViewById(R.id.addEt);
        lists.add(new MyList("Hello"));


        listAdapter = new ListAdapter(getApplicationContext(), lists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);

        itemBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = itemEt.getText().toString();
                if(value.isEmpty()){
                    itemEt.setError("Field can't be empty");
                    itemEt.requestFocus();
                    return;
                }
                lists.add(new MyList(value));
                listAdapter.renewItem(lists);
                itemEt.setText("");
                Log.d(TAG,lists.toString());
            }
        });






    }
}