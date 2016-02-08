package lab5.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.Panel;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JToggleButton;

import lab5.model.Card;
import lab5.view.NetworkView;

import java.awt.Color;
import javax.swing.JList;
import javax.swing.JTextArea;

/**
 * ClientFrame - GUI for the game of Set.  Accessible only to the client
 * @author Danny Munro, Josh Sherman, Joey Woodson, Brittany Edwards
 * Class: CSE 132
 * Lab: 5
 *
 */

public class ClientFrame extends JFrame {
	private Client client;
	private ArrayList<JToggleButton> cardButtons = new ArrayList<JToggleButton>();
	private Random rand;
	private SetMsgInputStream smis;
	private JList list;
	private JTextArea textArea;
	public static final int DEFAULT_ROWS = 4;
	public static final int ALL_ROWS = 7;
	public static final int COLUMNS = 3;
	public static final int HORIZONTAL_INDENTATION = 30;
	public static final int HORIZONTAL_SPACING = 200;
	public static final int VERTICAL_INDENTATION = 5;
	public static final int VERTICAL_SPACING = 95;
	public static final int BUTTON_WIDTH = 80;
	public static final int BUTTON_HEIGHT = 87;
	public static final int MAX_SELECTABLE_CARDS = 3;
	
	/**
	 * Creates the frame, and sets up the hint button and score interface (a JTextArea)
	 * @param client
	 */
	public ClientFrame(final Client client) { 
		this.client = client;
		rand = new Random();
		smis = client.getSmis();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 812, 807);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JButton btnHint = new JButton("Hint");
		menuBar.add(btnHint);
		btnHint.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				client.appealForHint();
			}
		});
		
		getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(599, 11, 187, 164);
		getContentPane().add(textArea);
	}
	
	/**
	 * Displays the cards to the GUI
	 * @param cardsToDeal - an ArrayList of card filenames to be mounted as images to JToggleButtons
	 */
	public void dealCards(ArrayList<String> cardsToDeal){	
		//Remove old buttons
		int size = cardButtons.size();
		for(int i = 0; i < size; i++){
			this.remove(cardButtons.remove(0));
		}
		//Adds JToggleButtons with loaded images to board
		int rowsToDeal = 0;
		//Determines how many rows to deal (i.e. the default deal or dealing a hint) from how many card filenames are passed in
		rowsToDeal = cardsToDeal.size() / COLUMNS;
		for(int i = 0; i < rowsToDeal; i++){
			for(int j = 0; j < COLUMNS; j++){
				//Takes the first card from the ArrayList of cards to be dealt (using the same aforementioned index-pushback 
				//technique for ArrayLists), and mounts an image created out of the filename to a JToggleButton
				final String cardFilename = cardsToDeal.remove(0);
				final JToggleButton newCardButton = new JToggleButton(new ImageIcon(Card.USER_HOME_FOLDER + cardFilename)); 
				//Adds an actionlistener adding the card to a list of three cards, which the user presumably believes is a set,
				//as soon as the button is selected
				newCardButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						//Adds the location of the JToggleButton to an ArrayList of JToggleButton field locations called selectedCards
						client.getSelectedCards().add((byte)cardButtons.indexOf(arg0.getSource()));
						//As soon as three cards are selected, the client queries the server, asking it to observe its set
						if(client.getSelectedCards().size() == MAX_SELECTABLE_CARDS)
							client.notifyServerForSet();
					}
				});
				//Display the card on the JFrame
				newCardButton.setBounds(HORIZONTAL_INDENTATION + HORIZONTAL_SPACING*j, VERTICAL_INDENTATION + VERTICAL_SPACING*i, BUTTON_WIDTH, BUTTON_HEIGHT);
				getContentPane().add(newCardButton);
				cardButtons.add(newCardButton);
				getContentPane().validate();
				getContentPane().repaint();
			}
		}
	}
	
	/**
	 * If three cards do not make up a set, this method deselects all of the cards' JToggleButtons
	 */
	public void deselectButtons(){
		for(int i = 0; i < cardButtons.size(); i++){
			if(cardButtons.get(i).isSelected()){
				cardButtons.get(i).setSelected(false);
			}
		}
		client.getSelectedCards().clear();
	}
	
	/**
	 * Inputs text into the JTextArea
	 * @param text to be inputted - used by the 
	 * SetMsgInputStream when updating players' scores
	 */
	public void setTextAreaText(String text) {
		textArea.setText(text);
	}
}