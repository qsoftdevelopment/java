package com.pubnub.api.endpoints.objects_api.memberships.read.member;

import com.pubnub.api.endpoints.objects_api.memberships.PNMember;
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;

import lombok.Getter;

@Getter
public class PNGetMembersResult extends EntityArrayEnvelope<PNMember> {

    static PNGetMembersResult create(EntityArrayEnvelope<PNMember> envelope) {
        PNGetMembersResult result = new PNGetMembersResult();
        result.totalCount = envelope.getTotalCount();
        result.next = envelope.getNext();
        result.prev = envelope.getPrev();
        result.data = envelope.getData();
        return result;
    }

    static PNGetMembersResult create() {
        return new PNGetMembersResult();
    }

}
