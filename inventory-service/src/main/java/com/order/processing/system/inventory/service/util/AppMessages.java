package com.order.processing.system.inventory.service.util;

public class AppMessages {
    /////////////////////////////////////ERROR MESSAGES///////////////////////////////////
    /////////////////////////////////////ERROR MESSAGES///////////////////////////////////
    public static final String NULL_PRODUCT_NAME = "Oops! Product name cannot be null, please check.";
    public static final String EMPTY_PRODUCT_NAME = "Oops! Product name cannot be empty, please check.";
    public static final String MIN_PRODUCT_QUANTITY_EXCEEDED_REQUEST = "Oops! Product stock quantity cannot be less than 1, please check.";
    public static final String MIN_PRODUCT_QUANTITY_EXCEEDED = "Oops! Product stock quantity cannot be less than 0, please check.";
    public static final String MIN_PRODUCT_UNIT_PRICE_EXCEEDED = "Oops! Product unit price cannot be less than 0, please check.";
    public static final String NULL_PRODUCT_QUANTITY = "Oops! Product stock quantity cannot be null, please check.";
    public static final String NULL_PRODUCT_UNIT_PRICE = "Oops! Product unit price cannot be null, please check.";
    public static final String NULL_PRODUCT_STOCK_AVAILABLE = "Oops! Product stock available cannot be null, please check.";
    public static final String NULL_PRODUCT_UUID = "Oops! Product uuid cannot be null, please check.";
    public static final String EMPTY_PRODUCT_UUID = "Oops! Product uuid cannot be empty, please check.";
    public static final String PRODUCT_ALREADY_EXISTS = "Oops! A product with this name already exists, kindly use another name or update the stock availability.";
    public static final String NO_PRODUCT_FOUND_WITH_ID = "Oops! No product found with the uuid provided, please check.";
    public static final String NO_PRODUCT_FOUND = "Oops! No product found in stock.";

    /////////////////////////////////////SUCCESS MESSAGES///////////////////////////////////
    /////////////////////////////////////SUCCESS MESSAGES///////////////////////////////////
    public static final String PRODUCT_ADDED_SUCCESSFULLY = "Awesome! Product has been created successfully.";
    public static final String PRODUCT_RETRIEVED_SUCCESSFULLY = "Awesome! Product(s) retrieved successfully.";
    public static final String PRODUCT_UPDATED_SUCCESSFULLY = "Awesome! Your product has been updated successfully.";
    public static final String PRODUCT_AVAILABILITY_SUCCESSFULLY = "Awesome! Your product stock availability has been updated successfully.";
    public static final String PRODUCT_DELETED_SUCCESSFULLY = "Awesome! Your product has been deleted successfully.";
}
