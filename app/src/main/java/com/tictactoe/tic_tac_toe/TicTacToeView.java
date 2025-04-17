package com.tictactoe.tic_tac_toe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TicTacToeView extends View {

    // Game state variables
    private boolean playerX;
    private boolean gameDone = false;
    private int winner = -1; // 1: X, 2: O, 3: Tie
    private int player1wins = 0, player2wins = 0, ties = 0;
    private int[][] board = new int[3][3];

    // UI references
    private TextView playerXScoreView;
    private TextView playerOScoreView;
    private TextView tiesScoreView;
    private TextView statusTextView;
    private TextView crownEmojiView;

    // Drawing settings
    private int boardPadding = 60;
    private int cellSize;
    private Paint paint;

    // Modern color palette
    private int bgColor = Color.parseColor("#F8F9FA");       // Light background
    private int gridColor = Color.parseColor("#5D5FEF");     // Vibrant purple grid lines
    private int xColor = Color.parseColor("#FF6B6B");        // Coral for X
    private int oOuterColor = Color.parseColor("#4ECDC4");   // Teal for O outer
    private int oInnerColor = Color.parseColor("#F7FFF7");   // Light inner for O
    private int uiTextColor = Color.parseColor("#2D3142");   // Dark slate text
    private int highlightColor = Color.parseColor("#FFD166"); // Golden highlight

    // Bitmap for X image (loaded from res/drawable)
    private Bitmap xBitmap;

    // Listener to callback when the game is over
    private OnGameOverListener gameOverListener;

    // Interface for game over callback
    public interface OnGameOverListener {
        void onGameOver(int winner);
    }

    public void setOnGameOverListener(OnGameOverListener listener) {
        this.gameOverListener = listener;
    }

    // Method to set UI elements
    public void setUIElements(TextView xScore, TextView oScore, TextView tiesScore,
                              TextView statusText, TextView crownEmoji) {
        this.playerXScoreView = xScore;
        this.playerOScoreView = oScore;
        this.tiesScoreView = tiesScore;
        this.statusTextView = statusText;
        this.crownEmojiView = crownEmoji;
        updateScoreDisplay();
        updateStatusDisplay();
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
        // Use a custom game font (you'll need to add this to your res/font folder)
        Typeface gameFont = Typeface.create("sans-serif-medium", Typeface.BOLD);
        paint.setTypeface(gameFont);
        resetGame();
        // Load X image from drawable resources
        xBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.orangex);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Calculate the cell size based on the smaller dimension to ensure it fits in both portrait and landscape
        int minDimension = Math.min(w, h);
        int boardSize = minDimension - (2 * boardPadding);
        cellSize = boardSize / 3;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        // Make the view square with height equal to width + space for scores
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Center the board in the available space
        drawBoard(canvas);
    }

    private void drawBoard(Canvas canvas) {
        // Draw the board in the center of the view
        int boardSize = cellSize * 3;

        // Calculate the starting position to center the board
        int startX = (getWidth() - boardSize) / 2;
        int startY = (getHeight() - boardSize) / 2;

        // Draw the grid
        drawGrid(canvas, startX, startY);

        // Draw the pieces
        drawPieces(canvas, startX, startY);
    }

    private void drawGrid(Canvas canvas, int startX, int startY) {
        paint.setColor(gridColor);
        paint.setStrokeWidth(12); // Thicker grid lines for better visibility

        // Draw vertical grid lines
        for (int i = 1; i < 3; i++) {
            int x = startX + i * cellSize;
            canvas.drawLine(x, startY, x, startY + cellSize * 3, paint);
        }

        // Draw horizontal grid lines
        for (int i = 1; i < 3; i++) {
            int y = startY + i * cellSize;
            canvas.drawLine(startX, y, startX + cellSize * 3, y, paint);
        }
    }

    private void drawPieces(Canvas canvas, int startX, int startY) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int value = board[i][j];
                int left = startX + i * cellSize;
                int top = startY + j * cellSize;
                int centerX = left + cellSize / 2;
                int centerY = top + cellSize / 2;
                int radius = (cellSize - 120) / 2; // Adjust padding for better appearance

                if (value == 1) { // Draw X using bitmap with better scaling
                    float padding = cellSize * 0.15f; // 15% padding
                    float pieceSize = cellSize - (2 * padding);
                    Bitmap scaledX = Bitmap.createScaledBitmap(xBitmap, (int)pieceSize, (int)pieceSize, true);
                    canvas.drawBitmap(scaledX, left + padding, top + padding, null);
                } else if (value == 2) { // Draw O as a hollow circle with better styling
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(45); // Adjust stroke width for desired thickness
                    paint.setColor(oOuterColor);
                    canvas.drawCircle(centerX, centerY, radius, paint);
                    paint.setStyle(Paint.Style.FILL); // Reset to fill for other drawings
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!gameDone && event.getAction() == MotionEvent.ACTION_DOWN) {
            float touchX = event.getX();
            float touchY = event.getY();

            // Calculate board position
            int boardSize = cellSize * 3;
            int startX = (getWidth() - boardSize) / 2;
            int startY = (getHeight() - boardSize) / 2;

            // Verify touch is within board boundaries
            if (touchX >= startX && touchX <= startX + boardSize &&
                    touchY >= startY && touchY <= startY + boardSize) {

                int i = (int) ((touchX - startX) / cellSize);
                int j = (int) ((touchY - startY) / cellSize);

                if (i >= 0 && i < 3 && j >= 0 && j < 3) {
                    if (board[i][j] == 0) {
                        board[i][j] = playerX ? 1 : 2;
                        playerX = !playerX;
                        updateStatusDisplay();
                        checkWinner();
                        invalidate();
                    }
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
            else if (winner == 3)
                ties++;

            updateScoreDisplay();
            updateStatusDisplay();

            // Notify Activity that the game is over so it can show the Play Again button
            if (gameOverListener != null) {
                gameOverListener.onGameOver(winner);
            }
        }
    }

    private void updateScoreDisplay() {
        if (playerXScoreView != null && playerOScoreView != null && tiesScoreView != null) {
            playerXScoreView.setText(String.valueOf(player1wins));
            playerOScoreView.setText(String.valueOf(player2wins));
            tiesScoreView.setText(String.valueOf(ties));
        }
    }

    private void updateStatusDisplay() {
        if (statusTextView != null && crownEmojiView != null) {
            if (gameDone) {
                if (winner == 1) {
                    statusTextView.setText("Player X Wins!");
                    crownEmojiView.setVisibility(VISIBLE);
                } else if (winner == 2) {
                    statusTextView.setText("Player O Wins!");
                    crownEmojiView.setVisibility(VISIBLE);
                } else if (winner == 3) {
                    statusTextView.setText("It's a Tie!");
                    crownEmojiView.setVisibility(GONE);
                }
            } else {
                statusTextView.setText(playerX ? "X's Turn" : "O's Turn");
                crownEmojiView.setVisibility(GONE);
            }
        }
    }

    public void resetGame() {
        playerX = true; // X always starts
        gameDone = false;
        winner = -1;
        board = new int[3][3]; // Reset the board
        updateStatusDisplay();
        invalidate(); // Request a redraw
    }

    // Getter methods for the scores
    public int getPlayer1Wins() {
        return player1wins;
    }

    public int getPlayer2Wins() {
        return player2wins;
    }

    public int getTies() {
        return ties;
    }
}
