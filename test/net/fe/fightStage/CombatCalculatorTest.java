package net.fe.fightStage;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Collections;
import net.fe.fightStage.Effective;
import net.fe.fightStage.Reaver;
import net.fe.rng.RNG;
import net.fe.unit.Class;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;
import net.fe.unit.Statistics;

public final class CombatCalculatorTest {
	
	@Test
	public void calculateBaseDamage_MagicWeaponAndNoTriggers() {
		Statistics leftVals = new Statistics();
		leftVals = leftVals.copy("HP", 20);
		leftVals = leftVals.copy("Mov", 5);
		leftVals = leftVals.copy("Con", 8);
		leftVals = leftVals.copy("Mag", 10);
		Unit left = new Unit("left", Class.createClass("Sage"), '-', leftVals, leftVals);
		left.equip(createWeapon(Weapon.Type.LIGHT, 8));
		org.junit.Assert.assertTrue(null != left.getWeapon());
		
		Statistics rightVals = new Statistics();
		rightVals = rightVals.copy("HP", 20);
		rightVals = rightVals.copy("Mov", 5);
		rightVals = rightVals.copy("Con", 8);
		rightVals = rightVals.copy("Res", 5);
		Unit right = new Unit("right", Class.createClass("Phantom"), '-', rightVals, rightVals);
		
		assertEquals(13, CombatCalculator.calculatePreviewStats(left, right, true).damage);
	}
	
	@Test
	public void calculateBaseDamage_PhysicalWeaponAndNoTriggers() {
		Statistics leftVals = new Statistics();
		leftVals = leftVals.copy("HP", 20);
		leftVals = leftVals.copy("Mov", 5);
		leftVals = leftVals.copy("Con", 8);
		leftVals = leftVals.copy("Str", 10);
		Unit left = new Unit("left", Class.createClass("Phantom"), '-', leftVals, leftVals);
		left.equip(createWeapon(Weapon.Type.AXE, 8));
		
		Statistics rightVals = new Statistics();
		rightVals = rightVals.copy("HP", 20);
		rightVals = rightVals.copy("Mov", 5);
		rightVals = rightVals.copy("Con", 8);
		rightVals = rightVals.copy("Def", 5);
		Unit right = new Unit("right", Class.createClass("Phantom"), '-', rightVals, rightVals);
		
		assertEquals(13, CombatCalculator.calculatePreviewStats(left, right, true).damage);
	}
	
	@Test
	public void calculatePreviewDamage_MagicWeaponAndNoTriggers() {
		Statistics leftVals = new Statistics();
		leftVals = leftVals.copy("HP", 20);
		leftVals = leftVals.copy("Mov", 5);
		leftVals = leftVals.copy("Con", 8);
		leftVals = leftVals.copy("Mag", 10);
		Unit left = new Unit("left", Class.createClass("Sage"), '-', leftVals, leftVals);
		left.equip(createWeapon(Weapon.Type.LIGHT, 8));
		
		Statistics rightVals = new Statistics();
		rightVals = rightVals.copy("HP", 20);
		rightVals = rightVals.copy("Mov", 5);
		rightVals = rightVals.copy("Con", 8);
		rightVals = rightVals.copy("Res", 5);
		Unit right = new Unit("right", Class.createClass("Phantom"), '-', rightVals, rightVals);
		
		assertEquals(13, CombatCalculator.calculatePreviewStats(left, right, true).damage);
	}
	
	@Test
	public void calculatePreviewDamage_PhysicalWeaponAndNoTriggers() {
		Statistics leftVals = new Statistics();
		leftVals = leftVals.copy("HP", 20);
		leftVals = leftVals.copy("Mov", 5);
		leftVals = leftVals.copy("Con", 8);
		leftVals = leftVals.copy("Str", 10);
		Unit left = new Unit("left", Class.createClass("Phantom"), '-', leftVals, leftVals);
		left.equip(createWeapon(Weapon.Type.AXE, 8));
		
		Statistics rightVals = new Statistics();
		rightVals = rightVals.copy("HP", 20);
		rightVals = rightVals.copy("Mov", 5);
		rightVals = rightVals.copy("Con", 8);
		rightVals = rightVals.copy("Def", 5);
		Unit right = new Unit("right", Class.createClass("Phantom"), '-', rightVals, rightVals);
		
		assertEquals(13, CombatCalculator.calculatePreviewStats(left, right, true).damage);
	}
	
