package Translate;
import Symbol.Symbol;
import Tree.BINOP;
import Tree.CJUMP;
import Temp.Temp;
import Temp.Label;

public class Translate {
  public Frame.Frame frame;
  public Translate(Frame.Frame f) {
    frame = f;
  }
  private Frag frags;
  public void procEntryExit(Level level, Exp body) {
    Frame.Frame myframe = level.frame;
    Tree.Exp bodyExp = body.unEx();
    Tree.Stm bodyStm;
    if (bodyExp != null)
      bodyStm = MOVE(TEMP(myframe.RV()), bodyExp);
    else
      bodyStm = body.unNx();
    ProcFrag frag = new ProcFrag(myframe.procEntryExit1(bodyStm), myframe);
    frag.next = frags;
    frags = frag;
  }
  public Frag getResult() {
    return frags;
  }

  private static Tree.Exp CONST(int value) {
    return new Tree.CONST(value);
  }
  private static Tree.Exp NAME(Label label) {
    return new Tree.NAME(label);
  }
  private static Tree.Exp TEMP(Temp temp) {
    return new Tree.TEMP(temp);
  }
  private static Tree.Exp BINOP(int binop, Tree.Exp left, Tree.Exp right) {
    return new Tree.BINOP(binop, left, right);
  }
  private static Tree.Exp MEM(Tree.Exp exp) {
    return new Tree.MEM(exp);
  }
  private static Tree.Exp CALL(Tree.Exp func, Tree.ExpList args) {
    return new Tree.CALL(func, args);
  }
  private static Tree.Exp ESEQ(Tree.Stm stm, Tree.Exp exp) {
    if (stm == null)
      return exp;
    return new Tree.ESEQ(stm, exp);
  }

  private static Tree.Stm MOVE(Tree.Exp dst, Tree.Exp src) {
    return new Tree.MOVE(dst, src);
  }
  private static Tree.Stm UEXP(Tree.Exp exp) {
    return new Tree.UEXP(exp);
  }
  private static Tree.Stm JUMP(Label target) {
    return new Tree.JUMP(target);
  }
  private static
  Tree.Stm CJUMP(int relop, Tree.Exp l, Tree.Exp r, Label t, Label f) {
    return new Tree.CJUMP(relop, l, r, t, f);
  }
  private static Tree.Stm SEQ(Tree.Stm left, Tree.Stm right) {
    if (left == null)
      return right;
    if (right == null)
      return left;
    return new Tree.SEQ(left, right);
  }
  private static Tree.Stm LABEL(Label label) {
    return new Tree.LABEL(label);
  }

  private static Tree.ExpList ExpList(Tree.Exp head, Tree.ExpList tail) {
    return new Tree.ExpList(head, tail);
  }
  private static Tree.ExpList ExpList(Tree.Exp head) {
    return ExpList(head, null);
  }
  private static Tree.ExpList ExpList(ExpList exp) {
    if (exp == null)
      return null;
    return ExpList(exp.head.unEx(), ExpList(exp.tail));
  }

  public Exp Error() {
    return new Ex(CONST(666));
  }

  public Exp SimpleVar(Access access, Level level) {
    // A variable to hold the frame pointer and the level
    Tree.Exp framePointer = TEMP(frame.FP());
    Level lev = level;
    
    // Go up the levels to find the one where the variable was declared
    // Along the way update the frame pointer
    while (lev != access.home) {
      framePointer = level.frame.formals.head.exp(framePointer);
      lev = level.parent;
    }
    
    // Get the variable's location
    Tree.Exp location = access.acc.exp(framePointer);
    
    // Return a Ex with the variables location as its value
    return new Ex(location);
  }

  public Exp FieldVar(Exp record, int index) {
    // Calculate the offset
    int offset = index * frame.wordSize();
    
    // Move the record pointer into a new register
    Temp pointerRegister = new Temp();
    Tree.Stm movePointerStm = MOVE(TEMP(pointerRegister), record.unEx());
    
    // Find the value of the field var at pointerRegister + offset
    Tree.Exp loadPointerExp = MEM(BINOP(Tree.BINOP.PLUS, TEMP(pointerRegister), CONST(offset)));
    
    // Return the stm and the value loaded in an ESEQ
    return new Ex(ESEQ(movePointerStm, loadPointerExp));
  }

