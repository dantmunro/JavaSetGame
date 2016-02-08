package lab5.view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.DefaultListModel;

import lab5.client.ClientFrame;
import lab5.model.Card;
import lab5.model.Player;
import lab5.model.SetGame;

/**
 * PlayerHandler - manages communication between the server and a client.  Determines the server's behavior given an inputted message
 * type from the client
 * @author Danny Munro, Josh Sherman, Joey Woodson, Brittany Edwards
 * Class: CSE 132
 * Lab: 5
 *
 */

public class PlayerHandler implements Runnable {
	private Socket s;
	private SetGame model;
	private DataInputStream dis;
	private DataOutputStream dos;
	private static final byte VERSION_NUMBER = 1;
	private static final byte DEFAULT_CARD_DEAL = 12; // Cards dealt at the beginning of the game
	private static final byte ADDITIONAL_CARD_DEAL = 3;
	private short score;
	private NetworkView view;
	private Random rand;
	private Map<String, Short> map;
	private byte playerNumber;
	private String name;

	public String getName() {
		return name;
	}

	/**
	 * Constructor for PlayerHandler - opens up IO pertaining to a given client, and initializes the model (SetGame) and NetworkView
	 * @param s
	 * @param model
	 * @param view
	 */
	public PlayerHandler(Socket s, SetGame model, NetworkView view) {
		super();
		this.s = s;
		this.model = model;
		this.view = view;
		this.rand = new Random();
		this.score = 0;
		try {
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
		} catch (IOException e) {
		}
	}

	/**
	 * run() method for PlayerHandler.  Contains the switch statement determining which messages to send from the server given a 
	 * message type from the client
	 */
	@Override
	public void run() {
		try {
			while (true) {
				int command = dis.readByte();
				// Switch statement
				if (command == 0) {
					// Create new player with given name and assign them the number of people currently signed on
					name = dis.readUTF();
					model.addPlayer(name);
					// Send message type 1
					startMsg(dos, 1, 2); // This message is 2 bytes: 1 for the version number, and one for the number of the querying player
					dos.writeByte(VERSION_NUMBER);
					playerNumber = (byte)model.getNumPlayers();
					dos.writeByte(playerNumber);
					
					// find length of the payload for message type 6
					int lengthSix = 1 + model.getNumPlayers(); // Every payload has at least a byte representing the number of players and a byte per player
					// Add the byte lengths of each player name
					for (Player player : model.getPlayers()) {
						lengthSix += player.getName().getBytes().length;
					}

					// Send message type 6
					view.broadcast6(lengthSix);
				} else if (command == 13) {
					byte playerNumber = dis.readByte();
					startMsg(dos, 14, 1); // Every payload is 1 byte - the player number
					dos.writeByte(playerNumber); // Sends the player's number back to the player's client
				} else if (command == 15) {
					ArrayList<byte[]> cardData = new ArrayList<byte[]>();
					byte playerNumber = dis.readByte();
					for (int i = 0; i < ClientFrame.MAX_SELECTABLE_CARDS; i++) {
						byte cardIndex = dis.readByte();
						cardData.add(view.getField().get(cardIndex));
					}
					if (NetworkView.isSet(cardData.get(0), cardData.get(1), cardData.get(2))) {
						view.broadcast((byte) 17, playerNumber, (byte) 0);
						for (int i = 0; i < 3; i++) {
							view.getField().remove(cardData.get(i));
							if (view.getField().size() < DEFAULT_CARD_DEAL && view.getDeck().size() > 0)
								view.getField().add(view.getDeck().remove(rand.nextInt(view.getDeck().size())));
						}
						view.broadcastField();
						score++;
						view.broadcastScore();
					} else {
						view.broadcast((byte) 18, playerNumber, (byte) 2);
					}
				} else if (command == 19) {
					// Server reads in player number, spits back out (aside from the standard info), the current deck size, current field size, and each card to be dealt
					byte playerNumber = dis.readByte();
					if (view.getField().size() < 21 && view.getDeck().size() > 0) {
						view.broadcast((byte) 20, playerNumber, (byte) 0);
						for (int i = 0; i < ADDITIONAL_CARD_DEAL; i++) {
							byte[] cardInfo = view.getDeck().remove(rand.nextInt(view.getDeck().size()));
							view.getField().add(cardInfo);
						}
						view.broadcastField();
					} else if (view.getField().size() >= 21){
						view.broadcast((byte) 21, playerNumber, (byte) 1);
					} else {
						view.broadcast((byte) 21, playerNumber, (byte) 2);
					}
				}
			}
		} catch (EOFException e) {
			// graceful termination on EOF
		} catch (IOException e) {
			System.out.println("Remote connection reset");
		}
	}
	
	/**
	 * Get score
	 * @return score
	 */
	public short getScore() {
		return score;
	}
	
	/**
	 * Get player number
	 * @return player number
	 */
	public byte getPlayerNumber() {
		return playerNumber;
	}
	
	/**
	 * Start message
	 */
	public void startMsg(DataOutputStream s, int type, int length) throws IOException {
		s.writeByte((int) '!'); // msg delimiter
		s.writeByte(type); // msg type
		s.writeShort(length); // payload length (in bytes)
	}
	
	/**
	 * Get data output stream
	 * @return data output stream
	 */
	public DataOutputStream getDos() {
		return dos;
	}
}
