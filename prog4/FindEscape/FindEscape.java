package FindEscape;

public class FindEscape {
  Symbol.Table escEnv = new Symbol.Table(); // escEnv maps Symbol to Escape

  public FindEscape(Absyn.Exp e) { traverseExp(0, e);  }

  void traverseVar(int depth, Absyn.Var v) {
  }

  void traverseExp(int depth, Absyn.Exp e) {
  }

  void traverseDec(int depth, Absyn.Dec d) {
  }
}
