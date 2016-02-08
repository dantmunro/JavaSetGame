package lab5.client;

import java.awt.EventQueue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import lab5.model.Player;
import lab5.model.SetGame;

/**
 *  Client
 *   Player program for game of Set.
 *
 *  @author Danny Munro, Josh Sherman, Joey Woodson, Brittany Edwards
 *  Class: CSE 132
 *  Lab: 5
 */

public class Client {
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	SetMsgInputStream smis;
	SetMsgOutputStream smos;
	final private Random  rand;
	ClientFrame frame;
	private String playerName;
	private byte playerNumber;
	private ArrayList<Byte> selectedCards;  //Indexes on the JToggleButton ArrayList cardButtons of the cards selected by the player 
											//(i.e. which indices represent selected JToggleButtons)
	private Map<Byte, String> players;

	/**
	 * Constructor for the client.  Opens up a socket to the server, and sets up I/O connections, as well as showing an Input Dialog
	 * asking for the name of the user
	 */
	public Client() {
		rand = new Random();
		selectedCards = new ArrayList<Byte>();
		try {
			this.socket= new Socket("localhost", 25151);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			smis = new SetMsgInputStream(dis, this);
			smos = new SetMsgOutputStream(dos, this);
			playerName = (String)JOptionPane.showInputDialog(new JFrame(), "Please enter a gamer tag", "Enter Name", JOptionPane.PLAIN_MESSAGE, null, null, "");
			players = new HashMap<Byte, String>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Getter for the player number-to-name map
	 * @return a HashMap of player numbers and names
	 */
	public Map<Byte, String> getPlayers() {
		return players;
	}

	/**
	 * Setter for the playerNumber - this is only used by the server when it assigns a number to a player of a given name
	 * @param playerNumber - the client's player's number
	 */
	public void setPlayerNumber(byte playerNumber) {
		this.playerNumber = playerNumber;
	}

	/**
	 * Setter for the playerName - only used by the client when it saves the input from the InputDialog asking for a name
	 * @param playerName - the client's player's name
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * Getter for the cards currently selected by the client
	 * @return an ArrayList of indexes (within the cardButtons array of JToggleButtons) of selected JToggleButtons
	 */
	public ArrayList<Byte> getSelectedCards() {
		return selectedCards;
	}

	/**
	 * Getter for the playerName
	 * @return the client's player's name
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Getter for the SetMsgInputStream
	 * @return the client's SetMsgInputStream
	 */
	public SetMsgInputStream getSmis() {
		return smis;
	}

	/**
	 * Setter for the SetMsgInputStream
	 * @param the client's SetMsgInputStream
	 */
	public void setSmis(SetMsgInputStream smis) {
		this.smis = smis;
	}

	/**
	 * Getter for the SetMsgOutputStream
	 * @return the client's SetMsgOutputStream
	 */
	public SetMsgOutputStream getSmos() {
		return smos;
	}

	/**
	 * Setter for the SetMsgOutputStream
	 * @param the client's SetMsgOutputStream
	 */
	public void setSmos(SetMsgOutputStream smos) {
		this.smos = smos;
	}

	/**
	 * Sends a "hello message" (client-to-server message type 0) when it signs on to the game, by having the client's 
	 * SetMsgOutputStream send message type 0
	 */
	public void helloMessage(){
		smos.write((byte)0);
	}
	
	/**
	 * Notifies the server that three cards are selected (i.e. the client believes it has found a set), by by having the client's 
	 * SetMsgOutputStream send message type 13 (client-to-server message type 13)
	 */
	public void notifyServerForSet(){
		smos.write((byte)13); 
	}
	
	/**
	 * Queries the server as to whether the three cards it has selected are a set, by by having the client's 
	 * SetMsgOutputStream send message type 15 (client-to-server message type 15)
	 */
	public void appealForSet(){
		smos.write((byte)15);
	}
	
	/**
	 * Created an extra protocol number on the client side (19) to appeal for a hint (i.e. appeal to have the server deal out
	 * three extra cards, and an two extra ones on the server side (20 - valid hint request, and 21 - invalid hint request/maximum number
	 * of hints have been called).  This method appeals to the server for a hint by having its SetMsgOutputStream send message type 19
	 */ 
	public void appealForHint(){
		smos.write((byte)19);
	}

	/**
	 * Getter for player number
	 * @return the client's player's number
	 */
	public byte getPlayerNumber() {
		return playerNumber;
	}

	/**
	 * Starts the client frame.
	 */
	public void run() {
		helloMessage();
		frame = new ClientFrame(this);
		frame.setVisible(true);
	}

	/**
	 * Getter for the ClientFrame/JFrame for this client
	 * @return the client's JFrame
	 */
	public ClientFrame getFrame() {
		return frame;
	}

	/**
	 * Entry point for lab5 client.
	 * @param args unused
	 */
	public static void main(String[] args) {
		Client c = new Client();
		c.run();
	}
}
