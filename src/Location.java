import java.io.Serializable;


public class Location implements Serializable {

	private static final long serialVersionUID = 1L;
	private int row;
	private int col;

	public Location(int r, int c) {
		row = r; col = c;
	}

	public int getRow() { return row; }
	public int getCol() { return col; }

	public Location getAdjacentLocation(double dir) {
		int dx = (int) Math.round(Math.cos(dir));
		int dy = (int) (-1*Math.round(Math.sin(dir)));
		return new Location(row+dy, col+dx);
	}

	public boolean equals(Location other) {
		return row == other.row && col == other.col;
	}

	// problem - assumes not flipped
	public String algNot(Color color) {
		int rank = -1;
		char file = '=';
		switch(color) {
		case WHITE:
			rank = 8-row;
			file = (char)('a' + col);
			break;
		case BLACK:
			rank = 1+row;
			file = (char) ('h'-col);
			break;
		}
		return "" + file + rank;
	}

	public String toString() {
		return "(" + row + "," + col + ")";
	}

	public static final double NORTH = Math.PI/2;
	public static final double NORTHEAST = Math.PI/4;
	public static final double EAST = 0;
	public static final double SOUTHEAST = 7*Math.PI/4;
	public static final double SOUTH = 3*Math.PI/2;
	public static final double SOUTHWEST = 5*Math.PI/4;
	public static final double WEST = Math.PI;
	public static final double NORTHWEST = 3*Math.PI/4;


}
