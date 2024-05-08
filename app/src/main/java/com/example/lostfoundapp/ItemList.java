package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ItemList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);

        recyclerView = findViewById(R.id.recyclerViewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DatabaseHelper(this);

        adapter = new ItemAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        Button backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ItemList.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateItemList();
    }
    private void updateItemList() {
        List<Item> items = dbHelper.getAllItems();
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }
}