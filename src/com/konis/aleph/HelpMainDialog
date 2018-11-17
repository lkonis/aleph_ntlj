package com.konis.aleph;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class NewAssignmentChooser extends JDialog{
	static int debugCnt =0;
	static JLabel topLabel = new JLabel();
	static JPanel mainPanel = new JPanel();
	static JLabel bottomLabel = new JLabel();
	static JButton easyBtn = new JButton();
	static JButton mediumBtn = new JButton();
	static JButton hardBtn = new JButton();
	AlephFrame fwb;
	public NewAssignmentChooser(JFrame owner) {
		super(owner);
		setName("mainAssChosFrame");
		setLayout(new BorderLayout());
		setSize(300, 300);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		mainPanel.setLayout(new GridLayout(3, 0, 10, 10));
		mainPanel.setBorder(new TitledBorder("Main panel"));
		add(mainPanel, BorderLayout.CENTER);
		topLabel.setText("איזה רמה תבחר לשחק?");
		topLabel.setHorizontalAlignment(JLabel.RIGHT);
		topLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		add(topLabel, BorderLayout.NORTH);
		
		bottomLabel.setText("nothing here");
		bottomLabel.setBorder(new TitledBorder("Bottom Label"));
		add(bottomLabel, BorderLayout.SOUTH);

		// draw all options
		// define lock icon
		String iconpath = AlephFrame.getClassResourcePath("/data/icon/");
//		ImageIcon ic = new ImageIcon("/home/ubtosh/workspace/Aleph/data/icon/lock-icon64.png");
		ImageIcon ic = new ImageIcon(iconpath+"lock-icon64.png");

		easyBtn.setText("קל");
		easyBtn.setHorizontalAlignment(JLabel.RIGHT);
		easyBtn.setFont(new Font("Serif", Font.PLAIN, 20));
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
		
		setVisible(true);
		setModal(true);
		System.out.println("modality: "+getModalityType());
	}
	public NewAssignmentChooser(AlephFrame caller_fwb, int score){
		this((JFrame) caller_fwb);
		fwb = caller_fwb;
		  try {
			fwb.fillNewAssignment(AlephFrame.bgImagePath);
		  } catch (IOException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		  return;
	}
/*		if (score > fwb.EASYTHRESH)
		{
			mediumBtn.setEnabled(true);
			mediumBtn.setIcon(null);
		}
		if (score > fwb.MEDIUMTHRESH)
		{
			hardBtn.setEnabled(true);
			hardBtn.setIcon(null);
		}
		// trying another way to implement (see below)
		ActionListener easyBtnActionImplementer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Component qb= getRootPane().getParent();
				  System.out.println("easy clicked: "+debugCnt+". qb:"+qb.toString());
				  debugCnt++;
				  fwb.assignmentType = AssignmentType.VOWEL;
				  String bgImagePath = fwb.getResourcePath("/data/bg_images/") + "springGiraffe.jpg";
				  try {
					fwb.fillNewAssignment(bgImagePath);
				  } catch (IOException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  ((JDialog)qb).dispose();
			}
		};
		
		easyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Component qb= getRootPane().getParent();
				  System.out.println("easy clicked: qb:"+qb.toString());
				  fwb.difficulty = Difficulty.EASY;
				  fwb.assignmentType = AssignmentType.VOWEL;
				  fwb.afterNewAssignment();
				  dispose();
				  }
				});
		ActionListener easyBtnDummyActionImplementer = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("I'm another easy listener:"+debugCnt);
				debugCnt++;
			}
		};
		// register easy button event to a dedicated listener object 
		easyBtn.addActionListener(easyBtnActionImplementer);
//		easyBtn.addActionListener(easyBtnDummyActionImplementer);

		// register medium button event to a dedicated listener object 
		mediumBtn.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent evt) {
				  Component qb= getRootPane().getParent();
				  System.out.println("medium clicked: qb:"+qb.toString());
				  fwb.assignmentType = AssignmentType.SPELLHINT;
				  String bgImagePath = fwb.getResourcePath("/data/bg_images/") + "girlApple.jpg";
				  try {
					fwb.fillNewAssignment(bgImagePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  dispose();
				  }
				});
		
		// register hard button event to a dedicated listener object 
		hardBtn.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent evt) {
				  Component qb= getRootPane().getParent();
				  System.out.println("hard clicked: qb:"+qb.toString());
				  fwb.assignmentType = AssignmentType.SPELL;
				  String bgImagePath = fwb.getResourcePath("/data/bg_images/") + "rainbow.jpg";
				  try {
					fwb.fillNewAssignment(bgImagePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  dispose();
				  }
				});
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		fwb.setEnabled(true);
		super.dispose();
	}
	public void changeBtnVisibility(String btnName, boolean release){
		
		for (Component c: getComponents()){
			if (c instanceof JButton){
				JButton btn = (JButton)c; 
				if (btn.getName().equals(btnName))
					System.out.println("kk");
			}
		}
	}
*/
	/**
	 * main for testing this class
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		AlephFrame fwb = new AlephFrame();
		new NewAssignmentChooser(fwb, 200);
		
	}

}
