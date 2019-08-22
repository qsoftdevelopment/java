package com.pubnub.api.endpoints.objects_api.spaces.create;

import com.pubnub.api.endpoints.objects_api.spaces.PNSpace;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PNCreateSpaceResult {

    PNSpace space;
}
