package com.gabizou.annotation;

import com.gabizou.DemoKeys;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.mutable.Value;

@ImmutableData
public abstract class ImmutableAnnonTest implements ImmutableDataManipulator<ImmutableAnnonTest, AnnonTest> {

    @ValueKey(@KeyReference(container = DemoKeys.class, keyName = "demobool"))
    public abstract Value<Boolean> demoBool();

}
