
import java.util.Arrays;
import java.util.Scanner;


public class Controller {
	static PipeLineRegister pipeline[];
	static int cycle;
	
public static void main(String[] args) {
		init();
		int numInstructions = Memory.readCode() + 4;
		System.out.println(numInstructions);
		while (numInstructions-- > 0) {
			nextCycle();
			System.out.println("After clock cycle: " + cycle);
			System.out.println();
			for (int i = 4; i >0; i--) {
				pipeline[i] = pipeline[i - 1];
			}			
		//	System.out.println(Arrays.toString(pipeline)+"**************************");

			if (numInstructions >= 4)
				fetch();
			if (numInstructions >= 3 )
				decode();
			if (numInstructions >= 2)
				execute();
			if (numInstructions >= 1)
				Mem();
			WB();
		}
	}
	public Controller() {
		pipeline = new PipeLineRegister[5];
		// loadProgram[];
//		init();
		

	}

	public void loadInstructions() {
		System.out.print("Enter number of instructions :");
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		System.out.println();
		while (n-- > 0) {
			Memory.loadInstruction(Integer.parseInt(sc.nextLine()));
		}
	}
	
	public static void init() {
		Memory mem = new Memory();
		RegisterFile rf = new RegisterFile();
		new Controller();

	}

	public static void nextCycle() {
		cycle++;

	}

	public static void execute() {
		PipeLineRegister pr = Controller.pipeline[2];
		if(Controller.pipeline[2]==null)return;

		if (pr.AlUop != null) {
			int res = 0;
			if (pr.ALUsrc) {
				res = ALU.ALUEvaluator(pr.AlUop, pr.src1Val, pr.ImmediateVal);
			} else {
				res = ALU.ALUEvaluator(pr.AlUop, pr.src1Val, pr.src2Val);

			}

			pr.ALUresult = res;
			pr.Zero = ALU.isZero();
		}

		/*
		 * 
		 * zero flag: 1 branch address: 0000 0000 0000 0000 0000 0000 0000 1000 ALU
		 * result/address: 0000 0000 0000 0000 0000 0000 0000 0000 register value to
		 * write to memory: 0000 0000 0000 0000 0000 0000 0000 0000 rt/rd register:
		 * 01001 WB controls: MemToReg: 0, RegWrite: 1 MEM controls: MemRead: 1,
		 * MemWrite: 0, Branch: 0
		 */
		String instToDecode = pr.instruction;
		String src1 = getSrcOne(instToDecode);
		String src2 = getSrcTwo(instToDecode);
		String dest = getDest(instToDecode);
		String src1Val = InstAsString(RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode), 2)]);
		String src2Val = InstAsString(RegisterFile.registers[Integer.parseInt(getSrcTwo(instToDecode), 2)]);
		String shamt = getShamt(instToDecode);
		String ImmVal = SingExtender(getImm(instToDecode));
		String jumpVal = InstAsString(RegisterFile.registers[Integer.parseInt(getJAddress(instToDecode), 2)]);
		System.out.println("execute stage");
		System.out.println("Instruction "+pr.instruction);
		
		System.out.println("Zero flag: " + pr.Zero);
		System.out.println("ALU result/addres: " + pr.ALUresult);
		System.out.println("register value to write to memory: " + InstAsString(pr.toMemoryVal));
		System.out.println("Read data 1: " + src1Val);
		System.out.println("Read data 2: " + src2Val);
		System.out.println("Immediate Value (Sign-Extended): " + ImmVal);
		System.out.println("Next PC: " + InstAsString(Memory.PC));
		System.out.println("Src1: " + src1);
		System.out.println("Src2: " + src2);
		System.out.println("Dest: " + dest);
		System.out.println("Shift amount: " + shamt);
		System.out.println("Jump Value: " + jumpVal);

		System.out.println("Control signals -->");
		System.out.printf("WB control: memToReg-> %d, RegWrite-> %d\n", (pr.memToReg ? 1 : 0), (pr.RegWrite ? 1 : 0));
		System.out.printf("Memory control: memRead-> %d, memWrite-> %d, branch-> %d, jump-> %d\n", (pr.memRead ? 1 : 0),
				(pr.memWrite ? 1 : 0), (pr.branch ? 1 : 0), (pr.jump ? 1 : 0));
