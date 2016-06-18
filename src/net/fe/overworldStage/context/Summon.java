package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import chu.engine.anim.AudioPlayer;
import net.fe.FEResources;
import net.fe.overworldStage.*;
import net.fe.unit.Statistics;
import net.fe.unit.Unit;
import net.fe.unit.WeaponFactory;
import net.fe.network.command.SummonCommand;

/**
 * A OverworldContext used to select a space to create a summon in
 */
public final class Summon extends SelectNodeContext {
	
	/**
	 * Instantiates a new summon context
	 *
	 * @param stage the stage
	 * @param context the context
	 * @param z the z
	 * @param unit the unit
	 */
	public Summon(ClientOverworldStage stage, OverworldContext context, Zone z, Unit unit) {
		super(stage, context, z, unit);
	}

	/**
	 * Find targets.
	 *
	 * @param unit the unit
	 */
	@Override
	protected List<Node> findTargets(Unit unit, Zone zone) {
		ArrayList<Node> targets = new ArrayList<Node>();
		for (Node n : zone.getNodes()) {
			Unit u = grid.getUnit(n.x, n.y);
			if (u == null
					&& grid.getTerrain(n.x, n.y).getMoveCost(
							net.fe.unit.Class.createClass("Phantom")) <
							unit.getStats().mov) {
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
		SummonCommand c = new SummonCommand(getCurrentTarget().x, getCurrentTarget().y);
		stage.addCmd(c);
		c.applyClient(stage, unit, null, new EmptyRunnable()).run();
		stage.send();
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.reset();
	}
	
	/** The summon count. */
	private static int summonCount = 0;
	
	/**
	 * Generate summon.
	 *
	 * @param summoner the summoner
	 * @return the unit
	 */
	public static Unit generateSummon(Unit summoner) {
		WeaponFactory.loadWeapons();
		
		Statistics bases = new Statistics(
			/* hp = */ 1,
			/* str = */ 5,
			/* mag = */ 0,
			/* skl = */ 2,
			/* spd = */ 4,
			/* def = */ 0,
			/* res = */ 0,
			/* lck = */ 0,
			/* mov = */ 5,
			/* con = */ 11,
			/* aid = */ 10
		);
		Statistics growths = new Statistics(
			/* hp = */ 0,
			/* str = */ 55,
			/* mag = */ 15,
			/* skl = */ 35,
			/* spd = */ 45,
			/* def = */ 15,
			/* res = */ 15,
			/* lck = */ 50,
			/* mov = */ 0,
			/* con = */ 0,
			/* aid = */ 0
		);
		summonCount = summonCount + 1;
		final Unit summon = new Unit("Phantom " + summonCount, net.fe.unit.Class.createClass("Phantom"), '-', bases, growths);
		summon.addToInventory(net.fe.unit.Item.getItem("Iron Axe"));
		summon.initializeEquipment();
		summon.setLevel(summoner.getLevel());
		summon.fillHp();
		summon.setMoved(true);
		
		summoner.getParty().addUnit(summon);
		summon.stage = summoner.stage;
		return summon;
	}

	private static final class EmptyRunnable implements Runnable {
		@Override public void run() {}
	}
}
