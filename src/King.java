import java.util.*;

public class King extends Piece {

	private static final long serialVersionUID = 1L;
	// needed for castling
	private int numSteps;

	public King(Color c, Board b) {
		super(c, b);
		numSteps = 0;
	}
	
	public int getValue() { return Integer.MAX_VALUE; }

	public Set<Location> getPossibleMoves() {
		Set<Location> ret = new HashSet<Location>(10);
		for(int r = -1; r <= 1; r++) {
			for(int c = -1; c <= 1; c++) {
				Location testLoc = new Location(getLoc().getRow()+r,getLoc().getCol()+c);
				if(getBoard().isValid(testLoc)) {
					Piece testPiece = getBoard().get(testLoc);
					if(!testLoc.equals(getLoc()) && (testPiece == null || testPiece.getColor() != getColor()))
						ret.add(testLoc);
				}
			}
		}
		// castling
		// king can't have moved and can't castle out of check
		if(numSteps == 0 && !getBoard().isInCheck(getColor())) {
			Location[] backRow = new Location[8];
			for(int c = 0; c < 8; c++)
				backRow[c] = new Location(7,c);
			switch(getColor()) {
			case WHITE:
				// kingside
				if(getBoard().get(backRow[7]) instanceof Rook && ((Rook)getBoard().get(backRow[7])).getNumSteps() == 0) {
					// two spots in between need to be vacant
					if(getBoard().isVacant(backRow[5]) && getBoard().isVacant(backRow[6])) {
						// can't castle through check
						Board maybeBoard = new Board(getBoard());
						Piece maybeKing = maybeBoard.get(getLoc());
						maybeBoard.remove(getLoc());
						maybeBoard.put(backRow[5], maybeKing);
						if(!maybeBoard.isInCheck(getColor()))
							ret.add(backRow[6]);
					}
				}
				// queenside
				if(getBoard().get(backRow[0]) instanceof Rook && ((Rook)getBoard().get(backRow[0])).getNumSteps() == 0) {
					// three spots need to be vacant (those between king and rook)
					if(getBoard().isVacant(backRow[1]) && getBoard().isVacant(backRow[2]) && getBoard().isVacant(backRow[3])) {
						// can't castle out of check
						Board maybeBoard = new Board(getBoard());
						Piece maybeKing = maybeBoard.get(getLoc());
						maybeKing.moveTo(getLoc().getAdjacentLocation(Location.WEST));
						if(!maybeBoard.isInCheck(getColor()))
							ret.add(backRow[2]);
					}
				}
				break;
			case BLACK:
				// kingside
				if(getBoard().get(backRow[0]) instanceof Rook && ((Rook)getBoard().get(backRow[0])).getNumSteps() == 0) {
					// two spots in between need to be vacant
					if(getBoard().isVacant(backRow[1]) && getBoard().isVacant(backRow[2])) {
						// can't castle through check
						Board maybeBoard = new Board(getBoard());
						Piece maybeKing = maybeBoard.get(getLoc());
						maybeBoard.remove(getLoc());
						maybeBoard.put(backRow[2], maybeKing);
						if(!maybeBoard.isInCheck(getColor()))
							ret.add(backRow[1]);
					}
				}
				// queenside
				if(getBoard().get(backRow[7]) instanceof Rook && ((Rook)getBoard().get(backRow[7])).getNumSteps() == 0) {
					// three spots need to be vacant (those between king and rook)
					if(getBoard().isVacant(backRow[4]) && getBoard().isVacant(backRow[5]) && getBoard().isVacant(backRow[6])) {
						// can't castle out of check
						Board maybeBoard = new Board(getBoard());
						Piece maybeKing = maybeBoard.get(getLoc());
						maybeBoard.remove(getLoc());
						maybeBoard.put(backRow[4], maybeKing);
						if(!maybeBoard.isInCheck(getColor()))
							ret.add(backRow[5]);
					}
				}
			}
		}
		return ret;
	}

	public void moveTo(Location loc) {
		super.moveTo(loc);
		numSteps++;
	}

	public String toString() { 
		if(getColor() == Color.WHITE)
			return "K";
		else
			return "k";
	}

}
