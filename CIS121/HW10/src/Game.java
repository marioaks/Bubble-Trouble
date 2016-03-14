/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	public void run() {
		// NOTE : recall that the 'final' keyword notes inmutability
		// even for local variables.

		// Top-level frame in which game components live
		// Be sure to change "TOP LEVEL FRAME" to the name of your game
		final JFrame frame = new JFrame("BUBBLE TROUBLE");
		frame.setLocation(300, 300);

		// Status panel
		final JPanel status_panel = new JPanel();
		frame.add(status_panel, BorderLayout.SOUTH);
		final JLabel status = new JLabel("Running...");
		status_panel.add(status);

		// Reset button
		final JPanel control_panel = new JPanel();
		frame.add(control_panel, BorderLayout.NORTH);

		// Note here that when we add an action listener to the reset
		// button, we define it as an anonymous inner class that is
		// an instance of ActionListener with its actionPerformed()
		// method overridden. When the button is pressed,
		// actionPerformed() will be called.
		final JLabel highScore = new JLabel();
		highScore.setText("HIGH SCORE: 0");
		control_panel.add(highScore);

		// Main playing area
		final GameCourt court = new GameCourt(status, highScore);
		frame.add(court, BorderLayout.CENTER);

		final JButton reset = new JButton("New Game");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.livesInt = 6;
				highScore.setText("HIGH SCORE: "
						+ Integer.toString(court.highScore));
				court.newGame();
			}
		});
		control_panel.add(reset);

		final JButton instructions = new JButton("Instructions");
		instructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String instruct = "";
				court.pause();
				try {
					BufferedReader br = new BufferedReader(new FileReader(
							"instructions.txt"));
					String line = null;
					while ((line = br.readLine()) != null) {
						instruct += line + "\n";
					}
					br.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JTextArea textArea = new JTextArea(instruct);
				JScrollPane scrollPane = new JScrollPane(textArea);
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				scrollPane.setPreferredSize(new Dimension(600, 400));
				JOptionPane.showMessageDialog(null, scrollPane,
						"Instructions", JOptionPane.PLAIN_MESSAGE); 
				int input = JOptionPane.OK_OPTION;
				if (input == 0) {
					court.resume();
				}		
				
			}
			
		});
		instructions.setFocusable(false);
		control_panel.add(instructions);

		/*
		 * final JButton pause = new JButton("Pause/Resume");
		 * pause.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { if (court.playing == true)
		 * {court.pause();} else {court.resume();} } });
		 * control_panel.add(pause);
		 */

		// Put the frame on the screen
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// Start game
		court.reset();

	}

	/*
	 * Main method run to start and run the game Initializes the GUI elements
	 * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
	 * this in the final submission of your game.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
}
