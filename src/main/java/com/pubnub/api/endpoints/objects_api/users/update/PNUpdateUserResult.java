package com.pubnub.api.endpoints.objects_api.users.update;


import com.pubnub.api.endpoints.objects_api.users.PNUser;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PNUpdateUserResult {

    PNUser user;
}
