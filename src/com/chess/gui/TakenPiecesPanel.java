package com.chess.gui;

import com.chess.engine.Alliance;
import com.chess.engine.move.Move;
import com.chess.engine.pieces.Piece;
import com.google.common.primitives.Ints;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.chess.engine.Alliance.BLACK;
import static com.chess.engine.Alliance.WHITE;
import static com.chess.gui.Table.*;

public class TakenPiecesPanel extends JPanel {
    private final JPanel northPanel;
    private final JPanel southPanel;

    private static final Color PANEL_COLOR = Color.decode("0xFDFE6");
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40, 80);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    public TakenPiecesPanel() {
        super(new BorderLayout());
        this.setBackground(PANEL_COLOR);
        this.setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        add(this.northPanel, BorderLayout.NORTH);
        add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }

    public void redo(final MoveLog moveLog) {
        northPanel.removeAll();
        southPanel.removeAll();

        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();

        for (final Move move: moveLog.getMoves()) {
            if (move.isAttack()) {
                final Piece takenPiece = move.getAttackedPiece();
                if (takenPiece.getPieceAlliance().isWhite()) {
                    whiteTakenPieces.add(takenPiece);
                } else {
                    blackTakenPieces.add(takenPiece);
                }
            }
        }
        Collections.sort(whiteTakenPieces, (p1, p2) -> Ints.compare(p1.getPieceValue(), p2.getPieceValue()));
        Collections.sort(blackTakenPieces, (p1, p2) -> Ints.compare(p1.getPieceValue(), p2.getPieceValue()));



        drawTakenPieces(whiteTakenPieces, WHITE);
        drawTakenPieces(blackTakenPieces, BLACK);

        validate();
    }

    public void drawTakenPieces(List<Piece> takenPieces, Alliance alliance) {
        for (final Piece takenPiece : takenPieces) {
            try {
                /*First we get the image as a buffred inmage, and the an image icon
                so that we can scale it*/
                final BufferedImage image = ImageIO.read(new File("art/simple/"
                        + takenPiece.getPieceAlliance().toString().substring(0,1) + takenPiece.toString()  + ".gif"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(
                        icon.getIconWidth() - 15, icon.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                if ((alliance == WHITE)) {
                    this.southPanel.add(imageLabel);
                } else {
                    this.northPanel.add(imageLabel);
                }
            } catch(final IOException e) {
                e.printStackTrace();
            }
        }
    }
}
