package com.pubnub.api.endpoints.objects_api.spaces.update;

import com.pubnub.api.endpoints.objects_api.spaces.PNSpace;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PNUpdateSpaceResult {

    PNSpace space;
}
