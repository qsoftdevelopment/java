package com.pubnub.api.endpoints.objects_api.memberships.manage;

import com.pubnub.api.endpoints.Endpoint;

public interface MembershipChainProvider<PubNubEndpoint extends Endpoint, PropertyType extends PropertyEnvelope> {

    PubNubEndpoint add(PropertyType... list);

    PubNubEndpoint update(PropertyType... list);

    PubNubEndpoint remove(PropertyType... list);
}
