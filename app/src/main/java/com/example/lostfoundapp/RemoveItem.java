package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RemoveItem extends AppCompatActivity {

    private TextView textViewItemName, textViewPhone, textViewDescription, textViewDate, textViewLocation, textViewStatus;
    private Button btnDeleteItem;
    private DatabaseHelper dbHelper;
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove_item);
        dbHelper = new DatabaseHelper(this);
        initializeViews();
        setupItemDetails();
        setupDeleteButton();
        Button backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemoveItem.this, ItemList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }
    private void initializeViews() {
        textViewItemName = findViewById(R.id.textViewItemName);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewDate = findViewById(R.id.textViewDate);
        textViewLocation = findViewById(R.id.textViewLocation);
        textViewStatus = findViewById(R.id.textViewStatus);
        btnDeleteItem = findViewById(R.id.btnDeleteItem);
    }
    private void setupItemDetails() {
        itemId = getIntent().getIntExtra("item_id", -1);
        if (itemId != -1) {
            Item item = dbHelper.getItem(itemId);
            if (item != null) {
                textViewItemName.setText(item.getName());
                textViewPhone.setText(item.getPhone());
                textViewDescription.setText(item.getDescription());
                textViewDate.setText(item.getDate());
                textViewLocation.setText(item.getLocation());
                textViewStatus.setText(item.isLost() ? "Lost" : "Found");
            }
        }
    }
    private void setupDeleteButton() {
        btnDeleteItem.setOnClickListener(v -> {
            if (itemId != -1) {
                dbHelper.deleteItem(itemId);
                Toast.makeText(this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
