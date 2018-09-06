package com.example.noor.inventoryapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noor.inventoryapp.data.InventoryContract;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Uri mCurrentProductUri;
    TextView name;
    TextView price;
    TextView quantity;
    TextView supplierName;
    TextView supplierPhoneNumber;

    Button subBtn;
    Button addBtn;
    Button callBtn;
    Button deleteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        name = findViewById(R.id.name_vtv);
        price = findViewById(R.id.price_vtv);
        quantity = findViewById(R.id.quantity_vtv);
        supplierName = findViewById(R.id.supplier_name_vtv);
        supplierPhoneNumber = findViewById(R.id.supplier_phone_number_vtv);

        subBtn = findViewById(R.id.sub);
        addBtn = findViewById(R.id.add);
        deleteBtn = findViewById(R.id.delete);
        callBtn = findViewById(R.id.call);


        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();
        if (mCurrentProductUri == null) {
            invalidateOptionsMenu();
        } else {
            getSupportLoaderManager().initLoader(0, null, this);
        }


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER
        };
        return new CursorLoader(this,
                mCurrentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        {
            if (data == null || data.getCount() < 1) {
                return;
            }
            if (data.moveToFirst()) {

                final int idColumnIndex = data.getColumnIndex(InventoryContract.InventoryEntry._ID);
                int nameColumnIndex = data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
                int priceColumnIndex = data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE);
                int quantityColumnIndex = data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY);
                int supplierNameColumnIndex = data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
                int supplierPhoneColumnIndex = data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);

                String currentName = data.getString(nameColumnIndex);
                final int currentPrice = data.getInt(priceColumnIndex);
                final int currentQuantity = data.getInt(quantityColumnIndex);
                String currentSupplierName = data.getString(supplierNameColumnIndex);
                final int currentSupplierPhoneNumber = data.getInt(supplierPhoneColumnIndex);
                String currentPriceStr = Integer.toString(currentPrice);
                String currentQuantityStr = Integer.toString(currentQuantity);
                final String currentSupplierPhoneNumberStr = Integer.toString(currentSupplierPhoneNumber);


                name.setText(currentName);
                price.setText(currentPriceStr);
                quantity.setText(currentQuantityStr);
                supplierName.setText(currentSupplierName);
                supplierPhoneNumber.setText(currentSupplierPhoneNumberStr);


                subBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        decreaseCount(idColumnIndex, currentQuantity);
                    }
                });

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        increaseCount(idColumnIndex, currentQuantity);
                    }
                });

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteConfirmation();
                    }
                });

                callBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + currentSupplierPhoneNumberStr));
                        startActivity(intent);
                    }
                });

            }
        }

    }

    public void decreaseCount(int productID, int productQuantity) {
        productQuantity = productQuantity - 1;
        if (productQuantity >= 0) {
            updateProduct(productQuantity);
            Toast.makeText(this, R.string.quantity_changed, Toast.LENGTH_SHORT).show();

            Log.d("Log msg", " done productID: " + productID);
        } else {
            Toast.makeText(this, R.string.out_of_stock, Toast.LENGTH_SHORT).show();
        }
    }

    public void increaseCount(int productID, int productQuantity) {
        productQuantity = productQuantity + 1;
        if (productQuantity >= 0) {
            updateProduct(productQuantity);
            Toast.makeText(this, R.string.added, Toast.LENGTH_SHORT).show();

            Log.d("Log msg", " done productID: " + productID + productQuantity);
        }
    }

    private void updateProduct(int productQuantity) {

        if (mCurrentProductUri == null) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);

        if (mCurrentProductUri == null) {
            Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, R.string.fail,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.done,
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, R.string.fail,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.done,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void DeleteConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_text);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct() {
        if (mCurrentProductUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, R.string.fail,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.done,
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }


    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }


}
