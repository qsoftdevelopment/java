package com.pubnub.api.endpoints.objects_api.enums;

public enum PNSpaceFields {
    CUSTOM("custom");

    private final String value;

    PNSpaceFields(String s) {
        value = s;
    }

    public String toString() {
        return this.value;
    }
}
