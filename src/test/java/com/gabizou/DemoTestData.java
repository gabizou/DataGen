package com.gabizou;

import com.gabizou.data.AbstractData;
import com.google.common.base.Optional;
import com.google.common.primitives.Booleans;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

public class DemoTestData extends AbstractData<DemoTestData, ImmutableDemoTestData> {

    private boolean demoBool;

    public DemoTestData() {
        super(DemoTestData.class);
    }

    public DemoTestData(boolean demoBool) {
        this();
        this.demoBool = demoBool;
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(DemoKeys.DEMO_BOOL, this::isDemoBool);
        registerFieldSetter(DemoKeys.DEMO_BOOL, value -> setDemoBool((Boolean) value));
        registerKeyValue(DemoKeys.DEMO_BOOL, this::getDemoboolVal);
    }

    @Override
    public Optional<DemoTestData> fill(DataHolder dataHolder, MergeFunction overlap) {
        return Optional.absent();
    }

    @Override
    public Optional<DemoTestData> from(DataContainer container) {
        return Optional.absent();
    }

    @Override
    public DemoTestData copy() {
        return new DemoTestData(this.demoBool);
    }

    @Override
    public ImmutableDemoTestData asImmutable() {
        return new ImmutableDemoTestData(this.demoBool);
    }

    @Override
    public int compareTo(DemoTestData o) {
        return Booleans.compare(o.isDemoBool(), this.demoBool);
    }

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer()
            .set(DemoKeys.DEMO_BOOL.getQuery(), this.demoBool);
    }

    public boolean isDemoBool() {
        return demoBool;
    }

    public void setDemoBool(boolean demoBool) {
        this.demoBool = demoBool;
    }

    public Value<Boolean> getDemoboolVal() {
        return DemoPlugin.getGame().getRegistry().createValueBuilder().createValue(DemoKeys.DEMO_BOOL, this.demoBool, false);
    }

}
