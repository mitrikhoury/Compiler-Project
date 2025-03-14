package project;

import java.util.*;

class Lexer {
    private final String input;
    private int pos;
    private int line;
    private final int length;

    /**
     * Constructor for Lexer
     * @param input input string (source code)
     */
    public Lexer(String input) {
        this.input = input;
        this.pos = 0;
        this.line = 1;
        this.length = input.length();
    }
    private static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("exit", TokenType.EXIT);
        keywords.put("include", TokenType.INCLUDE);
        keywords.put("const", TokenType.CONST);
        keywords.put("var", TokenType.VAR);
        keywords.put("int", TokenType.INT);
        keywords.put("float", TokenType.FLOUT);
        keywords.put("char", TokenType.CHAR);
        keywords.put("function", TokenType.FUNCTION);
        keywords.put("newb", TokenType.NEWB);
        keywords.put("endb", TokenType.ENDB);
        keywords.put("mod", TokenType.MOD);
        keywords.put("div", TokenType.DIV);
        keywords.put("cin", TokenType.CIN);
        keywords.put("cout", TokenType.COUT);
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("while", TokenType.WHILE);
        keywords.put("repeat", TokenType.REPEAT);
        keywords.put("until", TokenType.UNTIL);
        keywords.put("call", TokenType.CALL);      
    }

    /**
     * Get the current character
     * @return the current character
     */
    private char currentChar() {
        return pos < length ? input.charAt(pos) : '\0';
    }

    /**
     * Advance the position
     */
    private void advance() {
    	 if (pos < length) {
             if (currentChar() == '\n') {
                 line++; // Increment line when encountering a newline
             }
             pos++;
         }
    }

    /**
     * Skip the whitespace characters
     */
    private void skipWhitespace() {
        while (pos < length && Character.isWhitespace(currentChar())) {
            advance();
        }
    }

    /**
     * Get the next token
     * @return the next token
     */
    public Token nextToken() {
        // Ignore the whitespaces
        skipWhitespace();
        // Check if the position is at the end of the input
        if (pos >= length) return new Token(TokenType.EOF, "" ,line);

        char current = currentChar();

        // Check if the current character is a letter (identifier)
        if (Character.isLetter(current)) {
            StringBuilder sb = new StringBuilder();
            int tokenLine = line;
            while (pos < length && (Character.isLetter(currentChar()) || Character.isDigit(currentChar()))) {
                sb.append(currentChar());
                advance();
            }
            String value = sb.toString();

            return new Token(keywords.getOrDefault(value, TokenType.IDENTIFIER), value ,tokenLine);
        }

        // Check if the current character is a digit (integer or real value)
        if (Character.isDigit(current)) {
            StringBuilder sb = new StringBuilder();
            int tokenLine = line;
            while (pos < length && Character.isDigit(currentChar())) {
                sb.append(currentChar());
                advance();
            }
            if (currentChar() == '.') {
                sb.append('.');
                advance();
                while (pos < length && Character.isDigit(currentChar())) {
                    sb.append(currentChar());
                    advance();
                }
                return new Token(TokenType.FLOUT, sb.toString() ,tokenLine);
            }
            return new Token(TokenType.INT, sb.toString() ,tokenLine);
        }

        // Check for the symbols and operators
        switch (current) {
            case ':':
                advance();
                if (currentChar() == '=') {
                    advance();
                    return new Token(TokenType.ASSIGN, ":=" , line);
                }
                return new Token(TokenType.INVALID, String.valueOf(current) , line);
		    case '+':
                advance();
                return new Token(TokenType.ADD, "+" , line);
		    case '#':
		    	advance();
		    	return new Token(TokenType.HASH, "#" , line); //#
            case '-':
                advance();
                return new Token(TokenType.SUBTRACT, "-" , line);
            case '*':
                advance();
                return new Token(TokenType.MULTIPLY, "*" , line);
            case '/':
                advance();
                return new Token(TokenType.DIVIDE, "/" , line);
            case ',':
                advance();
                return new Token(TokenType.COMMA, "," , line);
            case ';':
                advance();
                return new Token(TokenType.SEMICOLON, ";" , line);
            case '.':
                advance();
                return new Token(TokenType.DOT, "." , line);
            case '(':
                advance();
                return new Token(TokenType.LPAREN, "(" , line);
            case ')':
                advance();
                return new Token(TokenType.RPAREN, ")" , line);
            case '=':
                advance();
                if(currentChar() == '!') {  // no equal case 
                	advance();
                	return new Token(TokenType.NOT_EQUAL, "=!" , line);
                }else if(currentChar() == '<') { // =< 
                	advance();
                	return new Token(TokenType.LESS_EQUAL, "=<" , line);
                }else if(currentChar() == '>'){
                	advance();
                	return new Token(TokenType.GREATER_EQUAL, "=>" , line);
                }
                return new Token(TokenType.EQUAL, "=" , line);
            case '<':
                advance();
                if (currentChar() == '<') {
                    advance();
                    return new Token(TokenType.LEFT_SHIFT, "<<" ,line);
                }
                return new Token(TokenType.LESS, "<" , line);
            
            case '>':
                advance();
                if (currentChar() == '>') {
                    advance();
                    return new Token(TokenType.RIGHT_SHIFT, ">>" ,line);
                }
                return new Token(TokenType.GREATER, ">" , line);
		case 'm':
                advance();
                if (currentChar() == 'o') {
                    advance();
                    if (currentChar() == 'd') {
                        advance();
                        return new Token(TokenType.MOD, "mod" , line);
                    }
                }
                break;
            case 'd':
                advance();
                if (currentChar() == 'i') {
                    advance();
                    if (currentChar() == 'v') {
                        advance();
                        return new Token(TokenType.DIV, "div" , line);
                    }
                }
                break;
            case 'c':
            	advance();
                if (currentChar() == 'a') {
                    advance();
                    if (currentChar() == 'l') {
                        advance();
                        if(currentChar() == 'l') {
                        	advance();
                        	return new Token(TokenType.CALL, "call" , line);
                        	
                        }
                    }
                }
                break;
            default:
                advance();
                return new Token(TokenType.INVALID, String.valueOf(current) , line);
        }
        return new Token(TokenType.INVALID, String.valueOf(current) , line);
    }
}
