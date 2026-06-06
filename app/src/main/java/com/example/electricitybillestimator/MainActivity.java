package com.example.electricitybillestimator;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spMonth;
    EditText etUnit;

    SeekBar seekRebate;

    TextView tvRebate;
    TextView tvTotal;
    TextView tvFinal;

    Button btnCalculate;
    Button btnSave;
    Button btnRecord;
    Button btnAbout;

    DatabaseHelper db;

    double totalCharge = 0;
    double finalCost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        spMonth = findViewById(R.id.spMonth);

        etUnit = findViewById(R.id.etUnit);

        seekRebate = findViewById(R.id.seekRebate);

        tvRebate = findViewById(R.id.tvRebate);
        tvTotal = findViewById(R.id.tvTotal);
        tvFinal = findViewById(R.id.tvFinal);

        btnCalculate = findViewById(R.id.btnCalculate);
        btnSave = findViewById(R.id.btnSave);

        btnRecord = findViewById(R.id.btnRecord);
        btnAbout = findViewById(R.id.btnAbout);

        String[] months = {
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        months);

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spMonth.setAdapter(adapter);

        seekRebate.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser) {

                        tvRebate.setText(
                                "Rebate : " + progress + "%");
                    }

                    @Override
                    public void onStartTrackingTouch(
                            SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(
                            SeekBar seekBar) {
                    }
                });

        btnCalculate.setOnClickListener(v -> {

            String unitText =
                    etUnit.getText().toString();

            if(unitText.isEmpty())
            {
                etUnit.setError(
                        "Please enter unit");
                return;
            }

            int unit =
                    Integer.parseInt(unitText);

            if(unit < 1 || unit > 1000)
            {
                etUnit.setError(
                        "Unit must be between 1 and 1000");
                return;
            }

            totalCharge =
                    calculateBill(unit);

            int rebate =
                    seekRebate.getProgress();

            finalCost =
                    totalCharge -
                            (totalCharge * rebate / 100.0);

            tvTotal.setText(
                    "Total Charges : RM "
                            + String.format("%.2f",
                            totalCharge));

            tvFinal.setText(
                    "Final Cost : RM "
                            + String.format("%.2f",
                            finalCost));
        });

        btnSave.setOnClickListener(v -> {

            String unitText =
                    etUnit.getText().toString();

            if(unitText.isEmpty())
            {
                Toast.makeText(
                        MainActivity.this,
                        "Calculate first",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            String month =
                    spMonth.getSelectedItem()
                            .toString();

            int unit =
                    Integer.parseInt(unitText);

            int rebate =
                    seekRebate.getProgress();

            boolean status =
                    db.insertData(
                            month,
                            unit,
                            rebate,
                            totalCharge,
                            finalCost);

            if(status)
            {
                Toast.makeText(
                        MainActivity.this,
                        "Data Saved",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(
                        MainActivity.this,
                        "Failed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnRecord.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            RecordActivity.class);

            startActivity(intent);

        });

        btnAbout.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            AboutActivity.class);

            startActivity(intent);

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