package com.konis.aleph.brickPkg;
import java.awt.*;
import javax.swing.*;



/**
 *@author Victoria Suwardiman & Andrew Dammann
 *@course CSC 2053
 *@instructor Dr. Helwig
 *@project Project 4: Brickbreaker
 */
@SuppressWarnings("serial")
public class TextfieldPanel extends JPanel{

    //declares text fields
    private JTextField time, score, lives;

    //declare labels for text fields
    private JLabel timeLabel, scoreLabel, livesLabel;

    //constructor for textfield panel
    public TextfieldPanel(){

        //creates text field for displaying time
        time = new JTextField(8);
        time.setEditable(false);
        time.setFont(new Font("Arial", Font.BOLD, 20));
        time.setText("00:00");
        time.setBackground(Color.YELLOW);
        time.setHorizontalAlignment(JTextField.CENTER);

        //creates text field for displaying score
        score = new JTextField(8);
        score.setEditable(false);
        score.setFont(new Font("Arial", Font.BOLD, 20));
        score.setText("0");
        score.setBackground(Color.YELLOW);
        score.setHorizontalAlignment(JTextField.CENTER);

        //creates text field for displaying number of lives
        lives = new JTextField(8);
        lives.setEditable(false);
        lives.setFont(new Font("Arial", Font.BOLD, 20));
        lives.setText("3");
        lives.setBackground(Color.YELLOW);
        lives.setHorizontalAlignment(JTextField.CENTER);

        //sets up different panels for each variable
        JPanel timePanel = new JPanel();
        timeLabel = new JLabel();
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setText("Time: ");
        timePanel.add(timeLabel);
        timePanel.add(time);
        timePanel.setBackground(Color.BLUE);

        JPanel scorePanel = new JPanel();
        scoreLabel = new JLabel();
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setText("Score: ");
        scorePanel.add(scoreLabel);
        scorePanel.add(score);
        scorePanel.setBackground(Color.BLUE);

        JPanel livesPanel = new JPanel();
        livesLabel = new JLabel();
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setText("Lives: ");
        livesPanel.add(livesLabel);
        livesPanel.add(lives);
        livesPanel.setBackground(Color.BLUE);

        //sets up panel in a grid layout of previously created panels
        this.setLayout(new GridLayout(3,1));
        this.add(timePanel);
        this.add(scorePanel);
        this.add(livesPanel);

    }

    //gets the number of lives left from text field
    public JTextField getLives() {
        return lives;
    }

    //sets the lives text field
    public void setLives(String lives) {
        this.lives.setText(lives);
    }

    //gets current score from text field
    public JTextField getScore() {
        return score;
    }

    //sets the score text field
    public void setScore(String score) {
        this.score.setText(score);
    }

    //gets the time from text field
    public JTextField getTime() {
        return time;
    }

    //sets the time text field
    public void setTime(String time) {
        this.time.setText(time);
    }

}
