package com.konis.aleph;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class QuestionButton extends JButton {
    public boolean isPressed=false;
    public boolean completed=false;
    public hebWord hebword;
    public QuestionButton(String text) {
        super(text);
    }
    public QuestionButton(hebWord hw){
        super(hw.toString());
        hebword = hw;
    }
}
