package lab5.view;

import java.awt.Image;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import lab5.client.Client;
import lab5.client.ClientFrame;
import lab5.model.Card;
import lab5.model.Player;
import lab5.model.SetGame;

/**
 * NetworkView - acts as the server and remote view for the game.
 * @author Danny Munro, Josh Sherman, Joey Woodson, Brittany Edwards
 * Class: CSE 132
 * Lab: 5
 *
 */

public class NetworkView implements Runnable {
	private SetGame model;
	private static final byte DEFAULT_CARD_DEAL = 12; //Cards dealt at the beginning of the game
	private static final int CARD_TRAIT_VARIETIES = 3; //Number of different possibilities regarding each trait (color, shape, shading, number)
	private static final byte MAX_PLAYERS = 2; //This many clients must be run before cards will be dealt
	private ArrayList<byte[]> deck; 
	private ArrayList<byte[]> field;
	private ArrayList<PlayerHandler> playerHandlers;
	private final int PORT = 25151;
	private Random rand;

	/**
	 * Constructor for NetworkView.  Initializes the deck and field, and adds every type of cards to the deck in an unshuffled fashion
	 * @param model - the SetGame() class that keeps track of players and scores
	 * @throws IOException
	 */
	public NetworkView(SetGame model) throws IOException {
		this.model = model;
		deck = new ArrayList<byte[]>();
		field = new ArrayList<byte[]>();
		rand = new Random();
		playerHandlers = new ArrayList<PlayerHandler>();

		for(byte i = 1; i <= CARD_TRAIT_VARIETIES; i++){
			for(byte j = 1; j <= CARD_TRAIT_VARIETIES; j++){
				for(byte k = 1; k <= CARD_TRAIT_VARIETIES; k++){
					for(byte l = 1; l <= CARD_TRAIT_VARIETIES; l++){
						deck.add(new byte[] {i, j, k, l});
					}
				}
			}
		}		
	}

	/**
	 * run() method for the NetworkView.  Establishes connection with clients and deals cards to the field.  Opens sockets for 
	 * every new player
	 */
	public void run() {
		try {
			ServerSocket ss = new ServerSocket(PORT);
			while (playerHandlers.size() < MAX_PLAYERS) {
				Socket s = ss.accept();
				PlayerHandler ph = new PlayerHandler(s, model, this);
				playerHandlers.add(ph);
				new Thread(ph).start();
			}
			for(int i = 0; i < DEFAULT_CARD_DEAL; i++) {
				byte[] cardInfo = deck.remove(rand.nextInt(deck.size())); 
				field.add(cardInfo);
			}
			Thread.sleep(1000);
			broadcastField();
		} catch (Exception e) {e.printStackTrace();}
	}

	/**
	 * Getter for deck
	 * @return deck - an ArrayList of byte arrays of cards yet to be dealt
	 */
	public ArrayList<byte[]> getDeck() {
		return deck;
	}
	
	/**
	 * Getter for field
	 * @return field - an ArrayList of byte arrays of cards currently on the field
	 */
	public ArrayList<byte[]> getField() {
		return field;
	}
	
	/**
	 * Determines whether three given cards are a set.  Checks that all three traits for each card are either all the same or all
	 * different.  Checks that the user is not selecting the same card three times
	 * @param card1 - a byte array containing the bytes representing all of the first card's traits
	 * @param card2 - a byte array containing the bytes representing all of the second card's traits
	 * @param card3 - a byte array containing the bytes representing all of the third card's traits
	 * @return
	 */
	public static boolean isSet(byte[] card1, byte[] card2, byte[] card3){
		return (((card1[0] == card2[0] && card2[0] == card3[0] && card1[0] == card3[0]) || (card1[0] != card2[0] && card2[0] != card3[0] && card1[0] != card3[0])) 
		&& ((card1[1] == card2[1] && card2[1] == card3[1] && card1[1] == card3[1]) || (card1[1] != card2[1] && card2[1] != card3[1] && card1[1] != card3[1]))
		&& ((card1[2] == card2[2] && card2[2] == card3[2] && card1[2] == card3[2]) || (card1[2] != card2[2] && card2[2] != card3[2] && card1[2] != card3[2]))
		&& ((card1[3] == card2[3] && card2[3] == card3[3] && card1[3] == card3[3]) || (card1[3] != card2[3] && card2[3] != card3[3] && card1[3] != card3[3]))
		&& (!Arrays.equals(card1, card2) && !Arrays.equals(card2, card3) && !Arrays.equals(card1, card3)));
	}
	
