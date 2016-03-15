package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import chu.engine.anim.AudioPlayer;
import net.fe.FEResources;
import net.fe.overworldStage.*;
import net.fe.unit.Unit;
import net.fe.unit.WeaponFactory;

/**
 * A category of overworld context which selects a node 
 */
public abstract class SelectNodeContext extends OverworldContext {
	
	/** The zone. */
	private Zone zone;
	
	/** The targets. */
	private List<Node> targets;
	
	/** The selected. */
	protected int selected;
	
	/** The unit. */
	protected Unit unit;

	/**
	 * Instantiates a new summon context
	 *
	 * @param stage the stage
	 * @param context the context
	 * @param z the z
	 * @param unit the unit
	 */
	public SelectNodeContext(ClientOverworldStage stage, OverworldContext context, Zone z, Unit unit) {
		super(stage, context);
		zone = z;
		targets = new ArrayList<Node>();
		this.unit = unit;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#startContext()
	 */
	public void startContext() {
		super.startContext();
		targets.addAll(this.findTargets(unit, zone));
		stage.addEntity(zone);
		updateCursor();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#cleanUp()
	 */
	public void cleanUp() {
		super.cleanUp();
		stage.removeEntity(zone);
	}

	/**
	 * Find targets.
	 *
	 * @param unit the unit
	 */
	protected abstract List<Node> findTargets(Unit unit, Zone zone);

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onSelect()
	 */
	@Override
	public abstract void onSelect();

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onUp()
	 */
	public void onUp() {
		prevTarget();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onDown()
	 */
	public void onDown() {
		nextTarget();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onLeft()
	 */
	public void onLeft() {
		prevTarget();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onRight()
	 */
	public void onRight() {
		nextTarget();
	}

	/**
	 * Prev target.
	 */
	public void prevTarget() {
		selected--;
		if (selected < 0) {
			selected += targets.size();
		}
		updateCursor();
	}

	/**
	 * Next target.
	 */
	public void nextTarget() {
		selected++;
		selected %= targets.size();
		updateCursor();
	}

	/**
	 * Gets the current target.
	 *
	 * @return the current target
	 */
	public Node getCurrentTarget() {
		return targets.get(selected);
	}

	/**
	 * Update cursor.
	 */
	public void updateCursor() {
		AudioPlayer.playAudio("cursor");
		cursor.setXCoord(targets.get(selected).x);
		cursor.setYCoord(targets.get(selected).y);
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onCancel()
	 */
	@Override
	public void onCancel() {
		super.onCancel();
		// Reset the position of the cursor on cancels
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
	}
	
}
