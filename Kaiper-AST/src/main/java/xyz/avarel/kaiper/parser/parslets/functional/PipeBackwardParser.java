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

package xyz.avarel.kaiper.parser.parslets.functional;

import xyz.avarel.kaiper.Precedence;
import xyz.avarel.kaiper.ast.Expr;
import xyz.avarel.kaiper.ast.Single;
import xyz.avarel.kaiper.ast.functions.FunctionNode;
import xyz.avarel.kaiper.ast.invocation.Invocation;
import xyz.avarel.kaiper.ast.tuples.TupleEntry;
import xyz.avarel.kaiper.ast.tuples.TupleExpr;
import xyz.avarel.kaiper.ast.variables.Identifier;
import xyz.avarel.kaiper.exceptions.SyntaxException;
import xyz.avarel.kaiper.lexer.Token;
import xyz.avarel.kaiper.parser.BinaryParser;
import xyz.avarel.kaiper.parser.KaiperParser;

import java.util.ArrayList;
import java.util.List;

public class PipeBackwardParser extends BinaryParser {
    public PipeBackwardParser() {
        super(Precedence.INFIX);
    }

    @Override
    public Expr parse(KaiperParser parser, Single left, Token token) {
        if (!parser.getParserFlags().allowInvocation()) {
            throw new SyntaxException("Function invocation are disabled");
        }

        Single right = parser.parseSingle(getPrecedence() - 1);

        if (left instanceof Invocation) {
            Single argument = ((Invocation) left).getArgument();

            TupleExpr tuple = argument instanceof TupleExpr ? (TupleExpr) argument : new TupleExpr(argument);
            List<TupleEntry> entries = new ArrayList<>(tuple.getEntries());

            for (int i = 0; i < entries.size(); i++) {
                TupleEntry entry = entries.get(i);
                if (entry.getName().equals("_" + i)) {
                    entries.set(i, new TupleEntry(entry.getPosition(), "_" + (i + 1), entry.getExpr()));
                }
            }

            entries.add(0, new TupleEntry(right.getPosition(), "_0", right));

            return new Invocation(token.getPosition(), left, new TupleExpr(tuple.getPosition(), entries));
        } else if (left instanceof FunctionNode || left instanceof Identifier) {
            return new Invocation(token.getPosition(), left, right);
        }

        throw new SyntaxException(
                "Pipe-backward requires the left operand to be either: invocation, function, or name",
                token.getPosition());
    }
}
