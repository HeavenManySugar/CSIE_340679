import java.util.*;

/**
 * the representation of the problem is as follows:
 * the grid has 6 columns, numbered 0 to 5 from left to right
 * and 6 rows, numbered 0 to 5 from top to bottom
 *
 * there are nbCars cars, numbered from 0 to nbCars-1
 * for each car i:
 * - color[i] gives its color
 * - horiz[i] indicates if it is a horizontal car
 * - len[i] gives its length (2 or 3)
 * - moveOn[i] indicates on which line it moves for a horizontal car
 * and on which column for a vertical car
 *
 * the car 0 is the one that must exit, so we have
 * horiz[0]==true, len[0]==2, moveOn[0]==2
 */
class RushHour {
	int nbCars;
	String[] color;
	boolean[] horiz;
	int[] len;
	int[] moveOn;

	public RushHour(int nbCars, String[] color, boolean[] horiz, int[] len, int[] moveOn) {
		this.nbCars = nbCars;
		this.color = color;
		this.horiz = horiz;
		this.len = len;
		this.moveOn = moveOn;
	}

	/** return the list of possible moves from s */
	LinkedList<State> moves(State s) {
		throw new Error("Method moves(State s) to be completed (Question 2)");
	}

	State solveDFS(State s) {
		throw new Error("Method solveDFS(State s) to be completed (Question 3.1)");
	}

	/** search for a solution from state s */
	State solveBFS(State s) {
		throw new Error("Method solveBFS(State s) to be completed (Question 3.2)");
	}

	/** print the solution */
	void printSolution(State s) {
		throw new Error("Method printSolution(State s) to be completed (Question 4)");
	}

}

/**
 * given the position of each car, with the following convention:
 * for a horizontal car it is the column of its leftmost square
 * for a vertical car it is the column of its topmost square
 * (recall: the leftmost column is 0, the topmost row is 0)
 */
class State {
	RushHour plateau;
	int[] pos;

	/** we remember which move led to this state, for the display of the solution */
	State prev;
	int c;
	int d;

	/** construct an initial state (c, d and prev are not significant) */
	public State(RushHour plateau, int[] pos) {
		this.plateau = plateau;
		this.pos = pos;
		this.prev = null;
		this.c = -1;
		this.d = 0;
	}

	/** construct a state obtained from s by moving car c by d (-1 or +1) */
	public State(State s, int c, int d) {
		this.plateau = s.plateau;
		this.pos = new int[s.pos.length];
		for (int i = 0; i < s.pos.length; i++)
			this.pos[i] = s.pos[i];
		this.prev = s;
		this.c = c;
		this.d = d;
		this.pos[c] += d;
	}

	/** winning ? */
	public boolean success() {
		if (!plateau.horiz[0] || plateau.len[0] != 2) {
			return false;
		}
		return pos[0] + 1 == 5;
	}

	/** what are the free places */
	public boolean[][] free() {
		boolean[][] free = new boolean[6][6];
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 6; j++)
				free[i][j] = true;
		for (int i = 0; i < plateau.nbCars; i++) {
			int p = pos[i];
			if (plateau.horiz[i]) {
				for (int j = 0; j < plateau.len[i]; j++)
					free[plateau.moveOn[i]][p + j] = false;
			} else {
				for (int j = 0; j < plateau.len[i]; j++)
					free[p + j][plateau.moveOn[i]] = false;
			}
		}
		return free;
	}

	/** test of equality of two states */
	public boolean equals(Object o) {
		if (o == null || !(o instanceof State))
			return false;
		State s = (State) o;
		if (s.plateau != plateau)
			return false;
		for (int i = 0; i < pos.length; i++)
			if (pos[i] != s.pos[i])
				return false;
		return true;
	}

	/** hash code of the state */
	public int hashCode() {
		int h = 0;
		for (int i = 0; i < pos.length; i++)
			h = 37 * h + pos[i];
		return h;
	}

}
