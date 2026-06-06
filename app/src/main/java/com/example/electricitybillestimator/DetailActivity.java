package com.example.electricitybillestimator;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    EditText tvMonth;
    EditText tvUnit;
    EditText tvRebateDetail;
    EditText tvTotalDetail;
    EditText tvFinalDetail;

    Button btnUpdate;
    Button btnDelete;

    DatabaseHelper db;

    String recordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvMonth = findViewById(R.id.tvMonth);
        tvUnit = findViewById(R.id.tvUnit);
        tvRebateDetail = findViewById(R.id.tvRebateDetail);
        tvTotalDetail = findViewById(R.id.tvTotalDetail);
        tvFinalDetail = findViewById(R.id.tvFinalDetail);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        db = new DatabaseHelper(this);

        recordId =
                getIntent().getStringExtra(
                        "recordId");

        Cursor cursor =
                db.getOneData(recordId);

        if(cursor.moveToFirst())
        {
            tvMonth.setText(cursor.getString(1));

            tvUnit.setText(
                    String.valueOf(
                            cursor.getInt(2)));

            tvRebateDetail.setText(
                    String.valueOf(
                            cursor.getInt(3)));

            tvTotalDetail.setText(
                    String.format("%.2f",
                            cursor.getDouble(4)));

            tvFinalDetail.setText(
                    String.format("%.2f",
                            cursor.getDouble(5)));
        }

        btnDelete.setOnClickListener(v -> {

            db.deleteData(recordId);

            Toast.makeText(
                    DetailActivity.this,
                    "Record Deleted",
                    Toast.LENGTH_SHORT).show();

            finish();

        });

        btnUpdate.setOnClickListener(v -> {

            String month =
                    tvMonth.getText().toString();

            int unit =
                    Integer.parseInt(
                            tvUnit.getText().toString());

            int rebate =
                    Integer.parseInt(
                            tvRebateDetail.getText().toString());

            double totalCharge =
                    calculateBill(unit);

            double finalCost =
                    totalCharge -
                            (totalCharge * rebate / 100.0);

            tvTotalDetail.setText(
                    String.format("%.2f",
                            totalCharge));

            tvFinalDetail.setText(
                    String.format("%.2f",
                            finalCost));

            db.updateData(
                    recordId,
                    month,
                    unit,
                    rebate,
                    totalCharge,
                    finalCost);

            Toast.makeText(
                    DetailActivity.this,
                    "Record Updated",
                    Toast.LENGTH_SHORT).show();

        });
    }

    private double calculateBill(int unit)
    {
        double charge = 0;

        if(unit <= 200)
        {
            charge = unit * 0.218;
        }
        else if(unit <= 300)
        {
            charge =
                    (200 * 0.218)
                            +
                            ((unit - 200) * 0.334);
        }
        else if(unit <= 600)
        {
            charge =
                    (200 * 0.218)
                            +
                            (100 * 0.334)
                            +
                            ((unit - 300) * 0.516);
        }
        else
        {
            charge =
                    (200 * 0.218)
                            +
                            (100 * 0.334)
                            +
                            (300 * 0.516)
                            +
                            ((unit - 600) * 0.546);
        }

        return charge;
    }

}