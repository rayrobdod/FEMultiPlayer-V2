package net.fe.unit;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.fe.fightStage.Brave;
import net.fe.fightStage.CombatTrigger;
import net.fe.fightStage.EclipseSix;
import net.fe.fightStage.Effective;
import net.fe.fightStage.LunaPlus;
import net.fe.fightStage.Nosferatu;
import net.fe.fightStage.CrossBow;

public final class WeaponFactoryTest {
	@Before
	public void preload() {
		WeaponFactory.loadWeapons();
	}
	
	@Test
	public void test_assorted_getTriggers() {
		/* Created as a quick regression test; will probably be invalidated as soon as the balancing happens */
		assertEquals(Collections.emptyList(), WeaponFactory.getWeapon("Iron Sword").getTriggers());
		assertEquals(Collections.emptyList(), WeaponFactory.getWeapon("Steel Sword").getTriggers());
		assertEquals(Collections.singletonList(new Effective(2, java.util.Arrays.asList("Paladin", "Valkyrie", "Ephraim", "Eirika", "Eliwood"))), WeaponFactory.getWeapon("Sol Katti").getTriggers());
		assertEquals(Collections.singletonList(new Effective(3, WeaponFactory.fliers)), WeaponFactory.getWeapon("Iron Bow").getTriggers());
		assertEquals(Collections.singletonList(new Brave()), WeaponFactory.getWeapon("Brave Sword").getTriggers());
		assertEquals(Collections.singletonList(new Brave()), WeaponFactory.getWeapon("Brave Lance").getTriggers());
		assertEquals(Collections.singletonList(new Brave()), WeaponFactory.getWeapon("Brave Axe").getTriggers());
		assertEquals(java.util.Arrays.asList(new Effective(3, WeaponFactory.fliers), new Brave()), WeaponFactory.getWeapon("Brave Bow").getTriggers());
		assertEquals(Collections.singletonList(new EclipseSix()), WeaponFactory.getWeapon("Eclipse").getTriggers());
		assertEquals(Collections.singletonList(new LunaPlus()), WeaponFactory.getWeapon("Lunase").getTriggers());
		assertEquals(Collections.singletonList(new Nosferatu()), WeaponFactory.getWeapon("Nosferatu").getTriggers());
		assertEquals(java.util.Arrays.asList(new Effective(3, WeaponFactory.fliers), new CrossBow()), WeaponFactory.getWeapon("Bowgun").getTriggers());
		assertEquals(java.util.Arrays.asList(new Effective(3, WeaponFactory.fliers), new CrossBow()), WeaponFactory.getWeapon("Crossbow").getTriggers());
		assertEquals(java.util.Arrays.asList(new Effective(3, WeaponFactory.fliers), new CrossBow()), WeaponFactory.getWeapon("Aqqar").getTriggers());
		assertEquals(java.util.Arrays.asList(new Effective(3, WeaponFactory.fliers), new CrossBow()), WeaponFactory.getWeapon("Arbalest").getTriggers());
	}
}