package net.fe.modifier;

import java.util.stream.Stream;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderResources;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Statistics;
import net.fe.unit.HealingItem;
import net.fe.unit.Unit;
import net.fe.unit.Item;

// TODO: Auto-generated Javadoc
/**
 * All units start with 1 hp.
 *
 * @author Shawn
 */
public class SuddenDeath implements Modifier{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4684401842583775643L;

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#modifyTeam(net.fe.builderStage.TeamBuilderStage)
	 */
	@Override
	public TeamBuilderResources modifyTeamResources(TeamBuilderResources limits) {
		return limits;
	}
	
	@Override
	public Stream<Unit> modifyUnits(Stream<Unit> units) {
		return units.map(SuddenDeath::getCopyOfUnitWithOneHitpoint);
	}

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#modifyShop(net.fe.builderStage.ShopMenu)
	 */
	@Override
	public Stream<Item> modifyShop(Stream<Item> shop) {
		return shop;
	}

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#initOverworld(net.fe.overworldStage.OverworldStage)
	 */
	@Override
	public void initOverworldUnits(Iterable<Unit> units) {
	}

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#getDescription()
	 */
	@Override
	public String getDescription() {
		return "All units start at 1 HP.";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Sudden Death";
	}
	
	private static Unit getCopyOfUnitWithOneHitpoint(Unit u) {
		Statistics newBases = u.bases.copy("HP", 1);
		Statistics newGrowths = u.growths.copy("HP", 0);
		return u.getCopyWithNewStats(newBases, newGrowths);
	}
}
