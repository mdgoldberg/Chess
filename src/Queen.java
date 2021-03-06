import java.util.*;

public class Queen extends Piece {

	private static final long serialVersionUID = 1L;

	public Queen(Color c, Board b) {
		super(c, b);
	}
	
	public int getValue() { return 9; }

	public Set<Location> getPossibleMoves() {
		Set<Location> ret = new HashSet<Location>();
		Location currLoc;
		Set<Location> inWay = getBoard().getPiecesOfColor(getColor());

		// first traverse up-right
		currLoc = getLoc().getAdjacentLocation(Location.NORTHEAST);
		while(!includes(inWay, currLoc) && getBoard().isValid(currLoc)) {
			ret.add(currLoc);
			if(getBoard().get(currLoc) != null && getBoard().get(currLoc).getColor() != getColor())
				break;
			currLoc = currLoc.getAdjacentLocation(Location.NORTHEAST);
		}
		// up-left
		currLoc = getLoc().getAdjacentLocation(Location.NORTHWEST);
		while(!includes(inWay, currLoc) && getBoard().isValid(currLoc)) {
			ret.add(currLoc);
			if(getBoard().get(currLoc) != null && getBoard().get(currLoc).getColor() != getColor())
				break;
			currLoc = currLoc.getAdjacentLocation(Location.NORTHWEST);
		}
		// down-right
		currLoc = getLoc().getAdjacentLocation(Location.SOUTHEAST);
		while(!includes(inWay, currLoc) && getBoard().isValid(currLoc)) {
			ret.add(currLoc);
			if(getBoard().get(currLoc) != null && getBoard().get(currLoc).getColor() != getColor())
				break;
			currLoc = currLoc.getAdjacentLocation(Location.SOUTHEAST);
		}
		// down-left
		currLoc = getLoc().getAdjacentLocation(Location.SOUTHWEST);
		while(!includes(inWay, currLoc) && getBoard().isValid(currLoc)) {
			ret.add(currLoc);
			if(getBoard().get(currLoc) != null && getBoard().get(currLoc).getColor() != getColor())
				break;
			currLoc = currLoc.getAdjacentLocation(Location.SOUTHWEST);
		}
		// up
		currLoc = getLoc().getAdjacentLocation(Location.NORTH); 
		while(!includes(inWay, currLoc) && getBoard().isValid(currLoc)) {
			ret.add(currLoc);
			if(getBoard().get(currLoc) != null && getBoard().get(currLoc).getColor() != getColor())
				break;
			currLoc = currLoc.getAdjacentLocation(Location.NORTH);
		}
		// left
		currLoc = getLoc().getAdjacentLocation(Location.WEST);
		while(!includes(inWay, currLoc) && getBoard().isValid(currLoc)) {
			ret.add(currLoc);
			if(getBoard().get(currLoc) != null && getBoard().get(currLoc).getColor() != getColor())
				break;
			currLoc = currLoc.getAdjacentLocation(Location.WEST);
		}
		// down
		currLoc = getLoc().getAdjacentLocation(Location.SOUTH);
		while(!includes(inWay, currLoc) && getBoard().isValid(currLoc)) {
			ret.add(currLoc);
			if(getBoard().get(currLoc) != null && getBoard().get(currLoc).getColor() != getColor())
				break;
			currLoc = currLoc.getAdjacentLocation(Location.SOUTH);
		}
		// right
		currLoc = getLoc().getAdjacentLocation(Location.EAST);
		while(!includes(inWay, currLoc) && getBoard().isValid(currLoc)) {
			ret.add(currLoc);
			if(getBoard().get(currLoc) != null && getBoard().get(currLoc).getColor() != getColor())
				break;
			currLoc = currLoc.getAdjacentLocation(Location.EAST);
		}
		return ret;
	}

	public String toString() {
		if(getColor() == Color.WHITE)
			return "Q";
		else
			return "q";

	}

}
