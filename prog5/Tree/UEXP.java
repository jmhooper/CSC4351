package Tree;
import Temp.Temp;
import Temp.Label;
public class UEXP extends Stm {
  public Exp exp; 
  public UEXP(Exp e) {exp=e;}
  public ExpList kids() {return new ExpList(exp,null);}
  public Stm build(ExpList kids) {
    return new UEXP(kids.head);
  }
}
