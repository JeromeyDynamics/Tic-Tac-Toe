
//imports
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.*;

//the game will be a JPanel element
public class TicTacToe extends JPanel implements ActionListener {
    // logic variables
    boolean playerIsX; // tells game which players turn it is
    boolean gameDone = false; // lets the game know if the game is done
    int winner = -1; // theres no winner so the value is -1
    int player1Wins = 0, player2Wins = 0; // tells the game how many wins each player has
    int board[][] = new int[3][3]; // represents the tic-tac-toe game board

    // repeating number variables
    int lineWidth = 10; // for the lines width to separate the boxes on the board
    int lineLength = 540; // for the lines length to separate the boxes on the board
    int x = 30, y = 200; // location of the first lines starting position
    int offset = 190; // box width

    // stores the x and y location of each mouse click
    int a = 0, b = 5;
    int selX = 0, selY = 0;

    // colors
    Color turtleGreen = new Color(0x80bdab);
    Color orange = new Color(0xfdcb9e);
    Color offWhite = new Color(0xf7f7f7);
    Color darkGray = new Color(0x3f3f44);

    // components
    JButton playAgainButton; // the user will click this button to play again (added JButton from the
                             // javax.swing class)

    public TicTacToe() {
        // init before game
        // sets the size of the gui JPanel
        Dimension size = new Dimension(840, 600);

        // 840 x 600 panel size and cannot be changed
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        // creates a play again button
        playAgainButton = new JButton("Play Again?");

        // listens for when the button will be clicked
        playAgainButton.addActionListener(this);

        // adds the play again button to the JPanel
        add(playAgainButton);

        // hides the play again button while the game is going
        playAgainButton.setVisible(false);

        // adds a mouse listener to tell where the current player is clicking
        addMouseListener(new XOListener());
    }

    public static void main(String[] args) {
        // new JFrame of the title Tic Tac Toe
        JFrame frame = new JFrame("Tic Tac Toe");
        // JFrames hold JPanels, JLabels, JButtons, etc
        frame.getContentPane();

        // creates TicTacToe object TTTPanel
        TicTacToe TTTPanel = new TicTacToe();
        // adds TicTacToe object to frame
        frame.add(TTTPanel);

        // frame set up
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // adding default settings
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // will reset the game when the play again button is pressed
        resetGame();

    }

    // getter method for play again button
    public JButton getPlayAgainButton() {
        return playAgainButton;
    }

    // setter method for player 1 wins
    public void setPlayer1Wins(int wins) {
        player1Wins = wins;
    }

    // setter method for player 2 wins
    public void setPlayer2Wins(int wins) {
        player2Wins = wins;
    }

