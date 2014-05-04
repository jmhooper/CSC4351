PROCEDURE tigermain
# Before canonicalization: 
MOVE(
 TEMP $v0,
 BINOP(PLUS,
  CONST 5,
  CONST 5))
# After canonicalization: 
MOVE(
 TEMP $v0,
 BINOP(PLUS,
  CONST 5,
  CONST 5))
# Basic Blocks: 
#
LABEL L1
MOVE(
 TEMP $v0,
 BINOP(PLUS,
  CONST 5,
  CONST 5))
JUMP(
 NAME L0)
LABEL L0
# Trace Scheduled: 
LABEL L1
MOVE(
 TEMP $v0,
 BINOP(PLUS,
  CONST 5,
  CONST 5))
JUMP(
 NAME L0)
LABEL L0
# Instructions: 
L1:
li t34,5
li t35,5
add t33,t34,t35
move $v0,t33
j L0
L0:
END tigermain
