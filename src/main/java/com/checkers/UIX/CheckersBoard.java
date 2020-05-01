package com.checkers.UIX;

import com.checkers.UIX.panels.GameHistoryPanel;
import com.checkers.UIX.panels.TakenFigurePanel;
import com.checkers.engine.board.Board;
import com.checkers.engine.move.Move;
import com.checkers.engine.players.BlackPlayer;
import com.checkers.engine.players.Player;
import com.checkers.engine.players.Player.PlayerType;
import com.checkers.engine.players.WhitePlayer;
import com.checkers.engine.strategy.ai.DifficultyLevel;
import com.checkers.engine.strategy.ai.MiniMax;
import com.checkers.engine.strategy.ai.MoveStrategy;
import com.checkers.engine.strategy.ai.RandomMove;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.checkers.UIX.UIXContent.*;
import static com.checkers.engine.players.Player.PlayerType.BLACK;
import static com.checkers.engine.players.Player.PlayerType.WHITE;
import static com.checkers.engine.strategy.ai.DifficultyLevel.*;
import static com.checkers.engine.utils.EngineUtils.*;

public class CheckersBoard extends Canvas {
    private final Board board;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenFigurePanel takenFigurePanel;
    private final Label message;
    private final Set<Move> attackChainMoves;
    private final Set<Board> boards;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private Player currentPlayer;
    private MoveStrategy strategy;
    private List<Move> legalMoves;
    private boolean gameInProgress;
    private int selectedRow, selectedColumn;

    public CheckersBoard(Label message, TakenFigurePanel takenFigurePanel, GameHistoryPanel gameHistoryPanel) {
        super(708, 708);
        this.message = message;
        this.takenFigurePanel = takenFigurePanel;
        this.gameHistoryPanel = gameHistoryPanel;
        this.attackChainMoves = new HashSet<>();
        this.boards = new HashSet<>();
        this.board = new Board();
        this.whitePlayer = new WhitePlayer(board);
        this.blackPlayer = new BlackPlayer(board);
        this.currentPlayer = whitePlayer;
        this.strategy = new RandomMove();
    }

    public void createGame() {
        initGame();
        computeLegalMoves();
        drawBoard();
        takenFigurePanel.drawTakenFigurePanel();
        gameHistoryPanel.drawGameHistoryPanel();
    }

    public void computerStartGame() {
        if (currentPlayer.isComputerPlayer()) {
            computerMakeMove();
        }
    }

    public void computerVsComputer() {
        if (whitePlayer.isComputerPlayer() && blackPlayer.isComputerPlayer()) {
            while (gameInProgress) {
                if (legalMoves.isEmpty()) {
                    if (currentPlayer.getPlayerType() == WHITE) {
                        gameOver("WHITE has no moves.  BLACK wins!");
                    } else {
                        gameOver("BLACK has no moves.  WHITE wins!");
                    }
                    drawBoard();
                    return;
                }
                computerMakeMove();
            }
        }
    }

    public void giveUp() {
        if (currentPlayer.getPlayerType() == WHITE) {
            gameOver("WHITE surrender.  BLACK wins!");
        } else {
            gameOver("BLACK surrender.  WHITE wins!");
        }
    }

    public void mousePressed(MouseEvent mouseEvent) {
        if (!gameInProgress) {
            printTextInMessageField("Click on Top Menu Bar \"File -> New Game\" to start a new game or \"Help -> Game Manual\"");
        } else {
            int clickedColumn = (int) ((mouseEvent.getX() - 4) / BOARD_FIELD_SIZE);
            int clickedRow = (int) ((mouseEvent.getY() - 4) / BOARD_FIELD_SIZE);
            if (clickedColumn >= 0 && clickedColumn < COLUMN_COUNT && clickedRow >= 0 && clickedRow < ROW_COUNT)
                actionClickSquare(clickedRow, clickedColumn);
        }
    }

    public Board getBoard() {
        return board;
    }

