package com.gabizou.annotation;

import org.spongepowered.api.data.manipulator.DataManipulator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as being a specific {@link DataManipulator} that is to be
 * auto-generated
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MutableData {

}
