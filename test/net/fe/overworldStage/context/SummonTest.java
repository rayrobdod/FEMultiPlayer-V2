package net.fe.overworldStage.context;

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
import net.fe.unit.Unit;
import net.fe.unit.Statistics;

public class SummonTest {
	
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
	public void testTargetsWhenAllPlainsThenFourTargets() {
		// things that have nothing to do with the test but need to be set up anyway
		Session session = new Session();
		session.addPlayer(FEMultiplayer.getLocalPlayer());
		ClientOverworldStage stage = new ClientOverworldStage(session);
		stage.cursor.stage = stage; // this just looks wrong
		stage.grid = new Grid(3,3, Terrain.PLAIN);
		
		Zone zone = new Zone(stage.grid.getRange(new Node(1,1), 1), null);
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 2, 0);
		Unit unit = new Unit("test", Class.createClass("Sorcerer"), '-', vals, vals);
		
		// the actual tests
		Summon dut = new Summon(stage, null, zone, unit);
		dut.startContext();
		
		assertEquals(new Node(0,1), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(1,0), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(1,2), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(2,1), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(0,1), dut.getCurrentTarget());
	}

	@Test
	public void testTargetsWhenMountainThenNotATargets() {
		// things that have nothing to do with the test but need to be set up anyway
		Session session = new Session();
		session.addPlayer(FEMultiplayer.getLocalPlayer());
		ClientOverworldStage stage = new ClientOverworldStage(session);
		stage.cursor.stage = stage; // this just looks wrong
		stage.grid = new Grid(3,3, Terrain.PLAIN);
		stage.grid.setTerrain(1,2, Terrain.WALL);
		
		Zone zone = new Zone(stage.grid.getRange(new Node(1,1), 1), null);
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 2, 0);
		Unit unit = new Unit("test", Class.createClass("Sorcerer"), '-', vals, vals);
		
		// the actual tests
		Summon dut = new Summon(stage, null, zone, unit);
		dut.startContext();
		
		assertEquals(new Node(0,1), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(1,0), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(2,1), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(0,1), dut.getCurrentTarget());
	}

	@Test
	public void testTargetsWhenCornerThenTargetsStillInBounds() {
		// things that have nothing to do with the test but need to be set up anyway
		Session session = new Session();
		session.addPlayer(FEMultiplayer.getLocalPlayer());
		ClientOverworldStage stage = new ClientOverworldStage(session);
		stage.cursor.stage = stage; // this just looks wrong
		stage.grid = new Grid(3,3, Terrain.PLAIN);
		
		Zone zone = new Zone(stage.grid.getRange(new Node(0,0), 1), null);
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 2, 0);
		Unit unit = new Unit("test", Class.createClass("Sorcerer"), '-', vals, vals);
		
		// the actual tests
		Summon dut = new Summon(stage, null, zone, unit);
		dut.startContext();
		
		assertEquals(new Node(0,1), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(1,0), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(0,1), dut.getCurrentTarget());
	}

	@Test
	public void testTargetsWhenTwoRange() {
		// things that have nothing to do with the test but need to be set up anyway
		Session session = new Session();
		session.addPlayer(FEMultiplayer.getLocalPlayer());
		ClientOverworldStage stage = new ClientOverworldStage(session);
		stage.cursor.stage = stage; // this just looks wrong
		stage.grid = new Grid(5,5, Terrain.PLAIN);
		
		Zone zone = new Zone(stage.grid.getRange(new Node(2,2), 2), null);
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 2, 0);
		Unit unit = new Unit("test", Class.createClass("Sorcerer"), '-', vals, vals);
		
		// the actual tests
		Summon dut = new Summon(stage, null, zone, unit);
		dut.startContext();
		
		assertEquals(new Node(0,2), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(1,1), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(2,0), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(1,3), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(3,1), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(2,4), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(3,3), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(4,2), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(0,2), dut.getCurrentTarget());
		dut.nextTarget();
	}
	
	@Test
	public void TestGenerateThenSummonLevelMatchesSummonerLevel() {
		for (int i = 1; i <= 20; i++) {
			Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 2, 0);
			Unit summoner = new Unit("test", Class.createClass("Sorcerer"), '-', vals, vals);
			summoner.setLevel(i);
			summoner.setParty(new net.fe.Party());
			
			Unit result = Summon.generateSummon(summoner);
			
			assertEquals(i, result.getLevel());
		}
	}
	
	@Test
	public void TestGenerateThenIsMoved() {
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 2, 0);
		Unit summoner = new Unit("test", Class.createClass("Sorcerer"), '-', vals, vals);
		summoner.setParty(new net.fe.Party());
		
		Unit result = Summon.generateSummon(summoner);
		
		assertTrue(result.hasMoved());
	}
}
