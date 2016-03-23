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
							unit.get("Mov")) {
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
		stage.addCmd("SUMMON", getCurrentTarget().x, getCurrentTarget().y);
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
		
		HashMap<String, Integer> bases = new HashMap<String, Integer>();
		HashMap<String, Integer> growths = new HashMap<String, Integer>();
		bases.put("HP", 1);
		bases.put("Str", 5);
		bases.put("Def", 0);
		bases.put("Mag", 0);
		bases.put("Res", 0);
		bases.put("Lck", 0);
		bases.put("Skl", 2);
		bases.put("Spd", 4);
		bases.put("Mov", 5);
		growths.put("HP", 0);
		growths.put("Str", 55);
		growths.put("Def", 15);
		growths.put("Mag", 15);
		growths.put("Res", 15);
		growths.put("Lck", 50);
		growths.put("Skl", 35);
		growths.put("Spd", 45);
		growths.put("Mov", 0);
		summonCount = summonCount + 1;
		final Unit summon = new Unit("Phantom " + summonCount, net.fe.unit.Class.createClass("Phantom"), '-', bases, growths);
		summon.addToInventory(net.fe.unit.Item.getItem("Iron Axe"));
		summon.initializeEquipment();
		summon.setLevel(summoner.get("Lvl"));
		summon.fillHp();
		summon.setMoved(true);
		
		summoner.getParty().addUnit(summon);
		summon.stage = summoner.stage;
		return summon;
	}
}
