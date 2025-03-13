package com.order.processing.system.order.service.util;

public class AppMessages {
    /////////////////////////////////////ERROR MESSAGES///////////////////////////////////
    /////////////////////////////////////ERROR MESSAGES///////////////////////////////////
    public static final String NULL_PRODUCT_NAME = "Oops! Product name cannot be null, please check.";
    public static final String EMPTY_PRODUCT_NAME = "Oops! Product name cannot be empty, please check.";
    public static final String MIN_PRODUCT_QUANTITY_EXCEEDED = "Oops! Quantity of product ordered cannot be less than 1, please check.";
    public static final String NULL_PRODUCT_QUANTITY = "Oops! Quantity of product ordered cannot be null, please check.";
    public static final String NULL_PRODUCT_UUID = "Oops! Product uuid cannot be null, please check.";
    public static final String EMPTY_PRODUCT_UUID = "Oops! Product uuid cannot be empty, please check.";
    public static final String NO_ORDER_FOUND_WITH_ID = "Oops! No order found with the uuid provided, please check.";
    public static final String NO_ORDER_FOUND = "Oops! No order(s) found.";
    public static final String NULL_ORDER_UUID = "Oops! Order uuid cannot be null, please check.";
    public static final String EMPTY_ORDER_UUID = "Oops! Order uuid cannot be empty, please check.";
    public static final String PRODUCT_STOCK_UNAVAILABLE = "Oops! No stock available at the moment, check back later.";

    public static final String MIN_PRODUCT_QUANTITY_EXCEEDED_REQUEST = "Oops! Product stock quantity cannot be less than 1, please check.";
    public static final String MIN_PRODUCT_UNIT_PRICE_EXCEEDED = "Oops! Product unit price cannot be less than 0, please check.";
    public static final String NULL_PRODUCT_UNIT_PRICE = "Oops! Product unit price cannot be null, please check.";
    public static final String NULL_PRODUCT_STOCK_AVAILABLE = "Oops! Product stock available cannot be null, please check.";

    /////////////////////////////////////SUCCESS MESSAGES///////////////////////////////////
    /////////////////////////////////////SUCCESS MESSAGES///////////////////////////////////
    public static final String ORDER_CREATED_SUCCESSFULLY = "Awesome! Order has been created successfully. Kindly confirm the order to checkout.";
    public static final String ORDER_RETRIEVED_SUCCESSFULLY = "Awesome! Order(s) retrieved successfully.";
    public static final String ORDER_CONFIRMED_SUCCESSFULLY = "Awesome! Your order has been confirmed successfully.";
    public static final String ORDER_CANCELLED_SUCCESSFULLY = "Awesome! Your order has been cancelled successfully.";
    public static final String ORDER_DELETED_SUCCESSFULLY = "Awesome! Your order has been deleted successfully.";
    public static final String PRODUCT_STOCK_AVAILABLE = "Awesome! Product available in stock.";
}
