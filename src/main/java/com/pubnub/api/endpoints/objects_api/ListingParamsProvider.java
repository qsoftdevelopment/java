package com.pubnub.api.endpoints.objects_api;

import com.pubnub.api.endpoints.Endpoint;

public interface ListingParamsProvider<PubNubEndpoint extends Endpoint> {

    PubNubEndpoint limit(Integer limit);

    PubNubEndpoint start(String start);

    PubNubEndpoint end(String end);

    PubNubEndpoint withTotalCount(Boolean count);
}
