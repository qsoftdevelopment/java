package com.pubnub.api.endpoints.objects_api.memberships;

import com.pubnub.api.endpoints.objects_api.PNObject;
import com.pubnub.api.endpoints.objects_api.users.PNUser;
import lombok.Getter;

@Getter
public class PNMember extends PNObject {

    private PNUser user;

    private PNMember() {
    }
}
