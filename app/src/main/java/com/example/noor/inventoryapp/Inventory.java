package com.example.noor.inventoryapp;

public class Inventory {

    private String productName;
    private String productPrice;

    private String productQuantity;
    private String supplierName;
    private String supplierPhoneNumber;

    public Inventory(String productName, String productPrice, String productQuantity, String supplierName, String supplierPhoneNumber) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.supplierName = supplierName;
        this.supplierPhoneNumber = supplierPhoneNumber;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierPhoneNumber() {
        return supplierPhoneNumber;
    }
}
