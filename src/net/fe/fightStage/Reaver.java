package net.fe.fightStage;

import net.fe.rng.RNG;
import net.fe.unit.Unit;

/**
 * The weapon triangle reversing Combat Trigger
 */
public final class Reaver extends CombatTrigger{
	private static final long serialVersionUID = 1L;

	public Reaver(){
		super(NO_NAME_MOD, 0);
	}

	@Override
	public boolean attempt(Unit user, int range, Unit opponent, RNG rng) {
		return true;
	}
	
	public boolean isReaver() {
		return true;
	}
	
	public CombatTrigger getCopy(){
		return new Reaver();
	}
}
