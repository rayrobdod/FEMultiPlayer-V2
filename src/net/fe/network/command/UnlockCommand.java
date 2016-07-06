package net.fe.network.command;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import net.fe.fightStage.AttackRecord;
import net.fe.overworldStage.Terrain;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.fieldskill.Unlock;
import net.fe.overworldStage.Tile;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Unit;
import net.fe.unit.Item;
import net.fe.unit.FieldSkillItem;
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
		final int deltaX = Math.abs(unlockX - unit.getXCoord());
		final int deltaY = Math.abs(unlockY - unit.getYCoord());
		
		if (deltaX + deltaY != 1) {
			throw new IllegalStateException("UNLOCK: door is not one space away from unlocker (" + deltaX + " + " + deltaY + ")");
		} else if (stage.grid.getTerrain(unlockX, unlockY) != Terrain.DOOR) {
			throw new IllegalStateException("UNLOCK: not unlocking a door (" + stage.grid.getTerrain(unlockX, unlockY) + ")");
		} else {
			if (unit.getTheClass().fieldSkills.contains(new Unlock())) {
				// if the class has locktouch, no need to have a key
				stage.grid.setTerrain(unlockX, unlockY, Terrain.FLOOR);
				return null;
			} else {
				// if class not has locktouch, then it needs a key
				int keyToUse = -1;
				List<Item> items = unit.getInventory();
				for(int z = 0; z < items.size(); z++){
					
					if (items.get(z) instanceof FieldSkillItem) {
						if (new Unlock().equals(((FieldSkillItem) items.get(z)).skill)) {
							keyToUse = z;
						}
					}
				}
				
				if (keyToUse == -1) {
					throw new IllegalStateException("UNLOCK: Unit has neither locktouch nor key.");
				} else {
					stage.grid.setTerrain(unlockX, unlockY, Terrain.FLOOR);
					return null;
				}
			}
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
