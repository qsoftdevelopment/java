package com.pubnub.api.endpoints.objects_api.memberships.manage.membership;

import com.pubnub.api.endpoints.objects_api.memberships.PNMembership;
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;

import lombok.Getter;

@Getter
public class PNMembershipsResult extends EntityArrayEnvelope<PNMembership> {

    static PNMembershipsResult create(EntityArrayEnvelope<PNMembership> envelope) {
        PNMembershipsResult result = new PNMembershipsResult();
        result.totalCount = envelope.getTotalCount();
        result.next = envelope.getNext();
        result.prev = envelope.getPrev();
        result.data = envelope.getData();
        return result;
    }

    static PNMembershipsResult create() {
        return new PNMembershipsResult();
    }
}
