package com.game;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.game.characters.DwarfWarrior;
import com.game.characters.ElfScout;
import com.game.characters.MagicMan;
import com.game.characters.Player;
import com.main.ReaderWrapper;

public class Game {
	
	private Player firstPlayer;
	private Player secondPlayer;
	private ArrayList<Player> heroes;
	private Turn currentTurn = new Turn();
	private int level;
	
	public Game() {
		level = 20;
	}
	
	public void startGame() {
		heroes = new ArrayList<>(3);
		
		heroes.add(new MagicMan());
		heroes.add(new DwarfWarrior());
		heroes.add(new ElfScout());
		
		firstPlayer = selectHero();
		secondPlayer = selectHero();
		
		Player winner;
		
		if ((1 +(int) (Math.random() * 2)) % 2 == 0) {
			currentTurn.swap();
			winner = game(secondPlayer, firstPlayer);
		}
		else
			winner = game(firstPlayer, secondPlayer);
		
		System.out.println(winner.getName() + " - ПОБЕДИТЕЛЬ!!!");
	}
	
	private Player selectHero() {
				
		int choice;

		do {
			System.out.println("Выберете героя: \n1:Человек-маг"
					+ "\n2:Гном-воин\n3:Эльф-разведчик");
			
			choice = new ReaderWrapper().getInt();

			if (choice <= 0 || choice > heroes.size()) {
				System.out.println("НЕВЕРНОЕ ЧИСЛО");
				continue;
			}
			
			if (heroes.get(choice - 1) != null) {
				switch (choice) {
				case 1:
					heroes.set(0, null);
					MagicMan mm = new MagicMan();
					currentTurn.addPlayer(mm);
					return mm;
				case 2:
					heroes.set(1, null);
					DwarfWarrior dw = new DwarfWarrior();
					currentTurn.addPlayer(dw);
					return dw;
				case 3:
					heroes.set(2, null);
					ElfScout es = new ElfScout();
					currentTurn.addPlayer(es);
					return es;
				}
			}
			
			System.out.println("Герой занят");

		} while (true);
	}
	
	private Player game(Player firstPlayer, Player secondPlayer) {
		
		int turn = 1;
		
		Session session = initSession();
		session.beginTransaction();
		
		System.out.println("\n\n\nИГРА НАЧАЛАСЬ");
		
		while (firstPlayer.getLevel() < level && secondPlayer.getLevel() < level) {
			
			System.out.println("\n\nХОД " + turn + ":\n");
			System.out.println(firstPlayer + "\n" + secondPlayer);
			
			System.out.println("\nХОД " + firstPlayer.getName() + ":");
			currentTurn.setFirstPlayerAction(selectAction(firstPlayer, secondPlayer));
			
			session.save(currentTurn);
			
			if (firstPlayer.getLevel() >= level)
				break;
			
			System.out.println("\nХОД " + secondPlayer.getName());
			currentTurn.setSecondPlayerAction(selectAction(secondPlayer, firstPlayer));
			
			session.persist(currentTurn);
			
			firstPlayer.setSpecAtionUsed(false);
			secondPlayer.setSpecAtionUsed(false);
			
			turn++;
			
			currentTurn = new Turn();
		}
		
		System.out.println(firstPlayer + "\n" + secondPlayer);
		session.getTransaction().commit();
		session.close();
		
		return (firstPlayer.getLevel() >= level) ? firstPlayer : secondPlayer;
	}
	
	private int selectAction(Player firstPlayer, Player secondPlayer) {
		int choice = -1;
		
		System.out.println("Выберете действие: \n1:Отдых\n2:Спуск"
				+ "\n3:Быстрый спуск\n4:Особое действие\n5:инфо");
		
		do {
			choice = new ReaderWrapper().getInt();
			
			switch (choice) {
			case 1:
				System.out.println("Выбран: отдых");
				firstPlayer.rest();
				return choice;
			case 2:
				System.out.println("Выбран: спуск");
				if (firstPlayer.move(secondPlayer))
					return choice;
				else
					break;
			case 3:
				System.out.println("Выбран: быстрый спуск");
				if (firstPlayer.fastMove(secondPlayer))
					return choice;
				else
					break;
			case 4:
				System.out.println("Выбран: особое действие");
				if (firstPlayer.specialAction(secondPlayer))
					return choice;
				else
					break;
			case 5:
				moveDescription(firstPlayer);
				break;
				
			default:
					System.out.println("НЕВЕРНОЕ ЧИСЛО");
			}
		} while (true);	
	}
	
	private void moveDescription(Player player) {
		System.out.println("Отдых: Восстанавливает дополнительно 3 ед. энергии Всего получаем 5 единиц.");		
		System.out.println("Спуск: Перемещает персонажа на этаж ниже. Стоимость: " + player.getMoveCost());
		System.out.println("Быстрый спуск: Перемещает персонажа на 2 этажа ниже." + player.getFastMoveCost());
		System.out.println("Особое действие: " + player.getSpecialActionDescription() + " Стоимость: " + player.getSpecActionCost());
	}
	
	private Session initSession() {
		SessionFactory factory = new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(Turn.class)
				.addAnnotatedClass(MagicMan.class)
				.addAnnotatedClass(DwarfWarrior.class)
				.addAnnotatedClass(ElfScout.class)
				.buildSessionFactory();
		
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		
		session.createQuery("DELETE FROM Turn").executeUpdate();
		session.createSQLQuery("ALTER TABLE turn AUTO_INCREMENT=1").executeUpdate();
		session.createSQLQuery("ALTER TABLE player AUTO_INCREMENT=1").executeUpdate();
		session.getTransaction().commit();
		
		return factory.getCurrentSession();
	}
}
