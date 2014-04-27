package Mips;
import java.util.Hashtable;
import Symbol.Symbol;
import Temp.Temp;
import Temp.Label;
import Frame.Frame;
import Frame.Access;
import Frame.AccessList;

public class MipsFrame extends Frame {

  private int count = 0;
  
  // Instance variables
  int offset = 0;
  private final int wordSize = 4;
  
  // Getters
  public int wordSize() { return wordSize; }
  
  // Constructors
  public MipsFrame() {}
    
  private MipsFrame(Label n, Util.BoolList f) {
    name = n;
    
    // Process the bool list
    formals = traverseFormals(0, f);
  }
  
  private AccessList traverseFormals(int offset, Util.BoolList formals) {
    Access access;
    if(formals == null) {
      return null;
    } else if (formals.head) {
      // If there is a formal add a InFrame
      access = new InFrame(offset);
    } else {
      // Otherwise and an InReg
      access = new InReg(new Temp());
    }
    
    return new AccessList(access, traverseFormals(offset + wordSize, formals.tail));
  }
  
  public Frame newFrame(Symbol name, Util.BoolList formals) {
    Label label;
    if (name == null)
      label = new Label();
    else if (this.name != null)
      label = new Label(this.name + "." + name + "." + count++);
    else
      label = new Label(name);
    return new MipsFrame(label, formals);
  }
  
  
  public Access allocLocal(boolean escape) {
    if (!escape) {
      // Unescaped variables go to a reg
      return new InReg(new Temp());
    } else {
      // If the variables escapes it goes into the frame with an offset of MIPS's word size
      offset = offset - wordSize;
      return new InFrame(offset);
    }
  } 
}
