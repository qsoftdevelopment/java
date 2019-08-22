package com.pubnub.api.endpoints.objects_api.spaces.read;

import com.pubnub.api.endpoints.objects_api.spaces.PNSpace;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PNGetSpaceResult {

    PNSpace space;
}
