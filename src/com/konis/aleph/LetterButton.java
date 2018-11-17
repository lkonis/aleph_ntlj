package com.konis.aleph;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;

import com.konis.aleph.AlephFrame.AssignmentType;
import com.konis.aleph.hebLetter.Vowel;
/**
 * A floating button that can be either letter or vowel
 * @author ubtosh
 *
 */
public class LetterButton extends JButton{

    private static final long serialVersionUID = 1L;

    // graphic constants
    static int boxSize=60; // TODO: take size from calling function
    static int letter_y_offset=50;// TODO: calculate from above boxSize
    private int minOverlap;
    // constant colors
    Color bgBtn1 = new Color(50, 250, 50, 200);
    Color fgBtn1 = new Color(20,15,150);
    Color bgVwlBtn = new Color(150, 150, 50, 120);
    Color fgVwlBtn = new Color(20,15,250);

    // graphical components
    LetterBox foundOverlappedBox;
    JComponent overlapComponent;
    static JLabel show;

    // state variables
    private volatile int draggedAtX, draggedAtY;
    Point lastButtonLocation;
    public boolean stopMove=false;		// lock this button in position
    public boolean matchLetterBox=false;	//> this button match a letter box
    private boolean imVowelThatMatch = false;	//< this Letter button match Vowel button
    String letter;							// the printed letter on this button
    public Vowel vowel = null;				// null if LetterBox, the attached vowel if vowel
    boolean dagesh;
    //	boolean isMouseReleased=true;
    //	boolean nothingOverlaps=true;

    public Vowel getVowel() {
        return vowel;
    }

    public int getVowelIndex(){
        switch (vowel){
            case PATAH:
                return 0;
            case TZERE:
                return 1;
            case HIRIK:
                return 2;
            case HOLAM:
                return 3;
            case KUBUTZ:
                return 4;
            default:
                return -1;

        }
    }

    public void setVowel(Vowel vowel) {
        this.vowel = vowel;
    }

    public void setDagesh(boolean dagesh){
        this.dagesh=dagesh;
    }

    /**
     * paint this function replace any other graphics for buttons
     */
    public void paint(Graphics g) {
        int localBoxsize = boxSize;
        int pointSize=6;
        int curvSize=10;
        Graphics2D g2d = (Graphics2D) g;
        // case this is a letter and not vowel
        if (getLetter()!="vowel")
        {
            g2d.setColor(bgBtn1);
            g2d.fillRoundRect(0, 0, localBoxsize, localBoxsize, curvSize, curvSize);
            g2d.setColor(fgBtn1);
            double scaleText = 1.5;
            g2d.scale(scaleText, scaleText);
            g2d.drawString(getText(), (int) (localBoxsize*.2), (int) (localBoxsize*.45));
            g2d.scale(1/scaleText, 1/scaleText);
            if (dagesh){
                if (getLetter()=="shin")
                    g2d.fillOval(localBoxsize - 10, (int)(localBoxsize*0.1),
                            pointSize, pointSize);
                else
                    g2d.fillOval((int) (localBoxsize*.45),(int) (localBoxsize*.4),
                            pointSize, pointSize);
            }
            else if (getLetter()=="shin")
                g2d.fillOval(10, 10, pointSize, pointSize);

        }
        else
        {
            g2d.setColor(bgVwlBtn);
            g2d.fillRoundRect(0, 0, localBoxsize, localBoxsize, curvSize, curvSize);
            g2d.setColor(fgVwlBtn);
            // now get icon with transparent rectangle for Vowel LetterButton
            switch (getVowel())
            {
                case PATAH:
                    g2d.fillRect((int)(localBoxsize*0.2), (int)(localBoxsize*0.8),
                            (int)(localBoxsize*0.5), (int)(localBoxsize*0.1));
                    break;
                case HIRIK:
                    g2d.fillOval((int)(localBoxsize*0.35), (int)(localBoxsize*0.8),
                            (int)(localBoxsize*0.15), (int)(localBoxsize*0.15));
                    break;
                case TZERE:
                    g2d.fillOval((int)(localBoxsize*0.2), (int)(localBoxsize*0.8),
                            (int)(localBoxsize*0.15), (int)(localBoxsize*0.15));
                    g2d.fillOval((int)(localBoxsize*0.6), (int)(localBoxsize*0.8),
                            (int)(localBoxsize*0.15), (int)(localBoxsize*0.15));
                    break;
                case HOLAM:
                    g2d.fillOval((int)(localBoxsize*0.2), (int)(localBoxsize*0.15),
                            (int)(localBoxsize*0.15), (int)(localBoxsize*0.15));
                    break;
                case KUBUTZ:
                    g2d.fillOval((int)(localBoxsize*0.28), (int)(localBoxsize*0.65),
                            (int)(localBoxsize*0.15), (int)(localBoxsize*0.15));
                    g2d.fillOval((int)(localBoxsize*0.4), (int)(localBoxsize*0.75),
                            (int)(localBoxsize*0.15), (int)(localBoxsize*0.15));
                    g2d.fillOval((int)(localBoxsize*0.54), (int)(localBoxsize*0.85),
                            (int)(localBoxsize*0.15), (int)(localBoxsize*0.15));
                    break;
                case SHVA:
                    break;
                default:
                    break;
            }
//			log("letter: "+getLetter()+", vowel: "+getVowel()+", text: "+getText());

        }
    }

