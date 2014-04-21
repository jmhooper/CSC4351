package Main;

class Main {

  static Frame.Frame frame = new Mips.MipsFrame();

  static Assem.InstrList codegen(Frame.Frame f, Tree.StmList stms) {
    Assem.InstrList first = null, last = null;
    for (Tree.StmList s = stms; s != null; s = s.tail) {
      Assem.InstrList i = f.codegen(s.head);
      if (last == null) {
	if (first != null)
	  throw new Error("Main.codegen");
	first = last = i;
      } else {
	while (last.tail != null)
	  last = last.tail;
	last = last.tail = i;
      }
    }
    return first;
  }


  static void emitProc(java.io.PrintWriter out, Translate.ProcFrag f) {
    java.io.PrintWriter debug = 
      /* new java.io.PrintWriter(new NullOutputStream()); */
      out;
    Temp.TempMap tempmap = new Temp.CombineMap(f.frame, new Temp.DefaultMap());
    Tree.Print print = new Tree.Print(debug, tempmap);
    debug.println("PROCEDURE " + f.frame.name);
    Assem.InstrList instrs = null;
    if (f.body != null) {
      debug.println("# Before canonicalization: ");
      print.prStm(f.body);
      debug.println("# After canonicalization: ");
      Tree.StmList stms = Canon.Canon.linearize(f.body);
      print.prStmList(stms);
      debug.println("# Basic Blocks: ");
      Canon.BasicBlocks b = new Canon.BasicBlocks(stms);
      for(Canon.StmListList l = b.blocks; l!=null; l=l.tail) {
	debug.println("#");
	print.prStmList(l.head);
      }
      print.prStm(new Tree.LABEL(b.done));
      debug.println("# Trace Scheduled: ");
      Tree.StmList traced = (new Canon.TraceSchedule(b)).stms;
      print.prStmList(traced);
      instrs = codegen(f.frame, traced);
    }
    debug.println("# Instructions: ");
    for(Assem.InstrList p = instrs; p != null; p = p.tail)
      debug.println(p.head.format(tempmap));
    debug.println("END " + f.frame.name);
    debug.flush();
  }

  public static void main(String args[]) throws java.io.IOException {
    for (int i = 0; i < args.length; ++i) {
      String src = args[i];
      if (src.endsWith(".tig")) {
	if (args.length > 1)
	  System.out.println("***Compiling: " + src);
	Parse.Parse parse = new Parse.Parse(src);
	String dst = src.substring(0, src.lastIndexOf(".tig")) + ".s";
	Translate.Translate translate = new Translate.Translate(frame);
	Semant.Semant semant = new Semant.Semant(translate, parse.errorMsg);
	Translate.Frag frags = semant.transProg(parse.absyn);
	if (!parse.errorMsg.anyErrors) {
	  java.io.PrintWriter out =
	    new java.io.PrintWriter(new java.io.FileOutputStream(dst));
	  for(Translate.Frag f = frags; f!=null; f=f.next)
	    if (f instanceof Translate.ProcFrag)
	      emitProc(out, (Translate.ProcFrag)f);
	    else if (f instanceof Translate.DataFrag)
	      out.println(((Translate.DataFrag)f).data);
	  out.close();
	}
      } else
	System.err.println
	  ("File extension is not \".tig\": ignoring " + src);
    }
  }

}

class NullOutputStream extends java.io.OutputStream {
  public void write(int b) {}
}
