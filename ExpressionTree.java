public class ExpressionTree {

	// This class contains a template for describing the nodes in an expression
	// tree. The basic operations that are provided for expression trees are to
	// evaluate the tree and to format the tree in RPN notation.
	//
	// Read and understand the basic structure of the template.
	// Answer the following questions (as comments in your code - yes right here).

	// 1. Why do we not declare any data members in the root class Node?
	// Because different Node children will need different data members/types; some need ints, others strings, etc.
	// 2. Why do we introduce derived abstract classes for unary/binary operator nodes?
	// Because you never actually have a "unary" or "binary" operator - you have operators that are concrete children of them (add / subtract)
	// 3. Why do we introduce an abstract method for the operator symbol?
	// Because there is no single "operator" - similar to last question, you may have multiple operators that act differently.
	// 4. Why do we not need to override format in each of the individual operator classes?
	// Because the notation for each operator is the same - operand operand operator; we declare it in the parent Unary/Binary Operator class
	// 5. The format method is recursive; why is there no explicit test for the base case?
	// Because eventually the recursion ends when you hit the "child" class - Number.format() returns a String, not a recursive function call.
	// 6. Why are there constructors in the abstract classes for unary/binary op nodes?
	// so we can use super() in their  children and have the same Node members available to us. Also no need for duplicate constructors to be declared in each.

	// Complete this code template to handle the unary operators negate and increment (++)
	// as well as the standard binary operators add (+), subtract (-), multiply (*),
	// divide (/) and mod (%). You will also need to complete this template for the
	// leaf nodes: literals (numbers) and variables.
	//
	// There is a main method which can be used to test your program which takes an
	// expression from the command line in RPN notation and builds an expression tree.
	// Take care when invoking the test program: operands and operators must be separated
	// by spaces and some operators (e.g., '*') must be placed in single quotes to
	// prevent the shell from interpreting them as a wild card for file name matching:
	//
	// java ExpressionTree 2 5 '*'

	private static LinearProbingHashMap<String, Integer> table;

	public static abstract class Node {

		// An abstract class for the nodes in an expression tree.

		public abstract int evaluate(); // Evaluate the sub-tree rooted at this node

		public abstract String format(); // Format (RPN) the sub-tree rooted at this node
	}

	public static class Number extends Node {

		// A (sub)class to hold literals (integers) in an expression tree.
		int value;

		public Number(int value) {
			this.value = value;
		}

		@Override
		public int evaluate() {
			return value;
		}

		@Override
		public String format() {
			return String.valueOf(value);
		}
	}

	public static class Variable extends Node {

		// A (sub)class to hold variables in an expression tree.
		// Variable names are strings following the Java syntax
		// for variable names.

		// For now, assume that the value of all variables is zero.
		// We will add a symbol table later to keep track of the
		// current value of each variable.

		String name;

		public Variable(String name) {
			this.name = name;
		}

		@Override
		public int evaluate() {
			if (!table.contains(name)) {
				try {
					throw new Exception("Invalid variable - does not exist");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return table.find(name);
		}

		@Override
		public String format() {
			return name;
		}
	}

	public abstract static class UnaryOperator extends Node { // like x++, one child

		// An abstract subclass for unary operator nodes.

		private Node operand; // child

		public UnaryOperator(Node operand) {
			this.operand = operand;
		}

		public Node operand() {
			return this.operand;
		}

		@Override
		public String format() {
			return operand.format() + " " + this.op();
		}

		public abstract String op();
		// The symbol (string) used for this operator
	}

	public static class Negate extends UnaryOperator {

		// A subclass for expression tree nodes for a particular
		// unary operator: negate.

		public Negate(Node operand) {
			super(operand);
		}

		@Override
		public int evaluate() {
			return -operand().evaluate();
		}

		@Override
		public String op() {
			return "~"; // For now, use '~' to disambiguate it from the binary subtract op.
		}
	}

	public static class Increment extends UnaryOperator {

		// Use Negate as a template to complete the code for the increment (++) operator.

		public Increment(Node operand) {
			super(operand);
		}

		@Override
		public int evaluate() {
			return operand().evaluate() + 1;
		}

		@Override
		public String op() {
			return "++";
		}
	}

	public abstract static class BinaryOperator extends Node {

		// An abstract subclass for binary operator nodes.

		private Node leftOperand;
		private Node rightOperand;

		public BinaryOperator(Node first, Node second) {
			this.leftOperand = first;
			this.rightOperand = second;
		}

		public Node leftOperand() {
			return this.leftOperand;
		}

		public Node rightOperand() {
			return this.rightOperand;
		}

		public String format() {
			return leftOperand.format() + " " + rightOperand.format() + " " + this.op();
		}

		public abstract String op();
		// The symbol (string) used for this operator
	}

	// And then create the subclasses for the specific binary operators.
	// Note that the main program has some expections about the constructors.

	public static class Add extends BinaryOperator {

		public Add(Node left, Node right) {
			super(left, right);
		}

		@Override
		public String op() {
			return "+";
		}

		@Override
		public int evaluate() {
			return (this.leftOperand().evaluate() + this.rightOperand().evaluate());
		}

	}

	public static class Subtract extends BinaryOperator {

		public Subtract(Node left, Node right) {
			super(left, right);
		}

		@Override
		public String op() {
			return "-";
		}

		@Override
		public int evaluate() {
			return (this.leftOperand().evaluate() - this.rightOperand().evaluate());
		}

	}

	public static class Multiply extends BinaryOperator {

		public Multiply(Node left, Node right) {
			super(left, right);
		}

		@Override
		public String op() {
			return "*";
		}

		@Override
		public int evaluate() {
			return (this.leftOperand().evaluate() * this.rightOperand().evaluate());
		}

	}

	public static class Divide extends BinaryOperator {

		public Divide(Node left, Node right) {
			super(left, right);
		}

		@Override
		public String op() {
			return "/";
		}

		@Override
		public int evaluate() {
			return (this.leftOperand().evaluate() / this.rightOperand().evaluate());
		}

	}

	public static class Mod extends BinaryOperator {

		public Mod(Node left, Node right) {
			super(left, right);
		}

		@Override
		public String op() {
			return "%";
		}

		@Override
		public int evaluate() {
			return (this.leftOperand().evaluate() % this.rightOperand().evaluate());
		}

	}

	// TODO: somehow the result should have the variable and not the number being replaced.
	public static class Equal extends BinaryOperator {

		public Equal(Variable left, Number right) {
			super(left, right);
		}

		@Override
		public String op() {
			return "="; // so it doesn't double-print =
		}

		@Override
		public int evaluate() {
			table.add(this.leftOperand().format(), this.rightOperand().evaluate());
			return table.find(this.leftOperand().format());
		}

	}

	// The main program to test your class hierachy implementation.

	private static Node[] stack;
	private static int top = 0;

	private static Node pop() {
		return stack[--top];
	}

	private static void push(Node node) {
		stack[top++] = node;
	}

	public static void main(String[] args) {
		//String[] args2 = { "5", "x", "3", "=", "++"};
		//args = args2;
		table = new LinearProbingHashMap<String, Integer>();
		stack = new Node[args.length];
		Node left, right;

		for (String arg : args) {
			switch (arg) {
			case "~":
				push(new Negate(pop()));
				break;

			case "++":
				push(new Increment(pop()));
				break;

			case "+":
				right = pop();
				left = pop();
				push(new Add(left, right));
				break;

			case "-":
				right = pop();
				left = pop();
				push(new Subtract(left, right));
				break;

			case "*":
				right = pop();
				left = pop();
				push(new Multiply(left, right));
				break;

			case "/":
				right = pop();
				left = pop();
				push(new Divide(left, right));
				break;

			case "%":
				right = pop();
				left = pop();
				push(new Mod(left, right));
				break;

			case "=":
				// the way this is done probably isn't right - 
				// variables are set to numbers when you evaluate, so 
				// no need for ++ or -- to find the variable
				right = pop();
				left = pop();
				if (isInteger(left.toString())) {
					try {
						throw new Exception("Invalid variable");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				push(new Equal((Variable) left, (Number) right));
				break;

			default:
				try {
					int value = Integer.parseInt(arg);
					push(new Number(value));
				} catch (NumberFormatException e) {
					push(new Variable(arg));
				}
			}
		}

		Node expression = pop();
		System.out.println(expression.format() + " = " + expression.evaluate());
	}

	// really no better way for this?
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}
}
