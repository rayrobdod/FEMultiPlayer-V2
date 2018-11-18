package net.fe.unit;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.ArrayList;

public final class ItemTest {
	
	@Test
	public void hasAbsoluteOrdering() {
		final List<Item> items = new ArrayList<>(64);
		Item.getAllItems().forEach(items::add);
		
		for (int i = 0; i < items.size(); i++)
		for (int j = i + 1; j < items.size(); j++) {
			Item a = items.get(i);
			Item b = items.get(j);
			
			int atob = a.compareTo(b);
			int btoa = b.compareTo(a);
			
			assertNotEquals("compared equal: " + a + " " + b, 0, atob);
			assertNotEquals("compared equal: " + a + " " + b, 0, btoa);
			assertFalse("inconsistent ordering: " + a + " " + b, atob < 0 && btoa < 0);
			assertFalse("inconsistent ordering: " + a + " " + b, atob > 0 && btoa > 0);
		}
	}
}