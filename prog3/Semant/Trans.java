package Semant;
import Translate.Exp;
import Types.Type;

public abstract class Trans {
 
  protected static final Types.VOID   VOID   = new Types.VOID();
  protected static final Types.INT    INT    = new Types.INT();
  protected static final Types.STRING STRING = new Types.STRING();
  protected static final Types.NIL    NIL    = new Types.NIL();
  
  Env env;
  
  protected void error(int pos, String msg) {
    env.errorMsg.error(pos, msg);
  }
  
  // Override one of these methods in each class
  
  public void transDec(Absyn.Dec d) {
    TransDec transDecObject = new TransDec(env);
    transDecObject.transDec(d);
  }
  
  public ExpTy transExp(Absyn.Exp e) {
    TransExp transExpObject = new TransExp(env);
    return transExpObject.transExp(e);
  }
  
  public Type transTy(Absyn.Ty t) {
    TransTy transTyObject = new TransTy(env);
    return transTyObject.transTy(t);
  }
  
  public ExpTy transVar(Absyn.Var v) {
    TransVar transVarObject = new TransVar(env);
    return transVarObject.transVar(v);
  }
  
  // type check methods
  
  protected Exp checkInt(ExpTy et, int pos) {
    if (!INT.coerceTo(et.ty))
      error(pos, "integer required");
    return et.exp;
  }
  
  protected Exp checkComparable(ExpTy et, int pos) {
    Type type = et.ty.actual();
    if (!(type instanceof Types.INT || type instanceof Types.STRING || type instanceof Types.NIL || type instanceof Types.RECORD || type instanceof Types.ARRAY))
      error(pos, "type is not comparable");
    return et.exp;
  }

  protected Exp checkOrderable(ExpTy et, int pos) {
    Type type = et.ty.actual();
    if (!(type instanceof Types.INT || type instanceof Types.STRING))
      error(pos, "integer or string required");
    return et.exp;
  }
  
}