    public List<Move> getLegalMoves() {
        return legalMoves;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public WhitePlayer getWhitePlayer() {
        return whitePlayer;
    }

    public BlackPlayer getBlackPlayer() {
        return blackPlayer;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        if (difficultyLevel == EASY) {
            strategy = new RandomMove();
        } else if (difficultyLevel == MEDIUM) {
            strategy = new MiniMax(2);
        } else if (difficultyLevel == HARD) {
            strategy = new MiniMax(4);
        }
    }

    public void setWhitePlayerAsComputer(boolean setValue) {
        whitePlayer.setComputerPlayer(setValue);
    }

    public void setBlackPlayerAsComputer(boolean setValue) {
        blackPlayer.setComputerPlayer(setValue);
    }

    private void actionClickSquare(int clickedRow, int clickedColumn) {
        for (Move legalMove : legalMoves) {
            if (legalMove.initialRow == clickedRow && legalMove.initialColumn == clickedColumn) {
                selectedRow = clickedRow;
                selectedColumn = clickedColumn;
                if (currentPlayer.getPlayerType() == WHITE) {
                    if (legalMove.isPawnAttackMove() || legalMove.isQueenAttackMove()) {
                        printTextInMessageField("WHITE:  Make your jump to green field.");
                    } else {
                        printTextInMessageField("WHITE:  Make your move to green field.");
                    }
                } else {
                    if (legalMove.isPawnAttackMove() || legalMove.isQueenAttackMove()) {
                        printTextInMessageField("BLACK:  Make your jump to green field.");
                    } else {
                        printTextInMessageField("BLACK:  Make your move to green field.");
                    }
                }
                drawBoard();
                return;
            }
        }
        if (selectedRow < 0) {
            printTextInMessageField("Click the figure you want to move.");
            return;
        }
        for (Move legalMove : legalMoves) {
            if (legalMove.initialRow == selectedRow && legalMove.initialColumn == selectedColumn &&
                    legalMove.destinationRow == clickedRow && legalMove.destinationColumn == clickedColumn) {
                actionMakeMove(legalMove);
                return;
            }
        }
        printTextInMessageField("Click green square of figure you already want to move or click on next valid figure.");
    }

    private void actionMakeMove(Move move) {
        if (move.isPawnMajorMove() || move.isQueenMajorMove()) {
            board.executeMove(move);
            board.pawnPromotion(move, legalMoves);
            boards.add(new Board(board));
        } else if (move.isPawnAttackMove() || move.isQueenAttackMove()) {
            board.executeJump(move);
            boards.add(new Board(board));
            computeLegalJump(move);
            attackChainMoves.add(move);
            board.pawnPromotion(move, legalMoves);
            if (!legalMoves.isEmpty()) {
                if (currentPlayer.getPlayerType() == WHITE) {
                    printTextInMessageField("WHITE:  You must continue jumping.");
                } else {
                    printTextInMessageField("BLACK:  You must continue jumping.");
                }
                selectedRow = move.destinationRow;
                selectedColumn = move.destinationColumn;
                drawBoard();
                return;
            } else {
                for (Move attackMove : attackChainMoves) {
                    board.killFigure(attackMove);
                }
                attackChainMoves.clear();
            }
        }
        if (currentPlayer.getPlayerType() == WHITE) {
            changeCurrentPlayer(BLACK, board);
            computeLegalMoves();
            if (legalMoves.isEmpty()) {
                gameOver("BLACK has no moves.  WHITE wins!");
                drawBoard();
                return;
            } else if (legalMoves.get(0).isPawnAttackMove() || legalMoves.get(0).isQueenAttackMove()) {
                printTextInMessageField("BLACK:  Make your move.  You must jump.");
            } else {
                printTextInMessageField("BLACK:  Click on valid figure and make your move.");
            }
        } else {
            changeCurrentPlayer(WHITE, board);
            computeLegalMoves();
            if (legalMoves.isEmpty()) {
                gameOver("WHITE has no moves.  BLACK wins.");
                drawBoard();
                return;
            } else if (legalMoves.get(0).isPawnAttackMove() || legalMoves.get(0).isQueenAttackMove()) {
                printTextInMessageField("WHITE:  Make your move.  You must jump.");
            } else {
                printTextInMessageField("WHITE:  Click on valid figure and make your move.");
            }
        }
        if (currentPlayer.isComputerPlayer()) {
            computerMakeMove();
        }
        selectedRow = -1;
        if (!legalMoves.isEmpty()) {
            boolean sameStartSquare = true;
            for (int i = 1; i < legalMoves.size(); i++) {
                if (legalMoves.get(i).initialRow != legalMoves.get(0).initialRow || legalMoves.get(i).initialColumn != legalMoves.get(0).initialColumn) {
                    sameStartSquare = false;
                    break;
                }
            }
            if (sameStartSquare) {
                selectedRow = legalMoves.get(0).initialRow;
                selectedColumn = legalMoves.get(0).initialColumn;
            }
        }
        drawBoard();
    }

    private void computerMakeMove() {
        Move move = strategy.execute(this);
        if (move.isPawnMajorMove() || move.isQueenMajorMove()) {
            board.executeMove(move);
            board.pawnPromotion(move, legalMoves);
            boards.add(new Board(board));
        } else if (move.isPawnAttackMove() || move.isQueenAttackMove()) {
            while (!legalMoves.isEmpty()) {
                board.executeJump(move);
                boards.add(new Board(board));
                computeLegalJump(move);
                attackChainMoves.add(move);
                board.pawnPromotion(move, legalMoves);
                if (!legalMoves.isEmpty()) {
                    if (currentPlayer.getPlayerType() == WHITE) {
                        printTextInMessageField("WHITE:  You must continue jumping.");
                    } else {
                        printTextInMessageField("BLACK:  You must continue jumping.");
                    }
                    selectedRow = move.destinationRow;
                    selectedColumn = move.destinationColumn;
                    move = strategy.execute(this);
                    drawBoard();
                } else {
                    for (Move attackMove : attackChainMoves) {
                        board.killFigure(attackMove);
                    }
                    attackChainMoves.clear();
                    if (currentPlayer.getPlayerType() == WHITE) {
                        changeCurrentPlayer(BLACK, board);
                    } else {
                        changeCurrentPlayer(WHITE, board);
                    }
                    computeLegalMoves();
                    return;
                }
            }
        }
        if (currentPlayer.getPlayerType() == WHITE) {
            changeCurrentPlayer(BLACK, board);
        } else {
            changeCurrentPlayer(WHITE, board);
        }
        computeLegalMoves();
        drawBoard();
    }

    private void changeCurrentPlayer(PlayerType playerType, Board board) {
        if (playerType.isWhite()) {
            whitePlayer.setBoard(board);
            currentPlayer = whitePlayer;
        } else {
            blackPlayer.setBoard(board);
            currentPlayer = blackPlayer;
        }
    }

    private void drawBoard() {
        int fieldNumber = 1;
        GraphicsContext graphics = initGraphicsContext();
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COLUMN_COUNT; col++) {
                if (row % 2 == col % 2) {
                    drawBoardField(graphics, row, col, Color.CORNSILK);
                } else {
                    drawBoardField(graphics, row, col, Color.SIENNA);
                    drawFieldNumber(fieldNumber++, graphics, row, col);
                }
                drawFigures(graphics, row, col);
            }
        }
        if (gameInProgress) {
            for (Move legalMove : legalMoves) {
                drawHelpContour(graphics, Color.CYAN, legalMove.initialColumn, legalMove.initialRow);
            }
            if (selectedRow >= 0) {
                drawHelpContour(graphics, Color.YELLOW, selectedColumn, selectedRow);
                for (Move legalMove : legalMoves) {
                    if (legalMove.initialColumn == selectedColumn && legalMove.initialRow == selectedRow) {
                        drawHelpContour(graphics, Color.LIME, legalMove.destinationColumn, legalMove.destinationRow);
                    }
                }
                if (!attackChainMoves.isEmpty()) {
                    for (Move attackMove : attackChainMoves) {
                        drawHelpContour(graphics, Color.RED, attackMove.getEnemyDestinationColumn(), attackMove.getEnemyDestinationRow());
                    }
                }
            }
        }
    }

    private void initGame() {
        board.initBoard();
        changeCurrentPlayer(WHITE, board);
        boards.add(new Board(board));
        selectedRow = -1;
        printTextInMessageField("WHITE: First move is yours!  Click on valid figure and make your move.");
        gameInProgress = true;
    }

    private void gameOver(String text) {
        printTextInMessageField(text);
        gameInProgress = false;
    }

    private void computeLegalMoves() {
        legalMoves = board.calculateMovesOnBoard(currentPlayer.getPlayerType());
    }

    private void computeLegalJump(Move move) {
        legalMoves = board.calculateNextJumpOnBoard(currentPlayer.getPlayerType(), move.destinationRow, move.destinationColumn);
    }

    private void drawHelpContour(GraphicsContext graphics, Color color, int fromCol, int fromRow) {
        graphics.setStroke(color);
        graphics.setLineWidth(3);
        graphics.strokeRect(4 + fromCol * BOARD_FIELD_SIZE, 4 + fromRow * BOARD_FIELD_SIZE, BOARD_FIELD_SIZE, BOARD_FIELD_SIZE);
    }

    private void drawFigures(GraphicsContext graphics, int row, int col) {
        if (board.getBoardField(row, col).isBoardFieldOccupied()) {
            switch (board.getBoardField(row, col).getFigure().getFigureType()) {
                case WHITE_PAWN:
                    graphics.drawImage(WP, 14 + col * BOARD_FIELD_SIZE, 14 + row * BOARD_FIELD_SIZE, 50, 50);
                    break;
                case BLACK_PAWN:
                    graphics.drawImage(BP, 14 + col * BOARD_FIELD_SIZE, 14 + row * BOARD_FIELD_SIZE, 50, 50);
                    break;
                case WHITE_QUEEN:
                    graphics.drawImage(WQ, 14 + col * BOARD_FIELD_SIZE, 14 + row * BOARD_FIELD_SIZE, 50, 50);
                    break;
                case BLACK_QUEEN:
                    graphics.drawImage(BQ, 14 + col * BOARD_FIELD_SIZE, 14 + row * BOARD_FIELD_SIZE, 50, 50);
                    break;
            }
        }
    }

    private void drawFieldNumber(int fieldNumber, GraphicsContext graphics, int row, int col) {
        graphics.setFill(Color.CORNSILK);
        graphics.fillText("" + fieldNumber, 7 + col * BOARD_FIELD_SIZE, 18 + row * BOARD_FIELD_SIZE);
    }

    private void drawBoardField(GraphicsContext graphics, int row, int col, Color color) {
        graphics.setFill(color);
        graphics.fillRect(4 + col * BOARD_FIELD_SIZE, 4 + row * BOARD_FIELD_SIZE, BOARD_FIELD_SIZE, BOARD_FIELD_SIZE);
    }

    private GraphicsContext initGraphicsContext() {
        GraphicsContext graphics = getGraphicsContext2D();
        graphics.setFont(Font.font(14));
        graphics.setStroke(Color.GREY);
        graphics.setLineWidth(8);
        graphics.strokeRect(0, 0, 708, 708);
        return graphics;
    }

    private void printTextInMessageField(String text) {
        message.setText(text);
    }
}
