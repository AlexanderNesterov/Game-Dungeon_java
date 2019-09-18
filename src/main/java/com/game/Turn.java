package com.game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.game.characters.DwarfWarrior;
import com.game.characters.ElfScout;
import com.game.characters.MagicMan;
import com.game.characters.Player;

@Entity
@Table(name="turn")
public class Turn {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="player1_action")
	private int firstPlayerAction;
	
	@Column(name="player2_action")
	private int secondPlayerAction;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="turn_id")
	private List<Player> players;
	
	public Turn() { }

	public void swap() {
		Player temp = players.get(0);
		players.set(0, players.get(1));
		players.set(1, temp);
	}
	
	private Player definePlayer(int index) {
		if (players.get(index) instanceof MagicMan)
			return new MagicMan();
		
		if (players.get(index) instanceof DwarfWarrior)
			return new DwarfWarrior();
		
		if (players.get(index) instanceof ElfScout)
			return new ElfScout();
		
		return null;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getFirstPlayerAction() {
		return firstPlayerAction;
	}

	public void setFirstPlayerAction(int firstPlayerAction) {
		this.firstPlayerAction = firstPlayerAction;
	}

	public int getSecondPlayerAction() {
		return secondPlayerAction;
	}

	public void setSecondPlayerAction(int secondPlayerAction) {
		this.secondPlayerAction = secondPlayerAction;
	}
	
	public Player getFirstPlayer() {
		return definePlayer(0);
	}
	
	public Player getSecondPlayer() {
		return definePlayer(1);
	}
	
	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public void addPlayer(Player player) {
		if (players == null)
			players = new ArrayList<>(2);
		
		players.add(player);
	}

	@Override
	public String toString() {
		return "Turn [id=" + id + ", player1Action=" + firstPlayerAction 
				+ ", player2Action=" + secondPlayerAction + "]";
	}
}
