package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.move.Move;

import java.util.Collection;

public abstract class Piece {

    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final PieceType pieceType;
    private final boolean isFirstMove;
    private final int cachedHashCode;

    Piece(final int piecePosition,
          final Alliance pieceAlliance,
          final PieceType pieceType,
          final boolean isFirstMove) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.pieceType = pieceType;
        this.cachedHashCode = computeHashCode();
        this.isFirstMove = isFirstMove;
    }

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        else if (other  instanceof Piece) {
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return (piecePosition ==  otherPiece.getPiecePosition() &&
                pieceAlliance == otherPiece.getPieceAlliance() &&
                pieceType == otherPiece.getPieceType() &&
                isFirstMove == otherPiece.isFirstMove());
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public boolean isKing() {
        return (pieceType == PieceType.KING);
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    @Override
    public String toString() {
        return pieceType.toString();
    }

    public abstract Piece movePiece(final Move move);

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    public enum PieceType {
        PAWN("P", 100),
        KNIGHT("N", 300),
        BISHOP("B", 300),
        ROOK("R", 500),
        QUEEN("Q", 900),
        KING("K", 10000);

        private String pieceName;
        private int pieceValue;

        PieceType(final String pieceName, final int pieceValue) {
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        private int getPieceValue() {
            return this.pieceValue;
        }
    }
}
