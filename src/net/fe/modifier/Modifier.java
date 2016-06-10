package net.fe.modifier;

import java.io.Serializable;
import java.util.stream.Stream;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderResources;
import net.fe.unit.Item;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Interface Modifier.
 */
public interface Modifier extends Serializable{
	
	/**
	 * Edit the resources availiable while building a team.
	 * 
	 * A default implementation is to return the parameter.
	 * 
	 * @param stage the initial starting resources
	 * @return the new starting resources
	 */
	public TeamBuilderResources modifyTeamResources(TeamBuilderResources limits);
	
	/**
	 * Transforms a list of units.
	 * 
	 * A default implementation is to return the parameter.
	 *
	 * @param units the units
	 * @return the modified list
	 */
	public Stream<Unit> modifyUnits(Stream<Unit> units);
	
	/**
	 * Transforms a list of items.
	 * 
	 * A default implementation is to return the parameter.
	 *
	 * @param shop the items
	 * @return the modified list
	 */
	public Stream<Item> modifyShop(Stream<Item> shop);
	
	/**
	 * Modifies a list of units; for use immediately before the game starts
	 *
	 * Modifies the list in-line
	 * 
	 * @param units the units
	 */
	public void initOverworldUnits(Iterable<Unit> units);
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription();
	
	/**
	 * Returns a modifier which has the effects of both parameters.
	 */
	public static Modifier combine(final Modifier lhs, final Modifier rhs) {
		System.out.println(lhs);
		System.out.println(rhs);
		
		return new Modifier() {
			private static final long serialVersionUID = 2L;
			
			@Override
			public TeamBuilderResources modifyTeamResources(TeamBuilderResources limits) {
				return rhs.modifyTeamResources(lhs.modifyTeamResources(limits));
			}
			
			@Override
			public Stream<Unit> modifyUnits(Stream<Unit> units) {
				return rhs.modifyUnits(lhs.modifyUnits(units));
			}
			
			@Override
			public Stream<Item> modifyShop(Stream<Item> shop) {
				return rhs.modifyShop(lhs.modifyShop(shop));
			}
			
			@Override
			public void initOverworldUnits(Iterable<Unit> units) {
				lhs.initOverworldUnits(units);
				rhs.initOverworldUnits(units);
			}
			
			@Override
			public String toString() {
				return lhs.toString() + " AND " + rhs.toString();
			}
			
			@Override
			public String getDescription() {
				return lhs.getDescription() + " AND " + rhs.getDescription();
			}
		};
	}
	
	/**
	 * Returns a modifier which has no effects.
	 */
	public static Modifier identity() {
		return new Modifier() {
			@Override public TeamBuilderResources modifyTeamResources(TeamBuilderResources limits) { return limits; }
			@Override public Stream<Unit> modifyUnits(Stream<Unit> units) { return units; }
			@Override public Stream<Item> modifyShop(Stream<Item> shop) { return shop; }
			@Override public void initOverworldUnits(Iterable<Unit> units) { }
			@Override public String toString() { return "IdentityModifier"; }
			@Override public String getDescription() { return ""; }
		};
	}

}
