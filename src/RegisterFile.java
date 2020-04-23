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
	public static void fetch() {
		//Load the instruction from memory to the nextInstruction Variable of controller.
		Controller.pipeline[0]= Memory.getInstruction(); //Load Instruction to First Pipeline Stage.
	}
	//Convert int Instruction to String for ease of manipulation.
	public static String InstAsString(int inst) {
		String instToString = Integer.toBinaryString(inst);
		while(instToString.length()<32) {
			instToString = "0"+instToString;
		}
		return instToString;
	}

	//Determine the Type of instruction to deal with.
	public static void decode() {
		int inst = Controller.pipeline[1];
		String instToDecode= InstAsString(inst);
		switch(getType(instToDecode)) {
		case "00" : handleR(instToDecode);break;
		case "01" : handleI(instToDecode);break;
		case "10" : handleJ(instToDecode);break;
		default: System.out.println("Wrong Type Code Please revise your input data");
		}
	}
	//Handles J Instructions. //Not-Correct
	private static void handleJ(String instToDecode) {
		if(getOpcode(instToDecode)=="1101") {
			int jumpVal = RegisterFile.registers[Integer.parseInt(getJAddress(instToDecode),2)];
			Memory.setPC(jumpVal);

		}else {
			System.out.println("This is not a well formatted J Type Instruction");
		}
	}
	//Need to Send more info for ALU to be able to do what it wants.
	private static void handleR(String instToDecode) {
		switch(getOpcode(instToDecode)) {
		case "0000" : ALU.ALUEvaluator("0011",RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode),2)],RegisterFile.registers[Integer.parseInt(getSrcTwo(instToDecode),2)]);break;
		case "0001":  ALU.ALUEvaluator("0010",RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode),2)],RegisterFile.registers[Integer.parseInt(getSrcTwo(instToDecode),2)]);break;
		case "0011" : ALU.ALUEvaluator("0100",RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode),2)],RegisterFile.registers[Integer.parseInt(getSrcTwo(instToDecode),2)]);break;
		case "0100" : ALU.ALUEvaluator("0001",RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode),2)],RegisterFile.registers[Integer.parseInt(getSrcTwo(instToDecode),2)]);break;
		case "0110" : ALU.ALUEvaluator("0110",RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode),2)],Integer.parseInt(getShamt(instToDecode),2)%32);break;
		case "0111" : ALU.ALUEvaluator("0101",RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode),2)],Integer.parseInt(getShamt(instToDecode),2)%32);break;
		case "1100" : ALU.ALUEvaluator("0111",RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode),2)],RegisterFile.registers[Integer.parseInt(getSrcTwo(instToDecode),2)]);break;
		default : System.out.println("This is not a correct R-Format Instruction");

		}

	}
	private static void handleI(String instToDecode) {
		switch(getOpcode(instToDecode)) {
		case "0010" : ALU.ALUEvaluator("0010",RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode),2)],RegisterFile.registers[Integer.parseInt(getImm(instToDecode),2)]);break;
		case "0101" : ALU.ALUEvaluator("0000",RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode),2)],RegisterFile.registers[Integer.parseInt(getImm(instToDecode),2)]);break;
		case "1000" : ALU.ALUEvaluator("0010",RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode),2)],RegisterFile.registers[Integer.parseInt(getImm(instToDecode),2)]);break;
		case "1001" : ALU.ALUEvaluator("0010",RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode),2)],RegisterFile.registers[Integer.parseInt(getImm(instToDecode),2)]);break;
		case "1010" : ALU.ALUEvaluator("0011",RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode),2)],RegisterFile.registers[Integer.parseInt(getImm(instToDecode),2)]);break;
		case "1011" : ALU.ALUEvaluator("0011",RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode),2)],RegisterFile.registers[Integer.parseInt(getImm(instToDecode),2)]);break;

		default : System.out.println("This is not a correct I-Format Instruction");

		}

	}
	//Not Used
	public static String SingExtender(String s) {
		while(s.length()<32) {
			if(s.charAt(0)=='1') {
				s="1"+s;
			}else {
				s="0"+s;
			}
		}
		return s;
	}
	public static String getSrcOneImm(String s) {
		return s.substring(12,17);
	}
	public static String getImm(String s) {
		return s.substring(12,28);
	}
	public static String getType(String s) {
		return s.substring(0,2);
	}
	public static String getJAddress(String s) {
		return s.substring(2,7);
	}
	public static String getOpcode(String s) {
		return s.substring(28,32);
	}
	public static String getDest(String s) {
		return s.substring(2,7);
	}
	public static String getSrcOne(String s) {
		return s.substring(7,12);
	}
	public static String getSrcTwo(String s) {
		return s.substring(12,17);
	}
	public static String getShamt(String s) {
		return s.substring(17,28);
	}
}
