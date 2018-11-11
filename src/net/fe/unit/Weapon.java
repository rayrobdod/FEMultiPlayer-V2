package net.fe.unit;

import java.util.*;
import java.util.function.Function;

import net.fe.fightStage.Brave;
import net.fe.fightStage.CombatTrigger;
import net.fe.fightStage.EclipseSix;
import net.fe.fightStage.Effective;
import net.fe.fightStage.LunaPlus;
import net.fe.fightStage.Nosferatu;
import net.fe.fightStage.CrossBow;

// TODO: Auto-generated Javadoc
/**
 * The Class Weapon.
 */
public final class Weapon extends Item {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6496663141806177211L;
	
	/** The modifiers. */
	public final Statistics modifiers;
	
	/** The crit. */
	public final int mt, hit, crit;
	
	/** The weapon's range. */
	public final Function<Statistics, List<Integer>> range;
	
	/** The type. */
	public final Type type;
	
	/** The effective. */
	private final List<String> effective;
	
	/** Whether to show this item in the shop */
	/* Probably to be replaced with ruleset framework changes,
	 * i.e. the stuff that allows the one legendary weapon per team rule
	 */
	public final boolean showInShop;

	
	/**
	 * Instantiates a new weapon.
	 *
	 * @param name the weapon's name
	 * @param maxUses the durability of the weapon
	 * @param id icon number
	 * @param cost price in shop
	 * @param type the type of the weapon
	 * @param range the weapon's range. If this weapon is expected to travel over a network, this
	 * 		should be serializable and have a stable hashCode. Note that lambdas don't generally
	 * 		satisfy this property
	 */
	public Weapon(String name, int maxUses, int id, int cost,
			Type type, int mt, int hit, int crit, 
			Function<Statistics, List<Integer>> range,
			Statistics modifiers,
			List<String> effective, boolean showInShop) {
		super(name, maxUses, id, cost);
		this.type = type;
		this.modifiers = modifiers;
		this.mt = mt;
		this.hit = hit;
		this.crit = crit;
		this.effective = java.util.Collections.unmodifiableList(new ArrayList<String>(effective));
		this.range = range;
		this.showInShop = showInShop;
	}
	
	/**
	 * Represents a location in the weapon triangle.
	 * Red beats green beats blue beats red.
	 */
	private enum TrianglePosition {
		RED(1), BLUE(2), GREEN(3), NEUTRAL(0);
		
		/** A number used to reduce if statement count */
		private final int mark;
		private TrianglePosition(int mark) {
			this.mark = mark;
		}
		
		/**
		 * Checks the relative advantage between two TrianglePositions
		 * 
		 * @return 1 if this position has the advantage over the rhs,
		 		-1 if this position has the disadvantage over the rhs,
		 		or 0 if this is neutral compared to the rhs.
		 */
		public int modifier(TrianglePosition rhs) {
			if (this == NEUTRAL || rhs == NEUTRAL) {
				return 0;
			} else {
				return ((4 + this.mark - rhs.mark) % 3) - 1;
			}
		}
	}
	
	/**
	 * Weapon categories.
	 * 
	 * Determines who can wield a weapon, the triangle position of a
	 * weapon, and whether the weapon deals magical or physical damage
	 */
	public enum Type{
		/** Swords */
		SWORD(TrianglePosition.RED, false),
		/** Lances */
		LANCE(TrianglePosition.BLUE, false),
		/** Axes */
		AXE(TrianglePosition.GREEN, false),
		/** Bows */
		BOW(TrianglePosition.NEUTRAL, false),
		/** Crossbows */
		CROSSBOW(TrianglePosition.NEUTRAL, false),
		/** Light tomes */
		LIGHT(TrianglePosition.RED, true),
		/** Anima tomes */
		ANIMA(TrianglePosition.BLUE, true),
		/** Dark Tomes */
		DARK(TrianglePosition.GREEN, true),
		/** Staves */
		STAFF(TrianglePosition.NEUTRAL, false),
		
		SEALED_SWORD(TrianglePosition.RED, false),
		DURANDAL(TrianglePosition.RED, false),
		SOL_KATTI(TrianglePosition.RED, false),
		SIEGLINDE(TrianglePosition.RED, false),
		FALCHION(TrianglePosition.RED, false),
		RAGNELL(TrianglePosition.RED, false),
		SIEGMUND(TrianglePosition.BLUE, false),
		ARMADS(TrianglePosition.GREEN, false);
		
