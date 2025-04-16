// app/src/main/java/com/tictactoe2/tic_tac_toe2/MainActivity.java
package com.tictactoe2.tic_tac_toe2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TicTacToeView gameView;
    private Button playAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameView = findViewById(R.id.gameView);
        playAgainButton = findViewById(R.id.playAgainButton);

        // Set a listener so that when the game ends, the Play Again button is shown.
        gameView.setOnGameOverListener(new TicTacToeView.OnGameOverListener() {
            @Override
            public void onGameOver() {
                playAgainButton.setVisibility(View.VISIBLE);
            }
        });

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.resetGame();
                playAgainButton.setVisibility(View.GONE);
            }
        });
    }
}
