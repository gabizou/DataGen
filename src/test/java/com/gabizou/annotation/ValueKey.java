package com.gabizou.annotation;

public @interface ValueKey {

    /**
     * The {@link KeyReference} to the key to gather
     * type information from.
     *
     * @return The key reference
     */
    KeyReference value();
}
