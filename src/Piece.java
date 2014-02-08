import java.io.Serializable;
import java.util.*;

public abstract class Piece implements Serializable {

	private static final long serialVersionUID = 1L;
	private Color color;
	private Location location;
	private Board board;

	public Piece(Color c, Board b) {
		color = c;
		location = null;
		board = b;
	}

	public Color getColor() { return color; }
	public Location getLoc() { return location; }
	public void setLoc(Location newLoc) { location = newLoc; }
	public Board getBoard() { return board; }
	public void getsCaptured() {
		location = null;
		board.addCaptured(this);
		board = null;
	}
	protected void removeFromBoard() {
		board = null;
		location = null;
	}

	public abstract int getValue();
	public abstract Set<Location> getPossibleMoves();
	public abstract String toString();

	public boolean validMove(Location loc) {
		boolean allowed = includes(getPossibleMoves(), loc);
		boolean putsInCheck = false;
		Board testBoard = new Board(board);
		Piece testPiece = testBoard.get(location);
		Location initLoc = location;
		testBoard.remove(initLoc);
		testBoard.put(loc, testPiece);
		// if now in check
		if(testBoard.isInCheck(color)) {
			putsInCheck = true;
			System.out.println("This move puts " + color.toString().toLowerCase() + " in check, so");
		}
		return allowed && !putsInCheck;
	}

	// checks if the move to loc is included in the Set of locations 
	protected boolean includes(Set<Location> set, Location loc) {
		for(Location maybe : set) {
			if(maybe.equals(loc))
				return true;
		}
		return false;
	}

	public void moveTo(Location loc) {
		if(board.get(getLoc()) != this)
			throw new IllegalStateException("SOMETHING IS VERY WRONG");
		board.remove(location);
		Piece capture = board.remove(loc);
		if(capture != null)
			capture.getsCaptured();
		board.put(loc, this);
	}

}