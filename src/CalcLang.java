import java.util.Scanner;

/**
 * Created by Ryan on 4/5/2017.
 */
public class CalcLang {

    public static void main(String[] args){
        Scanner scnr = new Scanner(System.in);
        System.out.print("Enter file name: ");
        String fileName = scnr.nextLine();


        Lexer lex = new Lexer(fileName);
        Parser parser = new Parser(lex);
        parser.head.execute(parser.head);
    }

}
