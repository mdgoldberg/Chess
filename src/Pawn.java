import java.util.*;

public class Pawn extends Piece {

	private static final long serialVersionUID = 1L;
	private boolean canBeEPd;
	private int numSteps;

	public Pawn(Color c, Board b) {
		super(c, b);
		canBeEPd = false;
		numSteps = 0;
	}

	public boolean canBeEPd() { return canBeEPd; }

	// works besides en passant
	public Set<Location> getPossibleMoves() {
		Board b = getBoard();
		Set<Location> ret = new HashSet<Location>();
		Location front = getLoc().getAdjacentLocation(Location.NORTH);
		Location attLeft = getLoc().getAdjacentLocation(Location.NORTHWEST);
		Location attRight = getLoc().getAdjacentLocation(Location.NORTHEAST);
		Location twiceFront = front.getAdjacentLocation(Location.NORTH);
		Location ePLeft = getLoc().getAdjacentLocation(Location.EAST);
		Location ePRight = getLoc().getAdjacentLocation(Location.WEST); 
		if(b.isValid(front) && b.isVacant(front))
			ret.add(front);
		if(numSteps == 0 && b.isVacant(front) && b.isVacant(twiceFront))
			ret.add(twiceFront);
		if(b.isValid(attLeft) && !b.isVacant(attLeft) && b.get(attLeft).getColor() != getColor())
			ret.add(attLeft);
		if(b.isValid(attRight) && !b.isVacant(attRight) && b.get(attRight).getColor() != getColor())
			ret.add(attRight);
		// en passant
		if(b.isValid(ePLeft) && b.get(ePLeft) instanceof Pawn && b.get(ePLeft).getColor() != getColor()
				&& ((Pawn)b.get(ePLeft)).canBeEPd())
			ret.add(attRight);
		if(b.isValid(ePRight) && b.get(ePRight) instanceof Pawn && b.get(ePRight).getColor() != getColor()
				&& ((Pawn)b.get(ePRight)).canBeEPd())
			ret.add(attLeft);
		return ret;
	}
	
	public int getValue() { return 1; }

	public void moveTo(Location loc) {
		Board board = getBoard();
		canBeEPd = false;
		if((getLoc().getRow() == 6 && loc.getRow() == 4) || (getLoc().getRow() == 1 && loc.getRow() == 3))
			canBeEPd = true;
		if(board.get(getLoc()) != this)
			throw new IllegalStateException("SOMETHING IS VERY WRONG");
		board.remove(getLoc());
		Piece capture;
		// if is en passant capture
		if(board.get(loc.getAdjacentLocation(Location.SOUTH)) instanceof Pawn &&
				((Pawn)board.get(loc.getAdjacentLocation(Location.SOUTH))).canBeEPd())
			capture = board.remove(loc.getAdjacentLocation(Location.SOUTH));
		else
			capture = board.remove(loc);
		if(capture != null)
			capture.getsCaptured();
		board.put(loc, this);
		numSteps++;
	}

	public String toString() {
		if(getColor() == Color.WHITE)
			return "P";
		else
			return "p";
	}

}
