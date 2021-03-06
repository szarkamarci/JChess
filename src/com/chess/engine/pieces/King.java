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

public class King extends Piece{
    private final static int[] CANDIDATE_MOVE_COORDINATES = { 9, 8, 7, 1, -1, -7, -8, -9 };

    public King(final Color pieceColor, final int piecePosition) {
        super(PieceType.KING, piecePosition, pieceColor);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();



        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){

            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isEightColumnExclusion(this.piecePosition, currentCandidateOffset)){
                continue;
            }
            if(BoardValues.isValidTileCoordinate(candidateDestinationCoordinate)){
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied()){ // is there anything on the desired destination?
                    legalMoves.add(new Move.MajorMove(board,this, candidateDestinationCoordinate));//where to be moved
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Color pieceColor = pieceAtDestination.getPieceAlliance();

                    if (this.pieceColor != pieceColor) { // if its occupied, is it the same colour? if not then...
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));//if its not the same colour
                    }
                }
            }

        }
        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public King movePiece(final Move move) {
        return new King(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    @Override
    public String toString(){
        return PieceType.KING.toString();
        }
        private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardValues.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 ||
                candidateOffset == 7);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardValues.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 ||
                candidateOffset == 9);
}
}
