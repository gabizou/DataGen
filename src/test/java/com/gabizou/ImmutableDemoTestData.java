package com.gabizou;

import com.gabizou.data.ImmutableAbstractData;
import com.google.common.base.Optional;
import com.google.common.primitives.Booleans;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public class ImmutableDemoTestData extends ImmutableAbstractData<ImmutableDemoTestData, DemoTestData> {

    private final boolean demoBool;

    public ImmutableDemoTestData(boolean demoBool) {
        super(ImmutableDemoTestData.class);
        this.demoBool = demoBool;
    }

    @Override
    protected void registerGetters() {
        registerKeyValue(DemoKeys.DEMO_BOOL, this::getBoolVal);
        registerFieldGetter(DemoKeys.DEMO_BOOL, this::isDemoBool);
    }

    public boolean isDemoBool() {
        return demoBool;
    }

    public ImmutableValue<Boolean> getBoolVal() {
        return DemoPlugin.getGame().getRegistry().createValueBuilder().createValue(DemoKeys.DEMO_BOOL, this.demoBool).asImmutable();
    }

    @Override
    public <E> Optional<ImmutableDemoTestData> with(Key<? extends BaseValue<E>> key, E value) {
        if (key.equals(DemoKeys.DEMO_BOOL)) {
            if (this.demoBool) {
                if (!(Boolean) value) {
                    return Optional.of(new ImmutableDemoTestData(false));
                }
            } else {
                if ((Boolean) value) {
                    return Optional.of(new ImmutableDemoTestData(true));
                }
            }
        }
        return Optional.absent();
    }

    @Override
    public ImmutableDemoTestData copy() {
        return this;
    }

    @Override
    public DemoTestData asMutable() {
        return new DemoTestData(this.demoBool);
    }

    @Override
    public int compareTo(ImmutableDemoTestData o) {
        return Booleans.compare(o.demoBool, this.demoBool);
    }

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer()
            .set(DemoKeys.DEMO_BOOL, this.demoBool);
    }
}
