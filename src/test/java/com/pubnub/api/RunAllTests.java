package com.pubnub.api;

import com.pubnub.api.endpoints.*;
import com.pubnub.api.endpoints.access.AuditEndpointTest;
import com.pubnub.api.endpoints.access.GrantEndpointTest;
import com.pubnub.api.endpoints.channel_groups.*;
import com.pubnub.api.endpoints.presence.*;
import com.pubnub.api.endpoints.pubsub.PublishTest;
import com.pubnub.api.endpoints.pubsub.SubscribeEndpointTest;
import com.pubnub.api.endpoints.push.ListPushProvisionsTest;
import com.pubnub.api.endpoints.push.ModifyPushChannelsForDeviceTest;
import com.pubnub.api.managers.BasePathManagerTest;
import com.pubnub.api.managers.PublishSequenceManagerTest;
import com.pubnub.api.managers.SubscriptionManagerTest;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

public class RunAllTests {

    @Test
    public void runAll() {
        //Class for running test synchronously
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        junit.run(PubNubTest.class);
        junit.run(PubNubExceptionTest.class);
        junit.run(Base64Test.class);

        //Manager tests
        junit.run(SubscriptionManagerTest.class);
        junit.run(PublishSequenceManagerTest.class);
        junit.run(BasePathManagerTest.class);

        //Endpoint tests
        junit.run(TimeEndpointTest.class);
        junit.run(HistoryEndpointTest.class);
        junit.run(HeartbeatEndpointTest.class);
        junit.run(FetchMessagesEndpointTest.class);
        junit.run(EndpointTest.class);
        junit.run(DeleteMessagesEndpointTest.class);

        junit.run(ListPushProvisionsTest.class);
        junit.run(ModifyPushChannelsForDeviceTest.class);

        junit.run(PublishTest.class);
        junit.run(SubscribeEndpointTest.class);

        junit.run(WhereNowEndpointTest.class);
        junit.run(SetStateEndpointTest.class);
        junit.run(LeaveTest.class);
        junit.run(HereNowEndpointTest.class);
        junit.run(GetStateEndpointTest.class);

        junit.run(AddChannelChannelGroupEndpointTest.class);
        junit.run(AllChannelsChannelGroupEndpointTest.class);
        junit.run(DeleteChannelGroupEndpointTest.class);
        junit.run(ListAllChannelGroupEndpointTest.class);
        junit.run(RemoveChannelChannelGroupEndpointTest.class);

        junit.run(AuditEndpointTest.class);
        junit.run(GrantEndpointTest.class);
    }

}
