/* HW1. Battle
 * This file contains two classes :
 * 		- Deck represents a pack of cards,
 * 		- Battle represents a battle game.
 */

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

class Deck { // represents a pack of cards

	LinkedList<Integer> cards;
	// The methods toString, hashCode, equals, and copy are used for
	// display and testing, you should not modify them.

	@Override
	public String toString() {
		return cards.toString();
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		Deck d = (Deck) o;
		return cards.equals(d.cards);
	}

	Deck copy() {
		Deck d = new Deck();
		for (Integer card : this.cards)
			d.cards.addLast(card);
		return d;
	}

	// constructor of an empty deck
	Deck() {
		cards = new LinkedList<Integer>();
	}

	// constructor from field
	Deck(LinkedList<Integer> cards) {
		this.cards = cards;
	}

	// constructor of a complete sorted deck of cards with nbVals values
	Deck(int nbVals) {
		cards = new LinkedList<Integer>();
		for (int j = 1; j <= nbVals; j++)
			for (int i = 0; i < 4; i++)
				cards.add(j);
	}

	// Question 1

	// takes a card from deck d to put it at the end of the current packet
	int pick(Deck d) {
		// throw new Error("Method pick(Deck d) to complete (Question 1)");
		if (!d.cards.isEmpty()) {
			int x = d.cards.removeFirst();
			cards.addLast(x);
			return x;
		} else {
			return -1;
		}
	}

	// takes all the cards from deck d to put them at the end of the current deck
	void pickAll(Deck d) {
		// throw new Error("Method pickAll(Deck d) to complete (Question 1)");
		while (!d.cards.isEmpty()) {
			pick(d);
		}
	}

	// checks if the current packet is valid
	boolean isValid(int nbVals) {
		// throw new Error("Method isValid(int nbVals) to complete (Question 1)");
		int[] numbers = new int[nbVals];
		for (Integer x : cards) {
			if (x < 1 || x > nbVals || numbers[x - 1] > 3)
				return false;
			numbers[x - 1]++;
		}
		return true;
	}

	// Question 2.1

	// chooses a position for the cut
	int cut() {
		int result = 0;
		for (int i = 0; i < cards.size(); i++) {
			if (Math.random() < 0.5) {
				result++;
			}
		}
		return result;
	}

	// cuts the current packet in two at the position given by cut()
	Deck split() {
		Deck d = new Deck();
		int cut = cut();
		for (int i = 0; i < cut; i++) {
			d.cards.add(cards.removeFirst());
		}
		return d;
	}

	// Question 2.2

	// mixes the current deck and the deck d
	void riffleWith(Deck d) {
		Deck f = new Deck();
		double p;
		while (!cards.isEmpty() && !d.cards.isEmpty()) {
			p = (double) cards.size() / (cards.size() + d.cards.size());
			if (Math.random() < p) {
				f.cards.add(cards.removeFirst());
			} else {
				f.cards.add(d.cards.removeFirst());
			}
		}
		while (!cards.isEmpty()) {
			f.cards.add(cards.removeFirst());
		}
		while (!d.cards.isEmpty()) {
			f.cards.add(d.cards.removeFirst());
		}
		cards = f.cards;
	}

	// Question 2.3

	// shuffles the current deck using the riffle shuffle
	void riffleShuffle(int m) {
		for (int i = 0; i < m; i++) {
			riffleWith(split());
		}
	}
}

class Battle { // represents a battle game

	Deck player1;
	Deck player2;
	Deck trick;
	boolean turn = true;

	// constructor of a battle without cards
	Battle() {
		player1 = new Deck();
		player2 = new Deck();
		trick = new Deck();
	}

	// constructor from fields
	Battle(Deck player1, Deck player2, Deck trick) {
		this.player1 = player1;
		this.player2 = player2;
		this.trick = trick;
	}

	// copy the battle
	Battle copy() {
		Battle r = new Battle();
		r.player1 = this.player1.copy();
		r.player2 = this.player2.copy();
		r.trick = this.trick.copy();
		return r;
	}

	// string representing the battle
	@Override
	public String toString() {
		return "Player 1 : " + player1.toString() + "\n" + "Player 2 : " + player2.toString() + "\nPli "
				+ trick.toString();
	}

	// Question 3.1

	// constructor of a battle with a deck of cards of nbVals values
	Battle(int nbVals) {
		player1 = new Deck();
		player2 = new Deck();
		trick = new Deck(nbVals);
		trick.riffleShuffle(7);
		while (!trick.cards.isEmpty()) {
			player1.cards.add(trick.cards.removeFirst());
			player2.cards.add(trick.cards.removeFirst());
		}
	}

	// Question 3.2

	// test if the game is over
	boolean isOver() {
		return player1.cards.isEmpty() || player2.cards.isEmpty();
	}

	// effectue un tour de jeu
	boolean oneRound() {
		return oneRound(true);
	}

	boolean oneRound(boolean disableTurn) {
		int card1;
		int card2;
		if (isOver()) {
			return false;
		}
		while (!isOver()) {
			if (disableTurn || turn) {
				card1 = player1.cards.removeFirst();
				card2 = player2.cards.removeFirst();
			} else {
				card2 = player2.cards.removeFirst();
				card1 = player1.cards.removeFirst();
			}
			turn = !turn; // Alternate turn
			trick.cards.add(card1);
			trick.cards.add(card2);
			if (card1 > card2) {
				player1.pickAll(trick);
				return true;
			} else if (card1 < card2) {
				player2.pickAll(trick);
				return true;
			} else {
				if (isOver()) {
					return false;
				}
				if (disableTurn || turn) {
					trick.cards.add(player1.cards.removeFirst());
					trick.cards.add(player2.cards.removeFirst());
				} else {
					trick.cards.add(player2.cards.removeFirst());
					trick.cards.add(player1.cards.removeFirst());
				}
				turn = !turn; // Alternate turn
				if (isOver()) {
					return false;
				}
			}
		}
		return true;
	}

	// Question 3.3

	// returns the winner
	int winner() {
		int a = player1.cards.size();
		int b = player2.cards.size();
		if (a > b) {
			return 1;
		} else if (a < b) {
			return 2;
		} else {
			return 0;
		}
	}

	// plays a game with a fixed maximum number of moves
	int game(int turns) {
		int i = 0;
		while (i++ < turns && oneRound())
			;
		return winner();
	}

	// Question 4.1

	// plays a game without limit of moves, but with detection of infinite games
	int game() {
		return game(true);
	}

	int game(boolean disableTurn) {
		// Floyd Cycle Detection Algorithm (Tortoise and Hare Algorithm)
		Battle tortoise = this.copy();
		Battle hare = this;

		while (!hare.isOver()) {
			tortoise.oneRound(disableTurn);
			hare.oneRound(disableTurn);
			if (hare.isOver()) {
				break;
			}
			hare.oneRound(disableTurn);
			if (hare.toString().equals(tortoise.toString())) {
				return 3;
			}
		}

		return hare.winner();
	}

	// Question 4.2

	// performs statistics on the number of infinite games
	static void stats(int nbVals, int nbGames) {
		int[] result = new int[4];

		for (int i = 0; i < nbGames; i++) {
			result[new Battle(nbVals).game(false)]++;
		}

		System.out.println("Draws: " + result[0]);
		System.out.println("Player 1 wins: " + result[1]);
		System.out.println("Player 2 wins: " + result[2]);
		System.out.println("Infinite games: " + result[3]);
	}
}
