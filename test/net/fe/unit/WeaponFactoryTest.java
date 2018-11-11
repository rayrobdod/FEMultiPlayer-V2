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

public final class WeaponFactoryTest {
	
	@Before
	public void before_loadItems() {
		WeaponFactory.loadWeapons();
	}
	
	@Test
	public void getDefault_doesNotThrow() {
		for (Type t : Type.values()) {
			assertTrue(null != WeaponFactory.getDefaultForType(t));
		}
	}
	
}
