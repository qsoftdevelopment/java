package com.pubnub.api.endpoints.objects_api.users.create;


import com.pubnub.api.endpoints.objects_api.users.PNUser;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PNCreateUserResult {

    PNUser user;
}
