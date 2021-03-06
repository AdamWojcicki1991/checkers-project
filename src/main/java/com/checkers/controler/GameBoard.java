package com.checkers.controler;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.checkers.UIX.UIXContent.*;
import static com.checkers.engine.players.Player.PlayerType.BLACK;
import static com.checkers.engine.players.Player.PlayerType.WHITE;
import static com.checkers.engine.strategy.ai.DifficultyLevel.*;
import static com.checkers.engine.utils.EngineUtils.*;

public class GameBoard extends Canvas {
    private final Board board;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenFigurePanel takenFigurePanel;
    private final Label message;
    private final List<Move> attackChainMoves;
    private final Set<Board> boards;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private Player currentPlayer;
    private MoveStrategy strategy;
    private List<Move> legalMoves;
    private boolean gameInProgress;
    private int selectedRow, selectedColumn;
    private int queenMoveCount;

    public GameBoard(Label message, TakenFigurePanel takenFigurePanel, GameHistoryPanel gameHistoryPanel) {
        super(708, 708);
        this.message = message;
        this.takenFigurePanel = takenFigurePanel;
        this.gameHistoryPanel = gameHistoryPanel;
        this.attackChainMoves = new ArrayList<>();
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
        takenFigurePanel.resetFiguresCounter();
        takenFigurePanel.resetFiguresFromPanels();
        gameHistoryPanel.resetNotationCounters();
        gameHistoryPanel.resetNotationFromPanels();
        takenFigurePanel.createTakenFigurePanel();
        gameHistoryPanel.createGameHistoryPanel();
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
                if (queenMoveCount == 30) {
                    gameOver("We have a DRAW!");
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

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        if (difficultyLevel == EASY) {
            strategy = new RandomMove();
        } else if (difficultyLevel == MEDIUM) {
            strategy = new MiniMax(1);
        } else if (difficultyLevel == HARD) {
            strategy = new MiniMax(2);
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
        if (selectedRow < 0 && gameInProgress) {
            printTextInMessageField("Click the figure you want to move.");
            return;
        }
        for (Move legalMove : legalMoves) {
            if (legalMove.initialRow == selectedRow && legalMove.initialColumn == selectedColumn &&
                    legalMove.destinationRow == clickedRow && legalMove.destinationColumn == clickedColumn) {
                humanMakeMove(legalMove);
                return;
            }
        }
        printTextInMessageField("Click green square of figure you already want to move or click on next valid figure.");
    }

    private void humanMakeMove(Move move) {
        if (move.isPawnMajorMove() || move.isQueenMajorMove()) {
            makeMove(move);
        } else if (move.isPawnAttackMove() || move.isQueenAttackMove()) {
            queenMoveCount = 0;
            board.executeJump(move);
            boards.add(new Board(board));
            computeLegalJump(move);
            attackChainMoves.add(move);
            board.pawnPromotion(move, legalMoves);
            if (!legalMoves.isEmpty()) {
                printTextInMessageField(currentPlayer.getPlayerType() + ":  You must continue jumping.");
                selectedRow = move.destinationRow;
                selectedColumn = move.destinationColumn;
                drawBoard();
                return;
            } else {
                executeAttackChain();
            }
        }
        if (currentPlayer.getPlayerType() == WHITE) {
            if (switchPlayerAndRecalculate(BLACK)) return;
        } else {
            if (switchPlayerAndRecalculate(WHITE)) return;
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

    private boolean switchPlayerAndRecalculate(PlayerType black) {
        changeCurrentPlayer(black, board);
        computeLegalMoves();
        if (legalMoves.isEmpty()) {
            gameOver(currentPlayer.getPlayerType() + "; has no moves.  other wins!");
            drawBoard();
            return true;
        } else if (queenMoveCount == 30) {
            gameOver("We have a DRAW!");
            drawBoard();
            return true;
        } else if (legalMoves.get(0).isPawnAttackMove() || legalMoves.get(0).isQueenAttackMove()) {
            printTextInMessageField(currentPlayer.getPlayerType() + ": Make your move.  You must jump.");
        } else {
            printTextInMessageField(currentPlayer.getPlayerType() + ":  Click on valid figure and make your move.");
        }
        return false;
    }

    private void computerMakeMove() {
        Move move = strategy.execute(this);
        if (move.isPawnMajorMove() || move.isQueenMajorMove()) {
            makeMove(move);
        } else if (move.isPawnAttackMove() || move.isQueenAttackMove()) {
            while (!legalMoves.isEmpty()) {
                queenMoveCount = 0;
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
                    move = strategy.execute(this);
                    drawBoard();
                } else {
                    executeAttackChain();
                    if (currentPlayer.getPlayerType() == WHITE) {
                        changeCurrentPlayer(BLACK, board);
                    } else {
                        changeCurrentPlayer(WHITE, board);
                    }
                    computeLegalMoves();
                    if (legalMoves.isEmpty()) {
                        if (currentPlayer.getPlayerType() == WHITE) {
                            gameOver("WHITE has no moves.  BLACK wins!");
                        } else {
                            gameOver("BLACK has no moves.  WHITE wins!");
                        }
                        drawBoard();
                    }
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

    private void makeMove(Move move) {
        if (move.isQueenMajorMove()) {
            queenMoveCount++;
        } else {
            queenMoveCount = 0;
        }
        board.executeMove(move);
        gameHistoryPanel.addNotationToGameHistoryPanel(currentPlayer.getPlayerType(), board.getBoardArray(), move);
        board.pawnPromotion(move, legalMoves);
        boards.add(new Board(board));
    }

    private void executeAttackChain() {
        int count = 0;
        for (Move attackMove : attackChainMoves) {
            if (count == 0) {
                gameHistoryPanel.addNotationToGameHistoryPanel(currentPlayer.getPlayerType(), board.getBoardArray(), attackMove);
            } else {
                gameHistoryPanel.addAttackChainNotationToGameHistoryPanel(currentPlayer.getPlayerType(), board.getBoardArray(), attackMove);
            }
            takenFigurePanel.addTakenFigureToPanel(board.getBoardArray()[attackMove.getEnemyDestinationRow()][attackMove.getEnemyDestinationColumn()].getFigure());
            board.killFigure(attackMove);
            count++;
        }
        attackChainMoves.clear();
    }

    private void changeCurrentPlayer(PlayerType playerType, Board board) {
        if (playerType == WHITE) {
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
