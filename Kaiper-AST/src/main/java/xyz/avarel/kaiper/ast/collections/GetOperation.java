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

package xyz.avarel.kaiper.ast.collections;

import xyz.avarel.kaiper.ast.ExprVisitor;
import xyz.avarel.kaiper.ast.Single;
import xyz.avarel.kaiper.lexer.Position;

public class GetOperation extends Single {
    private final Single left;
    private final Single key;

    public GetOperation(Position position, Single left, Single key) {
        super(position);
        this.left = left;
        this.key = key;
    }

    public Single getLeft() {
        return left;
    }

    public Single getKey() {
        return key;
    }

    @Override
    public <R, C> R accept(ExprVisitor<R, C> visitor, C scope) {
        return visitor.visit(this, scope);
    }

    @Override
    public void ast(StringBuilder builder, String indent, boolean isTail) {
        builder.append(indent).append(isTail ? "└── " : "├── ").append("get");

        builder.append('\n');
        left.ast("target", builder, indent + (isTail ? "    " : "│   "), false);

        builder.append('\n');
        key.ast("key", builder, indent + (isTail ? "    " : "│   "), true);
    }
}
