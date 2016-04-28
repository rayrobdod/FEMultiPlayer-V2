package net.fe.overworldStage.context;

import java.util.List;
import java.util.HashMap;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.lwjgl.opengl.Display;

import net.fe.FEMultiplayer;
import net.fe.Player;
import net.fe.Session;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.Terrain;
import net.fe.overworldStage.Zone;
import net.fe.unit.Class;
import net.fe.unit.Item;
import net.fe.unit.RiseTome;
import net.fe.unit.Unit;

public final class UnitMovedTest {
	
	@Before
	public void globalDisplayBefore() {
		try {
			Display.setDisplayMode(new org.lwjgl.opengl.DisplayMode(5, 5));
			Display.create();
		} catch (org.lwjgl.LWJGLException e) {
			
		}
	}
	
	@Before
	public void globalPlayerBefore() {
		Player p = new Player("null pointers", (byte) 0);
		p.setTeam(Player.TEAM_BLUE);
		p.getParty().setColor(net.fe.Party.TEAM_BLUE);
		FEMultiplayer.setLocalPlayer(p);
	}
	
	@After
	public void globalDisplayAfter() {
		Display.destroy();
	}

	@Test
	public void testGetCommands_whenEmptyPlains_thenItemWait() {
		// things that have nothing to do with the test but need to be set up anyway
		Session session = new Session();
		session.setMap("test"); // Must be a valid name, despite the bypass `stage.grid = ` later
		session.addPlayer(FEMultiplayer.getLocalPlayer());
		ClientOverworldStage stage = new ClientOverworldStage(session);
		stage.cursor.stage = stage; // this just looks wrong
		stage.grid = new Grid(3,3, Terrain.PLAIN);
		
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		Unit unit = new Unit("test", Class.createClass("Roy"), '-', vals, vals);
		
		// the actual tests
		UnitMoved dut = new UnitMoved(stage, null, unit, false, false);
		List<String> res = dut.getCommands(unit);
		
		assertEquals(res, java.util.Arrays.asList("Item", "Wait"));
	}

	@Test
	public void testGetCommands_whenSummoner_thenSummon() {
		// things that have nothing to do with the test but need to be set up anyway
		Session session = new Session();
		session.setMap("test"); // Must be a valid name, despite the bypass `stage.grid = ` later
		session.addPlayer(FEMultiplayer.getLocalPlayer());
		ClientOverworldStage stage = new ClientOverworldStage(session);
		stage.cursor.stage = stage; // this just looks wrong
		stage.grid = new Grid(3,3, Terrain.PLAIN);
		
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		Unit unit = new Unit("test", Class.createClass("Sorcerer"), '-', vals, vals);
		unit.addToInventory(new RiseTome());
		
		// the actual tests
		UnitMoved dut = new UnitMoved(stage, null, unit, false, false);
		List<String> res = dut.getCommands(unit);
		
		assertTrue(res.contains("Summon"));
	}

	@Test
	public void testGetCommands_whenNonSummonerHasRise_thenNotSummon() {
		// things that have nothing to do with the test but need to be set up anyway
		Session session = new Session();
		session.setMap("test"); // Must be a valid name, despite the bypass `stage.grid = ` later
		session.addPlayer(FEMultiplayer.getLocalPlayer());
		ClientOverworldStage stage = new ClientOverworldStage(session);
		stage.cursor.stage = stage; // this just looks wrong
		stage.grid = new Grid(3,3, Terrain.PLAIN);
		
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		Unit unit = new Unit("test", Class.createClass("Roy"), '-', vals, vals);
		unit.addToInventory(new RiseTome());
		
		// the actual tests
		UnitMoved dut = new UnitMoved(stage, null, unit, false, false);
		List<String> res = dut.getCommands(unit);
		
		assertTrue(! res.contains("Summon"));
	}

	@Test
	public void testGetCommands_whenDoorAndThief_thenUnlock() {
		// things that have nothing to do with the test but need to be set up anyway
		Session session = new Session();
		session.setMap("test"); // Must be a valid name, despite the bypass `stage.grid = ` later
		session.addPlayer(FEMultiplayer.getLocalPlayer());
		ClientOverworldStage stage = new ClientOverworldStage(session);
		stage.cursor.stage = stage; // this just looks wrong
		stage.grid = new Grid(3,3, Terrain.DOOR);
		
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		Unit unit = new Unit("test", Class.createClass("Assassin"), '-', vals, vals);
		
		// the actual tests
		UnitMoved dut = new UnitMoved(stage, null, unit, false, false);
		List<String> res = dut.getCommands(unit);
		
		assertTrue(res.contains("Unlock"));
	}

