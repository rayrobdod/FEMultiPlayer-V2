package net.fe.modifier;

import java.util.stream.Stream;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderResources;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;
import net.fe.unit.Item;

// TODO: Auto-generated Javadoc
/**
 * All units have Miracle, with 100% chance to proc.
 *
 * @author Shawn
 */
public class DivineIntervention implements Modifier {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7509901063099817137L;

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#modifyTeam(net.fe.builderStage.TeamBuilderStage)
	 */
	@Override
	public TeamBuilderResources modifyTeamResources(TeamBuilderResources limits) {
		return limits;
	}
	
	@Override
	public Stream<Unit> modifyUnits(Stream<Unit> units) {
		return units.peek(u -> u.addSkill(new Miracle()));
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Divine Intervention";
	}
	
	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#getDescription()
	 */
	@Override
	public String getDescription() {
		return "All units have a version of Miracle that is guarenteed to activate.";
	}
}
