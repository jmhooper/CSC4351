package Semant;
import Translate.Exp;
import Types.Type;

public class TransDec extends Trans {
    
  public TransDec(Env e){
    env = e;
  }
  
  public void transDec(Absyn.Dec d) {
    if (d instanceof Absyn.FunctionDec)
      transDec((Absyn.FunctionDec)d);
    else if (d instanceof Absyn.TypeDec)
      transDec((Absyn.TypeDec)d);
    else if (d instanceof Absyn.VarDec)
      transDec((Absyn.VarDec)d);
    else
      throw new Error("TransDec.transDec");
  }
  
  void transDec(Absyn.FunctionDec d) {
    // Loop through the functions to statisfy all the definiations
    Absyn.FunctionDec func = d;
    while (func != null){
      // First, type-check the fields while creating a record type to descrbe them
      Types.RECORD initialField = null;
      Types.RECORD fields = null;
      Absyn.FieldList param = func.params;
      while (param != null) {
        // Lookup the type in the symbol table
        Types.NAME type = (Types.NAME)env.tenv.get(param.typ);
        
        // If the type is null, it is undefined
        if (type == null) {
          error(param.pos, "type undefined");
        }
        
        // Add the type to our record
        if (initialField == null) {
          initialField = new Types.RECORD(param.name, type, null);
          fields = initialField;
        } else {
          fields.tail = new Types.RECORD(param.name, type, null);
          fields = fields.tail;
        }
        
        param = param.tail;
      }
      
      // Now figure out the return type
      Type returnType = VOID;
      if (func.result != null)
        returnType = transTy(func.result);
      
      // Add the function to the environment
      func.entry = new FunEntry(initialField, returnType);
      env.venv.put(func.name, func.entry);
      
      func = func.next;
    }
    
    // Loop through again, this time working on the function bodies
    Absyn.FunctionDec fun = d;
    while (fun != null){
      // Add the paramenters to the scope
      env.venv.beginScope();
      for (Types.RECORD parameter = fun.entry.formals; parameter != null; parameter = parameter.tail) {
        env.venv.put(parameter.fieldName, new VarEntry(parameter.fieldType));
      }
      
      // Analyze the function body
      ExpTy funcBody = transExp(fun.body);
      
      // If the function's return type and the body's return type are different, then error
      if (!funcBody.ty.coerceTo(fun.entry.result))
	      error(fun.body.pos, "function return type is incorrect");
      
      // Finished with this scope
      env.venv.endScope();
      
      fun = fun.next;
    }
  }
  
  void transDec(Absyn.TypeDec d) {
    // Go through the type decs and process them recursively
    // Create the NAME object and set it up
    Types.NAME name = new Types.NAME(d.name);
    name.bind(transTy(d.ty));
    d.entry = name;
    
    // Put the type dec in the env
    env.tenv.put(d.name, d.entry);
    
    // If there is another type, in the TypeDec, process it
    if (d.next != null)
      transDec(d.next);
  }

  void transDec(Absyn.VarDec d) {
    // Translate the value being assigned to the variable
    ExpTy init = transExp(d.init);
    
    // Get the variable's type
    Type type = null;
    if (d.typ == null) {
      // If the type is not explicit, just use the initial value's type
      type = init.ty;
    } else {
      // If the type is explicit, translate the type and check that it is compatible
      type = transTy(d.typ);
      if (!init.ty.coerceTo(type))
        error(d.pos, "types are not compatible");
    }
    
    // Add the variable to the variable environment
    d.entry = new VarEntry(type);
    env.venv.put(d.name, d.entry);
  }
  
}