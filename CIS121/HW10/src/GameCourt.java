/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.swing.*;

import java.util.ArrayList;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 * 
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

	// the state of the game logic
	private boolean gravOn;
	private int level = 0;
	private invSquare invSquare; // Arrow Director
	private ArrayList<Circle> circles;
	private Circle snitch0; // the Golden Snitch, bounces
	private Circle snitch1;
	private Circle snitch2;
	private Guy guy; // the Poison Mushroom, doesn't move
	private Arrow arrow; // Shooting growing arrow
	private Background background;
	// public Square highScorePausedBox;
	private Circle hit;
	public int livesInt = -1;
	private String lives;
	private ArrayList<LivesImg> guy_front;
	private int time = 0;
	// 0 is biggest, 4 is smallest
	private int snitch_level = 3;
	private double max_time = 2000 / Math.pow(2, snitch_level);
	private TimeKeeper timeKeeper;
	public static int score = 0;
	public int pastScore = 0;
	private Score scoreBox;
	private ArrayList<Clock> clocks;
	private ArrayList<GravArrow> gravArrow;
	private ArrayList<ExtraLife> extraLivesArray;
	private boolean lifeCollected = false;
	int pausedTime;
	private boolean paused = false;
	private boolean gameRunning = true;

	public boolean arrowHit = true;
	public boolean playing = false; // whether the game is running
	private JLabel status; // Current status text (i.e. Running...)
	private JLabel highScoreStatus;

	// Game constants
	public static final int COURT_WIDTH = 1200;
	public static final int COURT_HEIGHT = 600;
	public static final int SQUARE_VELOCITY = 5;
	public static final int ARROW_VELOCITY = 11;
	// Update interval for timer, in milliseconds
	public static int INTERVAL = 20;

	public Writer out;
	public Reader in;

	public int highScore;

	public GameCourt(JLabel status, JLabel highScore) {
		// creates border around the court area, JComponent method
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// The timer is an object which triggers an action periodically
		// with the given INTERVAL. One registers an ActionListener with
		// this timer, whose actionPerformed() method will be called
		// each time the timer triggers. We define a helper method
		// called tick() that actually does everything that should
		// be done in a single timestep.

		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
				int a = 0;
				
				//Create the timer for the TimeKeeper
				time += 1;

				//Make sure guy does not move higher than the ceiling
				if (guy.pos_y <= 0 && gravOn == true) {
					guy.pos_y = 0;
				}
				
				//Reset invSquare after collision
				if (arrowHit == true) {
					if (guy.pos_y != 0 && guy.pos_y != 550) {
						if (gravOn) {
							invSquare.pos_y = 0;
						} else {
							invSquare.pos_y = COURT_HEIGHT;
						}
					} else {
						invSquare.pos_y = guy.pos_y;
						invSquare.pos_x = guy.pos_x + guy.width / 2;
					}
				}
				
				//Fix timeKeeper pos_y after gravity Shift.
				if (timeKeeper.pos_y >= 500) {
					timeKeeper.pos_y = 500;
				} else if (timeKeeper.pos_y <= 55) {
					timeKeeper.pos_y = 55;
				}

				//Fix Lives images pos_y after gravity shift
				for (int i = 0; i < guy_front.size(); i++) {
					if (guy_front.get(i).pos_y < 0) {
						guy_front.get(i).pos_y = 0;
					}
				}

				//Add lifeImage if lifeCollected is true
				if (lifeCollected == true) {
					LivesImg guy1 = new LivesImg(COURT_WIDTH, COURT_HEIGHT);
					guy_front.add(guy1);
					lifeCollected = false;
					if (gravOn) {
						GameObj.gravity = -.5;
						guy1.pos_y = 550;
					}

				}

				//Functionality of when arrow hits a bubble
				for (int i = 0; i < circles.size(); i++) {
					if (arrow.intersectsArrow(circles.get(i))
							&& (!(guy.intersects(circles.get(i))) 
									&& (guy.pos_y == 0 || guy.pos_y == 550))) {
						//change score
						score += 1000 / circles.get(i).width;
						//Fix invSquare after collision
						invSquare.pos_x = guy.pos_x + guy.width / 2;
						invSquare.pos_y = guy.pos_y;
						invSquare.v_y = 0;
						a = 1;
						//if circles are small enough, erase them. 
						if (circles.get(i).width <= 15) {
							circles.remove(circles.get(i));
							break;
						}
						// Drop the powerups from the bubbles randomly
						if (Math.random() < .4) {
							double rand = Math.random();
							if (rand < .4) {
								Clock clock1 = new Clock(COURT_WIDTH,
										COURT_HEIGHT);
								if (gravOn) {
									clock1.v_y *= -1;
								}
								clock1.pos_x = circles.get(i).pos_x;
								clock1.pos_y = circles.get(i).pos_y;
								clocks.add(clock1);
							} else if (rand < .7) {
								ExtraLife extraLife = new ExtraLife(
										COURT_WIDTH, COURT_HEIGHT);
								if (gravOn) {
									extraLife.v_y *= -1;
								}
								extraLife.pos_x = circles.get(i).pos_x;
								extraLife.pos_y = circles.get(i).pos_y;
								extraLivesArray.add(extraLife);

							} else {
								GravArrow gravArrow1 = new GravArrow(
										COURT_WIDTH, COURT_HEIGHT);
								if (gravOn) {
									gravArrow1.v_y *= -1;
								}
								gravArrow1.pos_x = circles.get(i).pos_x;
								gravArrow1.pos_y = circles.get(i).pos_y;
								gravArrow.add(gravArrow1);
							}
						}

						//Create two new circles and delete the old one. 
						Circle c1 = new Circle(COURT_WIDTH, COURT_HEIGHT,
								//Change drop velocity to alter bounce height
								circles.get(i).dropVel - 2.5,
								circles.get(i).color,
								circles.get(i).color_border);
						circles.add(c1);
						c1.pos_x = circles.get(i).pos_x;
						c1.pos_y = circles.get(i).pos_y;
						// fix for gravity and force from arrow hit
						if (gravOn) {
							c1.v_y = Math.max(circles.get(i).v_y, 10.5);
						} else {
							c1.v_y = Math.min(circles.get(i).v_y, -10.5);
						}

						c1.width = circles.get(i).width / 2;
						c1.height = circles.get(i).height / 2;
						if (circles.get(i).v_x >= 0) {
							c1.v_x = circles.get(i).v_x;
						} else {
							c1.v_x = -circles.get(i).v_x;
						}
						c1.max_y = COURT_HEIGHT - c1.height;
						c1.max_x = COURT_WIDTH - c1.width;
						Circle c2 = new Circle(COURT_WIDTH, COURT_HEIGHT,
								circles.get(i).dropVel - 2.5,
								circles.get(i).color,
								circles.get(i).color_border);
						circles.add(c2);
						c2.pos_x = circles.get(i).pos_x;
						c2.pos_y = circles.get(i).pos_y;
						if (circles.get(i).v_x >= 0) {
							c2.v_x = -circles.get(i).v_x;
						} else {
							c2.v_x = circles.get(i).v_x;
						}

						if (gravOn) {
							c2.v_y = Math.max(circles.get(i).v_y, 10.5);
						} else {
							c2.v_y = Math.min(circles.get(i).v_y, -10.5);
						}

						c2.width = circles.get(i).width / 2;
						c2.height = circles.get(i).height / 2;
						c2.max_y = COURT_HEIGHT - c2.height;
						c2.max_x = COURT_WIDTH - c2.width;
						circles.remove(circles.get(i));
						
						//Change gravity for gravity shift
						if (gravOn) {
							GameObj.gravity *= -1;
						}
						break;
					}
					/*
					 * if (circles.get(i).pos_y < -circles.get(i).height) {
					 * circles.remove(circles.get(i)); score += 100; }
					 */
				}

				//Fix invSquare for gravity shift. Make arrowHit true
				if (gravOn == false) {
					if (a == 1 || invSquare.pos_y >= guy.pos_y
							|| invSquare.pos_y <= 0) {
						arrowHit = true;
						invSquare.v_y = 0;
					}
				} else {
					if (a == 1 || invSquare.pos_y <= guy.pos_y
							|| invSquare.pos_y >= 599) {
						arrowHit = true;
						invSquare.v_y = 0;
					}
				}

				//Change opacity for powerUps and remove if collected
				for (int i = 0; i < clocks.size(); i++) {
					clocks.get(i).time += 1;
					clocks.get(i).opacity -= (float) (1 / 300.0);
					if (clocks.get(i).time == 300) {
						clocks.remove(i);
						break;
					} else if (guy.intersects(clocks.get(i))) {
						time -= 100;
						clocks.remove(i);
						break;
					}
					if (clocks.get(i).pos_y < 0) {
						clocks.get(i).pos_y = 0;
					}
				}

				for (int i = 0; i < extraLivesArray.size(); i++) {
					extraLivesArray.get(i).time += 1;
					extraLivesArray.get(i).opacity -= (float) (1 / 300.0);
					if (extraLivesArray.get(i).time == 300) {
						extraLivesArray.remove(i);
						break;
					} else if (guy.intersects(extraLivesArray.get(i))) {
						livesInt += 1;
						extraLivesArray.remove(i);
						lifeCollected = true;
						GameObj.gravity *= -1;
						break;
					}
					if (extraLivesArray.get(i).pos_y < 0) {
						extraLivesArray.get(i).pos_y = 0;
					}
				}

				//Fix gravity for all objects
				for (int i = 0; i < gravArrow.size(); i++) {
					gravArrow.get(i).time += 1;
					gravArrow.get(i).opacity -= (float) (1 / 300.0);
					if (gravArrow.get(i).time == 300) {
						gravArrow.remove(i);
						break;
					} else if (guy.intersects(gravArrow.get(i))) {
						GameObj.gravity *= -1;
						invSquare.pos_y = guy.pos_y;
						guy.v_y *= -1;
						if (GameObj.gravity < 0) {
							Arrow.startPoint = 0;
							timeKeeper.v_y = 50;
							for (int j = 0; j < guy_front.size(); j++) {
								guy_front.get(j).v_y = 50;
							}
							for (int j = 0; j < clocks.size(); j++) {
								clocks.get(j).v_y *= -1;
							}

							for (int j = 0; j < extraLivesArray.size(); j++) {
								extraLivesArray.get(j).v_y *= -1;
							}

							for (int j = 0; j < gravArrow.size(); j++) {
								gravArrow.get(j).v_y *= -1;
							}

						} else {
							Arrow.startPoint = COURT_HEIGHT;
							timeKeeper.v_y = -50;
							for (int j = 0; j < guy_front.size(); j++) {
								guy_front.get(j).v_y = -50;
							}
							for (int j = 0; j < clocks.size(); j++) {
								clocks.get(j).v_y *= -1;
							}

							for (int j = 0; j < extraLivesArray.size(); j++) {
								extraLivesArray.get(j).v_y *= -1;
							}

							for (int j = 0; j < gravArrow.size(); j++) {
								gravArrow.get(j).v_y *= -1;
							}
						}
						if (gravOn) {
							gravOn = false;
						} else {
							gravOn = true;
						}

						gravArrow.remove(i);
						break;
					}
					if (gravArrow.get(i).pos_y < 0) {
						gravArrow.get(i).pos_y = 0;
					}
				}

				//Draw arrow
				arrow.pos_x = invSquare.pos_x + (invSquare.width / 2);
				arrow.height = invSquare.pos_y;

			}

		});
		timer.start(); // MAKE SURE TO START THE TIMER!

		// Enable keyboard focus on the court area.
		// When this component has the keyboard focus, key
		// events will be handled by its key listener.
		setFocusable(true);

		// This key listener allows the square to move as long
		// as an arrow key is pressed, by changing the square's
		// velocity accordingly. (The tick method below actually
		// moves the square.)
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					guy.v_x = -SQUARE_VELOCITY;
					/*
					 * if (invSquare.pos_y == guy.pos_y) { invSquare.v_x =
					 * -SQUARE_VELOCITY; }
					 */
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					guy.v_x = SQUARE_VELOCITY;
					/*
					 * if (invSquare.pos_y == guy.pos_y) { invSquare.v_x =
					 * SQUARE_VELOCITY; }
					 */
				}

				//Shoot arrow differently if gravOn is true or false
				else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					arrowHit = false;
					if (gravOn == false) {
						invSquare.v_y = -ARROW_VELOCITY;
					} else {
						invSquare.v_y = ARROW_VELOCITY;
					}
					invSquare.v_x = 0;
				} 
				
				/*
				 * else if (e.getKeyCode() == KeyEvent.VK_G) { GameObj.gravity
				 * *= -1; invSquare.pos_y = guy.pos_y; guy.v_y *= -1; if
				 * (GameObj.gravity < 0) { Arrow.startPoint = 0; timeKeeper.v_y
				 * = 50; for (int i = 0; i < guy_front.size(); i++) {
				 * guy_front.get(i).v_y = 50; } for (int i = 0; i <
				 * clocks.size(); i++) { clocks.get(i).v_y *= -1; }
				 * 
				 * for (int i = 0; i < extraLivesArray.size(); i++) {
				 * extraLivesArray.get(i).v_y *= -1; }
				 * 
				 * for (int i = 0; i < gravArrow.size(); i++) {
				 * gravArrow.get(i).v_y *= -1; }
				 * 
				 * } else { Arrow.startPoint = COURT_HEIGHT; timeKeeper.v_y =
				 * -50; for (int i = 0; i < guy_front.size(); i++) {
				 * guy_front.get(i).v_y = -50; } for (int i = 0; i <
				 * clocks.size(); i++) { clocks.get(i).v_y *= -1; }
				 * 
				 * for (int i = 0; i < extraLivesArray.size(); i++) {
				 * extraLivesArray.get(i).v_y *= -1; }
				 * 
				 * for (int i = 0; i < gravArrow.size(); i++) {
				 * gravArrow.get(i).v_y *= -1; } } if (gravOn) { gravOn = false;
				 * } else { gravOn = true; } }
				 */
			}

			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT
						|| e.getKeyCode() == KeyEvent.VK_LEFT) {
					guy.v_x = 0;
					invSquare.v_x = 0;
				}
			}
		});
		
		//Change status for highScore
		this.status = status;
		this.highScoreStatus = highScore;
	}

	//Pause and resume for instructions
	public void resume() {
		if (gameRunning == false) {
			playing = false;
		} else {
			playing = true;
			paused = false;
			status.setText("Running...");
			time = pausedTime;
		}

	}

	public void pause() {
		playing = false;
		paused = true;
		status.setText("Paused...");
		pausedTime = time;
	}

	/**
	 * (Re-)set the game to its initial state.
	 */
	public void reset() {
		gameRunning = true;
		Arrow.startPoint = COURT_HEIGHT;
		gravOn = false;
		background = new Background(COURT_WIDTH, COURT_HEIGHT);
		invSquare = new invSquare(COURT_WIDTH, COURT_HEIGHT);
		timeKeeper = new TimeKeeper(COURT_WIDTH, COURT_HEIGHT, 0, Color.YELLOW);
		arrow = new Arrow(COURT_WIDTH, COURT_HEIGHT);
		guy = new Guy(COURT_WIDTH, COURT_HEIGHT);

		if (level < 0) {
			level = 0;
		}

		if (level < 3) {
			snitch_level = 3;
		} else if (level < 6) {
			snitch_level = 2;
		} else if (level < 9) {
			snitch_level = 1;
		} else {
			snitch_level = 0;
		}

		//Auto Levels!
		circles = new ArrayList<Circle>();
		if ((level % 3) + 1 > 0) {
			snitch0 = new Circle(COURT_WIDTH, COURT_HEIGHT, 18.5, Color.RED,
					Color.black);

			//Alter starting positions of bubbles depending on their sizes. 
			snitch0.width = (int) (150 / (Math.pow(2, snitch_level)));
			snitch0.height = snitch0.width;
			snitch0.dropVel = 18.5 - (2.5 * snitch_level);
			snitch0.pos_y = 250 - snitch0.height;
			snitch0.max_y = COURT_HEIGHT - snitch0.height;
			snitch0.max_x = COURT_WIDTH - snitch0.width;
			circles.add(snitch0);

			if ((level % 3) + 1 > 1) {
				snitch1 = new Circle(COURT_WIDTH, COURT_HEIGHT, 18.5,
						Color.red, Color.black);

				snitch1.width = (int) (150 / (Math.pow(2, snitch_level)));
				snitch1.height = snitch0.width;
				snitch1.dropVel = 18.5 - (2.5 * snitch_level);
				snitch1.pos_y = 250 - snitch0.height;
				snitch1.max_y = COURT_HEIGHT - snitch0.height;
				snitch1.max_x = COURT_WIDTH - snitch0.width;
				snitch0.pos_x = COURT_WIDTH / 10;
				snitch1.pos_x = 4 * COURT_WIDTH / 5;
				circles.add(snitch1);

				if ((level % 3) + 1 > 2) {

					snitch2 = new Circle(COURT_WIDTH, COURT_HEIGHT, 18.5,
							Color.RED, Color.black);

					snitch2.width = (int) (150 / (Math.pow(2, snitch_level)));
					snitch2.height = snitch0.width;
					snitch2.dropVel = 18.5 - (2.5 * snitch_level);
					snitch2.pos_y = 250 - snitch0.height;
					snitch2.max_y = COURT_HEIGHT - snitch0.height;
					snitch2.max_x = COURT_WIDTH - snitch0.width;
					snitch0.pos_x = COURT_WIDTH / 10;
					snitch1.pos_x = COURT_WIDTH / 2;
					snitch2.pos_x = 4 * COURT_WIDTH / 5;
					circles.add(snitch2);
				}
			}
		}

		//Set maximum time per level. Depends on how many bubbles there are
		max_time = (2200 * circles.size()) / Math.pow(2, snitch_level);

		clocks = new ArrayList<Clock>();
		extraLivesArray = new ArrayList<ExtraLife>();
		gravArrow = new ArrayList<GravArrow>();

		//Keep the same score for every level
		pastScore = score;

		if (level == 0) {
			score = 0;
			pastScore = 0;
		}

		scoreBox = new Score(COURT_WIDTH, COURT_HEIGHT, Integer.toString(score));

		guy_front = new ArrayList<LivesImg>();

		time = 0;

		hit = new Circle(COURT_WIDTH, COURT_HEIGHT, 0, Color.BLUE, Color.BLUE);
		hit.width = hit.width * 4 + 30;
		hit.pos_x = COURT_WIDTH / 2 - hit.width / 2;
		hit.pos_y = COURT_HEIGHT / 3 - hit.height / 2;

		if (livesInt < 0) {
			livesInt = 6;
		}

		for (int i = 0; i < livesInt; i++) {
			guy_front.add(new LivesImg(COURT_WIDTH, COURT_HEIGHT));
		}

		playing = true;
		status.setText("Running...");

		if (level >= 12) {
			playing = false;
			gameRunning = false;
			status.setText("YOU WON");
		}
		
		//Read in High Score
		try {
			File f = new File("highScore.txt");
			if (!(f.exists())) {
				out = new FileWriter("highScore.txt");
				BufferedWriter bw = new BufferedWriter(out);
				bw.close();
			}

			in = new FileReader("highScore.txt");
			BufferedReader br = new BufferedReader(in);
			String nextLine = br.readLine();
			if (nextLine == null) {
				highScore = 0;
			} else {
				highScore = Integer.valueOf(nextLine);
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		highScoreStatus.setText("HIGH SCORE: " + Integer.toString(highScore));

		if (paused) {
			pause();
		}

		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}

	//To start at first level.
	public void newGame() {
		level = 0;
		reset();
		paused = false;
	}

	/*
	 * public void highScore() { if (paused == false) { playing = false;
	 * status.setText("Paused..."); pausedTime = time; paused = true;
	 * highScorePausedBox = new Square(COURT_WIDTH, COURT_HEIGHT);
	 * highScorePausedBox.pos_x = 0;
	 * 
	 * highScorePausedBox.height = Math.max(COURT_HEIGHT, COURT_WIDTH);
	 * highScorePausedBox.pos_y = -highScorePausedBox.height;
	 * highScorePausedBox.v_y = 10; } }
	 * 
	 * public void resume() { if (paused == true) { playing = true;
	 * status.setText("Running..."); time = pausedTime; paused = false;
	 * highScorePausedBox.pos_y = -highScorePausedBox.height;
	 * highScorePausedBox.v_y = 0; }
	 * 
	 * }
	 */

	/**
	 * This method is called every time the timer defined in the constructor
	 * triggers.
	 */
	void tick() {
		if (playing) {
			// change the width of the timeKeeper
			timeKeeper.width = (int) (time / (max_time / (COURT_WIDTH - 57)));
			if (time >= max_time) {
				//Change color of timeKeeper
				timeKeeper.color = Color.RED;
			}

			for (int i = 0; i < guy_front.size(); i++) {
				guy_front.get(i).move();
			}

			// highScorePausedBox.move();

			invSquare.move();

			timeKeeper.move();

			arrow.move();
			
			//Made own function drop() that is like move, but uses gravity. 
			for (int i = 0; i < circles.size(); i++) {
				circles.get(i).drop();
			}
			guy.move();

			for (int i = 0; i < clocks.size(); i++) {
				clocks.get(i).move();
			}

			for (int i = 0; i < extraLivesArray.size(); i++) {
				extraLivesArray.get(i).move();
			}

			for (int i = 0; i < gravArrow.size(); i++) {
				gravArrow.get(i).move();
			}

			// make the snitch bounce off walls..
			for (int i = 0; i < circles.size(); i++) {
				circles.get(i).bounce(circles.get(i).hitWall());
			}
			// ...and the mushroom
			/* snitch.bounce(snitch.hitObj(guy)); */

			// check for the conditions when you lose a life
			for (int i = 0; i < circles.size(); i++) {
				if (guy.intersects(circles.get(i))
						&& (guy.pos_y >= COURT_HEIGHT - guy.height 
						|| guy.pos_y <= 0)
						|| time >= max_time) {
					livesInt -= 1;
					score = pastScore;
					status.setText("You lose!");
					playing = false;
					gameRunning = false;

					//Create delay in before automatic reset();
					if (livesInt != 0) {

						Timer timer1 = new Timer(1000000, new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								reset();
							}
						});
						timer1.setInitialDelay(2000);
						timer1.start();

					}
					break;
				}
			}

			//Check for losing conditions
			if (livesInt == 0) {
				level = -1;
				playing = false;
				gameRunning = false;

				status.setText("You Lose!");
			}

			//Check if the level is passed
			if (circles.isEmpty()) {
				playing = false;
				gameRunning = false;
				status.setText("You Win!");
				level += 1;
				//Delay until next level resets automatically. 
				Timer timer1 = new Timer(1000000, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						reset();
					}
				});
				timer1.setInitialDelay(2000);
				timer1.start();

			}

			// update the display
			repaint();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		background.draw(g);
		for (int i = 0; i < guy_front.size(); i++) {
			guy_front.get(i).pos_x = i * guy_front.get(i).width;
			guy_front.get(i).draw(g);
		}

		scoreBox.draw(g);
		timeKeeper.draw(g);
		for (int i = 0; i < clocks.size(); i++) {
			clocks.get(i).draw(g);
		}

		for (int i = 0; i < extraLivesArray.size(); i++) {
			extraLivesArray.get(i).draw(g);
		}

		for (int i = 0; i < gravArrow.size(); i++) {
			gravArrow.get(i).draw(g);
		}

		arrow.draw(g);
		guy.draw(g);
		
		//Draw stuff in between playing. So, lives and levels if life lost or 
		//level passed. 
		for (int i = 0; i < circles.size(); i++) {
			circles.get(i).draw(g);
		}
		lives = "Lives: " + Integer.toString(livesInt);

		if (playing == false && livesInt > 0 && circles.isEmpty()) {
			hit.draw(g);
			g.setColor(Color.black);
			Font f = new Font("Dialog", Font.PLAIN, 50);
			g.setFont(f);

			g.drawString("Lives: " + guy_front.size() + " ... Level "
					+ (level + 1), hit.pos_x + 55, hit.pos_y + hit.height / 2
					+ 10);

			timeKeeper.color = Color.GREEN;
			timeKeeper.width = COURT_WIDTH - 57;

			timeKeeper.draw(g);

		} else if (playing == false && livesInt < 1) {
			Font f = new Font("Dialog", Font.PLAIN, 50);
			g.setFont(f);
			if (highScore < pastScore) {
				g.drawString("NEW HIGH SCORE!!!!!", COURT_WIDTH / 2 - 250, 400);
				try {
					out = new FileWriter("highScore.txt");
					BufferedWriter bw = new BufferedWriter(out);
					bw.write(Integer.toString(pastScore));
					bw.close();

					in = new FileReader("highScore.txt");
					BufferedReader br = new BufferedReader(in);
					highScore = Integer.valueOf(br.readLine());
					br.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
				highScoreStatus.setText("HIGH SCORE: "
						+ Integer.toString(highScore));
			}

			hit.draw(g);
			g.setColor(Color.black);

			lives = "Lives: 0 ... Score: " + pastScore;
			g.drawString(lives, hit.pos_x + 55, hit.pos_y + hit.height / 2 + 10);
		} else if (playing == false && livesInt > 0) {
			hit.draw(g);
			g.setColor(Color.black);
			Font f = new Font("Dialog", Font.PLAIN, 50);
			g.setFont(f);
			if (level >= 12) {
				g.drawString("YOU WIN...Score: " + pastScore, hit.pos_x + 55,
						hit.pos_y + hit.height / 2 + 10);
			} else {
				g.drawString(lives, hit.pos_x + 200, hit.pos_y + hit.height / 2
						+ 10);
			}
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}
}
