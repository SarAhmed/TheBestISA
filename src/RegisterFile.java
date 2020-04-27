/* Type:
 * R: 00
 * I: 01
 * J: 10
 */

/* R Type Format
Type 2
Reg1  5 Destionation
reg2   5 Source 1
reg3   5 Source 2
shamt  11 (10101010110)%(32)
OpCode 4 
 */

/*I Type
Type 2
Reg1  5 Destionation
reg2   5 Source 1
Imeediate 16
OpCode 4 
 */

/*
//J type
2 Bit Type
5 Source Address
21 Not Used
4 Opcode8 */
/*Op code list:
0. Sub. 0000
1. Add. 0001
2. Add immediate. 0010
3. Multiply.      0011
4. Or. 0100
5. And immediate. 0101
6. Shift right logical. 0110
7. Shift left logical. 0111
8. Load word. 1000
9. Store word. 1001
10. Branch on equal. 1010
11. Branch on less than. 1011
12. Set on less than immediate. 1100
13. Jump Register. 1101
 */

public class RegisterFile {
	static int registers[];

	public RegisterFile() {
		registers = new int[32];
	}

}
