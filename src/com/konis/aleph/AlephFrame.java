package com.konis.aleph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.konis.aleph.hebLetter.Vowel;


public class AlephFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    final static Charset ENCODING = StandardCharsets.UTF_8;

    // graphic components
    static PlayPanel mainPanel;
    static JPanel bottomPanel;
    static JPanel questPanel;
    final static ArrayList<QuestionButton> buttons = new ArrayList<QuestionButton>();
    private static final boolean use_full_log = false;
    // watch
    static JLabel show;
    static JLabel scoreLabel;
    static JLabel medal;
    static JTextField syllableIn;

    // different data structures
    ArrayList<hebWord> words = new ArrayList<hebWord>();

    // parameters
    int how_many_questions = 3;
    String vowelpath;// = "/home/ubtosh/workspace/Aleph/data/vowels/";
    String iconpath;// = "/home/ubtosh/workspace/Aleph/data/icon/";
    String saypath;
    String[] vowelIcons = { "patah", "tzere", "hirik", "holam", "kubutz" };
    String[] vowelFullIcons = { "patah_full2", "tzere_full2", "hirik_full2",
            "holam_full2", "kubutz_full2" };

    // graphic constants
    int delt_x_align = 10;
    int offs_x_align = 50;
    int boxSize = 60;
    int vowelSize = boxSize;//50;
    int minOverlap = 0;

    /**
     * states
     */
    //
    String playerName = "";
    // score and level related states variables and thresholds
    static int levelScore = 0; // score to switch between assignments types
    private int deltaScore=1;
    // thresholds to allow switching to new game type ("levels")
    final int EASYTHRESH = deltaScore*1;
    final int MEDIUMTHRESH = EASYTHRESH+deltaScore*10;
    final int HARDTHRESH = MEDIUMTHRESH+deltaScore*10;
    static int difficultyScore=0; // score to switch between letters difficulties
    // thresholds to switch between difficulties (and medals...)
    final int LEVEL1THRESH = how_many_questions*deltaScore*5;
    final int LEVEL2THRESH = LEVEL1THRESH + how_many_questions*deltaScore*10;
    final int LEVEL3THRESH = LEVEL2THRESH + how_many_questions*deltaScore*10;
    final int BRICKGAMETHRESH = 100;
    int bbSpeed = 3; // BrickBreaker game initial speed

    // score visual effects
    private int scoreFlashSpeed = 100;
    private int scoreFlashTime = 1000;
    private int highlight_countdown = scoreFlashTime;
    private int coin_countdown = 700;

    // assignments
    public int AssignmentCountMax = how_many_questions;
    public int AssignmentCountCurrent = 0;
    int SubAssignCountMax = 0; // how many task in current word
    int SubAssignCountCurrent = 0; // sub-assignment count current points
    public AssignmentType assignmentType = AssignmentType.SYLLABLE;
    // TODO the 2 below are unused
    public Difficulty difficulty = Difficulty.EASY; // the user can choose
    public int GameLevel = 0; // from 0 to 6
    public int LevelType = 0;


    static Color scoreLabelColor;
    static Color medalColor;
    boolean soundOff = true;
    boolean useFalseVowel = true;
    boolean rem_hesofit;
    boolean rem_kub_hol;

    // preferences keys
    Preferences prefs;

    public boolean isSoundOn() {
        return !soundOff;
    }

    public void setSoundOff(boolean soundOff) {
        this.soundOff = soundOff;
    }

    public boolean isFalseVowel(){
        return useFalseVowel;
    }

    public void setFalseVowel(boolean falseVowel){
        this.useFalseVowel = falseVowel;
    }



    /**
     * enums
     */
    public enum AssignmentType {
        SYLLABLE, VOWEL, SPELLHINT, SPELL_ONE_LETTER, SPELL, KEYBOARD, GUESS
    }

    public enum Difficulty {
        EASY, MEDIUM, HARD, HARDEST
    }

    private boolean vowel_are_not_placed = true;
    static String bgImagePath;

    /**
     * constructor frame_with_buttons
     * @throws IOException
     */
    public AlephFrame() throws IOException {
        setLayout(new BorderLayout());
        setTitle("Aleph game - אלף");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(listener);

        // define dimensions
        int height = 500;
        int width = 800;
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
                .getScreenSize();
        int maxWidth = (int) (screenSize.width * 0.6);
        int maxHeight = (int) (screenSize.height * 0.6);
        height = Math.min(maxHeight, height);
        width = Math.min(maxWidth, width);

        // main menu
        createMenu();

        // load preferences
        getPreference();

        // main panel (will contains later floating buttons)
        bgImagePath = getResourcePath("/data/bg_images/") + "babyComputer.jpg";
        bgImagePath = prefs.get("bgImagePath", bgImagePath);
        mainPanel = new PlayPanel(bgImagePath, width, height);
        mainPanel.setBorder(new TitledBorder("main panel"));
        mainPanel.setLayout(null); // if I want buttons to flow around free,
        // this must be absolute (=null)
        add(mainPanel, BorderLayout.CENTER);

        // debug component to show pixels while mouse drag
        show = new JLabel("Pixels");
        show.setBounds(90, 87, 150, 15);
        show.setFont(new Font("Dialog", Font.BOLD, 12));
        show.setForeground(Color.BLUE);
        show.setBorder(new LineBorder(Color.red));
        show.setName("PixShow");
        // score component
        scoreLabel = new JLabel("Score:");
        scoreLabel.setBounds(250, 87, 150, 30);
        scoreLabel.setFont(new Font("Dialog", Font.BOLD, 30));
        scoreLabel.setForeground(Color.blue);
        scoreLabel.setOpaque(true);
        scoreLabel.setBorder(new EtchedBorder());
        scoreLabel.setText("Score: " + levelScore);
        // bottom panel contains score and status
        bottomPanel = new JPanel();
        bottomPanel.setBorder(new LineBorder(Color.red));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(scoreLabel);
        //		bottomPanel.add(show);
        //		medal = addMedal();
        scoreLabelColor = scoreLabel.getBackground();

        // fill database with new words from file
        String path_to_input = "/home/konisas/IdeaProjects/al/src/data/input.txt";
        if (System.getProperty("os.name").startsWith("Win"))
            path_to_input = "C:\\Users\\lkonis\\workspace\\data\\input.txt";
        //		String npath = AlephFrame.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        //		npath += "data/input.txt";
        //		log("npath: "+npath);
        //		path_to_input = "src/data/input.txt";
//		if (isFalseVowel())
        //		path_to_input = getResourcePath("/data/") + "input_falsevowels.txt";
        //else
        path_to_input = getResourcePath("/data/") + "input.txt";

        try {
            log("path to input: "+path_to_input);
            getWordsFromFile(path_to_input);
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }

        // prepare quest panel
        initNewQuest();
        // change height according to number of questions
        height += (height * how_many_questions * 5) / 100;
        setSize(width, height);
    }

    /**
     *
     */
    public JLabel addMedal(boolean doHighlight) {
        // level icon component
        JLabel medal = new JLabel();
        medal.setSize(10, 10);
        //		medal.setBorder(new LineBorder(Color.red));
        medal.setName("medal");
        iconpath = getResourcePath("/data/icon/");
        Icon icon = new ImageIcon(iconpath+"smiley-glad-icon.png");
        medal.setIcon(icon);
        bottomPanel.add(medal);
        medalColor = medal.getBackground();
        if (doHighlight)
        {
            highlightScore(medal, fallingCoinsTaskPerformer);
            String bbStrSpeed = Integer.toString(bbSpeed);
            // call brick game with speed and number of bricks as parameters
            bbSpeed += 1;
            ProcessBuilder pb = new ProcessBuilder("/usr/bin/java", "-jar", "/home/ubtosh/Downloads/bb.jar" , bbStrSpeed);
            pb.directory(new File("/home/ubtosh/Downloads/"));
            try {
                pb.start();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // TODO:			new BrickBreaker("10");
        }
        return medal;
    }

    /**
     * creating menu with mostly empty buttons - will be used for choosing
     * difficulty level
     */
    private void createMenu() {
        // create menu bar and add it to frame
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Define and add drop down menus to the menu bar
        //======================================
        // 1. Define and add tasks bar
        JMenu doMenu = new JMenu("תפריט");
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(doMenu);
        // Task bar submenus - Create and add simple menu item to one of the drop down menu
        JMenuItem newLevelNewBackground = new JMenuItem("רמה חדשה");
        JMenuItem resetGame = new JMenuItem("משחק חדש");
        // join all functions to menu items
        doMenu.add(newLevelNewBackground);
        doMenu.add(resetGame);
        // 2. add settings bar
        JMenu settingsMenu= new JMenu("הגדרות");
        menuBar.add(settingsMenu);
        // add sound toggle checkBox button and activate default value
        JCheckBoxMenuItem soundToggle = new JCheckBoxMenuItem("צליל");
        soundToggle.setSelected(true);
        // add sound mute to settings menu
        settingsMenu.add(soundToggle);
        // toggle false vowels option
        JCheckBoxMenuItem falseVowel = new JCheckBoxMenuItem("בלי תנועות");
        settingsMenu.add(falseVowel);


        // 3. help bar
        // Define and add help option
        JMenu helpMenu = new JMenu("תפריט עזרה");
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);
        // help sub menus
        JMenuItem helpAbout = new JMenuItem("אודות");
        JMenuItem helpMain = new JMenuItem("עזרה");
        // join all functions to menu items
        helpMenu.add(helpAbout);
        helpMenu.add(helpMain);

		/*		// Create and add Radio Buttons as simple menu items to one of the drop
		// down menu
		JRadioButtonMenuItem radioAction1 = new JRadioButtonMenuItem(
				"Radio Button1");
		JRadioButtonMenuItem radioAction2 = new JRadioButtonMenuItem(
				"Radio Button2");
		// Create a ButtonGroup and add both radio Button to it. Only one radio
		// button in a ButtonGroup can be selected at a time.
		ButtonGroup bg = new ButtonGroup();
		bg.add(radioAction1);
		bg.add(radioAction2);
		 */


        resetGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // reset preferences
                difficulty = Difficulty.EASY;
                assignmentType = AssignmentType.SYLLABLE;
                levelScore=0;
                difficultyScore=0;
                bbSpeed = 3;
                bgImagePath = getResourcePath("/data/bg_images/") + "babyComputer.jpg";
                setPreference();
                // reset score label and medals
                scoreLabel.setText("Score: 0");
                for (Component c: bottomPanel.getComponents())
                    if (c.getName()=="medal")
                        bottomPanel.remove(c);
            }

        });
        helpAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                log("HELP");
            }

        });
        helpMain.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Object jc = getRootPane().getParent();
                if (!(jc instanceof AlephFrame)) {
                    System.out.println("I was expected frame_with_buttons");
                    return;
                }
                //				AlephFrame fwb = (AlephFrame) jc;
                JDialog helpDialog = new JDialog();
                helpDialog.setVisible(true);
                helpDialog.setName("mainHelpDialog");
                helpDialog.setLayout(new BorderLayout());
                helpDialog.setSize(300, 300);
                //				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                helpDialog.setResizable(false);
                helpDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                String text2Help="";

                String path_to_input = getResourcePath("/data/") + "help.txt";
                Path path = Paths.get(path_to_input);
                try (Scanner scanner = new Scanner(path, ENCODING.name()))
                {
                    while (scanner.hasNextLine()) {
                        // process each line in some way
                        try{
                            String line = scanner.nextLine();
                            int string_offst=0;
                            if (line.startsWith("word=", string_offst))
                            {
                                log("start with word=");
                            }

                            text2Help += line;
                            text2Help += "\n";
                        }
                        catch (Exception e) {
                            log("main help: caught exception here");
                        }
                        finally {
//							log("main help: finally got here");
                        }
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                JLabel helpText = new JLabel(text2Help);
                helpText.setHorizontalAlignment(JLabel.CENTER);
                helpDialog.add(helpText);

            }

        });

        newLevelNewBackground.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                Object jc = getRootPane().getParent();
                if (!(jc instanceof AlephFrame)) {
                    System.out.println("I was expected AlephFrame");
                    return;
                }
                AlephFrame fwb = (AlephFrame) jc;
                // first clear any components from quest panel
                Rectangle newWinBounds = fwb.getBounds();
                //				newWinBounds.height /=1.5;
                newWinBounds.width /=2;
                log("hint listener");
                newAssignmentDialog(fwb, newWinBounds, AlephFrame.levelScore);
                if (bgImagePath!=null){
                    try {
                        fillNewAssignment(bgImagePath);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        });

        /**
         * sound toggle action
         */
        soundToggle.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setSoundOff(!soundOff);
