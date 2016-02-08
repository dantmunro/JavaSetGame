package lab5.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import lab5.client.Client;

/**
 * Card - an abstract class that provides constants and methods relating to cards in the game
 * @author Danny Munro, Josh Sherman, Joey Woodson, Brittany Edwards
 * Class: CSE 132
 * Lab: 5
 *
 */

public abstract class Card {

	public static final String USER_HOME_FOLDER = "Images/";
	public static final int NUMBER_OF_TRAITS = 4; //Number of traits of a card (color, shape, shading, number)
	
	/**
	 * Gets an image filename from four bytes representing a card,
	 * in the order specified by the server response
	 * @param cardColor A one-indexed number representing the color of the objects on the card
	 * @param cardShape A one-indexed number representing the shape of the objects on the card
	 * @param cardShade A one-indexed number representing the shading of the objects on the card
	 * @param cardNumber A one-indexed number representing the number of objects on the card
	 * @return the filename of the picture corresponding to that card
	 */
	public static String getFilenameFromBytes(byte cardColor, byte cardShape, byte cardShade, byte cardNumber) { 
		String filename = Byte.toString(cardNumber);
		switch (cardColor) {
		case 1:
			filename += " Blue";
			break;
		case 2:
			filename += " Green";
			break;
		case 3:
			filename += " Red";
			break;
		}
		switch (cardShade) {
		case 1:
			filename += " Filled";
			break;
		case 2:
			filename += " Open";
			break;
		case 3:
			filename += " Shaded";
			break;
		}
		switch (cardShape) {
		case 1:
			filename += " Diamonds.jpg";
			break;
		case 2:
			filename += " Ovals.jpg";
			break;
		case 3:
			filename += " Squiggles.jpg";
			break;
		}
		return filename;
	}
}