  public Exp SubscriptVar(Exp array, Exp index) {
    // Calculate the offset
    Tree.Exp offset = BINOP(Tree.BINOP.MUL, index.unEx(),  CONST(frame.wordSize()));
    
    // Move the record pointer into a new register
    Temp pointerRegister = new Temp();
    Tree.Stm movePointerStm = MOVE(TEMP(pointerRegister), array.unEx());
    
    // Find the value of the element at pointerRegister + offset
    Tree.Exp loadPointerExp = MEM(BINOP(Tree.BINOP.PLUS, TEMP(pointerRegister), offset));
    
    // Return the stm and the value loaded in an ESEQ
    return new Ex(ESEQ(movePointerStm, loadPointerExp));
  }

  public Exp NilExp() {
    // Just return the integer value 0
    return new Ex(CONST(0));
  }

  public Exp IntExp(int value) {
    // ints are nodes
    return new Ex(CONST(value));
  }

  private java.util.Hashtable strings = new java.util.Hashtable();
  public Exp StringExp(String lit) {
    String u = lit.intern();
    Label lab = (Label)strings.get(u);
    if (lab == null) {
      lab = new Label();
      strings.put(u, lab);
      DataFrag frag = new DataFrag(frame.string(lab, u));
      frag.next = frags;
      frags = frag;
    }
    return new Ex(NAME(lab));
  }

  private Tree.Exp CallExp(Symbol f, ExpList args, Level from) {
    return frame.externalCall(f.toString(), ExpList(args));
  }
  private Tree.Exp CallExp(Level f, ExpList args, Level from) {
    // A temporary frame pointer and level
    Tree.Exp framePointer = TEMP(from.frame.FP());
    Level lev = from;
    
    // Go up the levels to find the level that f was defined in
    while (lev != f.parent) {
      framePointer = lev.frame.formals.head.exp(framePointer);
      lev = lev.parent;
    }
    
    // Figure out the functions name and arguments
    // Add the frame pointer to the beginning of f's args
    Tree.Exp func = NAME(f.frame.name);
    Tree.ExpList argsWithPointer = ExpList(framePointer, ExpList(args));
    
    // Return the tree.exp for the function call
    return CALL(func, argsWithPointer);
  }

  public Exp FunExp(Symbol f, ExpList args, Level from) {
    return new Ex(CallExp(f, args, from));
  }
  public Exp FunExp(Level f, ExpList args, Level from) {
    return new Ex(CallExp(f, args, from));
  }
  public Exp ProcExp(Symbol f, ExpList args, Level from) {
    return new Nx(UEXP(CallExp(f, args, from)));
  }
  public Exp ProcExp(Level f, ExpList args, Level from) {
    return new Nx(UEXP(CallExp(f, args, from)));
  }

  public Exp OpExp(int op, Exp left, Exp right) {
    return new Ex(BINOP(op, left.unEx(), right.unEx()));
  }

  public Exp StrOpExp(int op, Exp left, Exp right) {
    return new RelCx(op, left.unEx(), right.unEx());
  }

  public Exp RecordExp(ExpList init) {
    // Count the number of arguments
    int fieldCount = 0;
    for (ExpList field = init; field != null; field = field.tail) {
      fieldCount++;
    }
    
    // Create an empty record
    Temp location = new Temp();
    Tree.Stm recordCreateStm = MOVE(TEMP(location), frame.externalCall("allocRecord", ExpList(CONST(fieldCount))));
    
    // Add the fields
    Tree.Stm fields = recordFields(init, location, 0);
    
    // Put all the values together and return
    return new Ex(ESEQ(SEQ(recordCreateStm, fields), TEMP(location)));
  }
  
