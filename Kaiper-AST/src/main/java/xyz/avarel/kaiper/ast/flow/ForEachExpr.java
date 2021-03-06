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

package xyz.avarel.kaiper.ast.flow;

import xyz.avarel.kaiper.ast.Expr;
import xyz.avarel.kaiper.ast.ExprVisitor;
import xyz.avarel.kaiper.ast.Single;
import xyz.avarel.kaiper.lexer.Position;

public class ForEachExpr extends Single {
    private final String variant;
    private final Single iterable;
    private final Expr action;

    public ForEachExpr(Position position, String variant, Single iterable, Expr action) {
        super(position);
        this.variant = variant;
        this.iterable = iterable;
        this.action = action;
    }

    public String getVariant() {
        return variant;
    }

    public Single getIterable() {
        return iterable;
    }

    public Expr getAction() {
        return action;
    }

    @Override
    public <R, C> R accept(ExprVisitor<R, C> visitor, C scope) {
        return visitor.visit(this, scope);
    }

    @Override
    public void ast(StringBuilder builder, String indent, boolean isTail) {
        builder.append(indent).append(isTail ? "└── " : "├── ").append("for each");

        builder.append('\n');
        builder.append(indent).append(isTail ? "    " : "│   ").append("├── variant: ").append(variant);

        builder.append('\n');
        iterable.ast("iterable", builder, indent + (isTail ? "    " : "│   "), false);

        builder.append('\n');
        action.ast("action", builder, indent + (isTail ? "    " : "│   "), true);
    }
}
