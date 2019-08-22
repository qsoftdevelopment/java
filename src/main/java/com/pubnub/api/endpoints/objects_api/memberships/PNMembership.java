package com.pubnub.api.endpoints.objects_api.memberships;

import com.pubnub.api.endpoints.objects_api.PNObject;
import com.pubnub.api.endpoints.objects_api.spaces.PNSpace;
import lombok.Getter;

@Getter
public class PNMembership extends PNObject {

    private PNSpace space;

    private PNMembership() {
    }
}
