package lab5.client;

import java.awt.HeadlessException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import lab5.model.Card;

/**
 * SetMsgInputStream - manages all of the different types of messages sent by the client (wraps DataOutputStream)
 *  @author Danny Munro, Josh Sherman, Joey Woodson, Brittany Edwards
 *  Class: CSE 132
 *  Lab: 5
 *
 */

public class SetMsgOutputStream{
	
	private Client client;
	private DataOutputStream dos;

	/**
	 * Constructor for SetMsgInputStream
	 * @param dis - the DataInputStream to be wrapped 
	 * @param client - the client that owns the DataInputStream
	 */
	public SetMsgOutputStream(DataOutputStream dos, Client client) {
		this.dos = dos;
		this.client=client;
	}
	
	/**
	 * Writes the message type and any associated information required in client-to-server messages.
	 * @param messageType
	 */
	public void write(byte messageType){
		try {
			dos.writeByte(messageType);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(messageType == 0){
			try {
				dos.writeUTF((client.getPlayerName()));
			} catch (HeadlessException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		else if(messageType == 13){
			try {
				dos.writeByte(client.getPlayerNumber());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(messageType == 15){
			try {
				dos.writeByte(client.getPlayerNumber()); //Sends player number
				System.out.println("Player appealing for set is " + client.getPlayerNumber());
				for(int i = 0; i < client.getSelectedCards().size(); i++){
					dos.writeByte(client.getSelectedCards().get(i));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		//appeal for hint writes player number
		else if(messageType == 19){
			try {
				dos.writeByte(client.getPlayerNumber());
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}

}
