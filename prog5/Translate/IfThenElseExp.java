package Translate;
import Temp.Temp;
import Temp.Label;

class IfThenElseExp extends Exp {
  Exp cond, a, b;
  Label t = new Label();
  Label f = new Label();
  Label join = new Label();

  IfThenElseExp(Exp cc, Exp aa, Exp bb) {
    cond = cc; 
    a = aa; 
    b = bb;
  }

  Tree.Stm unCx(Label tt, Label ff) {
    // This is the naive implementation; you should extend it to eliminate
    // unnecessary JUMP nodes
    Tree.Stm aStm = a.unCx(tt, ff);
    Tree.Stm bStm = b.unCx(tt, ff);

    Tree.Stm condStm = cond.unCx(t, f);

    if (aStm == null && bStm == null)
      return condStm;
    if (aStm == null)
      return new Tree.SEQ(condStm, new Tree.SEQ(new Tree.LABEL(f), bStm));
    if (bStm == null)
      return new Tree.SEQ(condStm, new Tree.SEQ(new Tree.LABEL(t), aStm));
    return new Tree.SEQ(condStm,
			new Tree.SEQ(new Tree.SEQ(new Tree.LABEL(t), aStm),
				     new Tree.SEQ(new Tree.LABEL(f), bStm)));
  }

  Tree.Exp unEx() {
    // You must implement this function
    return new Tree.CONST(0);
  }

  Tree.Stm unNx() {
    // You must implement this function
    return null;
  }
}
