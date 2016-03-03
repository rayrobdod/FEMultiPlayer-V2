package net.fe.overworldStage;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TileTest {

	@Test
	public void testGetTerrainMatchesStatic() {
		for (int i = 0; i < 100; i++) {
			assertEquals(Tile.getTerrainFromID(i), new Tile(0,0,i).getTerrain());
		}
	}
}