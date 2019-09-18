package com.main;

import com.game.Game;
import com.game.Replay;

public class Main {
	public static void main(String[] args) {
		
		System.out.println("Выберете: \n1:Начать новую игру\n2:Запустить повтор");
		int choice;
		
		while (true) {
			choice = new ReaderWrapper().getInt();
			
			switch(choice) {
			case 1:
				new Game().startGame();
				return;
			case 2:
				new Replay().replay();
				return;
				
			default:
					System.out.println("Неверное число");
			}
		}
	}
}
