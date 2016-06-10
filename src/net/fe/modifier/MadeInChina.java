package net.fe.modifier;

import java.util.stream.Stream;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderResources;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Item;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;

// TODO: Auto-generated Javadoc
/**
 * All weapons have 2 durability. Players are given
 * extra gold to compensate.
 * @author Shawn
 *
 */
public class MadeInChina implements Modifier {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3929819526675171008L;

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#modifyTeam(net.fe.builderStage.TeamBuilderStage)
	 */
	@Override
	public TeamBuilderResources modifyTeamResources(TeamBuilderResources limits) {
		return limits.copyWithNewFunds((i) -> i * 2);
	}
	
	@Override
	public Stream<Unit> modifyUnits(Stream<Unit> units) {
		return units.peek(MadeInChina::decreaseInventoryWeaponUses);
	}
	
	/** Modifies each weapon in `shop` to have a maximum of two uses
	 * @see net.fe.modifier.Modifier#modifyShop(net.fe.builderStage.ShopMenu)
	 */
	@Override
	public Stream<Item> modifyShop(Stream<Item> shop) {
		return shop.map(MadeInChina::decreaseUsesIfWeapon);
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
		return "Made In China";
	}
	
	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#getDescription()
	 */
	@Override
	public String getDescription() {
		return "All weapons have greatly reduced durability. Start with extra gold.";
	}
	
	private static Item decreaseUsesIfWeapon(Item i) {
		if (i instanceof Weapon) {
			Weapon w = (Weapon) i;
			return new Weapon(w.name, 2, w.id, w.getCost(),
				w.type, w.mt, w.hit, w.crit, w.range,
				w.modifiers, w.effective, w.pref);
		} else {
			return i;
		}
	}
	
	private static void decreaseInventoryWeaponUses(Unit u) {
		// new list to prevent concurrent modification
		final java.util.List<Item> items = new java.util.ArrayList<>(u.getInventory());
		for (Item i : items) {
			u.removeFromInventory(i);
			u.addToInventory(decreaseUsesIfWeapon(i));
		}
	}
}
