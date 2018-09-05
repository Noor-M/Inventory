package com.example.noor.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.noor.inventoryapp.data.InventoryContract;
import com.example.noor.inventoryapp.data.InventoryDBHelper;

public class InventoryProvider extends ContentProvider {
    InventoryDBHelper helper;
    public static final UriMatcher sMatcher= new UriMatcher(UriMatcher.NO_MATCH);
    public static final int INVENTORY=100;
    public static final int INVENTORY_ID = 200;
    static {
        sMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,InventoryContract.PATH,INVENTORY);
       sMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,InventoryContract.PATH+"/#",INVENTORY_ID);
    }





    @Override
    public boolean onCreate() {

            helper = new InventoryDBHelper(getContext());

        return true;
    }
    SQLiteDatabase database;
    Cursor cursor;

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        int match = sMatcher.match(uri);
        database= helper.getReadableDatabase();


        switch (match){
            case INVENTORY:
                //all table
                if (s==null) {
                    s="";
                    cursor = database.query(InventoryContract.InventoryEntry.TABLE_NAME, strings, s, strings1, null, null, s1);
                }
                break;
            case INVENTORY_ID:
                //single row
                if(s==null){
                    s="";
                }
                s +=InventoryContract.InventoryEntry._ID;
                s=InventoryContract.InventoryEntry._ID+"=?";
                strings1=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor =database.query(InventoryContract.InventoryEntry.TABLE_NAME,strings,s,strings1,null,null,s1);
                break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return InventoryContract.InventoryEntry.CONTENT_LIST_TYPE;
            case INVENTORY_ID:
                return InventoryContract.InventoryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI" + uri + " with match " + match);
        }    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final int match = sMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion isn't valid for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase database = helper.getWritableDatabase();
        int rowsDeleted;

        final int match = sMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                rowsDeleted = database.delete(InventoryContract.InventoryEntry.TABLE_NAME, s, strings);
                break;
            case INVENTORY_ID:
                s = InventoryContract.InventoryEntry._ID + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(InventoryContract.InventoryEntry.TABLE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("we can't delete it because " + uri + "is not valid");
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {

            final int match = sMatcher.match(uri);
            switch (match) {
                case INVENTORY:
                    return updateProduct(uri, contentValues, s, strings);
                case INVENTORY_ID:
                    s = InventoryContract.InventoryEntry._ID + "=?";
                    strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                    return updateProduct(uri, contentValues, s, strings);
                default:
                    throw new IllegalArgumentException("Update is not supported for " + uri);
            }
    }


    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME)) {
            String nameProduct = values.getAsString(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
            if (nameProduct == null) {
                throw new IllegalArgumentException("Product name requires");
            }
        }
        if (values.containsKey(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE)) {
            Integer priceProduct = values.getAsInteger(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE);
            if (priceProduct != null && priceProduct < 0) {
                throw new
                        IllegalArgumentException("Product price requires valid");
            }
        }

        if (values.containsKey(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY)) {
            Integer quantityProduct = values.getAsInteger(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantityProduct != null && quantityProduct < 0) {
                throw new
                        IllegalArgumentException("Product quantity requires valid");
            }
        }
        if (values.containsKey(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME)) {
            Integer supplierName = values.getAsInteger(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            if (supplierName == null || supplierName<0) {
                throw new IllegalArgumentException("Supplier Name requires valid");
            }
        }

        if (values.containsKey(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER)) {
            Integer supplierPhone = values.getAsInteger(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);
            if (supplierPhone != null && supplierPhone < 0) {
                throw new
                        IllegalArgumentException("Supplier Phone requires valid");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = helper.getWritableDatabase();

        int rowsUpdated = database.update(InventoryContract.InventoryEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    private Uri insertProduct(Uri uri, ContentValues values) {
        String nameProduct = values.getAsString(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        if (nameProduct == null) {
            throw new IllegalArgumentException("Product name requires");
        }

        Integer priceProduct = values.getAsInteger(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE);
        if (priceProduct != null && priceProduct < 0) {
            throw new IllegalArgumentException("Product price requires valid");
        }



        Integer supplierName = values.getAsInteger(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
        if (supplierName == null || supplierName<0) {
            throw new IllegalArgumentException("Pet requires valid gender");
        }

        Integer supplierPhone = values.getAsInteger(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);
        if (supplierPhone != null && supplierPhone < 0) {
            throw new IllegalArgumentException("Supplier Phone requires valid");
        }
        Integer quantityProduct = values.getAsInteger(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY);
        if (quantityProduct != null && quantityProduct < 0) {
            throw new IllegalArgumentException("Product quantity requires valid");
        }

        SQLiteDatabase database = helper.getWritableDatabase();
        long id = database.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.v("message:", "Failed to insert new row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }
}
