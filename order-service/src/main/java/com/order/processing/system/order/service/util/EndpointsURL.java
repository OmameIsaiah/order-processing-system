package com.order.processing.system.order.service.util;

public class EndpointsURL {
    public static final String ORDER_BASE_URL = "/api/v1";
    public static final String CREATE_ORDER = "/order";
    public static final String CONFIRM_ORDER = "/order/confirm";
    public static final String CANCEL_ORDER = "/order/cancel";
    public static final String VIEW_ORDER_BY_ID = "/order";
    public static final String VIEW_USERS_ORDERS = "/order/by-user";
    public static final String VIEW_ALL_ORDERS = "/orders";
    public static final String DELETE_ORDER = "/order";

    public static final String PRODUCT_BASE_URL = "/api/v1";
    public static final String ADD_PRODUCT = "/product";
    public static final String UPDATE_PRODUCT = "/product";
    public static final String UPDATE_STOCK_AVAILABILITY = "/product/update-stock";
    public static final String CHECK_PRODUCT_AVAILABILITY = "/product/check-stock";
    public static final String VIEW_PRODUCT_BY_ID = "/product";
    public static final String VIEW_ALL_PRODUCTS = "/products";
    public static final String DELETE_PRODUCT = "/product";
}
