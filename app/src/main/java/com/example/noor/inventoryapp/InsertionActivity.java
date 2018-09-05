package com.example.noor.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noor.inventoryapp.data.InventoryContract;

public class InsertionActivity extends AppCompatActivity {
    EditText productNameEditText;
    EditText productPriceEditText;
    EditText productQuantityEditText;
    EditText supplierNameEditText;
    EditText supplierPhoneNumberEditText;

    private boolean mProductHasChanged = false;



    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertion);
        productNameEditText =findViewById(R.id.product_name_et);
        productPriceEditText =findViewById(R.id.product_price_et);
        productQuantityEditText = findViewById(R.id.product_quantity_et);
        supplierNameEditText = findViewById(R.id.supplier_name_et);
        supplierPhoneNumberEditText = findViewById( R.id.supplier_phone_number_et);


        productNameEditText.setOnTouchListener(onTouchListener);





    }





}
