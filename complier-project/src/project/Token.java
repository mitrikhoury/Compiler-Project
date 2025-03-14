package project;

/**
 * A class to represent a token
 * TokenType and the value of the token and the line number
 */
public class Token {
    public TokenType type;
    public String value;
    public int line;

    public Token(TokenType type, String value ,int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", line=" + line +
                '}';
    }
}