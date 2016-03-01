package net.fe.unit;

import static org.junit.Assert.*;

import org.junit.Test;

public class WeaponTest {

	@Test
	public void testWhenOneRangeAxeThenPrimaryTypeIsAxe() {
		Weapon dut = new Weapon("debug");
		dut.distanceType = new java.util.HashMap<Integer, Weapon.Type>();
		dut.distanceType.put(1, Weapon.Type.AXE);
		org.junit.Assert.assertEquals(Weapon.Type.AXE, dut.getPrimaryType());
	}

	@Test
	public void testWhenTwoRangeBowThenPrimaryTypeIsBow() {
		Weapon dut = new Weapon("debug");
		dut.distanceType = new java.util.HashMap<Integer, Weapon.Type>();
		dut.distanceType.put(2, Weapon.Type.BOW);
		org.junit.Assert.assertEquals(Weapon.Type.BOW, dut.getPrimaryType());
	}

	@Test
	public void testWhenOneToTwoRangeAnimaThenPrimaryTypeIsAnima() {
		Weapon dut = new Weapon("debug");
		dut.distanceType = new java.util.HashMap<Integer, Weapon.Type>();
		dut.distanceType.put(1, Weapon.Type.ANIMA);
		dut.distanceType.put(2, Weapon.Type.ANIMA);
		org.junit.Assert.assertEquals(Weapon.Type.ANIMA, dut.getPrimaryType());
	}

	@Test
	public void testWhenMagicSwordThenPrimaryTypeIsSword() {
		Weapon dut = new Weapon("debug");
		dut.distanceType = new java.util.HashMap<Integer, Weapon.Type>();
		dut.distanceType.put(1, Weapon.Type.SWORD);
		dut.distanceType.put(2, Weapon.Type.ANIMA);
		org.junit.Assert.assertEquals(Weapon.Type.SWORD, dut.getPrimaryType());
	}

}
