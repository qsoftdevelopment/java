package com.pubnub.api.endpoints.objects_api.memberships.manage.member;

import com.pubnub.api.endpoints.objects_api.memberships.manage.PropertyEnvelope;

public class Member extends PropertyEnvelope<Member> {

    private Member() {

    }

    public static Member userId(String userId) {
        Member member = new Member();
        member.id = userId;
        return member;
    }

}
