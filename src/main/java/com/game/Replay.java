package com.game;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.game.characters.DwarfWarrior;
import com.game.characters.ElfScout;
import com.game.characters.MagicMan;
import com.game.characters.Player;

public class Replay {
	
	private Player firstPlayer;
	private Player secondPlayer;
	private Turn currentTurn;
	
	public void replay() {
		Session session = initSession();
		session.beginTransaction();
		
		int turn = 1;
		
		currentTurn = session.get(Turn.class, turn);
		
		if (currentTurn == null) {
			System.out.println("Нет повторов");
			return;
		}
		
		firstPlayer = currentTurn.getFirstPlayer();
		secondPlayer = currentTurn.getSecondPlayer();
		
		while (currentTurn != null) {
			
			System.out.println("\n\nХОД " + turn++ + ":\n");
			System.out.println(firstPlayer + "\n" + secondPlayer);
			
			System.out.println("\nХОД " + firstPlayer.getName() + ":");
			showAction(firstPlayer, secondPlayer, 
					currentTurn.getFirstPlayerAction());
			
			if (currentTurn.getSecondPlayerAction() == 0)
				break;
			
			System.out.println("\nХОД " + secondPlayer.getName());
			showAction(secondPlayer, firstPlayer, 
					currentTurn.getSecondPlayerAction());
			
			currentTurn = session.get(Turn.class, turn);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		session.getTransaction().commit();
		session.close();
		
		if (firstPlayer.getLevel() > secondPlayer.getLevel())
			System.out.println("\n" + firstPlayer.getName() + " - ПОБЕДИТЕЛЬ!!!");
		else
			System.out.println("\n" + secondPlayer.getName() + " - ПОБЕДИТЕЛЬ!!!");
		
		System.out.println(firstPlayer + "\n" + secondPlayer);
	}
	
	private void showAction(Player player1, Player player2, int choice) {
		
		switch (choice) {
		case 1:
			System.out.println("Выбран: отдых");
			player1.rest();
			return;
		case 2:
			System.out.println("Выбран: спуск");
		    player1.move(player2);
			return;
		case 3:
			System.out.println("Выбран: быстрый спуск");
			player1.fastMove(player2);
			return;
		case 4:
			System.out.println("Выбран: особое действие");
			player1.specialAction(player2);
			return;
		case 0:
			return;
		}
	}
	
	private Session initSession() {
		SessionFactory factory = new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(Turn.class)
				.addAnnotatedClass(MagicMan.class)
				.addAnnotatedClass(DwarfWarrior.class)
				.addAnnotatedClass(ElfScout.class)
				.buildSessionFactory();
		
		return factory.getCurrentSession();
	}
}
