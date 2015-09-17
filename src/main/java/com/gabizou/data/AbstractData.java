package com.gabizou.data;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.gabizou.util.GetterFunction;
import com.gabizou.util.SetterFunction;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

@SuppressWarnings("unchecked")
public abstract class AbstractData<M extends AbstractData<M, I>, I extends ImmutableAbstractData<I, M>> implements DataManipulator<M, I> {

    private final Class<M> manipulatorClass;

    private final Map<Key<?>, GetterFunction<Value<?>>> keyValueMap = Maps.newHashMap();
    private final Map<Key<?>, GetterFunction<?>> keyFieldGetters = Maps.newHashMap();
    private final Map<Key<?>, SetterFunction<Object>> keyFieldSetters = Maps.newHashMap();

    public AbstractData(Class<M> manipulatorClass) {
        this.manipulatorClass = checkNotNull(manipulatorClass);
        registerGettersAndSetters();
    }

    /**
     * This is intentionally left abstract and is called in the constructure. Due
     * to object limitations of the game,
     */
    protected abstract void registerGettersAndSetters();

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
    protected final void registerKeyValue(Key<?> key, GetterFunction<Value<?>> function) {
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
        this.keyFieldGetters.put(checkNotNull(key), checkNotNull(function));
    }

    /**
     * Simple registration method for the keys to field setter methods.
     *
     * <p>Note that this is still going to be usable, but will be made simpler
     * when Java 8 is used, as lambda expressions can refrence methods. The
     * update won't actually change these registration methods, but the
     * {@link DataManipulator}s calling these registration methods will
     * become single line simplifications.</p>
     *
     * @param key The key for the value return type
     * @param function The function for setting the field
     */
    protected final void registerFieldSetter(Key<?> key, SetterFunction<Object> function) {
        this.keyFieldSetters.put(checkNotNull(key), checkNotNull(function));
    }


    // Beyond this point is all implementation with the getter/setter functions!

    @Override
    public Optional<M> fill(DataHolder dataHolder) {
        return fill(dataHolder, MergeFunction.IGNORE_ALL);
    }

    @Override
    public <E> M set(Key<? extends BaseValue<E>> key, E value) {
        checkArgument(supports(key), "This data manipulator doesn't support the following key: " + key.toString());
        this.keyFieldSetters.get(key).set(value);
        return (M) this;
    }

    @Override
    public M set(BaseValue<?> value) {
        checkArgument(supports(value), "This data manipulator doesn't support the following key: " + value.getKey().toString());
        this.keyFieldSetters.get(value.getKey()).set(value.get());
        return (M) this;
    }

    @Override
    public M set(BaseValue<?>... values) {
        for (BaseValue<?> value : checkNotNull(values)) {
            try {
                set(value);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return (M) this;
    }

    @Override
    public M set(Iterable<? extends BaseValue<?>> values) {
        for (BaseValue<?> value : checkNotNull(values)) {
            try {
                set(value);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return (M) this;
    }

    @Override
    public <E> M transform(Key<? extends BaseValue<E>> key, Function<E, E> function) {
        checkArgument(supports(key));
        this.keyFieldSetters.get(key).set(checkNotNull(function.apply((E) this.keyFieldGetters.get(key).get())));
        return (M) this;
    }

    @Override
    public <E> Optional<E> get(Key<? extends BaseValue<E>> key) {
        if (!supports(key)) {
            return Optional.absent();
        }
        return Optional.of((E) this.keyFieldGetters.get(key).get());
    }

    @Nullable
    @Override
    public <E> E getOrNull(Key<? extends BaseValue<E>> key) {
        return get(key).orNull();
    }

    @Override
    public <E> E getOrElse(Key<? extends BaseValue<E>> key, E defaultValue) {
        return get(key).or(checkNotNull(defaultValue));
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
        return this.keyFieldSetters.containsKey(checkNotNull(key));
    }

    @Override
    public boolean supports(BaseValue<?> baseValue) {
        return this.keyFieldSetters.containsKey(checkNotNull(baseValue.getKey()));
    }

    @Override
    public Set<Key<?>> getKeys() {
        return ImmutableSet.copyOf(this.keyFieldSetters.keySet());
    }

    @Override
    public Set<ImmutableValue<?>> getValues() {
        ImmutableSet.Builder<ImmutableValue<?>> builder = ImmutableSet.builder();
        for (GetterFunction<Value<?>> function : this.keyValueMap.values()) {
            builder.add(checkNotNull(function.get()).asImmutable());
        }
        return builder.build();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.manipulatorClass);
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
        final AbstractData other = (AbstractData) obj;
        return Objects.equal(this.manipulatorClass, other.manipulatorClass);
    }

}
