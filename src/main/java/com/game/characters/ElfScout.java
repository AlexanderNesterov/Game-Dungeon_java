package com.game.characters;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ES")
public class ElfScout extends Player {
	
	public ElfScout() {
		super("Elf-scout", 0, 40, 40, 2, 5, 12, 24);
		setSpecialActionDescription("Спускается на 3 уровня ниже");
	}
	
	@Override
	public boolean specialAction(Player anotherPlayer) {
		
		if (!checkEndurance(getSpecActionCost()))
			return false;
		
		int step = 3;
		
		if (checkSpecAction(anotherPlayer) == null)
			setLevel(getLevel() + step);
		else {
			int levelDifference = anotherPlayer.getLevel() - getLevel();
			checkStep(levelDifference, step, anotherPlayer.getLevel());
		}
		
		setSpecAtionUsed(true);
		setEndurance(getEndurance() - getSpecActionCost() + getRecovery());
		return true;
	}
}
