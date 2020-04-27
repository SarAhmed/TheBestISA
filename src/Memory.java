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

	public static boolean hasMoreInstruction() {
		return PC<nextInstruction;
	}

	// TO DO ask if it's possible to get SW in an address where there are
	// instruction

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

	public static int readCode() {
		return readFile("code.txt");
	}

	public Memory() {
		cache = new int[cacheSize];
		tag = new int[cacheSize];
		valid = new boolean[cacheSize];
		nonExecute = new boolean[cacheSize];

		mem = new int[memSize];
	}

	public static void setPC(int val) {
		PC = val % memSize;

	}

	

	public static void loadInstruction(int instruction) {
		mem[(nextInstruction++) ] = instruction; 
	}

	public static int getInstruction() {
		if (PC == nextInstruction)
			return -1;
		while (nonExecute[PC])
			PC = (PC + 1) % memSize;

		int inst = mem[PC];
		PC = (PC + 1) % memSize;
		return inst;
	}

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

	public static int readData(String address) {
		int ad = Integer.parseInt(address, 2);
		ad %= memSize;// size of data mem

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
