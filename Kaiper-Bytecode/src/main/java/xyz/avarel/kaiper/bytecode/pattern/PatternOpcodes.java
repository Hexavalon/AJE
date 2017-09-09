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

package xyz.avarel.kaiper.bytecode.pattern;

import xyz.avarel.kaiper.bytecode.DataOutputConsumer;
import xyz.avarel.kaiper.exceptions.InvalidBytecodeException;

import java.io.DataOutput;
import java.io.IOException;

/**
 * The Bytecode Instructions of the Kaiper Patterns.
 *
 * @author AdrianTodt
 * @version 2.0
 */
public enum PatternOpcodes implements DataOutputConsumer {
    END,

    PATTERN_CASE,
    WILDCARD,
    VARIABLE,
    TUPLE,
    REST,

    VALUE,
    DEFAULT;

    public static PatternOpcodes byId(int id) {
        PatternOpcodes[] values = values();
        if (id < values.length) return values[id];
        throw new InvalidBytecodeException("Invalid Instruction");
    }

    @Override
    public void writeInto(DataOutput out) throws IOException {
        out.writeByte(ordinal());
    }
}
