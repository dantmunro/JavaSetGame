package lab5.client;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import lab5.model.SetGame;
import lab5.view.NetworkView;

/**
 * ClientMain - main class for only client portion
 * @author Danny Munro, Josh Sherman, Joey Woodson, Brittany Edwards
 * Class: CSE 132
 * Lab: 5
 *
 */

public class ClientMain {
	/**
	 * Main method
	 * @param args unused
	 */

	public static void main(String[] args){
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
	}

}
