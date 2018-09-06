package com.example.noor.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.noor.inventoryapp.data.InventoryContract;
import com.example.noor.inventoryapp.data.InventoryDBHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public final String[] projection = {
            InventoryContract.InventoryEntry._ID,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER,
    };
    InventoryDBHelper helper;
    ListView listView;

    TextView emptyTextView;
    private InventoryCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new InventoryDBHelper(this);


        FloatingActionButton plus = findViewById(R.id.plus);


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InsertionActivity.class);
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.list_view);

        emptyTextView = findViewById(R.id.no_data);
        listView.setEmptyView(emptyTextView);


        cursorAdapter = new InventoryCursorAdapter(this, null);


        getSupportLoaderManager().initLoader(0, null, this);


    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader(this,
                InventoryContract.InventoryEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {


    }


}
