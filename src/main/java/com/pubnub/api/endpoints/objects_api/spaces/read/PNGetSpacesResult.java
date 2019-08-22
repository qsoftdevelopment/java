package com.pubnub.api.endpoints.objects_api.spaces.read;

import com.pubnub.api.endpoints.objects_api.spaces.PNSpace;
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;

import lombok.Getter;

@Getter
public class PNGetSpacesResult extends EntityArrayEnvelope<PNSpace> {

    static PNGetSpacesResult create(EntityArrayEnvelope<PNSpace> envelope) {
        PNGetSpacesResult result = new PNGetSpacesResult();
        result.totalCount = envelope.getTotalCount();
        result.next = envelope.getNext();
        result.prev = envelope.getPrev();
        result.data = envelope.getData();
        return result;
    }

    static PNGetSpacesResult create() {
        return new PNGetSpacesResult();
    }
}
