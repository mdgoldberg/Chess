import java.io.Serializable;

public enum Color implements Serializable {
	BLACK, WHITE;

	public String toString() {
		if(this == WHITE)
			return "White";
		else
			return "Black";
	}
}