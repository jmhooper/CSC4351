package Semant;
import Translate.Exp;
import Types.Type;

public class TransExp extends Trans {
    
  public TransExp(Env e) {
    env = e;
  }
  
  public ExpTy transExp(Absyn.Exp e) {
    ExpTy result;
    
    if (e == null)
      return new ExpTy(null, VOID);
    else if (e instanceof Absyn.ArrayExp)
      result = transExp((Absyn.ArrayExp)e);
    else if (e instanceof Absyn.AssignExp)
      result = transExp((Absyn.AssignExp)e);
    else if (e instanceof Absyn.BreakExp)
      result = transExp((Absyn.BreakExp)e);
    else if (e instanceof Absyn.CallExp)
      result = transExp((Absyn.CallExp)e);
    else if (e instanceof Absyn.ForExp)
      result = transExp((Absyn.ForExp)e);
    else if (e instanceof Absyn.IfExp)
      result = transExp((Absyn.IfExp)e);
    else if (e instanceof Absyn.IntExp)
      result = transExp((Absyn.IntExp)e);
    else if (e instanceof Absyn.LetExp)
      result = transExp((Absyn.LetExp)e);
    else if (e instanceof Absyn.NilExp)
      result = transExp((Absyn.NilExp)e);
    else if (e instanceof Absyn.OpExp)
      result = transExp((Absyn.OpExp)e);
    else if (e instanceof Absyn.RecordExp)
      result = transExp((Absyn.RecordExp)e);
    else if (e instanceof Absyn.SeqExp)
      result = transExp((Absyn.SeqExp)e);
    else if (e instanceof Absyn.StringExp)
      result = transExp((Absyn.StringExp)e);
    else if (e instanceof Absyn.VarExp)
      result = transExp((Absyn.VarExp)e);
    else if (e instanceof Absyn.WhileExp)
      result = transExp((Absyn.WhileExp)e);
    else throw new Error("TransExp.transExp");
    e.type = result.ty;
    return result;
  }
  
  // transExp methods
  
  ExpTy transExp(Absyn.ArrayExp e){
    // Get the array's type
    Types.NAME type = (Types.NAME)env.tenv.get(e.typ);
    
    // Calculate the array's size and initial value
    ExpTy size = transExp(e.size);
    ExpTy init = transExp(e.init);
    
    // Size must be an int
    checkInt(size, e.size.pos);
    
    // If nothing is loaded, the type is undefined
    if (type == null) {
      error(e.pos, "array of type " + e.typ + " undefined");
      return new ExpTy(null, VOID);
    // If the name is not an array, then there is a type mismatch
    } else if (!(type.actual() instanceof Types.ARRAY)) {
      error(e.pos, "not an array type");
      return new ExpTy(null, VOID);
    }
    
    Type element = ((Types.ARRAY)type.actual()).element;
    
    // If the initial type does not match the array's type, we have a problem
    if (!init.ty.coerceTo(element))
      error(e.init.pos, "initial element does not match array's type");
    
    // Everything checks out, return the array's type
    return new ExpTy(null, type);
  }
  
  ExpTy transExp(Absyn.AssignExp e){
    return null;
  }
  
  ExpTy transExp(Absyn.BreakExp e){
    return new ExpTy(null, VOID);
  }
  
  ExpTy transExp(Absyn.CallExp e){
    // Load the function being called
    Entry funEntry = (Entry)env.venv.get(e.func);
    
    // Check that we are calling a function
    if (funEntry == null || !(funEntry instanceof FunEntry)) {
      error(e.pos, "undefined function: " + e.func);
      return new ExpTy(null, VOID);
    }
    
    // Finish implementing
    
    /*if (x instanceof FunEntry) {
      FunEntry f = (FunEntry)x;
      transArgs(e.pos, f.formals, e.args);
      return new ExpTy(null, f.result);
    }
    error(e.pos, "undeclared function: " + e.func);
    return new ExpTy(null, VOID);*/
  }
  
  ExpTy transExp(Absyn.ForExp e){
    return null;
  }
  
  ExpTy transExp(Absyn.IfExp e){
    return null;
  }
  
  ExpTy transExp(Absyn.IntExp e){
    return new ExpTy(null, INT);
  }
  
  ExpTy transExp(Absyn.LetExp e){
    env.venv.beginScope();
    env.tenv.beginScope();
    for (Absyn.DecList d = e.decs; d != null; d = d.tail) {
      transDec(d.head);
    }
    ExpTy body = transExp(e.body);
    env.venv.endScope();
    env.tenv.endScope();
    return new ExpTy(null, body.ty);
  }
  
  ExpTy transExp(Absyn.NilExp e){
    return new ExpTy(null, NIL);
  }
  
  ExpTy transExp(Absyn.OpExp e){
    ExpTy left = transExp(e.left);
    ExpTy right = transExp(e.right);

    switch (e.oper) {
    case Absyn.OpExp.PLUS:
      checkInt(left, e.left.pos);
      checkInt(right, e.right.pos);
      return new ExpTy(null, INT);
    default:
      throw new Error("unknown operator");
    }
  }
  
  ExpTy transExp(Absyn.RecordExp e){
    // Load the record type from the type environment
    Types.NAME type = (Types.NAME)env.tenv.get(e.typ);
    
    // If nothing is found, or something that isn't a record is found, we have a problem
    if (type == null || !(type.actual() instanceof Types.RECORD)) {
      error(e.pos, "Record type undefined: " + e.typ);
      return new ExpTy(null, VOID);
    }
    
    Types.RECORD record = (Types.RECORD)type.actual();
    
    // Loop through the fields and assign the values
    for (Absyn.FieldExpList field = e.fields; field != null; field = field.tail) {
      // Find the type for the value
      ExpTy value = transExp(field.init);
      
      // Find potential problems
      if (record == null) {
        error(e.pos, "too many arguments for record type: " + e.typ);
      } else if (field.name != record.fieldName){
        error(field.pos, "field names are not aligned");
      } else if (!value.ty.coerceTo(record.fieldType)) {
        error(field.pos, "field types do not match");
      }
      
      // Go to the next entry
      if (record != null) record = record.tail;
    }
    
    // If the record isn't null, not enough arguments
    if (record != null) error(e.pos, "not enough arguments for record type: " + e.typ);
    
    // Return the ExpTy
    return new ExpTy(null, type);
  }
  
  ExpTy transExp(Absyn.SeqExp e){
    return null;
  }
  
  ExpTy transExp(Absyn.StringExp e){
    return new ExpTy(null, STRING);
  }
  
  ExpTy transExp(Absyn.VarExp e){
    return transVar(e.var);
  }
  
  ExpTy transExp(Absyn.WhileExp e){
    return null;
  }  
  
}