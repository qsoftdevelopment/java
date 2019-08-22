package com.pubnub.api.endpoints.objects_api;

import com.pubnub.api.endpoints.Endpoint;

public interface InclusionParamsProvider<PubNubEndpoint extends Endpoint, FieldsEnumType extends Enum> {

    PubNubEndpoint includeFields(FieldsEnumType... params);
}