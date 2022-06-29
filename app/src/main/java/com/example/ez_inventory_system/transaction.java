package com.example.ez_inventory_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class transaction extends AppCompatActivity {

    ListView listView;
    ArrayList<item> list;
    transaction_list_adapter adapter = null;
    public DBHelper DBHelper;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        Intent intent = getIntent();
        String categories1 = intent.getStringExtra(categories.EXTRA_TEXT);

        DBHelper = new DBHelper(transaction.this, "inventory.db", null, 1);
        DBHelper.queryData("CREATE TABLE IF NOT EXISTS transactions_table(transactions_id integer primary key autoincrement, item_id integer,item_name text,item_quantity integer,item_category,transactions integer,item_image blob)");


        listView = (ListView) findViewById(R.id.listView1);
        list = new ArrayList<>();

        adapter = new transaction_list_adapter(this, R.layout.item_stock, list);
        listView.setAdapter(adapter);

        // get all data from sqlite
        Cursor cursor = DBHelper.getData("SELECT * FROM transactions_table WHERE item_category = '"+categories1+"'");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(2);
            String quantity = cursor.getString(3);
            String price = cursor.getString(5);
            byte[] image = cursor.getBlob(6);
            list.add(new item(name, price, image, id,quantity));
        }

        backBtn= (Button) findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openCategory();
            }
        });

    }

    public void openCategory(){
        Intent intent = new Intent(this, transaction_categories.class);
        startActivity(intent);
        finish();
    }

}