//		System.out.printf("EX control: ALUsrc-> %d, ALUop-> %s, regDest-> %d, shamt->%d\n", (pr.ALUsrc ? 1 : 0),
//				pr.AlUop, (pr.regDest ? 1 : 0), (pr.shamt ? 1 : 0));
		System.out.println("--------------------------------------");
	}

	public static void Mem() {
		PipeLineRegister pr = Controller.pipeline[3];
		if(Controller.pipeline[3]==null)return;

		int address = pr.ALUresult;
		if (pr.memRead) {
			pr.WBvalue = Memory.readData(Integer.toBinaryString(address));
		}
		if (pr.memWrite) {
			int data = pr.src1Val;
			Memory.writeData(Integer.toBinaryString(address), data);

		}
		String instToDecode = pr.instruction;
		String src1 = getSrcOne(instToDecode);
		String src2 = getSrcTwo(instToDecode);
		String dest = getDest(instToDecode);
		String src1Val = InstAsString(RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode), 2)]);
		String src2Val = InstAsString(RegisterFile.registers[Integer.parseInt(getSrcTwo(instToDecode), 2)]);
		String shamt = getShamt(instToDecode);
		String ImmVal = SingExtender(getImm(instToDecode));
		String jumpVal = InstAsString(RegisterFile.registers[Integer.parseInt(getJAddress(instToDecode), 2)]);

		System.out.println("Mem stage");

//		System.out.println("Zero flag: "+pr.Zero);
		System.out.println("ALU result/addres: " + pr.ALUresult);
		System.out.println("register value to write to memory: " + InstAsString(pr.toMemoryVal));
//		System.out.println("Read data 1: " + src1Val);
//		System.out.println("Read data 2: " + src2Val);
//		System.out.println("Immediate Value (Sign-Extended): " + ImmVal);
//		System.out.println("Next PC: " + InstAsString(Memory.PC));
//		System.out.println("Src1: " + src1);
//		System.out.println("Src2: " + src2);
//		System.out.println("Dest: " + dest);
//		System.out.println("Shift amount: " + shamt);
//		System.out.println("Jump Value: " + jumpVal);
		System.out.println("memory word read: " + (pr.memRead ? pr.WBvalue : "do not care"));

		System.out.println("Control signals -->");
		System.out.printf("WB control: memToReg-> %d, RegWrite-> %d\n", (pr.memToReg ? 1 : 0), (pr.RegWrite ? 1 : 0));
