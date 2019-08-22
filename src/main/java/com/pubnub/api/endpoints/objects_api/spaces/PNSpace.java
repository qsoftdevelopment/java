package com.pubnub.api.endpoints.objects_api.spaces;

import com.pubnub.api.endpoints.objects_api.PNObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class PNSpace extends PNObject {

    private String name;
    private String description;

    public PNSpace(String id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public PNSpace setCustom(Object custom) {
        super.setCustom(custom);
        return this;
    }
}
