package com.tictactoe.tic_tac_toe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements TicTacToeView.OnGameOverListener {

    private TicTacToeView ticTacToeView;
    private Button playAgainButton;
    private TextView playerXScoreView;
    private TextView playerOScoreView;
    private TextView tiesScoreView;
    private TextView statusTextView;
    private TextView crownEmojiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        ticTacToeView = findViewById(R.id.ticTacToeView);
        playAgainButton = findViewById(R.id.playAgainButton);
        playerXScoreView = findViewById(R.id.playerXScore);
        playerOScoreView = findViewById(R.id.playerOScore);
        tiesScoreView = findViewById(R.id.tiesScore);
        statusTextView = findViewById(R.id.statusText);
        crownEmojiView = findViewById(R.id.crownEmoji);

        // Connect the UI elements to the TicTacToeView
        ticTacToeView.setUIElements(playerXScoreView, playerOScoreView, tiesScoreView,
                statusTextView, crownEmojiView);

        // Set the game over listener
        ticTacToeView.setOnGameOverListener(this);

        // Set up the play again button
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticTacToeView.resetGame();
                playAgainButton.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onGameOver(int winner) {
        // Show the play again button when the game is over
        playAgainButton.setVisibility(View.VISIBLE);
    }
}