//		System.out.printf("Memory control: memRead-> %d, memWrite-> %d, branch-> %d, jump-> %d\n", (pr.memRead ? 1 : 0),
//				(pr.memWrite ? 1 : 0), (pr.branch ? 1 : 0), (pr.jump ? 1 : 0));

		System.out.println("------------------------");
	}

	public static void WB() {
		PipeLineRegister pr = Controller.pipeline[4];
		if(Controller.pipeline[4]==null)return;

		if (pr.RegWrite && pr.memToReg) { // lw
			int val = pr.WBvalue;
			RegisterFile.registers[pr.WBaddress] = val;
		} else if (pr.RegWrite && !pr.memToReg) { // R-type ot I-type instruction
			int val = pr.ALUresult;
			RegisterFile.registers[pr.WBaddress] = val;
		} else if (pr.jump) {
			Memory.setPC(pr.jumVal);

		} else if (pr.branch && pr.Zero) {
			Memory.setPC(pr.ImmediateVal + pr.PCval); // to do we can make the addition in a separate component
		}
		System.out.println("WB stage");
		System.out.println("Instruction "+pr.instruction);
		System.out.println("----------------------------------");


	}

	public static void fetch() {
		// Load the instruction from memory to the nextInstruction Variable of

		// controller.
		PipeLineRegister pr = new PipeLineRegister();
		Controller.pipeline[0] = pr;
//		Controller.pipeline[0].intermediateOut = Memory.getInstruction(); // Load Instruction to First Pipeline Stage.
		int inst = Memory.getInstruction();
		if(inst==-1) {
			Controller.pipeline[0]=null;return;
		}
		pr.PCval = Memory.PC;
		String instToDecode = InstAsString(inst);
		pr.instruction = instToDecode;
		switch (getOpcode(instToDecode)) {
		case "0000":// sub
			pr.AlUop = ALU.encodingOF("sub");
			pr.ALUsrc = false;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.regDest = true;
			pr.RegWrite = true;
			pr.shamt = false;

			break;
		case "0001":// add
			pr.AlUop = ALU.encodingOF("add");
			pr.ALUsrc = false;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.regDest = true;
			pr.RegWrite = true;
			pr.shamt = false;

			break;
		case "0010":// add Imm
			pr.AlUop = ALU.encodingOF("add");
			pr.ALUsrc = true;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.regDest = false;
			pr.RegWrite = true;
			pr.shamt = false;

			break;
		case "0011":// mul
			pr.AlUop = ALU.encodingOF("mul");
			pr.ALUsrc = false;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.regDest = true;
			pr.RegWrite = true;
			pr.shamt = false;

			break;
		case "0100":// or
			pr.AlUop = ALU.encodingOF("or");
			pr.ALUsrc = false;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.regDest = true;
			pr.RegWrite = true;
			pr.shamt = false;

			break;
		case "0101":// and Imm
			pr.AlUop = ALU.encodingOF("and");
			pr.ALUsrc = true;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.regDest = false;
			pr.RegWrite = true;
			pr.shamt = false;

			break;
		case "0110":// shift r
			pr.AlUop = ALU.encodingOF("shiftr");
			pr.ALUsrc = false;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.regDest = true;
			pr.RegWrite = true;
			pr.shamt = true;
			break;
		case "0111":// shift l
			pr.AlUop = ALU.encodingOF("shiftl");
			pr.ALUsrc = false;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.regDest = true;
			pr.RegWrite = true;
			pr.shamt = true;
			break;
		case "1000":// lw
			pr.AlUop = ALU.encodingOF("add");
			pr.ALUsrc = true;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = true;
			pr.memToReg = true;
			pr.memWrite = false;
			pr.regDest = false;
			pr.RegWrite = true;
			pr.shamt = false;
			break;
		case "1001":// sw
			pr.AlUop = ALU.encodingOF("add");
			pr.ALUsrc = true;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = true;
			pr.regDest = false;
			pr.RegWrite = false;
			pr.shamt = false;
			break;
		case "1010":// beq
			pr.AlUop = ALU.encodingOF("sub");
			pr.ALUsrc = false;
			pr.branch = true;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.regDest = false;
			pr.RegWrite = false;
			pr.shamt = false;
			break;
		case "1011":// branch less than

			pr.AlUop = ALU.encodingOF("sgt");
			pr.ALUsrc = false;
			pr.branch = true;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.regDest = false;
			pr.RegWrite = false;
			pr.shamt = false;
			break;
		case "1100":// slt Imm
			pr.AlUop = ALU.encodingOF("slt");
			pr.ALUsrc = true;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.regDest = false;
			pr.RegWrite = true;
			pr.shamt = false;
			break;
		case "1101":// jump
			pr.AlUop = ALU.encodingOF("doNotCare");
			pr.ALUsrc = false;
			pr.branch = false;
			pr.jump = true;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.RegWrite = false;
			pr.shamt = false;
			break;
		default:
			System.out.println("Wrong Type Code Please revise your input data");
		}
		System.out.println("Fetch stage");
		System.out.println("Instruction: " + instToDecode);
		System.out.println("PC: "+InstAsString(Memory.PC));
		System.out.println("---------------------------");

	}

	public static String InstAsString(int inst) {
		String instToString = Integer.toBinaryString(inst);
		while (instToString.length() < 32) {
			instToString = "0" + instToString;
		}
		return instToString;
	}

	public static void decode() {
//		int inst = (int) Controller.pipeline[0].intermediateOut;
		if(Controller.pipeline[1]==null)return;
		String instToDecode = Controller.pipeline[1].instruction;
		PipeLineRegister pr = Controller.pipeline[1];
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
		/*
		 * Addi $t0, $0,5 in Decode stage: read data 1: 0000 0000 0000 0000 0000 0000
		 * 0000 0000 read data 2: 0000 0000 0000 0000 0000 0000 0000 0000 sign-extend:
		 * 0000 0000 0000 0000 0000 0000 0000 0101 Next PC: 0000 0000 0000 0000 0000
		 * 0000 0000 0100 rt: 01001 rd: don’t care WB controls: MemToReg: 1, RegWrite: 1
		 * MEM controls: MemRead: 0, MemWrite: 0, Branch: 0 EX controls: RegDest: 0,
		 * ALUOp: 010, ALUSrc: 1
		 */
		String src1 = getSrcOne(instToDecode);
		String src2 = getSrcTwo(instToDecode);
		String dest = getDest(instToDecode);
		String src1Val = InstAsString(RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode), 2)]);
		String src2Val = InstAsString(RegisterFile.registers[Integer.parseInt(getSrcTwo(instToDecode), 2)]);
		String shamt = getShamt(instToDecode);
		String ImmVal = SingExtender(getImm(instToDecode));
		String jumpVal = InstAsString(RegisterFile.registers[Integer.parseInt(getJAddress(instToDecode), 2)]);

		System.out.println("Decode stage");
		System.out.println("Instruction "+pr.instruction);
		System.out.println("Read data 1: " + src1Val);
		System.out.println("Read data 2: " + src2Val);
		System.out.println("Immediate Value (Sign-Extended): " + ImmVal);
		System.out.println("Next PC: " + InstAsString(Memory.PC));
		System.out.println("Src1: " + src1);
		System.out.println("Src2: " + src2);
		System.out.println("Dest: " + dest);
		System.out.println("Shift amount: " + shamt);
		System.out.println("Jump Value: " + jumpVal);

		System.out.println("Control signals -->");
		System.out.printf("WB control: memToReg-> %d, RegWrite-> %d\n", (pr.memToReg ? 1 : 0), (pr.RegWrite ? 1 : 0));
		System.out.printf("Memory control: memRead-> %d, memWrite-> %d, branch-> %d, jump-> %d\n", (pr.memRead ? 1 : 0),
				(pr.memWrite ? 1 : 0), (pr.branch ? 1 : 0), (pr.jump ? 1 : 0));
		System.out.printf("EX control: ALUsrc-> %d, ALUop-> %s, regDest-> %d, shamt->%d\n", (pr.ALUsrc ? 1 : 0),
				pr.AlUop, (pr.regDest ? 1 : 0), (pr.shamt ? 1 : 0));