  Tree.Stm recordFields(ExpList field, Temp initialLocation, int offset) {
    // Calculate the heads location
    Tree.Stm moveCurrentFieldStm = MOVE(MEM(BINOP(Tree.BINOP.PLUS, TEMP(initialLocation), CONST(offset))), field.head.unEx());
    
    if (field.head == null) {
      // If the head is null, there is a problem, just give a null
      return null;
    } else if (field.tail == null) {
      // If the tail is null, just the move sequence
      return moveCurrentFieldStm;
    } else {
      // If both are present, return a new list recusively
      Tree.Stm nextField = recordFields(field.tail, initialLocation, offset + frame.wordSize());
      
      return SEQ(moveCurrentFieldStm, nextField);
    }
  }
  
  public Exp SeqExp(ExpList e) {
    if (e.head == null) {
      // If the head is null, return null
      return NilExp();
    } else if (e.tail == null) {
      // If the tail is null, just return the head
      return new Ex(e.head.unEx());
    } else {
      // If both are present, return a new sequence
      Tree.Stm left = e.head.unNx();
      Tree.Exp right = SeqExp(e.tail).unEx();
      
      // Return the new sequence
      return new Ex(ESEQ(left, right));
    }
  }
  
  private Tree.Stm SeqStm(ExpList e) {
    if (e.head == null) {
      // If the head is null, no need for a tree, return null
      return null;
    } else if (e.tail == null) {
      // If the tail is null, just return the head
      return e.head.unNx();
    } else {
      // If both are present, return a new sequence
      Tree.Stm left = e.head.unNx();
      Tree.Stm right = SeqStm(e.tail);
      
      // Return the new sequence
      return SEQ(left, right);
    }
  }

  public Exp AssignExp(Exp lhs, Exp rhs) {
    return new Nx(MOVE(lhs.unEx(), rhs.unEx()));
  }

  public Exp IfExp(Exp cc, Exp aa, Exp bb) {
    return new IfThenElseExp(cc, aa, bb);
  }

  public Exp WhileExp(Exp test, Exp body, Label done) {
    // Create labels to jump between
    Label testLabel = new Label();
    Label bodyLabel = new Label();
    
    // Now create tree.stms for those lables
    Tree.Stm testStm = SEQ(LABEL(testLabel), test.unCx(bodyLabel, done));
    Tree.Stm bodyStm = SEQ(LABEL(bodyLabel), body.unNx());
    
    Tree.Stm left = SEQ(testStm, SEQ(bodyStm, JUMP(testLabel)));
    
    return new Nx(SEQ(left, LABEL(done)));
  }

  public Exp ForExp(Access i, Exp lo, Exp hi, Exp body, Label done) {
    return Error();
  }

  public Exp BreakExp(Label done) {
    return new Nx(JUMP(done));
  }

  public Exp LetExp(ExpList lets, Exp body) {
    // First handle the declerations
    Tree.Stm decsStm = SeqStm(lets);
    
    // Now handle the body
    Tree.Exp bodyExp = body.unEx();
    
    if (decsStm != null) {
      // If there are decs, add them to an ESEQ with the body on the right
      return new Ex(ESEQ(decsStm, bodyExp));
    } else {
      // No decs, just return the body
      return new Ex(bodyExp);
    }
  }

  public Exp ArrayExp(Exp size, Exp init) {
    return new Ex(frame.externalCall("initArray", ExpList(size.unEx(), ExpList(init.unEx()))));
  }

  public Exp VarDec(Access a, Exp init) {
    // Load the frame pointer
    Tree.Exp framePointer = TEMP(a.home.frame.FP());
    
    // Get the variable's destination
    Tree.Exp destination = a.acc.exp(framePointer);
    
    // Process the initial value
    Tree.Exp initialValue = init.unEx();
    
    // Return a stm that moves the initial value to the destination
    return new Nx(MOVE(destination, initialValue));
  }

  public Exp TypeDec() {
    return new Nx(null);
  }

  public Exp FunctionDec() {
    return new Nx(null);
  }
}
