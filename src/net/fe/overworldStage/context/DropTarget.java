package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.List;

import chu.engine.anim.AudioPlayer;
import net.fe.overworldStage.*;
import net.fe.unit.Unit;
import net.fe.network.command.DropCommand;

/**
 * A OverworldContext used to select a space to create a summon in
 */
public final class DropTarget extends SelectNodeContext {
	
	/**
	 * Instantiates a new drop target.
	 *
	 * @param stage the stage
	 * @param context the context
	 * @param z the z
	 * @param u the u
	 */
	public DropTarget(ClientOverworldStage stage, OverworldContext context,
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
			if (u == null
					&& grid.getTerrain(n.x, n.y).getMoveCost(
							unit.rescuedUnit().getTheClass()) < unit
							.rescuedUnit().getStats().mov) {
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
		DropCommand c = new DropCommand(getCurrentTarget().x, getCurrentTarget().y);
		c.applyClient(stage, unit, null, new EmptyRunnable()).run();
		stage.addCmd(c);
		stage.send();
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.reset();
	}

	private static final class EmptyRunnable implements Runnable {
		@Override public void run() {}
	}
}
