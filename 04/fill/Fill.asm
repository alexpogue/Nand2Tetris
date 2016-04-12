// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.

// inspiration from question at http://stackoverflow.com/q/3145433/3846218

// i = 0
@i
M=0
(LOOP)
// color = 0xFFFF (black)
D=0
D=!D
@color
M=D

// D = key code pressed
@KBD
D=M
// if (key is pressed) goto SKIP_WHITE
@SKIP_WHITE
D;JNE

// color = 0 (white) only if no keys are pressed
@color
M=0

(SKIP_WHITE)
// D = i
@i
D=M
// D = address(SCREEN) + i
@SCREEN
D=D+A
// target = address(SCREEN) + i
@target
M=D
// D = color
@color
D=M
// value(address(target)) = color
@target
A=M // write to address specified by value of target
M=D

// D = 8191 (max address for screen memory)
@8191
D=A
// i = i + 1
@i
M=M+1
// i = i & 8191 (wrap around to 0 when we get to max screen address)
M=D&M

@LOOP
0;JMP // infinite loop
