package com.pubnub.api.models.consumer.message_actions;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PNMessageAction {

    private String type;
    private String value;
    private String uuid;
    private Long actionTimetoken;
    private Long messageTimetoken;

    public PNMessageAction(String type, String value) {
        this.type = type;
        this.value = value;
    }

    PNMessageAction(PNMessageAction pnMessageAction) {
        this.type = pnMessageAction.type;
        this.value = pnMessageAction.value;
        this.uuid = pnMessageAction.uuid;
        this.actionTimetoken = pnMessageAction.actionTimetoken;
        this.messageTimetoken = pnMessageAction.messageTimetoken;
    }
}
