package Semant;
import Translate.Exp;
import Types.Type;

public class TransTy extends Trans {
  
 public TransTy(Env e) {
   env = e;
 }
 
 public Type transTy(Absyn.Ty t) {
   if (t instanceof Absyn.ArrayTy)
     return transTy((Absyn.ArrayTy)t);
   else if (t instanceof Absyn.NameTy)
     return transTy((Absyn.NameTy)t);
   else if (t instanceof Absyn.RecordTy)
     return transTy((Absyn.RecordTy)t);
   else
     throw new Error("TransTy.transTy");
 }
 
 Type transTy(Absyn.ArrayTy t) {
    // Load the type from the type environment
    Types.NAME type = (Types.NAME)env.tenv.get(t.typ);

    // If no type is found, there is an error
    if (type == null) {
      error(t.pos, "array of type " + t.typ + " is not defined: ");
      return VOID;
    }
    
    // Return the array type with the type found in the environment
    return new Types.ARRAY(type);
 }
 
 Type transTy(Absyn.NameTy t) {
   // Load the type from the type environment
   Types.NAME type = (Types.NAME)env.tenv.get(t.name);
   
   // If no type is found, there is an error
   if (type == null){
     error(t.pos, "type is not defined: " + t.name);
     return VOID;
   }
   
   // Return the type that was loaded from the environment
   return type;
 }
 
 Type transTy(Absyn.RecordTy t) {
   Types.RECORD firstRecord = null;
   Types.RECORD record = null;
   Absyn.FieldList field = t.fields;
   
   // Go through the fields and add them to the record
   while(field != null) {
     // Load the type from the type environment
     Types.NAME type = (Types.NAME)env.tenv.get(field.typ);
     
     // if the type is undefined, alert the user
     if (type == null) {
       error(t.pos, "type is not defined: " + field.typ);
     }
     
     // Create a new record with the pair
     if (firstRecord == null) {
       firstRecord = new Types.RECORD(field.name, type, null);
       record = firstRecord;
     } else {
       record.tail = new Types.RECORD(field.name, type, null);
       record = record.tail;
     }
     //record = new Types.RECORD(field.name, type, record);
     
     // Go to the next field
     field = field.tail;
   }
   
   return firstRecord;
 }
  
}