	@Test
	public void calculatePreviewDamage_PhysicalWeaponAndShowYourModTrigger() {
		Statistics leftVals = new Statistics();
		leftVals = leftVals.copy("HP", 20);
		leftVals = leftVals.copy("Mov", 5);
		leftVals = leftVals.copy("Con", 8);
		leftVals = leftVals.copy("Str", 10);
		Unit left = new Unit("left", Class.createClass("Phantom"), '-', leftVals, leftVals);
		left.equip(createWeapon(Weapon.Type.AXE, 8));
		left.addSkill(new CombatTrigger(CombatTrigger.NO_NAME_MOD, CombatTrigger.YOUR_TURN_MOD | CombatTrigger.SHOW_IN_PREVIEW) {
			public int runDamageMod(Unit a, Unit d, int damage) { return 2; }
			public boolean attempt(Unit user, int range, Unit opponent, RNG rng) { return true; }
			public CombatTrigger getCopy() {return this;}
		});
		
		Statistics rightVals = new Statistics();
		rightVals = rightVals.copy("HP", 20);
		rightVals = rightVals.copy("Mov", 5);
		rightVals = rightVals.copy("Con", 8);
		rightVals = rightVals.copy("Def", 5);
		Unit right = new Unit("right", Class.createClass("Phantom"), '-', rightVals, rightVals);
		
		assertEquals(2, CombatCalculator.calculatePreviewStats(left, right, true).damage);
	}
	
	@Test
	public void calculatePreviewDamage_PhysicalWeaponAndNoShowYourModTrigger() {
		Statistics leftVals = new Statistics();
		leftVals = leftVals.copy("HP", 20);
		leftVals = leftVals.copy("Mov", 5);
		leftVals = leftVals.copy("Con", 8);
		leftVals = leftVals.copy("Str", 10);
		Unit left = new Unit("left", Class.createClass("Phantom"), '-', leftVals, leftVals);
		left.equip(createWeapon(Weapon.Type.AXE, 8));
		left.addSkill(new CombatTrigger(CombatTrigger.NO_NAME_MOD, CombatTrigger.YOUR_TURN_MOD) {
			public int runDamageMod(Unit a, Unit d, int damage) { return 2; }
			public boolean attempt(Unit user, int range, Unit opponent, RNG rng) { return true; }
			public CombatTrigger getCopy() {return this;}
		});
		
		Statistics rightVals = new Statistics();
		rightVals = rightVals.copy("HP", 20);
		rightVals = rightVals.copy("Mov", 5);
		rightVals = rightVals.copy("Con", 8);
		rightVals = rightVals.copy("Def", 5);
		Unit right = new Unit("right", Class.createClass("Phantom"), '-', rightVals, rightVals);
		
		assertEquals(13, CombatCalculator.calculatePreviewStats(left, right, true).damage);
	}
	
	@Test
	public void calculatePreviewDamage_EffectiveDamage() {
		Weapon effectiveWeapon = createEffectiveWeapon(Weapon.Type.AXE, 5, "Phantom");
		
		Statistics leftVals = new Statistics();
		leftVals = leftVals.copy("HP", 20);
		leftVals = leftVals.copy("Str", 2);
		Unit left = new Unit("left", Class.createClass("Phantom"), '-', leftVals, leftVals);
		left.equip(effectiveWeapon);
		
		Statistics rightVals = new Statistics();
		rightVals = rightVals.copy("HP", 20);
		rightVals = rightVals.copy("Def", 3);
		Unit right = new Unit("right", Class.createClass("Phantom"), '-', rightVals, rightVals);
		
		assertEquals(2 + 5 * 3 - 3, CombatCalculator.calculatePreviewStats(left, right, true).damage);
	}
	
	@Test
	public void calculatePreviewDamage_NotEffectiveDamage() {
		Weapon effectiveWeapon = createEffectiveWeapon(Weapon.Type.AXE, 5, "Paladin");
		
		Statistics leftVals = new Statistics();
		leftVals = leftVals.copy("HP", 20);
		leftVals = leftVals.copy("Str", 2);
		Unit left = new Unit("left", Class.createClass("Phantom"), '-', leftVals, leftVals);
		left.equip(effectiveWeapon);
		
		Statistics rightVals = new Statistics();
		rightVals = rightVals.copy("HP", 20);
		rightVals = rightVals.copy("Def", 3);
		Unit right = new Unit("right", Class.createClass("Phantom"), '-', rightVals, rightVals);
		
		assertEquals(2 + 5 - 3, CombatCalculator.calculatePreviewStats(left, right, true).damage);
	}
	
