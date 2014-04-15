package Semant;

public class VarEntry extends Entry {
  Translate.Access access;
  public Types.Type ty;
  VarEntry(Types.Type t) {
    ty = t;
  }
}