		private final TrianglePosition trianglePosition;
		private final boolean isMagic;
		private Type(TrianglePosition triPos, boolean isMagic) {
			this.trianglePosition = triPos;
			this.isMagic = isMagic;
		}
		
		/**
		 * Weapon triangle modifier
		 *
		 * @param other the other
		 * @return 1 if advantage, -1 if disadvantage
		 */
		public int triangleModifier(Type other){
			if (this.isMagic == other.isMagic) {
				return this.trianglePosition.modifier(other.trianglePosition);
			} else {
				return 0;
			}
		}
		
		/**
		 * Checks if is magic.
		 *
		 * @return true, if this weapon type deals damage based on Mag and Res.
		 		false, if this weapon type deals damage based on Str and Def.
		 */
		public boolean isMagic(){
			return this.isMagic;
		}
	}
	
	/**
	 * Weapon triangle modifier
	 *
	 * @param other the other weapon
	 * @return 1 if advantage, -1 if disadvantage
	 */
	public int triMod(Weapon other){ 
		if(other == null) return 0;
		if(this.name.contains("reaver") || other.name.contains("reaver")){
			if(this.name.contains("reaver") && other.name.contains("reaver")){
				return type.triangleModifier(other.type);
			}
			return -2*type.triangleModifier(other.type);
		}
		return type.triangleModifier(other.type);
	}
	
	/**
	 * Checks if is magic.
	 *
	 * @return true, if is magic
	 */
	public boolean isMagic(){
		return type.isMagic();
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
		} else if (type == Type.CROSSBOW) {
			triggers.add(new CrossBow());
		}
		if (this.effective.size() != 0) {
			final int multiplier = (name.equals("Sol Katti") ? 2 : 3);
			triggers.add(new Effective(multiplier, this.effective));
		}
		return triggers;
	}
	
	
	/* (non-Javadoc)
	 * @see net.fe.unit.Item#getCopy()
	 */
	public Weapon getCopy(){
		return new Weapon(name, getMaxUses(), id, getCost(),
				type, mt, hit, crit, range,
				modifiers, effective, showInShop);
	}
	
	/** Returns an item identical to this one, with the exception of an updated mt, hit and crit */
	public Weapon getCopyWithNewMtHitCrit(int newmt, int newhit, int newcrit){
		return new Weapon(name, getMaxUses(), id, getCost(),
				type, newmt, newhit, newcrit, range,
				modifiers, effective, showInShop);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Item that) {
		if(that instanceof Weapon){
			int first = this.type.compareTo(((Weapon) that).type);
			if(first != 0) return first;
			int second = this.getCost() - that.getCost();
			return second;
		} else {
			return -1;
		}
	}
	
	@Override
	public int hashCode() {
		return (((((super.hashCode() * 31 +
				this.type.ordinal()) * 31 +
				this.range.hashCode()) * 31 +
				this.modifiers.hashCode()) * 31 +
				java.util.Objects.hashCode(this.showInShop)) * 31 +
				this.effective.hashCode()) * 31 +
				(crit << 14 + hit << 7 + mt);
	}
	
	@Override
	protected boolean canEquals(Object other) {
		return other instanceof Weapon;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof Weapon) {
			Weapon o2 = (Weapon) other;
			if (o2.canEquals(this)) {
				return super.equals(o2) &&
					this.mt == o2.mt &&
					this.hit == o2.hit &&
					this.crit == o2.crit &&
					this.type == o2.type &&
					this.range.equals(o2.range) &&
					this.effective.equals(o2.effective) &&
					this.showInShop == o2.showInShop &&
					this.modifiers.equals(o2.modifiers);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "Weapon [" +
			"name: " + name + "; " +
			"maxUses: " + getMaxUses() + "; " +
			"uses: " + getUses() + "; " +
			"id: " + id + "; " +
			"cost: " + getCost() + "; " +
			"type: " + type + "; " +
			"mt: " + mt + "; " +
			"hit: " + hit + "; " +
			"range: " + range + "; " +
			"modifiers: " + modifiers + "; " +
			"effective: " + effective + "; " +
			"showInShop: " + showInShop + "]";
	}
}
