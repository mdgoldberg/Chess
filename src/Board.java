import java.io.*;
import java.util.*;

public class Board implements Serializable {
  
  private static final long serialVersionUID = 1L;
  private Piece[][] board;
  private List<Piece> capturedWhitePieces;
  private List<Piece> capturedBlackPieces;
  private boolean flipped;
  private int numPlies;
  private Color whosTurn;
  
  // creates a deep copy of the board
  public Board(Board b) {
    board = new Piece[8][8];
    whosTurn = b.whosTurn;
    for(int r = 0; r < 8; r++) {
      for(int c = 0; c < 8; c++) {
        if(b.board[r][c] instanceof Pawn)
          add(r,c, new Pawn(b.board[r][c].getColor(), this));
        if(b.board[r][c] instanceof Knight)
          add(r,c, new Knight(b.board[r][c].getColor(), this));
        if(b.board[r][c] instanceof Rook)
          add(r,c, new Rook(b.board[r][c].getColor(), this));
        if(b.board[r][c] instanceof Bishop)
          add(r,c, new Bishop(b.board[r][c].getColor(), this));
        if(b.board[r][c] instanceof Queen)
          add(r,c, new Queen(b.board[r][c].getColor(), this));
        if(b.board[r][c] instanceof King)
          add(r,c, new King(b.board[r][c].getColor(), this));
      }
    }
    capturedWhitePieces = new ArrayList<Piece>();
    for(Piece p : b.capturedWhitePieces)
      capturedWhitePieces.add(p);
    capturedBlackPieces = new ArrayList<Piece>();
    for(Piece p : b.capturedBlackPieces)
      capturedBlackPieces.add(p);
    flipped = b.flipped;
    numPlies = b.numPlies;
  }
  
  public Board() { 
    board = new Piece[8][8];
    whosTurn = Color.WHITE;
    capturedWhitePieces = new ArrayList<Piece>();
    capturedBlackPieces = new ArrayList<Piece>();
    numPlies = 0;
    flipped = false;
    // pawns
    for(int c = 0; c < 8; c++) {
      add(1, c, new Pawn(Color.BLACK, this));
      add(6, c, new Pawn(Color.WHITE, this));
    }
    // rooks
    add(0,0, new Rook(Color.BLACK, this));
    add(0,7, new Rook(Color.BLACK, this));
    add(7,0, new Rook(Color.WHITE, this));
    add(7,7, new Rook(Color.WHITE, this));
    // knights
    add(0,1, new Knight(Color.BLACK, this));
    add(0,6, new Knight(Color.BLACK, this));
    add(7,1, new Knight(Color.WHITE, this));
    add(7,6, new Knight(Color.WHITE, this));
    // bishops
    add(0, 2, new Bishop(Color.BLACK, this));
    add(0, 5, new Bishop(Color.BLACK, this));
    add(7, 2, new Bishop(Color.WHITE, this));
    add(7, 5, new Bishop(Color.WHITE, this));
    // queens
    add(0,3, new Queen(Color.BLACK, this));
    add(7,3, new Queen(Color.WHITE, this));
    // kings
    add(0,4, new King(Color.BLACK, this));
    add(7,4, new King(Color.WHITE, this));
  }
  
  public void flipped() { flipped = !flipped; }
  public boolean isFlipped() { return flipped; }
  public int getNumPlies() { return numPlies; }
  public void incrementPlies() { numPlies++; }
  public Color whosTurn() { return whosTurn; }
  
  // switches whosTurn & flips board
  public void endTurn() {
    whosTurn = whosTurn == Color.WHITE ? Color.BLACK : Color.WHITE;
    flipBoard();
  }
  
  public void add(int r, int c, Piece p) {
    board[r][c] = p;
    p.setLoc(new Location(r,c));
  }
  
  // flips board; must do so after every turn
  private void flipBoard() {
    for(int r = 0; r < 8; r++) {
      for(int c = 0; c < 4; c++) {
        // swap (r,c) with (7-r, 7-c)
        Location loc = new Location(r,c);
        Location otherLoc = new Location(7-r, 7-c);
        Piece swap = get(otherLoc);
        put(otherLoc, get(loc));
        put(loc, swap);
      }
    }
    flipped();
  }
  
  public void addCaptured(Piece p) {
    switch(p.getColor()) {
      case WHITE:
        capturedWhitePieces.add(p);
        break;
      case BLACK:
        capturedBlackPieces.add(p);
        break;
    }
  }
  
