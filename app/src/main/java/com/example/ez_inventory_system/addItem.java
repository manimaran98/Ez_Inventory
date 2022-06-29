package com.example.ez_inventory_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class addItem extends AppCompatActivity {

    public static final String EXTRA_TEXT = "com.example.ez_inventory_system.EXTRA_TEXT";
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "StoreImageActivity";
    private Uri selectedImageUri;
    private DBHelper dbHelper;
    String categories1;
    String categories2;


    Button submitBtn,backBtn,uploadBtn;
    EditText itemName,itemQuantity,itemPrice;
    ImageView itemImage;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Intent intent = getIntent();
        categories2 = intent.getStringExtra(checkStock.EXTRA_TEXT);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        spinner = findViewById(R.id.inputItemCategory);
        ArrayList<String> categoryList = new ArrayList<String>();
        categoryList.add("Stationary");
        categoryList.add("Personal Care");
        categoryList.add("Electronic Appliances");
        categoryList.add("Groceries");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, categoryList);
        spinner.setAdapter(adapter);
        String username = getIntent().getStringExtra("USERNAME");
        submitBtn = findViewById(R.id.submit);
        backBtn = findViewById(R.id.back);
        itemName = findViewById(R.id.inputItemName);
        Spinner spinner = findViewById(R.id.inputItemCategory);
        itemQuantity = findViewById(R.id.inputItemQuantity);
        itemPrice = findViewById(R.id.inputItemPrice);
        itemImage = findViewById(R.id.inputItemImage);
        uploadBtn = findViewById(R.id.upload);
        dbHelper = new DBHelper(addItem.this, "inventory.db", null, 1);

        uploadBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){
                try {
                    openImageChooser();
                    Toast.makeText(addItem.this,"Gallery Open",Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    Toast.makeText(addItem.this,"Error",Toast.LENGTH_SHORT).show();
                }

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){
                try {

                    String itemName1 = itemName.getText().toString();
                    int itemQuantity1 = Integer.parseInt(itemQuantity.getText().toString());
                    String itemCategory1 = spinner.getSelectedItem().toString();
                    double itemPrice1 = Integer.parseInt(itemPrice.getText().toString());

                    if (itemName1.equals("") || itemQuantity1 ==0 || itemCategory1.equals("") || itemPrice1==0)
                    {
                        Toast.makeText(addItem.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                        dbHelper.close();
                    }
                    else {
                        InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
                        byte[] inputData = Utils.getBytes(iStream);
                        dbHelper.insertItem(itemName1, itemQuantity1, itemCategory1, itemPrice1, inputData);

                        dbHelper = new DBHelper(addItem.this, "inventory.db", null, 1);
                        dbHelper.queryData("CREATE TABLE IF NOT EXISTS transactions_table(transactions_id integer primary key autoincrement, item_id integer,item_name text,item_quantity integer,transactions integer,item_image blob)");

                        int id = 0;
                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                        final String MY_QUERY = "SELECT MAX(item_id) FROM item_table";
                        Cursor mCursor = db.rawQuery(MY_QUERY, null);
                        try {
                            if (mCursor.getCount() > 0) {
                                mCursor.moveToFirst();
                                id = mCursor.getInt(0);;
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        } finally {
                            db.close();
                        }

                        dbHelper.insertTransaction(id,itemName1, String.valueOf(itemQuantity1),itemCategory1,String.valueOf(itemQuantity1),inputData);

                        dbHelper.close();
                        categories1=itemCategory1;
                        openCheckStock();
                    }
                } catch (IOException ioe) {
                    Log.e(TAG, "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
                    dbHelper.close();
                }
            }

        });

        backBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){

                openCheckStock2();
            }

        });

    }

    // Choose an image from Gallery
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    itemImage.setImageURI(selectedImageUri);
                }
            }
        }
    }

    public void openCheckStock(){
        Intent intent = new Intent(this, checkStock.class);
        String text = categories1;
        intent.putExtra(EXTRA_TEXT, text);
        startActivity(intent);
        finish();
    }

    public void openCheckStock2(){
        Intent intent = new Intent(this, checkStock.class);
        String text = categories2;
        intent.putExtra(EXTRA_TEXT, text);
        startActivity(intent);
        finish();
    }
}