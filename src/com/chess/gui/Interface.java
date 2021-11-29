package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardValues;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.user.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Interface {

    private final Color lightTile = Color.decode("#F6E2B9");
    private final Color brownTile = Color.decode("#964B00");


    private Board chessBoard;
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;

    private Tile sourceTile;
    private Tile destinationTile;
    private Piece playerMovedPiece;

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private static String defaultIconPath = "icons/";

    public Interface() {
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu(){
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem openNewGame = new JMenuItem("Load New Game");
        openNewGame.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open New Game");
            }
        });

        fileMenu.add(openNewGame);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    private class BoardPanel extends JPanel{
        //this class corresponds to the chessboard
        final List<TilePanel> boardTiles;
        //keeping track of each tiles in "boardTiles"
        //and we also adding this JPanel in for method with this line "add(tilePanel);"

        BoardPanel() {
            //GridLayout is a layout manager that lays out a container's components in a rectangular grid
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();
            for(int i = 0; i< BoardValues.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final Board board){
            removeAll();
            for(final TilePanel tilePanel : boardTiles){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            //validate() performs relayout
            validate();
            //When call to repaint method is made, it performs a request to erase
            //and perform redraw of the component after a small delay in time.
            repaint();
        }

    }


    private class TilePanel extends JPanel{
        //this class corresponds to a tile on a chessboard

        private final int tileId;


        TilePanel(final BoardPanel boardPanel,
                  final int tileId){
            super(new GridBagLayout());
            //GridBagLayout places components in
            // a grid of rows and columns, allowing specified components to span multiple rows or columns
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {

                    if (isRightMouseButton(e)) {
                        sourceTile = null;
                        destinationTile = null;
                        playerMovedPiece = null;
                    }
                    else if (isLeftMouseButton(e)) {
                        if (sourceTile == null) {

                            // first click
                            sourceTile = chessBoard.getTile(tileId);
                            playerMovedPiece = sourceTile.getPiece();
                            if (playerMovedPiece == null) {
                                System.out.println("No piece selected");
                                sourceTile = null;
                            }

                        }
                        else {
                            destinationTile = chessBoard.getTile(tileId);
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(),destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);

                            if (transition.getMoveStatus().isDone()) {
                                System.out.println("Done: " + chessBoard.currentPlayer());
                                chessBoard = transition.getTransitionBoard();
                            }
                            sourceTile = null;
                            destinationTile = null;
                            playerMovedPiece = null;

                        }
                        //SwingUtilities class has a function to help with GUI rendering tasks
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }


                }




                @Override
                public void mousePressed(final MouseEvent e) {

                }

                @Override
                public void mouseReleased(final MouseEvent e) {

                }

                @Override
                public void mouseEntered(final MouseEvent e) {

                }

                @Override
                public void mouseExited(final MouseEvent e) {

                }
            });
            validate();
        }

        public void drawTile(final Board board){
            assignTileColor();
            assignTilePieceIcon(board);
            validate();
            repaint();
        }

        private void assignTilePieceIcon(final Board board){
            this.removeAll();
            if(board.getTile(this.tileId).isTileOccupied()){
                try {
                    final BufferedImage image =
                            ImageIO.read(new File(defaultIconPath +
                                    board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0,1) +
                                    board.getTile(this.tileId).getPiece().toString() + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private void assignTileColor(){
            if(BoardValues.FIRST_ROW[this.tileId] ||
                    BoardValues.THIRD_ROW[this.tileId] ||
                    BoardValues.FIFTH_ROW[this.tileId] ||
                    BoardValues.SEVENTH_ROW[this.tileId]){
                setBackground(this.tileId % 2 == 0 ? lightTile : brownTile);
            } else if(BoardValues.SECOND_ROW[this.tileId] ||
                        BoardValues.FOURTH_ROW[this.tileId] ||
                        BoardValues.SIXTH_ROW[this.tileId] ||
                        BoardValues.EIGHTH_ROW[this.tileId]){
                setBackground(this.tileId % 2 != 0 ? lightTile : brownTile);
            }

        }

    }

}
