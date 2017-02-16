import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LinearProbingHashMap<Key, Value> {

	private Key[] keys;
	private Value[] values;

	private int mod = 0;
	private int size;

	public LinearProbingHashMap(int size) {
		this.mod = size;
		this.size = 0;
		this.keys = (Key[]) new Object[mod];
		this.values = (Value[]) new Object[mod];
	}

	public LinearProbingHashMap() {
		this(16);
	}

	public int hash(Key key) {
		return (key.hashCode() & 0x7FFF_FFFF) % this.mod;
	}

	public int size() {
		return size;
	}

	public boolean contains(Key key) {
		int index = hash(key);
		while (keys[index] != null && !keys[index].equals(key) && keys[index] != null) {
			index++;
			index %= this.mod;
		}
		return keys[index] != null;
	}

	public void add(Key key, Value value) {
		if (key == null) {
			System.out.println("Key cannot be null");
			return;
		}

		if (value == null) {
			// what should i do?
		}

		if (size >= mod / 2) { // >= instead of =... took a while...
			resize(2 * mod);
		}

		int index = hash(key);
		while (keys[index] != null) {
			if (keys[index].equals(key)) { // replace existing value of key with new value
				values[index] = value;
				return;
			}
			index = (index + 1) % this.mod;
		}
		keys[index] = key;
		values[index] = value;
		size++;
	}

	private void resize(int resize) {
		LinearProbingHashMap<Key, Value> aux = new LinearProbingHashMap<Key, Value>(resize);
		for (int i = 0; i < mod; i++) { // copy over; mod because `size` is amount of items in.
			if (keys[i] != null) {
				aux.add(keys[i], values[i]);
			}
		}
		keys = aux.keys;
		values = aux.values;
		mod = aux.mod;
	}

	public Value find(Key key) {
		int index = hash(key);
		while (keys[index] != null && !keys[index].equals(key)) {
			index++;
			index %= this.mod;
		}
		return values[index];
	}

	public void remove(Key key) {
		if (key == null) {
			System.out.println("Key cannot be null");
			return;
		}

		if (!contains(key)) { // to remove() something it should probably be present...
			return;
		}

		// find position i of key
		int index = hash(key);
		while (!key.equals(keys[index])) { // iterate through keys until index is found
			index = (index + 1) % mod;
		}

		keys[index] = null;
		values[index] = null;

		index = (index + 1) % this.mod; // update index
		while (keys[index] != null) {
			Key tempKey = keys[index];
			Value tempVal = values[index];
			keys[index] = null;
			values[index] = null;
			size--;
			add(tempKey, tempVal);
			index = (index + 1) % mod;
		}

		size--;
	}

	public void print() {
		for (int i = 0; i < keys.length; i++) {
			System.out.println(i + ": " + keys[i] + " : " + values[i]);
		}
	}

	private static String getArgument(String line, int index) {
		String[] words = line.split("\\s");
		return words.length > index ? words[index] : "";
	}

	private static String getCommand(String line) {
		return getArgument(line, 0);
	}

	private static String getLine(BufferedReader input) {
		System.out.print("Command: ");
		try {
			return input.readLine().trim();
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		LinearProbingHashMap<String, String> table = new LinearProbingHashMap<>();
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		// Allow the user to enter commands on standard input:
		//
		//   contains <key>    prints true if a value is in the tree; false if not
		//   add <key>         adds an item to the tree
		//   remove <key>      removes an item from the tree (if present)
		//   clear             removes all items from the tree
		//   print             prints the contents of the hash table
		//   exit              quit the program

		String line = getLine(input);
		while (line != null) {
			String command = getCommand(line);
			String arg = getArgument(line, 1);

			switch (command) {
			case "hash":
				System.out.println(arg + " = " + arg.hashCode());
				break;

			case "index":
				System.out.println(arg + " = " + table.hash(arg));
				break;

			case "contains":
				System.out.println(table.contains(arg));
				break;

			case "add":
			case "insert":
				if (getArgument(line, 2) != null) {
					table.add(arg, getArgument(line, 2));
				} else {
					table.add(arg, arg);
				}
				break;

			case "delete":
			case "remove":
				table.remove(arg);
				break;

			case "find":
				System.out.println(arg + " = " + table.find(arg));
				break;

			case "print":
				table.print();
				break;

			case "clear":
				table = new LinearProbingHashMap<>();
				break;

			case "end":
			case "exit":
			case "quit":
				return;

			default:
				System.out.println("Invalid command: " + command);
				break;
			}

			line = getLine(input);
		}
	}
}
