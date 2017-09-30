package Huffman;

public class Tree {
	Node root;

	public Tree() {
		root = null;
	}

	public void insert(String symbol, int numOFOccurrence) {
		Node newnode = new Node();
		newnode.Char = symbol;
		newnode.NumberOfOccurrence = numOFOccurrence;

		if (root == null) {
			root = newnode;
			root.leftnode = null;
			root.rightnode = null;

		} else {
			Node nnode = new Node();
			nnode.Char = symbol + root.Char;
			nnode.NumberOfOccurrence = numOFOccurrence + root.NumberOfOccurrence;

			nnode.rightnode = newnode;
			nnode.leftnode = root;
			root = nnode;
		}
	}
}