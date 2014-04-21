package Frame;

public class Proc {
  public String prologue, epilogue;
  public Assem.InstrList body;

  public Proc(String p, Assem.InstrList b, String e) {
    prologue = p;
    body = b;
    epilogue = e;
  }
}