	@Test
	public void testGetCommands_whenDoorAndNotTheif_thenNotUnlock() {
		// things that have nothing to do with the test but need to be set up anyway
		Session session = new Session();
		session.setMap("test"); // Must be a valid name, despite the bypass `stage.grid = ` later
		session.addPlayer(FEMultiplayer.getLocalPlayer());
		ClientOverworldStage stage = new ClientOverworldStage(session);
		stage.cursor.stage = stage; // this just looks wrong
		stage.grid = new Grid(3,3, Terrain.DOOR);
		
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		Unit unit = new Unit("test", Class.createClass("Roy"), '-', vals, vals);
		
		// the actual tests
		UnitMoved dut = new UnitMoved(stage, null, unit, false, false);
		List<String> res = dut.getCommands(unit);
		
		assertTrue(! res.contains("Unlock"));
	}

	@Test
	public void testGetCommands_whenAdjacentAllies_thenTradeRescue() {
		// things that have nothing to do with the test but need to be set up anyway
		Session session = new Session();
		session.setMap("test"); // Must be a valid name, despite the bypass `stage.grid = ` later
		session.addPlayer(FEMultiplayer.getLocalPlayer());
		ClientOverworldStage stage = new ClientOverworldStage(session);
		stage.cursor.stage = stage; // this just looks wrong
		stage.grid = new Grid(3,3, Terrain.PLAIN);
		
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 10);
		vals.put("Aid", 15);
		Unit unit1 = new Unit("Roy", Class.createClass("Roy"), '-', vals, vals);
		Unit unit2 = new Unit("Ike", Class.createClass("Ike"), '-', vals, vals);
		stage.grid.addUnit(unit1, 0, 1);
		stage.grid.addUnit(unit2, 1, 1);
		FEMultiplayer.getLocalPlayer().getParty().addUnit(unit1);
		FEMultiplayer.getLocalPlayer().getParty().addUnit(unit2);
		
		// the actual tests
		UnitMoved dut = new UnitMoved(stage, null, unit2, false, false);
		List<String> res = dut.getCommands(unit2);
		
		assertEquals(res, java.util.Arrays.asList("Trade", "Rescue", "Item", "Wait"));
	}

	@Test
	public void testGetCommands_whenAdjacentEnemies_thenAttack() {
		// things that have nothing to do with the test but need to be set up anyway
		Session session = new Session();
		session.setMap("test"); // Must be a valid name, despite the bypass `stage.grid = ` later
		session.addPlayer(FEMultiplayer.getLocalPlayer());
		ClientOverworldStage stage = new ClientOverworldStage(session);
		stage.cursor.stage = stage; // this just looks wrong
		stage.grid = new Grid(3,3, Terrain.PLAIN);
		
		Player p2 = new Player("enemy", (byte) 1);
		p2.setTeam(Player.TEAM_RED);
		p2.getParty().setColor(net.fe.Party.TEAM_RED);
		
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 10);
		vals.put("Aid", 15);
		Unit unit1 = new Unit("Roy", Class.createClass("Roy"), '-', vals, vals);
		Unit unit2 = new Unit("Ike", Class.createClass("Ike"), '-', vals, vals);
		stage.grid.addUnit(unit1, 0, 1);
		stage.grid.addUnit(unit2, 1, 1);
		FEMultiplayer.getLocalPlayer().getParty().addUnit(unit1);
		p2.getParty().addUnit(unit2);
		net.fe.unit.WeaponFactory.loadWeapons();
		unit1.addToInventory(Item.getItem("Iron Sword"));
		unit1.reEquip();
		
		// the actual tests
		UnitMoved dut = new UnitMoved(stage, null, unit1, false, false);
		List<String> res = dut.getCommands(unit1);
		
		assertEquals(res, java.util.Arrays.asList("Attack", "Shove", "Item", "Wait"));
	}
}
