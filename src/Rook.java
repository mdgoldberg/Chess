import java.util.*;

public class Rook extends Piece {
  
  private static final long serialVersionUID = 1L;
  // for castling
  private int numSteps;
  
  public Rook(Color c, Board b) {
    super(c, b);
    numSteps = 0;
  }
  
  public int getNumSteps() { return numSteps; }
  
  public int getValue() { return 5; }
  
  public Set<Location> getPossibleMoves() {
    
    Set<Location> ret = new HashSet<Location>();
    Location currLoc;
    Set<Location> inWay = getBoard().getPiecesOfColor(getColor());
    
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
  
  public void moveTo(Location loc) {
    super.moveTo(loc);
    numSteps++;
  }
  
  public String toString() {
    if(getColor() == Color.WHITE)
      return "R";
    else
      return "r";
  }
  
}
