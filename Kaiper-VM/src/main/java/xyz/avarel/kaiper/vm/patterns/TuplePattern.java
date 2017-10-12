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

package xyz.avarel.kaiper.vm.patterns;

// a: is Int
// a: 2
// a: x
// a: (2, meme: 2, dank: 3)
public class TuplePattern extends NamedPattern {
    private final Pattern pattern;

    public TuplePattern(String name, Pattern pattern) {
        super(name);
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        return getName() + ": " + pattern;
    }

    public Pattern getPattern() {
        return pattern;
    }
}