package com.game.characters;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DW")
public class DwarfWarrior extends Player {

	public DwarfWarrior() {
		super("Dwarf-warrior", 0, 50, 50, 2, 5, 15, 20);
		setSpecialActionDescription("Перемещает персонажа на уровень ниже. "
				+ "До следующего хода никакой персонаж не может ни обогнать его, "
				+ "ни оказаться на одном этаже с ним");
	}
	
	@Override
	public boolean specialAction(Player anotherPlayer) {
		if (!checkEndurance(getSpecActionCost()))
			return false;
		
		int step = 1;
		
		setLevel(getLevel() + step);
		setSpecAtionUsed(true);
		
		setEndurance(getEndurance() - getSpecActionCost() + getRecovery());
		return true;
	}
}
