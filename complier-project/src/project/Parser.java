package project;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class Parser {

	private final Lexer lexer;
    private Token currentToken;
    
    /**
     * Constructor for Parser
     * @param lexer lexer object
     */
    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.nextToken();
    }
    
    /**
     * eat method to match the expected token type with the current token type and move to the next token
     * @param type expected token type
     */
    private void eat(TokenType type) {
        if (currentToken.type == type) {
            currentToken = lexer.nextToken();
        } else {
        	 System.err.println("Syntax Error at line " + currentToken.line + ": Expected " 
        	            + type + " but found " + currentToken.type + " [Token: " + currentToken.value + "]");
        	        System.exit(1); // Exit the program or handle as needed
        }
    }
    
    /**
     * program method to parse the program declaration
     * program ->  lib-decl  declarations  ( function-decl )*  block exit    
     */
    
    private void proram() {
    	libDecl();  //method
    	declarations();
   	 while (currentToken.type == TokenType.FUNCTION) {
        functionDecl();
    }
    	block();
    	eat(TokenType.EXIT);
    }
    /*
     * lib-decl  ->  # include  <   file-name  >    ;     lib-decl     |      λ
     */
    private void libDecl(){
    	if(currentToken.type == TokenType.HASH) {
    	eat(TokenType.HASH);
    	eat(TokenType.INCLUDE);
    	eat(TokenType.LESS);
    	eat(TokenType.IDENTIFIER);
    	eat(TokenType.GREATER);
    	eat(TokenType.SEMICOLON);
    	libDecl();
    	}
    }
    
    /*
     * declarations  -> const-decl       var-decl       
     */
    private void declarations() {
    	constDecl();
    	varDecl();
    }
    
    /*
     * const-decl  -> const  data-type   const-name   =    value   ;   const-decl    |   λ    
     */
    private void constDecl() {
    	if(currentToken.type == TokenType.CONST) {
    		eat(TokenType.CONST);
    		dataType();
    		eat(TokenType.IDENTIFIER);
    		eat(TokenType.EQUAL);
    		value();
    		eat(TokenType.SEMICOLON);
    		constDecl();
    	}
    		
    }
    
    /*
     * var-decl   ->  var    data-type    name-list     ;      var-decl      |     λ 
     */
    private void varDecl() {
    	if(currentToken.type == TokenType.VAR) {
    		 eat(TokenType.VAR);
    		dataType();
    		 nameList();   
    		eat(TokenType.SEMICOLON);
    		varDecl();
    	}
    }
    
    /*
     * name-list  ->   var-name     more-names 
     */
    private void nameList() {
    	eat(TokenType.IDENTIFIER);
    	moreNames();
    }
    
    /*
     * more-names   ->    ,     name-list       |        λ 
     */
    private void moreNames() {
    	if(currentToken.type == TokenType.COMMA) {
    		eat(TokenType.COMMA);
    		nameList();
    	}
    }
    
    
    /*
     * data-type  ->   int       |       float      |     char             
     */
   private void  dataType() {
	   if (currentToken.type == TokenType.INT) {
	        eat(TokenType.INT); // Match 'int'
	    } 
	   else if (currentToken.type == TokenType.FLOUT) {
	        eat(TokenType.FLOUT); // Match 'float'
	    } 
	    else if (currentToken.type == TokenType.CHAR) {
	        eat(TokenType.CHAR); // Match 'char'
	    } 
	    else {
	        System.err.println("Syntax Error at line " + currentToken.line + ": Expected a data type (int, float, or char) but found " 
	                + currentToken.type + " [Token: " + currentToken.value + "]");
	        System.exit(1); // Exit or handle the error as needed
	    }
   }
   
   /*
    * function-decl ->    function-heading        declarations        block       ;
    */
    
   private void functionDecl() {  
	   functionHeading();
	   declarations();
	    block();
	   eat(TokenType.SEMICOLON);
   }
   
   /*
    * function-heading   ->   function        function-name      ; 
    */
  private void functionHeading() {
	  eat(TokenType.FUNCTION); // check if the current token is function :)
	  eat(TokenType.IDENTIFIER);
	  eat(TokenType.SEMICOLON);  
  }
  
  /*
   * block  ->  newb    stmt-list    endb
   */
    
  private void block() {
	  eat(TokenType.NEWB);
	  stmtList();
	  eat(TokenType.ENDB);
  }
  
  /*
   * stmt-list ->     statement     ;     stmt-list         |        λ
   */
    
  private void stmtList() { // add endb to condtion 
      while (currentToken.type != TokenType.ENDB && currentToken.type != TokenType.EOF && currentToken.type != TokenType.ELSE && currentToken.type != TokenType.UNTIL &&currentToken.type != TokenType.RIGHT_SHIFT) {
          statement();
          eat(TokenType.SEMICOLON);
      }
  }
  /*
   * statement -> ass-stmt   |   inout-stmt   |   if-stmt   |  while-stmt   |   block    |     repeat-stmt  
   *              |      function-call-stmt
   */
  private void statement() {
      switch (currentToken.type) {
          case IDENTIFIER:
              assStmt(); // call assignment statement
              break;
          case CIN:
          case COUT:
              inoutStmt(); // call input/output statement
              break;
          case IF:
              ifStmt(); // call if statement
              break;
          case WHILE:
              whileStmt(); //call while statement
              break;
          case REPEAT:
              repeatStmt(); //call repeat statement
              break;
          case CALL:
              functionCallStmt(); // call function call statement
              break;
          case NEWB:
              block(); // call block of statements
              break;
          default:
              System.err.println("Syntax Error: Unexpected token " + currentToken.value 
                      + " at line " + currentToken.line);
              System.exit(1); // Handle unexpected tokens with an error
      }
  }

  /*
   * ass-stmt -> var-name     :=      exp
   */
  private void assStmt() {
	  eat(TokenType.IDENTIFIER); // var-name
	  eat(TokenType.ASSIGN); //:=
	  exp();
  }
  
  /*
   * exp -> term      exp-prime
   */
  private void exp() {
	  term();
	  expPrime();
  }
    
  /*
   * exp-prime -> add-oper     term     exp-prime       |       λ
   */
  private void expPrime() {
	  if(currentToken.type == TokenType.ADD || currentToken.type == TokenType.SUBTRACT) {
		  addOper();
		  term();
		  expPrime();
  	}
  }
  
  /*
   * add-oper ->  +    |     -  
   */
  private void addOper() {
	  if (currentToken.type == TokenType.ADD) {
	        eat(TokenType.ADD); // Match + token
	    } else if (currentToken.type == TokenType.SUBTRACT) {
	        eat(TokenType.SUBTRACT); // Match - token
	    } else {
	        System.err.println("Syntax Error at line " + currentToken.line + ": Expected '+' or '-' but found " 
	                + currentToken.type + " [Token: " + currentToken.value + "]");
	        System.exit(1); // Exit or handle the error as needed
	    }
  }
  
  /*
   * term -> factor        term-prime  
   */
  
  private void term() {
	  factor();
	   termPrime();
  }
  /*
   * term-prime  ->  mul-oper       factor       term-prime        |       λ
   */
  private void termPrime() {
	  if (currentToken.type == TokenType.MULTIPLY || 
		        currentToken.type == TokenType.DIVIDE || 
		        currentToken.type == TokenType.MOD || 
		        currentToken.type == TokenType.DIV) {
		        
		        mulOper();      //call mulOper
		        factor();          //call factor
		        termPrime();    //call termPrime
		    }
		    // λ case: Do nothing and return
  }
    
  /*
   * mul-oper -> *     |     /       |      mod     |    div
   */
  private void mulOper() {
	  if (currentToken.type == TokenType.MULTIPLY) {
	        eat(TokenType.MULTIPLY); // Match * token
	    } else if (currentToken.type == TokenType.DIVIDE) {
	        eat(TokenType.DIVIDE);  // Match / token
	    } else if (currentToken.type == TokenType.MOD) {
	        eat(TokenType.MOD);     // Match mod token
	    } else if (currentToken.type == TokenType.DIV) {
	        eat(TokenType.DIV);     // Match div token
	    } else {
	        System.err.println("Syntax Error at line " + currentToken.line + ": Expected '*' or '/' or 'mod' or 'div' but found " 
	                + currentToken.type + " [Token: " + currentToken.value + "]");
	        System.exit(1); // Exit or handle the error as needed
	    }
  }
  
  /*
   * factor ->  (     exp     )     |     var-name      |      const-name     |     value
   */
  private void factor() {
	  if (currentToken.type == TokenType.LPAREN) { // ( case
	        eat(TokenType.LPAREN);  // Match (
	        exp();                   //call  expression 
	        eat(TokenType.RPAREN); // Match )
	    } else if (currentToken.type == TokenType.IDENTIFIER) { // var-name or const-name
	       eat(TokenType.IDENTIFIER); // Match an identifier (variable or constant name)
	    } else if (currentToken.type == TokenType.INT || currentToken.type == TokenType.FLOUT) { // value
	        value(); // call the value (either integer or real)
	    } else {
	        System.err.println("Syntax Error at line " + currentToken.line + ": Expected '(' or identifier or value but found " 
	                + currentToken.type + " [Token: " + currentToken.value + "]");
	        System.exit(1); // Exit or handle the error as needed
	    }
  }
  
  /*
   * value -> integer-value   |   real-value
   */
  
  private void value() {
	  if (currentToken.type == TokenType.INT) { // Integer value
	        integerValue(); // call(parse) an integer value
	    } else if (currentToken.type == TokenType.FLOUT) { // Real value
	        realValue(); // call a real value
	    } else {
	        System.err.println("Syntax Error at line " + currentToken.line + ": Expected an integer or real value but found " 
	                + currentToken.type + " [Token: " + currentToken.value + "]");
	        System.exit(1); 
	    }
  }
  
  /*
   * integer-value -> digit ( digit )*    
   */
  private void integerValue() {
	  if (currentToken.type == TokenType.INT) {
	        eat(TokenType.INT); // Match the integer value
	    } else {
	        System.err.println("Syntax Error at line " + currentToken.line + ": Expected an integer value but found " 
	                + currentToken.type + " [Token: " + currentToken.value + "]");
	        System.exit(1); // error
	    }
  }
  
  /*
   * real-value -> digit ( digit )*. digit ( digit )*
   */
  private void realValue() {
	  if (currentToken.type == TokenType.FLOUT) {
	        eat(TokenType.FLOUT); // Match the real value
	    } else {
	        System.err.println("Syntax Error at line " + currentToken.line + ": Expected a real value but found " 
	                + currentToken.type + " [Token: " + currentToken.value + "]");
	        System.exit(1); // Exit or handle the error as needed
	    }
  }
  
  /*
   * inout-stmt -> cin    >>    var-name         |    cout     <<    name-value
   */
  
  private void inoutStmt() {
	  if (currentToken.type == TokenType.CIN) {  //case  cin
	        eat(TokenType.CIN); 
	         eat(TokenType.RIGHT_SHIFT); 
	        eat(TokenType.IDENTIFIER);
	    } else if (currentToken.type == TokenType.COUT) {  // case cout
	        eat(TokenType.COUT); 
	        eat(TokenType.LEFT_SHIFT);  ///<<
	        nameValue(); 
	    } else {
	        System.err.println("Syntax Error: Expected 'cin' or 'cout' but found " 
	                + currentToken.value + " at line " + currentToken.line);
	        System.exit(1);   // error
	    }
  }
  /*
   * name-value ->  var-name    |    const-name   |      value 
   */

  private void nameValue() {  
	  switch (currentToken.type) {
      case IDENTIFIER:
          eat(TokenType.IDENTIFIER); 
          break;
      case INT:
      case FLOUT:
          value(); 
          break;
      default:
          System.err.println("Syntax Error: Expected a variable, constant, or value but found " 
                  + currentToken.value + " at line " + currentToken.line);
          System.exit(1); 
     }
  }
  
  
  /*
   * 
   * if-stmt -> if  (   condition  )  statement     else-part     
   */
  private void ifStmt() {
	  eat(TokenType.IF);//matchinggggg the if
	  eat(TokenType.LPAREN);
	  condition();
	  eat(TokenType.RPAREN);
	  statement();
	  elsePart();
  }
  /*
   * 
   * else-part ->  else     statement   |   λ
   */
  private void elsePart() {
	  if(currentToken.type == TokenType.ELSE) {
		  eat(TokenType.ELSE); // matchinnnnng the else
		  statement();
	  }
  }
  
  /*
   * condition -> name-value       relational-oper        name-value 
   */
  private void condition() {
	  nameValue();
	  relationalOper();
	  nameValue();
  }
  /*
   * relational-oper ->  =      |       =!         |     <     |       =<     |     >     |     =>
   */
  
  private void relationalOper() {
	    switch (currentToken.type) {
	        case EQUAL: // '='
	            eat(TokenType.EQUAL);
	            break;
	        case NOT_EQUAL: // '=!'
	            eat(TokenType.NOT_EQUAL);
	            break;
	        case LESS: // '<'
	            eat(TokenType.LESS);
	            break;
	        case LESS_EQUAL: // '=<'
	            eat(TokenType.LESS_EQUAL);
	            break;
	        case GREATER: // '>'
	            eat(TokenType.GREATER);
	            break;
	        case GREATER_EQUAL: // '=>'
	            eat(TokenType.GREATER_EQUAL);
	            break;
	        default:
	            System.err.println("Syntax Error at line " + currentToken.line + ": Expected a relational operator but found " + currentToken.value);
	            System.exit(1); //error
	    }
	}
  
  /*
   * while-stmt -> while   (   condition    )   newb    stmt-list    endb
   */

  private void whileStmt() {
	  eat(TokenType.WHILE);//matchinggggg the while
	  eat(TokenType.LPAREN);
	  condition();
	  eat(TokenType.RPAREN);
	  eat(TokenType.NEWB);
	  stmtList();
	  eat(TokenType.ENDB);
  }
  /*
   * repeat-stmt   -> repeat      stmt-list       until        condition   
   */
  private void repeatStmt() {
	  eat(TokenType.REPEAT); //match repeat :(
	  stmtList();
	  eat(TokenType.UNTIL);  // match until
	  condition();
  }
  
  /*
   * function-call-stmt   ->  call function-name
   */
  private void functionCallStmt() { // Finally the last method :)
	  eat(TokenType.CALL);
	  eat(TokenType.IDENTIFIER);
  }
  public static void parse(String filePath) {
      String input;
      try {
          File file = new File(filePath);
          input = Files.readString(Path.of(file.getAbsolutePath()));
      } catch (IOException e) {
          System.out.println("Error reading file: " + e.getMessage());
          return;
      }
      Lexer lexer = new Lexer(input);
      Parser parser = new Parser(lexer);
      parser.proram();
      System.out.println("Parsing completed successfully.");  
      
  }
}
