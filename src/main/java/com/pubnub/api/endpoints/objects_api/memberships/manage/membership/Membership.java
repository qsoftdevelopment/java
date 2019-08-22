package com.pubnub.api.endpoints.objects_api.memberships.manage.membership;

import com.pubnub.api.endpoints.objects_api.memberships.manage.PropertyEnvelope;

public class Membership extends PropertyEnvelope<Membership> {

    private Membership() {

    }

    public static Membership spaceId(String spaceId) {
        Membership membership = new Membership();
        membership.id = spaceId;
        return membership;
    }

}
