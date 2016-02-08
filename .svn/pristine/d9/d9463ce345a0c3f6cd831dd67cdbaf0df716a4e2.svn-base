package lab5.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import lab5.model.Card;
import lab5.model.Player;
import lab5.view.NetworkView;

/**
 * SetMsgInputStream - manages all of the different types of messages sent by the server/NetworkView (wraps DataInputStream)
 *  @author Danny Munro, Josh Sherman, Joey Woodson, Brittany Edwards
 *  Class: CSE 132
 *  Lab: 5
 *
 */

public class SetMsgInputStream{

	private Client client;
	private DataInputStream dis;
	private Map<Byte, Short> map;


	/**
	 * Constructor for SetMsgInputStream
	 * @param dis - the DataInputStream to be wrapped 
	 * @param client - the client that owns the DataInputStream
	 */
	public SetMsgInputStream(DataInputStream dis, Client client) {
		this.dis = dis;
		this.client=client;
		map = new HashMap<Byte, Short>();
	}

	/**
	 * Reads the standard info sent by any message of the server's (delimiter, message type, payload length, etc.), and contains a large
	 * if statement which handles different message types
	 * @return 0 - a placeholder return value
	 */
	public int read(){
		char delimiter;
		try {
			delimiter = (char)dis.readByte();
			if(delimiter == '!'){
				byte messageType=dis.readByte();
				short payloadLength = dis.readShort();
				//This is the server's returning of the client's hello message.  The server reads the game 
				//version and sends a player number, which the SetMsgInputStream assigns to the client
				if(messageType == 1){ 
					try {
						Byte gameVersion = dis.readByte();
						Byte playerNumber = dis.readByte();
						client.setPlayerNumber(playerNumber);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				//This is the server's message to deal cards to the field.  The client reads in the info of the cards sent by the 
				//server, byte-by-byte, and adds them to a client-side ArrayList known as cardsToBeDealt
				else if(messageType == 5){
					ArrayList<String> cardsToBeDealt = new ArrayList<String>(); 
					byte cardsLeft = dis.readByte();
					byte fieldSize = dis.readByte();
					for(int i = 0; i < fieldSize; i++){
						byte cardColor = dis.readByte();
						byte cardShape = dis.readByte();
						byte cardShade = dis.readByte();
						byte cardNumber = dis.readByte();
						cardsToBeDealt.add(Card.getFilenameFromBytes(cardColor, cardShape, cardShade, cardNumber));
					}
					client.getFrame().dealCards(cardsToBeDealt);
				}
				//This is the server's updating of the players currently in the game.  The SetMsgInputStream reads the name and 
				//number of every player, and informs the client by putting it in the client's "players" HashMap
				else if(messageType == 6){
					Byte numPlayers = dis.readByte();
					for(int x = 0; x < numPlayers; x++){
						Byte playerNum = dis.readByte();
						String playerName = dis.readUTF();
						client.getPlayers().put(playerNum, playerName);
					}
				}
				//This is the server's updating of game scores.  The SetMsgInputStream reads in every player's name and score
				else if(messageType == 7){
					Byte numPlayers = dis.readByte();
					String scoreText = "Scores:\n";
					
					for(int x = 0; x < numPlayers; x++){						
						byte playerNum = dis.readByte();
						short score = dis.readShort();
						map.put(playerNum, score);
						scoreText += client.getPlayers().get(playerNum) + ":  " + score + "\n";
					}
					
					client.getFrame().setTextAreaText(scoreText);
				}
				//This is the server's response to the client's notification that it has found a set.  The player's number is read in
				else if(messageType == 14){
					synchronized(this){
						byte playerNum = dis.readByte();
						client.appealForSet();
					}
					
				}
				//This is the server's response that a set is valid.  The SetMsgInputStream's client's score is read in
				else if(messageType == 17){
					byte winningPlayerNumber = dis.readByte();
					int size = client.getSelectedCards().size();
					for (int i = 0; i < size; i++) {
						client.getSelectedCards().remove(0);
					}							
				}
				//This is the server's response that a set is not valid, explaining why the set is invalid
				else if(messageType == 18){
					byte notWinningPlayer = dis.readByte();
					if(client.getPlayerNumber() == notWinningPlayer){
						byte reason = dis.readByte();
						if(reason == 1)
							JOptionPane.showMessageDialog(new JFrame(), "TIMEOUT");
						else if(reason == 2)
							JOptionPane.showMessageDialog(new JFrame(), "Not a Set");
					}
					//The GUI deselects all the buttons held down by the client
					client.getFrame().deselectButtons();
				}
				//The client is notified that a request for a hint is valid
				else if(messageType == 20){
					byte playerNumber = dis.readByte();
				}
				//The client is notified that a hint request is invalid
				else if(messageType == 21){
					byte playerNumber = dis.readByte();
					byte reason = dis.readByte();
					if (reason == 1)
						JOptionPane.showMessageDialog(new JFrame(), "The maximum number of hints has been requested");
					if (reason == 2)
						JOptionPane.showMessageDialog(new JFrame(), "There are no cards left in the deck");
				}

				else if(messageType == 60){
					JOptionPane.showMessageDialog(new JFrame(), "Game Over");
					byte winningPlayer = dis.readByte();
					JOptionPane.showMessageDialog(new JFrame(), client.getPlayers().get(winningPlayer) + " wins!");
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return 0;
	}

	/**
	 * Getter for map mapping player numbers to scores
	 * @return map mapping player numbers to scores
	 */
	public Map<Byte, Short> getMap() {
		return map;
	}
}
