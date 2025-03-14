package com.order.processing.system.account.service.enums;

import java.util.HashMap;
import java.util.Map;

public enum OnboardingStage {
    STARTED("Started"),
    VERIFIED("Verified");

    public final String label;
    private static final Map<String, OnboardingStage> map = new HashMap<>();

    static {
        for (OnboardingStage e : values()) {
            map.put(e.label, e);
        }
    }

    private OnboardingStage(String label) {
        this.label = label;

    }

    public static OnboardingStage valueOfName(String label) {
        return map.get(label);
    }
}
