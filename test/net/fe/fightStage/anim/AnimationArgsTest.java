package net.fe.fightStage.anim;

import java.util.HashMap;

import net.fe.unit.Class;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;
import net.fe.unit.Weapon.Type;

import static org.junit.Assert.*;
import org.junit.Test;

public class AnimationArgsTest {

	@Test
	public void test_WhenUnequippedSworduser_ThenSword() {
		Unit u = newSwordUser();
		
		AnimationArgs dut = new AnimationArgs(u, true, 1);
		org.junit.Assert.assertEquals("sword", dut.wepAnimName);
		org.junit.Assert.assertEquals("normal", dut.classification);
	}
	
	@Test
	public void test_WhenUnequippedSworduserAtRange_ThenSword() {
		Unit u = newSwordUser();
		
		AnimationArgs dut = new AnimationArgs(u, true, 2);
		org.junit.Assert.assertEquals("sword", dut.wepAnimName);
		org.junit.Assert.assertEquals("normal", dut.classification);
	}
	
	@Test
	public void test_WhenOneRangeSwordAtOneRange_ThenSword() {
		Unit u = newSwordUser();
		Weapon w = new Weapon("Iron Sword");
		w.distanceType = new java.util.HashMap<Integer, Weapon.Type>();
		w.distanceType.put(1, Weapon.Type.SWORD);
		u.getInventory().add(w);
		u.equip(0);
		
		AnimationArgs dut = new AnimationArgs(u, true, 1);
		org.junit.Assert.assertEquals("sword", dut.wepAnimName);
		org.junit.Assert.assertEquals("normal", dut.classification);
	}
	
	@Test
	public void test_WhenOneRangeSwordAtTwoRange_ThenSword() {
		Unit u = newSwordUser();
		Weapon w = new Weapon("Iron Sword");
		w.distanceType = new java.util.HashMap<Integer, Weapon.Type>();
		w.distanceType.put(1, Weapon.Type.SWORD);
		u.getInventory().add(w);
		u.equip(0);
		
		AnimationArgs dut = new AnimationArgs(u, true, 2);
		org.junit.Assert.assertEquals("sword", dut.wepAnimName);
		org.junit.Assert.assertEquals("normal", dut.classification);
	}
	
	@Test
	public void test_WhenTwoRangeSwordAtTwoRange_ThenRangedsword() {
		Unit u = newSwordUser();
		Weapon w = new Weapon("Iron Sword");
		w.distanceType = new java.util.HashMap<Integer, Weapon.Type>();
		w.distanceType.put(1, Weapon.Type.SWORD);
		w.distanceType.put(2, Weapon.Type.SWORD);
		u.getInventory().add(w);
		u.equip(0);
		
		AnimationArgs dut = new AnimationArgs(u, true, 2);
		org.junit.Assert.assertEquals("rangedsword", dut.wepAnimName);
		org.junit.Assert.assertEquals("ranged", dut.classification);
	}
	
	@Test
	public void test_WhenMagicSwordAtTwoRange_ThenMagic() {
		Unit u = newSwordUser();
		Weapon w = new Weapon("Iron Sword");
		w.distanceType = new java.util.HashMap<Integer, Weapon.Type>();
		w.distanceType.put(1, Weapon.Type.SWORD);
		w.distanceType.put(2, Weapon.Type.ANIMA);
		u.getInventory().add(w);
		u.equip(0);
		
		AnimationArgs dut = new AnimationArgs(u, true, 2);
		org.junit.Assert.assertEquals("magic", dut.wepAnimName);
		org.junit.Assert.assertEquals("magic", dut.classification);
	}
	
	
	
	
	private Unit newSwordUser() {
		HashMap<String, Integer> bases = new HashMap<String, Integer>();
		bases.put("Lvl", 1);
		bases.put("HP", 10);
		bases.put("Str", 10);
		bases.put("Mag", 10);
		bases.put("Skl", 10);
		bases.put("Spd", 10);
		bases.put("Lck", 10);
		bases.put("Def", 10);
		bases.put("Res", 10);
		bases.put("Con", 10);
		bases.put("Mov", 10);
		
		return new Unit("sworduser", Class.createClass("Roy"), '-', bases, bases);
	}
	
}
