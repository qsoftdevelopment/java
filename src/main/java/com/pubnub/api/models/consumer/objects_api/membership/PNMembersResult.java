package com.pubnub.api.models.consumer.objects_api.membership;

import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import lombok.Getter;

@Getter
public class PNMembersResult extends EntityArrayEnvelope<PNMember> {

    public static PNMembersResult create(EntityArrayEnvelope<PNMember> envelope) {
        PNMembersResult result = new PNMembersResult();
        result.totalCount = envelope.getTotalCount();
        result.next = envelope.getNext();
        result.prev = envelope.getPrev();
        result.data = envelope.getData();
        return result;
    }

    public static PNMembersResult create() {
        return new PNMembersResult();
    }

}
