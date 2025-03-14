package com.order.processing.system.account.service.enums;

import java.util.HashMap;
import java.util.Map;

public enum Permissions {
    CAN_VIEW_PRODUCTS("CAN_VIEW_PRODUCTS"),
    CAN_SEARCH_PRODUCTS("CAN_SEARCH_PRODUCTS"),
    CAN_ADD_PRODUCT("CAN_ADD_PRODUCT"),
    CAN_UPDATE_PRODUCT("CAN_UPDATE_PRODUCT"),
    CAN_DELETE_PRODUCT("CAN_DELETE_PRODUCT"),
    CAN_ORDER_PRODUCT("CAN_ORDER_PRODUCT"),
    CAN_VIEW_ORDERS("CAN_VIEW_ORDERS"),
    CAN_CANCEL_ORDER("CAN_CANCEL_ORDER"),
    CAN_VIEW_USERS("CAN_VIEW_USERS"),
    CAN_DELETE_USER("CAN_DELETE_USER"),
    CAN_ADD_ROLE("CAN_ADD_ROLE"),
    CAN_UPDATE_ROLE("CAN_UPDATE_ROLE"),
    CAN_DELETE_ROLE("CAN_DELETE_ROLE");

    public final String label;
    private static final Map<String, Permissions> map = new HashMap<>();

    static {
        for (Permissions e : values()) {
            map.put(e.label, e);
        }
    }

    private Permissions(String label) {
        this.label = label;

    }

    public static Permissions valueOfName(String label) {
        return map.get(label);
    }
}
