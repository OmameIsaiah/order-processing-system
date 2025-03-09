package com.order.processing.system.order.service.enums;

import java.util.HashMap;
import java.util.Map;

public enum OrderStatus {
    PENDING("Pending"),
    SUCCESS("Success"),
    FAILED("Failed"),
    CANCELLED("Cancelled");
    public final String label;
    private static final Map<String, OrderStatus> map = new HashMap<>();

    static {
        for (OrderStatus e : values()) {
            map.put(e.label, e);
        }
    }

    private OrderStatus(String label) {
        this.label = label;

    }

    public static OrderStatus valueOfName(String label) {
        return map.get(label);
    }
}
