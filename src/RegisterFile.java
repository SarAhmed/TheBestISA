public class RegisterFile {
	//Class representing the 32 registers in the register file.
	static int registers[];
	public RegisterFile() {
		registers = new int[32];
	}

}
/*The registers are named from 0 to 31 in binary respectively.
 * We no longer have a zero register like MIPS.
 * All 32 registers are general purpose and allow data and addresses to be stored in them.
 * No register has a special purpose except for the PC which is kept in the Memory for functionality issues.
 */
