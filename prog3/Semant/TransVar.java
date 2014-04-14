package Semant;
import Translate.Exp;
import Types.Type;

public class TransVar extends Trans {
    
  public TransVar(Env e){
    env = e;
  }
  
  public ExpTy transVar(Absyn.Var v) {
    if (v instanceof Absyn.FieldVar)
      return transVar((Absyn.FieldVar) v);
    else if (v instanceof Absyn.SimpleVar)
      return transVar((Absyn.SimpleVar) v);
    else if (v instanceof Absyn.SubscriptVar)
      return transVar((Absyn.SubscriptVar) v);
    else
      throw new Error("TransVar.transVar");
  }
  
  public ExpTy transVar(Absyn.FieldVar v) {
    // Load the vairiable type
    Type varType = transVar(v.var).ty.actual();
    
    // If the var isn't a record, we have a problem
    if (!(varType instanceof Types.RECORD)){
      error(v.pos, "expected record type");
      return new ExpTy(null, VOID);
    }
    
    // Loop through the record's fields and return the field we are looking for
    for (Types.RECORD record = (Types.RECORD)varType; record != null; record = record.tail) {
     if (record.fieldName == v.field)
       return new ExpTy(null, record.fieldType);
    }
    
    // Error if we can't find the field
    error(v.pos, "couldn't find field: " + v.field);
    return new ExpTy(null, VOID);
  }
  
  public ExpTy transVar(Absyn.SimpleVar v) {
    Entry value = (Entry)env.venv.get(v.name);
    if (value instanceof VarEntry) {
      return new ExpTy(null, ((VarEntry)value).ty);
    } else {
      error(v.pos, "variable: " + v.name + " is undefined");
      return new ExpTy(null, VOID);
    }
  }
  
  public ExpTy transVar(Absyn.SubscriptVar v) {
    // Get both parts of the SubstrciptVar
    ExpTy root = transVar(v.var);
    ExpTy index = transExp(v.index);
    
    // If index is not an int, we have a problem
    checkInt(index, v.index.pos);
    
    // If our root is not an array, we hav a problem
    if (!(root.ty.actual() instanceof Types.ARRAY)){
      error(v.pos, "expected array type");
      return new ExpTy(null, VOID);
    }
    
    // Return the array's type
    Types.ARRAY arrayType = (Types.ARRAY)root.ty.actual();
    return new ExpTy(null, arrayType.element);
  }
  
}