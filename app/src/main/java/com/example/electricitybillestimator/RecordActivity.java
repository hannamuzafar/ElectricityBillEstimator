package com.example.electricitybillestimator;

import android.database.Cursor;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {

    ListView listView;

    DatabaseHelper db;

    ArrayList<String> list;

    ArrayList<String> idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        listView = findViewById(R.id.listView);

        db = new DatabaseHelper(this);

        list = new ArrayList<>();

        idList = new ArrayList<>();

        Cursor cursor =
                db.getAllData();

        while(cursor.moveToNext())
        {
            String recordId =
                    cursor.getString(0);

            String month =
                    cursor.getString(1);

            double finalCost =
                    cursor.getDouble(5);

            idList.add(recordId);

            list.add(
                    month +
                            " - RM " +
                            String.format("%.2f",
                                    finalCost));
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(
                (parent, view, position, id) -> {

                    Intent intent =
                            new Intent(
                                    RecordActivity.this,
                                    DetailActivity.class);

                    intent.putExtra(
                            "recordId",
                            idList.get(position));

                    startActivity(intent);

                });
    }
}