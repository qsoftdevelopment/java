package com.pubnub.api.endpoints.objects_api.users.read;

import com.pubnub.api.endpoints.objects_api.users.PNUser;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PNGetUserResult {

    PNUser user;
}
