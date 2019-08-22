package com.pubnub.api.endpoints.objects_api.enums;

public enum PNUserFields {
    CUSTOM("custom");

    private final String value;

    PNUserFields(String s) {
        value = s;
    }

    public String toString() {
        return this.value;
    }
}