//		System.out.println("ALUsrc: " + (pr.ALUsrc ? 1 : 0));
//		System.out.println("branch: " + (pr.branch ? 1 : 0));
//		System.out.println("memRead: " + (pr.memRead ? 1 : 0));
//		System.out.println("memToReg: " + (pr.memToReg ? 1 : 0));
//		System.out.println("memWrite: " + (pr.memWrite ? 1 : 0));
//		System.out.println("RegWrite: " + (pr.RegWrite ? 1 : 0));
//		System.out.println("shamt: " + (pr.shamt ? 1 : 0));
//		System.out.println("regDest: " + (pr.regDest ? 1 : 0));
		System.out.println("---------------------------");

	}

	// Handles J Instructions. //Not-Correct
	private static void handleJ(String instToDecode) {
		PipeLineRegister pr = Controller.pipeline[1];
		if (getOpcode(instToDecode).equals("1101")) {// .equals();;
			int jumpVal = RegisterFile.registers[Integer.parseInt(getJAddress(instToDecode), 2)];
//			Memory.setPC(jumpVal);
			pr.jumVal = jumpVal;

		} else {
			System.out.println("This is not a well formatted J Type Instruction");
		}
	}

	// Need to Send more info for ALU to be able to do what it wants.
	private static void handleR(String instToDecode) {

		int src1Val = RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode), 2)];
		int src2Val = RegisterFile.registers[Integer.parseInt(getSrcTwo(instToDecode), 2)];
		int shamt = Integer.parseInt(getShamt(instToDecode), 2);
		PipeLineRegister pr = Controller.pipeline[1];
