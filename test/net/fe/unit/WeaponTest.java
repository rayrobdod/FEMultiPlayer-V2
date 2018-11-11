package net.fe.unit;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.fe.unit.Weapon.Type;

public final class WeaponTest {
	
	@Test
	public void test_equals() {
		Weapon left = new Weapon("asdf", 20, 0, 1, Weapon.Type.SWORD, 2,3,4, new Static1Range(1), new Statistics(), new ArrayList<>(), true);
		Weapon right = new Weapon("asdf", 20, 0, 1, Weapon.Type.SWORD, 2,3,4, new Static1Range(1), new Statistics(), new ArrayList<>(), true);
		assertEquals(left, right);
	}
	
	@Test
	public void test_hashCode() {
		Weapon left = new Weapon("asdf", 20, 0, 1, Weapon.Type.SWORD, 2,3,4, new Static1Range(1), new Statistics(), new ArrayList<>(), true);
		Weapon right = new Weapon("asdf", 20, 0, 1, Weapon.Type.SWORD, 2,3,4, new Static1Range(1), new Statistics(), new ArrayList<>(), true);
		assertEquals(left.hashCode(), right.hashCode());
	}
	
	@Test
	public void test_triMod() {
		List<Type> typs = Arrays.asList(
			Type.SWORD, Type.LANCE, Type.AXE,
			Type.BOW, Type.CROSSBOW,
			Type.LIGHT, Type.ANIMA, Type.DARK,
			Type.STAFF
		);
		int[][] exp = {
			{0, -1, 1, 0, 0, 0, 0, 0, 0},
			{1, 0, -1, 0, 0, 0, 0, 0, 0},
			{-1, 1, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, -1, 1, 0},
			{0, 0, 0, 0, 0, 1, 0, -1, 0},
			{0, 0, 0, 0, 0, -1, 1, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0}
		};
		
		for (int leftIdx = 0; leftIdx < typs.size(); leftIdx++) {
			for (int rightIdx = 0; rightIdx < typs.size(); rightIdx++) {
				Type leftTyp = typs.get(leftIdx);
				Type rightTyp = typs.get(rightIdx);
				
				Weapon left = new Weapon("asdf", 20, 0, 1, leftTyp, 2,3,4, new Static1Range(1), new Statistics(), new ArrayList<>(), true);
				Weapon right = new Weapon("asdf", 20, 0, 1, rightTyp, 2,3,4, new Static1Range(1), new Statistics(), new ArrayList<>(), true);
				assertEquals("" + leftTyp + " " + rightTyp, exp[leftIdx][rightIdx], left.triMod(right));
			}
		}
	}
	
	
	
	
	/** A range whose range does not depend on unit statistics */
	private final static class Static1Range implements Function<Statistics, List<Integer>> {
		private final int value;
		public Static1Range(int value) { this.value = value; }
		@Override public List<Integer> apply(Statistics s) { return java.util.Arrays.asList(value); }
		@Override public String toString() { return "" + value; }
		@Override public boolean equals(Object o) { return o instanceof Static1Range && ((Static1Range) o).value == this.value;}
		@Override public int hashCode() { return value; }
	}
}
