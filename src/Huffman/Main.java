package Huffman;

import java.io.IOException;

public class Main {
	public static void main(String args[]) throws IOException  {
		StanderHuffman huff=new StanderHuffman();
		huff.Commpress();
		huff.DeCompress();
		
	}
}
