package com.pubnub.api.callbacks;

import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

public class SubscribeCallback extends SubscribeCallbackOld {
    @Override
    public void status(PubNub pubnub, PNStatus status) {

    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {

    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }

    public void signal(PubNub pubnub, PNMessageResult signal) {

    }
}
