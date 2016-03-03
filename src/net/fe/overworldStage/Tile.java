package net.fe.overworldStage;

import net.fe.FEResources;
import chu.engine.Game;
import chu.engine.GriddedEntity;
import chu.engine.anim.Renderer;
import chu.engine.anim.Tileset;

// TODO: Auto-generated Javadoc
/**
 * A tile on a game board
 */
public final class Tile extends GriddedEntity implements DoNotDestroy{
	
	/** The identifier of the type of tile */
	private int id;
	
	/** The tileset. */
	private static Tileset tileset;
	
	static {
		if(Game.glContextExists()) {
			tileset = new Tileset(FEResources.getTexture("terrain_tiles"), 16, 16);
		}
	}
	
	/** P - Plain A - pAth M - Mountain ^ - Forest = - Wall S - Sea L - fLoor T - forT K - peaK I - pIllar D - Desert - - fence N - None V - Village O - thrOne (or castle entrance) C - Cliff ~ - Hill H - House. */
	private static String terrainMap =
			"PPPPAAAAAAA----SSSS      " +
			"PPPPAAAAAAA----SSSS      " +
			"PPPKKPAAA^^====SSSS      " +
			"MKKKAAAANNN====SSSS      " +
			"----AAAANVN==LSSS        " +
			"NNNNNNNN^N^SLLSSS        " +
			"NVNNNNVN^V^SLLSSS        " +
			"KPKKKPPPAAAANNNNNNNNN    " +
			"KPPPKPPPAAAANNNNNNNNN    " +
			"KKKKKPPPAAAANVNNONNON    " +
			"KKKKKAAAPPAAAAAHHSSSS    " +
			"KKKKKAAAAAAAAPTHTSSSS    " +
			"KKKKKAAAAAAAANSSSSS      " +
			"KKKKKKKMMMMCCCSSSNNN     " +
			"KKKKKPK^^^^CPCSSSNVN     " +
			"KKKKKPKKKKKKKCCC PA      " +
			"=======LL=P=LLSSS====    " +
			"=======LL=P=LLSS======   " +
			"LLL====LLLPOLLSS=L=IOI   " +
			"LLLLLILLIILLLLSSSLLLLL   " +
			"LLLLLILLILLLLLSSSLLL     " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         ";

	/**
	 * Instantiates a new tile.
	 *
	 * @param x the x
	 * @param y the y
	 * @param id the id
	 */
	public Tile(int x, int y, int id) {
		super(x,y);
		renderDepth = ClientOverworldStage.TILE_DEPTH;
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render(){
		Renderer.addClip(0, 0, 368, 240, false);
		ClientOverworldStage c = (ClientOverworldStage)stage;
		tileset.render(x - c.camX, y - c.camY, id%25, id/25, renderDepth);
	}

	/**
	 * Gets the terrain.
	 *
	 * @return the terrain
	 */
	public Terrain getTerrain() {
		return Tile.getTerrainFromID(this.id);
	}
	
	/**
	 * Gets the terrain from id.
	 *
	 * @param id the id
	 * @return the terrain from id
	 */
	public static Terrain getTerrainFromID(int id) {
		char ch = terrainMap.charAt(id);
		Terrain t;
		if(ch == 'P') t = Terrain.PLAIN;
		else if(ch == '=') t = Terrain.WALL;
		else if(ch == 'A') t = Terrain.PATH;
		else if(ch == 'S') t = Terrain.SEA;
		else if(ch == '^') t = Terrain.FOREST;
		else if(ch == 'L') t = Terrain.FLOOR;
		else if(ch == 'K') t = Terrain.PEAK;
		else if(ch == 'M') t = Terrain.MOUNTAIN;
		else if(ch == 'T') t = Terrain.FORT;
		else if(ch == 'V') t = Terrain.VILLAGE;
		else if(ch == 'N') t = Terrain.NONE;
		else if(ch == '-') t = Terrain.FENCE;
		else if(ch == 'D') t = Terrain.DESERT;
		else if(ch == 'I') t = Terrain.PILLAR;
		else if(ch == 'O') t = Terrain.THRONE;
		else if(ch == 'H') t = Terrain.HOUSE;
		else if(ch == '~') t = Terrain.HILL;
		
		else t = Terrain.NONE;
		return t;
	}
}