    public void resetGame() {
        // resetting variables from the beginning of the class
        playerIsX = true; // player X's turn
        winner = -1; // no winner yet
        gameDone = false; // game is restarted so the game isn't done

        // 0 is empty, 1 is X, and 2 is O on the array for the board
        // setting all boxes to being empty (value = 0)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0;
            }
        }

        // hides the play again button while the game is going
        getPlayAgainButton().setVisible(false);

    }

    // graphics
    public void paintComponent(Graphics page) {
        super.paintComponent(page);
        // draws game board with the lines and background color
        drawBoard(page);
        drawUI(page);
        drawGame(page);

    }

    public void drawBoard(Graphics page) {
        // sets background color
        setBackground(turtleGreen);

        // sets line color
        page.setColor(darkGray);

        // makes both horizontal lines (draws line of the dark gray color as set by the
        // line above)
        page.fillRoundRect(x, y, lineLength, lineWidth, 5, 30);
        // since offset is the space in between the boxes we simply add it to the y so
        // that the horisontal lines are the width/length of the box away from each
        // other
        page.fillRoundRect(x, y + offset, lineLength, lineWidth, 5, 30);

        // you just need to reverse the parameters of the horizontal lines to make the
        // verticle lines
        page.fillRoundRect(y, x, lineWidth, lineLength, 30, 5);
        // since offset is the space in between the boxes we simply add it to the y so
        // that the vertical lines are the width/length of the box away from each other
        page.fillRoundRect(y + offset, x, lineWidth, lineLength, 30, 5);

    }

    public void drawUI(Graphics page) {
        // creates a gray rectangle to the right of the screen
        page.setColor(darkGray);
        page.fillRect(600, 0, 240, 600);

        // setting the font of the words that will be put in the gray rectangle
        Font font = new Font("Helvetica", Font.PLAIN, 40);
        page.setFont(font);

        // setting the win counter which will be in the gray rectangle
        // sets the color of the text for the win counter
        page.setColor(offWhite);
        // writes the words "Win Count" in the gray rectangle
        page.drawString("Win Count", 620, 60);
        // shows the both players wins using their respective win count variables
        page.drawString(": " + player1Wins, 724, 140);
        page.drawString(": " + player2Wins, 724, 210);

        // drawing an X icon to go before the first players number of wins
        // creates an instance of the "orangex.png"
        ImageIcon xIcon = new ImageIcon("orangex.png");
        // gets the image from the image icon because the ImageIcon was part of the
        // javax.swing package and Image was part of the java.awt package
        Image xImg = xIcon.getImage();
        // upscale the x image
        Image newXImg = xImg.getScaledInstance(54, 54, Image.SCALE_SMOOTH);
        // transforms the x image into an image icon (doesn't work if you directly use
        // newXImg in the line where the x in draw on to the page
        ImageIcon newXIcon = new ImageIcon(newXImg);
        // puts the x icon image right in front of the score for player 1
        page.drawImage(newXIcon.getImage(), 658, 94, null);

        // draws an O icon to go before the second players number of wins
        // creates an illusion of an outline of a circle with 2 circles
        // what will be the outline of the circle
        page.setColor(offWhite);
        page.fillOval(656, 160, 60, 60);
        // the hole in the middle of the filled in circle from the line before
        page.setColor(darkGray);
        page.fillOval(668, 170, 38, 38);

        // draws the words for the players turn and once the game is finished, who won
        // or if it is a tie
        page.setColor(offWhite);
        Font font1 = new Font("Serif", Font.ITALIC, 36);
        page.setFont(font1);

        if (gameDone) {
            // shows winner
            // player 1 wins
            if (winner == 1) {
                // displays "The winner is" as text on the screen
                page.drawString("The winner is", 620, 300);
                // draws the X on the screen to signify that play 1 won
                page.drawImage(xImg, 335, 160, null);
            }

            // player 2 wins
            else if (winner == 2) {
                // displays "The winner is" as text on the screen
                page.drawString("The winner is", 620, 300);
                // draws the circle on the screen to signify that player 2 won
                page.setColor(offWhite);
                page.fillOval(664, 320, 100, 100);
                page.setColor(darkGray);
                page.fillOval(684, 340, 60, 60);
            }

            // there is a tie
            else if (winner == 3) {
                // displays "It's a tie" as text on the screen
                page.drawString("It's a tie", 660, 356);
            }
        } else {
            // shows whose turn it is
            Font font2 = new Font("Serif", Font.ITALIC, 40);
            page.setFont(font2);
            // displays the players turn depending on if the player is X
            page.drawString("It's", 700, 320);
            if (playerIsX) {
                page.drawString("X's turn", 650, 360);
            } else {
                page.drawString("O's turn", 650, 360);
            }
        }

    }

    public void drawGame(Graphics page) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // there is no X or O on that space
                if (board[i][j] == 0) {
                    // nothing is done since there is X's or O's on that space
                }

                // there is an X on that space
                else if (board[i][j] == 1) {
                    // creates an instance of the "orangex.png"
                    ImageIcon xIcon = new ImageIcon("orangex.png");
                    // gets the image from the image icon because the ImageIcon was part of the
                    // javax.swing package and Image was part of the java.awt package
                    Image xImg = xIcon.getImage();
                    // upscale the x image
                    Image newXImg = xImg.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    // transforms the x image into an image icon (doesn't work if you directly use
                    // newXImg in the line where the x in draw on to the page
                    ImageIcon newXIcon = new ImageIcon(newXImg);
                    // puts the x icon image right in front of the score for player 1
                    page.drawImage(newXIcon.getImage(), 60 + offset * i, 60 + offset * j, null);
                }

                // there is an O on that space
                else if (board[i][j] == 2) {
                    page.setColor(offWhite);
                    page.fillOval(60 + offset * i, 60 + offset * j, 100, 100);
                    page.setColor(turtleGreen);
                    page.fillOval(80 + offset * i, 80 + offset * j, 60, 60);
                }
            }
        }
    }

    // creating a subclass to define the mouse listener added in the constructor
    public class XOListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent event) {
            // stores the x and y of the box that was clicked (0-2, 0-2)
            selX = -1;
            selY = -1;

            // makes sure X's and O's can't be spawned when the game is finished
            if (gameDone == false) {
                // making a and b equal to the mouse's x and y
                a = event.getX();
                b = event.getY();

                // Debugging: Log mouse coordinates
                System.out.println("Mouse Clicked at: (" + a + ", " + b + ")");

                // Debugging: Log calculated cell
                System.out.println("Calculated Cell: (" + selX + ", " + selY + ")");

                // determines which column the mouse is in when its clicked
                if (a > 24 && a < 198) {
                    selX = 0;
                } else if (a > 206 && a < 390) {
                    selX = 1;
                } else if (a > 400 && a < 574) {
                    selX = 2;
                } else {
                    selX = -1;
                }

                // determines which row the mouse is in when its clicked
                if (b > 24 && b < 198) {
                    selY = 0;
                } else if (b > 206 && b < 390) {
                    selY = 1;
                } else if (b > 400 && b < 574) {
                    selY = 2;
                } else {
                    selY = -1;
                }

                // checks if the user clicked in a valid box
                if (selX != -1 && selY != -1) {
                    // if the box is empty, the player will be able to place their X or O in the box
                    if (board[selX][selY] == 0) {
                        if (playerIsX) {
                            board[selX][selY] = 1;
                            playerIsX = false;
                        } else {
                            board[selX][selY] = 2;
                            playerIsX = true;
                        }

                        System.out.println("Clicked => x: " + a + ", y: " + b + ", board: (" + selX + "," + selY + ")");

                        // checks if there is a winner when a play is successfully made
                        checkWinner();
                    }
                } else {
                    System.out
                            .println("Invalid Click => x: " + a + ", y: " + b + ", board: (" + selX + "," + selY + ")");
                }

                // Trigger a repaint to update the UI
                repaint();
            }
        }

        @Override
        public void mousePressed(MouseEvent event) {

        }

        @Override
        public void mouseReleased(MouseEvent event) {

        }

        @Override
        public void mouseEntered(MouseEvent event) {

        }

        @Override
        public void mouseExited(MouseEvent event) {

        }
    }

    public void checkWinner() {
        // checks if the game is already done
        if (gameDone == true) {
            System.out.println("Game Done :)");
            return;
        }

        int temp = -1;

        // X wins if these all equal 1 while O wins if these all equal 2
        // checking column 1
        if (board[0][0] == board[0][1] && board[0][1] == board[0][2] && board[0][0] != 0) {
            temp = board[0][0];
        }

        // checking column 2
        else if (board[1][0] == board[1][1] && board[1][1] == board[1][2] && board[1][0] != 0) {
            temp = board[1][0];
        }

        // checking column 3
        else if (board[2][0] == board[2][1] && board[2][1] == board[2][2] && board[2][0] != 0) {
            temp = board[2][0];
        }

        // checking row 1
        else if (board[0][0] == board[1][0] && board[1][0] == board[2][0] && board[0][0] != 0) {
            temp = board[0][0];
        }

        // checking row 2
        else if (board[0][1] == board[1][1] && board[1][1] == board[2][1] && board[0][1] != 0) {
            temp = board[0][1];
        }

        // checking row 3
        else if (board[0][2] == board[1][2] && board[1][2] == board[2][2] && board[0][2] != 0) {
            temp = board[0][2];
        }

        // checking diagonal 1
        else if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != 0) {
            temp = board[0][0];
        }

        // checking diagonal 2
        else if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != 0) {
            temp = board[0][2];
        }

        // searching for any empty spots on the board and if there are none, it is a tie
        // while if there is one, the game continues
        else {
            // determines if the game is a tie or if it is still going
            boolean notDone = false;

            // checks if there is a tie
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 0) {
                        notDone = true;
                        break;
                    }
                }
            }

            // there was a tie
            if (notDone == false) {
                temp = 3;
            }
        }

        if (temp > 0) {
            // holds the winner
            winner = temp;

            // checks if the game is a tie or if there is a winner
            if (winner == 1) {
                player1Wins++;
                System.out.println("X wins!");
            } else if (winner == 2) {
                player2Wins++;
                System.out.println("O wins!");
            } else if (winner == 3) {
                System.out.println("It's a tie!");
            }
            gameDone = true;

            // gives the player the option to play again
            getPlayAgainButton().setVisible(true);
        }
    }
}