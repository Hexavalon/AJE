package xyz.avarel.aje.parser;

import xyz.avarel.aje.AJEException;
import xyz.avarel.aje.parser.lexer.Token;
import xyz.avarel.aje.parser.lexer.TokenType;

import java.util.*;

public class Parser {
    private final Iterator<Token> lexer;
    private final List<Token> tokens;
    private final Grammar grammar;

    private Token last;

    public Parser(Iterator<Token> lexer) {
        this(lexer, new Grammar());
    }

    public Parser(Iterator<Token> lexer, Grammar grammar) {
        this.lexer = lexer;
        this.tokens = new ArrayList<>();
        this.grammar = grammar;
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

    public Map<TokenType, PrefixParser> getPrefixParsers() {
        return grammar.getPrefixParsers();
    }

    public Map<TokenType, InfixParser> getInfixParsers() {
        return grammar.getInfixParsers();
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
            throw error("Expected token " + expected + " but found " + token.getType());
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
        InfixParser parser = grammar.getInfixParsers().get(peek(0).getType());
        if (parser != null) return parser.getPrecedence();

        return 0;
    }


    public AJEException error(String message) {
        return error(message, getLast().getPos());
    }

    public AJEException error(String message, int position) {
        return new AJEException(message + ", position " + position + ".");
    }

}