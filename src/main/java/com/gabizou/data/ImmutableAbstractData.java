package com.gabizou.data;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.gabizou.util.GetterFunction;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

/**
 * So, considering this is the root of the immutable variants of
 * {@link DataManipulator}, otherwise known as {@link ImmutableDataManipulator}s.
 * The advantage of these types of {@link DataManipulator}s is that they can not be
 * mutated once created. In other words, it's safe to pass around these immutable
 * variants across threads without worry of the underlying values being changed.
 *
 * It may be possible that some commonly used {@link ImmutableDataManipulator}s
 * may be cached for better performance when processing obtaining new
 * {@link ImmutableDataManipulator}s with different values.
 *
 * <p>Note: It is ABSOLUTELY REQUIRED to {@link #registerKeyValue(Key, GetterFunction)}
 * and {@link #registerFieldGetter(Key, GetterFunction)} for all possible
 * {@link Key}s and {@link Value}s the {@link DataManipulator} may provide as
 * all of the implementation methods provided here are handled using those.</p>
 *
 * @param <I> The immutable data manipulator type
 * @param <M> The mutable manipulator type
 */
@SuppressWarnings("unchecked")
public abstract class ImmutableAbstractData<I extends ImmutableAbstractData<I, M>, M extends AbstractData<M, I>> implements ImmutableDataManipulator<I, M> {

    private final Class<I> immutableClass;

    // Ok, so, you're probably asking "Why the hell are you doing this type of hackery?"
    // Answer: Because I'd rather have these abstract functions (read method references)
    // to get and set field values according to the key, and get values based on key
    // instead of using a Value/Data Processor. The advantage of course is that
    // in Java 8, this would all be done with lambda expressions.
    //
    // There was a possibility for using annotations, but I (gabizou) decided against
    // it since there's a lot of magic that goes on with it, and there is very little
    // customization when it comes to the method calls for setting/getting the values.
    // The largest issue was implementation. Since most fields are simple to get and
    // set, other values, such as ItemStacks require a bit of finer tuning.
    //
    private final Map<Key<?>, GetterFunction<ImmutableValue<?>>> keyValueMap = Maps.newHashMap();
    private final Map<Key<?>, GetterFunction<?>> keyFieldGetterMap = Maps.newHashMap();

    protected ImmutableAbstractData(Class<I> immutableClass) {
        this.immutableClass = checkNotNull(immutableClass);
        registerGetters();
    }

    /**
     * Simple registration method for the keys to value return methods.
     *
     * <p>Note that this is still going to be usable, but will be made simpler
     * when Java 8 is used, as lambda expressions can refrence methods. The
     * update won't actually change these registration methods, but the
     * {@link DataManipulator}s calling these registration methods will
     * become single line simplifications.</p>
     *
     * @param key The key for the value return type
     * @param function The function for getting the value
     */
    protected final void registerKeyValue(Key<?> key, GetterFunction<ImmutableValue<?>> function) {
        this.keyValueMap.put(checkNotNull(key), checkNotNull(function));
    }

    /**
     * Simple registration method for the keys to field getter methods.
     *
     * <p>Note that this is still going to be usable, but will be made simpler
     * when Java 8 is used, as lambda expressions can refrence methods. The
     * update won't actually change these registration methods, but the
     * {@link DataManipulator}s calling these registration methods will
     * become single line simplifications.</p>
     *
     * @param key The key for the value return type
     * @param function The function for getting the field
     */
    protected final void registerFieldGetter(Key<?> key, GetterFunction<?> function) {
        this.keyFieldGetterMap.put(checkNotNull(key), checkNotNull(function));
    }

    protected abstract void registerGetters();

    @Override
    public <E> Optional<E> get(Key<? extends BaseValue<E>> key) {
        if (!supports(key)) {
            return Optional.absent();
        }
        return Optional.of((E) this.keyFieldGetterMap.get(key).get());
    }

    @Nullable
    @Override
    public <E> E getOrNull(Key<? extends BaseValue<E>> key) {
        checkArgument(supports(key));
        return get(key).orNull();
    }

    @Override
    public <E> E getOrElse(Key<? extends BaseValue<E>> key, E defaultValue) {
        checkArgument(supports(key));
        return get(key).or(checkNotNull(defaultValue, "Provided a null default value for 'getOrElse(Key, null)'!"));
    }

    @Override
    public <E, V extends BaseValue<E>> Optional<V> getValue(Key<V> key) {
        if (!this.keyValueMap.containsKey(key)) {
            return Optional.absent();
        }
        return Optional.of((V) checkNotNull(this.keyValueMap.get(key).get()));
    }

    @Override
    public boolean supports(Key<?> key) {
        return this.keyFieldGetterMap.containsKey(checkNotNull(key));
    }

    @Override
    public boolean supports(BaseValue<?> baseValue) {
        return this.keyFieldGetterMap.containsKey(checkNotNull(baseValue).getKey());
    }

    @Override
    public Set<Key<?>> getKeys() {
        return ImmutableSet.copyOf(this.keyValueMap.keySet());
    }

    @Override
    public Set<ImmutableValue<?>> getValues() {
        ImmutableSet.Builder<ImmutableValue<?>> builder = ImmutableSet.builder();
        for (GetterFunction<ImmutableValue<?>> function : this.keyValueMap.values()) {
            builder.add(checkNotNull(function.get()));
        }
        return builder.build();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.immutableClass);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ImmutableAbstractData other = (ImmutableAbstractData) obj;
        return Objects.equal(this.immutableClass, other.immutableClass);
    }

    @Override
    public Optional<I> with(BaseValue<?> value) {
        return with((Key) value.getKey(), value);
    }
}

