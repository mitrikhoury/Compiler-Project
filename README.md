# Compiler Project

## Overview
This project implements a simple compiler based on a custom programming language grammar. The compiler parses and processes code written using the defined set of production rules.

## Grammar Definition
The programming language follows the given BNF (Backus-Naur Form) grammar, defining syntax rules for variable declarations, function declarations, expressions, and control structures.

### Key Features
- **Library Declarations**: Supports `#include` directives.
- **Variable and Constant Declarations**: Handles `var` and `const` keyword-based declarations.
- **Data Types**: Supports `int`, `float`, and `char`.
- **Control Structures**: Includes `if`, `while`, and `repeat` loops.
- **Expressions and Operators**: Supports arithmetic (`+`, `-`, `*`, `/`, `mod`, `div`) and relational operations (`=`, `!=`, `<`, `<=`, `>`, `>=`).
- **Input/Output Statements**: Uses `cin >>` and `cout <<` for reading and printing values.
- **Function Declarations and Calls**: Allows function definition and invocation using `function` and `call`.

## Project Structure
```
compiler_project/
│── src/                  # Source code for the compiler
│── examples/             # Example programs in the custom language
│── docs/                 # Documentation and grammar definitions
│── README.md             # Project documentation
```


