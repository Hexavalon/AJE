package xyz.avarel.aje.parser.parslets;

import xyz.avarel.aje.Precedence;
import xyz.avarel.aje.ast.Expr;
import xyz.avarel.aje.ast.atoms.UndefAtom;
import xyz.avarel.aje.ast.variables.DeclarationExpr;
import xyz.avarel.aje.parser.AJEParser;
import xyz.avarel.aje.parser.PrefixParser;
import xyz.avarel.aje.parser.lexer.Token;
import xyz.avarel.aje.parser.lexer.TokenType;

public class DeclarationParser implements PrefixParser {
    @Override
    public Expr parse(AJEParser parser, Token token) {
        Token name = parser.eat(TokenType.NAME);

        if (parser.match(TokenType.ASSIGN)) {
            return new DeclarationExpr(name.getText(), parser.parseExpr(Precedence.ASSIGNMENT));
        }

        return new DeclarationExpr(name.getText(), UndefAtom.VALUE);
    }
}