    /**
     * main constructor
     * @param hl hebLetter attached to this button
     * @param container_size size of containing panel (to check limits)
     */
    public LetterButton(hebLetter hl, final Dimension container_size){
        super(hl.toString());
        setDoubleBuffered(false);
        setMargin(new Insets(0, 0, 0, 0));
        setPreferredSize(new Dimension(25, 25));
        setSize(boxSize, boxSize);
        setFont(new Font("Serif", Font.PLAIN, 20));

        // add punctuation (dagesh) if needed


        final Color fgC = getForeground();
        final Color bgC = getBackground();

        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                draggedAtX = e.getX();
                draggedAtY = e.getY();
                int howManyClicks = e.getClickCount();
                log("you clicked "+howManyClicks+" times");
            }
            /**
             * use mouseClick for talking hints
             */
            public void mouseClicked(MouseEvent e){
                Vowel vowel = getVowel();

                if ((vowel!=null) && getLetter()=="vowel")
                    Say(vowel);
                else
                    Say(getLetter(), vowel , true);
            }
            /** TODO
             * here it must be checked that if there is LetterBox that is
             * isOccupied && !isOccupiedCorrectly && non-overlap
             * then it should be released (isOccupied=false)
             */
            public void mouseReleased(MouseEvent e){
                Component parent = getParent();
                if (!(parent instanceof JLayeredPane))
                    return;
                Point newP = new Point();
                newP.x = getX();
                newP.y = getY();
                AlephFrame fwb = null;
                Component qb= getRootPane().getParent();
                if (!(qb instanceof AlephFrame))
                    return;
                fwb = (AlephFrame)qb;
                if (fwb.assignmentType == AssignmentType.SYLLABLE)
                    return;
                // check if LetterButton match LetterBox

                try {
                    checkLetterButtonMatchLetterBox(newP, parent);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                setLocation(newP);

                // if LetterButton overlaps with LetterButton, move it back to last position
                // checks also for successVowelMatch
                if (checkLetterButtonOverlapLetterButton((JLayeredPane) parent))
                {
                    setLocation(lastButtonLocation);
                    return;
                }
                // if successful vowel match, then update last location to current
                if (imVowelThatMatch)
                {
                    lastButtonLocation = getLocation();
                    return;
                }
                // check if LetterButton overlap with LetterBox
                foundOverlappedBox = checkOverlapLetterBox((JLayeredPane) getParent());
                LetterBox lb = null;
                if (foundOverlappedBox!=null)
                {
                    lb = (LetterBox) foundOverlappedBox;
                    lb.isOccupied=true;
                    setLocation(lastButtonLocation);
                    lb.isOccupied=false;
                    setForeground(fgC);
                    setBackground(bgC);
                    log("Letterbox overlap");
                }
                else
                {
                    lastButtonLocation = getLocation();
                    // if there's any box that was still occupied but not Occupied Correctly it must be 'released'
                    lb = isThereOccupied((JLayeredPane) getParent());
                    if (lb!=null){
                        lb.isOccupied=false;
                        if (!matchLetterBox){
                            setForeground(fgC);
                            setBackground(bgC);
                        }
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseDragged(MouseEvent e){
                if (stopMove)
                    return;
                int newX = e.getX() - draggedAtX + getLocation().x;
                int newY = e.getY() - draggedAtY + getLocation().y;
                Point newP = new Point(newX, newY);
                if (checkLimit(container_size, newX, newY))
                    return;

                setLocation(newP);
                ShowPix();

            }
            /*			*//**
             * if there's any Letter box in our way, check if match
             * @param newP
             * @param parent should be Layered Panel
             * @throws Exception
             *//*
			private void checkLetterButtonMatchLetterBox(Point newP, Object parent) throws Exception {
				// first check if there's non-occupied Letter box
				overlapComponent = checkOverlapLetterBox((JLayeredPane) parent);
				if (overlapComponent!=null)
				{
					foundOverlappedBox = (LetterBox)overlapComponent;
					// if there is, check if no one is in there already
					if (!foundOverlappedBox.isOccupied)
					{
						foundOverlappedBox.isOccupied=true;
						// now check is there's a match
						if (letter == foundOverlappedBox.letter)
						{ // success
							newP.x = overlapComponent.getX();
							newP.y = overlapComponent.getY();
							setSuccessLetterBoxMatch();
						}
						else{
							setBackground(Color.black);
							setForeground(Color.red);
							// next call for timer is obsolete
							// it was meant in case stopMove was set to true also when there's no matchs
							Timer timer = new Timer(2000,taskPerformer);
							timer.setRepeats(false);
							timer.start();
						}
					}
					if (foundOverlappedBox.isOccupiedCorrectly)
						if (letter == "vowel")
						{ //TODO: never gets here
							if (!foundOverlappedBox.isVowelOccupied)
							{
								foundOverlappedBox.isVowelOccupied=true;
								// now check is there's a match
								if (vowel == foundOverlappedBox.vowel)
									log("vowel same");

							}
							throw new Exception("something went wrong if I'm here");
						}
				}
			}*/
            /*			*//**
             * things to do when LetterButton match LetterBox
             *//*
			private void setSuccessLetterBoxMatch() {
				foundOverlappedBox.whoOccupiesMe = getName();
				foundOverlappedBox.isOccupiedCorrectly=true;
				setBackground(Color.green);
				setForeground(Color.black);
				stopMove=true;
				matchLetterBox = true;
				// now check if quest accomplished
				if (checkAllLetterBoxGood((JLayeredPane) getParent()))
				{
					// Note: rootpane is the one create automatically under Jframe
					// there is exactly one rootpane for every toplevel frame and to get it,
					// we use getRootPane(). but this get us only to rootpane. the frame is the ancestor of this root
					// to get the rootpane from top-level frame we use getContentPane()

					// now that mission succeeded, all other unfinished quests should be enabled again
					Component qb= getRootPane().getParent();
					if (!(qb instanceof frame_with_buttons))
						log("not good: this is not frame_with_buttons");
					else
					{
						frame_with_buttons fwb = (frame_with_buttons)qb;
						fwb.questAccomplished();
						log("All GOOD!! "+fwb.getContentPane().getName());
						if (fwb.isSoundOn())
							new SoundClip("cheers.wav");
					}

				}
			}
			ActionListener taskPerformer = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					log("stopMove=false, Letter="+letter+", boxLetter:"+foundOverlappedBox.letter);
					stopMove=false;
			      }
			};
			  */
        });

    }

    protected void Say(String letter, Vowel vowel, boolean use_wav) {
        Component qb= getRootPane().getParent();
        if (!(qb instanceof AlephFrame)){
            log("not good: this is not frame_with_buttons");
            File file = new File("/home/ubtosh/Downloads/boo.wav");
            if (file!=null){
                new Wav_say("/home/ubtosh/Downloads/boo.wav");
            }
        }

        else
        {
            AlephFrame fwb = (AlephFrame)qb;
            if (fwb.isSoundOn())
                if ((fwb.assignmentType==AssignmentType.SPELL  || fwb.assignmentType==AssignmentType.SPELLHINT))
                {
                    String letterSoundPath = fwb.getResourcePath("/data/sounds/letters/");
                    MP3_say mp3 = null;
                    letterSoundPath += letter;
                    if (use_wav)
                        new Wav_say(letterSoundPath+".wav");
                    else{
                        mp3 = new MP3_say(letterSoundPath + ".mp3");
                        mp3.play();
                    }

                    log("Say "+letter.toString());
                }
                else // say the letter+vowel
                {
                    String letterSoundPath = fwb.getResourcePath("/data/sounds/letters_vowels/");
                    MP3_say mp3 = null;
                    letterSoundPath += getLetterVowel(letter, vowel, dagesh);
                    if (use_wav)
                        new Wav_say(letterSoundPath+".wav");
                    else{
                        mp3 = new MP3_say(letterSoundPath + ".mp3");
                        mp3.play();
                    }
                }
        }
    }

    private String getLetterVowel(String letter, Vowel vowel, boolean dagesh) {
        String lettervowel = "";
        switch (letter){
            case "alef":
                lettervowel = "a";
                break;
            case "bet":
                if (dagesh)
                    lettervowel = "b";
                else
                    lettervowel = "v";
                break;
            case "gimel":
                lettervowel = "g";
                break;
            case "dalet":
                lettervowel = "d";
                break;
            case "he":
                lettervowel = "h";
                break;
            case "vav":
                lettervowel = "v";
                break;
            case "zain":
                lettervowel = "z";
                break;
            case "het":
                lettervowel = "ch";
                break;
            case "tet":
                lettervowel = "t";
                break;
            case "yod":
                lettervowel = "y";
                break;
            case "kaf":
                if (dagesh)
                    lettervowel = "k";
                else
                    lettervowel = "j";
                break;
            case "lamed":
                lettervowel = "l";
                break;
            case "mem":
                lettervowel = "m";
                break;
            case "nun":
                lettervowel = "n";
                break;
            case "sameh":
                lettervowel = "s";
                break;
            case "ain":
                lettervowel = "a";
                break;
            case "pe":
                if (dagesh)
                    lettervowel = "p";
                else
                    lettervowel = "f";
                break;
            case "tzadi":
                lettervowel = "ts";
                break;
            case "kof":
                lettervowel = "k";
                break;
            case "resh":
                lettervowel = "r";
                break;
            case "shin":
                if (dagesh)
                    lettervowel = "sh";
                else
                    lettervowel = "s";
                break;
            case "tav":
                lettervowel = "t";
                break;
            case "memE":
                lettervowel = "m";
                break;
            case "nunE":
                lettervowel = "n";
                break;
            case "kafE":
                if (dagesh)
                    lettervowel = "k";
                else
                    lettervowel = "j";
                break;
            case "peE":
                if (dagesh)
                    lettervowel = "p";
                else
                    lettervowel = "f";
                break;
            case "tzadiE":
                lettervowel = "ts";
                break;
        }
        if (vowel==null)
        {
            if (letter=="vav")
                vowel = Vowel.KUBUTZ;
            else if(letter=="yod")
                vowel = Vowel.HIRIK;
            else
                vowel = Vowel.SHVA;
        }
        switch (vowel){
            case PATAH:
                lettervowel += "a";
                break;
            case TZERE:
                lettervowel += "e";
                break;
            case HIRIK:
                lettervowel += "i";
                break;
            case HOLAM:
                lettervowel += "o";
                break;
            case KUBUTZ:
                lettervowel += "u";
                break;
            default:
                lettervowel += "y";
        }
        // TODO Auto-generated method stub
        return lettervowel;
    }

    protected void Say(Vowel vowel) {
        Component qb= getRootPane().getParent();
        if (!(qb instanceof AlephFrame))
            log("Say not good: this is not frame_with_buttons");
        else
        {
            AlephFrame fwb = (AlephFrame)qb;
            if (fwb.isSoundOn())
            {
                String vowelSoundPath = fwb.getResourcePath("/data/sounds/vowels/");
                MP3_say mp3 = null;
                switch (vowel)
                {
                    case PATAH:
                        vowelSoundPath+="aa";
                        break;
                    case TZERE:
                        vowelSoundPath+="ee";
                        break;
                    case HIRIK:
                        vowelSoundPath+="ii";
                        break;
                    case HOLAM:
                        vowelSoundPath+="oo";
                        break;
                    case KUBUTZ:
                        vowelSoundPath+="uu";
                        break;
                    default:
                        break;
                }
                mp3 = new MP3_say(vowelSoundPath + ".mp3");
                if (mp3!=null)
                    mp3.play();
            }
            log("Say "+vowel.toString());
        }
    }

    /**
     * if there's any Letter box in our way, check if match
     * @param newP
     * @param parent should be Layered Panel
     * @throws Exception
     */
    protected void checkLetterButtonMatchLetterBox(Point newP, Object parent) throws Exception {
        // first check if there's non-occupied Letter box
        foundOverlappedBox = checkOverlapLetterBox((JLayeredPane) parent);
        if (foundOverlappedBox!=null)
        {
            //			foundOverlappedBox = (LetterBox)overlapComponent;
            // if there is, check if no one is in there already
            if (!foundOverlappedBox.isOccupied)
            {
                foundOverlappedBox.isOccupied=true;
                // now check is there's a match
                if (letter == foundOverlappedBox.letter)
                { // success
                    newP.x = foundOverlappedBox.getX();
                    newP.y = foundOverlappedBox.getY();
                    setSuccessLetterBoxMatch();
                }
                else{
                    setBackground(Color.black);
                    setForeground(Color.red);
                    // next call for timer is obsolete
                    // it was meant in case stopMove was set to true also when there's no matchs
					/*					Timer timer = new Timer(2000,taskStopTimeout);
					timer.setRepeats(false);
					timer.start();
					 */				}
            }
            if (foundOverlappedBox.isOccupiedCorrectly)
                if (letter == "vowel")
                { //TODO: never gets here
                    if (!foundOverlappedBox.isVowelOccupied)
                    {
                        foundOverlappedBox.isVowelOccupied=true;
                        // now check is there's a match
                        if (vowel == foundOverlappedBox.vowel)
                            log("vowel same");

                    }
                    throw new Exception("something went wrong if I'm here");
                }
        }
    }

    /**
     * things to do when LetterButton match LetterBox
     */
    private void setSuccessLetterBoxMatch() {
        foundOverlappedBox.whoOccupiesMe = getName();
        foundOverlappedBox.isOccupiedCorrectly=true;
        setBackground(Color.green);
        setForeground(Color.black);
        stopMove=true;
        matchLetterBox = true;
        // now check if quest accomplished
        if (checkAllLetterBoxGood((JLayeredPane) getParent()))
        {
            // Note: rootpane is the one create automatically under Jframe
            // there is exactly one rootpane for every toplevel frame and to get it,
            // we use getRootPane(). but this get us only to rootpane. the frame is the ancestor of this root
            // to get the rootpane from top-level frame we use getContentPane()

            // now that mission succeeded, all other unfinished quests should be enabled again
            Component qb= getRootPane().getParent();
            if (!(qb instanceof AlephFrame))
                log("not good: this is not frame_with_buttons");
            else
            {
                AlephFrame fwb = (AlephFrame)qb;
                fwb.questAccomplished("");
                log("All GOOD!! "+fwb.getContentPane().getName());
                //				if (fwb.isSoundOn())
                //					new SoundClip("cheers.wav");
            }

        }
    }

    /**
     * timer for released stopped
     */
    ActionListener taskStopTimeout = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            if (foundOverlappedBox==null)
                log("foundOverlappedBox==null");
            else
                log("stopMove=false, Letter="+letter+", boxLetter:"+foundOverlappedBox.letter);
            stopMove=false;
        }
    };

    /*
     * constructor with Point location specifier
     * @param text
     * @param p
     */
    public LetterButton(hebLetter hl, Point p, final Dimension d){
        this(hl, d);
        setLocation(p);
        lastButtonLocation = getLocation();
    }

    /**
     * constructor with x,y location specifier
     * @param text
     * @param try_x
     * @param try_y
     */
    public LetterButton(hebLetter hl, int try_x, int try_y, final Dimension d, int minO){
        this(hl, d);
        setLocation(try_x, try_y);
        lastButtonLocation = getLocation();
        minOverlap = Math.max(0, minO); // otherwise, weird things can happen
        //		letter = hl.letter;
        //		vowel = hl.vowel;
        setDagesh(hl.isDagesh());
        setVowel(hl.vowel);
        setLetter(hl.letter);
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String inLetter) {
        letter = inLetter;
    }

    protected LetterBox isThereOccupied(JLayeredPane containingPanel) {
        for (Component jc: containingPanel.getComponents())
        {
            LetterBox lb = null;
            if (jc instanceof LetterBox)
                lb = (LetterBox)jc;
            else
                continue;
            if (!lb.isOccupiedCorrectly && lb.isOccupied)
                return lb;
        }
        return null;
    }

    /**
     * check if all LetterBoxes are occupied correctly
     * @param parent
     */
    public boolean checkAllLetterBoxGood(JLayeredPane parent) {
        boolean allGood=true;
        for (Component jc: parent.getComponents())
        {
            LetterBox lb = null;
            if (jc instanceof LetterBox)
            {
                lb = (LetterBox)jc;
                if (!lb.isOccupiedCorrectly)
                    allGood = false;
            }
        }
        return allGood;
    }

    /**
     * check if there's any non-occupied box component in containingPanel overlapping with this button
     * @param containingPanel
     * @return overlapping component, if no component found, return null
     */
    public LetterBox checkOverlapLetterBox(JLayeredPane containingPanel) {
        for (Component jc: containingPanel.getComponents())
        {
            LetterBox lb = null;
            if (jc instanceof LetterBox)
                lb = (LetterBox)jc;
            Rectangle myLocation = getBounds();
            Rectangle jcLocation = jc.getBounds();
            if (lb!=null)
                if (rectOverlap(myLocation, jcLocation, minOverlap))
                    if (!lb.isOccupiedCorrectly)
                        return (LetterBox) jc;
        }
        return null;
    }

    /**
     * check if this LetterButton doesn't overlap with any other LetterButton (not vowel)
     * if this is vowel, check if match other LetterButton's vowel
     * @param containingPanel
     * @return true if overlapping with other LetterButton
     */
    public boolean checkLetterButtonOverlapLetterButton(JLayeredPane containingPanel) {
        Component[] clist = containingPanel.getComponents();
        for (Component any_comp: clist)
        {
            if (!(any_comp instanceof LetterButton))
                continue;
            LetterButton other_LB = (LetterButton)any_comp;
            Rectangle myLocation = getBounds();
            Rectangle jcLocation = other_LB.getBounds();
            Component qb= getRootPane().getParent();
            if (qb instanceof AlephFrame)
            {
                AlephFrame fwb = (AlephFrame)qb;

                if (
                        rectOverlap(myLocation, jcLocation, minOverlap) &&
                                (!(other_LB==this)) &&
                                (fwb.assignmentType.equals(AssignmentType.VOWEL)))
                {
                    return checkVowelMatchLetterButton(other_LB);
                }
            }
        }
        return false;
    }

    /**
     * Check if this is vowel that matches LetterButton's vowel
     * @param other_LB
     * @return
     */
    private boolean checkVowelMatchLetterButton(LetterButton other_LB) {
        if (this.vowel!=null && other_LB.matchLetterBox && !imVowelThatMatch)
        {
            Component qb= getRootPane().getParent();
            AlephFrame fwb = (AlephFrame)qb;
            String soundEffectPath = fwb.getResourcePath("/data/sounds/effects/");
            if (other_LB.vowel==vowel){
                successVowelMatch(other_LB);
                return false;
            }
            else
            {
                fwb.decreaseScore();
                if (fwb.isSoundOn())
                {
                    soundEffectPath += "drumTimpany";
                    new Wav_say(soundEffectPath+".wav");
                }
                return true;
            }
        }
        else
            return true;
    }

    /**
     * successful vowel match
     * @param drg_letter
     * TODO: maybe this should be moved to frame application
     */
    private void successVowelMatch(LetterButton drg_letter) {
        imVowelThatMatch=true;
        stopMove=true;
        Component qb= getRootPane().getParent();
        if (qb instanceof AlephFrame)
        {
            AlephFrame fwb = (AlephFrame)qb;
            fwb.SubAssignCountCurrent++;
            if (fwb.isSoundOn())
            {
                String soundEffectPath = fwb.getResourcePath("/data/sounds/effects/");
                new Wav_say(soundEffectPath+"magic-chime-02.wav");
            }
            if (fwb.SubAssignCountCurrent==fwb.SubAssignCountMax)
            {
                fwb.questAccomplished("");
            }
            fwb.dealFloatingVowel(fwb.getMainPanelLimits(), drg_letter.getVowelIndex(), true);

            // TODO: paint with gradient gold effect
            drg_letter.setBackground(new Color(180,150,0));
            drg_letter.setForeground(Color.black);
            // make sure vowel is located nice proportionally to letter (exactly on letter)
            putNiceVowelOnLetter(drg_letter);
            // replace icon with transparent rectangle to only vowel
            String iconFullPath = fwb.vowelpath + getVowel().toString().toLowerCase() + ".png";
            Icon icon = new ImageIcon(iconFullPath);
            setIcon(icon);
        }

    }

    /**
     * make sure vowel is placed perfectly and nicely on letter
     * @param drg_letter
     */
    public void putNiceVowelOnLetter(LetterButton drg_letter) {
        Insets insets = getInsets();
        //int x_vowel = drg_letter.getBounds().x + insets.left/2;
        //int y_vowel = drg_letter.getY() + drg_letter.getHeight() - getHeight() ;// + insets.top + y_offset;//drg_letter.getBounds().height;
        setLocation(drg_letter.getX(), drg_letter.getY());//x_vowel,y_vowel);
        log("new location: "+drg_letter.getBounds()+", insets: "+insets);
    }

    /**
     * check if locations of two LetterButtons overlap
     * @param myL
     * @param jcL
     * @param tol tolerance - not used
     * @return
     */
    private boolean rectOverlap(Rectangle myL, Rectangle jcL, int tol) {
        boolean doesOverlap=false;
        int tmpX;
        int tmpY;
        int jclX0 = jcL.x;
        int jclY0 = jcL.y;
        int jclX1 = jcL.x+jcL.width;
        int jclY1 = jcL.y+jcL.height;
        tmpX = myL.x+(Integer)(myL.width/2);
        tmpY = myL.y+(Integer)(myL.height/2);
        if (tmpX<jclX1 && tmpX>jclX0 && tmpY<jclY1 && tmpY>jclY0)
            doesOverlap=true;
        return doesOverlap;
    }

    /**
     * Try set location and check if not overlapping
     * @param try_x
     * @param try_y
     * @return true if location is ok
     */
    public boolean setButtonLocation(int try_x, int try_y){
        // first try the new location
        setLocation(try_x, try_y);
        Component parent = getParent();
        if (parent != null)
            if (parent instanceof JLayeredPane)
            {
                // check if not overlap with any other LetterButton
                if (checkLetterButtonOverlapLetterButton((JLayeredPane) getParent()))
                {
                    setLocation(lastButtonLocation);
                    return false;
                }
                // check if not overlap with any Letterbox
                if (checkOverlapLetterBox((JLayeredPane) getParent()) != null)
                {
                    setLocation(lastButtonLocation);
                    return false;
                }

            }
        lastButtonLocation = getLocation();
        return true;
    }

    /**
     * helper function
     * @return
     */
    public void ShowPix(){
        int x = getLocation().x;
        int y = getLocation().y;
        Point p = new Point(x, y);
        if (show!=null){
            show.setText("Pixels. X: "+x+" y:"+y);
        }
        else
            AlephFrame.ShowPix(x,y);
        //return p;
    }

    /**
     * @param container_size
     * @param newX
     * @param newY
     * @return
     */
    public boolean checkLimit(final Dimension container_size,
                              int newX, int newY) {
        boolean outOfLimit=false;
        if (getSize() !=null)
        {
            int maxX = newX + getSize().width;
            int maxY = newY + getSize().height;
            int frameWidth = container_size.width;
            int frameHeight = container_size.height;
            if ((maxX > frameWidth) || (newX<0) || (newY<0) || (maxY>frameHeight))
            {
                outOfLimit=true;
            }
        }
        return outOfLimit;
    }

    /**
     * main
     * @param args
     * @throws UnsupportedLookAndFeelException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        //		UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

        JFrame frame = new JFrame("DragButton");
        frame.setName("mainFrame");
        frame.setLayout(null);
        frame.pack();
        frame.setSize(300, 300);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        show = new JLabel("Pixels");
        show.setBounds(90, 87, 150, 15);
        show.setFont(new Font("Dialog",Font.BOLD,12));
        show.setForeground(Color.BLUE);
        show.setBorder(new LineBorder(Color.red));
        show.setName("PixShow");

        JLayeredPane mainpanel = new JLayeredPane();
        mainpanel.setBorder(new LineBorder(Color.cyan));
        //mainpanel.setLayout(null);

        mainpanel.add(show);

        int minO = 5;
        //		int delt_x_align=50;
        //		int offs_x_align=50;
        //		int offs_y = frame.getSize().height-100;
        Random r = new Random();
        Dimension b  = frame.getRootPane().getSize();

        int rx;
        int ry;
        hebWord hw = new hebWord("ארנב");
        log("mainpanel: "+frame.toString());
        int limitx;
        int limity;
        for (int c=0; c< hw.length; c++){
            //			DragButton dr = new DragButton(hw.getChar(c), offs_x_align + (hw.length-c-1)*delt_x_align,offs_y, frame.getSize(), minO);
            limitx = b.width - boxSize;
            rx = r.nextInt(limitx);
            limity =  b.height - letter_y_offset - boxSize;
            ry = letter_y_offset + r.nextInt(limity);
            LetterButton dr = new LetterButton(hw.getChar(c), rx,ry, frame.getSize(), minO);
            dr.setName("dr_b"+c);
            mainpanel.add(dr);
        }
        b  = mainpanel.getSize();
        // add text box
        LetterBox bt0 = new LetterBox("tBox0", 100, letter_y_offset, boxSize, "alef");
        mainpanel.add(bt0);
        frame.setContentPane(mainpanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                log("pressed: "+ e.getKeyChar());
            }
        });
    }

    /**
     * helper function for debug
     * @param s
     */
    public static void log(String s) {
        System.out.println(s);
    }


}
