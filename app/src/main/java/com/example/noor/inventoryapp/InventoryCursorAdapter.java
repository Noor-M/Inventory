package com.example.noor.inventoryapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noor.inventoryapp.data.InventoryContract;

public class InventoryCursorAdapter extends CursorAdapter {
    private static final String TAG = "InventoryCursorAdapter1";

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.inventory_list, viewGroup, false);

    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final TextView productName = view.findViewById(R.id.name_tv);
        TextView productQuantity = view.findViewById(R.id.quantity_tv);
        TextView productPrice = view.findViewById(R.id.price_tv);
        TextView supplier_name = view.findViewById(R.id.supplier_name_tv);
        TextView supplierPhoneNumber = view.findViewById(R.id.supplier_phone_number_tv);

        Button saleBtn = view.findViewById(R.id.sale);
        Button editBtn = view.findViewById(R.id.edit);
        Button showBtn = view.findViewById(R.id.show);


        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE);
        int supplierNameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
        int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);

        int id = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry._ID));
        final String productId = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry._ID));

        final String productNameStr = cursor.getString(nameColumnIndex);
        String priceStr = cursor.getInt(priceColumnIndex) + context.getString(R.string.dollar);
        final int quantityInt = cursor.getInt(quantityColumnIndex);
        String supplierNameStr = cursor.getString(supplierNameColumnIndex);
        int supplierPhoneNumberInt = cursor.getInt(supplierPhoneNumberColumnIndex);

        String quantityStr = String.valueOf(quantityInt);
        String supplierPhoneNumberStr = String.valueOf(supplierPhoneNumberInt);


        final Uri currentProductUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, id);

        Log.d(TAG, " Uri: " + currentProductUri + " Product name: " + productName + " id: " + id);

        productName.setText(productNameStr);
        productPrice.setText(priceStr);
        productQuantity.setText(quantityStr);
        supplier_name.setText(supplierNameStr);
        supplierPhoneNumber.setText(supplierPhoneNumberStr);


        saleBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, productNameStr + " quantity= " + quantityInt);
                ContentResolver resolver = view.getContext().getContentResolver();
                ContentValues values = new ContentValues();
                if (quantityInt > 0) {
                    int qq = quantityInt;
                    Log.d(TAG, "new quantity= " + qq);
                    values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, --qq);
                    resolver.update(
                            currentProductUri,
                            values,
                            null,
                            null
                    );
                    context.getContentResolver().notifyChange(currentProductUri, null);
                } else {
                    Toast.makeText(context, R.string.out_of_stock, Toast.LENGTH_SHORT).show();
                }
            }
        });

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                Uri currentProductUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, Long.parseLong(productId));
                intent.setData(currentProductUri);
                context.startActivity(intent);

            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InsertionActivity.class);
                Uri currentUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, Long.parseLong(productId));
                intent.setData(currentUri);
                context.startActivity(intent);
            }
        });


    }
}
