package com.pubnub.api.endpoints.objects_api.memberships.manage.member;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.objects_api.InclusionParamsProvider;
import com.pubnub.api.endpoints.objects_api.ListingParamsProvider;
import com.pubnub.api.endpoints.objects_api.enums.PNMemberFields;
import com.pubnub.api.endpoints.objects_api.memberships.PNMember;
import com.pubnub.api.endpoints.objects_api.memberships.manage.MembershipChainProvider;
import com.pubnub.api.endpoints.objects_api.memberships.manage.PNPatchPayload;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

@Accessors(chain = true, fluent = true)
public class Members extends Endpoint<EntityArrayEnvelope<PNMember>, PNMembersResult> implements
        InclusionParamsProvider<Members, PNMemberFields>,
        ListingParamsProvider<Members>,
        MembershipChainProvider<Members, Member> {

    private Map<String, String> extraParamsMap;
    private PNPatchPayload<Member> pnPatchPayload;

    @Setter
    private String spaceId;

    public Members(PubNub pubnubInstance, TelemetryManager telemetry, RetrofitManager retrofitInstance) {
        super(pubnubInstance, telemetry, retrofitInstance);
        extraParamsMap = new HashMap<>();
        pnPatchPayload = new PNPatchPayload<>();
    }

    @Override
    protected List<String> getAffectedChannels() {
        return null;
    }

    @Override
    protected List<String> getAffectedChannelGroups() {
        return null;
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (this.getPubnub().getConfiguration().getSubscribeKey() == null
                || this.getPubnub().getConfiguration().getSubscribeKey().isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SUBSCRIBE_KEY_MISSING).build();
        }

        if (this.spaceId == null || this.spaceId.isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SPACE_ID_MISSING).build();
        }
    }

    @Override
    protected Call<EntityArrayEnvelope<PNMember>> doWork(Map<String, String> params) {

        params.putAll(extraParamsMap);

        params.putAll(encodeParams(params));

        return this.getRetrofit()
                .getMembershipService()
                .members(this.getPubnub().getConfiguration().getSubscribeKey(), spaceId, pnPatchPayload, params);
    }

    @Override
    protected PNMembersResult createResponse(Response<EntityArrayEnvelope<PNMember>> input) throws PubNubException {
        if (input.body() != null) {
            return PNMembersResult.create(input.body());
        }
        return PNMembersResult.create();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNMembers;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @Override
    public Members includeFields(PNMemberFields... params) {
        return appendInclusionParams(extraParamsMap, params);
    }

    @Override
    public Members limit(Integer limit) {
        return appendLimitParam(extraParamsMap, limit);
    }

    @Override
    public Members start(String start) {
        extraParamsMap.put("start", start);
        return this;
    }

    @Override
    public Members end(String end) {
        extraParamsMap.put("end", end);
        return this;
    }

    @Override
    public Members withTotalCount(Boolean count) {
        extraParamsMap.put("count", String.valueOf(count));
        return this;
    }

    @Override
    public Members add(Member... list) {
        pnPatchPayload.setAdd(list);
        return this;
    }

    @Override
    public Members update(Member... list) {
        pnPatchPayload.setUpdate(list);
        return this;
    }

    @Override
    public Members remove(Member... list) {
        pnPatchPayload.setRemove(list);
        return this;
    }
}


