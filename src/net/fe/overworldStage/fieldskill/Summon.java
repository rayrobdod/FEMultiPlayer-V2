package net.fe.overworldStage.fieldskill;

import java.io.Serializable;
import java.util.Set;

import net.fe.unit.Unit;
import net.fe.overworldStage.FieldSkill;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Zone;
import net.fe.overworldStage.Zone.RangeIndicator;
import net.fe.overworldStage.Zone.RangeIndicator.RangeType;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.Path;
import net.fe.unit.Weapon;

/**
 * A skill in which a unit creates a weak allied unit
 */
public final class Summon extends FieldSkill {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** A Phantom's mov; used to determine if a terrain is a valid target to place a new phantom */
	private static final int phantomMov = net.fe.overworldStage.context.Summon.BASES.mov;
	
	/**
	 * 
	 */
	public Summon() {
	}
	
	@Override
	public boolean allowed(Unit unit, Grid grid) {
		// This skill assumes that the unit has the rise tome,
		// since the rise tome is theoretically the only thing that grants this skill
		
		// Only dark-tome users are allowed to summon
		if (! unit.getTheClass().usableWeapon.contains(Weapon.Type.DARK)) {
			return false;
		}
		
		
		// Summoning is only possible if there is an adjacent space open to place the new unit
		if (! grid.getRange(new Node(unit.getXCoord(), unit.getYCoord()), 1).stream()
			.anyMatch((Node n) -> grid.getVisibleUnit(n.x, n.y) == null &&
				grid.getVisibleTerrain(n.x, n.y).getMoveCost(net.fe.unit.Class.createClass("Phantom")) < phantomMov
			)
		) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public OverworldContext onSelect(ClientOverworldStage stage, OverworldContext context, Zone z, Unit unit) {
		return new net.fe.overworldStage.context.Summon(stage, context, z, unit);
	}
	
	@Override
	public Zone getZone(Unit unit, Grid grid) {
		return new RangeIndicator(grid.getRange(
					new Node(unit.getXCoord(), unit.getYCoord()), 1),
					RangeType.MOVE_DARK);
	}
	
	
	@Override
	public int hashCode() { return (int) serialVersionUID; }
	@Override
	public boolean equals(Object other) { return other instanceof Summon; }
}
