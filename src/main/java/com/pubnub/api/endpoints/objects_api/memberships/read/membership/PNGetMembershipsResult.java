package com.pubnub.api.endpoints.objects_api.memberships.read.membership;

import com.pubnub.api.endpoints.objects_api.memberships.PNMembership;
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;

import lombok.Getter;

@Getter
public class PNGetMembershipsResult extends EntityArrayEnvelope<PNMembership> {

    static PNGetMembershipsResult create(EntityArrayEnvelope<PNMembership> envelope) {
        PNGetMembershipsResult result = new PNGetMembershipsResult();
        result.totalCount = envelope.getTotalCount();
        result.next = envelope.getNext();
        result.prev = envelope.getPrev();
        result.data = envelope.getData();
        return result;
    }

    static PNGetMembershipsResult create() {
        return new PNGetMembershipsResult();
    }
}