	@Test
	public void calculatePreviewDamage_EffectiveTriangleDamage() {
		Weapon effectiveWeapon = createEffectiveWeapon(Weapon.Type.AXE, 5, "Paladin");
		
		Statistics leftVals = new Statistics();
		leftVals = leftVals.copy("HP", 20);
		leftVals = leftVals.copy("Str", 2);
		Unit left = new Unit("left", Class.createClass("Phantom"), '-', leftVals, leftVals);
		left.equip(effectiveWeapon);
		
		Statistics rightVals = new Statistics();
		rightVals = rightVals.copy("HP", 20);
		rightVals = rightVals.copy("Def", 3);
		Unit right = new Unit("right", Class.createClass("Paladin"), '-', rightVals, rightVals);
		
		right.equip(createWeapon(Weapon.Type.AXE, 8));
		int normalDamage = CombatCalculator.calculatePreviewStats(left, right, true).damage;
		
		right.equip(createWeapon(Weapon.Type.LANCE, 8));
		int wtaDamage = CombatCalculator.calculatePreviewStats(left, right, true).damage;
		
		right.equip(createWeapon(Weapon.Type.SWORD, 8));
		int wtdDamage = CombatCalculator.calculatePreviewStats(left, right, true).damage;
		
		assertEquals(3, wtaDamage - normalDamage);
		assertEquals(3, normalDamage - wtdDamage);
	}
	
	@Test
	public void calculatePreviewDamage_triangle_normalAdvantage() {
		Statistics bases = new Statistics();
		bases = bases.copy("HP", 20);
		Unit axeUnit = new Unit("left", Class.createClass("Paladin"), '-', bases, new Statistics());
		axeUnit.equip(createWeapon(Weapon.Type.AXE, 5));
		Unit swordUnit = new Unit("right", Class.createClass("Paladin"), '-', bases, new Statistics());
		swordUnit.equip(createWeapon(Weapon.Type.SWORD, 5));
		
		int neutralDamage = CombatCalculator.calculatePreviewStats(axeUnit, axeUnit, true).damage;
		int advantageDamage = CombatCalculator.calculatePreviewStats(swordUnit, axeUnit, true).damage;
		
		assertEquals(1, advantageDamage - neutralDamage);
	}
	
	@Test
	public void calculatePreviewDamage_triangle_normalDisadvantage() {
		Statistics bases = new Statistics();
		bases = bases.copy("HP", 20);
		Unit axeUnit = new Unit("left", Class.createClass("Paladin"), '-', bases, new Statistics());
		axeUnit.equip(createWeapon(Weapon.Type.AXE, 5));
		Unit swordUnit = new Unit("right", Class.createClass("Paladin"), '-', bases, new Statistics());
		swordUnit.equip(createWeapon(Weapon.Type.SWORD, 5));
		
		int neutralDamage = CombatCalculator.calculatePreviewStats(axeUnit, axeUnit, true).damage;
		int disadvantageDamage = CombatCalculator.calculatePreviewStats(axeUnit, swordUnit, true).damage;
		
		assertEquals(-1, disadvantageDamage - neutralDamage);
	}
	
	@Test
	public void calculatePreviewDamage_triangle_reaverAdvantage() {
		Statistics bases = new Statistics();
		bases = bases.copy("HP", 20);
		Unit axeUnit = new Unit("left", Class.createClass("Paladin"), '-', bases, new Statistics());
		axeUnit.equip(createReaverWeapon(Weapon.Type.AXE, 5));
		Unit swordUnit = new Unit("right", Class.createClass("Paladin"), '-', bases, new Statistics());
		swordUnit.equip(createWeapon(Weapon.Type.SWORD, 5));
		
		int neutralDamage = CombatCalculator.calculatePreviewStats(axeUnit, axeUnit, true).damage;
		int advantageDamage = CombatCalculator.calculatePreviewStats(swordUnit, axeUnit, true).damage;
		
		assertEquals(-2, advantageDamage - neutralDamage);
	}
	
	@Test
	public void calculatePreviewDamage_triangle_reaverDisadvantage() {
		Statistics bases = new Statistics();
		bases = bases.copy("HP", 20);
		Unit axeUnit = new Unit("left", Class.createClass("Paladin"), '-', bases, new Statistics());
		axeUnit.equip(createReaverWeapon(Weapon.Type.AXE, 5));
		Unit swordUnit = new Unit("right", Class.createClass("Paladin"), '-', bases, new Statistics());
		swordUnit.equip(createWeapon(Weapon.Type.SWORD, 5));
		
		int neutralDamage = CombatCalculator.calculatePreviewStats(axeUnit, axeUnit, true).damage;
		int disadvantageDamage = CombatCalculator.calculatePreviewStats(axeUnit, swordUnit, true).damage;
		
		assertEquals(2, disadvantageDamage - neutralDamage);
	}
	
