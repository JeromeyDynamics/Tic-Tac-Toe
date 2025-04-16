// app/src/main/java/com/tictactoe2/tic_tac_toe2/TicTacToeView.java
package com.tictactoe2.tic_tac_toe2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TicTacToeView extends View {

    // Game state variables
    private boolean playerX;
    private boolean gameDone = false;
    private int winner = -1; // 1: X, 2: O, 3: Tie
    private int player1wins = 0, player2wins = 0;
    private int[][] board = new int[3][3];

    // Drawing settings
    private int boardPadding = 40;
    private int cellSize;
    private Paint paint;

    // Updated color palette
    private int bgColor = Color.parseColor("#FFFFFF");       // White background
    private int gridColor = Color.parseColor("#3F51B5");       // Indigo grid lines
    private int oOuterColor = Color.parseColor("#FFC107");     // Amber outer circle for O
    private int oInnerColor = Color.parseColor("#FFEB3B");     // Lighter yellow inner circle for O
    private int uiTextColor = Color.parseColor("#212121");     // Dark text

    // Bitmap for X image (load from res/drawable)
    private Bitmap xBitmap;

    // Listener to callback when the game is over
    private OnGameOverListener gameOverListener;

    // Interface for game over callback
    public interface OnGameOverListener {
        void onGameOver();
    }

    public void setOnGameOverListener(OnGameOverListener listener) {
        this.gameOverListener = listener;
    }

    public TicTacToeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TicTacToeView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        // Use a medium sans-serif font with bold style
        paint.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));
        resetGame();
        // Load X image from drawable resources
        xBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.orangex);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Set the overall background color
        canvas.drawColor(bgColor);
        // Calculate the cell size so the board fits nicely on the screen
        int boardWidth = getWidth() - 2 * boardPadding;
        cellSize = boardWidth / 3;
        // Draw the grid, game pieces, and UI elements
        drawGrid(canvas);
        drawPieces(canvas);
        drawUI(canvas);
    }

    private void drawGrid(Canvas canvas) {
        paint.setColor(gridColor);
        paint.setStrokeWidth(8);
        // Draw vertical grid lines
        for (int i = 1; i < 3; i++) {
            int x = boardPadding + i * cellSize;
            canvas.drawLine(x, boardPadding, x, boardPadding + cellSize * 3, paint);
        }
        // Draw horizontal grid lines
        for (int i = 1; i < 3; i++) {
            int y = boardPadding + i * cellSize;
            canvas.drawLine(boardPadding, y, boardPadding + cellSize * 3, y, paint);
        }
    }

    private void drawPieces(Canvas canvas) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int value = board[i][j];
                int left = boardPadding + i * cellSize;
                int top = boardPadding + j * cellSize;
                int centerX = left + cellSize / 2;
                int centerY = top + cellSize / 2;
                int radius = (cellSize - 70) / 2; // Adjust padding as needed

                if (value == 1) { // Draw X using bitmap
                    Bitmap scaledX = Bitmap.createScaledBitmap(xBitmap, cellSize - 20, cellSize - 20, true);
                    canvas.drawBitmap(scaledX, left + 10, top + 10, null);
                } else if (value == 2) { // Draw O as a hollow circle
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(50); // Adjust stroke width for desired thickness
                    paint.setColor(oOuterColor);
                    canvas.drawCircle(centerX, centerY, radius, paint);
                    paint.setStyle(Paint.Style.FILL); // Reset to fill for other drawings
                }
            }
        }
    }

    private void drawUI(Canvas canvas) {
        // Draw the score and current game status below the board
        int centerX = getWidth() / 2;
        int uiY = boardPadding + cellSize * 3 + 60;
        paint.setColor(uiTextColor);
        paint.setTextSize(48);
        canvas.drawText("X Wins: " + player1wins + "    O Wins: " + player2wins, centerX, uiY, paint);
        uiY += 60;
        paint.setTextSize(56);
        if (gameDone) {
            if (winner == 1) {
                canvas.drawText("Winner: X", centerX, uiY, paint);
            } else if (winner == 2) {
                canvas.drawText("Winner: O", centerX, uiY, paint);
            } else if (winner == 3) {
                canvas.drawText("It's a Tie!", centerX, uiY, paint);
            }
        } else {
            canvas.drawText((playerX ? "X's Turn" : "O's Turn"), centerX, uiY, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!gameDone && event.getAction() == MotionEvent.ACTION_DOWN) {
            float touchX = event.getX();
            float touchY = event.getY();
            int i = (int) ((touchX - boardPadding) / cellSize);
            int j = (int) ((touchY - boardPadding) / cellSize);
            if (i >= 0 && i < 3 && j >= 0 && j < 3) {
                if (board[i][j] == 0) {
                    board[i][j] = playerX ? 1 : 2;
                    playerX = !playerX;
                    checkWinner();
                    invalidate();
                }
            }
        }
        return true;
    }

    private void checkWinner() {
        int temp = -1;
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 &&
                    board[i][0] == board[i][1] &&
                    board[i][1] == board[i][2])
                temp = board[i][0];
            if (board[0][i] != 0 &&
                    board[0][i] == board[1][i] &&
                    board[1][i] == board[2][i])
                temp = board[0][i];
        }
        // Diagonals
        if (board[0][0] != 0 &&
                board[0][0] == board[1][1] &&
                board[1][1] == board[2][2])
            temp = board[0][0];
        if (board[0][2] != 0 &&
                board[0][2] == board[1][1] &&
                board[1][1] == board[2][0])
            temp = board[0][2];
        // Check for a tie
        if (temp == -1) {
            boolean tie = true;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 0) {
                        tie = false;
                        break;
                    }
                }
            }
            if (tie)
                temp = 3;
        }

        if (temp > 0) {
            winner = temp;
            gameDone = true;
            if (winner == 1)
                player1wins++;
            else if (winner == 2)
                player2wins++;
            // Notify Activity that the game is over so it can show the Play Again button
            if (gameOverListener != null) {
                gameOverListener.onGameOver();
            }
        }
    }

    public void resetGame() {
        playerX = true;
        gameDone = false;
        winner = -1;
        board = new int[3][3];
        invalidate();
    }
}
