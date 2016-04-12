// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Multiplies by repeatedly adding R0 to R2, R1 times.

// R2 = 0;
@R2
M=0
// D = R1
@R1
D=M
// i = R1, and i will decrement until 0
@i
M=D
(LOOP)
// if (i <= 0) goto END;
// use <= so that we break early and just return 0 if R1 is negative
@i
D=M
@END
D;JLE
// D = R0
@R0
D=M
// R2 = R2 + R0
@R2
M=D+M
// i = i - 1
@i
M=M-1
// goto LOOP;
@LOOP
D;JMP
(END)
