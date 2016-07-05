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
import net.fe.unit.Statistics;
import net.fe.unit.Class;
import net.fe.unit.Unit;

public class UnlockTargetTest {
	
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
	public void testTargetsWhenAllDoorsThenFourTargets() {
		// things that have nothing to do with the test but need to be set up anyway
		Session session = new Session();
		session.addPlayer(FEMultiplayer.getLocalPlayer());
		ClientOverworldStage stage = new ClientOverworldStage(session);
		stage.cursor.stage = stage; // this just looks wrong
		stage.grid = new Grid(3,3, Terrain.DOOR);
		
		Zone zone = new Zone(stage.grid.getRange(new Node(1,1), 1), null);
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 2, 0);
		Unit unit = new Unit("test", Class.createClass("Sorcerer"), '-', vals, vals);
		
		// the actual tests
		UnlockTarget dut = new UnlockTarget(stage, null, zone, unit);
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
	public void testTargetsWhenNotDoorThenNotATargets() {
		// things that have nothing to do with the test but need to be set up anyway
		Session session = new Session();
		session.addPlayer(FEMultiplayer.getLocalPlayer());
		ClientOverworldStage stage = new ClientOverworldStage(session);
		stage.cursor.stage = stage; // this just looks wrong
		stage.grid = new Grid(3,3, Terrain.DOOR);
		stage.grid.setTerrain(1,2, Terrain.PLAIN);
		
		Zone zone = new Zone(stage.grid.getRange(new Node(1,1), 1), null);
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 2, 0);
		Unit unit = new Unit("test", Class.createClass("Sorcerer"), '-', vals, vals);
		
		// the actual tests
		UnlockTarget dut = new UnlockTarget(stage, null, zone, unit);
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
		stage.grid = new Grid(3,3, Terrain.DOOR);
		
		Zone zone = new Zone(stage.grid.getRange(new Node(0,0), 1), null);
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 2, 0);
		Unit unit = new Unit("test", Class.createClass("Sorcerer"), '-', vals, vals);
		
		// the actual tests
		UnlockTarget dut = new UnlockTarget(stage, null, zone, unit);
		dut.startContext();
		
		assertEquals(new Node(0,1), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(1,0), dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(new Node(0,1), dut.getCurrentTarget());
	}

}
