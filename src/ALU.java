
public class ALU {
	private static boolean Z;

	// ALU control lines | Function
	// -------------------+-------------------
	// 0000 | AND
	// 0001 | OR
	// 0010 | add
	// 0011 | subtract
	// 0100 | multiply
	// 0101 | shift left
	// 0110 | shift right
	// 0111 | slt

	public static int ALUEvaluator(String Op, int Operand1, int Operand2) {
		int Output = 0;
		String OperationName = "";
		switch (Op) {
		case "0000": // I-AND
			Output = Operand1 & Operand2;
			OperationName = "AND";
			break;
		case "0001": // R-Type OR
			Output = Operand1 | Operand2;
			OperationName = "OR";
			break;
		case "0010": // R-Add
			Output = Operand1 + Operand2;
			OperationName = "add";
			break;
		case "0011": // R-Sub/Beq/Blt
			Output = Operand1 - Operand2;
			OperationName = "sub";
			break;
		case "0100": // R-MultiplyS
			Output = Operand1 * Operand2;
			OperationName = "multiply";
			break;
		case "0101":// R-shift left
			Output = Operand1 << (Operand2 % 32);
			OperationName = "shift left";
			break;
		case "0110":// R-Shift Right
			Output = Operand1 >> (Operand2 % 32);
			OperationName = "shift right";
			break;
		case "0111": // I-Slt
			Output = (Operand1 < Operand2) ? 1 : 0;
			OperationName = "slt";
			break;
		case "1000": // I-Sgt
			Output = (Operand1 >= Operand2) ? 1 : 0;
			OperationName = "sgt";
			break;

		}
		Z = Output == 0;
//		System.out.printf("Operation Name: %s\r\n" + "1st Operand : %d\r\n" + "2nd Operand : %d\r\n" + "Output : %d\r\n"
//				+ "Z-Flag Value : %d\r\n" + "\r\n", OperationName, Operand1, Operand2, Output, Z ? 1 : 0);
		return Output;
	}

	public static boolean isZero() {
		return Z;
	}

	public static String encodingOF(String op) {
		switch (op.toLowerCase()) {
		case "and":
			return "0000";
		case "or":
			return "0001";
		case "add":
			return "0010";
		case "sub":
			return "0011";
		case "mul":
			return "0100";
		case "shiftl":
			return "0101";
		case "shiftr":
			return "0110";
		case "slt":
			return "0111";
		case "sgt":
			return "1000";
		default:
			return null;

		}

	}

}
