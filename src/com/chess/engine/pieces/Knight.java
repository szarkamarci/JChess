package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardValues;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES= {-17, -15, -10, -6, 6, 10, 15, 17}; //possible moves knight

    public Knight(Color pieceColor, int piecePosition) { //destructor
        super(PieceType.BISHOP,piecePosition, pieceColor);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {


        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES ) { // Go thru


        final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;// adding move

            if(BoardValues.isValidTileCoordinate(candidateDestinationCoordinate)) { //is it legal move?

                if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset)|| //possible legal moves
                isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isEigthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                    continue;
                }


                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied()){ // is there anything on the desired destination?
                    legalMoves.add(new Move.MajorMove(board,this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Color pieceColor = pieceAtDestination.getPieceAlliance();

                    if (this.pieceColor != pieceColor) { // if its occupied, is it the same color? if not then...
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
         return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
        }

    //knight rules if it is in the first column, second, seventh, eight
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardValues.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10 ||
                candidateOffset == 6 || candidateOffset == 15);
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardValues.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
}


    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset){
       return BoardValues.SEVENTH_COLUMN[currentPosition] && (candidateOffset == 10|| candidateOffset == -6);
}


    private static boolean isEigthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardValues.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -15|| (candidateOffset == -6 ||
                candidateOffset == 10) || candidateOffset ==17);
}
}
