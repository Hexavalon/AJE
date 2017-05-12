package xyz.avarel.aje.parser;

import xyz.avarel.aje.AJEException;
import xyz.avarel.aje.parser.lexer.Token;
import xyz.avarel.aje.parser.lexer.TokenType;

import java.util.*;

public abstract class Parser {
    private final Iterator<Token> lexer;
    private final List<Token> tokens = new ArrayList<>();
    private final Map<TokenType, PrefixParser> prefixParsers = new HashMap<>();
    private final Map<TokenType, InfixParser> infixParsers = new HashMap<>();
    private Token last;

    public Parser(Iterator<Token> lexer) {
        this.lexer = lexer;
    }

    public void register(TokenType token, PrefixParser parselet) {
        prefixParsers.put(token, parselet);
    }

    public void register(TokenType token, InfixParser parselet) {
        infixParsers.put(token, parselet);
    }

    public Map<TokenType, PrefixParser> getPrefixParsers() {
        return prefixParsers;
    }

    public Map<TokenType, InfixParser> getInfixParsers() {
        return infixParsers;
    }

    public Token getLast() {
        return last;
    }

    public boolean match(TokenType expected) {
        Token token = peek(0);
        if (token.getType() != expected) {
            return false;
        }
        eat();
        return true;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public Iterator<Token> getLexer() {
        return lexer;
    }

    public Token eat(TokenType expected) {
        Token token = peek(0);
        if (token.getType() != expected) {
            throw new AJEException("Expected token " + expected + " but found " + token.getType());
        }
        return eat();
    }

    public Token eat() {
        // Make sure we've read the token.
        peek(0);

        return last = tokens.remove(0);
    }

    public Token peek(int distance) {
        // Read in as many as needed.
        while (distance >= tokens.size()) {
            tokens.add(lexer.next());
        }

        // Get the queued token.
        return tokens.get(distance);
    }

    public int getPrecedence() {
        InfixParser parser = infixParsers.get(peek(0).getType());
        if (parser != null) return parser.getPrecedence();

        return 0;
    }
}
