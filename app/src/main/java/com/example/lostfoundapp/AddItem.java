package com.example.lostfoundapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import java.util.Locale;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class AddItem extends AppCompatActivity {
    private EditText textName, textPhone, textDescription, textDate, textLocation;
    private RadioGroup radioGroup;
    private RadioButton radioLost, radioFound;
    private Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        initializeViews();
        setupListeners();
    }
    private void initializeViews() {
        textName = findViewById(R.id.textName);
        textPhone = findViewById(R.id.textPhone);
        textDescription = findViewById(R.id.textDescription);
        textDate = findViewById(R.id.textDate);
        textLocation = findViewById(R.id.textLocation);
        radioGroup = findViewById(R.id.radioGroup);
        radioLost = findViewById(R.id.radioLost);
        radioFound = findViewById(R.id.radioFound);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }
    private void setupListeners() {
        textDate.setOnClickListener(v -> showDatePickerDialog());
        btnSave.setOnClickListener(v -> saveItem());
        btnCancel.setOnClickListener(v -> finish());
    }
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String dateString = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    textDate.setText(dateString);
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void saveItem() {
        String name = textName.getText().toString();
        String phone = textPhone.getText().toString();
        String description = textDescription.getText().toString();
        String date = textDate.getText().toString();
        String location = textLocation.getText().toString();
        boolean isLost = radioLost.isChecked();

        if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            Item newItem = new Item(name, phone, description, date, location, isLost);
            long result = dbHelper.addItem(newItem);

            if (result != -1) {
                Toast.makeText(this, "Item saved successfully!", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            } else {
                Toast.makeText(this, "Failed to save item", Toast.LENGTH_SHORT).show();
            }
        }
    }
}