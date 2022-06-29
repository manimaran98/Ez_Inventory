package com.example.ez_inventory_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class categories extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.ez_inventory_system.EXTRA_TEXT";
    Button stationarybtn, personalCareBtn, electronicAppBtn, groceriesbtn,menuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_categories);
        stationarybtn= (Button) findViewById(R.id.stationarybtn);

        stationarybtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openAddItem1();
            }
        });

        personalCareBtn= (Button) findViewById(R.id.personalCareBtn);

        personalCareBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openAddItem2();
            }
        });

        electronicAppBtn= (Button) findViewById(R.id.electronicAppBtn);

        electronicAppBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openAddItem3();
            }
        });

        groceriesbtn= (Button) findViewById(R.id.groceriesbtn);

        groceriesbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openAddItem4();
            }
        });

        menuBtn= (Button) findViewById(R.id.menuBtn);

        menuBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openAddItem5();
            }
        });

    }
    public void openAddItem1 () {
        String text = "Stationary";
        Intent intent = new Intent(this, checkStock.class);
        intent.putExtra(EXTRA_TEXT, text);
        startActivity(intent);
    }
    public void openAddItem2 () {
        String text = "Personal Care";
        Intent intent = new Intent(this, checkStock.class);
        intent.putExtra(EXTRA_TEXT, text);
        startActivity(intent);
    }
    public void openAddItem3 () {
        Intent intent = new Intent(this, checkStock.class);
        String text = "Electronic Appliances";
        intent.putExtra(EXTRA_TEXT, text);
        startActivity(intent);
    }
    public void openAddItem4 () {
        Intent intent = new Intent(this, checkStock.class);
        String text = "Groceries";
        intent.putExtra(EXTRA_TEXT, text);
        startActivity(intent);
    }
    public void openAddItem5 () {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}