  public Piece get(Location loc) {
    if(!isValid(loc))
      throw new IllegalArgumentException("Invalid location");
    return board[loc.getRow()][loc.getCol()];
  }
  
  public Piece put(Location loc, Piece p) {
    if(!isValid(loc))
      throw new IllegalArgumentException("Invalid location");
    Piece old = this.get(loc);
    this.board[loc.getRow()][loc.getCol()] = p;
    if(p != null)
      p.setLoc(loc);
    return old;
  }
  
  public Piece remove(Location loc) {
    return put(loc, null);
  }
  
  public boolean isValid(Location loc) {
    return 0 <= loc.getRow() && loc.getRow() < 8 && 0 <= loc.getCol() && loc.getCol() < 8;
  }
  
  public boolean isVacant(Location loc) {
    if(!isValid(loc))
      return false;
    return get(loc) == null;
  }
  
  public Set<Location> getPiecesOfColor(Color col) {
    Set<Location> ret = new HashSet<Location>(16);
    for(int r = 0; r < 8; r++) {
      for(int c = 0; c < 8; c++) {
        if(board[r][c] != null && board[r][c].getColor() == col)
          ret.add(new Location(r,c));
      }
    }
    return ret;
  }
  
  // returns two-cell Location array: first is location of piece moving, second is destination
  public Location[] parseMove(String rawStr, Color whosTurn) {
    String str = rawStr.toLowerCase();
    if(str.split(" ").length != 3)
      throw new NumberFormatException("Improper Move Format");
    Location[] ret = new Location[2];
    Scanner sc = new Scanner(str);
    String first = sc.next();
    if(first.length() != 2 || first.charAt(0) < 'a' || first.charAt(0) > 'h' || first.charAt(1) < '1' || first.charAt(1) > '8')
      throw new NumberFormatException("Improper Move Format");
    sc.next();
    String second = sc.next();
    if(second.length() != 2 || second.charAt(0) < 'a' || second.charAt(0) > 'h' || second.charAt(1) < '1' || second.charAt(1) > '8')
      throw new NumberFormatException("Improper Move Format");
    if(!flipped)
      ret[0] = new Location(8-Integer.parseInt(Character.toString(first.charAt(1))), (int)(first.charAt(0)-'a'));
    else
      ret[0] = new Location(Integer.parseInt(Character.toString(first.charAt(1)))-1, (int)('h'-first.charAt(0)));
    if(get(ret[0]) == null || get(ret[0]).getColor() != whosTurn)
      throw new IllegalArgumentException(whosTurn + " has no piece at " + ret[0].algNot(whosTurn));
    if(!flipped)
      ret[1] = new Location(8-Integer.parseInt(Character.toString(second.charAt(1))), (int)(second.charAt(0)-'a'));
    else
      ret[1] = new Location(Integer.parseInt(Character.toString(second.charAt(1)))-1, (int)('h'-second.charAt(0)));
    Piece piece = get(ret[0]);
    if(!piece.validMove(ret[1]))
      throw new IllegalArgumentException(rawStr + " is an illegal move");
    return ret;
  }
  
  public void printBoard() {
    System.out.print("   ");
    for(int c = 0; c < 8; c++) {
      if(flipped)
        System.out.print((char)('h'-c) + " ");
      else
        System.out.print((char)(c + 'a') + " ");
    }
    System.out.println();
    System.out.println("  _________________  ");
    for(int r = 0; r < 8; r++) {
      if(flipped)
        System.out.print("" + (1+r) + " |");
      else
        System.out.print("" + (8-r) + " |");
      for(int c = 0; c < 8; c++) {
        Location loc = new Location(r,c);
        if(get(loc) == null)
          System.out.print("_|");
        else
          System.out.print(get(loc) + "|");
      }
      if(flipped)
        System.out.println(" " + (1+r));
      else
        System.out.println(" " + (8-r));
    }
    System.out.println("  -----------------  ");
    System.out.print("   ");
    for(int c = 0; c < 8; c++) {
      if(flipped) {
        System.out.print((char)('h'-c) + " ");
      } else
        System.out.print((char)(c + 'a') + " ");
    }
    System.out.println();
    System.out.println("Captured White Pieces: " + capturedWhitePieces);
    System.out.println("Captured Black Pieces: " + capturedBlackPieces);
  }
  
