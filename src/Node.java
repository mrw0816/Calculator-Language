
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Ryan on 4/4/2017.
 */
public class Node {

    private static HashMap<String, Double> memmory = new HashMap<>();
    private Node parent;

    private Token.Type type;
    protected Node[] child;
    private String data;

    public Node(Node par, Token.Type typ, int num, String d){

        parent = par;
        type = typ;

        child = new Node[num];
        data = d;

    }

    public void execute(Node head){

        executeStatements(head);

    }

    private void executeStatements(Node parent){
        //System.out.println("executeStatements() called");

        executeStatement(parent.child[0]);

        if(parent.child[1] != null){
            executeStatements(parent.child[1]);
        }else{
            //System.out.print("error in executeStatements");
            //System.exit(1);
        }
    }

    private void executeStatement(Node parent){
        //System.out.println("executeStatement() called");
        if(parent.child[0].type == Token.Type.SHOW){
            System.out.print( executeExpression( parent.child[1] ) );
        }else if(parent.child[0].type == Token.Type.MESSAGE){
            System.out.print(parent.child[1].data);
        }else if(parent.child[0].type == Token.Type.INPUT){
            Scanner scnr = new Scanner(System.in);
            System.out.print(parent.child[1].data);
            memmory.put(parent.child[2].data, scnr.nextDouble());
        }else if(parent.child[0].type == Token.Type.NEWLINE){
            System.out.println();
        }else if(parent.child[0].type == Token.Type.VARIABLE){
            memmory.put(parent.child[0].data, executeExpression(parent.child[2]));
        }else{
            System.out.println();
            System.out.println("Problem executing in executeStatement");
        }
    }

    private double executeExpression(Node parent){
        //System.out.println("executeExpression() called");
        double answer = 0;

        answer = executeTerm(parent.child[0]);

        if(parent.child[1] != null) {
            if (parent.child[1].type == Token.Type.PLUS) {
                answer += executeExpression(parent.child[2]);
            } else if (parent.child[1].type == Token.Type.MINUS) {
                answer -= executeExpression(parent.child[2]);
            } else {
                System.out.print("error in executeExpression()");
                System.exit(1);
            }
        }
        return answer;
    }

    private double executeTerm(Node parent){
        //System.out.println("executeTerm() called");
        double answer = 0;

        answer = executeFunction(parent.child[0]);
        if(parent.child[1] != null) {
            if (parent.child[1].type == Token.Type.MULTIPLY) {
                answer *= executeTerm(parent.child[2]);
            } else if (parent.child[1].type == Token.Type.DIVIDE) {
                answer /= executeTerm(parent.child[2]);
            } else {
                System.out.print("error in executeTerm()");
                System.exit(1);
            }
        }
        return answer;
    }

    private double executeFunction(Node parent){
        //System.out.println("executeFunction() called");
        double answer = 0;

        if(parent.child[0].type == Token.Type.INTEGER){
            answer = Integer.parseInt(parent.child[0].data);
        }else if(parent.child[0].type == Token.Type.DOUBLE){
            answer = Double.parseDouble(parent.child[0].data);
        }else if(parent.child[0].type == Token.Type.VARIABLE){
            answer = memmory.get(parent.child[0].data);
        }else if(parent.child[0].type == Token.Type.LPAREN){
            answer = executeExpression(parent.child[1]);
        }else if(parent.child[0].type == Token.Type.MINUS){
            answer = -(executeFunction(parent.child[1]));
        }else if(parent.child[0].type == Token.Type.BIFN){
            if(parent.child[0].child[0].type == Token.Type.COS){
                answer = Math.cos( executeExpression(parent.child[2]));
            }else if(parent.child[0].child[0].type == Token.Type.SIN){
                answer = Math.sin( executeExpression(parent.child[2]));
            }else if(parent.child[0].child[0].type == Token.Type.SQRT){
                answer = Math.sqrt( executeExpression(parent.child[2]));
            }
        }else{
            System.out.print("error in executeFunction()");
            System.exit(1);
        }

        return answer;
    }

}
