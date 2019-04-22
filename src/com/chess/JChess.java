package com.chess;

import com.chess.engine.board.Board;
import com.chess.gui.Table;

public class JChess {
    public static void main(String[] args) {
        // Detta är kodens början, vi skapar vårt bräde
        Board board = Board.createStandardBoard();
        //System.out.println(board);
        Table table = new Table();
    }
}
