package mod.gottsch.forge.treasure2.sandbox;

import java.util.ArrayList;
import java.util.List;

public class ListTest {
	public static void main(String[] args) {
		List<String> a = new ArrayList<>();
		List<String> b = new ArrayList<>();
		
		b.add("mark");
		b.addAll(a);
		System.out.print(b);
	}
}
