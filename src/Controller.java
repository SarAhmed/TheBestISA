import java.util.Arrays;
import java.util.Scanner;


public class Controller {
	static PipeLineRegister pipeline[];
	static int cycle;
	public Controller() {
		pipeline = new PipeLineRegister[5];
	}
	//Main method to simulate the program & control excution.
	//An Empty Cycle is printed out After execution is done.
	//We call this the Cleaning Cycle.It checks that the pipeline is empty & there are not other instructions.
	//The Cleaning Cycle has no effect at all in fact it is a mere check that everything went as expected.
	
	public static void main(String[] args) {
		init();
		Memory.readCode();
		while (Memory.hasMoreInstruction() || !isNull()) { 
			nextCycle();
			System.out.println("After clock cycle: " + cycle);
			System.out.println();
			for (int i = 4; i >0; i--) {
				pipeline[i] = pipeline[i - 1];
			}			
			fetch();
			decode();
			execute();
			Mem();
			WB();
		}
	}
//Check if there anything in the pipeline before halting exction
public static boolean isNull() {
	for(int i =0;i<pipeline.length;i++) {
		if(pipeline[i]!=null)
			return false;
	}
	return true;
}
//A function to initialize the components.
	public static void init() {
		new Memory();
		new RegisterFile();
		new Controller();

	}
	//Increment the variable cycle to simulate next cycle.
	public static void nextCycle() {
		cycle++;
	}
	/*Fetch method that fetches instruction and set control signals.
	 *Load the instruction from memory to the nextInstruction Variable of nextInstruction. (Checked).
	*/
	public static void fetch() {
		PipeLineRegister pr = new PipeLineRegister();
		Controller.pipeline[0] = pr;
		int inst = Memory.getInstruction();
		if(inst==-1) {
			Controller.pipeline[0]=null;return;
		}
		pr.PCval = Memory.PC;
		String instToDecode = InstAsString(inst);
		pr.instruction = instToDecode;
		switch (getOpcode(instToDecode)) {
		case "0000"://Sub R-Type (Checked)
			pr.AlUop = ALU.encodingOF("sub");
			pr.ALUsrc = false;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.RegWrite = true;
			pr.shamt = false;

			break;
		case "0001":// Add R-Type (Checked)
			pr.AlUop = ALU.encodingOF("add");
			pr.ALUsrc = false;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.RegWrite = true;
			pr.shamt = false;

			break;
		case "0010":// Add-Imm (Checked)
			pr.AlUop = ALU.encodingOF("add");
			pr.ALUsrc = true;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.RegWrite = true;
			pr.shamt = false;

			break;
		case "0011":// Mul R-Type(Checked)
			pr.AlUop = ALU.encodingOF("mul");
			pr.ALUsrc = false;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.RegWrite = true;
			pr.shamt = false;

			break;
		case "0100":// OR R-Type(Checked)
			pr.AlUop = ALU.encodingOF("or");
			pr.ALUsrc = false;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.RegWrite = true;
			pr.shamt = false;

			break;
		case "0101":// AND-Imm(Checked)
			pr.AlUop = ALU.encodingOF("and");
			pr.ALUsrc = true;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.RegWrite = true;
			pr.shamt = false;

			break;
		case "0110":// Shift-R(Checked)
			pr.AlUop = ALU.encodingOF("shiftr");
			pr.ALUsrc = false;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.RegWrite = true;
			pr.shamt = true;
			break;
		case "0111":// Shift-L(Checked)
			pr.AlUop = ALU.encodingOF("shiftl");
			pr.ALUsrc = false;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.RegWrite = true;
			pr.shamt = true;
			break;
		case "1000":// LW (Checked).
			pr.AlUop = ALU.encodingOF("add");
			pr.ALUsrc = true;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = true;
			pr.memToReg = true;
			pr.memWrite = false;
			pr.RegWrite = true;
			pr.shamt = false;
			break;
		case "1001":// SW (Checked).
			pr.AlUop = ALU.encodingOF("add");
			pr.ALUsrc = true;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = true;
			pr.RegWrite = false;
			pr.shamt = false;
			break;
		case "1010":// BEQ(Checked)
			pr.AlUop = ALU.encodingOF("sub");
			pr.ALUsrc = false;
			pr.branch = true;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.RegWrite = false;
			pr.shamt = false;
			break;
		case "1011":// BLT(Checked)

			pr.AlUop = ALU.encodingOF("sgt");
			pr.ALUsrc = false;
			pr.branch = true;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.RegWrite = false;
			pr.shamt = false;
			break;
		case "1100":// SLT-Imm(Checked).
			pr.AlUop = ALU.encodingOF("slt");
			pr.ALUsrc = true;
			pr.branch = false;
			pr.jump = false;
			pr.memRead = false;
			pr.memToReg = false;
			pr.memWrite = false;
			pr.RegWrite = true;
			pr.shamt = false;
			break;
		case "1101":// Jump
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
		System.out.println("next PC: "+InstAsString(pr.PCval));
		System.out.println("---------------------------");
	}
	//Method Handling the decode Stage.
	public static void decode() {
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
		String src1 = getSrcOne(instToDecode);
		String src2 = getSrcTwo(instToDecode);
		String dest = getDest(instToDecode);
		String src1Val = InstAsString(RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode), 2)]);
		String src2Val = InstAsString(RegisterFile.registers[Integer.parseInt(getSrcTwo(instToDecode), 2)]);
		String shamt = getShamt(instToDecode);
		String ImmVal = SingExtender(getImm(instToDecode));
		String jumpVal = InstAsString(RegisterFile.registers[Integer.parseInt(getJAddress(instToDecode), 2)]);
		System.out.println("Decode stage");
		System.out.println("Instruction: "+pr.instruction);
		System.out.println("Read data 1: " + src1Val);
		System.out.println("Read data 2: " + src2Val);
		System.out.println("Immediate Value (Sign-Extended): " + ImmVal);
		System.out.println("Next PC: " + InstAsString(pr.PCval));
		System.out.println("Src1: " + src1);
		System.out.println("Src2: " + src2);
		System.out.println("Dest: " + dest);
		System.out.println("Shift amount: " + shamt);
		System.out.println("Jump Value: " + jumpVal);
		System.out.println("Control signals -->");
		System.out.printf("WB control: memToReg-> %d, RegWrite-> %d\n", (pr.memToReg ? 1 : 0), (pr.RegWrite ? 1 : 0));
		System.out.printf("Memory control: memRead-> %d, memWrite-> %d, branch-> %d, jump-> %d\n", (pr.memRead ? 1 : 0),
				(pr.memWrite ? 1 : 0), (pr.branch ? 1 : 0), (pr.jump ? 1 : 0));
		System.out.printf("EX control: ALUsrc-> %d, ALUop-> %s, shamt->%d\n", (pr.ALUsrc ? 1 : 0),
				pr.AlUop,(pr.shamt ? 1 : 0));
		System.out.println("---------------------------");

	}
	// Handles J Instructions (Checked).
	private static void handleJ(String instToDecode) {
		PipeLineRegister pr = Controller.pipeline[1];
		if (getOpcode(instToDecode).equals("1101")) {
			int jumpVal = RegisterFile.registers[Integer.parseInt(getJAddress(instToDecode), 2)];
			pr.jumVal = jumpVal;

		} else {
			System.out.println("This is not a well formatted J Type Instruction");
		}
	}
	//Handles the decoding of R-Type Instructions (Checked).
	private static void handleR(String instToDecode) {
		int src1Val = RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode), 2)];
		int src2Val = RegisterFile.registers[Integer.parseInt(getSrcTwo(instToDecode), 2)];
		int shamt = Integer.parseInt(getShamt(instToDecode), 2);
		PipeLineRegister pr = Controller.pipeline[1];
		pr.WBaddress = Integer.parseInt(getDest(instToDecode), 2);
		pr.src1Val = src1Val;

		if (pr.shamt) {
			pr.src2Val = shamt;
		} else {
			pr.src2Val = src2Val;
		}

	}

	//Handles the decode of I-Type Instructions(Checked and added WB address).
	private static void handleI(String instToDecode) {
		PipeLineRegister pr = Controller.pipeline[1];
		pr.src1Val = RegisterFile.registers[Integer.parseInt(getSrcOne(instToDecode), 2)];
		pr.WBaddress = Integer.parseInt(getDest(instToDecode), 2);
		pr.ImmediateVal = Integer.parseUnsignedInt(SingExtender(getImm(instToDecode)), 2);
		pr.toMemoryVal = RegisterFile.registers[Integer.parseInt(getDest(instToDecode), 2)];
	}

	//Method Handling the Excution (Checked).
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
		//Added branch address display here since it is in the required document.
		String branchAdress = InstAsString(pr.ImmediateVal + pr.PCval);
		System.out.println("execute stage");
		System.out.println("Instruction: "+pr.instruction);

		System.out.println("Zero flag: " + (pr.Zero? 1 : 0));
		System.out.println("Branch Address: "+branchAdress);
		System.out.println("ALU result/addres: " + InstAsString(pr.ALUresult));
		System.out.println("register value to write to memory: " + InstAsString(pr.toMemoryVal));
		System.out.println("Read data 1: " + src1Val);
		System.out.println("Read data 2: " + src2Val);
		System.out.println("Immediate Value (Sign-Extended): " + ImmVal);
		System.out.println("Next PC: " + InstAsString(pr.PCval));
		System.out.println("Src1: " + src1);
		System.out.println("Src2: " + src2);
		System.out.println("Dest: " + dest);
		System.out.println("Shift amount: " + shamt);
		System.out.println("Jump Value: " + jumpVal);

		System.out.println("Control signals -->");
		System.out.printf("WB control: memToReg-> %d, RegWrite-> %d\n", (pr.memToReg ? 1 : 0), (pr.RegWrite ? 1 : 0));
		System.out.printf("Memory control: memRead-> %d, memWrite-> %d, branch-> %d, jump-> %d\n", (pr.memRead ? 1 : 0),
				(pr.memWrite ? 1 : 0), (pr.branch ? 1 : 0), (pr.jump ? 1 : 0));
		System.out.println("--------------------------------------");
	}
	//Method handling reading and writing to memory (Checked).
	public static void Mem() {
		PipeLineRegister pr = Controller.pipeline[3];
		if(Controller.pipeline[3]==null)return;

		int address = pr.ALUresult;
		if (pr.memRead) {
			pr.WBvalue = Memory.readData(Integer.toBinaryString(address));
		}
		if (pr.memWrite) {
			int data = pr.toMemoryVal;
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
		System.out.println("Instruction: "+pr.instruction);
		System.out.println("ALU result/addres: " + InstAsString(pr.ALUresult));
		System.out.println("Register value to write to memory: " + (pr.memWrite ? InstAsString(pr.toMemoryVal) : "do not care"));
		System.out.println("Memory word read: " + (pr.memRead ? InstAsString(pr.WBvalue) : "do not care"));
		System.out.println("Control signals -->");
		System.out.printf("WB control: memToReg-> %d, RegWrite-> %d\n", (pr.memToReg ? 1 : 0), (pr.RegWrite ? 1 : 0));

		System.out.println("------------------------");
	}
	//Method handling the WriteBack (Checked).
	public static void WB() {
		PipeLineRegister pr = Controller.pipeline[4];
		if(Controller.pipeline[4]==null)return;

		if (pr.RegWrite && pr.memToReg) { // lw
			int val = pr.WBvalue;
			RegisterFile.registers[pr.WBaddress] = val;
		} else if (pr.RegWrite && !pr.memToReg) { // R-type or I-type instruction
			int val = pr.ALUresult;
			RegisterFile.registers[pr.WBaddress] = val;
		} else if (pr.jump) {
			Memory.setPC(pr.jumVal);

		} else if (pr.branch && pr.Zero) {
			Memory.setPC(pr.ImmediateVal + pr.PCval);//Immitating the Addition Module.
		}
		System.out.println("WB stage");
		System.out.println("Instruction "+pr.instruction);
		System.out.println("----------------------------------");


	}

	
	//Extend with "0" any int to 32 bits.
	public static String InstAsString(int inst) {
		String instToString = Integer.toBinaryString(inst);
		while (instToString.length() < 32) {
			instToString = "0" + instToString;
		}
		return instToString;
	}



	//Sign Extender Module.
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

	//Group of Methods for fast String Extraction of parts of instructions.

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
//Class Representing the Pipeline Register
class PipeLineRegister {
	boolean  branch, memRead, memToReg, memWrite, ALUsrc, RegWrite, jump, shamt;
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
