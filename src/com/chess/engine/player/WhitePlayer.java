package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.move.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.move.Move.*;

public class WhitePlayer extends Player {

    public WhitePlayer(final Board board,
                       final Collection<Move> legalMoves,
                       final Collection<Move> opponentLegalMoves) {
        super(board, legalMoves, opponentLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
                                                    final Collection<Move> opponentsLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        if (this.getPlayerKing().isFirstMove() && !this.isInCheck()) {
            // King-side castle move
            if (!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
                if (Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() &&
                    Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty()) {
                    final Tile rookTile = this.board.getTile(63);

                    if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                        kingCastles.add(new KingSideCastleMove(this.board,
                                                                    this.playerKing,
                                                 62,
                                                                    (Rook) rookTile.getPiece(),
                                                                    rookTile.getTileCoordinate(),
                                                 61));
                    }
                }
        }
            // Queen-side castle move
            if (!this.board.getTile(59).isTileOccupied() &&
                !this.board.getTile(58).isTileOccupied() &&
                !this.board.getTile(57).isTileOccupied()) {
                if (Player.calculateAttacksOnTile(58, opponentsLegals).isEmpty() &&
                    Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty()) {
                    final Tile rookTile = this.board.getTile(56);
                    if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                        kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 58, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
                    }
                }
            }

        }
        return ImmutableList.copyOf(kingCastles);
    }
}
