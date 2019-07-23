package com.pubnub.api.endpoints.pubsub;

import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.PubNubUtil;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.endpoints.TestHarness;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import org.awaitility.Awaitility;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.findAll;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_MISSING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SignalTest extends TestHarness {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options().port(this.PORT), false);

    private PubNub pubnub;

    @Before
    public void beforeEach() throws IOException {
        pubnub = this.createPubNubInstance();
        wireMockRule.start();
    }

    @After
    public void afterEach() {
        pubnub.destroy();
        pubnub = null;
        wireMockRule.stop();
    }

    @Test
    public void testSignalGetSuccessSync() throws PubNubException {
        stubFor(get(urlMatching("/signal/myPublishKey/mySubscribeKey/0/coolChannel.*"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"1000\"]")));

        Map<String, Object> payload = new HashMap<>();
        payload.put("text", "hello");

        pubnub.signal()
                .channel("coolChannel")
                .meta(payload)
                .message(payload)
                .sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        LoggedRequest request = requests.get(0);
        assertEquals("myUUID", request.queryParameter("uuid").firstValue());
        assertEquals(PubNubUtil.urlDecode(pubnub.getMapper().toJson(payload)), request.queryParameter("meta")
                .firstValue());
    }


    @Test
    public void testSignalGetSuccessAsync() {

        String payload = UUID.randomUUID().toString();

        stubFor(get(urlMatching("/signal/myPublishKey/mySubscribeKey/0/coolChannel.*"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"1000\"]")));

        final AtomicBoolean success = new AtomicBoolean();

        pubnub.signal()
                .channel("coolChannel")
                .message(payload)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        assertFalse(status.isError());
                        assertEquals(PNOperationType.PNSignalOperation, status.getOperation());
                        assertEquals("1000", result.getTimetoken().toString());
                        success.set(true);
                    }
                });

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilTrue(success);

    }

    @Test
    public void testSignalSuccessReceive() {

        pubnub.getConfiguration().setPresenceTimeoutWithCustomInterval(60, 0);

        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/coolChannel/0"))
                .willReturn(aResponse().withBody("{\"m\":[{\"c\":\"coolChannel\",\"f\":\"0\",\"i\":\"uuid\"," +
                        "\"d\":\"hello\",\"e\":1,\"p\":{\"t\":1000,\"r\":1},\"k\":\"mySubscribeKey\"," +
                        "\"b\":\"coolChannel\"}],\"t\":{\"r\":\"56\",\"t\":1000}}")));

        AtomicInteger count = new AtomicInteger();

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                count.incrementAndGet();
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }

            @Override
            public void signal(PubNub pubnub, PNMessageResult signal) {
                Assert.assertEquals("coolChannel", signal.getChannel());
                Assert.assertEquals("hello", signal.getMessage().getAsString());
                Assert.assertEquals("uuid", signal.getPublisher());
                count.incrementAndGet();
                pubnub.disconnect();
            }
        });

        pubnub.subscribe()
                .channels(Collections.singletonList("coolChannel"))
                .execute();

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAtomic(count, IsEqual.equalTo(1));
    }

    @Test
    public void testSignalQueryParams() throws PubNubException {
        stubFor(get(urlMatching("/signal/myPublishKey/mySubscribeKey/0/coolChannel.*"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"1000\"]")));

        pubnub.signal()
                .channel("coolChannel")
                .message(UUID.randomUUID().toString())
                .sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        LoggedRequest request = requests.get(0);
        assertFalse(request.getQueryParams().containsKey("instanceid"));
        // assertFalse(request.getQueryParams().containsKey("requestid"));
    }


    @Test
    public void testSignalHttpMethod() throws PubNubException {
        stubFor(get(urlMatching("/signal/myPublishKey/mySubscribeKey/0/coolChannel.*"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"1000\"]")));

        pubnub.signal()
                .channel("coolChannel")
                .message(UUID.randomUUID().toString())
                .sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/signal.*")));
        assertEquals(1, requests.size());
        LoggedRequest request = requests.get(0);
        assertEquals(RequestMethod.GET, request.getMethod());
    }

    @Test
    public void testSignalFailNoChannel() {
        try {
            pubnub.signal()
                    .message(UUID.randomUUID().toString())
                    .sync();
        } catch (PubNubException e) {
            Assert.assertEquals(PNERROBJ_CHANNEL_MISSING.getMessage(), e.getPubnubError().getMessage());
        }
    }

    @Test
    public void testSignalFailNoMessage() {
        try {
            pubnub.signal()
                    .channel(UUID.randomUUID().toString())
                    .sync();
        } catch (PubNubException e) {
            Assert.assertEquals(PNERROBJ_MESSAGE_MISSING.getMessage(), e.getPubnubError().getMessage());
        }
    }
}
