package Parse;
import java.io.PrintWriter;

public class Main {

  public static void main(String argv[]) throws java.io.IOException {
    for (int i = 0; i < argv.length; ++i) {
      String filename = argv[i];
      if (argv.length > 1)
	System.out.println("***Processing: " + filename);
      ErrorMsg.ErrorMsg errorMsg = new ErrorMsg.ErrorMsg(filename);
      java.io.InputStream inp=new java.io.FileInputStream(filename);
      Lexer lexer = new Yylex(inp,errorMsg);
      java_cup.runtime.Symbol tok;

      do {
	 String extra = "";
         tok=lexer.nextToken();
	 switch (tok.sym) {
	 case sym.ID:     extra = "\t$" + tok.value; break;
	 case sym.INT:    extra = "\t#" + tok.value; break;
	 case sym.STRING: extra = " \"" + tok.value + "\""; break;
	 }
	 System.out.println(symnames[tok.sym] + " " + tok.left + extra);
      } while (tok.sym != sym.EOF);

      inp.close();
    }
  }

  static String symnames[] = new String[100];
  static {
     
     symnames[sym.FUNCTION] = "FUNCTION"; // function
     symnames[sym.EOF] = "EOF"; //
     symnames[sym.INT] = "INT"; // 1234
     symnames[sym.GT] = "GT"; // >
     symnames[sym.DIVIDE] = "DIVIDE"; // /
     symnames[sym.COLON] = "COLON"; // :
     symnames[sym.ELSE] = "ELSE"; // else
     symnames[sym.OR] = "OR"; // |
     symnames[sym.NIL] = "NIL"; // nil
     symnames[sym.DO] = "DO"; // do
     symnames[sym.GE] = "GE"; // >=
     symnames[sym.error] = "error";
     symnames[sym.LT] = "LT"; // <
     symnames[sym.OF] = "OF"; // of
     symnames[sym.MINUS] = "MINUS"; // -
     symnames[sym.ARRAY] = "ARRAY"; // array
     symnames[sym.TYPE] = "TYPE"; // type
     symnames[sym.FOR] = "FOR"; // for
     symnames[sym.TO] = "TO"; // to
     symnames[sym.TIMES] = "TIMES"; // *
     symnames[sym.COMMA] = "COMMA"; // ,
     symnames[sym.LE] = "LE"; // <=
     symnames[sym.IN] = "IN"; // in
     symnames[sym.END] = "END"; // end
     symnames[sym.ASSIGN] = "ASSIGN"; // :=
     symnames[sym.STRING] = "STRING"; // "string"
     symnames[sym.DOT] = "DOT"; // .
     symnames[sym.LPAREN] = "LPAREN"; // (
     symnames[sym.RPAREN] = "RPAREN"; // )
     symnames[sym.IF] = "IF"; // if
     symnames[sym.SEMICOLON] = "SEMICOLON"; // ;
     symnames[sym.ID] = "ID"; // identifier
     symnames[sym.WHILE] = "WHILE"; // while
     symnames[sym.LBRACK] = "LBRACK"; // {
     symnames[sym.RBRACK] = "RBRACK"; // }
     symnames[sym.NEQ] = "NEQ"; // <>
     symnames[sym.VAR] = "VAR"; // var
     symnames[sym.BREAK] = "BREAK"; // break
     symnames[sym.AND] = "AND"; // &
     symnames[sym.PLUS] = "PLUS"; // +
     symnames[sym.LBRACE] = "LBRACE"; // [
     symnames[sym.RBRACE] = "RBRACE"; // ]
     symnames[sym.LET] = "LET"; // let
     symnames[sym.THEN] = "THEN"; // then
     symnames[sym.EQ] = "EQ"; // =
   }

}