  public boolean isInCheck(Color color) {
    boolean isInCheck = false;
    Color oppColor = (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    Set<Location> oppPieces = getPiecesOfColor(oppColor);
    Set<Location> pieces = getPiecesOfColor(color);
    Piece king = null;
    for(Location l : pieces) {
      if(get(l) instanceof King)
        king = get(l);
    }
    Location kingLoc = king.getLoc();
    for(Location l : oppPieces) {
      Piece oppPiece = get(l);
      if(!(oppPiece instanceof King)) {
        Set<Location> moves = oppPiece.getPossibleMoves();
        for(Location moveLoc : moves) {
          if(moveLoc.equals(kingLoc))
            isInCheck = true;
        }
      }
    }
    return isInCheck;
  }
  
  public boolean isInCheckMate(Color color) {
    boolean isCheckMate = true;
    boolean beginsInCheck = isInCheck(color);
    Board testBoard = new Board(this);
    if(!beginsInCheck)
      return false;
    Set<Location> pieceLocations = testBoard.getPiecesOfColor(color);
    Set<Piece> pieces = new HashSet<Piece>(16);
    for(Location l : pieceLocations)
      pieces.add(testBoard.get(l));
    for(Piece piece : pieces) {
      Set<Location> possMoves = piece.getPossibleMoves();
      for(Location testLoc : possMoves) {
        // save state before move
        Piece cappedPiece = testBoard.get(testLoc);
        Location initLoc = piece.getLoc();
        // try the move - using remove & put instead of moveTo to avoid errors - I know it will be valid
        testBoard.remove(initLoc);
        testBoard.put(testLoc, piece);
        // if it got them out of check on the test board...
        if(!testBoard.isInCheck(color))
          return false;
        // otherwise, put everything back the way it was and we'll try the next move
        else {
          testBoard.remove(testLoc);
          testBoard.put(initLoc, piece);
          if(cappedPiece != null)
            testBoard.put(testLoc, cappedPiece);
        }
      }
    }
    return isCheckMate;
  }
  
  // TODO : AI!
  
  
  
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    Scanner kb = new Scanner(System.in);
    String in;
    String fileName = "";
    Board board = null;
    do {
      System.out.println("SAVED game or NEW Game? (All caps words for options)");
      in = kb.nextLine();
      if(in.toLowerCase().equals("saved")) {
        File saveNamesFile = new File("savenames.txt");
        Scanner saveNames = new Scanner(saveNamesFile);
        String saveFileNames = "";
        System.out.println("Save files:");
        // print file names with this while loop
        while(saveNames.hasNext()) {
          String n = saveNames.nextLine();
          saveFileNames += (n + "\n");
          System.out.println(n);
        }
        // load save files
        FileInputStream fis = null;
        do {
          System.out.println("Name of save file?");
          fileName = kb.nextLine();
          try {
            fis = new FileInputStream(fileName + ".dat");
          } catch(FileNotFoundException fnfe) {
            System.out.println("File not found. Please try again.");
            System.out.println("Save files:");
            System.out.print(saveFileNames);
          }
        } while(fis == null);
        ObjectInputStream ois = new ObjectInputStream(fis);
        board = (Board) ois.readObject();
        ois.close();
      }
      else if(in.toLowerCase().equals("new")) {
        board = new Board();
      }
    } while(board == null);
    
    boolean gameOver = false;
    Location[] move;
    boolean toSave = false;
    
    System.out.println("Directions: Enter moves in algebraic notation (e.g., \"e2 to e4\")");
    System.out.println("In the board representation, white pieces are capitalized, black pieces are lower case");
    System.out.println("To save the game, type \"SAVE\" instead of your next move");
    System.out.println("To end the game without saving, type \"END GAME\"");
    gameplay:
      while(!gameOver) {
      
      if(board.getNumPlies()%3 == 0 && board.getNumPlies() > 0)
        System.out.println("To save the game, type \"SAVE\" instead of your next move");
      if(board.getNumPlies()%5 == 0 && board.getNumPlies() > 0)
        System.out.println("To end the game without saving, type \"END GAME\"");
      
      toSave = false;
      
      if(board.isInCheck(board.whosTurn())) {
        if(board.isInCheckMate(board.whosTurn())) {
          System.out.println(board.whosTurn() + " loses!");
          Color winningColor = (board.whosTurn() == Color.WHITE) ? Color.BLACK : Color.WHITE;
          System.out.println(winningColor + " wins!");
          board.printBoard();
          gameOver = true;
          break gameplay;
        }
        else
          System.out.println(board.whosTurn() + " is in check");
      }
      
      move = null;
      // get player's move
      do {
        board.printBoard();
        System.out.println(board.whosTurn() + " to move:");
        String rawMove = kb.nextLine();
        if(rawMove.toLowerCase().equals("save")) {
          toSave = true;
          break gameplay;
        }
        if(rawMove.toLowerCase().equals("end game") || rawMove.toLowerCase().equals("endgame") ||
           rawMove.toLowerCase().equals("end")) {
          if(in.toLowerCase().equals("saved")) {
            File fileToDel = new File(fileName + ".dat");
            fileToDel.delete();
            // delete from savenames.txt
            String saveNamesString = "";
            File sn = new File("savenames.txt");
            Scanner sc = new Scanner(sn);
            while(sc.hasNext()) {
              String line = sc.nextLine();
              if(!line.equals(fileName))
                saveNamesString += (line + "\n");
            }
            sc = new Scanner(saveNamesString);
            FileWriter fw = new FileWriter(sn);
            while(sc.hasNext())
              fw.write(sc.nextLine());
            fw.close();
          }
          break gameplay;
        }
        try {
          move = board.parseMove(rawMove, board.whosTurn());
        } catch(NumberFormatException e) {
          System.out.println(e.getMessage());
          System.out.println("Please try again using notation \"e2 to e4\"");
        } catch(IllegalArgumentException e) {
          System.out.println(e.getMessage());
          System.out.println("Please try again with a legal move");
        }
      } while(move == null);
      
      Piece piece = board.get(move[0]);
      piece.moveTo(move[1]);
      
      // move rook if castling
      Piece rook;
      if(piece instanceof King && Math.abs(move[1].getCol() - move[0].getCol()) == 2) {
        if(move[1].getCol() - move[0].getCol() == 2) {
          rook = board.get(new Location(7,7));
          board.remove(rook.getLoc());
          board.put(piece.getLoc().getAdjacentLocation(Location.WEST), rook);
        } else {
          rook = board.get(new Location(7,0));
          board.remove(rook.getLoc());
          board.put(piece.getLoc().getAdjacentLocation(Location.EAST), rook);
        }
      }
      
      // promotion of pawns
      if(piece instanceof Pawn && move[1].getRow() == 0) {
        String type;
        boolean done;
        do {
          done = true;
          System.out.println("Promote pawn to queen, bishop, knight, or rook?");
          type = kb.nextLine();
          type = type.toLowerCase();
          if(type.equals("bishop"))
            board.add(piece.getLoc().getRow(), piece.getLoc().getCol(), new Bishop(board.whosTurn(), board));
          else if(type.equals("knight"))
            board.add(piece.getLoc().getRow(), piece.getLoc().getCol(), new Knight(board.whosTurn(), board));
          else if(type.equals("queen"))
            board.add(piece.getLoc().getRow(), piece.getLoc().getCol(), new Queen(board.whosTurn(), board));
          else if(type.equals("rook"))
            board.add(piece.getLoc().getRow(), piece.getLoc().getCol(), new Rook(board.whosTurn(), board));
          else {
            System.out.println("Invalid answer. Please try again");
            done = false;
          }
        } while(!done);
      }
      
      // other player's turn, flip board
      board.endTurn();
      board.incrementPlies();
    }
      if(toSave) {
        System.out.println("Name the save file:");
        fileName = kb.nextLine();
        FileOutputStream gameFileOutput = new FileOutputStream(fileName + ".dat");
        ObjectOutputStream gameObjectOutput = new ObjectOutputStream(gameFileOutput);
        Scanner saveNameReader = new Scanner(new File("savenames.txt"));
        String savedNames = "";
        // copy over all the filenames to placeholder string
        while(saveNameReader.hasNext())
          savedNames += (saveNameReader.nextLine() + "\n");
        // unnecessary to do this all if it's just overwriting a file
        if(!savedNames.contains(fileName)) {
          savedNames += fileName; // adding filename
          FileWriter saveNameWriter = new FileWriter(new File("savenames.txt"));
          // add file names back to savenames.txt
          Scanner saveNames = new Scanner(savedNames);
          while(saveNames.hasNext())
            saveNameWriter.write(saveNames.nextLine() + "\n");
          saveNameWriter.close();
        }
        // write board object to filename.dat
        gameObjectOutput.writeObject(board);
        gameObjectOutput.close();
      }
  }
}
