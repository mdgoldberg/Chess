import java.util.*;

public class Knight extends Piece {

	private static final long serialVersionUID = 1L;

	public Knight(Color c, Board b) {
		super(c, b);
	}

	public int getValue() { return 3; }
	
	public Set<Location> getPossibleMoves() {
		Set<Location> ret = new HashSet<Location>(8);
		int[] dx = {-2,-1,1,2};
		int[] dy = {-2,-1,1,2};
		for(int x : dx) {
			for(int y : dy) {
				if(Math.abs(x) + Math.abs(y) == 3) {
					Location testLoc = new Location(getLoc().getRow()+y, getLoc().getCol()+x);
					if(getBoard().isValid(testLoc)) {
						Piece testPiece = getBoard().get(testLoc);
						// if empty cell or piece of other color (for attack)
						if(testPiece == null || testPiece.getColor() != getColor())
							ret.add(testLoc);
					}
				}
			}
		}
		return ret;
	}

	public String toString() {
		if(getColor() == Color.WHITE)
			return "N";
		else
			return "n";
	}

}
