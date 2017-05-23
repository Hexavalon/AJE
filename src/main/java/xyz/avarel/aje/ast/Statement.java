package xyz.avarel.aje.ast;

import xyz.avarel.aje.runtime.Obj;
import xyz.avarel.aje.runtime.pool.Scope;

public class Statement implements Expr {
    private final Expr before;
    private final Expr after;

    private boolean hasNext;

    public Statement(Expr before, Expr after) {
        this.before = before;
        this.after = after;

        if (before instanceof Statement) {
            ((Statement) before).hasNext = true;
        }
    }

    public Expr getBefore() {
        return before;
    }

    public Expr getAfter() {
        return after;
    }

    @Override
    public Obj accept(ExprVisitor visitor, Scope scope) {
        return visitor.visit(this, scope);
    }

    @Override
    public void ast(StringBuilder builder, String prefix, boolean isTail) {
        before.ast(builder, prefix, false);
        builder.append('\n');
        after.ast(builder, prefix, !hasNext);
    }
}
