PROCEDURE tigermain
# Before canonicalization: 
SEQ(
 SEQ(
  MOVE(
   TEMP t33,
   CONST 1),
  MOVE(
   TEMP t34,
   CONST 3)),
 SEQ(
  SEQ(
   SEQ(
    MOVE(
     TEMP t33,
     CALL(
      NAME tigermain.add.0,
       TEMP $fp,
       TEMP t33,
       TEMP t34)),
    MOVE(
     TEMP t34,
     BINOP(PLUS,
      CONST 5,
      TEMP t33))),
   MOVE(
    TEMP t33,
    BINOP(PLUS,
     CONST 7,
     CONST 2))),
  MOVE(
   TEMP t34,
   BINOP(PLUS,
    TEMP t33,
    CONST 4))))
# After canonicalization: 
MOVE(
 TEMP t33,
 CONST 1)
MOVE(
 TEMP t34,
 CONST 3)
MOVE(
 TEMP t33,
 CALL(
  NAME tigermain.add.0,
   TEMP $fp,
   TEMP t33,
   TEMP t34))
MOVE(
 TEMP t34,
 BINOP(PLUS,
  CONST 5,
  TEMP t33))
MOVE(
 TEMP t33,
 BINOP(PLUS,
  CONST 7,
  CONST 2))
MOVE(
 TEMP t34,
 BINOP(PLUS,
  TEMP t33,
  CONST 4))
# Basic Blocks: 
#
LABEL L1
MOVE(
 TEMP t33,
 CONST 1)
MOVE(
 TEMP t34,
 CONST 3)
MOVE(
 TEMP t33,
 CALL(
  NAME tigermain.add.0,
   TEMP $fp,
   TEMP t33,
   TEMP t34))
MOVE(
 TEMP t34,
 BINOP(PLUS,
  CONST 5,
  TEMP t33))
MOVE(
 TEMP t33,
 BINOP(PLUS,
  CONST 7,
  CONST 2))
MOVE(
 TEMP t34,
 BINOP(PLUS,
  TEMP t33,
  CONST 4))
JUMP(
 NAME L0)
LABEL L0
# Trace Scheduled: 
LABEL L1
MOVE(
 TEMP t33,
 CONST 1)
MOVE(
 TEMP t34,
 CONST 3)
MOVE(
 TEMP t33,
 CALL(
  NAME tigermain.add.0,
   TEMP $fp,
   TEMP t33,
   TEMP t34))
MOVE(
 TEMP t34,
 BINOP(PLUS,
  CONST 5,
  TEMP t33))
MOVE(
 TEMP t33,
 BINOP(PLUS,
  CONST 7,
  CONST 2))
MOVE(
 TEMP t34,
 BINOP(PLUS,
  TEMP t33,
  CONST 4))
JUMP(
 NAME L0)
LABEL L0
# Instructions: 
L1:
li t38,1
move t33,t38
li t39,3
move t34,t39
	addu t40 $sp tigermain_framesize
	move $a0 t40
	move $a1 t33
	move $a2 t34
	jal Tree.NAME@68346762
move t33,$v0
add t41,5, t33
move t34,t41
add t42,7,2
move t33,t42
add t43, t33,4
move t34,t43
j L0
L0:
END tigermain
PROCEDURE tigermain.add.0
# Before canonicalization: 
MOVE(
 TEMP $v0,
 BINOP(PLUS,
  TEMP t36,
  TEMP t37))
# After canonicalization: 
MOVE(
 TEMP $v0,
 BINOP(PLUS,
  TEMP t36,
  TEMP t37))
# Basic Blocks: 
#
LABEL L3
MOVE(
 TEMP $v0,
 BINOP(PLUS,
  TEMP t36,
  TEMP t37))
JUMP(
 NAME L2)
LABEL L2
# Trace Scheduled: 
LABEL L3
MOVE(
 TEMP $v0,
 BINOP(PLUS,
  TEMP t36,
  TEMP t37))
JUMP(
 NAME L2)
LABEL L2
# Instructions: 
L3:
add t44, t36,t37
move $v0,t44
j L2
L2:
END tigermain.add.0
