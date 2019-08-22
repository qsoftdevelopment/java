package com.pubnub.api.endpoints.objects_api.memberships.manage.membership;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.objects_api.InclusionParamsProvider;
import com.pubnub.api.endpoints.objects_api.ListingParamsProvider;
import com.pubnub.api.endpoints.objects_api.enums.PNMembershipFields;
import com.pubnub.api.endpoints.objects_api.memberships.PNMembership;
import com.pubnub.api.endpoints.objects_api.memberships.manage.MembershipChainProvider;
import com.pubnub.api.endpoints.objects_api.memberships.manage.PNPatchPayload;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class Memberships extends Endpoint<EntityArrayEnvelope<PNMembership>, PNMembershipsResult> implements
        InclusionParamsProvider<Memberships, PNMembershipFields>,
        ListingParamsProvider<Memberships>,
        MembershipChainProvider<Memberships, Membership> {

    private Map<String, String> extraParamsMap;
    private PNPatchPayload<Membership> pnPatchPayload;

    @Setter
    private String userId;

    public Memberships(PubNub pubnubInstance, TelemetryManager telemetry, RetrofitManager retrofitInstance) {
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

        if (this.userId == null || this.userId.isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_USER_ID_MISSING).build();
        }
    }

    @Override
    protected Call<EntityArrayEnvelope<PNMembership>> doWork(Map<String, String> params) {

        params.putAll(extraParamsMap);

        params.putAll(encodeParams(params));

        return this.getRetrofit()
                .getMembershipService()
                .memberships(this.getPubnub().getConfiguration().getSubscribeKey(), userId, pnPatchPayload, params);
    }

    @Override
    protected PNMembershipsResult createResponse(Response<EntityArrayEnvelope<PNMembership>> input) throws PubNubException {
        if (input.body() != null) {
            return PNMembershipsResult.create(input.body());
        }
        return PNMembershipsResult.create();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNMemberships;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @Override
    public Memberships includeFields(PNMembershipFields... params) {
        return appendInclusionParams(extraParamsMap, params);
    }

    @Override
    public Memberships add(Membership... list) {
        pnPatchPayload.setAdd(list);
        return this;
    }

    @Override
    public Memberships update(Membership... list) {
        pnPatchPayload.setUpdate(list);
        return this;
    }

    @Override
    public Memberships remove(Membership... list) {
        pnPatchPayload.setRemove(list);
        return this;
    }

    @Override
    public Memberships limit(Integer limit) {
        return appendLimitParam(extraParamsMap, limit);
    }

    @Override
    public Memberships start(String start) {
        extraParamsMap.put("start", start);
        return this;
    }

    @Override
    public Memberships end(String end) {
        extraParamsMap.put("end", end);
        return this;
    }

    @Override
    public Memberships withTotalCount(Boolean count) {
        extraParamsMap.put("count", String.valueOf(count));
        return this;
    }
}
