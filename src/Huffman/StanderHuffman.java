package Huffman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StanderHuffman {
	ArrayList<String> Char = new ArrayList<>();
	ArrayList<String> code = new ArrayList<>();
	ArrayList<Integer> NumberOfOccurrence = new ArrayList<>();

	public void Commpress() {
		ArrayList<Character> tablechar = new ArrayList<>();
		ArrayList<Integer> occurrence = new ArrayList<>();
		String data = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader("ReadFile.txt"));
			String str = "";
			while ((str = in.readLine()) != null)
				data += str;
			in.close();
		} catch (IOException e) {
		}
		// System.out.println(data);
		int count = 0;
		char ch;
		while (count < data.length()) {
			ch = data.charAt(count);
			if (tablechar.contains(ch)) {
				count++;
			} else {
				tablechar.add(ch);
				count++;
			}
		}
		int count1 = 0, count2 = 0;
		while (count2 < tablechar.size()) {
			char c = tablechar.get(count2);
			count1 = 0;
			for (int i = 0; i < data.length(); i++) {
				if (data.charAt(i) == c)
					count1++;
			}
			occurrence.add(count1);
			count2++;
		}
		for (int i = 0; i < tablechar.size(); i++) {
			System.out.println(tablechar.get(i) + "    Occurrence    " + occurrence.get(i));
		}
		// copy 2 array in another 2array
		for (int i = 0; i < tablechar.size(); i++) {
			Char.add(String.valueOf(tablechar.get(i)));
			NumberOfOccurrence.add(occurrence.get(i));
		}
		// sort
		int minindex, index;
		String temp;
		for (int i = 0; i < NumberOfOccurrence.size(); i++) {
			minindex = i;
			for (int j = 0; j < NumberOfOccurrence.size(); j++) {
				if (NumberOfOccurrence.get(j) > NumberOfOccurrence.get(minindex))
					minindex = j;
				if (minindex != i) {
					index = NumberOfOccurrence.get(i);
					NumberOfOccurrence.set(i, NumberOfOccurrence.get(minindex));
					NumberOfOccurrence.set(minindex, index);
					temp = Char.get(i);
					Char.set(i, Char.get(minindex));
					Char.set(minindex, temp);
				}
			}
		}
		System.out.println();
		System.out.println("After Sorting ");
		System.out.println();
		for (int i = 0; i < NumberOfOccurrence.size(); i++) {
			System.out.println(Char.get(i) + " ---> " + NumberOfOccurrence.get(i));
		}
		Tree t = new Tree();
		while (!NumberOfOccurrence.isEmpty()) {
			int num = NumberOfOccurrence.get(0);
			NumberOfOccurrence.remove(0);
			String str = Char.get(0);
			Char.remove(0);
			t.insert(str, num);
		}

		// System.out.println("scs");
		// for (int i = 0; i < NumberOfOccurrence.size(); i++) {
		// System.out.println(Char.get(i) + " ---> " +
		// NumberOfOccurrence.get(i));
		// }

		// binary
		String st = "";
		int Index = 0;
		while (t.root != null) {
			st = "";
			if (Index == 0) {
				NumberOfOccurrence.add(Index, t.root.rightnode.NumberOfOccurrence);
				Char.add(Index, t.root.rightnode.Char);
				code.add("1");
				t.root = t.root.leftnode;
			} else if (t.root.leftnode == null) {
				NumberOfOccurrence.add(Index, t.root.NumberOfOccurrence);
				Char.add(Index, t.root.Char);
				for (int i = 0; i < Index; i++)
					st += 0;
				code.add(Index, st + "0");
				t.root = t.root.leftnode;
			} else {
				NumberOfOccurrence.add(Index, t.root.rightnode.NumberOfOccurrence);
				Char.add(Index, t.root.rightnode.Char);
				for (int i = 0; i < Index; i++)
					st += 0;
				code.add(Index, st + "1");
				t.root = t.root.leftnode;
			}
			Index++;
		}
		for (int i = 0; i < NumberOfOccurrence.size(); i++) {
			System.out.println(Char.get(i) + " ---> " + NumberOfOccurrence.get(i) + " ---> " + code.get(i));
		}
		String com = "", read = "";
		index = 0;
		while (index < data.length()) {
			read = "";
			read += data.charAt(index);
			com += code.get(Char.indexOf(read));
			index++;
		}
		System.out.println("compression code");
		System.out.println(com);
		// System.out.println(read);

		FileOutputStream out = null;
		File file = new File("BinaryFile.txt");
		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		DataOutputStream output = new DataOutputStream(out);
		try {
			output.writeInt(com.length());
			output.writeInt(Char.size());
			for (int i = 0; i < Char.size(); i++) {
				output.writeUTF(Char.get(i));
				output.writeUTF(code.get(i));
			}
			int num = 0;
			ArrayList<Integer> body = new ArrayList<Integer>();
			while (com.length() > 0) {
				if (com.length() >= 32) {
					body.add(Integer.parseInt(com.substring(0, 31), 2));
					com = com.substring(32, com.length());
				} else {
					num = com.length();
					body.add(Integer.parseInt(com, 2));
					break;
				}
			}
			output.writeInt(num);
			output.writeInt(body.size());
			for (int i = 0; i < body.size(); i++) {
				output.writeInt(body.get(i));
				// System.out.println(+body.get(i));
			}
			output.close();
		} catch (IOException e) {
		}
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException ex) {
				Logger.getLogger(StanderHuffman.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void DeCompress() throws IOException {
		String com = "";
		ArrayList<String> Char1 = new ArrayList<>();
		ArrayList<String> code1 = new ArrayList<>();
		try {
			File file = new File("BinaryFile.txt");
			FileInputStream input = new FileInputStream(file);
			DataInputStream in = new DataInputStream(input);

			int sizee = in.readInt();
			// System.out.println(sizee);
			for (int i = 0; i < sizee; i++) {
				Char1.add(in.readUTF());
				code1.add(in.readUTF());
			}
			
			System.out.println("++++++++");

			System.out.println(" ");
			System.out.println(com);
		} catch (IOException e) {
		}
		String getcode = "";
		String res = "";
		int pos = 0;

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("Decompress.txt"));
			out.write(res);
			out.close();
		} catch (IOException e) {
			System.out.print("error");
		}
	}
}
