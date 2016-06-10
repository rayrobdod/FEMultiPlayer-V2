package net.fe.modifier;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderResources;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Class;
import net.fe.unit.HealingItem;
import net.fe.unit.Item;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;
import net.fe.unit.Statistics;

public final class MadeInChinaTest {
	
	/** A range whose range does not depend on unit statistics */
	private final static class Static1Range implements Function<Statistics, List<Integer>> {
		private final int value;
		public Static1Range(int value) { this.value = value; }
		@Override public List<Integer> apply(Statistics s) { return java.util.Arrays.asList(value); }
		@Override public boolean equals(Object o) { return o instanceof Static1Range && ((Static1Range) o).value == this.value;}
	}
	
	@Test
	public void test_modifyTeamResources() {
		MadeInChina dut = new MadeInChina();
		TeamBuilderResources exp = new TeamBuilderResources(2000, 1000);
		TeamBuilderResources res = dut.modifyTeamResources(
				new TeamBuilderResources(1000, 1000)
		);
		
		assertEquals(exp, res);
	}
	
	@Test
	public void test_modifyUnits() {
		MadeInChina dut = new MadeInChina();
		Statistics vals = new Statistics(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
		
		Unit input = new Unit("blah", Class.createClass("Lyn"), '-', vals, vals);
		Weapon inputWeapon = new Weapon("baton", 15, 0, 0,
			Weapon.Type.AXE, 0, 0, 0, new Static1Range(1),
			new Statistics(), new java.util.ArrayList<>(), null
		);
		input.addToInventory(inputWeapon);
		
		
		Unit expected = new Unit("blah", Class.createClass("Lyn"), '-', vals, vals);
		Weapon expectedWeapon = new Weapon("baton", 2, 0, 0,
			Weapon.Type.AXE, 0, 0, 0, new Static1Range(1),
			new Statistics(), new java.util.ArrayList<>(), null
		);
		expected.addToInventory(expectedWeapon);
		
		Unit result = dut.modifyUnits( Stream.of(input) ).findAny().get();
		
		assertEquals(expected.bases, result.bases);
		assertEquals(expected.getInventory(), result.getInventory());
	}
	
	@Test
	public void test_modifyShop_weapon() {
		MadeInChina dut = new MadeInChina();
		
		Item input = new Weapon("baton", 15, 0, 0,
			Weapon.Type.AXE, 0, 0, 0, new Static1Range(1),
			new Statistics(), new java.util.ArrayList<>(), null
		);
		Item expected = new Weapon("baton", 2, 0, 0,
			Weapon.Type.AXE, 0, 0, 0, new Static1Range(1),
			new Statistics(), new java.util.ArrayList<>(), null
		);
		
		Item result = dut.modifyShop( Stream.of(input) ).findAny().get();
		
		assertEquals(expected, result);
	}
	
	@Test
	public void test_modifyShop_nonweapon() {
		MadeInChina dut = new MadeInChina();
		
		Item expected = new HealingItem("Liquid Gold", 15, 2, 2000);
		Item result = dut.modifyShop( Stream.of(expected) ).findAny().get();
		
		assertEquals(expected, result);
	}
	
}
