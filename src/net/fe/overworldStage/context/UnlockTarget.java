package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.List;

import chu.engine.anim.AudioPlayer;
import net.fe.overworldStage.*;
import net.fe.unit.Unit;

/**
 * A OverworldContext used to select a door to unlock
 */
public final class UnlockTarget extends SelectNodeContext {
	
	/**
	 * Instantiates a new drop target.
	 *
	 * @param stage the stage
	 * @param context the context
	 * @param z the z
	 * @param u the u
	 */
	public UnlockTarget(ClientOverworldStage stage,
			OverworldContext context,
			Zone z, Unit u) {
		super(stage, context, z, u);

	}


	/**
	 * Find targets.
	 *
	 * @param unit the unit
	 */
	protected List<Node> findTargets(Unit unit, Zone zone) {
		List<Node> targets = new ArrayList<Node>();
		for (Node n : zone.getNodes()) {
			Unit u = grid.getUnit(n.x, n.y);
			if (grid.getTerrain(n.x, n.y) == Terrain.DOOR) {
				targets.add(n);
			}
		}
		return targets;
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onSelect()
	 */
	@Override
	public void onSelect() {
		AudioPlayer.playAudio("select");
		stage.addCmd("UNLOCK", getCurrentTarget().x, getCurrentTarget().y);
		stage.send();
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.reset();
	}

}
