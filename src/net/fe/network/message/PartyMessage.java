package net.fe.network.message;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.function.Function;

import net.fe.builderStage.TeamBuilderResources;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.network.Message;
import net.fe.modifier.Modifier;
import net.fe.unit.Unit;
import net.fe.unit.Item;

// TODO: Auto-generated Javadoc
/**
 * The Class PartyMessage.
 */
public final class PartyMessage extends Message {

	/** The team data. */
	public final List<Unit> teamData;
	
	/**
	 * Instantiates a new party message.
	 *
	 * @param data the data
	 */
	public PartyMessage(List<Unit> data) {
		super();
		teamData = data;
	}
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6657648098112938492L;
	
	/* (non-Javadoc)
	 * @see net.fe.network.Message#toString()
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		for(Unit u: teamData){
			sb.append("\n\t");
			sb.append(u.name);
			sb.append(" Lv" + u.getLevel());
			for (Item i: u.getInventory()) {
				sb.append("\n\t\t");
				sb.append(i.name);
			}
		}
		sb.append("\n");
		return sb.toString();
	}
	
	/**
	 * Compares the units contained in this message with the parameters to detect anomalies
	 * 
	 * @return None if the team is acceptable; a Some with a message otherwise
	 */
	public Optional<String> validateTeam(Stream<Unit> realUnits, Stream<Item> realItems, Iterable<Modifier> mods) {
		
		TeamBuilderResources usableRes = new TeamBuilderResources(TeamBuilderStage.FUNDS, TeamBuilderStage.EXP);
		for (Modifier m : mods) {
			usableRes = m.modifyTeamResources(usableRes);
			realItems = m.modifyShop(realItems);
			realUnits = m.modifyUnits(realUnits);
		}
		List<Item> realItems2 = realItems.collect(java.util.stream.Collectors.toList());
		Map<String, Unit> realUnits2 = realUnits.collect(java.util.stream.Collectors.toMap(u -> u.name, java.util.function.Function.identity()));
		
		
		TeamBuilderResources usedRes = new TeamBuilderResources(0, 0);
		for (Unit u : this.teamData) {
			{
				Unit u2 = u.getCopy();
				usedRes = usedRes.copyWithNewFunds((i) -> i + u2.squeezeGold());
				usedRes = usedRes.copyWithNewExp((i) -> i + u2.squeezeExp());
			}
			{
				Unit u3 = realUnits2.get(u.name);
				u3.setLevel(u.getLevel());
				if (! u.getBase().equals(u3.getBase())) {
					return Optional.of("unit's stats don't match expected: " +
						"\n\tClient: " + u.getBase() +
						"\n\tServer: " + u3.getBase()
					);
				}
				if (! u.getTheClass().equals(u3.getTheClass())) {
					return Optional.of("unit's class don't match expected" +
						"\n\tClient: " + u.getTheClass() +
						"\n\tServer: " + u3.getTheClass()
					);
				}
				if (! u.getTriggers().equals(u3.getTriggers())) {
					return Optional.of("unit's skills don't match expected" +
						"\n\tClient: " + u.getTriggers() +
						"\n\tServer: " + u3.getTriggers()
					);
				}
			}
			
			for (Item i : u.getInventory()) {
				if (! realItems2.contains(i)) {
					return Optional.of("Item does not exist in list of real items: " + i);
				}
			}
		}
		
		if (usedRes.exp > usableRes.exp) {
			return Optional.of("Used EXP exceeded usable EXP: " + usedRes.exp + " > " + usableRes.exp);
		}
		if (usedRes.funds > usableRes.funds) {
			return Optional.of("Used FUNDS exceeded usable FUNDS: " + usedRes.funds + " > " + usableRes.funds);
		}
		
		return Optional.empty();
	}
	
}