	@Test
	public void calculatePreviewDamage_triangle_reaverAdvantage2() {
		Statistics bases = new Statistics();
		bases = bases.copy("HP", 20);
		Unit axeUnit = new Unit("left", Class.createClass("Paladin"), '-', bases, new Statistics());
		axeUnit.equip(createWeapon(Weapon.Type.AXE, 5));
		Unit swordUnit = new Unit("right", Class.createClass("Paladin"), '-', bases, new Statistics());
		swordUnit.equip(createReaverWeapon(Weapon.Type.SWORD, 5));
		
		int neutralDamage = CombatCalculator.calculatePreviewStats(axeUnit, axeUnit, true).damage;
		int advantageDamage = CombatCalculator.calculatePreviewStats(swordUnit, axeUnit, true).damage;
		
		assertEquals(-2, advantageDamage - neutralDamage);
	}
	
	@Test
	public void calculatePreviewDamage_triangle_reaverDisadvantage2() {
		Statistics bases = new Statistics();
		bases = bases.copy("HP", 20);
		Unit axeUnit = new Unit("left", Class.createClass("Paladin"), '-', bases, new Statistics());
		axeUnit.equip(createWeapon(Weapon.Type.AXE, 5));
		Unit swordUnit = new Unit("right", Class.createClass("Paladin"), '-', bases, new Statistics());
		swordUnit.equip(createReaverWeapon(Weapon.Type.SWORD, 5));
		
		int neutralDamage = CombatCalculator.calculatePreviewStats(axeUnit, axeUnit, true).damage;
		int disadvantageDamage = CombatCalculator.calculatePreviewStats(axeUnit, swordUnit, true).damage;
		
		assertEquals(2, disadvantageDamage - neutralDamage);
	}
	
	@Test
	public void calculatePreviewDamage_triangle_doubleReaverAdvantage() {
		Statistics bases = new Statistics();
		bases = bases.copy("HP", 20);
		Unit axeUnit = new Unit("left", Class.createClass("Paladin"), '-', bases, new Statistics());
		axeUnit.equip(createReaverWeapon(Weapon.Type.AXE, 5));
		Unit swordUnit = new Unit("right", Class.createClass("Paladin"), '-', bases, new Statistics());
		swordUnit.equip(createReaverWeapon(Weapon.Type.SWORD, 5));
		
		int neutralDamage = CombatCalculator.calculatePreviewStats(axeUnit, axeUnit, true).damage;
		int advantageDamage = CombatCalculator.calculatePreviewStats(swordUnit, axeUnit, true).damage;
		
		assertEquals(1, advantageDamage - neutralDamage);
	}
	
	@Test
	public void calculatePreviewDamage_triangle_doubleReaverDisadvantage() {
		Statistics bases = new Statistics();
		bases = bases.copy("HP", 20);
		Unit axeUnit = new Unit("left", Class.createClass("Paladin"), '-', bases, new Statistics());
		axeUnit.equip(createReaverWeapon(Weapon.Type.AXE, 5));
		Unit swordUnit = new Unit("right", Class.createClass("Paladin"), '-', bases, new Statistics());
		swordUnit.equip(createReaverWeapon(Weapon.Type.SWORD, 5));
		
		int neutralDamage = CombatCalculator.calculatePreviewStats(axeUnit, axeUnit, true).damage;
		int disadvantageDamage = CombatCalculator.calculatePreviewStats(axeUnit, swordUnit, true).damage;
		
		assertEquals(-1, disadvantageDamage - neutralDamage);
	}
	
	
	private Weapon createWeapon(Weapon.Type type, int might) {
		
		Weapon retVal = new Weapon(
			"fork", 1, 0, 0,
			type, might, 0, 0, (s) -> java.util.Arrays.asList(1),
			new Statistics(), new java.util.ArrayList<>(), null
		);
		return retVal;
	}
	
	private Weapon createReaverWeapon(Weapon.Type type, int might) {
		Weapon retVal = new Weapon(
			"forkreaver", 1, 0, 0,
			type, might, 0, 0, (s) -> java.util.Arrays.asList(1),
			new Statistics(), Collections.singletonList(new Reaver()), null
		);
		return retVal;
	}
	
	private Weapon createEffectiveWeapon(Weapon.Type type, int might, String effectiveClass) {
		Weapon retVal = new Weapon(
			"forkreaver", 1, 0, 0,
			type, might, 0, 0, (s) -> java.util.Arrays.asList(1),
			new Statistics(), Collections.singletonList(new Effective(3, Collections.singletonList(effectiveClass))), null
		);
		return retVal;
	}
}
