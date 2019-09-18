package com.game.characters;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MM")
public class MagicMan extends Player {
	
	public MagicMan() {
		super("Magic-man", 0, 30, 30, 2, 5, 13, 15);
		setSpecialActionDescription("Если на уровне ниже есть персонаж, "
				+ "то меняется с ним местами иначе спускается на один уровень.");
	}
	
	@Override
	public boolean specialAction(Player anotherPlayer) {
		
		if (!checkEndurance(getSpecActionCost()))
			return false;
		
		if (checkSpecAction(anotherPlayer) == null) {
			int level = getLevel();
			setLevel(anotherPlayer.getLevel());
			anotherPlayer.setLevel(level);
		}
		
		setSpecAtionUsed(true);
		setEndurance(getEndurance() - getSpecActionCost() + getRecovery());
		return true;
	}
}
