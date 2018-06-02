package boot;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class Run {
	private static PrintWriter writer;

	public static void main(String[] args) throws Exception {
		int n = Integer.parseInt(args[0]);
		for (int i = 0; i < n; i++) {
			writer = new PrintWriter(new FileOutputStream((i + 1) + ".txt", true));
			StringBuilder builder = new StringBuilder("");
			for (int j = 0; j <= i; j++) {
				char ch = (char) ('a' + j + 1);
				builder.append(ch);
			}
			compute(builder.toString(), "");
			writer.close();
		}

	}

	public static void compute(String str, String prev) throws IOException {
		if (str.length() == 0) {
			writer.println(prev);
			return;
		}
		int len = str.length();
		for (int i = 0; i < len; i++) {
			char ch = str.charAt(i);
			String newStr = str.replaceAll(ch + "", "");
			String newPrev = prev + ch;
			compute(newStr, newPrev);
		}
	}
}
