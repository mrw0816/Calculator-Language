/**
 * Created by Ryan on 3/27/2017.
 */
public class Token {

    public enum Type{INTEGER, DOUBLE, STRING, BIFN, NEWLINE, MESSAGE, SHOW,
                    INPUT, MULTIPLY, MINUS, DIVIDE, PLUS, EQUALS, LPAREN, RPAREN,
                    STATEMENTS, STATEMENT, VARIABLE, EXPRESSION, TERM, FUNCTION,
                    COS, SIN, SQRT}

    Type type;
    String data;

    Token(String d, Type t){
        data = d;
        type = t;
    }

    public boolean isType( Type t ){
        return type == t;
    }

    public Type getType(){
        return type;
    }

    public String getDetails(){
        return data;
    }

    public boolean matches( Type t, String d){
        return ( type == t && data.equals(d) );
    }

    public String toString(){
        return "[" + type.name() + ", " + data + "]";
    }
}
