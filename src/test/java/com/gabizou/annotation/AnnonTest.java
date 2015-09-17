package com.gabizou.annotation;

import com.gabizou.DemoKeys;
import com.google.common.base.Optional;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

@MutableData
public abstract class AnnonTest implements DataManipulator<AnnonTest, ImmutableAnnonTest> {

    @ValueKey(@KeyReference(container = DemoKeys.class, keyName = "demobool"))
    public abstract Value<Boolean> anonymousBoolean();

    @MethodImplementation
    @Override
    public Optional<AnnonTest> fill(DataHolder dataHolder) {
        return null;
    }

    @MethodImplementation
    @Override
    public Optional<AnnonTest> fill(DataHolder dataHolder, MergeFunction overlap) {
        return null;
    }

    @MethodImplementation
    @Override
    public Optional<AnnonTest> from(DataContainer container) {
        return null;
    }
}
