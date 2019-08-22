package com.pubnub.api.endpoints.objects_api.memberships.manage.member;

import com.pubnub.api.endpoints.objects_api.memberships.PNMember;
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;

import lombok.Getter;

@Getter
public class PNMembersResult extends EntityArrayEnvelope<PNMember> {

    static PNMembersResult create(EntityArrayEnvelope<PNMember> envelope) {
        PNMembersResult result = new PNMembersResult();
        result.totalCount = envelope.getTotalCount();
        result.next = envelope.getNext();
        result.prev = envelope.getPrev();
        result.data = envelope.getData();
        return result;
    }

    static PNMembersResult create() {
        return new PNMembersResult();
    }

}
