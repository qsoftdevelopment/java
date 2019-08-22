package com.pubnub.api.endpoints.objects_api.memberships.manage;

public abstract class PropertyEnvelope<T> {

    protected String id;
    protected Object custom;

    public T custom(Object custom) {
        this.custom = custom;
        return (T) this;
    }

    public Object custom() {
        return custom;
    }

}
