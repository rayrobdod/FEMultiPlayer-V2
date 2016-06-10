package net.fe.modifier;

import java.util.stream.Stream;

import net.fe.modifier.Pavise;
import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderResources;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;
import net.fe.unit.Item;

public class ProTactics implements Modifier {

	@Override
	public TeamBuilderResources modifyTeamResources(TeamBuilderResources limits) {
		return limits;
	}
	
	@Override
	public Stream<Unit> modifyUnits(Stream<Unit> units) {
		return units.peek(u -> u.addSkill(new Pavise()));
	}

	@Override
	public Stream<Item> modifyShop(Stream<Item> shop) {
		return shop;
	}

	@Override
	public void initOverworldUnits(Iterable<Unit> units) {
	}

	@Override
	public String toString() {
		return "Pro Tactics";
	}

	@Override
	public String getDescription() {
		return "Halves damage to better emulate traditional GBA games. All units get 100%Pavise.";
	}

}