/*				if (soundOff)
					soundOff = false;
				else
					soundOff = true;*/
            }
        });
        /**
         * falseVowel button
         */
        falseVowel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setFalseVowel(!useFalseVowel);
            }
        });
        // public void itemStateChanged(ItemEvent e) { }
    }


    static int debugCnt =0;
    /**
     * newAssignmentDialog
     * @param fwb
     * @param newWinBounds
     * @param score
     */
    public void newAssignmentDialog(AlephFrame fwb,
                                    Rectangle newWinBounds, int score)
    {
        JLabel topLabel = new JLabel();
        JPanel mainPanel = new JPanel();
        JLabel bottomLabel = new JLabel();
        JButton easiestBtn = new JButton();
        JButton easyBtn = new JButton();
        JButton mediumBtn = new JButton();
        JButton hardBtn = new JButton();

        final JDialog dialog = new JDialog(fwb);
        dialog.setModal(true);
        dialog.setBounds(newWinBounds);
        dialog.setName("mainAssChosFrame");
        dialog.setLayout(new BorderLayout());
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        mainPanel.setLayout(new GridLayout(4, 0, 10, 10));
        mainPanel.setBorder(new TitledBorder("Main panel"));
        dialog.add(mainPanel, BorderLayout.CENTER);
        topLabel.setText("בחר משימה");
        topLabel.setHorizontalAlignment(JLabel.RIGHT);
        topLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        dialog.add(topLabel, BorderLayout.NORTH);

        bottomLabel.setText("nothing here");
        bottomLabel.setBorder(new TitledBorder("Bottom Label"));
        dialog.add(bottomLabel, BorderLayout.SOUTH);

        // draw all options
        // define lock icon
        String iconpath = AlephFrame.getClassResourcePath("/data/icon/");
        //		ImageIcon ic = new ImageIcon("/home/ubtosh/workspace/Aleph/data/icon/lock-icon64.png");
        ImageIcon ic = new ImageIcon(iconpath+"lock-icon64.png");

        easiestBtn.setText("הכי קל");
        easiestBtn.setHorizontalAlignment(JLabel.RIGHT);
        easiestBtn.setFont(new Font("Serif", Font.PLAIN, 20));
        mainPanel.add(easiestBtn);

        easyBtn.setText("קל");
        easyBtn.setHorizontalAlignment(JLabel.RIGHT);
        easyBtn.setFont(new Font("Serif", Font.PLAIN, 20));
        easyBtn.setEnabled(false);
        easyBtn.setIcon(ic);
        mainPanel.add(easyBtn);

        mediumBtn.setText("בינוני");
        mediumBtn.setHorizontalAlignment(JLabel.RIGHT);
        mediumBtn.setFont(new Font("Serif", Font.PLAIN, 20));
        mediumBtn.setEnabled(false);
        mediumBtn.setIcon(ic);
        mainPanel.add(mediumBtn);


        hardBtn.setText("קשה");
        hardBtn.setHorizontalAlignment(JLabel.RIGHT);
        hardBtn.setFont(new Font("Serif", Font.PLAIN, 20));
        hardBtn.setEnabled(false);
        hardBtn.setIcon(ic);
        mainPanel.add(hardBtn);
        if (score > fwb.EASYTHRESH)
        {
            easyBtn.setEnabled(true);
            easyBtn.setIcon(null);
        }
        if (score > fwb.MEDIUMTHRESH)
        {
            mediumBtn.setEnabled(true);
            mediumBtn.setIcon(null);
        }
        if (score > fwb.HARDTHRESH)
        {
            hardBtn.setEnabled(true);
            hardBtn.setIcon(null);
        }
        // trying another way to implement (see below)
        ActionListener easyBtnActionImplementer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                assignmentType = AssignmentType.VOWEL;
                bgImagePath = getResourcePath("/data/bg_images/") + "springGiraffe.jpg";
                dialog.dispose();
            }
        };

        // register easy button event to a dedicated listener object
        easyBtn.addActionListener(easyBtnActionImplementer);

        // register easiest button event to a dedicated listener object
        easiestBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Component qb= getRootPane().getParent();
                System.out.println("easiset clicked: "+debugCnt+". qb:"+qb.toString());
                debugCnt++;
                assignmentType = AssignmentType.SYLLABLE;
                bgImagePath = getResourcePath("/data/bg_images/") + "babyComputer.jpg";
                dialog.dispose();
            }
        });
        // register medium button event to a dedicated listener object
        mediumBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Component qb= getRootPane().getParent();
                System.out.println("medium clicked: "+debugCnt+". qb:"+qb.toString());
                debugCnt++;
                assignmentType = AssignmentType.SPELLHINT;
                bgImagePath = getResourcePath("/data/bg_images/") + "girlApple.jpg";
                dialog.dispose();
            }
        });

        // register hard button event to a dedicated listener object
        hardBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Component qb= getRootPane().getParent();
                System.out.println("hard clicked: "+debugCnt+". qb:"+qb.toString());
                debugCnt++;
                assignmentType = AssignmentType.SPELL;
                bgImagePath = getResourcePath("/data/bg_images/") + "rainbow.jpg";
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    /**
     * this is called from NewAssignment and perform the rest of the tasks
     * @throws IOException
     */
    public void fillNewAssignment(String bgfilename) throws IOException {
        fillNewAssignment();
        //		questPanel.removeAll();
        //		mainPanel.removeAll();
        mainPanel.replaceBackgroundImage(bgfilename);
        mainPanel.repaint();
        mainPanel.revalidate();
        //		fillNewQuest();
        //		questPanel.revalidate();
    }

    /**
     * Propose the same assignment - this is a minimized version of New assignment without replacing background
     */
    public void fillNewAssignment()  {
        questPanel.removeAll();
        mainPanel.removeAll();
        //		mainPanel.replaceBackgroundImage(bgfilename);
        mainPanel.revalidate();
        fillNewQuest();
        questPanel.revalidate();
    }

    /**
     * fill right panel with question/images
     *
     *
     */
    public void fillNewQuest() {
        Icon ic;
        ArrayList<hebWord> wordsSubset;
        wordsSubset = selectWords(words); // select supset according to difficulty
        Collections.shuffle(wordsSubset);
        wordsSubset = GenSubList(wordsSubset, how_many_questions);
        QuestionButton questButton;
        // insert question buttons with names and icons (take first N from shuffled list)
        for (int word_indx = 0; word_indx < wordsSubset.size(); word_indx++) {
            questButton = new QuestionButton(wordsSubset.get(word_indx));// .toString());
            if (assignmentType.equals(AssignmentType.SPELL)) // if without hint
                questButton.setText("");
            questButton.addActionListener(this);
            ic = new ImageIcon(wordsSubset.get(word_indx).getIconPath());
            questButton.setIcon(ic);
            buttons.add(questButton);
            questPanel.add(questButton);
        }
        mainPanel.removeAll(); // remove all components but JLabels
        mainPanel.revalidate();
    }

    /**
     * select words according to current difficulty
     * @param words
     * @return
     */
    private ArrayList<hebWord> selectWords(ArrayList<hebWord> words) {
        ArrayList<hebWord> selectedWordArray = new ArrayList<>();
        switch (difficulty) {
            case EASY:
                for (hebWord hw: words)
                {
                    if (testEasyness(hw)<2)
                        selectedWordArray.add(hw);
                }
                break;
            case MEDIUM:
                for (hebWord hw: words)
                {
                    if (testEasyness(hw)<6)
                        selectedWordArray.add(hw);
                }
                break;
            case HARD:
                for (hebWord hw: words)
                {
                    if (testEasyness(hw)<10)
                        selectedWordArray.add(hw);
                }
                break;
            default: // HARDEST
                for (hebWord hw: words)
                {
                    selectedWordArray.add(hw);
                }
                break;
        }
        return selectedWordArray;
    }

    /**
     * testEasyness
     * @param hw
     * @return
     */
    public int testEasyness(hebWord hw) {
        int thislevel=0;
        //		log(hw.getWord());
        for (hebLetter hl: hw.getWord())
        {
            thislevel+=hl.level;
            if (hl.vowel== Vowel.KUBUTZ)
                thislevel+=3;
            if (hl.vowel == Vowel.HOLAM)
                thislevel+=4;
            if (hl.vowel == Vowel.TZERE)
                thislevel+=2;
        }
        thislevel += Math.max(0,(hw.length-3));
        thislevel += hw.level; // add extra level reported from input
        return thislevel;
    }

    /**
     * fill for first time right panel with question/images
     */
    private void initNewQuest() {

        questPanel = new JPanel();
        questPanel.setName("questPanel");
        questPanel.setBorder(new TitledBorder("Quest"));
        questPanel.setLayout(new GridLayout(0, 1, 0, 10));

        fillNewQuest();
        add(questPanel, BorderLayout.EAST);
    }

    /**
     * what to do when quest button is pressed
     */
    public void actionPerformed(ActionEvent e) {
        QuestionButton thisbutton = (QuestionButton) e.getSource();
        thisbutton.isPressed = true;
        // set all other question buttons to disabled until this one is complete
        for (QuestionButton qb : buttons) {
            if (!(qb == thisbutton)) {
                qb.setEnabled(false);
            }
        }
        // TODO: complete all other words with "say"
        if (isSoundOn())
        {
            //			log("thisbutton saypath: "+thisbutton.hebword.getSayPath());
			/*			MP3_say wordsay = new MP3_say(thisbutton.hebword.getSayPath());
			wordsay.play();
			 */		}
        // create new task, i.e. put new letters and empty letter box in playing
        // area
        // dealBoard(thisbutton.getText());
        vowel_are_not_placed = true;
        mainPanel.removeAll();
        mainPanel.repaint();
        dealBoard(thisbutton.hebword);
    }

    /**
     * fill word database with words from file
     *
     * @param aFileName
     * @throws IOException
     */
    public void getWordsFromFile(String aFileName) throws IOException {
        Path path = Paths.get(aFileName);
        try (Scanner scanner = new Scanner(path, ENCODING.name())) {
            iconpath = getResourcePath("/data/icon/");
            saypath = getResourcePath("/data/sounds/words/");
            hebWord hw = null;
            int string_offst = 0;
            boolean[] letters_to_remove=null;

            while (scanner.hasNextLine()) {
                // process each line in some way
                try{
                    String line = scanner.nextLine();
                    if (line.startsWith("word=", string_offst)) {
                        line = line.substring(5 + string_offst);
                        if (line.equals(""))
                            break;
                        hw = new hebWord(line);
                        string_offst = 0; // it happened only in first line
                    } else if (line.startsWith("icon=")) {
                        line = line.substring(5);
                        if (hw != null) {
                            String iconFullPath = iconpath + line + ".png";
                            hw.setIconPath(iconFullPath);
                        }
                    } else if (line.startsWith("vowel=")) {
                        line = line.substring(6);
                        try
                        {
                            hw.addVowels(line, isFalseVowel());
                        }
                        catch (Exception e){
                            log(e);
                        }
                        words.add(hw);

                    } else if (line.startsWith("say=")) {
                        line = line.substring(4);
                        if (hw != null) {
                            String sayFullPath = saypath + line + ".mp3";
                            hw.setSayPath(sayFullPath);
                        }
                    } else if (line.startsWith("level=")) {
                        line = line.substring(6);
                        if (hw != null) {
                            hw.level = Integer.parseInt(line);
                        }
                    }
                }
                catch (Exception e) {
                    log("caught exception here");
                }
                finally {
                    if (use_full_log)
                        log("read input.txt: finally got here");
                }
            }
        }
        catch (Exception e){
            log("cannot open file: "+e);
        }
    }

    /**
     * deal new buttons, vowel buttons and empty letters box
     * all according to assignmentTypes
     * @param chose_word
     */
    public void dealBoard(hebWord chose_word) {
        // first clear any components from main panel
        mainPanel.removeAll();
        mainPanel.revalidate();

        SubAssignCountMax = chose_word.vowelCount; // this is the assignment
        SubAssignCountCurrent = 0;

        int offs_y = 150;
        int fontSize = (int) Math.round(boxSize * .6);
        hebWord hw = chose_word;
        Random rnd = new Random();
        int mainPanelWidthLimits = getMainPanelLimits();

        // syllable assignment
        if (assignmentType==AssignmentType.SYLLABLE)
        {
            syllableIn = new JTextField("");
            Rectangle r = mainPanel.getBounds();
            int inputSize = 30;

            r.x += Math.round(r.width*.8) - inputSize;
            r.y += Math.round(r.height*.7 - inputSize);
            r.width = inputSize*2;
            r.height = inputSize*2;
            syllableIn.setBounds(r);
            syllableIn.setAlignmentX(CENTER_ALIGNMENT);
            syllableIn.setFont(new Font("Serif", Font.PLAIN, inputSize));
            syllableIn.setForeground(Color.RED);
            syllableIn.addActionListener(syllableCnt);

            syllableIn.setVisible(true);
            mainPanel.add(syllableIn, 0);
            mainPanel.repaint();
            syllableIn.requestFocus();
            syllableIn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    log("syllable pressed");
                }

            });
            KeyListener keyListener = new KeyListener() {
                public void keyPressed(KeyEvent keyEvent) {
                    syllableIn.setText("");
                    printIt("Pressed", keyEvent);
                }
                public void keyReleased(KeyEvent keyEvent) {
                    printIt("Released", keyEvent);
                    checkIt("Released", keyEvent);
                }
                public void keyTyped(KeyEvent keyEvent) {
                    //					printIt("Typed", keyEvent);
                }
                private void printIt(String title, KeyEvent keyEvent) {
                    int keyCode = keyEvent.getKeyCode();
                    String keyText = KeyEvent.getKeyText(keyCode);
                    System.out.println(title + " : " + keyText+". cnt="+SubAssignCountMax);
                }
                private void checkIt(String title, KeyEvent keyEvent) {
                    int keyCode = keyEvent.getKeyCode();
                    String keyText = KeyEvent.getKeyText(keyCode);
                    String soundEffectPath = getResourcePath("/data/sounds/effects/");
                    if (SubAssignCountMax== Integer.parseInt(keyText))
                    {
                        soundEffectPath += "magic-chime-02";
                        questAccomplished("onIncreaseAssignmentCnt");
                        syllableIn.setEnabled(false);
                    }
                    else
                    {
                        decreaseScore();
                        scoreLabel.setText("Score: " + levelScore);
                        soundEffectPath += "drumTimpany";
                    }
                    if (isSoundOn())
                        new Wav_say(soundEffectPath+".wav");

                }
            };
            syllableIn.addKeyListener(keyListener);

        }
        // spread empty letter boxes - not for first assignments
        shuffleLetterBox(offs_y, hw, mainPanelWidthLimits);

        // spread letter buttons (randomly or not)
        shuffleLetterButtons(offs_y, fontSize, hw, rnd, mainPanelWidthLimits);

        // spread vowel in place at bottom
        if (assignmentType.equals(AssignmentType.VOWEL)) {
            //			if (System.getProperty("os.name").startsWith("Win"))
            //				vowelpath = "C:\\Users\\lkonis\\workspace\\Aleph\\data\\vowels\\";
            vowelpath = getResourcePath("/data/vowels/");
            // fillNewQuest()
            for (int i = 0; i < vowelIcons.length; i++) {
                // placing vowel icons that can be dragged
                // they are placed on top of static images
                dealFloatingVowel(mainPanelWidthLimits, i, vowel_are_not_placed);
            }
            if (vowel_are_not_placed) {
                // log("vowel label are placed only first time");
                vowel_are_not_placed = false;
            }
            // put empty labels for vowels, only for VOWEL quest
            for (int i = 0; i < vowelIcons.length; i++) {
                //				dealStaticVowelLabel(mainPanel.getWidth(), i, true);
            }
        }
    }

    ActionListener syllableCnt = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            log("syllableCnt action listener");
        }
    };

    /** shuffle either letter or vowel buttons
     * @param offs_y
     * @param fontSize
     * @param hw
     * @param rnd
     * @param mainPanelWidthLimits
     */
    private void shuffleLetterButtons(int offs_y, int fontSize, hebWord hw,
                                      Random rnd, int mainPanelWidthLimits) {
        for (int c = 0; c < hw.length; c++) {
            LetterButton dr = new LetterButton(hw.getChar(c),
                    mainPanelWidthLimits
                            - (offs_x_align + (c + 1)
                            * (delt_x_align + boxSize)), offs_y,
                    mainPanel.getSize(), minOverlap);
            Rectangle r = dr.getBounds();
            r.height = boxSize;
            r.width = boxSize;
            dr.setFont(new Font("Serif", Font.PLAIN, fontSize));
            dr.setBounds(r);
            dr.setName("drB" + c);
            mainPanel.add(dr, 0);
            // if first or second assignments - then not randomly
            if (assignmentType.equals(AssignmentType.VOWEL)||assignmentType.equals(AssignmentType.SYLLABLE)) {
                // TODO: LetterButtons are placed immediately on LetterBoxes
                // log("LetterButtons are placed immediately on LetterBoxes");
                dr.stopMove = true;
                dr.matchLetterBox = true;
            } else {

                // now try to locate button in random place but without
                // Overriding any other object
                int rnd_x = rnd.nextInt(mainPanel.getSize().width - boxSize);
                int rnd_y = rnd.nextInt(mainPanel.getSize().height - boxSize);
                while (!(dr.setButtonLocation(rnd_x, rnd_y))) {
                    rnd_x = rnd.nextInt(mainPanel.getSize().width - boxSize);
                    rnd_y = rnd.nextInt(mainPanel.getSize().height - boxSize);
                    log("in while loop");
                }
            }
        }
    }

    /** Shuffle empty letter boxes
     * @param offs_y
     * @param hw
     * @param mainPanelWidthLimits
     */
    private void shuffleLetterBox(int offs_y, hebWord hw, int mainPanelWidthLimits) {
        if ((assignmentType.equals(AssignmentType.SPELL)||(assignmentType.equals(AssignmentType.SPELLHINT))||(assignmentType.equals(AssignmentType.SYLLABLE))))
            for (int c = 0; c < hw.length; c++) {
                // empty text box
                LetterBox tf = new LetterBox("", mainPanelWidthLimits
                        - offs_x_align - (c + 1) * (delt_x_align + boxSize),
                        offs_y, boxSize, hw.getChar(c).letter,
                        hw.getChar(c).vowel);
                mainPanel.add(tf, 1);
            }
    }

    /**
     * deal floating vowels images
     *
     * @param questWidthLimits
     * @param i
     * @param vowel_is_not_placed
     */
    public void dealFloatingVowel(int questWidthLimits, int i,
                                  boolean vowel_is_not_placed) {
        if (!(vowel_is_not_placed))
            return;
        Icon ic;
        Vowel v = null;
        try {
            v = hebLetter.getVowelfromIndex(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        hebLetter hl = new hebLetter('0', v);
        Point p = new Point();
        p.x = questWidthLimits - ((i + 1) * (delt_x_align + vowelSize));
        p.y = mainPanel.getSize().height - 2 * vowelSize;
        LetterButton vw = new LetterButton(hl, p.x, p.y, mainPanel.getSize(),
                minOverlap);
        Rectangle r = new Rectangle();// = vw.getBounds();
        r.x = p.x;
        r.y = p.y;
        r.height = vowelSize;
        r.width = vowelSize;
        vw.setBounds(r);
        vw.setName("voB" + i);
        vw.setFont(new Font("Dialog", Font.BOLD, 20));
        vw.setOpaque(false);
        vw.setContentAreaFilled(false);

        // now get icon with transparent rectangle for Vowel LetterButton
        String iconFullPath = vowelpath + vowelFullIcons[i] + ".png";
        Icon icon = new ImageIcon(iconFullPath);

        // generate graphics object to allow scaling
        //		double scaleAmount = 1.0;
        //		ic =scaleIcon(icon, scaleAmount);
        ic = icon;
        vw.setText(""); // so it won't print '0'
        vw.setIcon(ic);
        mainPanel.add(vw, 0);
    }

    /** Scale image for icon
     * @param icon
     * @param scaleAmount
     */
    public Icon scaleIcon(Icon icon, double scaleAmount) {
        Icon ic;
        BufferedImage bi;
        Graphics2D g;
        // scale icon size to match label size
        double scale = (double) ((double) vowelSize / (double) icon
                .getIconWidth()) * scaleAmount;
        bi = new BufferedImage((int) (scale * icon.getIconWidth()),
                (int) (scale * icon.getIconHeight()),
                BufferedImage.TYPE_INT_ARGB);
        g = bi.createGraphics();
        g.scale(scale, scale);
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        ic = new ImageIcon(bi);
        return ic;
    }

    /**
     * deal static label containing vowels images
     *
     * @param containingPanelWidthLimits
     * @param i
     * @param vowel_are_not_placed
     */
    public void dealStaticVowelLabel(int containingPanelWidthLimits, int i,
                                     boolean vowel_are_not_placed) {
        if (vowel_are_not_placed) {
            Rectangle r1 = new Rectangle();// = vw.getBounds();
            r1.x = containingPanelWidthLimits
                    - ((i + 1) * (delt_x_align + vowelSize));
            r1.y = mainPanel.getSize().height - 2 * vowelSize;
            r1.height = vowelSize;
            r1.width = vowelSize;
            JLabel vw_lb = placeVowels(r1);
            mainPanel.add(vw_lb, 0);
        }
    }

    /**
     * get main panel limits to allows limit movement
     *
     * @return
     */
    public int getMainPanelLimits() {
        // spread empty letter boxes in place at top
        int questWidthLimits = getSize().width - questPanel.getSize().width
                - getInsets().left;
        return questWidthLimits;
    }

    /**
     * place static labels with nice images to represent hebrew vowels placed
     * immediately
     *
     * @param r
     *            dimension (rectangle)
     * @return JLabel object
     */
    private JLabel placeVowels(Rectangle r) {
        JLabel vw_lb = new JLabel();
        vw_lb.setName("vw_lb" + r.x);
        vw_lb.setBounds(r);
        vw_lb.setBackground(Color.blue);
        vw_lb.setBorder(new LineBorder(Color.red));

        /*
         * String iconFullPath1 = vowelpath + vowelIcons[i] + ".png"; Icon icon1
         * = new ImageIcon(iconFullPath1); double scale1 = (double) ((double)
         * vowelSize / (double) icon1 .getIconWidth()) * .8; // scale icon
         * BufferedImage bi = new BufferedImage( (int) (scale1 *
         * icon1.getIconWidth()), (int) (scale1 * icon1.getIconHeight()),
         * BufferedImage.TYPE_INT_ARGB); Graphics2D g = bi.createGraphics();
         * g.scale(scale1, scale1); icon1.paintIcon(null, g, 0, 0); g.dispose();
         * ic = new ImageIcon(bi); vw_lb.setIcon(ic);
         */
        return vw_lb;
    }

    /**
     * generate sublist from n first elements
     *
     * @param words
     * @param n
     * @return
     */
    private ArrayList<hebWord> GenSubList(ArrayList<hebWord> words, int n) {
        ArrayList<hebWord> words_sublist = new ArrayList<hebWord>();
        for (int i = 0; i < n; i++) {
            hebWord tmpHW = words.get(i);
//			tmpHW.simplify_word(useFalseVowel, rem_kub_hol);
            words_sublist.add(tmpHW);

        }
        return words_sublist;
    }

    /** polish a given word according to rules to make it easy to learn
     * the rules can be:
     * 1. omit the he at end of word	 parameter: rem_hesofit
     * 2. replace holam with holam haser parameter: rem_kub_hol
     * 3. replace shuru with kubuts
     *
     * @param hebWord
     * @return heb word according to mitigation rules
     *
     */
    // TODO: this should be in hebWord class
    private hebWord polish_word(hebWord hebWord) {
        if (rem_kub_hol)
        {

        }
        if (rem_hesofit){


        }


        return hebWord;
    }

    /**
     * disabled completed quest and set all other question buttons to 'enabled'
     */
    public void questAccomplished(String inArg) {
        highlight_countdown = scoreFlashTime;
        AssignmentCountCurrent++;
        increaseScore();
        if (/*inArg!="onIncreaseAssignmentCnt" &&*/ AssignmentCountCurrent == AssignmentCountMax) {
            highlight_countdown = scoreFlashTime;
            highlightScore(scoreLabel, highlightBonusTaskPerformer);

            AssignmentCountCurrent = 0;
            log("Quest DONE");
        }
        else
        {
            //			increaseScore(); // increase score only once
            scoreLabel.setText("Score: " + levelScore);
            highlightScore(scoreLabel, highlightTaskPerformer);
        }
        //		reEnableQuestButtons();
    }

    /**
     *
     */
    public void increaseDifficulty() {
        if (difficulty == Difficulty.EASY && levelScore >= LEVEL1THRESH)
        {
            difficulty = Difficulty.MEDIUM;
            System.out.println("(difficulty == Difficulty.EASY && levelScore > LEVEL1THRESH)");
            medal = addMedal(true);
        }
        if (difficulty == Difficulty.MEDIUM && levelScore >= LEVEL2THRESH)
        {
            difficulty = Difficulty.HARD;
            System.out.println("(difficulty == Difficulty.MEDIUM && levelScore > LEVEL2THRESH");
            medal = addMedal(true);
        }
        if (difficulty == Difficulty.HARD && levelScore >= LEVEL3THRESH)
        {
            difficulty = Difficulty.HARDEST;
            System.out.println("(difficulty == Difficulty.HARD && levelScore > LEVEL3THRESH)");
            medal = addMedal(true);
        }
    }

    /**
     *
     */
    public void reEnableQuestButtons() {
        for (QuestionButton qb : buttons) {
            if (qb.isPressed) {
                qb.setEnabled(false);
                qb.completed = true;
                qb.isPressed = false;
            } else if (!qb.completed) {
                qb.setEnabled(true);
            }
        }
    }

    public void increaseScore() {
        levelScore += deltaScore;
        increaseDifficulty();
    }

    public void decreaseScore() {
        levelScore -= deltaScore;
        if (levelScore<0)
            levelScore=0;
        scoreLabel.setText("Score: " + levelScore);
    }

    /**
     * use Timer to flash several times
     * @param scorelabel
     */
    private void highlightScore(JLabel scorelabel, ActionListener hl) {
        int delay = 100; // milliseconds
        Timer timer = new Timer(delay, hl);
        timer.start(); // it will repeat until stopped from within ActionListener
    }

    /**
     * ActionListener for highlightBonus
     */
    ActionListener highlightBonusTaskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            if (!scoreLabel.getBackground().equals(Color.yellow)) {
                scoreLabel.setBackground(Color.yellow);
            } else {
                scoreLabel.setBackground(scoreLabelColor);
            }
            scoreLabel.setText("Score: " + levelScore);
            GameLevel+=1;
            // sound of falling coins
			/*			if (isSoundOn()) {
				String path_to_effect = getResourcePath("/data/sounds/effects/");
				MP3 mp3 = new MP3(path_to_effect+"coins.mp3");
				mp3.play();
			}
			 */
            highlight_countdown -= scoreFlashSpeed;
            if (highlight_countdown <= 0)
            {
                ((Timer) evt.getSource()).stop();
                // propose new assignment of same type
                fillNewAssignment();
            }
        }
    };
    /**
     * ActionListener for highlightBonus
     */
    ActionListener fallingCoinsTaskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
			/*			if (medal==null) // that is,
			{
				log("not first time medal");
				return;
			}
			 */			if (!medal.getBackground().equals(Color.yellow)) {
                medal.setBackground(Color.yellow);
            } else {
                medal.setBackground(medalColor);
            }
            // sound of falling coins
            if (isSoundOn()) {
                String path_to_effect = getResourcePath("/data/sounds/effects/");
                MP3_say mp3 = new MP3_say(path_to_effect+"coins.mp3");
                mp3.play();
            }
            coin_countdown -= scoreFlashSpeed;
            if (coin_countdown <= 0)
            {
                ((Timer) evt.getSource()).stop();
            }
        }
    };

    /**
     * This ActionListener serves the highlight score it flashes with two colors
     * in 100ms rate highlight_countdown counts down until 0 - then flash stops
     */
    ActionListener highlightTaskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            if (!scoreLabel.getBackground().equals(Color.yellow)) {
                scoreLabel.setBackground(Color.yellow);
            } else {
                scoreLabel.setBackground(scoreLabelColor);
            }
            highlight_countdown -= scoreFlashSpeed ;
            if (highlight_countdown <= 0)
            {
                ((Timer) evt.getSource()).stop();
                disableVowels();
                reEnableQuestButtons();
            }
        }
    };

    /**
     * get class resource static method
     * @param resPath
     * @return
     */
    public static String getClassResourcePath(String resPath)
    {
        JFrame fwb = null;
        // first check that frame object exists since we need it to call getClass() method

        for (Frame frame: AlephFrame.getFrames())
            if (frame.getName()=="mainframe")
                fwb = (AlephFrame) frame;
        if (fwb==null)
            return "";


        int respathOffset = 5;
        if (System.getProperty("os.name").startsWith("Win"))
            respathOffset=6;

        URL resurl = fwb.getClass().getResource(resPath);
        String respath=resurl.toString();
        if (respath.length() > 1)
        {
            if (respath.charAt(respath.length() - 1) == '/')
            {
                respath.substring(0, respath.length() - 2);
            }

            if (respath.substring(0,6).equalsIgnoreCase("file:/"))
            {
                respath = respath.substring(respathOffset, respath.length());
            }
        }
        return respath;

    }
    /**
     * get resource full path
     * @param resPath resource relative path in the form /.../
     * @return string of full path
     * @throws Exception
     */
    public String getResourcePath(String resPath) {
        @SuppressWarnings("unused")
        int respathOffset = 5;
        if (System.getProperty("os.name").startsWith("Win"))
            respathOffset=6;
        URL resurl = AlephFrame.class.getResource(resPath);
        if (resurl==null)
        {
            log("OO, res url is null. resPath is: "+resPath);
        }
        else
        {
            if (use_full_log)
                log("res url is fine. resPath is: "+resPath);
        }
        String respath = resurl.toString();
        if (respath.length() > 1)
        {
            if (respath.charAt(respath.length() - 1) == '/')
            {
                log(respath.substring(0, respath.length() - 1));
            }

            String[] respaths = respath.split(":");
            if (respaths.length>1)
                respath = respaths[1];
			/*
			if (respath.substring(0,6).equalsIgnoreCase("file:/"))
			{
				respath = respath.substring(respathOffset, respath.length());
			}
			 */		}
        return respath;
    }

    /**
     * after completing word, disable option vowels until next question is set
     */
    protected void disableVowels() {
        for (Component c: mainPanel.getComponents())
        {
            if (!(c instanceof LetterButton))
                continue;
            LetterButton lb = (LetterButton)c;
            //			if (lb.letter=="vowel")
            //				lb.setEnabled(false);
            lb.setEnabled(false);
            lb.stopMove=true;
        }
    }

    /**
     * set preferences
     */
    public void setPreference() {
        // This will define a node in which the preferences can be stored
        prefs = Preferences.userRoot().node(this.getClass().getName());
        int saveDifficulty = 0;
        switch (difficulty){
            case EASY:
                saveDifficulty=0;
                break;
            case MEDIUM:
                saveDifficulty=1;
                break;
            case HARD:
                saveDifficulty=2;
                break;
            case HARDEST:
                saveDifficulty=3;
                break;
        }
        int saveAssignmentType = 0;
        switch (assignmentType)
        {
            case SYLLABLE:
                saveAssignmentType=0;
                break;
            case VOWEL:
                saveAssignmentType = 1;
                break;
            case SPELLHINT:
                saveAssignmentType = 2;
                break;
            case SPELL_ONE_LETTER:
                saveAssignmentType = 3;
                break;
            case SPELL:
                saveAssignmentType = 4;
                break;
            case KEYBOARD:
                saveAssignmentType = 5;
                break;
            case GUESS:
                saveAssignmentType = 6;
                break;
        }

        prefs.putInt("difficulty", saveDifficulty);
        prefs.putInt("assignmentType", saveAssignmentType);
        prefs.put("playerName", playerName);
        prefs.putInt("levelScore", levelScore);
        prefs.putInt("difficultyScore", difficultyScore);
        prefs.put("bgImagePath", bgImagePath);
        prefs.putBoolean("sound",soundOff);
        prefs.putBoolean("falseVowel", useFalseVowel);
        prefs.putBoolean("rem_hesofit", rem_hesofit);
        prefs.putBoolean("rem_kub_hol", rem_kub_hol);
        prefs.putInt("bbSpeed", bbSpeed);
    }

    /**
     *
     */
    public void getPreference() {
        prefs = Preferences.userRoot().node(this.getClass().getName());
        int saveDifficulty = prefs.getInt("difficulty", 0);
        switch (saveDifficulty){
            case 0:
                difficulty = Difficulty.EASY;
                break;
            case 1:
                difficulty = Difficulty.MEDIUM;
                break;
            case 2:
                difficulty = Difficulty.HARD;
                break;
            case 3:
                difficulty = Difficulty.HARDEST;
                break;
        }

        int saveAssignmentType = prefs.getInt("assignmentType", 0);

        switch (saveAssignmentType)
        {
            case 0:
                assignmentType = AssignmentType.SYLLABLE;
                break;
            case 1:
                assignmentType = AssignmentType.VOWEL;
                break;
            case 2:
                assignmentType = AssignmentType.SPELLHINT;
                break;
            case 3:
                assignmentType = AssignmentType.SPELL_ONE_LETTER;
                break;
            case 4:
                assignmentType = AssignmentType.SPELL;
                break;
            case 5:
                assignmentType = AssignmentType.KEYBOARD;
                break;
            case 6:
                assignmentType = AssignmentType.GUESS;
                break;
        }
        playerName = prefs.get("playerName",playerName);
        levelScore = prefs.getInt("levelScore", levelScore);
        difficultyScore = prefs.getInt("difficultyScore", difficultyScore);
        String bgImagePath = getResourcePath("/data/bg_images/") + "babyComputer.jpg";
        bgImagePath = prefs.get("bgImagePath", bgImagePath);
        soundOff = prefs.getBoolean("sound",soundOff);
        useFalseVowel = prefs.getBoolean("falseVowel", useFalseVowel);
        rem_hesofit = prefs.getBoolean("rem_hesofit", rem_hesofit);
        rem_kub_hol = prefs.getBoolean("rem_kub_hol", rem_kub_hol);
        bbSpeed = prefs.getInt("bbSpeed", bbSpeed);

    }

    private WindowAdapter listener = new WindowAdapter() {

        @Override
        public void windowClosing(WindowEvent e) {
            log("window is closing");
            setPreference();
        }
    };

    /**
     * main function
     * @param args
     * @throws UnsupportedLookAndFeelException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException, IOException {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");// "javax.swing.plaf.metal.MetalLookAndFeel");
        AlephFrame frame = new AlephFrame();
        frame.setName("mainframe");
        frame.getContentPane().setName("frame's root pane");
        log("component count: " + mainPanel.getComponentCount());
        Component[] comps = mainPanel.getComponents();
        for (Component c : comps) {
            log(c.getName() + ", ");
        }
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // load last saved preferences
        Preferences prefs = Preferences.userRoot().node(frame.getClass().getName());
        int saveDifficulty = prefs.getInt("difficulty", 0);
        // restore the amount of medals
        for (int i=0; i<saveDifficulty; i++)
        {
            frame.addMedal(false);
        }
        if (saveDifficulty>0)
            log("There are already "+saveDifficulty+" medals");

        // start immediately with level chooser
        // NewAssignmentChooser nas = new NewAssignmentChooser(frame);
        // nas.setBounds(frame.getBounds());
        // nas.toFront();
    }

    /**
     * helper function for debug
     *
     * @param x
     * @param y
     */
    public static void ShowPix(int x, int y) {
        if (show != null) {
            show.setText("Pixels. X: " + x + " y:" + y);
        }
    }

    /**
     * helper function for debug
     *
     * @param aMsg
     */
    private static void log(Object aMsg) {
        System.out.println(String.valueOf(aMsg));
    }
}
