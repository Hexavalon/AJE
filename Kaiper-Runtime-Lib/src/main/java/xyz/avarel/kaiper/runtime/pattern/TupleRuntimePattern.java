/*
 *  Copyright 2017 An Tran and Adrian Todt
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package xyz.avarel.kaiper.runtime.pattern;

import xyz.avarel.kaiper.runtime.Obj;

// a: is Int
// a: 2
// a: x
// a: (2, meme: 2, dank: 3)
public class TupleRuntimePattern extends RuntimePattern {
    private final Obj obj;

    public TupleRuntimePattern(String name, Obj obj) {
        super(name);
        this.obj = obj;
    }

    public Obj getObj() {
        return obj;
    }

    @Override
    public <R, C> R accept(RuntimePatternVisitor<R, C> visitor, C scope) {
        return visitor.visit(this, scope);
    }

    @Override
    public String toString() {
        return getName() + ": " + getObj();
    }

    @Override
    public int compareTo(RuntimePattern other) {
        int compare = super.compareTo(other);

        if (compare == 0
                && (!(other instanceof TupleRuntimePattern)
                || !obj.equals(((TupleRuntimePattern) other).obj))) {
            return -1; // put tuple patterns with different values on different levels, so it doesnt matter
        }

        return compare;
    }
}