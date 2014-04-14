package Semant;
import Translate.Exp;
import Types.Type;

public class Semant {
  
  // ExpTranslator
  TransExp transExpObject;
  
  Env env;
  public Semant(ErrorMsg.ErrorMsg err) {
    this(new Env(err));
  }
  Semant(Env e) {
    env = e;
    transExpObject = new TransExp(e);
  }

  public void transProg(Absyn.Exp exp) {
    transExp(exp);
  }

  static final Types.VOID   VOID   = new Types.VOID();
  static final Types.INT    INT    = new Types.INT();
  static final Types.STRING STRING = new Types.STRING();
  static final Types.NIL    NIL    = new Types.NIL();

  ExpTy transExp(Absyn.Exp e) {
    return transExpObject.transExp(e);
  }
  
}

