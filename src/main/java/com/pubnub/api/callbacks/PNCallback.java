package com.pubnub.api.callbacks;


import org.jetbrains.annotations.Nullable;

/**
 * Left for backwards compatibility. Replace with {@link PNResultCallback}.
 */
@Deprecated
public abstract class PNCallback<@Nullable X> implements PNResultCallback<X> {
}
