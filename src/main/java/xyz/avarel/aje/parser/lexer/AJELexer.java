package xyz.avarel.aje.parser.lexer;

import xyz.avarel.aje.AJEException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AJELexer implements Iterator<Token>, Iterable<Token> {
    private Reader reader;
    private List<Token> tokens;

    private Entry[] history;
    private int previous;
    
    private boolean init;
    private boolean eof;

    private long lineIndex;
    private long index;
    private long line;
    private char current;

    public AJELexer(InputStream inputStream) {
        this(new InputStreamReader(inputStream));
    }

    public AJELexer(String s) {
        this(new StringReader(s));
    }

    public AJELexer(Reader reader) {
        this(reader, 3);
    }

    public AJELexer(Reader reader, int historyBuffer) {
        this.reader = reader.markSupported()
                ? reader
                : new BufferedReader(reader);
        this.eof = false;
        this.tokens = new ArrayList<>();

        history = new Entry[historyBuffer];

        this.current = 0;
        this.index = 0;
        this.lineIndex = 0;
        this.line = 1;
    }

    /**
     * @return The next token.
     */
    @Override
    public Token next() {
        if (!init) lexTokens();
        if (tokens.isEmpty()) return readToken();
        return tokens.remove(0);
    }

    @Override
    public boolean hasNext() {
        return !(previous == 0 && this.eof);
    }

    @Override
    public Iterator<Token> iterator() {
        return this;
    }

    /**
     * Lex and remove useless tokens based on context.
     */
    private void lexTokens() {
        Token prev = null;

        do {
            Token next = readToken();

            if (prev != null) {
                if (tokens.size() == 0) {
                    switch (next.getType()) {
                        case SEMICOLON:
                        case LINE:
                            continue;
                    }
                }
                switch (prev.getType()) {
                    case LEFT_BRACE:
                    case LEFT_BRACKET:
                    case LEFT_PAREN:
                    case ARROW:
                    case COMMA:
                        switch (next.getType()) {
                            case LINE: // Remove next LINE token
                                continue;
                        }
                        break;
                    case LINE: // Remove previous LINE token if followed by:
                        switch (next.getType()) {
                            case RIGHT_BRACE:
                            case RIGHT_BRACKET:
                            case RIGHT_PAREN:
                            case COMMA:
                            case SEMICOLON:
                            case PIPE_FORWARD:
                            case LINE:
                                tokens.remove(prev);
                        }
                    case SEMICOLON:
                        switch (next.getType()) {
                            case SEMICOLON:
                                continue;
                        }
                }
            }

            switch (next.getType()) {
                case SKIP:
                    continue;
            }

            tokens.add(next);

            prev = next;
        } while (hasNext());

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        init = true;
    }

    private Token readToken() {

        char c = advance();

        while (Character.isSpaceChar(c)) c = advance();

        switch (c) {
            case '(': return make(TokenType.LEFT_PAREN);
            case ')': return make(TokenType.RIGHT_PAREN);

            case '[': return make(TokenType.LEFT_BRACKET);
            case ']': return make(TokenType.RIGHT_BRACKET);

            case '{': return make(TokenType.LEFT_BRACE);
            case '}': return make(TokenType.RIGHT_BRACE);

            case '_': return make(TokenType.UNDERSCORE);

            case '.': return match('.')
                    ? make(TokenType.RANGE_TO)
                    : make(TokenType.DOT);
            case ',': return make(TokenType.COMMA);
            case '!': return match('=')
                    ? make(TokenType.NOT_EQUAL)
                    : make(TokenType.BANG);
            case '?': return make(TokenType.QUESTION);
            case '~': return make(TokenType.TILDE);

            case '+': return make(TokenType.PLUS);
            case '-': return match('>')
                    ? make(TokenType.ARROW)
                    : make(TokenType.MINUS);
            case '*': return make(TokenType.ASTERISK);
            case '/': return match('\\')
                    ? make(TokenType.AND)
                    : match('/')
                    ? nextComment()
                    : make(TokenType.SLASH);
            case '%': return make(TokenType.PERCENT);
            case '^': return make(TokenType.CARET);

            case '\\': return match('/')
                    ? make(TokenType.OR)
                    : make(TokenType.BACKSLASH);

            case ':': return make(TokenType.COLON);

            case '=': return match('=')
                    ? make(TokenType.EQUALS)
                    : make(TokenType.ASSIGN);
            case '>': return match('=')
                    ? make(TokenType.GTE)
                    : make(TokenType.GT);
            case '<': return match('=')
                    ? make(TokenType.LTE)
                    : make(TokenType.LT);
            case '|': return match('|')
                    ? make(TokenType.OR)
                    : match('>')
                    ? make(TokenType.PIPE_FORWARD)
                    : make(TokenType.VERTICAL_BAR);
            case '&': return match('&')
                    ? make(TokenType.AND)
                    : make(TokenType.AMPERSAND);

            case ';':
                match('\r');
                match('\n');
                return make(TokenType.SEMICOLON);

            case '\r': match('\n');
            case '\n': return make(TokenType.LINE);

            case '\0': return make(TokenType.EOF);

            case (char) -1: return make(TokenType.EOF);

            default:
                if (Character.isDigit(c)) {
                    return nextNumber(c);
                } else if (Character.isLetter(c)) {
                    return nextName(c);
                } else {
                    if (hasNext()) return make(TokenType.EOF);
                    throw syntaxError("Unrecognized `" + c + "`");
                }
        }
    }

    private Token nextComment() {
        while (peek() != '\n') {
            advance();
        }
        return make(TokenType.SKIP);
    }

    private Token nextNumber(char init) {
        boolean point = false;

        char c;

        StringBuilder sb = new StringBuilder();
        sb.append(init);

        while (true) {
            c = advance();

            if (Character.isDigit(c)) {
                sb.append(c);
            } else switch (c) {
                case 'i':
                    back();
                    return make(TokenType.DECIMAL, sb.toString());
                case '.':
                    if (point) {
                        back();
                        return make(TokenType.DECIMAL, sb.toString());
                    }

                    if (!Character.isDigit(peek())) {
                        back();
                        return make(TokenType.INT, sb.toString());
                    }

                    sb.append(c);
                    point = true;
                    break;
                case '_':
                    break;
                default:
                    back();
                    if (Character.isAlphabetic(peek())) {
                        queue('*');
                    }
                    if (point) {
                        return make(TokenType.DECIMAL, sb.toString());
                    } else {
                        return make(TokenType.INT, sb.toString());
                    }
            }
        }
    }

    private Token nextName(char init) {
        StringBuilder sb = new StringBuilder();
        sb.append(init);

        char c;
        while (true) {
            c = advance();

            if (Character.isLetterOrDigit(c)) {
                sb.append(c);
            } else {
                break;
            }
        }

        back();

        return nameOrKeyword(sb.toString());
    }

    private Token nameOrKeyword(String value) {
        switch (value) {
            case "var": return make(TokenType.VAR);
            case "func": return make(TokenType.FUNCTION);
            case "true": return make(TokenType.BOOLEAN, "true");
            case "false": return make(TokenType.BOOLEAN, "false");
            case "i": return make(TokenType.IMAGINARY);
            case "and": return make(TokenType.AND);
            case "or": return make(TokenType.OR);
            default: return make(TokenType.NAME, value);
        }
    }

    private Token make(TokenType type) {
        return make(new Position(index, line, lineIndex), type);
    }

    private Token make(TokenType type, String value) {
        return make(new Position(index, line, lineIndex - value.length()), type, value);
    }

    private Token make(Position position, TokenType type) {
        return new Token(position, type);
    }

    private Token make(Position position, TokenType type, String value) {
        return new Token(position, type, value);
    }

    // useful for lexer-phase desugaring
    private void queue(char character) {
        System.arraycopy(history, previous + 1, history, 3, history.length - 3);
        history[previous] = new Entry(index, line, lineIndex, character);
        back();
    }

    /**
     * Get the readToken character in the source string.
     *
     * @return The readToken character, or 0 if past the end of the source string.
     */
    private char advance() {
        int c;
        if (this.previous != 0) {
            this.previous--;

            Entry entry = history[previous];

            current = entry.character;
            index = entry.index;
            line = entry.line;
            lineIndex = entry.lineIndex;

            return this.current;
        } else {
            try {
                c = this.reader.read();
            } catch (IOException exception) {
                throw syntaxError("Exception occurred while lexing", exception);
            }

            if (c <= 0) { // End of stream
                this.eof = true;
                c = 0;
            }
        }
        this.index += 1;
        if (this.current == '\r') {
            this.line += 1;
            this.lineIndex = c == '\n' ? 0 : 1;
        } else if (c == '\n') {
            this.line += 1;
            this.lineIndex = 0;
        } else {
            this.lineIndex += 1;
        }
        this.current = (char) c;

        System.arraycopy(history, 0, history, 1, history.length - 1);

        history[0] = new Entry(index, line, lineIndex, current);

        return this.current;
    }


    /**
     * Consume the next character, and check that
     * it matches a specified character.
     *
     * @param c The character to match.
     * @return The character.
     */
    private char advance(char c) {
        char n = this.advance();
        if (n != c) {
            throw this.syntaxError("Expected '" + c + "' and instead saw '" + n + "'");
        }
        return n;
    }


    /**
     * Get the next n characters.
     *
     * @param n The number of characters to take.
     * @return A string of n characters.
     * @throws AJEException Substring bounds error if there are not
     *                      n characters remaining in the source string.
     */
    private String advance(int n) {
        if (n == 0) {
            return "";
        }

        char[] chars = new char[n];
        int pos = 0;

        while (pos < n) {
            chars[pos] = this.advance();
            if (this.hasNext()) {
                throw this.syntaxError("Substring bounds error");
            }
            pos += 1;
        }
        return new String(chars);
    }


    /**
     * Get the next char in the string, skipping whitespace.
     *
     * @return A character, or 0 if there are no more characters.
     */
    private char advanceClean() {
        while (true) {
            char c = this.advance();
            if (c == 0 || c > ' ') {
                return c;
            }
        }
    }

    /**
     * @return The next character.
     */
    private char peek() {
        char c = advance();
        back();
        return c;
    }

    /**
     * Peek and advance if the prompt is the same as the peeked character.
     *
     * @param prompt The character to match.
     * @return if the prompt is the same as the peeked character.
     */
    private boolean match(char prompt) {
        if (advance() == prompt) {
            return true;
        }
        back();
        return false;
    }

    /**
     * Back up one character. This provides a sort of lookahead capability,
     * so that you can test for a digit or letter before attempting to parse
     * the readToken number or identifier.
     */
    private void back() {
        previous++;
    }

    /**
     * Get the text up but not including the specified character or the
     * end of line, whichever comes first.
     *
     * @param delimiter A delimiter character.
     * @return A string.
     */
    private String advanceTo(char delimiter) {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = this.advance();
            if (c == delimiter || c == 0 || c == '\n' || c == '\r') {
                if (c != 0) {
                    this.back();
                }
                return sb.toString().trim();
            }
            sb.append(c);
        }
    }


    /**
     * Get the text up but not including one of the specified delimiter
     * characters or the end of line, whichever comes first.
     *
     * @param delimiters A set of delimiter characters.
     * @return A string, trimmed.
     */
    private String advanceTo(String delimiters) {
        char c;
        StringBuilder sb = new StringBuilder();
        while (true) {
            c = this.advance();
            if (delimiters.indexOf(c) >= 0 || c == 0 ||
                    c == '\n' || c == '\r') {
                if (c != 0) {
                    this.back();
                }
                return sb.toString().trim();
            }
            sb.append(c);
        }
    }


    /**
     * Skip characters until the readToken character is the requested character.
     * If the requested character is not found, no characters are skipped.
     *
     * @param to A character to skip to.
     * @return The requested character, or zero if the requested character
     * is not found.
     */
    private char skipTo(char to) {
        char c;
        try {
            long startIndex = this.index;
            long startCharacter = this.lineIndex;
            long startLine = this.line;
            this.reader.mark(1000000);
            do {
                c = this.advance();
                if (c == 0) {
                    this.reader.reset();
                    this.index = startIndex;
                    this.lineIndex = startCharacter;
                    this.line = startLine;
                    return c;
                }
            } while (c != to);
        } catch (IOException exception) {
            throw syntaxError("Exception occurred while lexing", exception);
        }
        this.back();
        return c;
    }


    /**
     * Make an AJEException to signal a syntax error.
     *
     * @param message The error message.
     * @return A AJEException object, suitable for throwing
     */
    private AJEException syntaxError(String message) {
        return new AJEException(message + this.toString());
    }

    /**
     * Make an AJEException to signal a syntax error.
     *
     * @param message  The error message.
     * @param causedBy The throwable that caused the error.
     * @return A AJEException object, suitable for throwing
     */
    private AJEException syntaxError(String message, Throwable causedBy) {
        return new AJEException(message + this.toString(), causedBy);
    }

    public String tokensToString() {
        if (!init) lexTokens();
        return tokens.toString();
    }

    /**
     * Make a printable string of this AJELexer.
     *
     * @return " at {index} [character {character} line {line}]"
     */
    @Override
    public String toString() {
        return " at " + this.index + " [line " + this.line + " : char " + this.lineIndex + "]";
    }

    private static final class Entry {
        private final long index;
        private final long line;
        private final long lineIndex;
        private final char character;

        private Entry(long index, long line, long lineIndex, char character) {
            this.index = index;
            this.line = line;
            this.lineIndex = lineIndex;
            this.character = character;
        }

        @Override
        public String toString() {
            return String.valueOf(character);
        }
    }
}