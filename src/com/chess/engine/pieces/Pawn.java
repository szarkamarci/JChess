package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardValues;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.*;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece {

	private final static int[] CANDIDATE_MOVE_COORDINATE = {8,16,7,9};

	public Pawn(final Color pieceColor,
			    final int piecePosition) {
		super(PieceType.PAWN, piecePosition, pieceColor);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {

		final List<Move> legalMoves = new ArrayList<>();

		for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE ) {// Go thru legal moves
			final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() *currentCandidateOffset);
			if(!BoardValues.isValidTileCoordinate(candidateDestinationCoordinate)) { // is it a legal move??
				continue;
			}
			if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
				legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
			}
			//checking pawn specified moves possibilities
			else if(currentCandidateOffset == 16 && this.isFirstMove() &&
				   ((BoardValues.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
				   (BoardValues.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite()))) {
				final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceColor.getDirection() * 8);
				if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
					!board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
				}
			}
			//checking pawn specified moves possibilities
			else if(currentCandidateOffset == 7 &&
					!(BoardValues.EIGHTH_COLUMN[this.piecePosition] && this.pieceColor.isWhite() ||
					BoardValues.FIRST_COLUMN[this.piecePosition] && this.pieceColor.isBlack())) {
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) { // is there anything on the desired destination?
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					if(this.pieceColor != pieceOnCandidate.getPieceAlliance()) {
						legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
					}
				} else if(board.getEnPassantPawn() != null) {
					if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceColor.getFacingDirection()))) {
						final Piece pieceOnCandidate = board.getEnPassantPawn();
						if (this.pieceColor != pieceOnCandidate.getPieceAlliance()) {
							legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                     }
                 }
				}
				}

			else if(currentCandidateOffset == 9 &&
					!(BoardValues.EIGHTH_COLUMN[this.piecePosition] && this.pieceColor.isBlack() ||
					BoardValues.FIRST_COLUMN[this.piecePosition] && this.pieceColor.isWhite())) {
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					if(this.pieceColor != pieceOnCandidate.getPieceAlliance()) {
						legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
					}
				} else if(board.getEnPassantPawn() != null) {
					if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceColor.getFacingDirection()))) {
						final Piece pieceOnCandidate = board.getEnPassantPawn();
						if (this.pieceColor != pieceOnCandidate.getPieceAlliance()) {
							legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                     }
					}
                 }
			}
		}

			return ImmutableList.copyOf(legalMoves);

		}

	@Override
	public Pawn movePiece(final Move move) {
		return new Pawn (move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
	}

	@Override
	public String toString() {
		return PieceType.PAWN.toString();
	}
	}
