import java.util.Scanner;

public class Controller {
	static PipeLineRegister pipeline[];
	static int cycle;

	public Controller() {
		pipeline = new PipeLineRegister[4];

	}
	public void loadInstructions() {
		System.out.print("Enter number of instructions :");
		Scanner sc= new Scanner(System.in);
		int n=sc.nextInt();
		System.out.println();
		while(n-->0) {
			Memory.loadInstruction(Integer.parseInt(sc.nextLine()));
		}
	}
	public void init() {
		Memory mem= new Memory();
		RegisterFile rf=new RegisterFile();
		
	}
	
	public static void nextCycle() {
		cycle++;
		
	
	}
	
	
	
	
	
	
	
	
	
	public static void fetch() {
		// Load the instruction from memory to the nextInstruction Variable of
	
		// controller.
		Controller.pipeline[0] = new PipeLineRegister();
		Controller.pipeline[0].intermediateOut = Memory.getInstruction(); // Load Instruction to First Pipeline Stage.
	}
	
	public static String InstAsString(int inst) {
		String instToString = Integer.toBinaryString(inst);
		while (instToString.length() < 32) {
			instToString = "0" + instToString;
		}
		return instToString;
	}
	
public static void decode() {
		int inst = (int) Controller.pipeline[0].intermediateOut;
		String instToDecode = InstAsString(inst);
		switch (getType(instToDecode)) {
		case "00":
			handleR(instToDecode);
			break;
		case "01":
			handleI(instToDecode);
			break;
		case "10":
			handleJ(instToDecode);
			break;
		default:
			System.out.println("Wrong Type Code Please revise your input data");
		}
	}

	// Handles J Instructions. //Not-Correct
	private static void handleJ(String instToDecode) {
		if (getOpcode(instToDecode).equals("1101")) {// .equals();;
			int jumpVal = RegisterFile.registers[Integer.parseInt(getJAddress(instToDecode), 2)];
			Memory.setPC(jumpVal);

		} else {
			System.out.println("This is not a well formatted J Type Instruction");
		}
	}

	// Need to Send more info for ALU to be able to do what it wants.
	private static void handleR(String instToDecode) {
		switch (getOpcode(instToDecode)) {
		case "0000":
			ALU.ALUEvaluator("0011", RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode), 2)],
					RegisterFile.registers[Integer.parseInt(getSrcTwo(instToDecode), 2)]);
			break;
		case "0001":
			ALU.ALUEvaluator("0010", RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode), 2)],
					RegisterFile.registers[Integer.parseInt(getSrcTwo(instToDecode), 2)]);
			break;
		case "0011":
			ALU.ALUEvaluator("0100", RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode), 2)],
					RegisterFile.registers[Integer.parseInt(getSrcTwo(instToDecode), 2)]);
			break;
		case "0100":
			ALU.ALUEvaluator("0001", RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode), 2)],
					RegisterFile.registers[Integer.parseInt(getSrcTwo(instToDecode), 2)]);
			break;
		case "0110":
			ALU.ALUEvaluator("0110", RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode), 2)],
					Integer.parseInt(getShamt(instToDecode), 2) % 32);
			break;
		case "0111":
			ALU.ALUEvaluator("0101", RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode), 2)],
					Integer.parseInt(getShamt(instToDecode), 2) % 32);
			break;
		default:
			System.out.println("This is not a correct R-Format Instruction");

		}

	}

	private static void handleI(String instToDecode) {
		switch (getOpcode(instToDecode)) {
		case "0010":
			ALU.ALUEvaluator("0010", RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode), 2)],
					RegisterFile.registers[Integer.parseInt(getImm(instToDecode), 2)]);
			break;
		case "0101":
			ALU.ALUEvaluator("0000", RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode), 2)],
					RegisterFile.registers[Integer.parseInt(getImm(instToDecode), 2)]);
			break;
		case "1000":
			ALU.ALUEvaluator("0010", RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode), 2)],
					RegisterFile.registers[Integer.parseInt(getImm(instToDecode), 2)]);
			break;
		case "1001":
			ALU.ALUEvaluator("0010", RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode), 2)],
					RegisterFile.registers[Integer.parseInt(getImm(instToDecode), 2)]);
			break;
		case "1010":
			ALU.ALUEvaluator("0011", RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode), 2)],
					RegisterFile.registers[Integer.parseInt(getImm(instToDecode), 2)]);
			break;
		case "1011":
			ALU.ALUEvaluator("0011", RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode), 2)],
					RegisterFile.registers[Integer.parseInt(getImm(instToDecode), 2)]);
			break;
		case "1100":
			ALU.ALUEvaluator("0111", RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode), 2)],
					RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode), 2)]);
			break;

		default:
			System.out.println("This is not a correct I-Format Instruction");

		}

	}

	// Not Used
	public static String SingExtender(String s) {
		while (s.length() < 32) {
			if (s.charAt(0) == '1') {
				s = "1" + s;
			} else {
				s = "0" + s;
			}
		}
		return s;
	}

	public static String getSrcOneImm(String s) {
		return s.substring(12, 17);
	}

	public static String getImm(String s) {
		return s.substring(12, 28);
	}

	public static String getType(String s) {
		return s.substring(0, 2);
	}

	public static String getJAddress(String s) {
		return s.substring(2, 7);
	}

	public static String getOpcode(String s) {
		return s.substring(28, 32);
	}

	public static String getDest(String s) {
		return s.substring(2, 7);
	}

	public static String getSrcOne(String s) {
		return s.substring(7, 12);
	}

	public static String getSrcTwo(String s) {
		return s.substring(12, 17);
	}

	public static String getShamt(String s) {
		return s.substring(17, 28);
	}


}
/*
 * /First Cycle Instruction 1 in pipeline[0] == Fetched/de (instr) Second CYcle
 * Instruction 1 in pipeline[1] == Decoded/Ex ( inst+ 2 32-bit values) Second
 * CYcle Instruction 2 in Pipeline[0] fetched
 *
 */

class EX {
	int val1, val2;
	String operation;
}

class M {
	int address;

}

class WB {
	int reg;
}

class PipeLineRegister {
	Object intermediateOut;
	EX ex;
	M m;
	WB reg;

}
/*
 * R type dest, src1,src2,operation EX : val src1, val src2, operation M: null
 * WB: dest
 */
