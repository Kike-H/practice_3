package com.example.practice_3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Input
    EditText input;

    //List Tasks
    ArrayList<String> tasks;
    ListView list_tasks;
    ArrayAdapter<String> adapter_t;
    SharedPreferences preferences_t;

    //List doing
    ArrayList<String> doings;
    ListView list_doing;
    ArrayAdapter<String> adapter_d;
    SharedPreferences preferences_d;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Input
        input = findViewById(R.id.input);

        //List tasks
        list_tasks = findViewById(R.id.list_tasks);
        tasks = new ArrayList<String>();
        adapter_t = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tasks);
        list_tasks.setAdapter(adapter_t);

        //List tasks
        list_doing = findViewById(R.id.list_doing);
        doings = new ArrayList<String>();
        adapter_d = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, doings);
        list_doing.setAdapter(adapter_d);

        addDoing();
        removeDoing();
        load();

    }

    public void addTask(View view) {
        String new_task = input.getText().toString();
        preferences_t = getSharedPreferences("tasks", Context.MODE_PRIVATE);
        tasks.add(new_task);
        adapter_t.notifyDataSetChanged();

        SharedPreferences.Editor editor = preferences_t.edit();
        editor.putString(new_task, new_task);
        editor.commit();

        Toast.makeText(this, "Task saved", Toast.LENGTH_LONG).show();
    }

    public void addDoing() {
        list_tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                preferences_t = getSharedPreferences("tasks", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences_t.edit();

                String task = tasks.get(i);
                tasks.remove(i);
                adapter_t.notifyDataSetChanged();

                editor.remove(task);
                editor.commit();

                preferences_d = getSharedPreferences("doings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = preferences_d.edit();
                editor1.putString(task, task);
                editor1.commit();

                doings.add(task);
                adapter_d.notifyDataSetChanged();
            }
        });
    }

    public void load() {
        preferences_t = getSharedPreferences("tasks", Context.MODE_PRIVATE);
        Map<String, ?> data = preferences_t.getAll();
        for (Map.Entry<String, ?>element : data.entrySet()) {
            tasks.add(element.getValue().toString());
        }
        adapter_t.notifyDataSetChanged();

        preferences_d = getSharedPreferences("doings", Context.MODE_PRIVATE);
        Map<String, ?> data1 = preferences_d.getAll();
        for (Map.Entry<String, ?>element : data1.entrySet()) {
            doings.add(element.getValue().toString());
        }
        adapter_d.notifyDataSetChanged();
    }

    public void removeDoing() {
        list_doing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pox, long l) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Alert");
                dialog.setMessage("Are you sure that you want to delete this task?");
                dialog.setCancelable(false);

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        preferences_d = getSharedPreferences("doings", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences_d.edit();

                        String doing = doings.get(pox);
                        doings.remove(pox);
                        adapter_d.notifyDataSetChanged();

                        editor.remove(doing);
                        editor.commit();
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });
    }
}