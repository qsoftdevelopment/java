package com.pubnub.api.endpoints.channel_groups;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.endpoints.TestHarness;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult;
import org.awaitility.Awaitility;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RemoveChannelChannelGroupEndpointTest extends TestHarness {
    private RemoveChannelChannelGroup partialRemoveChannelChannelGroup;
    private PubNub pubnub;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void beforeEach() throws IOException {
        pubnub = this.createPubNubInstance(8080);
        partialRemoveChannelChannelGroup = pubnub.removeChannelsFromChannelGroup();
        wireMockRule.start();
    }

    @Test
    public void testSyncSuccess() throws IOException, PubNubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {}, \"service\": \"ChannelGroups\"}")));

        PNChannelGroupsRemoveChannelResult response = partialRemoveChannelChannelGroup.channelGroup("groupA").channels(Arrays.asList("ch1", "ch2")).sync();
        assertNotNull(response);
    }

    @Test(expected = PubNubException.class)
    public void testSyncMissinGroup() throws IOException, PubNubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {}, \"service\": \"ChannelGroups\"}")));

        partialRemoveChannelChannelGroup.channels(Arrays.asList("ch1", "ch2")).sync();
    }

    @Test(expected = PubNubException.class)
    public void testSyncMissinChannel() throws IOException, PubNubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {}, \"service\": \"ChannelGroups\"}")));

        partialRemoveChannelChannelGroup.channelGroup("groupA").sync();
    }

    @Test
    public void testIsAuthRequiredSuccessSync() throws IOException, PubNubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {}, \"service\": \"ChannelGroups\"}")));

        pubnub.getConfiguration().setAuthKey("myKey");
        partialRemoveChannelChannelGroup.channelGroup("groupA").channels(Arrays.asList("ch1", "ch2")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myKey", requests.get(0).queryParameter("auth").firstValue());
    }

    @Test
    public void testOperationTypeSuccessAsync() throws IOException, PubNubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {}, \"service\": \"ChannelGroups\"}")));

        final AtomicInteger atomic = new AtomicInteger(0);

        partialRemoveChannelChannelGroup.channelGroup("groupA").channels(Arrays.asList("ch1", "ch2")).async(new PNCallback<PNChannelGroupsRemoveChannelResult>() {
            @Override
            public void onResponse(PNChannelGroupsRemoveChannelResult result, PNStatus status) {
                if (status != null && status.getOperation() == PNOperationType.PNRemoveChannelsFromGroupOperation) {
                    atomic.incrementAndGet();
                }
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));
    }
}
