package com.gabizou;

import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.value.mutable.Value;

@SuppressWarnings("unchecked")
public class DemoKeys {

    public static final Key<Value<Boolean>> DEMO_BOOL = KeyFactory.makeSingleKey(Boolean.class, (Class<Value<Boolean>>) (Class) Value.class, DataQuery.of("demoBoolean"));

}
