package com.pubnub.api.endpoints.objects_api.enums;

public enum PNMemberFields {
    CUSTOM("custom"),
    USER("user"),
    USER_CUSTOM("user.custom");

    private final String value;

    PNMemberFields(String s) {
        value = s;
    }

    public String toString() {
        return this.value;
    }
}