	/**
	 * Sends a given byte from the server to each client via the clients' playerHandlers' DataOutputStream
	 * @param b - a byte to be sent
	 */
	public void broadcastByte(int b) {
		for (PlayerHandler ph : playerHandlers) {
			try {
				ph.getDos().writeByte(b);
			} catch (IOException e) {}
		}
	}
	
	/**
	 * Sends a given UTF-8 encoded string from the server to the client via the clients' playerHandlers' DataOutputStream
	 * @param s - a string to be sent
	 */
	public void broadcastUTF(String s) {
		for (PlayerHandler ph : playerHandlers) {
			try {
				ph.getDos().writeUTF(s);
			} catch (IOException e) {}
		}
	}
	
	/**
	 * Tells the clients (through their playerHandlers) which cards are currently on the field, and instructs the clients to display 
	 * the respective cards on their GUI
	 * @throws IOException
	 */
	public void broadcastField() throws IOException {
		for (PlayerHandler ph : playerHandlers) {
			try {
				DataOutputStream dos = ph.getDos();
				ph.startMsg(dos, 5, (short) (deck.size() + field.size() * Card.NUMBER_OF_TRAITS + 1)); //1 is the size of the byte representing the field size
				dos.writeByte(deck.size());
				dos.writeByte(field.size()); // field size
				for(int i = 0; i < field.size(); i++){
					byte[] cardInfo = field.get(i);
					for(int j = 0; j < cardInfo.length; j++){
						dos.writeByte(cardInfo[j]);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Tells the clients (through their playerHandlers) their scores
	 * @throws IOException
	 */
	public void broadcastScore() throws IOException {
		for (PlayerHandler ph : playerHandlers) {
			try {
				DataOutputStream dos = ph.getDos();
				ph.startMsg(dos, 7, 3 * model.getNumPlayers() + 1);
				dos.writeByte(model.getNumPlayers());
				for(PlayerHandler player : playerHandlers){
					dos.writeByte(player.getPlayerNumber());
					dos.writeShort(player.getScore());
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Tells the clients (through their playerHandlers) how many players are currently signed on, along with each of their 
	 * names and numbers
	 * @param len - the length of the payload, in bytes, to be sent to the server
	 */
	public void broadcast6(int len) {
		for (PlayerHandler ph : playerHandlers) {
			try {
				DataOutputStream dos = ph.getDos();
				ph.startMsg(dos, 6, len);
				dos.writeByte(model.getNumPlayers()); //One byte representing the number of players
				for(Player player : model.getPlayers()){
					dos.writeByte(player.getID());
					dos.writeUTF(player.getName());
				}
			} catch (IOException e) {}
		}
	}
	
	/**
	 * Tells the clients (through their playerHandlers) about any other information not already mentioned. This includes all of the 
	 * other server-to-client message types not already hardcoded
	 * @param type - the message type
	 * @param b - a byte to be sent by the server
	 * @param extra - an additional byte to be sent, if necessary
	 */
	public void broadcast(byte type, byte b, byte extra) {
		for (PlayerHandler ph : playerHandlers) {
			try {
				int length = 1;
				if (extra > 0)
					length++;
				DataOutputStream dos = ph.getDos();
				ph.startMsg(dos, type, length);
				dos.writeByte(b);
				if (extra > 0)
					dos.writeByte(extra);
			} catch (IOException e) {}
		}
	}
	
	/**
	 * Verifies whether the game is over by checking that the deck is empty
	 * and that no three cards make up a set.  Sends message type 60, which closes
	 * down the game
	 */
	public void checkGameOver(){
		if (deck.size()==0){ 
			for (int i =0; i<field.size(); i++){
				for(int j=i+1; j<field.size(); j++){
					for(int k = j+1; k<field.size(); k++){
						if (isSet(field.get(i), field.get(j), field.get(k))){
							return;
						}
					}
				}
			}
			short topScore = -1;
			byte winningPlayer = 0;
			for (PlayerHandler ph : playerHandlers) {
				if (ph.getScore() > topScore) {
					topScore = ph.getScore();
					winningPlayer = ph.getPlayerNumber();
				}
			}
			broadcast((byte)60, winningPlayer, (byte)0);
		}
		
	}
}
