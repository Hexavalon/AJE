package xyz.avarel.kaiper.ast.variables;

import xyz.avarel.kaiper.ast.Expr;
import xyz.avarel.kaiper.ast.ExprVisitor;
import xyz.avarel.kaiper.ast.Single;
import xyz.avarel.kaiper.ast.pattern.PatternCase;
import xyz.avarel.kaiper.lexer.Position;

public class BindAssignmentExpr extends Expr {
    private final PatternCase patternCase;
    private final Single expr;

    public BindAssignmentExpr(Position position, PatternCase patternCase, Single expr) {
        super(position);
        this.patternCase = patternCase;
        this.expr = expr;
    }

    public PatternCase getPatternCase() {
        return patternCase;
    }

    public Single getExpr() {
        return expr;
    }

    @Override
    public <R, C> R accept(ExprVisitor<R, C> visitor, C scope) {
        return visitor.visit(this, scope);
    }

    @Override
    public void ast(StringBuilder builder, String indent, boolean isTail) {
        builder.append(indent).append(isTail ? "└── " : "├── ").append("declare");

        builder.append('\n');
        builder.append(indent).append(isTail ? "    " : "│   ").append("├── pattern ").append(patternCase);

        builder.append('\n');
        expr.ast(builder, indent + (isTail ? "    " : "│   "), true);
    }

    @Override
    public String toString() {
        return patternCase + " = " + expr;
    }
}