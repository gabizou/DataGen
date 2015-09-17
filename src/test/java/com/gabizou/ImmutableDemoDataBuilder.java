package com.gabizou;

import com.google.common.base.Optional;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.ImmutableDataHolder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulatorBuilder;
import org.spongepowered.api.service.persistence.InvalidDataException;

public class ImmutableDemoDataBuilder implements ImmutableDataManipulatorBuilder<ImmutableDemoTestData, DemoTestData> {

    @Override
    public ImmutableDemoTestData createImmutable() {
        return new ImmutableDemoTestData(false);
    }

    @Override
    public Optional<ImmutableDemoTestData> createFrom(DataHolder dataHolder) {
        return Optional.absent();
    }

    @Override
    public Optional<ImmutableDemoTestData> createFrom(ImmutableDataHolder<?> dataHolder) {
        return Optional.absent();
    }

    @Override
    public Optional<ImmutableDemoTestData> build(DataView container) throws InvalidDataException {
        if (!container.contains(DemoKeys.DEMO_BOOL.getQuery())) {
            throw new InvalidDataException("The container doesn't have the required keys!");
        }
        return Optional.of(new ImmutableDemoTestData(container.getBoolean(DemoKeys.DEMO_BOOL.getQuery()).get()));
    }
}
