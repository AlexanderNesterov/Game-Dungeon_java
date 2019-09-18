package com.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ReaderWrapper {
	
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	public int getInt() {
		int number = -1;
		
		while (number == -1) {
			try {
				number = Integer.parseInt(reader.readLine());
			} catch(Exception e) {
				System.out.println("Не является целым числом!");
			}
		}
		
		return number;
	}
}
