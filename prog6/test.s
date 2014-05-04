PROCEDURE tigermain
# Before canonicalization: 
MOVE(
 TEMP $v0,
 ESEQ(
  MOVE(
   TEMP t33,
   CONST 0),
  ESEQ(
   MOVE(
    TEMP t33,
    CALL(
     NAME tigermain.hello.0,
      TEMP $fp)),
   BINOP(PLUS,
    TEMP t33,
    CONST 5))))
# After canonicalization: 
MOVE(
 TEMP t33,
 CONST 0)
MOVE(
 TEMP t33,
 CALL(
  NAME tigermain.hello.0,
   TEMP $fp))
MOVE(
 TEMP $v0,
 BINOP(PLUS,
  TEMP t33,
  CONST 5))
# Basic Blocks: 
#
LABEL L1
MOVE(
 TEMP t33,
 CONST 0)
MOVE(
 TEMP t33,
 CALL(
  NAME tigermain.hello.0,
   TEMP $fp))
MOVE(
 TEMP $v0,
 BINOP(PLUS,
  TEMP t33,
  CONST 5))
JUMP(
 NAME L0)
LABEL L0
# Trace Scheduled: 
LABEL L1
MOVE(
 TEMP t33,
 CONST 0)
MOVE(
 TEMP t33,
 CALL(
  NAME tigermain.hello.0,
   TEMP $fp))
MOVE(
 TEMP $v0,
 BINOP(PLUS,
  TEMP t33,
  CONST 5))
JUMP(
 NAME L0)
LABEL L0
# Instructions: 
L1:
move t33,$0
	addu t35 $sp tigermain_framesize
	move $a0 t35
	jal Tree.NAME@306f7492
move t33,$v0
li t37,5
add t36,t33,t37
move $v0,t36
j L0
L0:
END tigermain
PROCEDURE tigermain.hello.0
# Before canonicalization: 
MOVE(
 TEMP $v0,
 CONST 5)
# After canonicalization: 
MOVE(
 TEMP $v0,
 CONST 5)
# Basic Blocks: 
#
LABEL L3
MOVE(
 TEMP $v0,
 CONST 5)
JUMP(
 NAME L2)
LABEL L2
# Trace Scheduled: 
LABEL L3
MOVE(
 TEMP $v0,
 CONST 5)
JUMP(
 NAME L2)
LABEL L2
# Instructions: 
L3:
li t38,5
move $v0,t38
j L2
L2:
END tigermain.hello.0
