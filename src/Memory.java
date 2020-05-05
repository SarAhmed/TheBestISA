import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Memory {
	static final int cacheSize = 512;// no. in words;
	static final int memSize = 1024;// no in words;
	static int[] cache, mem, tag;
	static boolean[] valid;
	static boolean[] nonExecute;
	static int PC;
	static int nextInstruction;
	//Checks if there is a valid next instruction.
	public static boolean hasMoreInstruction() {
		return PC<nextInstruction;
	}
	// A function that reads the code from text file and add it to main memory.
	public static int readFile(String name) {
		String Data = "";
		int count = 0;
		File file = new File(name);
		try {
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()) {
				Data = scan.nextLine().trim();
				Memory.loadInstruction(Integer.parseUnsignedInt(Data, 2));
				count++;
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return count;
	}
	//A function to read code from text file.
	public static int readCode() {
		return readFile("code.txt");
	}
	//Initialize the different memory Arrays.
	public Memory() {
		cache = new int[cacheSize];
		tag = new int[cacheSize];
		valid = new boolean[cacheSize];
		nonExecute = new boolean[cacheSize];
		mem = new int[memSize];
	}
	// Set PC value to the given value (Checks that PC does not go beyond instructions).
	public static void setPC(int val) {
		PC = val % memSize;
	}
	//Loads instruction to memory one by one (A helper for the readFile method).
	public static void loadInstruction(int instruction) {
		mem[(nextInstruction++) ] = instruction; 
	}
	//Fetches the nextInstruction if it exists or returns -1 if no more instructions.
	//Increment the PC and make sure the next in a valid instruction not data.
	public static int getInstruction() {
		if (PC == nextInstruction)
			return -1;
		while (nonExecute[PC])
			PC = (PC + 1) % memSize;

		int inst = mem[PC];
		PC = (PC + 1) % memSize;
		return inst;
	}
	//Writes data to given address.
	//Adds it to both cache and Memory.
	public static void writeData(String address, int data) {
		int ad = Integer.parseInt(address, 2);
		ad %= memSize;
		int idx = ad % 512;
		int t = ad / 512;
		valid[idx] = true;
		tag[idx] = t;
		cache[idx] = mem[ad] = data;
		nonExecute[ad] = true;

	}
		//Reads data from given address.
		//Fetch it from cache or from memory if it is not in the cache.
	public static int readData(String address) {
		int ad = Integer.parseInt(address, 2);
		ad %= memSize;
		int idx = ad % 512;
		int t = ad / 512;
		if (!valid[idx] || t != tag[idx]) {
			valid[idx] = true;
			tag[idx] = t;
			cache[idx] = mem[ad];
		}
		return cache[idx];
	}

}
