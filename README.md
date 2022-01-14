# Puzzle Solver


This is an University project, the goal is to solve a Puzzle as fast as possible (although it's in Java).

## What I Did

To achieve the good enough result I applied a compression to the input matrices which will become an array of long so that each number occupies the minimum required space. On the other hand, the heuristics as well as the usual manhattan distace there is a control over all the linear conflicts that can be created.

## Linear conflits

## How run the program

Very simple, compile:

    javac Solver.java Board.java

After you need a file to read from, specified in line arguments:

    java Solver file

### File format

    3
    0 1 3 4 2 5 7 8 6

The first line contain the side of the puzzle and the next line all the numbers in the puzzle