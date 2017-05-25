package xyz.avarel.aje.ast.operations;

import xyz.avarel.aje.ast.Expr;
import xyz.avarel.aje.ast.ExprVisitor;
import xyz.avarel.aje.runtime.Obj;
import xyz.avarel.aje.runtime.pool.Scope;

public class RangeExpr implements Expr {
    private final Expr left;
    private final Expr right;
    private final boolean exclusive;

    public RangeExpr(Expr left, Expr right, boolean exclusive) {
        this.left = left;
        this.right = right;
        this.exclusive = exclusive;
    }

    public Expr getLeft() {
        return left;
    }

    public Expr getRight() {
        return right;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    @Override
    public Obj accept(ExprVisitor visitor, Scope scope) {
        return visitor.visit(this, scope);
    }

    @Override
    public void ast(StringBuilder builder, String prefix, boolean isTail) {
        builder.append(prefix).append(isTail ? "└── " : "├── ").append("range").append(exclusive ? " exclusive" : " inclusive").append('\n');
        left.ast(builder, prefix + (isTail ? "    " : "│   "), false);
        builder.append('\n');
        right.ast(builder, prefix + (isTail ? "    " : "│   "), true);
    }
}
