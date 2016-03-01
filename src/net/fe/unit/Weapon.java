package net.fe.unit;

import java.util.*;

import net.fe.fightStage.Brave;
import net.fe.fightStage.CombatTrigger;
import net.fe.fightStage.EclipseSix;
import net.fe.fightStage.LunaPlus;
import net.fe.fightStage.Nosferatu;

// TODO: Auto-generated Javadoc
/**
 * A class representing a weapon
 */
public class Weapon extends Item {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6496663141806177211L;
	
	/** Stat modifiers while the weapon is equipped */
	public HashMap<String, Integer> modifiers;
	
	/** The weapon's might */
	public int mt;
	/** The weapon's innate hit rate */
	public int hit;
	/** The weapon's innate crit rate */
	public int crit;
	
	/** A Map indicating the weapon's type at any particular range */
	public Map<Integer, Type> distanceType;
	
	/** A list of classes that this is effective against */
	public ArrayList<String> effective;
	
	/**
	 * The weapon's preference; null if any unit with the correct weapon levels can use
	 * otherwise the name of the class that is allowed to use it.
	 */
	public String pref; 

	
	/**
	 * Instantiates a new weapon.
	 *
	 * @param name the name
	 */
	public Weapon(String name) {
		super(name);
		// Initialize modifiers to 0
		modifiers = new HashMap<String, Integer>();
		modifiers.put("Skl", 0);
		modifiers.put("Lck", 0);
		modifiers.put("HP",  0);
		modifiers.put("Str", 0);
		modifiers.put("Mag", 0);
		modifiers.put("Def", 0);
		modifiers.put("Res", 0);
		modifiers.put("Spd", 0);
		modifiers.put("Lvl", 0);
		modifiers.put("Mov", 0);
		mt = 0;
		hit = 0;
		crit = 0;
		effective = new ArrayList<String>();
	}
	
	/**
	 * The Enum Type.
	 */
	public enum Type{
		
		/** The sword. */
		SWORD, 
		/** The lance. */
		LANCE, 
		/** The axe. */
		AXE, 
		/** The bow. */
		BOW, 
		/** The light. */
		LIGHT, 
		/** The anima. */
		ANIMA, 
		/** The dark. */
		DARK, 
		/** The staff. */
		STAFF;
		
		/**
		 * Triangle modifier.
		 *
		 * @param other the other
		 * @return the int
		 */
		public int triangleModifier(Type other){
			switch(this){
			case SWORD:
				if(other == LANCE) return -1;
				if(other == AXE) return 1;
				return 0;
			case LANCE:
				if(other == AXE) return -1;
				if(other == SWORD) return 1;
				return 0;
			case AXE:
				if(other == SWORD) return -1;
				if(other == LANCE) return 1;
				return 0;
				
			case LIGHT:
				if(other == ANIMA) return -1;
				if(other == DARK) return 1;
				return 0;
			case ANIMA:
				if(other == DARK) return -1;
				if(other == LIGHT) return 1;
				return 0;
			case DARK:
				if(other == LIGHT) return -1;
				if(other == ANIMA) return 1;
				return 0;
			default:
				return 0;
			}
		}
		
		/**
		 * Checks if is magic.
		 *
		 * @return true, if is magic
		 */
		public boolean isMagic(){
			return this == ANIMA || this == LIGHT || this == DARK;
		}
	}
	
	/**
	 * Triangle modifier
	 * @param other the other
	 * @return the modifier
	 */
	//Returns 1 if advantage, -1 if disadvantage
	public int triMod(Weapon other, int distance){ 
		if(other == null) return 0;
		if(this.name.contains("reaver") || other.name.contains("reaver")){
			if(this.name.contains("reaver") && other.name.contains("reaver")){
				return this.distanceType.get(distance).triangleModifier(other.distanceType.get(distance));
			}
			return -2* this.distanceType.get(distance).triangleModifier(other.distanceType.get(distance));
		}
		return this.distanceType.get(distance).triangleModifier(other.distanceType.get(distance));
	}
	
	/**
	 * Checks if is magic.
	 *
	 * @return true, if is magic
	 */
	public boolean isMagic(int range){
		if (distanceType.keySet().contains(range)) {
			return distanceType.get(range).isMagic();
		} else {
			return this.getPrimaryType().isMagic();
		}
	}
	
	/**
	 * Gets the triggers.
	 *
	 * @return the triggers
	 */
	public List<CombatTrigger> getTriggers(){
		ArrayList<CombatTrigger> triggers = new ArrayList<CombatTrigger>();
		if(name.contains("Brave")){
			triggers.add(new Brave());
		} else if (name.equals("Nosferatu")){
			triggers.add(new Nosferatu());
		} else if (name.equals("Lunase")){
			triggers.add(new LunaPlus());
		} else if (name.equals("Eclipse")){
			triggers.add(new EclipseSix());
		}
		return triggers;
	}
	
	/** Returns the weapon's primary type */
	public Type getPrimaryType() {
		return distanceType.get(Collections.min(distanceType.keySet()));
	}
	
	/**
	 * Returns a list integers, where each integer is a distance that this weapon can reachs
	 */
	public List<Integer> getRange() {
		return new ArrayList<Integer>(distanceType.keySet());
	}

	/* (non-Javadoc)
	 * @see net.fe.unit.Item#getCopy()
	 */
	public Weapon getCopy(){
		Weapon w = new Weapon(name);
		w.distanceType = new TreeMap<Integer, Type>(distanceType);
		w.mt = mt;
		w.hit = hit;
		w.crit = crit;
		w.setMaxUses(getMaxUses());
		w.setCost(getCost());
		w.effective = new ArrayList<String>(effective);
		w.pref = pref;
		w.modifiers = new HashMap<String, Integer>(modifiers);
		w.id = id;
		return w;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Item that) {
		if(that instanceof Weapon){
			int first = this.getPrimaryType().compareTo(((Weapon) that).getPrimaryType());
			if(first != 0) return first;
			int second = this.getCost() - that.getCost();
			return second;
		} else {
			return -1;
		}
	}
}
