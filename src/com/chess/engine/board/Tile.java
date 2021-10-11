
import com.chess.engine.pieces.Piece;

public class Tile {

    protected final int tileCoordinate;

    //constructor
    private Tile(int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    //EmptyTile Class
    public static final class EmptyTile extends Tile{

        //constructor
       private EmptyTile(final int coordinate){
            super(coordinate);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }
    }

    //OccupiedTile Class
    public static final class OccupiedTile extends Tile{

        private final Piece pieceOnTile;

        //constructor
        private OccupiedTile(int tileCoordinate, Piece pieceOnTile){
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        public boolean isTileOccupied(){
            return true;
        }

        public Piece getPiece(){
            return pieceOnTile;
        }
    }

}




