//TA:MJ Grade:195/200

/**
 * Lab 5a
 * coverpage: 15/15
 * Sending and Receiving messages: 40/40
 * GUI: 30/35 - If you click single card three times, it considers it to check as a set. 
 * Handling erroneous messages: 10/10
 * 
 * Lab 5b
 * Multi-user: 40/40
 * Model: 30/30
 * Win Condition and game consistencies: 20/20 
 * Handling erroneous messages: 10/10
 * 
 *
 * 
 *
 * 
 * 
 */
package lab5;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import lab5.client.Client;
import lab5.model.SetGame;
import lab5.view.NetworkView;

/**
 * Main class for the program.  Use this when testing for a single client.
 * @author Danny Munro, Josh Sherman, Joey Woodson, Brittany Edwards
 * Class: CSE 132
 * Lab: 5
 */

public class ProtocolMain {

	/**
	 * Main method
	 * @param args unused
	 */
	public static void main(String[] args){
		SetGame sg = new SetGame();
		try {
			new Thread(new NetworkView(sg)).start();
			final Client c = new Client();
			c.run();
			new Thread(new Runnable(){
				@Override
				public void run() {
					while(true){
						c.getSmis().read();
					}
				}
			}).start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
