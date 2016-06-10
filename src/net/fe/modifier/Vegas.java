package net.fe.modifier;

import java.util.stream.Stream;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderResources;
import net.fe.unit.Item;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * Everyone's hit rate is halved and crit rate doubled
 */
public final class Vegas implements Modifier {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3407505862142624494L;

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#modifyTeam(net.fe.builderStage.TeamBuilderStage)
	 */
	@Override
	public TeamBuilderResources modifyTeamResources(TeamBuilderResources limits) {
		return limits;
	}
	
	@Override
	public Stream<Unit> modifyUnits(Stream<Unit> units) {
		return units.peek(u -> u.addSkill(new Gamble()));
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
		return "Gamble! All units have halved hit rates and doubled crit rates.";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Vegas";
	}
	

}
