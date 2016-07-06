package net.fe.unit;

import net.fe.overworldStage.FieldSkill;
import net.fe.overworldStage.fieldskill.Summon;
import net.fe.overworldStage.fieldskill.Unlock;

/**
 * An item that, when in the unit's inventory, grants a FieldSkill
 */
public final class FieldSkillItem extends Item {
	
	public static final FieldSkillItem RISE_TOME = new FieldSkillItem("Rise", 1, 3, 5000, new Summon());
	public static final FieldSkillItem DOOR_KEY = new FieldSkillItem("Door Key", 1, 100, 400, new Unlock());
	
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2L;
	
	public final FieldSkill skill;
	
	public FieldSkillItem(String name, int maxUses, int id, int cost, FieldSkill skill){
		super(name, maxUses, id, cost);
		this.skill = skill;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.unit.Item#getCopy()
	 */
	public FieldSkillItem getCopy(){
		return new FieldSkillItem(this.name, this.getMaxUses(), this.id, this.getCost(), this.skill);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Item that) {
		if (that instanceof FieldSkillItem) {
			return this.name.compareTo(that.name);
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
