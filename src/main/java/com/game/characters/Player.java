package com.game.characters;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="player")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="pl_type")
public abstract class Player {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="level")
	private int level;
	
	@Column(name="endurance")
	private int endurance;
	
	@Transient
	private int maxEndurance;
	
	@Transient
	private boolean isSpecAtionUsed = false;
	
	@Transient
	private int recovery = 2;
	
	@Transient
	private int moveCost;
	
	@Transient
	private int fastMoveCost;
	
	@Transient
	private int specActionCost;
	
	@Transient
	private String specialActionDescription;

	public Player(String name, int level, int endurance, int maxEndurance, int recovery, int moveCost, int fastMoveCost,
			int specActionCost) {
		this.name = name;
		this.level = level;
		this.endurance = endurance;
		this.maxEndurance = maxEndurance;
		this.recovery = recovery;
		this.moveCost = moveCost;
		this.fastMoveCost = fastMoveCost;
		this.specActionCost = specActionCost;
	}

	public abstract boolean specialAction(Player anotherPlayer);
	
	public void rest() {
		this.endurance += 5;
		
		if (endurance > maxEndurance)
			endurance = maxEndurance;
	}
	
	public boolean move(Player anotherPlayer) {
		
		if (!checkEndurance(moveCost)) 
			return false;
		
		int step = 1;
		
		if (checkSpecAction(anotherPlayer) == null)
			level = level + step;
		else {
			int levelDifference = anotherPlayer.getLevel() - level;
			checkStep(levelDifference, step, anotherPlayer.getLevel());
		}
		
		endurance = endurance - moveCost + recovery;
		return true;
	}
	
	public boolean fastMove(Player anotherPlayer) {
		
		if (!checkEndurance(fastMoveCost))
			return false;
		
		int step = 2;
		
		if (checkSpecAction(anotherPlayer) == null)
			level = level + step;
		else {
			int levelDifference = anotherPlayer.getLevel() - getLevel();
			checkStep(levelDifference, step, anotherPlayer.getLevel());
		}
		
		endurance = endurance - fastMoveCost + recovery;
		return true;
	}

	protected boolean checkEndurance(int enduranceCost) {
		if (endurance - enduranceCost >= 0)
			return true;
		else {
			System.out.println("НЕ ХВАТАЕТ ВЫНОСЛИВОСТИ");
			return false;
		}	
	}

	//FIX
	protected Player checkSpecAction(Player player) {
		return (player.isSpecAtionUsed()) ? player : null;
	}
	
	protected void checkStep(int levelDifference, int step, int anotherPlayerLevel) {
		if (levelDifference == 0)
			return;
		
		if (levelDifference < 0) {
			setLevel(getLevel() + step);
			return;
		}
	
		if (levelDifference > 0) {
			if (step - levelDifference == 0 || step - levelDifference > 0)
				setLevel(anotherPlayerLevel - 1);
			else
				level = level + step;
			
			return;
		}
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMaxEndurance() {
		return maxEndurance;
	}

	public void setMaxEndurance(int maxEndurance) {
		this.maxEndurance = maxEndurance;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getEndurance() {
		return endurance;
	}
	
	public void setEndurance(int endurance) {
		this.endurance = endurance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isSpecAtionUsed() {
		return isSpecAtionUsed;
	}

	public void setSpecAtionUsed(boolean isSpecAtionUsed) {
		this.isSpecAtionUsed = isSpecAtionUsed;
	}
	
	public int getRecovery() {
		return recovery;
	}

	public void setRecovery(int recovery) {
		this.recovery = recovery;
	}
	
	public int getMoveCost() {
		return moveCost;
	}

	public void setMoveCost(int moveCost) {
		this.moveCost = moveCost;
	}

	public int getFastMoveCost() {
		return fastMoveCost;
	}

	public void setFastMoveCost(int fastMoveCost) {
		this.fastMoveCost = fastMoveCost;
	}

	public int getSpecActionCost() {
		return specActionCost;
	}

	public void setSpecActionCost(int specActionCost) {
		this.specActionCost = specActionCost;
	}

	public String getSpecialActionDescription() {
		return specialActionDescription;
	}

	public void setSpecialActionDescription(String specialActionDescription) {
		this.specialActionDescription = specialActionDescription;
	}

	@Override
	public String toString() {
		return "Player " + name + 
				", уровень=" + level + 
				", выносливость=" + endurance;
	}
}
