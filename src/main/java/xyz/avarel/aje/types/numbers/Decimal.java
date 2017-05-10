package xyz.avarel.aje.types.numbers;

import xyz.avarel.aje.AJEException;
import xyz.avarel.aje.types.Type;
import xyz.avarel.aje.types.Any;
import xyz.avarel.aje.types.NativeObject;
import xyz.avarel.aje.types.others.Truth;

import java.math.BigDecimal;

public class Decimal implements Any<Decimal>, NativeObject<Double> {
    public static final Type<Decimal> TYPE = new Type<>(Decimal.of(0), "decimal", Complex.TYPE);

    private final double value;

    public Decimal(double value) {
        this.value = value;
    }

    public static void assertIs(Object... objs) {
        for (Object a : objs) {

            if (!(a instanceof Decimal)) {
                throw new AJEException("Value needs to be a number.");
            }
        }
    }

    public static Decimal of(double value) {
        return new Decimal(value);
    }

    public double value() {
        return value;
    }

    @Override
    public Double toNative() {
        return value();
    }

    @Override
    public Type<Decimal> getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public Any castUp(Type type) {
        if (type.getPrototype() instanceof Decimal) {
            return this;
        } else if (type.getPrototype() instanceof Complex) {
            return Complex.of(value);
        }
        return this;
    }

    @Override
    public Any castDown(Type type) {
        if (type.getPrototype() instanceof Int) {
            return Int.of((int) value);
        }
        return this;
    }

    @Override
    public Decimal plus(Decimal other) {
        return Decimal.of(value + other.value);
    }

    @Override
    public Decimal minus(Decimal other) {
        return Decimal.of(value - other.value);
    }

    @Override
    public Decimal times(Decimal other) {
        return Decimal.of(value * other.value);
    }

    @Override
    public Decimal divide(Decimal other) {
        return Decimal.of(value / other.value);
    }

    @Override
    public Decimal pow(Decimal other) {
        return Decimal.of(Math.pow(value, other.value));
    }

    @Override
    public Decimal root(Decimal other) {
        if (other.value == 1) {
            return other;
        } else if (other.value == 2) {
            return Decimal.of(Math.sqrt(this.value));
        } else if (other.value == 3) {
            return Decimal.of(Math.cbrt(this.value));
        } else {
            double result = Math.pow(other.value, 1.0 / value);
            double val = BigDecimal.valueOf(result).setScale(7, BigDecimal.ROUND_HALF_EVEN).doubleValue();
            return Decimal.of(val);
        }
    }

    @Override
    public Decimal mod(Decimal other) {
        Decimal n = Decimal.of((value % other.value + other.value) % other.value);
        System.out.println(n);
        return n;
    }

    @Override
    public Decimal negative() {
        return Decimal.of(-value);
    }

    @Override
    public Truth equals(Decimal other) {
        return value == other.value ? Truth.TRUE : Truth.FALSE;
    }

    @Override
    public Truth greaterThan(Decimal other) {
        return value > other.value ? Truth.TRUE : Truth.FALSE;
    }

    @Override
    public Truth lessThan(Decimal other) {
        return value < other.value ? Truth.TRUE : Truth.FALSE;
    }

    @Override
    public Truth greaterThanOrEqual(Decimal other) {
        return value >= other.value ? Truth.TRUE : Truth.FALSE;
    }

    @Override
    public Truth lessThanOrEqual(Decimal other) {
        return value <= other.value ? Truth.TRUE : Truth.FALSE;
    }
}