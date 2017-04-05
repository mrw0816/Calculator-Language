/**
 * Created by Ryan on 3/27/2017.
 */
public class Parser {

    private Lexer lex;
    Node head = null;

    public Parser(Lexer lexer){
        lex = lexer;


        head = parseStatements(head);

    }

    public Node parseStatements(Node parent){
        //System.out.println("parseStatements() called");
        Node newNode = new Node(parent, Token.Type.STATEMENTS, 2, null);

        newNode.child[0] = parseStatement(newNode);

        if(lex.peekFirst() != null){
            newNode.child[1] = parseStatements(newNode);
        }else{
            newNode.child[1] = null;
        }

        return newNode;
    }

    public Node parseStatement(Node parent){
        //System.out.println("parseStatement() called");
        Node newNode = null;
        Token token;

        if(lex.peekFirst().type == Token.Type.MESSAGE){
            newNode = new Node(parent, Token.Type.STATEMENT, 2, Token.Type.STATEMENT.name());
            //System.out.println("Message node created\n");

            newNode.child[0] = new Node(newNode, Token.Type.MESSAGE, 0, lex.pollFirst().data);
            newNode.child[1] = new Node(newNode, Token.Type.STRING, 0, lex.peekFirst().data.substring(1, lex.pollFirst().data.length() - 1));

        }else if(lex.peekFirst().type == Token.Type.NEWLINE){
            newNode = new Node(parent, Token.Type.STATEMENT, 1, Token.Type.STATEMENT.name());
            newNode.child[0] = new Node(newNode, Token.Type.NEWLINE, 0, lex.pollFirst().data);

        }else if(lex.peekFirst().type == Token.Type.INPUT){
            newNode = new Node(parent, Token.Type.STATEMENT, 3, Token.Type.STATEMENT.name());

            newNode.child[0] = new Node(newNode, Token.Type.INPUT, 0, lex.pollFirst().data);
            //System.out.println("Input node created");
            System.out.println();
            newNode.child[1] = new Node(newNode, Token.Type.STRING, 0, lex.peekFirst().data.substring(1, lex.pollFirst().data.length() - 1));
            newNode.child[2] = new Node(newNode, Token.Type.VARIABLE, 0, lex.pollFirst().data);

        }else if(lex.peekFirst().type == Token.Type.SHOW){
            newNode = new Node(parent, Token.Type.STATEMENT, 2, Token.Type.STATEMENT.name());
            //System.out.println("Show node created\n");
            newNode.child[0] = new Node(newNode, Token.Type.SHOW, 0, lex.pollFirst().data);
            newNode.child[1] = parseExpression(newNode);

        }else if(lex.peekFirst().type == Token.Type.VARIABLE){
            newNode = new Node(parent, Token.Type.STATEMENT, 3, Token.Type.STATEMENT.name());

            newNode.child[0] = new Node(newNode, Token.Type.VARIABLE, 0, lex.pollFirst().data);
            if(lex.peekFirst().type == Token.Type.EQUALS){
                newNode.child[1] = new Node(newNode, Token.Type.EQUALS, 0, lex.pollFirst().data);
            }else{
                error("No '=' found after variable");
            }

            newNode.child[2] = parseExpression(newNode);
        }


        return newNode;
    }



    private Node parseExpression(Node parent){
        //System.out.println("parseExpression() called");
        Node newNode = new Node(parent, Token.Type.EXPRESSION, 3, Token.Type.EXPRESSION.name());
        newNode.child[0] = parseTerm(newNode);

        if (lex.peekFirst().type == Token.Type.PLUS) {
            newNode.child[1] = new Node(newNode, Token.Type.PLUS, 0, lex.pollFirst().data);
            newNode.child[2] = parseExpression(newNode);
        } else if (lex.peekFirst().type == Token.Type.MINUS) {
            newNode.child[1] = new Node(newNode, Token.Type.MINUS, 0, lex.pollFirst().data);
            newNode.child[2] = parseExpression(newNode);
        } else {
            newNode.child[1] = null;
            newNode.child[2] = null;
        }
        return newNode;
    }

    private Node parseTerm(Node parent){
        //System.out.println("parseTerm() called");
        Node newNode = new Node(parent, Token.Type.TERM, 3, Token.Type.TERM.name());

        newNode.child[0] = parseFunction(newNode);

        if(lex.peekFirst().type == Token.Type.MULTIPLY){
            newNode.child[1] = new Node(newNode, Token.Type.MULTIPLY, 0, lex.pollFirst().data);
            newNode.child[2] = parseTerm(newNode);
        }else if(lex.peekFirst().type == Token.Type.DIVIDE){
            newNode.child[1] = new Node(newNode, Token.Type.DIVIDE, 0, lex.pollFirst().data);
            newNode.child[2] = parseTerm(newNode);
        }else{
            newNode.child[1] = null;
            newNode.child[2] = null;
        }

        return newNode;
    }

    private Node parseFunction(Node parent){
        //System.out.println("parseFunction() called");
        Node newNode = null;
        if(lex.peekFirst().type == Token.Type.INTEGER){
            newNode = new Node(parent, Token.Type.FUNCTION, 1, Token.Type.FUNCTION.name());
            newNode.child[0] = new Node(newNode, Token.Type.INTEGER, 0, lex.pollFirst().data);
        }else if(lex.peekFirst().type == Token.Type.DOUBLE){
            newNode = new Node(parent, Token.Type.FUNCTION, 1, Token.Type.FUNCTION.name());
            newNode.child[0] = new Node(newNode, Token.Type.DOUBLE, 0, lex.pollFirst().data);
        }else if(lex.peekFirst().type == Token.Type.VARIABLE) {
            newNode = new Node(parent, Token.Type.FUNCTION, 1, Token.Type.FUNCTION.name());
            newNode.child[0] = new Node(newNode, Token.Type.VARIABLE, 0, lex.pollFirst().data);
        }else if(lex.peekFirst().type == Token.Type.LPAREN){
            newNode = new Node(parent, Token.Type.FUNCTION, 3, Token.Type.FUNCTION.name());
            newNode.child[0] = new Node(newNode, Token.Type.LPAREN, 0, lex.pollFirst().data);
            newNode.child[1] = parseExpression(newNode);
            newNode.child[2] = new Node (newNode, Token.Type.RPAREN, 0, lex.pollFirst().data);
        }else if(lex.peekFirst().type == Token.Type.MINUS){
            newNode = new Node(parent, Token.Type.FUNCTION, 2, Token.Type.FUNCTION.name());
            newNode.child[0] = new Node(newNode, Token.Type.MINUS, 0, lex.pollFirst().data);
            newNode.child[1] = parseFunction(newNode);
        }else if(lex.peekFirst().type == Token.Type.COS || lex.peekFirst().type == Token.Type.SIN || lex.peekFirst().type == Token.Type.SQRT){
            newNode = new Node(parent, Token.Type.FUNCTION, 4, Token.Type.FUNCTION.name());
            newNode.child[0] = new Node(newNode, Token.Type.BIFN, 1, Token.Type.BIFN.name());
            newNode.child[0].child[0] = new Node(newNode.child[0], lex.peekFirst().type, 0, lex.pollFirst().data);
            newNode.child[1] = new Node(newNode, Token.Type.LPAREN, 0, lex.pollFirst().data);
            newNode.child[2] = parseExpression(newNode);
            newNode.child[3] = new Node(newNode, Token.Type.RPAREN, 0, lex.pollFirst().data);
        }else {
            error("error parsing function");
        }
        return newNode;
    }




    private static void error( String message ) {
        System.out.println( message );
        System.exit(1);
    }

}
