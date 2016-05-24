package net.fe.network.command;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.HashMap;
import net.fe.unit.UnitIdentifier;
import net.fe.network.message.CommandMessage;
import net.fe.FEMultiplayer;
import net.fe.Player;
import net.fe.Session;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.Terrain;
import net.fe.overworldStage.Zone;
import net.fe.unit.Class;
import net.fe.unit.Unit;
import net.fe.unit.HealingItem;
import net.fe.unit.RiseTome;
import net.fe.unit.Weapon;

public final class SmiteCommandTest {
	
	@Before
	public void globalPlayerBefore() {
		Player p = new Player("null pointers", (byte) 0);
		p.setTeam(Player.TEAM_BLUE);
		p.getParty().setColor(net.fe.Party.TEAM_BLUE);
		FEMultiplayer.setLocalPlayer(p);
	}
	
	@Test
	public void testServer() {
		
		// given a stage and a unit
		new net.fe.network.FEServer(); // ...
		Session session = new Session();
		session.setMap("test"); // Must be a valid name, despite the bypass `stage.grid = ` later
		session.addPlayer(FEMultiplayer.getLocalPlayer());
		OverworldStage stage = new OverworldStage(session);
		stage.grid = new Grid(6,6, Terrain.PLAIN);
		
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit shover = new Unit("shover", Class.createClass("Sorcerer"), '-', vals, vals);
		Unit shovee = new Unit("shovee", Class.createClass("Sorcerer"), '-', vals, vals);
		FEMultiplayer.getLocalPlayer().getParty().addUnit(shover); // processCommands cannot find the unit without this
		FEMultiplayer.getLocalPlayer().getParty().addUnit(shovee); // processCommands cannot find the unit without this 
		assertTrue("Failed to add unit to grid", stage.addUnit(shover, 1, 1));
		assertTrue("Failed to add unit to grid", stage.addUnit(shovee, 1, 2));
		
		Object result = new SmiteCommand(new UnitIdentifier(shovee)).applyServer(stage, shover);
		
		// then the shovee has a new position
		assertEquals(null, result);
		assertEquals("x", 1, shovee.getXCoord());
		assertEquals("y", 4, shovee.getYCoord());
	}
	
}
