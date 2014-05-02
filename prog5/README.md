# Project 5

### TODO ###

##### Translate.Translate #####
- ```java
public Exp SimpleVar(Access access, Level level);
```
- ```java public Exp FieldVar(Exp record, int index); ```
- ```java public Exp SubscriptVar(Exp array, Exp index); ```
- ```java public Exp NilExp(); ```
- ```java public Exp IntExp(int value); ```
- ```java private Tree.Exp CallExp(Level f, ExpList args, Level from); ```
- ```java public Exp OpExp(int op, Exp left, Exp right); ```
- ```java public Exp StrOpExp(int op, Exp left, Exp right); ```
- ```java public Exp RecordExp(ExpList init); ```
- ```java public Exp SeqExp(ExpList e); ```
- ```java public Exp AssignExp(Exp lhs, Exp rhs); ```
- ```java public Exp IfExp(Exp cc, Exp aa, Exp bb); ```
- ```java public Exp WhileExp(Exp test, Exp body, Label done); ```
- ```java public Exp ForExp(Access i, Exp lo, Exp hi, Exp body, Label done); ```
- ```java public Exp BreakExp(Label done); ```
- ```java public Exp LetExp(ExpList lets, Exp body); ```
- ```java public Exp ArrayExp(Exp size, Exp init); ```
- ```java public Exp VarDec(Access a, Exp init); ```
- ```java public Exp TypeDec(); ```
- ```java public Exp FunctionDec(); ```

##### Translate.IfThenElseExp #####

- ```java Tree.Stm unCx(Label tt, Label ff); ```
- ```java Tree.Exp unEx(); ```
- ```java Tree.Stm unNx(); ```
