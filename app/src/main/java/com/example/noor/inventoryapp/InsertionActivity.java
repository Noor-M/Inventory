package com.example.noor.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noor.inventoryapp.data.InventoryContract;

public class InsertionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static Uri currentProductUri;
    public final String[] projection = {
            InventoryContract.InventoryEntry._ID,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER,
    };
    EditText productNameEditText;
    EditText productPriceEditText;
    EditText productQuantityEditText;
    EditText supplierNameEditText;
    EditText supplierPhoneNumberEditText;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertion);
        productNameEditText = findViewById(R.id.product_name_et);
        productPriceEditText = findViewById(R.id.product_price_et);
        productQuantityEditText = findViewById(R.id.product_quantity_et);
        supplierNameEditText = findViewById(R.id.supplier_name_et);
        supplierPhoneNumberEditText = findViewById(R.id.supplier_phone_number_et);


        saveBtn = findViewById(R.id.save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });

        Intent intent = getIntent();
        currentProductUri = intent.getData();

        if (currentProductUri != null) {

            getSupportLoaderManager().initLoader(0, null, this);
        }


    }

    private void saveProduct() {
        String nameStr = productNameEditText.getText().toString().trim();
        String priceStr = productPriceEditText.getText().toString();
        String quantityStr = productQuantityEditText.getText().toString();
        String supplierNameStr = supplierNameEditText.getText().toString().trim();
        String supplierPhoneNumberStr = supplierPhoneNumberEditText.getText().toString().trim();

        if (TextUtils.isEmpty(nameStr) || TextUtils.isEmpty(priceStr)
                || TextUtils.isEmpty(quantityStr) || TextUtils.isEmpty(supplierNameStr)
                || TextUtils.isEmpty(supplierPhoneNumberStr)) {

            Toast.makeText(this, R.string.fill_out, Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();

        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, nameStr);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE, priceStr);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, quantityStr);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierNameStr);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, supplierPhoneNumberStr);

        if (currentProductUri == null) {

            Uri insertedRow = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);

            if (insertedRow == null) {
                Toast.makeText(this, R.string.fail, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.done, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        } else {
            int rowUpdated = getContentResolver().update(currentProductUri, values, null, null);

            if (rowUpdated == 0) {
                Toast.makeText(this, R.string.fail, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.done, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            }

        }


    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this,
                currentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }

        if (data.moveToFirst()) {

            int i_COLUMN_PRODUCT_NAME = 1;
            int i_COLUMN_PRODUCT_PRICE = 2;
            int i_COLUMN_PRODUCT_QUANTITY = 3;
            int i_COLUMN_PRODUCT_SUPPLIER_NAME = 4;
            int i_COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER = 5;

            String name = data.getString(i_COLUMN_PRODUCT_NAME);
            int quantity = data.getInt(i_COLUMN_PRODUCT_QUANTITY);
            float price = data.getFloat(i_COLUMN_PRODUCT_PRICE);
            String supplierName = data.getString(i_COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneNumber = data.getInt(i_COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);


            productNameEditText.setText(name);
            productPriceEditText.setText(String.valueOf(price));
            productQuantityEditText.setText(String.valueOf(quantity));
            supplierNameEditText.setText(supplierName);
            supplierPhoneNumberEditText.setText(String.valueOf(supplierPhoneNumber));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        productNameEditText.setText("");
        productPriceEditText.setText(String.valueOf(""));
        productQuantityEditText.setText(String.valueOf(""));
        supplierNameEditText.setText("");
        supplierPhoneNumberEditText.setText(String.valueOf(""));
    }
}
