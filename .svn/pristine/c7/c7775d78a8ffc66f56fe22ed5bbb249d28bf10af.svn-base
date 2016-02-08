package lab5;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import lab5.client.Client;
import lab5.model.SetGame;
import lab5.view.NetworkView;

/**
 * ServerMain - main class for only server portion
 * @author Danny Munro, Josh Sherman, Joey Woodson, Brittany Edwards
 * Class: CSE 132
 * Lab: 5
 *
 */

public class ServerMain {

	/**
	 * Main method
	 * @param args unused
	 */
	public static void main(String[] args){
		SetGame sg = new SetGame();
		try {
			new NetworkView(sg).run();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}