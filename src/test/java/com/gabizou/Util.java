package com.gabizou;

import org.mockito.Mockito;
import org.spongepowered.api.data.value.ValueBuilder;

public class Util {

    public static ValueBuilder getBuilder() {
        return Mockito.mock(ValueBuilder.class);
    }

}
