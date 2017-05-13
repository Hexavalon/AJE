package xyz.avarel.aje.types;

import xyz.avarel.aje.AJEException;
import xyz.avarel.aje.types.numbers.Decimal;

import java.util.Arrays;
import java.util.List;

/**
 * An interface containing all natively implemented operations.
 * @param <T> AJE representation.
 */
@SuppressWarnings("unchecked")
public interface Any<T extends Any> {
    Type<Any> TYPE = new Type("any");

    Type<T> getType();

    default boolean isNativeObject() {
        return this instanceof NativeObject;
    }

    @SuppressWarnings("unchecked")
    default Object toNative() {
        return ((NativeObject<T>) this).toNative();
    }



    // Basic arithmetic
    default Any plus(T other) {
        return Undefined.VALUE;
    }

    default Any<?> minus(T other) {
        return Undefined.VALUE;
    }

    default Any<?> times(T other) {
        return Undefined.VALUE;
    }

    default Any<?> divide(T other) {
        return Undefined.VALUE;
    }

    default Any<?> mod(T other) {
        return Undefined.VALUE;
    }

    default Any<?> pow(T other) {
        return Undefined.VALUE;
    }

    default Any<?> negative() {
        return Undefined.VALUE;
    }


    default Any<?> plus(double other) {
        return plus((T) Decimal.of(other));
    }

    default Any<?> minus(double other) {
        return minus((T) Decimal.of(other));
    }

    default Any<?> times(double other) {
        return times((T) Decimal.of(other));
    }

    default Any<?> divide(double other) {
        return divide((T) Decimal.of(other));
    }

    default Any<?> mod(double other) {
        return mod((T) Decimal.of(other));
    }

    default Any<?> pow(double other) {
        return pow((T) Decimal.of(other));
    }


    // Boolean logic
    default Truth equals(T other) {
        return this == other ? Truth.TRUE : Truth.FALSE;
    }

    default Truth greaterThan(T other) {
        throw unsupported("greater than comparison");
    }

    default Truth lessThan(T other) {
        throw unsupported("less than comparison");
    }


    // Slices
    default Slice rangeTo(T other) {
        throw unsupported("range to");
    }



    // Functional
    default Any<?> invoke(List<Any> args) {
        return Undefined.VALUE;
    }

    default Any invoke(Any... arguments) {
        return invoke(Arrays.asList(arguments));
    }

    default Any<?> identity() {
        return this;
    }

    default Any<?> set(T other) {
        throw unsupported("set");
    }

    default Any<?> get(String name) {
        return Undefined.VALUE;
    }

    default RuntimeException unsupported(String desc) {
        return new AJEException(getType() + " does not support " + desc + ".");
    }
}
