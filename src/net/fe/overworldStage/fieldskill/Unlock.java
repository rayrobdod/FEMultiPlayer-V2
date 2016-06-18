package net.fe.overworldStage.fieldskill;

import java.io.Serializable;
import java.util.Set;

import net.fe.unit.Unit;
import net.fe.overworldStage.FieldSkill;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Zone;
import net.fe.overworldStage.Terrain;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.Path;

/**
 * A skill in which a unit can turn a door into a floor
 */
public final class Unlock extends FieldSkill {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6468268282716381357L;
	
	/**
	 * A skill that can be used in the overworld
	 */
	public Unlock() {
	}
	
	/**
	 * Checks whether the unit is capable of shiving anyone
	 * @param unit the unit to check
	 * @param grid the grid containing the unit
	 */
	@Override
	public boolean allowed(Unit unit, Grid grid) {
		Set<Node> range = grid.getRange(new Node(unit.getXCoord(), unit.getYCoord()), 1);
		for (Node n : range) {
			if (grid.getTerrain(n.x, n.y) == Terrain.DOOR) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the context to start when this command is selected
	 */
	@Override
	public OverworldContext onSelect(ClientOverworldStage stage, OverworldContext context, Zone z, Unit unit) {
		return new net.fe.overworldStage.context.UnlockTarget(stage, context, z, unit);
	}
	
	@Override
	public Zone getZone(Unit unit, Grid grid) {
		return new Zone(grid.getRange(
					new Node(unit.getXCoord(), unit.getYCoord()), 1),
					Zone.MOVE_DARK);
	}
}
