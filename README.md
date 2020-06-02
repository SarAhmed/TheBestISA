# Pipelined MIPS datapath simulator
* Implemented with Java
### Description
* This simulator is a low-level cycle-accurate pipelined MIPS datapath simulator that simulates the datapath including all of its storage components (register file, memories, and pipeline registers) and all of its control signals.

### Stages of the pipeline
* IF : Instruction Fetch Stage
* ID : Instruction Decode Stage
* EX : Execution Stage
* MEM : Memory Stage
* WB : Write Back Stage
### Implemented Instruction Set
* Arithmetic Operations: add, sub, addi, mul
* Logic Operations: or, andi, srl, sll
* Data Transfer: lw, sw
* Conditional Branch: beq, blt
* Unconditional Jump : j
* Comparison: slti
### Implementation notes
* The register file contains 32 general purpose registers of size 32-bit.
* The chosen microarchitecture is the Von Neumann architecture having one memory for both data and instructions.
* In order to protect and handle our memory safely we adopted the no-execute bit approach which is an array of bits (Booleans) to             differentiate the data from the instructions to prevent data from being executed
* The instruction and data memory combined have a size of 2048 words (2048 * 32 bits).
* The Cache has a size of 512 words and use the direct mapping replacement policy using tag and index and valid bits.
### Simulation
* The Whole Programm is given as an input to the Simulator and it outputs all cycles info like :
  in clock cycle 2, what instruction is in each stage of the five stages in the
  pipelining, what are the operands, their values and the control signals values.
### More Details 
  * A Detailed Report for each component and detailed output
      > [Report](CA_Report.pdf)
