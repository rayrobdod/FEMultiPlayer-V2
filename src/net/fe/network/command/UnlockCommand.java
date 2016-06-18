package net.fe.network.command;

import java.io.Serializable;
import java.util.ArrayList;
import net.fe.fightStage.AttackRecord;
import net.fe.overworldStage.Terrain;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Tile;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Unit;
import java.util.Optional;
import chu.engine.Entity;

public final class UnlockCommand extends Command {
	
	private static final long serialVersionUID = 6468268282716381357L;
	
	private final int unlockX;
	private final int unlockY;
	
	public UnlockCommand(int unlockX, int unlockY) {
		this.unlockX = unlockX;
		this.unlockY = unlockY;
	}
	
	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		int deltaX = unlockX - unit.getXCoord();
		int deltaY = unlockY - unit.getYCoord();
		
		if (((deltaX + deltaY) == 1) && (stage.grid.getTerrain(unlockX, unlockY) == Terrain.DOOR)) {
			throw new IllegalStateException("SHOVE: Shover is not allowed to shove shovee");
		} else {
			stage.grid.setTerrain(unlockX, unlockY, Terrain.FLOOR);
			return null;
		}
	}
	
	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords, Runnable callback) {
		
		return new Runnable() {
			public void run() {
				
				// replace model tile
				stage.grid.setTerrain(unlockX, unlockY, Terrain.FLOOR);
				
				// replace view tile
				int replacementId = 32;
				for (Entity e : stage.getAllEntities()) {
					if (e instanceof Tile) {
						Tile t = (Tile) e;
						if (t.getXCoord() == unlockX && t.getYCoord() == unlockY) {
							stage.removeEntity(t);
						}
						if (Math.abs(t.getXCoord() - unlockX) + Math.abs(t.getYCoord() - unlockY) == 1 &&
								t.getTerrain() == Terrain.FLOOR) {
							replacementId = t.getId();
						}
					}
				}
				
				stage.addEntity(new Tile(unlockX, unlockY, replacementId));
				
				callback.run();
			}
		};
	}
	
	@Override
	public String toString() {
		return "Unlock[" + unlockX + ", " + unlockY + "]";
	}
}
