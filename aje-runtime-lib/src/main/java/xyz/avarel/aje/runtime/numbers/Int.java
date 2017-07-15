/*
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package xyz.avarel.aje.runtime.numbers;

import xyz.avarel.aje.runtime.Bool;
import xyz.avarel.aje.runtime.Obj;
import xyz.avarel.aje.runtime.Undefined;
import xyz.avarel.aje.runtime.types.NativeConstructor;
import xyz.avarel.aje.runtime.types.Type;

import java.util.List;

public class Int implements Obj<Integer> {
    public static final Type<Int> TYPE = new IntType();

    private final int value;

    private Int(int value) {
        this.value = value;
    }

    public static Int of(int i) {
        if (i >= IntCache.LOW && i <= IntCache.HIGH) {
            return IntCache.cache[i - IntCache.LOW];
        }
        return new Int(i);
    }

    public int value() {
        return value;
    }

    @Override
    public Integer toJava() {
        return value();
    }

    @Override
    public Type<Int> getType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Obj) {
            return isEqualTo((Obj) obj) == Bool.TRUE;
        } else {
            return Integer.valueOf(value) == obj;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public Obj plus(Obj other) {
        if (other instanceof Int) {
            return this.plus((Int) other);
        }
        return Undefined.VALUE;
    }

    private Int plus(Int other) {
        return Int.of(value + other.value);
    }

    @Override
    public Obj minus(Obj other) {
        if (other instanceof Int) {
            return this.minus((Int) other);
        }
        return Undefined.VALUE;
    }

    private Int minus(Int other) {
        return Int.of(value - other.value);
    }

    @Override
    public Obj times(Obj other) {
        if (other instanceof Int) {
            return this.times((Int) other);
        }
        return Undefined.VALUE;
    }

    private Int times(Int other) {
        return Int.of(value * other.value);
    }

    @Override
    public Obj divide(Obj other) {
        if (other instanceof Int) {
            return this.divide((Int) other);
        }
        return Undefined.VALUE;
    }

    private Obj divide(Int other) {
        if (other.value == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return Int.of(value / other.value);
    }

    @Override
    public Obj pow(Obj other) {
        if (other instanceof Int) {
            return this.pow((Int) other);
        }
        return Undefined.VALUE;
    }

    private Int pow(Int other) {
        return Int.of((int) Math.pow(value, other.value));
    }

    @Override
    public Obj mod(Obj other) {
        if (other instanceof Int) {
            return this.mod((Int) other);
        }
        return Undefined.VALUE;
    }

    private Int mod(Int other) {
        return Int.of(Math.floorMod(value, other.value));
    }

    @Override
    public Int negative() {
        return Int.of(-value);
    }

    @Override
    public Obj isEqualTo(Obj other) {
        if (other instanceof Int) {
            return this.isEqualTo((Int) other);
        }
        return Bool.FALSE;
    }

    private Obj isEqualTo(Int other) {
        return Bool.of(value == other.value);
    }

    @Override
    public Obj greaterThan(Obj other) {
        if (other instanceof Int) {
            return this.greaterThan((Int) other);
        }
        return Bool.FALSE;
    }

    private Obj greaterThan(Int other) {
        return Bool.of(value > other.value);
    }

    @Override
    public Obj lessThan(Obj other) {
        if (other instanceof Int) {
            return this.lessThan((Int) other);
        }
        return Bool.FALSE;
    }

    private Obj lessThan(Int other) {
        return Bool.of(value < other.value);
    }

    private static class IntCache {
        private static final int LOW = -128;
        private static final int HIGH = 127;
        private static final Int[] cache;

        static {
            cache = new Int[(HIGH - LOW) + 1];
            int counter = LOW;
            for (int i = 0; i < cache.length; i++) {
                cache[i] = new Int(counter++);
            }
        }

        private IntCache() {}
    }

    private static class IntType extends Type<Int> {
        public IntType() {
            super(Decimal.TYPE, "Int", new NativeConstructor(Obj.TYPE) {
                @Override
                protected Obj eval(List<Obj> arguments) {
                    Obj obj = arguments.get(0);
                    if (obj instanceof Int) {
                        return obj;
                    } else if (obj instanceof Decimal) {
                        return Int.of((int) ((Decimal) obj).value());
                    }
                    try {
                        return Int.of(Integer.parseInt(obj.toString()));
                    } catch (NumberFormatException e) {
                        return Undefined.VALUE;
                    }
                }
            });

            getScope().declare("MAX_VALUE", Int.of(Integer.MAX_VALUE));
            getScope().declare("MIN_VALUE", Int.of(Integer.MIN_VALUE));
            getScope().declare("BYTES", Int.of(Integer.BYTES));
            getScope().declare("SIZE", Int.of(Integer.SIZE));
        }
    }
}