//		switch (getOpcode(instToDecode)) {
//
//		case "0000":// sub
//			
//		case "0001":// add
//			
//		case "0011":// mul
//			
//		case "0100":// or
//			pr.src1Val = src1Val;
//			pr.src2Val = src2Val;
//			pr.WBaddress = Integer.parseInt(getDest(instToDecode), 2);
//			break;
//		case "0110":// shiftr
//			
//		case "0111":// shiftl
//			pr.src1Val = src1Val;
//			pr.src2Val = shamt;
//			pr.WBaddress = Integer.parseInt(getDest(instToDecode), 2);
//			break;
//		default:
//			System.out.println("This is not a correct R-Format Instruction");
//
//		}
		pr.WBaddress = Integer.parseInt(getDest(instToDecode), 2);
		pr.src1Val = src1Val;

		if (pr.shamt) {
			pr.src2Val = shamt;
		} else {
			pr.src2Val = src2Val;
		}

	}

//sw %t1,%t2,12
	private static void handleI(String instToDecode) {
		PipeLineRegister pr = Controller.pipeline[1];
		pr.src1Val = RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode), 2)];
		pr.ImmediateVal = Integer.parseInt(SingExtender(getImm(instToDecode)), 2);
		pr.toMemoryVal = RegisterFile.registers[Integer.parseInt(getDest(instToDecode), 2)];

//		switch (getOpcode(instToDecode)) {
//		case "0010"://add Imm
//			ALU.ALUEvaluator("0010", RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode), 2)],
//					Integer.parseInt(getImm(instToDecode), 2));
//			break;
//		case "0101"://and Imm
//			ALU.ALUEvaluator("0000", RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode), 2)],
//					RegisterFile.registers[Integer.parseInt(getImm(instToDecode), 2)]);
//			break;
//		case "1000"://lw
//			ALU.ALUEvaluator("0010", RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode), 2)],
//					RegisterFile.registers[Integer.parseInt(getImm(instToDecode), 2)]);
//			break;
//		case "1001"://sw
//			ALU.ALUEvaluator("0010", RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode), 2)],
//					RegisterFile.registers[Integer.parseInt(getImm(instToDecode), 2)]);
//			break;
//		case "1010"://beq
//			ALU.ALUEvaluator("0011", RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode), 2)],
//					RegisterFile.registers[Integer.parseInt(getImm(instToDecode), 2)]);
//			break;
//		case "1011"://blt
//			ALU.ALUEvaluator("0011", RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode), 2)],
//					RegisterFile.registers[Integer.parseInt(getImm(instToDecode), 2)]);
//			break;
//		case "1100"://slt Imm
//			ALU.ALUEvaluator("0111", RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode), 2)],
//					RegisterFile.registers[Integer.parseInt(getSrcOneImm(instToDecode), 2)]);
//			break;
//
//		default:
//			System.out.println("This is not a correct I-Format Instruction");
//
//		}

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
// I-type : 2 bits types 5 bits destination 5bits src1 5 bits Imm
//	public static String getSrcOneImm(String s) {
//		return s.substring(7, 12);
//	}

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

//class EX {
//	int val1, val2;
//	String operation;
//}
//
//class M {
//	int address;
//
//}
//
//class WB {
//	int reg;
//}

class PipeLineRegister {
	boolean regDest, branch, memRead, memToReg, memWrite, ALUsrc, RegWrite, jump, shamt;
	String AlUop;
	String instruction;
	int PCval;
	int toMemoryVal;
	int src1Val, src2Val, ImmediateVal;
	int WBaddress, WBvalue;
	int jumVal;
	int ALUresult;
	boolean Zero;

}
//[pr1, pr2, pr3, pr4,pr5]
//[pr6, pr1, pr2, pr3, pr4]

/*
 * R type dest, src1,src2,operation EX : val src1, val src2, operation M: null
 * WB: dest lw dst src imm sw src1 src2 imm
 */
