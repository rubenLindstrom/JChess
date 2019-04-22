package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.move.Move;
import com.chess.engine.pieces.*;

import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

import static com.chess.engine.Alliance.*;

public class Board {

    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private final Pawn enPassantPawn;


    private Board(final Builder builder) {
        // Vi populerar vår gameBoard-list med emptyTiles och occupiedTiles
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, BLACK);

        this.enPassantPawn = builder.enPassantPawn;

        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, blackStandardLegalMoves, whiteStandardLegalMoves);
        this.currentPlayer = (builder.nextMoveMaker == WHITE) ? this.whitePlayer : this.blackPlayer;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i= 0; i < BoardUtils.NUM_TILES; i++) {
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if ((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public Player whitePlayer() {
        return this.whitePlayer;
    }

    public Player blackPlayer() {
        return this.blackPlayer;
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final Piece piece: pieces) {
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard,
                                                    final Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>();
        for(final Tile tile : gameBoard) {
            if (tile.isTileOccupied()) {
                final Piece piece = tile.getPiece();
                if (piece.getPieceAlliance() == alliance) {
                    activePieces.add(piece);
                }
            }
        }
        return Collections.unmodifiableList(activePieces);
    }

    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    private static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    public static Board createStandardBoard() {
        // Vi skapar en builder, med vilken vi konstruerar vårt bräde
        final Builder builder = new Builder();
        // Pjäserna placeras ut i standardposition
        builder.setPiece(new Rook(0, BLACK, true));
        builder.setPiece(new Knight(1, BLACK, true));
        builder.setPiece(new Bishop(2, BLACK, true));
        builder.setPiece(new Queen(3, BLACK, true));
        builder.setPiece(new King(4, BLACK, true));
        builder.setPiece(new Bishop(5, BLACK, true));
        builder.setPiece(new Knight(6, BLACK, true));
        builder.setPiece(new Rook(7, BLACK, true));
        builder.setPiece(new Pawn(8, BLACK, true));
        builder.setPiece(new Pawn(9, BLACK, true));
        builder.setPiece(new Pawn(10, BLACK, true));
        builder.setPiece(new Pawn(11, BLACK, true));
        builder.setPiece(new Pawn(12, BLACK, true));
        builder.setPiece(new Pawn(13, BLACK, true));
        builder.setPiece(new Pawn(14, BLACK, true));
        builder.setPiece(new Pawn(15, BLACK, true));
        // WHITE
        builder.setPiece(new Pawn(48, WHITE, true));
        builder.setPiece(new Pawn(49, WHITE, true));
        builder.setPiece(new Pawn(50, WHITE, true));
        builder.setPiece(new Pawn(51, WHITE, true));
        builder.setPiece(new Pawn(52, WHITE, true));
        builder.setPiece(new Pawn(53, WHITE, true));
        builder.setPiece(new Pawn(54, WHITE, true));
        builder.setPiece(new Pawn(55, WHITE, true));
        builder.setPiece(new Rook(56, WHITE, true));
        builder.setPiece(new Knight(57, WHITE, true));
        builder.setPiece(new Bishop(58, WHITE, true));
        builder.setPiece(new Queen(59, WHITE, true));
        builder.setPiece(new King(60, WHITE, true));
        builder.setPiece(new Bishop(61, WHITE, true));
        builder.setPiece(new Knight(62, WHITE, true));
        builder.setPiece(new Rook(63, WHITE, true));

        builder.setMoveMaker(WHITE);
        return builder.build();
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
    }

    public Pawn getEnPassantPawn() {
        return this.enPassantPawn;
    }

    public static class Builder {

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        private Pawn enPassantPawn;

        public Builder() {
            this.boardConfig = new HashMap<>();
        }

        public void setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
        }

        public Builder setMoveMaker(final Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build() {
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn movedPawn) {
            this.enPassantPawn = movedPawn;
        }
    }
}
