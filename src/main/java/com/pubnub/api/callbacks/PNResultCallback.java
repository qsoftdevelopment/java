package com.pubnub.api.callbacks;

import com.pubnub.api.models.consumer.PNStatus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PNResultCallback<@Nullable X> {
   void onResponse(@Nullable X result, @NotNull PNStatus status);
}
