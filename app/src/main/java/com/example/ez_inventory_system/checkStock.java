package com.example.ez_inventory_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


public class checkStock extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.ez_inventory_system.EXTRA_TEXT";
    ListView listView;
    ArrayList<item> list;
    item_list_adapter adapter = null;
    public DBHelper DBHelper;
    int id_To_Update = 0;
    String category;
    String categories1;
    Button backBtn;

    public static int oldQuantity;
    public static int newQuantity;
    public static int transaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_stock);

        Intent intent = getIntent();
        categories1 = intent.getStringExtra(categories.EXTRA_TEXT);

        DBHelper = new DBHelper(checkStock.this, "inventory.db", null, 1);
        DBHelper.queryData("CREATE TABLE IF NOT EXISTS item_table(item_id integer primary key, item_name text,item_quantity integer, item_category text,item_price double,item_image blob)");

        listView = (ListView) findViewById(R.id.listView1);
        list = new ArrayList<>();

        adapter = new item_list_adapter(this, R.layout.item_stock, list);
        listView.setAdapter(adapter);

        // get all data from sqlite
        Cursor cursor = DBHelper.getData("SELECT * FROM item_table WHERE item_category = '"+categories1+"'");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String quantity = cursor.getString(2);
            String price = cursor.getString(4);
            byte[] image = cursor.getBlob(5);
            list.add(new item(name, price, image, id,quantity));
        }
        adapter.notifyDataSetChanged();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {"Update", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(checkStock.this);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            // update
                            DBHelper = new DBHelper(checkStock.this, "inventory.db", null, 1);
                            DBHelper.queryData("CREATE TABLE IF NOT EXISTS item_table(item_id integer primary key, item_name text,item_quantity integer, item_category text,item_price double,item_image blob)");
                            Cursor c = DBHelper.getData("SELECT item_id FROM item_table WHERE item_category = '"+categories1+"'");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            // show dialog update at here
                            showDialogUpdate(checkStock.this, arrID.get(position));

                        } else {
                            // delete
                            Cursor c = DBHelper.getData("SELECT item_id FROM item_table WHERE item_category = '"+categories1+"'");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddItem();
            }
        });

        backBtn= (Button) findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openCategory();
            }
        });
    }

    ImageView imageViewItem;
    private void showDialogUpdate(Activity activity, final int position){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.activity_edit_item);
        dialog.setTitle("Update");

        imageViewItem  = (ImageView) dialog.findViewById(R.id.imageViewItem);
        EditText name = (EditText) dialog.findViewById(R.id.edtName);
        EditText price = (EditText) dialog.findViewById(R.id.edtPrice);
        EditText quantity = (EditText) dialog.findViewById(R.id.edtQuantity);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);
        Button backBtn = (Button) dialog.findViewById(R.id.back);

        if(position>0){
            //means this is the view part not the add contact part.
            Cursor rs = DBHelper.getData1(position);
            id_To_Update = position;
            rs.moveToFirst();

            String nam = rs.getString(rs.getColumnIndex(com.example.ez_inventory_system.DBHelper.COLUMN_ITEM_NAME));
            String pric = rs.getString(rs.getColumnIndex(com.example.ez_inventory_system.DBHelper.COLUMN_ITEM_PRICE));
            String quan = rs.getString(rs.getColumnIndex(com.example.ez_inventory_system.DBHelper.COLUMN_ITEM_QUANTITY));
            category = rs.getString(rs.getColumnIndex(com.example.ez_inventory_system.DBHelper.COLUMN_ITEM_CATEGORY));
            byte[] image = rs.getBlob(5);

            oldQuantity = Integer.parseInt(quan);

            name.setText(nam);
            price.setText(pric);
            quantity.setText(quan);

           Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
           imageViewItem  = (ImageView) dialog.findViewById(R.id.imageViewItem);;
           imageViewItem.setImageBitmap((bmp));
        }

        // set width for dialog
        int width = (int) ( activity.getResources().getDisplayMetrics().widthPixels );
        // set height for dialog
        int height = (int) ( activity.getResources().getDisplayMetrics().heightPixels );
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        imageViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        checkStock.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){
                openCheckStock();
            }

        });

        ImageView finalImageViewItem = imageViewItem;
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DBHelper.updateData(
                            name.getText().toString().trim(),
                            price.getText().toString().trim(),
                            imageViewToByte(finalImageViewItem),
                            quantity.getText().toString().trim(),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update successfully!!!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
                updateItemList();

                DBHelper = new DBHelper(checkStock.this, "inventory.db", null, 1);
                DBHelper.queryData("CREATE TABLE IF NOT EXISTS transactions_table(transactions_id integer primary key autoincrement, item_id integer,item_name text,item_quantity integer,transactions integer,item_image blob)");
                newQuantity = Integer.parseInt(quantity.getText().toString());

                if(newQuantity<oldQuantity){
                    transaction = newQuantity - oldQuantity;
                    String transaction2 = String.valueOf(transaction);
                    DBHelper.insertTransaction(
                            position,
                            name.getText().toString().trim(),
                            quantity.getText().toString().trim(),
                            category.trim(),
                            transaction2.trim(),
                            imageViewToByte(finalImageViewItem));
                }
                else if(newQuantity>oldQuantity) {
                    transaction = newQuantity - oldQuantity;
                    String transaction2 = String.valueOf(transaction);
                    DBHelper.insertTransaction(
                            position,
                            name.getText().toString().trim(),
                            quantity.getText().toString().trim(),
                            category.trim(),
                            transaction2.trim(),
                            imageViewToByte(finalImageViewItem));
                }
            }
        });
    }

    private void showDialogDelete(final int idItem){
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(checkStock.this);

        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Are you sure you want to this delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Cursor rs = DBHelper.getData1(idItem);
                    id_To_Update = idItem;
                    rs.moveToFirst();
                    String nam = rs.getString(rs.getColumnIndex(com.example.ez_inventory_system.DBHelper.COLUMN_ITEM_NAME));
                    String quan = rs.getString(rs.getColumnIndex(com.example.ez_inventory_system.DBHelper.COLUMN_ITEM_QUANTITY));
                    category = rs.getString(rs.getColumnIndex(com.example.ez_inventory_system.DBHelper.COLUMN_ITEM_CATEGORY));
                    byte[] image = rs.getBlob(5);
                    oldQuantity = Integer.parseInt(quan);
                    transaction=-oldQuantity;
                    String transaction2 = String.valueOf(transaction);
                    quan="0";
                    DBHelper = new DBHelper(checkStock.this, "inventory.db", null, 1);
                    DBHelper.queryData("CREATE TABLE IF NOT EXISTS transactions_table(transactions_id integer primary key autoincrement, item_id integer,item_name text,item_quantity integer,transactions integer,item_image blob)");
                    DBHelper.insertTransaction(
                            idItem,
                            nam.trim(),
                            quan.trim(),
                            category.trim(),
                            transaction2.trim(),
                            image);

                    DBHelper.deleteData(idItem);

                    Toast.makeText(getApplicationContext(), "Delete successfully!!!",Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateItemList();
            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void updateItemList(){
        // get all data from sqlite
        DBHelper = new DBHelper(checkStock.this, "inventory.db", null, 1);
        DBHelper.queryData("CREATE TABLE IF NOT EXISTS item_table(item_id integer primary key, item_name text,item_quantity integer, item_category text,item_price double,item_image blob)");
        Cursor cursor = DBHelper.getData("SELECT * FROM item_table WHERE item_category = '"+categories1+"'");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String quantity = cursor.getString(2);
            String price = cursor.getString(4);
            byte[] image = cursor.getBlob(5);

            list.add(new item(name, price, image, id,quantity));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 888){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 888 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewItem.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void openAddItem(){
        Intent intent = new Intent(this, addItem.class);
        String text = categories1;
        intent.putExtra(EXTRA_TEXT, text);
        startActivity(intent);
    }

    public void openCheckStock(){
        Intent intent = new Intent(this, checkStock.class);
        String text = category;
        intent.putExtra(EXTRA_TEXT, text);
        startActivity(intent);
        finish();
    }

    public void openCategory(){
        Intent intent = new Intent(this, categories.class);
        startActivity(intent);
    }

}
