package com.pubnub.api.models.consumer.objects_api.membership;

import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import lombok.Getter;

@Getter
public class PNMembershipsResult extends EntityArrayEnvelope<PNMembership> {

    public static PNMembershipsResult create(EntityArrayEnvelope<PNMembership> envelope) {
        PNMembershipsResult result = new PNMembershipsResult();
        result.totalCount = envelope.getTotalCount();
        result.next = envelope.getNext();
        result.prev = envelope.getPrev();
        result.data = envelope.getData();
        return result;
    }

    public static PNMembershipsResult create() {
        return new PNMembershipsResult();
    }
}
