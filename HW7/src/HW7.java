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
		LinkedList<State> listMoves = new LinkedList<State>();
		boolean[][] free = s.free();
		for (int i = 0; i < nbCars; i++) {
			if (horiz[i]) {
				if (s.pos[i] > 0 && free[moveOn[i]][s.pos[i] - 1]) {
					listMoves.add(new State(s, i, -1));
				}
				if (s.pos[i] + len[i] < 6 && free[moveOn[i]][s.pos[i] + len[i]]) {
					listMoves.add(new State(s, i, 1));
				}
			} else {
				if (s.pos[i] > 0 && free[s.pos[i] - 1][moveOn[i]]) {
					listMoves.add(new State(s, i, -1));
				}
				if (s.pos[i] + len[i] < 6 && free[s.pos[i] + len[i]][moveOn[i]]) {
					listMoves.add(new State(s, i, 1));
				}
			}
		}
		return listMoves;
	}

	/** search for a solution from state s using DFS */
	State solveDFS(State s) {
		HashSet<State> visited = new HashSet<>();
		Stack<State> stack = new Stack<>();

		stack.push(s);
		visited.add(s);

		while (!stack.isEmpty()) {
			State current = stack.pop();

			if (current.success()) {
				return current; // Found solution
			}

			LinkedList<State> neighbors = moves(current);
			for (State neighbor : neighbors) {
				if (!visited.contains(neighbor)) {
					stack.push(neighbor);
					visited.add(neighbor);
				}
			}
		}

		return null; // No solution found
	}

	/** search for a solution from state s */
	State solveBFS(State s) {
		HashSet<State> visited = new HashSet<>();
		Queue<State> queue = new LinkedList<>();

		queue.add(s);
		visited.add(s);

		while (!queue.isEmpty()) {
			State current = queue.poll();

			if (current.success()) {
				return current; // Found solution
			}

			LinkedList<State> neighbors = moves(current);
			for (State neighbor : neighbors) {
				if (!visited.contains(neighbor)) {
					queue.add(neighbor);
					visited.add(neighbor);
				}
			}
		}

		return null; // No solution found
	}

	/** print the solution */
	void printSolution(State s) {
		if (s == null) {
			System.out.println("No solution found");
			return;
		}

		List<State> path = new ArrayList<>();
		while (s != null) {
			path.add(s);
			s = s.prev;
		}

		Collections.reverse(path);

		System.out.println((path.size() - 1) + " trips");

		for (int i = 0; i < path.size(); i++) {
			State state = path.get(i);
			if (i > 0) {
				String direction = state.d > 0 ? "right" : "left";
				if (!state.plateau.horiz[state.c]) {
					direction = state.d > 0 ? "down" : "up";
				}
				System.out.println("we move the " + state.plateau.color[state.c] + " vehicle to the " + direction);
			}
		}
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
