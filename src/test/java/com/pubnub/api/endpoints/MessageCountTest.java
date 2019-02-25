package com.pubnub.api.endpoints;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.models.consumer.history.PNMessageCountResult;
import org.junit.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class MessageCountTest extends TestHarness {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options().port(this.PORT), false);

    private MessageCounts messageCounts;
    private PubNub pubnub;

    @Before
    public void beforeEach() throws IOException {
        pubnub = this.createPubNubInstance();
        messageCounts = pubnub.messageCounts();
        wireMockRule.start();
    }

    @After
    public void afterEach() {
        pubnub.destroy();
        pubnub = null;
        wireMockRule.stop();
    }

    @Test
    public void testSyncDisabled() {
        String payload = "[\"Use of the history API requires the Storage & Playback which is not enabled for this " +
                "subscribe key.Login to your PubNub Dashboard Account and enable Storage & Playback.Contact support " +
                "@pubnub.com if you require further assistance.\",0,0]";

        stubFor(get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel"))
                .willReturn(aResponse().withBody(payload)));

        try {
            messageCounts.channels(Arrays.asList("my_channel"))
                    .timetoken((long) 10000).sync();
        } catch (PubNubException ex) {
            assertEquals("History is disabled", ex.getErrormsg());
        }
    }

    @Test
    public void testSingleChannel_withSingleTimestamp() throws PubNubException {
        stubFor(get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"error\": false, \"error_message\": \"\", " +
                        "\"channels\": {\"my_channel\":19}}")));

        PNMessageCountResult response =
                messageCounts.channels(Arrays.asList("my_channel"))
                        .timetoken((long) 10000).sync();

        Assert.assertEquals(response.getChannels().size(), 1);
        Assert.assertFalse(response.getChannels().containsKey("channel_dont_exist"));
        Assert.assertTrue(response.getChannels().containsKey("my_channel"));
        for (Map.Entry<String, Long> stringLongEntry : response.getChannels().entrySet()) {
            assertEquals("my_channel", stringLongEntry.getKey());
            assertEquals(Long.valueOf("19"), stringLongEntry.getValue());
        }
    }

    @Test
    public void testSingleChannel_withMultiTimestamp() throws PubNubException {
        stubFor(get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"error\": false, \"error_message\": \"\", " +
                        "\"channels\": {\"my_channel\":19}}")));

        PNMessageCountResult response =
                messageCounts.channels(Arrays.asList("my_channel"))
                        .channelsTimetoken(Arrays.asList((long) 10000, (long) 20000)).sync();

        Assert.assertEquals(response.getChannels().size(), 1);
        Assert.assertFalse(response.getChannels().containsKey("channel_dont_exist"));
        Assert.assertTrue(response.getChannels().containsKey("my_channel"));
        for (Map.Entry<String, Long> stringLongEntry : response.getChannels().entrySet()) {
            assertEquals("my_channel", stringLongEntry.getKey());
            assertEquals(Long.valueOf("19"), stringLongEntry.getValue());
        }
    }

    @Test
    public void testMultiChannel_withSingleTimestamp() throws PubNubException {
        stubFor(get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel,new_channel"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"error\": false, \"error_message\": \"\", " +
                        "\"channels\": {\"my_channel\":19, \"new_channel\":5}}")));

        PNMessageCountResult response =
                messageCounts.channels(Arrays.asList("my_channel", "new_channel"))
                        .timetoken((long) 10000).sync();

        Assert.assertEquals(response.getChannels().size(), 2);
        Assert.assertFalse(response.getChannels().containsKey("channel_dont_exist"));
        Assert.assertTrue(response.getChannels().containsKey("my_channel"));
        Assert.assertTrue(response.getChannels().containsKey("new_channel"));
        for (Map.Entry<String, Long> stringLongEntry : response.getChannels().entrySet()) {
            if (stringLongEntry.getKey().equals("my_channel")) {
                assertEquals(Long.valueOf("19"), stringLongEntry.getValue());
            } else if (stringLongEntry.getKey().equals("new_channel")) {
                assertEquals(Long.valueOf("5"), stringLongEntry.getValue());
            }
        }
    }

    @Test
    public void testMultiChannel_withMultiTimestamp() throws PubNubException {
        stubFor(get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel,new_channel"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"error\": false, \"error_message\": \"\", " +
                        "\"channels\": {\"my_channel\":19, \"new_channel\":5}}")));

        PNMessageCountResult response =
                messageCounts.channels(Arrays.asList("my_channel", "new_channel"))
                        .channelsTimetoken(Arrays.asList((long) 10000, (long) 20000)).sync();

        Assert.assertEquals(response.getChannels().size(), 2);
        Assert.assertFalse(response.getChannels().containsKey("channel_dont_exist"));
        Assert.assertTrue(response.getChannels().containsKey("my_channel"));
        Assert.assertTrue(response.getChannels().containsKey("new_channel"));
        for (Map.Entry<String, Long> stringLongEntry : response.getChannels().entrySet()) {
            if (stringLongEntry.getKey().equals("my_channel")) {
                assertEquals(Long.valueOf("19"), stringLongEntry.getValue());
            } else if (stringLongEntry.getKey().equals("new_channel")) {
                assertEquals(Long.valueOf("5"), stringLongEntry.getValue());
            }
        }
    }

    @Test
    public void testWithoutTimeToken() {
        stubFor(get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"error\": false, \"error_message\": \"\", " +
                        "\"channels\": {\"my_channel\":19}}")));

        PubNubException exception = null;
        try {
            messageCounts.channels(Arrays.asList("my_channel")).sync();
        } catch (PubNubException ex) {
            exception = ex;
        } finally {
            assertNotNull(exception);
            assertEquals("Timetoken Missing.", exception.getPubnubError().getMessage());
        }
    }

    @Test
    public void testWithoutChannels_SingleTimeToken() {
        stubFor(get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"error\": false, \"error_message\": \"\", " +
                        "\"channels\": {\"my_channel\":19, \"new_channel\":5}}")));

        PubNubException exception = null;
        try {
            messageCounts.timetoken((long) 10000).sync();
        } catch (PubNubException ex) {
            exception = ex;
        } finally {
            assertNotNull(exception);
            assertEquals("Channel Missing.", exception.getPubnubError().getMessage());
        }
    }

    @Test
    public void testWithoutChannels_TimeTokenList() {
        stubFor(get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"error\": false, \"error_message\": \"\", " +
                        "\"channels\": {\"my_channel\":19, \"new_channel\":5}}")));

        PubNubException exception = null;
        try {
            messageCounts.channelsTimetoken(Arrays.asList((long) 10000, (long) 20000)).sync();
        } catch (PubNubException ex) {
            exception = ex;
        } finally {
            assertNotNull(exception);
            assertEquals("Channel Missing.", exception.getPubnubError().getMessage());
        }
    }

    @Test
    public void testSingleChannel_withTwoTokens() throws PubNubException {
        stubFor(get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"error\": false, \"error_message\": \"\", " +
                        "\"channels\": {\"my_channel\":19}}")));

        PNMessageCountResult response =
                messageCounts.channels(Arrays.asList("my_channel"))
                        .channelsTimetoken(Arrays.asList((long) 10000, (long) 20000))
                        .timetoken((long) 10000).sync();

        Assert.assertEquals(response.getChannels().size(), 1);
        Assert.assertFalse(response.getChannels().containsKey("channel_dont_exist"));
        Assert.assertTrue(response.getChannels().containsKey("my_channel"));
        Assert.assertFalse(response.getChannels().containsKey("new_channel"));
        for (Map.Entry<String, Long> stringLongEntry : response.getChannels().entrySet()) {
            if (stringLongEntry.getKey().equals("my_channel")) {
                assertEquals(Long.valueOf("19"), stringLongEntry.getValue());
            } else if (stringLongEntry.getKey().equals("new_channel")) {
                assertEquals(Long.valueOf("5"), stringLongEntry.getValue());
            }
        }
    }

    @Test
    public void testMultiChannel_withTwoTokens() throws PubNubException {
        stubFor(get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel,new_channel"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"error\": false, \"error_message\": \"\", " +
                        "\"channels\": {\"my_channel\":19, \"new_channel\":5}}")));

        PNMessageCountResult response =
                messageCounts.channels(Arrays.asList("my_channel", "new_channel"))
                        .channelsTimetoken(Arrays.asList((long) 10000, (long) 20000))
                        .timetoken((long) 10000).sync();

        Assert.assertEquals(response.getChannels().size(), 2);
        Assert.assertFalse(response.getChannels().containsKey("channel_dont_exist"));
        Assert.assertTrue(response.getChannels().containsKey("my_channel"));
        Assert.assertTrue(response.getChannels().containsKey("new_channel"));
        for (Map.Entry<String, Long> stringLongEntry : response.getChannels().entrySet()) {
            if (stringLongEntry.getKey().equals("my_channel")) {
                assertEquals(Long.valueOf("19"), stringLongEntry.getValue());
            } else if (stringLongEntry.getKey().equals("new_channel")) {
                assertEquals(Long.valueOf("5"), stringLongEntry.getValue());
            }
        }
    }

    @Test
    public void testChannel_withSingleEmptyToken() {
        stubFor(get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"error\": false, \"error_message\": \"\", " +
                        "\"channels\": {\"my_channel\":19}}")));

        PubNubException exception = null;
        try {
            messageCounts.channels(Arrays.asList("my_channel"))
                    .timetoken(null).sync();
        } catch (PubNubException ex) {
            exception = ex;
        } finally {
            assertNotNull(exception);
            assertEquals("Timetoken Missing.", exception.getPubnubError().getMessage());
        }
    }

    @Test
    public void testChannel_withMultiEmptyToken() {
        stubFor(get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"error\": false, \"error_message\": \"\", " +
                        "\"channels\": {\"my_channel\":19}}")));

        PubNubException exception = null;
        try {
            messageCounts.channels(Arrays.asList("my_channel"))
                    .channelsTimetoken(Arrays.asList()).sync();
        } catch (PubNubException ex) {
            exception = ex;
        } finally {
            assertNotNull(exception);
            assertEquals("Timetoken Missing.", exception.getPubnubError().getMessage());
        }
    }

    @Test
    public void testChannel_withMultiNullToken() {
        stubFor(get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"error\": false, \"error_message\": \"\", " +
                        "\"channels\": {\"my_channel\":19}}")));

        PubNubException exception = null;
        try {
            messageCounts.channels(Arrays.asList("my_channel"))
                    .channelsTimetoken(null).sync();
        } catch (PubNubException ex) {
            exception = ex;
        } finally {
            assertNotNull(exception);
            assertEquals("Timetoken Missing.", exception.getPubnubError().getMessage());
        }
    }

}
