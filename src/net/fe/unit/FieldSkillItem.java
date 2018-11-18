package net.fe.unit;

import net.fe.overworldStage.fieldskill.Summon;
import net.fe.overworldStage.FieldSkill;

/**
 * An item that allows a unit to perform some non-standard action in the field
 */
public final class FieldSkillItem extends Item {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** An item that grants the Summon skill */
	public static final FieldSkillItem RISE_TOME = new FieldSkillItem("Rise", 1, 3, 5000, new Summon());
	
	/** The skill that this item grants */
	public final FieldSkill skill;
	
	private FieldSkillItem(String name, int uses, int id, int cost, FieldSkill skill) {
		super(name, uses, id, cost);
		this.skill = skill;
	}
	
	@Override
	public FieldSkillItem getCopy() {
		return new FieldSkillItem(this.name, this.getMaxUses(), this.id, this.getCost(), this.skill);
	}
	
	@Override
	public int compareTo(Item that) {
		if (that instanceof FieldSkillItem) {
			return this.id - ((FieldSkillItem) that).id;
		} else {
			return 1;
		}
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() * 31 + skill.hashCode();
	}
	
	@Override
	protected boolean canEquals(Object other) {
		return other instanceof FieldSkillItem;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof FieldSkillItem) {
			FieldSkillItem o2 = (FieldSkillItem) other;
			if (o2.canEquals(this)) {
				return super.equals(o2) && this.skill.equals(o2.skill);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
