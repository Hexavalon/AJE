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

package xyz.avarel.kaiper.ast;

import xyz.avarel.kaiper.ast.flow.Statements;
import xyz.avarel.kaiper.lexer.Position;

public abstract class Expr {
    private final Position position;

    protected Expr(Position position) {
        this.position = position;
    }

    public abstract <R, C> R accept(ExprVisitor<R, C> visitor, C scope);

    public Expr andThen(Expr after) {
        return new Statements(this, after);
    }

    public Position getPosition() {
        return position;
    }

    public void ast(StringBuilder builder, String indent, boolean isTail) {
        builder.append(indent).append(isTail ? "└── " : "├── ").append(toString());
    }

    public void ast(String label, StringBuilder builder, String indent, boolean tail) {
        builder.append(indent).append(tail ? "└── " : "├── ").append(label).append(':');

        builder.append('\n');
        ast(builder, indent + (tail ? "    " : "│   "), true);
    }
}
