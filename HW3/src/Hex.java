/* The Hex game
   https://en.wikipedia.org/wiki/Hex_(board_game)
   desigened by Jean-Christophe Filliâtre

   grid size : n*n

   playable cells : (i,j) with 1 <= i, j <= n

   blue edges (left and right) : i=0 or i=n+1, 1 <= j <= n
    red edges (top and bottom) : 1 <= i <= n, j=0 or j=n+1

      note: the four corners have no color

   adjacence :      i,j-1   i+1,j-1

                 i-1,j    i,j   i+1,j

                    i-1,j+1    i,j+1

*/

import java.util.Vector;

public class Hex {
  // a class to represent a disjoint set
  class UnionFind {
    private int[] link;

    UnionFind(int n) {
      if (n < 0) {
        throw new IllegalArgumentException("n must be non-negative");
      }
      link = new int[n];
      for (int i = 0; i < n; i++) {
        link[i] = i;
      }
    }

    int find(int i) {
      if (link[i] == i) {
        return i;
      }
      link[i] = find(link[i]);
      return link[i];
    }

    void union(int i, int j) {
      link[find(i)] = find(j);
    }
  }

  enum Player {
    NOONE, BLUE, RED
  }

  Vector<Vector<Player>> grid;
  int size;
  Player currentPlayer = Player.RED;
  UnionFind uf;
  Player winner = Player.NOONE;

  // create an empty board of size n*n
  Hex(int n) {
    size = n + 2;
    grid = new Vector<Vector<Player>>();
    for (int i = 0; i < size; i++) {
      Vector<Player> row = new Vector<Player>();
      for (int j = 0; j < size; j++) {
        row.add(Player.NOONE);
      }
      grid.add(row);
    }
    uf = new UnionFind(size * size);
    // connect the four corners
    for (int i = 1; i < size - 2; i++) {
      grid.get(0).set(i, Player.BLUE);
      grid.get(i).set(0, Player.RED);
      grid.get(size - 1).set(i, Player.BLUE);
      grid.get(i).set(size - 1, Player.RED);
      uf.union(label(0, i + 1), label(0, i));
      uf.union(label(i + 1, 0), label(i, 0));
      uf.union(label(size - 1, i + 1), label(size - 1, i));
      uf.union(label(i + 1, size - 1), label(i, size - 1));
    }
  }

  // return the color of cell i,j
  Player get(int i, int j) {
    return grid.get(i).get(j);
  }

  // return true if the cell (i,j) is a valid cell
  boolean isVaild(int i, int j) {
    return i >= 0 && i < size && j >= 0 && j < size;
  }

  // update the union-find data structure after the player with the trait plays
  // the cell (i, j)
  void updateUnion(int i, int j) {
    int[][] positions = { { i, j - 1 }, { i + 1, j - 1 }, { i - 1, j }, { i + 1, j }, { i - 1, j + 1 }, { i, j + 1 } };
    for (int[] pos : positions) {
      if (isVaild(pos[0], pos[1]) && get(pos[0], pos[1]) == get(i, j)) {
        uf.union(label(i, j), label(pos[0], pos[1]));
      }
    }
  }

  // update the board after the player with the trait plays the cell (i, j).
  // Does nothing if the move is illegal.
  // Returns true if and only if the move is legal.
  boolean click(int i, int j) {
    if (currentPlayer == Player.NOONE) {
      return false;
    }
    if (i < 0 || i > size - 1 || j < 0 || j > size - 1) {
      return false;
    }
    if (get(i, j) != Player.NOONE) {
      return false;
    }
    grid.get(i).set(j, currentPlayer);
    updateUnion(i, j);
    if (uf.find(label(0, i)) == uf.find(label(size - 1, i))) {
      currentPlayer = Player.NOONE;
      winner = Player.BLUE;
      return false;
    } else if (uf.find(label(i, 0)) == uf.find(label(i, size - 1))) {
      currentPlayer = Player.NOONE;
      winner = Player.RED;
      return false;
    }
    currentPlayer = currentPlayer == Player.BLUE ? Player.RED : Player.BLUE;
    return true;
  }

  // return the player with the trait or Player.NOONE if the game is over
  // because of a player's victory.
  Player currentPlayer() {
    return currentPlayer;
  }

  // return the winning player, or Player.NOONE if no player has won yet
  Player winner() {
    return winner;
  }

  int label(int i, int j) {
    return uf.find(i + j * size);
  }

  public static void main(String[] args) {
    HexGUI.createAndShowGUI();
  }
}
