import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Ryan on 3/27/2017.
 */
public class Lexer {


    private static int lineIndex = 0;
    private static String line;
    public static String margin = "";

    private LinkedList<Token> list;
    private Scanner input;

    public Lexer(String fileName){
        try{
            input = new Scanner(new File(fileName));
        }catch (Exception e){
            error("Problem opening file named [" + fileName + "]");
        }

        Token token = null;
        list = new LinkedList<Token>();
        do{
            try {
                token = getNext();
            }catch (IOException e){
                error("Problem reading file");
            }


            if(token != null){
                //System.out.println(token.data);
                list.add(token);
            }
        }while(token != null);
    }

    public Token getNext() throws IOException {


            int state = 1; //start state of FA
            String data = "";
            boolean done = false;
            int sym; //holds current symbol

            do{
                sym = getNextSymbol();
                //System.out.print((char)sym);
                if(sym != -1) {
                    if (state == 1) {
                        if (sym == '\n' || sym == '\t' || sym == '\r' || sym == ' ') {//new line, carriage return, ws
                            //stay at state 1
                        } else if ((sym >= 65 && sym <= 90) || (sym >= 97 && sym <= 122)) {//sym is a character
                            data += (char) sym;
                            state = 2;
                        } else if (sym == 61 || sym == 43 || sym == 45 || sym == 42 || sym == 47 || sym == 40 || sym == 41) {//=,+,-,*,/,(,)
                            data += (char) sym;
                            state = 3;
                        } else if (sym >= 48 && sym <= 57) {//0-9
                            data += (char) sym;
                            state = 4;
                        } else if (sym == 34) {//"
                            data += (char) sym;
                            state = 6;
                        } else if (sym == -1) {// end of file reached
                            done = true;
                        } else {
                            error("Error in lexical analysis phase with symbol "
                                    + sym + " in st/te " + state);
                        }
                    }//end of state 1
                    else if (state == 2) {
                        if (((sym >= 65 && sym <= 90) || (sym >= 97 && sym <= 122)) || (sym >= 48 && sym <= 57)) {//next character is a leter or digit
                            data += (char) sym;
                        } else {
                            putBackSymbol();
                            done = true;
                        }
                    }// end of state 2
                    else if (state == 3) {
                        putBackSymbol();
                        done = true;
                    }// end of state 3
                    else if (state == 4) {
                        if (sym >= 48 && sym <= 57) {//digit
                            data += (char) sym;
                        } else if (sym == 46) {//.
                            data += (char) sym;
                            state = 5;
                        } else {
                            putBackSymbol();
                            done = true;
                        }
                    }//end of state 4
                    else if (state == 5) {
                        if (sym >= 48 && sym <= 57) {//digit
                            data += (char) sym;
                        } else {
                            putBackSymbol();
                            done = true;
                        }
                    }//end of state 5
                    else if (state == 6) {
                        if (sym != 34) {
                            data += (char) sym;
                        } else if (sym == 34) {
                            data += (char) sym;
                            state = 7;
                        }
                    } else if (state == 7) {
                        putBackSymbol();
                        done = true;
                    } else {

                    }
                }
            }while(!done && sym != -1);

            if(sym == -1){
                return null;
            }
            Token token;

            if(state == 2){
                if( data.equals("sqrt") ) {
                    token = new Token(data, Token.Type.SQRT);
                    return token;
                }else if(data.equals("sin") ){
                    token = new Token(data, Token.Type.SIN);
                    return token;
                }else if( data.equals("cos") ) {
                    token = new Token(data, Token.Type.COS);
                   return token;
                }else if(data.equals("newline")){
                    token = new Token(data, Token.Type.NEWLINE);
                    return token;
                }else if(data.equals("msg")){
                    token = new Token(data, Token.Type.MESSAGE);
                    return token;
                }else if(data.equals("show")){
                    token = new Token(data, Token.Type.SHOW);
                    return token;
                }else if(data.equals("input")) {
                    token = new Token(data, Token.Type.INPUT);
                    return token;
                }else {
                    token = new Token(data, Token.Type.VARIABLE);
                    return token;
                }

            }else if(state == 3){//sym == 61 || sym == 43 || sym == 45 || sym == 42 || sym == 47 || sym == 40 || sym == 41
                if(data.equals("=")){
                    token = new Token(data, Token.Type.EQUALS);
                    return token;
                }else if(data.equals("+")){
                    token = new Token(data, Token.Type.PLUS);
                    return token;
                }else if(data.equals("-")){
                    token = new Token(data, Token.Type.MINUS);
                    return token;
                }else if(data.equals("*")){
                    token = new Token(data, Token.Type.MULTIPLY);
                    return token;
                }else if(data.equals("/")){
                    token = new Token(data, Token.Type.DIVIDE);
                    return token;
                }else if(data.equals("(")){
                    token = new Token(data, Token.Type.LPAREN);
                    return token;
                }else if(data.equals(")")){
                    token = new Token(data, Token.Type.RPAREN);
                    return token;
                }
            }else if(state == 4){
                token = new Token(data, Token.Type.INTEGER);
                return token;
            }else if(state == 5){
                token = new Token(data, Token.Type.DOUBLE);
                return token;
            }else if(state == 7){
                token = new Token(data, Token.Type.STRING);
                return token;
            }else {
                error("somehow Lexer FA halted in inappropriate state " + state);
                return null;
            }


        return null;
    }

    public Token getToken() {
        Token token = null;
        try {
            token = getNext();
            System.out.println("                                   got token: " + token );
        }catch (IOException e){}

        return token;
    }

    /*public void putBack( Token token ) {
        System.out.println( margin + "put back token " + token.toString() );
        stack.push( token );
    }*/

    private int getNextSymbol() throws IOException {
        if ((line == null || (lineIndex >= line.length())) && (input.hasNextLine())) {
            line = input.nextLine() + '\n';
            lineIndex = 0;
        } else if(lineIndex < line.length()){

        }else if (!input.hasNextLine()) {
            return -1;
        }
        return line.charAt(lineIndex++);
    }
    private void putBackSymbol() {
        lineIndex--;
    }

    private static void error( String message ) {
        System.out.println( message );
        System.exit(1);
    }

    public Token pollFirst(){
        return list.pollFirst();
    }

    public Token peekFirst(){
        return list.peekFirst();
    }

    public Token get(int index){
        return list.get(index);
    }
}
