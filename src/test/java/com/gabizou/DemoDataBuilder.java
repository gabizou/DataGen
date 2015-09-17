package com.gabizou;

import com.google.common.base.Optional;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.service.persistence.InvalidDataException;

public class DemoDataBuilder implements DataManipulatorBuilder<DemoTestData, ImmutableDemoTestData> {

    @Override
    public DemoTestData create() {
        return new DemoTestData(false);
    }

    @Override
    public Optional<DemoTestData> createFrom(DataHolder dataHolder) {
        return Optional.absent();
    }

    @Override
    public Optional<DemoTestData> build(DataView container) throws InvalidDataException {
        if (!container.contains(DemoKeys.DEMO_BOOL.getQuery())) {
            throw new InvalidDataException("The container doesn't have the required keys!");
        }
        return Optional.of(new DemoTestData(container.getBoolean(DemoKeys.DEMO_BOOL.getQuery()).get()));
    }
}
