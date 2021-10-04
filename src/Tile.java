public class Tile {

    int tileCoordinate;

    //constructor
    Tile(int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    public static final class EmptyTile extends Tile{

        EmptyTile(int coordinate){
            super(coordinate);
        }

        public boolean isTileOccupied() {
            return false;
        }

        public Piece getPiece() {
            return null;
        }
    }

    public static final class OccupiedTile extends Tile{

        Piece pieceOnTile;

        OccupiedTile(int tileCoordinate, Piece pieceOnTile){
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




