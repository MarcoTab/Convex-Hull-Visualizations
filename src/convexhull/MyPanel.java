package convexhull;

import java.awt.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MyPanel extends JPanel implements MouseListener {
	
	private static final long serialVersionUID = 1L;

	private JButton start = new JButton("Compute CH");
	
	private String[] algos = {"Slow Convex Hull", "Graham's Algorithm", "Quickhull", "Jarvis's March", "Incremental Construction", "Divide and Conquer"/*, "Chan's Algorithm"*/};
	private JComboBox<String> algoList = new JComboBox<String>(algos);
	
	private JButton restart = new JButton("Start Over");
	
	private JFileChooser input = new JFileChooser();
	private JButton inputBut = new JButton("Select File (CSV)");
	private JButton saveBut = new JButton("Save");
	private FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files only", "csv");
	private int retvalInput;
	
	private JTextArea results = new JTextArea(6, 26);
	private JScrollPane resultPanel = new JScrollPane(results); 
	
	//private static double scale = 1000;
	private static double scaleX = 1000;
	private static double scaleY = 1000;
	private static double offsetX = 10;
	private static double offsetY = 10;
	
	
	private static int windowHeight = 1000;
	private static int windowWidth = 1000;
	
	public void setHeight(int height) {
		windowHeight = height;
	}
	public void setWidth(int width) {
		windowWidth = width;
	}
	
//	private static int windowSize = windowHeight;
	
	private static Exception exception = null;
	
	private static long time = 0;
	private static int speed = 0;
	private final int MAX_SPEED = 0;
	private final int MIN_SPEED = 1000;
	private JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, MAX_SPEED, MIN_SPEED, MAX_SPEED);
	private JLabel speedLabel = new JLabel("Animation Delay (ms)");
	final private static Color BLUE = new Color(25, 90, 255);
	final private static Color GREEN = new Color(50, 150, 0);
	final private static Color RED = new Color(255, 0, 0);
	final private static Color BLACK = new Color(0,0,0);
	final private static Color WHITE = new Color(255,255,255);
	
	final public static int START = 1;
	final public static int END = 0;
	final public static int NEITHER = -1;
	
	@SuppressWarnings("unused")
	private static volatile Thread blinker = null;

	private static void runThread() {
//		blinker = new Thread(Thread.currentThread());
//        blinker.start();
//        blinker.setName("BLINKY");
//        run();
//        blinker = null;
		try {Thread.sleep(speed);} catch(InterruptedException ex){Thread.currentThread().interrupt();}
    }

    @SuppressWarnings("unused")
	private static void run() {
    	CountDownLatch count = new CountDownLatch(1);
//    	System.out.println(speed);
//    	synchronized (speed) {
//    		try {Thread.currentThread().wait(speed);} catch (InterruptedException e){}
//    	}
    }
	
	private static List<Point2D> pntarrScale = new ArrayList<Point2D>();
	private static List<Point2D> pntarrReal = new ArrayList<Point2D>();
	
	private static List<Integer> outarr = new ArrayList<Integer>();
	
	private static List<Line2D> linarr = new ArrayList<Line2D>();
	
	
	
	public MyPanel(int x, int y) {
		this.setSize(x, y);
		windowHeight = y;
		windowWidth = x;
		
		
		this.add(algoList);
		algoList.setBounds(3, 3, 190, 25);
		algoList.addMouseListener(this);
		
		this.add(inputBut);
		inputBut.setBounds(algoList.getX() + algoList.getWidth() + 3, algoList.getY(), 150, 25);
		inputBut.addMouseListener(this);
		input.setFileFilter(filter);
		
		this.add(saveBut);
		saveBut.setBounds(inputBut.getX() + inputBut.getWidth() + 3, inputBut.getY(), 150, 25);
		saveBut.addMouseListener(this);
		
		this.add(start);
		start.setBounds(this.getWidth() - 153 , 3 ,150,25);
		start.addMouseListener(this);
		
		this.add(restart);
		restart.setBounds(start.getX(), 31, 150, 25);
		restart.addMouseListener(this);
		
		
		this.add(resultPanel);
		results.setEditable(false);
		resultPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		resultPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		resultPanel.setBounds(3, this.getHeight()-100, 250, 96);
		
		this.add(speedSlider);
		speedSlider.setBounds(350, this.getHeight()-60, this.getWidth()-483, 55);
		speedSlider.setMajorTickSpacing(500);
		speedSlider.setMinorTickSpacing(125);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		speedSlider.setSnapToTicks(true);
		speedSlider.addMouseListener(this);
		
		this.add(speedLabel);
		speedLabel.setBounds(speedSlider.getX(), speedSlider.getY()-21, 150, 16);

		
		addMouseListener(this);
		this.setBackground(WHITE);
		this.setLayout(null);
		this.setVisible(true);
		
		
		this.addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentResized(ComponentEvent e) {
	            // TODO handle the change in relative way instead of absolute coordinates?
	        	algoList.setBounds(3, 3, 190, 25);
	        	inputBut.setBounds(algoList.getX() + algoList.getWidth() + 3, algoList.getY(), 150, 25);
	        	if (inputBut.getX() + inputBut.getWidth() + 3 >= e.getComponent().getWidth()) {
	        		inputBut.setBounds(algoList.getX(), algoList.getY() + algoList.getHeight() + 3, 150, 25);
	        	}
	        	saveBut.setBounds(inputBut.getX() + inputBut.getWidth() + 3, inputBut.getY(), 150, 25);
	        	if (saveBut.getX() + saveBut.getWidth() + 3 >= e.getComponent().getWidth()) {
	        		saveBut.setBounds(inputBut.getX(), inputBut.getY() + inputBut.getHeight() + 3, 150, 25);
	        	}
	        	start.setBounds(e.getComponent().getWidth() - 153 , 3 ,150,25);
	        	if (start.getX() <= saveBut.getX() + saveBut.getWidth() + 3) {
	        		start.setBounds(e.getComponent().getWidth() - 153, saveBut.getY()+saveBut.getHeight()+3, 150, 25);
	        	}
	        	restart.setBounds(start.getX(), start.getY() + start.getHeight() + 3, 150, 25);
	        	
	        	
	        	resultPanel.setBounds(3, e.getComponent().getHeight()-100, 250, 96);
	        	speedSlider.setBounds(350, e.getComponent().getHeight()-60, e.getComponent().getWidth()-483, 55);
//	        	speedLabel.setText(String.format("%d",speedSlider.getWidth()));  
	        	if (speedSlider.getWidth() < 250) {
	        		speedSlider.setBounds(resultPanel.getX(), resultPanel.getY()-58, 250, 55);
	        	}
	        	speedLabel.setBounds(speedSlider.getX(), speedSlider.getY()-21, 150, 16);
	        	
	        	((MyPanel) e.getComponent()).setHeight(e.getComponent().getHeight());
	        	((MyPanel) e.getComponent()).setWidth(e.getComponent().getWidth());
	        	if (linarr.size() > 0) {
	        		linarr = toRealLines(linarr);
	        	}
	        	if (pntarrReal.size() > 0) {
		        	MyPanel.scale();
		        	pntarrScale = toScale(pntarrReal);
	        	} else {
	        		scaleX = e.getComponent().getWidth();
	        		scaleY = e.getComponent().getHeight();
	        	}
	        	if (linarr.size() > 0) {
	        		linarr = toScaleLines(linarr);
	        	}
	        	
	        	e.getComponent().repaint();
	        }
	    });
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(BLACK);
		for (int i = 0; i < pntarrScale.size(); i++) {
			g2d.fillOval((int) pntarrScale.get(i).getX()-5, (int) pntarrScale.get(i).getY()-5, 10, 10);
			g2d.drawString(((Integer) i).toString(), (int) pntarrScale.get(i).getX()+5, (int) pntarrScale.get(i).getY()+15);
		}
		g2d.setColor(GREEN);
		for (int i = 0; i < outarr.size(); i++) {
			g2d.fillOval((int) pntarrScale.get(outarr.get(i)).getX()-5, (int) pntarrScale.get(outarr.get(i)).getY()-5, 10, 10);
		}
		for (int i = 0; i < linarr.size(); i++) {
			Line2D cur = linarr.get(i);
			g2d.drawLine((int) cur.getStart().getX(), (int) cur.getStart().getY(), (int) cur.getEnd().getX(), (int) cur.getEnd().getY());
			
		}
		int len = outarr.size();
		results.setText("");
		if (exception != null) {
			results.append(exception.getMessage());
			exception = null;
		} else if (len != 0) {
			results.append("Points on the Convex Hull:\n");
			for (int i = 0; i < len; i++) {
				results.append(outarr.get(i)+": " + pntarrReal.get(outarr.get(i)).toString() + "\n");
			}
			if (time != 0) {
				results.append("Computed in " + time/1000.0 + " seconds.\n");
				time = 0;
			}
		} else {
			results.append("Points on the Convex Hull:\nNone\n");
		}

	    
		
		
	}
	
	public void mouseExited(MouseEvent e) {}
	
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == algoList) {
			
		}
		else if (e.getSource() == speedSlider) {
			
		}
		else if (e.getSource() == inputBut) {
			retvalInput = input.showOpenDialog(this);
			if (retvalInput == JFileChooser.APPROVE_OPTION && input.getSelectedFile() != null) {
				List<Point2D> help = new ArrayList<Point2D>();
				help.addAll(csvRead(input.getSelectedFile()));
				pntarrReal.addAll(help);
				scale();
				pntarrScale.addAll(toScale(help));
				repaint();
				input.setSelectedFile(null); // Clear the file path so points don't get added twice.
				inputBut.setText("Select File (CSV)");
			}
			
			
			
			
		}
		else if (e.getSource() == saveBut) {
			if (pntarrReal.size() > 2) {
				int retvalOutput = input.showSaveDialog(this);
				if (retvalOutput == JFileChooser.APPROVE_OPTION) {
					File file = input.getSelectedFile();
					
					try {
						FileWriter writer = new FileWriter(file);
						for (int j = 0; j < pntarrReal.size()-1; j++) {
							writer.append(pntarrReal.get(j).getX() + "," + pntarrReal.get(j).getY());
							writer.append("\n");
						}
						
						writer.append(pntarrReal.get(pntarrReal.size()-1).getX() + "," + pntarrReal.get(pntarrReal.size()-1).getY());
						writer.flush();
						writer.close();
					} catch (NullPointerException ex) {System.out.println("Null File Name '" + file.getName() + "'");} catch (IOException e1) {e1.printStackTrace();}
				}
				input.setSelectedFile(null);
			} else {
				exception = new Exception("You must have at least\n3 points to save a file.");
				repaint();
			}
			
			
		}
		
		else if (e.getSource() == restart) {
			pntarrReal = new ArrayList<Point2D>();
			pntarrScale = new ArrayList<Point2D>();
			linarr = new ArrayList<Line2D>();
			outarr = new ArrayList<Integer>();
			repaint();
		}
		
		else if (e.getSource() == start) {
			speed = (Integer) speedSlider.getValue();
			
			if (linarr.size() != 0) {
				linarr = new ArrayList<Line2D>();
				outarr = new ArrayList<Integer>();
				repaint();
			}
			
			String alg = (String) algoList.getSelectedItem();
			
			if (alg == "Slow Convex Hull") {
				try {
					if (speed == 0) time = System.currentTimeMillis();
					outarr = slowHull(pntarrReal, this);
					if (speed == 0) time = System.currentTimeMillis() - time;
				} catch (Exception tooFewArgs) {
					exception = tooFewArgs;
					tooFewArgs.printStackTrace();
				}
				repaint();
				
			} else if (alg == "Graham's Algorithm") {
				
				try {
					if (speed == 0) time = System.currentTimeMillis();
					outarr = grahamHull(pntarrReal, this);
					if (speed == 0) time = System.currentTimeMillis() - time;
				} catch (Exception tooFewArgs) {
					exception = tooFewArgs;
					tooFewArgs.printStackTrace();
				}
				repaint();
				
			} else if (alg == "Quickhull") {
				try {
					if (speed == 0) time = System.currentTimeMillis();
					outarr = quickHull(pntarrReal, this);
					if (speed == 0) time = System.currentTimeMillis() - time;
				} catch (Exception tooFewArgs) {
					exception = tooFewArgs;
					tooFewArgs.printStackTrace();
				}
				
				repaint();
				
			} else if (alg == "Jarvis's March") {
				try {
					if (speed == 0) time = System.currentTimeMillis();
					outarr = jarvisHull(pntarrReal, this);
					if (speed == 0) time = System.currentTimeMillis() - time;
				} catch (Exception tooFewArgs) {
					exception = tooFewArgs;
					tooFewArgs.printStackTrace();
				}
				
				repaint();
			} else if (alg == "Incremental Construction") {
				try {
					if (speed == 0) time = System.currentTimeMillis();
					outarr = incrementalHull(pntarrReal, this);
					if (speed == 0) time = System.currentTimeMillis() - time;
				} catch (Exception tooFewArgs) {
					exception = tooFewArgs;
					tooFewArgs.printStackTrace();
				}
				
				repaint();
			} else if (alg == "Divide and Conquer") {
				try {
					if (speed == 0) time = System.currentTimeMillis();
					outarr = dncHull(pntarrReal, this);
					if (speed == 0) time = System.currentTimeMillis() - time;
				} catch (Exception tooFewArgs) {
					exception = tooFewArgs;
					tooFewArgs.printStackTrace();
				}
		
				repaint();
			} else {
				try {
					if (speed == 0) time = System.currentTimeMillis();
					outarr = chanHull(pntarrReal, this);
					if (speed == 0) time = System.currentTimeMillis() - time;
				} catch (Exception tooFewArgs) {
					exception = tooFewArgs;
					tooFewArgs.printStackTrace();
				}
				
				repaint();
			}
		}
		
		else if (e.getButton() == MouseEvent.BUTTON1) {
			Point2D help = new Point2D(e.getX(), e.getY());
			
			
			pntarrScale.add(help);
			pntarrReal.add(toReal(help));
			if (Vector2D.less(pntarrScale.get(pntarrScale.size()-1).getX(), 0) || Vector2D.less(pntarrScale.get(pntarrScale.size()-1).getY(), 0) || Vector2D.greater(pntarrScale.get(pntarrScale.size()-1).getY(), windowHeight) || Vector2D.greater(pntarrScale.get(pntarrScale.size()-1).getX(), windowWidth)) {
				scale();
			}
			repaint();
		} 
		
		else if (e.getButton() == MouseEvent.BUTTON3) {
			System.out.println("Clicked");
			speed = 0;
			
//			synchronized (speed) {
//				speed.notifyAll();
//			}
			
		}
	}
	
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	
	
	/**
	 * Implements the naive, or initial, algorithm for computing a convex hull
	 * @param input Point2D List (List<Point2D>) representing the points in the set
	 * @param container MyPanel representing a container. If null, no animation will be drawn.
	 * @return List<Integer> representing the indices of the points on the convex hull.
	 * @throws Exception 
	 */
	public static List<Integer> slowHull(List<Point2D> inputReal, MyPanel container) throws Exception {
		List<Point2D> inputScale = toScale(inputReal);
		List<Integer> output = new ArrayList<Integer>(); // Output list
		int len = inputReal.size(); // for loop use
		if (len <= 2) {
			throw new Exception("Too few points to compute \nwith Slow Convex Hull.");
		}
		Graphics2D g2d;
		try {g2d = (Graphics2D) container.getGraphics();} catch(NullPointerException ex) {g2d = null;}
		boolean draw = container != null && g2d != null;
		
		for (int i = 0; i < len; i++) { // for every point in input,
			for (int j = 0; j < len; j++) { // and every other (different) point in input
				if (i != j) {
					
					// Bookkeeping
					boolean valid = true;
					List<Point2D> collinear = new ArrayList<Point2D>();
					
					Line2D l = new Line2D(inputReal.get(i), inputReal.get(j)); // Draw a line
					Line2D l1 = toScale(l);
					linarr.add(l1);
					if (draw && speed != 0) {
						container.paintComponent(g2d);
						g2d.setColor(Color.GRAY);
						int b = (int) ( l1.getStart().getY() - ((l1.getEnd().getY() - l1.getStart().getY()) / (l1.getEnd().getX() - l1.getStart().getX()))*l1.getStart().getX());
						g2d.drawLine(0, b, windowWidth, (int) (((l1.getEnd().getY() - l1.getStart().getY()) / (l1.getEnd().getX() - l1.getStart().getX()))*windowWidth+b));
						g2d.setColor(BLUE);
						drawLine(g2d, l1);
						runThread();
					}

					for (int k = 0; k < len; k++) { // Check if this third point is 'inside' the convex hull, i.e. not to the left of the line we're checking.
						if (k != j && k != i) {
							
							if (Vector2D.less(Vector2D.above(l, inputReal.get(k)), 0)) { // If there is a point to the 'left' of the line (checking is backwards because the y axis is inverted for JPanel, if this were a normal xy plane, the checking would be > 0, not < 0)
								valid = false; // l is not valid
								if (draw && speed != 0) {
									g2d.setColor(RED);
									drawPoint(g2d, inputScale.get(k));
									runThread();
								}
								
							} else if (Vector2D.equals(Vector2D.above(l, inputReal.get(k)), 0)) { // l has collinear points, further testing is required (if valid).
								collinear.add(inputReal.get(k));
								if (draw && speed != 0) {
									g2d.setColor(GREEN);
									drawPoint(g2d, inputScale.get(k));
									runThread();
								}
								
							} else {
								if (draw && speed != 0) {
									g2d.setColor(GREEN);
									drawPoint(g2d, inputScale.get(k));
									runThread();									
								}
							}
						}
					}
					
					if (valid) {
						if (draw && speed != 0) {
							g2d.setColor(GREEN);
							drawLine(g2d, l1);
							runThread();
							container.paintComponent(g2d);
							runThread();
							
						}
						
						if (collinear.size() > 0) {
							collinear.add(l.getEnd());
							collinear.add(l.getStart());
							allButEnds(collinear); // Will cut off endpoints, i.e. the points we actually want to include in the convex hull
						}
						if (output.indexOf(pntarrReal.indexOf(l.getStart())) == -1 && !collinear.contains(l.getStart())) { // Only add the start of the line if the point isn't in the (now fixed) collinear array 
							output.add(pntarrReal.indexOf(l.getStart()));
						}
						if (output.indexOf(pntarrReal.indexOf(l.getEnd())) == -1 && !collinear.contains(l.getEnd())) { // Similar to above, but with the end of the line
							output.add(pntarrReal.indexOf(l.getEnd()));
						}
						 
					} else {
						if (draw && speed != 0) {
							g2d.setColor(RED);
							drawLine(g2d, l1);
							runThread();
						}
						linarr.remove(l1);
						if (draw && speed != 0) {
							container.paintComponent(g2d);
							runThread();
						}
					}
				}
			}
		}
		return output;
	}
	
	/**
	 * SlowHull helper function. Given an array of collinear points finds the end points and returns an array containing all but the endpoints.
	 * @param pntarr List<Point2D> representing a set of collinear points
	 */
	private static void allButEnds(List<Point2D> pntarr) {
		if (pntarr.size() >= 3) { // As long as the input array isn't empty, we can do stuff.
			
			int min = 0;
			int max = 0;
			
			if (Vector2D.equals(pntarr.get(0).getY(), pntarr.get(1).getY())) { // If the line is horizontal, we only check x coordinates
				
				for (int k = 1; k < pntarr.size(); k++) { // Find min and max
					if (Vector2D.less(pntarr.get(k).getX(), pntarr.get(min).getX())) {
						min = k;
					} else if (Vector2D.greater(pntarr.get(k).getX(), pntarr.get(max).getX())) {
						max = k;
					}
				}
				
			} else {
				
				for (int k = 1; k < pntarr.size(); k++) {
					if (Vector2D.less(pntarr.get(k).getY(), pntarr.get(min).getY())) {
						min = k;
					} else if (Vector2D.greater(pntarr.get(k).getY(), pntarr.get(max).getY())) {
						max = k;
					}
				}
				
			}
			
			if (min > max) {
				pntarr.remove(min); // Remove endpoints
				pntarr.remove(max);
			} else if (min < max) {
				pntarr.remove(max);
				pntarr.remove(min); // Remove endpoints
			} else {
				pntarr.remove(min);
			}
			
		}
	}
	
	
	/**
	 * Implements Graham's algorithm for computing a convex hull
	 * @param input Point2D List (List<Point2D>) representing the points in the set
	 * @param container MyPanel representing a container. If null, no animation will be drawn.
	 * @return List<Integer> representing the indices of the points on the convex hull.
	 * @throws Exception 
	 */
	public static List<Integer> grahamHull(List<Point2D> inputReal, MyPanel container) throws Exception{
		
		int size = inputReal.size();
		if (size <= 2) {
			throw new Exception("Too few points to compute \nwith Graham's Algorithm.");
		}
		// Bookkeeping
		List<Point2D> upper = new ArrayList<Point2D>();
		List<Point2D> lower = new ArrayList<Point2D>();
		
		int maxIndex = size-1;
		
		Graphics2D g2d;
		try {g2d = (Graphics2D) container.getGraphics();} catch(NullPointerException ex) {g2d = null;}
		boolean draw = container != null && g2d != null;
		
		Collections.sort(inputReal, inputReal.get(0).new SortByX()); // I didn't want the sorting to depend on the container, so I put it in the Point2D class
		pntarrScale = toScale(inputReal);
		
		upper.add(inputReal.get(0));
		upper.add(inputReal.get(1));
		linarr.add(toScale(new Line2D(upper.get(0), upper.get(1))));
		if (draw && speed != 0) {
			container.paintComponent(g2d);
			g2d.setColor(BLUE);
			drawLine(g2d, linarr.get(0));
		}
		
		
		for (int i = 2; i < inputReal.size(); i++) {
			upper.add(inputReal.get(i)); // Add new point
			
			linarr.add(toScale(new Line2D(upper.get(upper.size()-2), upper.get(upper.size()-1))));
			if (draw && speed != 0) {
				if (upper.size() > 2) {
					container.paintComponent(g2d);
					g2d.setColor(BLUE);
					drawLine(g2d, linarr.get(linarr.size()-1));
					drawLine(g2d, linarr.get(linarr.size()-2));
					drawAngle(g2d, linarr.get(linarr.size()-2), linarr.get(linarr.size()-1));
					runThread();
				}
			}
			
			while (upper.size() > 2 && !rightTurn(upper)) { // While there's at least 2 points and a left turn,
				if (draw && speed != 0) {
					g2d.setColor(RED);
					drawLine(g2d, linarr.get(linarr.size()-1));
					drawLine(g2d, linarr.get(linarr.size()-2));
					drawAngle(g2d, linarr.get(linarr.size()-2), linarr.get(linarr.size()-1));
					runThread();
				}
				
				upper.remove(upper.size()-2); // Remove the middle of the last 3
				
				linarr.remove(linarr.size()-2);
				linarr.remove(linarr.size()-1);
				linarr.add(toScale(new Line2D(upper.get(upper.size()-2), upper.get(upper.size()-1))));
				if (draw && speed != 0) {
					if (upper.size() > 2) { 
						container.paintComponent(g2d);
						g2d.setColor(BLUE);
						drawLine(g2d, linarr.get(linarr.size()-1));
						drawLine(g2d, linarr.get(linarr.size()-2));
						drawAngle(g2d, linarr.get(linarr.size()-2), linarr.get(linarr.size()-1));
						runThread();
					}
				}
			}
			
			if (draw && speed != 0) {
				if (upper.size() > 2) {
					container.paintComponent(g2d);
					g2d.setColor(GREEN);
					drawLine(g2d, linarr.get(linarr.size()-1));
					drawLine(g2d, linarr.get(linarr.size()-2));
					drawAngle(g2d, linarr.get(linarr.size()-2), linarr.get(linarr.size()-1));
					runThread();
				}
				
			}
			
		}
		
		lower.add(inputReal.get(maxIndex));
		lower.add(inputReal.get(maxIndex - 1));
		
		linarr.add(toScale(new Line2D(lower.get(0), lower.get(1))));
		
		if (draw && speed != 0) {
			container.paintComponent(g2d);
			g2d.setColor(BLUE);
			drawLine(g2d, linarr.get(linarr.size()-1));
		}
		
		
		for (int i = maxIndex-2; i >=0; i--) {
			lower.add(inputReal.get(i));
			
			linarr.add(toScale(new Line2D(lower.get(lower.size()-2), lower.get(lower.size()-1))));
			if (draw && speed != 0) {
				if (lower.size() > 2) {
					container.paintComponent(g2d);
					g2d.setColor(BLUE);
					drawLine(g2d, linarr.get(linarr.size()-1));
					drawLine(g2d, linarr.get(linarr.size()-2));
					drawAngle(g2d, linarr.get(linarr.size()-2), linarr.get(linarr.size()-1));
					runThread();
				}
			}
			
			while (lower.size() > 2 && !rightTurn(lower)) {
				if (draw && speed != 0) {
					g2d.setColor(RED);
					drawLine(g2d, linarr.get(linarr.size()-1));
					drawLine(g2d, linarr.get(linarr.size()-2));
					drawAngle(g2d, linarr.get(linarr.size()-2), linarr.get(linarr.size()-1));
					
					runThread();
				}
				
				lower.remove(lower.size()-2); // Remove the middle of the last three
				
				linarr.remove(linarr.size()-2);
				linarr.remove(linarr.size()-1);
				linarr.add(toScale(new Line2D(lower.get(lower.size()-2), lower.get(lower.size()-1))));
				if (draw && speed != 0) {
					if (lower.size() > 2) {
						container.paintComponent(g2d);
						g2d.setColor(BLUE);
						drawLine(g2d, linarr.get(linarr.size()-1));
						drawLine(g2d, linarr.get(linarr.size()-2));
						drawAngle(g2d, linarr.get(linarr.size()-2), linarr.get(linarr.size()-1));
						runThread();
					}
				}
				
			}
			
			if (draw && speed != 0) {
				if (lower.size() > 2) {
					container.paintComponent(g2d);
					g2d.setColor(GREEN);
					drawLine(g2d, linarr.get(linarr.size()-1));
					drawLine(g2d, linarr.get(linarr.size()-2));
					drawAngle(g2d, linarr.get(linarr.size()-2), linarr.get(linarr.size()-1));
					runThread();					
				}
			}
		}
		
		
		lower.remove(0); // Remove duplicates
		lower.remove(lower.size()-1);
		if (upper.size() > 0 && lower.size() > 0) {
			linarr.add(toScale(new Line2D(upper.get(0), lower.get(lower.size()-1))));
			linarr.add(toScale(new Line2D(lower.get(0), upper.get(upper.size()-1))));
		}
		
		
		upper.addAll(lower); // Append for returning
		List<Integer> output = new ArrayList<Integer>();
		for (Point2D i : upper) {
			output.add(pntarrReal.indexOf(i));
		}
		return output; // return the finalized convex hull list.
	}
	
	private static boolean rightTurn(List<Point2D> arr) {
		int k = arr.size()-1;
		return Vector2D.greater(Vector2D.turn(arr.get(k-2), arr.get(k-1), arr.get(k)), 0);
		
	}

	
	/**
	 * Implements the QuickHull algorithm for computing a convex hull
	 * @param input Point2D List representing the set of points
	 * @param container MyPanel representing a container. If null, no animation will be drawn.
	 * @return List<Integer> representing the indices of the points on the convex hull.
	 * @throws Exception 
	 */
	public static List<Integer> quickHull(List<Point2D> inputReal, MyPanel container) throws Exception {
		int size = inputReal.size();
		if (size <= 2) {
			throw new Exception("Too few points to compute \nwith Quickhull.");
		}
		Graphics2D g2d;
		try {g2d = (Graphics2D) container.getGraphics();} catch(NullPointerException ex) {g2d = null;}
		boolean draw = container != null && g2d != null;
		
		List<Integer> output = new ArrayList<Integer>();
		Collections.sort(inputReal, inputReal.get(0).new SortByX());
		pntarrScale = toScale(inputReal);
		
		Point2D min = inputReal.get(0);
		Point2D max = inputReal.get(size-1);
		List<Point2D> s1 = new ArrayList<Point2D>();
		List<Point2D> s2 = new ArrayList<Point2D>();
		Line2D curline = toScale(new Line2D(min, max));
		Line2D curlineCalc = new Line2D(min, max);
		
		if (draw && speed != 0) {
			container.paintComponent(g2d);
			g2d.setColor(BLUE);
			drawLine(g2d, curline);
			runThread();
		}
		
		for (int i = 0; i < inputReal.size(); i++) {
			if (!(inputReal.get(i).equals(min) || inputReal.get(i).equals(max))) {
				if (Vector2D.less(Vector2D.above(curlineCalc, inputReal.get(i)), 0)) {
					if (draw && speed != 0) {
						g2d.setColor(GREEN);
						drawPoint(g2d, pntarrScale.get(i));
						g2d.drawString("Set 1", (int) pntarrScale.get(i).getX()+10, (int) pntarrScale.get(i).getY()-5);
						runThread();
					}
					s1.add(inputReal.get(i));
				} else {
					if (draw && speed != 0) {
						g2d.setColor(RED);
						drawPoint(g2d, pntarrScale.get(i));
						g2d.drawString("Set 2", (int) pntarrScale.get(i).getX()+10, (int) pntarrScale.get(i).getY()-5);
						runThread();
					}
					s2.add(inputReal.get(i));
				}
			}
			
		}
		output.add(inputReal.indexOf(min));
		output.add(inputReal.indexOf(max));
		
		findHull(s1, min, max, output, container);
		findHull(s2, max, min, output, container);
		return output;
	}
	
	/**
	 * Helper function for QuickHull, does all the actual work
	 * @param points List<Point2D> representing the set of points in this iteration of findHull
	 * @param p Point2D representing the first point on the convex hull
	 * @param q Point2D representing the second point on the convex hull
	 * @param output List<Point2D> representing the set of points on the convex hull. Modified through reference, not returned
	 * @param container MyPanel representing a container . If null, no animation will be drawn.
	 */
	private static void findHull(List<Point2D> points, Point2D p, Point2D q, List<Integer> output, MyPanel container) {
		Graphics2D g2d;
		try {g2d = (Graphics2D) container.getGraphics();} catch(NullPointerException ex) {g2d = null;}
		boolean draw = container != null && g2d != null;
		List<Point2D> scaledpoints = toScale(points);
		if (points.size() == 0) {
			linarr.add(toScale(new Line2D(p, q)));
			if (draw && speed != 0) {
				container.paintComponent(g2d);
				g2d.setColor(GREEN);
				drawLine(g2d, linarr.get(linarr.size()-1));
				runThread();
				container.paintComponent(g2d);
			}
			return;
		} else {
			if (draw && speed != 0) {
				g2d.setColor(BLUE);
				drawLine(g2d, toScale(new Line2D(p, q)));
				runThread();
				for (int i = 0; i < points.size(); i++) {
					Point2D inter = Vector2D.findIntersection(toScale(new Line2D(p, q)), scaledpoints.get(i));
					drawPoint(g2d, scaledpoints.get(i));
					g2d.drawLine((int) scaledpoints.get(i).getX(), (int) scaledpoints.get(i).getY(), (int) inter.getX(), (int) inter.getY());
					runThread();
				}
			}
			Point2D c;
			try { c = Vector2D.maxOrthoDist(new Line2D(p, q), points);}
			catch (Exception collinear) { c = null; return;}// Find farthest orthogonal point			
			if (draw && speed != 0) {
				for (int i = 0; i < points.size(); i++) {
					g2d.setColor(RED);
					if (points.get(i).equals(c)) {
						g2d.setColor(GREEN);
					}
					Point2D inter = Vector2D.findIntersection(toScale(new Line2D(p, q)), scaledpoints.get(i));
					drawPoint(g2d, scaledpoints.get(i));
					g2d.drawLine((int) scaledpoints.get(i).getX(), (int) scaledpoints.get(i).getY(), (int) inter.getX(), (int) inter.getY());
					
				}
				runThread();
				container.paintComponent(g2d);
			}
			
			output.add(pntarrReal.indexOf(c));
			List<Point2D> l1 = new ArrayList<Point2D>();
			List<Point2D> l2 = new ArrayList<Point2D>();
			Line2D curline = new Line2D(p, c);
			
			if (draw && speed != 0) {
				g2d.setColor(BLUE);
				drawLine(g2d, toScale(new Line2D(p, c)));
				runThread();
			}
			
			for (int i = 0; i < points.size(); i++) {
				if (!c.equals(points.get(i))) {
					if (Vector2D.less(Vector2D.above(curline, points.get(i)), 0)) {
						if (draw && speed != 0) {
							g2d.setColor(GREEN);
							drawPoint(g2d, scaledpoints.get(i));
							runThread();
						}
						
						l1.add(points.get(i));
						
					} else {
						if (draw && speed != 0) {
							g2d.setColor(RED);
							drawPoint(g2d, scaledpoints.get(i));
							runThread();
						}
					}
				}
			}
			
			findHull(l1, p, c, output, container);
			
			curline = new Line2D(c, q);
			if (draw && speed != 0) {
				container.paintComponent(g2d);
				g2d.setColor(BLUE);
				drawLine(g2d, toScale(new Line2D(c, q)));
				runThread();
			}
			
			for (int i = 0; i < points.size(); i++) {
				if (!c.equals(points.get(i))) {
					if (Vector2D.less(Vector2D.above(curline, points.get(i)), 0)) {
						if (draw && speed != 0) {
							g2d.setColor(GREEN);
							drawPoint(g2d, scaledpoints.get(i));
							runThread();
						}
						l2.add(points.get(i));
					} else {
						if (draw && speed != 0) {
							g2d.setColor(RED);
							drawPoint(g2d, scaledpoints.get(i));
							runThread();
						}
					}
				}
			}
			
			findHull(l2, c, q, output, container);
			return;
		}
	}
	
	
	/**
	 * Implements Jarvis's March for computing the convex hull
	 * @param inputReal List<Point2D> representing the set of points.
	 * @param container MyPanel representing a container. If null, no animation will be drawn.
	 * @return List<Integer> representing the indices of the points on the convex hull.
	 * @throws Exception
	 */
	public static List<Integer> jarvisHull(List<Point2D> inputReal, MyPanel container) throws Exception{
		int size = inputReal.size();
		if (size < 3) {
			throw new Exception("Too few points to compute\nJarvis's March");
		}
		
		Graphics2D g2d;
		try {g2d = (Graphics2D) container.getGraphics();} catch(NullPointerException ex) {g2d = null;}
		boolean draw = container != null && g2d != null;
		
		List<Integer> output = new ArrayList<Integer>();
		Point2D pointOnHull = inputReal.get(0);
		for (Point2D i : inputReal) {
			if (Vector2D.less(i.getX(), pointOnHull.getX())) {
				pointOnHull = i;
			}
		}
		
		pntarrScale = toScale(inputReal);
													
		Point2D endpoint;
		
		int i = 0;
		do {
			output.add(inputReal.indexOf(pointOnHull));
			endpoint = inputReal.get(0);
			
			for (int j = 0; j < size; j++) {
				
				Vector2D v1 = new Vector2D(pointOnHull, endpoint);
				Vector2D v2 = new Vector2D(pointOnHull, inputReal.get(j));
				
				Line2D cur = new Line2D(pntarrScale.get(inputReal.indexOf(pointOnHull)), pntarrScale.get(inputReal.indexOf(endpoint)));
				Line2D check = new Line2D(pntarrScale.get(inputReal.indexOf(pointOnHull)), pntarrScale.get(j));
				
				if (draw && speed != 0) {
					g2d.setColor(BLUE);
					drawLine(g2d, cur);
					drawLine(g2d, check);
					runThread();
				}
				
				if (endpoint.equals(pointOnHull) || Vector2D.less(Vector2D.above(new Line2D(inputReal.get(output.get(i)), endpoint), inputReal.get(j)), 0)) {
					endpoint = inputReal.get(j);
					if (draw && speed != 0) {
						g2d.setColor(RED);
						drawLine(g2d, cur);
						runThread();
						container.paintComponent(g2d);
					}
				} else if (Vector2D.equals(Vector2D.above(new Line2D(inputReal.get(output.get(i)), endpoint), inputReal.get(j)), 0) && !inputReal.get(output.get(i)).equals(endpoint) && !inputReal.get(j).equals(endpoint) && !inputReal.get(j).equals(inputReal.get(output.get(i))) && Vector2D.greater(v2.getMag(), v1.getMag())) {
					endpoint = inputReal.get(j);
					if (draw && speed != 0) {
						g2d.setColor(RED);
						drawLine(g2d, cur);
						runThread();
						container.paintComponent(g2d);
					}
				} else if (draw && speed != 0) {
					g2d.setColor(RED);
					drawLine(g2d, check);
					runThread();
					container.paintComponent(g2d);
				}
				
			}
			i++;
			linarr.add(toScale(new Line2D(pointOnHull, endpoint)));
			if (draw && speed != 0) {
				g2d.setColor(GREEN);
				drawLine(g2d, linarr.get(linarr.size()-1));
				runThread();
				container.paintComponent(g2d);
			}
			pointOnHull = endpoint;
		} while (!endpoint.equals(inputReal.get(output.get(0))));
			
		return output;
	}
 
	
	/**
	 * Implements an incremental solution for computing a convex hull
	 * @param inputReal List<Point2D> representing the set of points
	 * @param container MyPanel representing a container. If null, no animation will be drawn.
	 * @return List<Integer> representing the indices of the points on the convex hull.
	 * @throws Exception
	 */
	public static List<Integer> incrementalHull(List<Point2D> inputReal, MyPanel container) throws Exception {
		int size = inputReal.size();
		if (size < 3) {
			throw new Exception("Too few points to compute\nIncremental Construction");
		}

		Graphics2D g2d;
		try {g2d = (Graphics2D) container.getGraphics();} catch(NullPointerException ex) {g2d = null;}
		boolean draw = container != null && g2d != null;
		
		List<Integer> output = new ArrayList<Integer>();
		Collections.sort(inputReal, inputReal.get(0).new SortByX());
		pntarrScale = toScale(inputReal);
		
		// Add first three points in clockwise order
		output.add(0);
		if (Vector2D.equals(Vector2D.above(new Line2D(inputReal.get(0), inputReal.get(1)), inputReal.get(2)), 0)) {
			output.add(2);
			linarr.add(new Line2D(pntarrScale.get(0), pntarrScale.get(2)));
			linarr.add(new Line2D(pntarrScale.get(2), pntarrScale.get(0)));
		} else if (Vector2D.greater(Vector2D.above(new Line2D(inputReal.get(0), inputReal.get(1)), inputReal.get(2)), 0)) { 
			output.add(1);
			output.add(2);
			linarr.add(new Line2D(pntarrScale.get(0), pntarrScale.get(1)));
			linarr.add(new Line2D(pntarrScale.get(1), pntarrScale.get(2)));	
			linarr.add(new Line2D(pntarrScale.get(2), pntarrScale.get(0)));
		} else {
			output.add(2);
			output.add(1);
			linarr.add(new Line2D(pntarrScale.get(0), pntarrScale.get(2)));
			linarr.add(new Line2D(pntarrScale.get(2), pntarrScale.get(1)));	
			linarr.add(new Line2D(pntarrScale.get(1), pntarrScale.get(0)));
		}
		output.add(0); // We repeat the first element again to maintain cyclical nature. We remove it later so no points are repeated on the convex hull
		
		if (draw && speed > 0) {
			container.paintComponent(g2d);
			g2d.setColor(GREEN);
			for (Line2D l : linarr) {
				drawLine(g2d, l);
			}
			runThread();
		}
		
		for (int i = 3; i < size; i++) {
			Point2D newpoint = inputReal.get(i);
			Line2D l1 = new Line2D(newpoint, inputReal.get(output.get(0)));
			Line2D l2 = new Line2D(newpoint, inputReal.get(output.get(0)));
			
			for (Integer j : output) {
				if (draw && speed > 0) {
					container.paintComponent(g2d);
					g2d.setColor(BLUE);
					drawLine(g2d, toScale(l1));
					runThread();
				}
				if (Vector2D.less(Vector2D.above(l1, inputReal.get(j)), 0) || (Vector2D.equals(Vector2D.above(l1, inputReal.get(j)), 0) && Vector2D.less(new Vector2D(l1.getStart(), l1.getEnd()).getMag(), new Vector2D(l1.getStart(), inputReal.get(j)).getMag()))) {
					if (draw && speed > 0) {
						g2d.setColor(RED);
						drawLine(g2d, toScale(l1));
					}
					
					l1.set(newpoint, inputReal.get(j));
					
					if (draw && speed > 0) {
						g2d.setColor(BLUE);
						drawLine(g2d, toScale(l1));
						runThread();
					}
				} else if (draw && speed > 0){
					g2d.setColor(RED);
					drawLine(g2d, toScale(new Line2D(newpoint, inputReal.get(j))));
					runThread();
				}
			}
			
			if (draw && speed > 0) {
				container.paintComponent(g2d);
				g2d.setColor(GREEN);
				drawLine(g2d, toScale(l1));
				runThread();
			}
			
			for (Integer j : output) {
				if (draw && speed > 0) {
					container.paintComponent(g2d);
					g2d.setColor(BLUE);
					drawLine(g2d, toScale(l2));
					g2d.setColor(GREEN);
					drawLine(g2d, toScale(l1));
					runThread();
				}
				if (Vector2D.greater(Vector2D.above(l2, inputReal.get(j)), 0) || (Vector2D.equals(Vector2D.above(l2, inputReal.get(j)), 0) && Vector2D.less(new Vector2D(l2.getStart(), l2.getEnd()).getMag(), new Vector2D(l2.getStart(), inputReal.get(j)).getMag()))) {
					if (draw && speed > 0) {
						g2d.setColor(RED);
						drawLine(g2d, toScale(l2));
						
					}
					
					l2.set(newpoint, inputReal.get(j));
					
					if (draw && speed > 0) {
						g2d.setColor(BLUE);
						drawLine(g2d, toScale(l2));
						runThread();
					}
				} else if (draw && speed > 0) {
					g2d.setColor(RED);
					drawLine(g2d, toScale(new Line2D(newpoint, inputReal.get(j))));
					runThread();
				}
			}
			
			if (draw && speed > 0) { 
				container.paintComponent(g2d);
				g2d.setColor(GREEN);
				drawLine(g2d, toScale(l2));
				drawLine(g2d, toScale(l1));
				runThread();
			}
			
			
			
			int max = output.lastIndexOf(inputReal.indexOf(l1.getEnd()));
			int min = output.indexOf(inputReal.indexOf(l2.getEnd()));
			
			if (min > max) {
				int temp = min;
				min = max;
				max = temp;
			}
			
			min += 1;
			while (min != max) {
				output.remove((int) min);
				max--;
				linarr.remove(min-1);
			}
			linarr.remove(max-1);
			output.add(max, inputReal.indexOf(newpoint));
			linarr.add(max-1, new Line2D(pntarrScale.get(output.get(max)), pntarrScale.get(output.get(max+1))));
			linarr.add(max-1, new Line2D(pntarrScale.get(output.get(max-1)), pntarrScale.get(output.get(max))));
			
			
		}
		output.remove(output.size()-1); // Removes repeated element.
		return output;
	}

	
	/**
	 * Implements a divide and conquer solution for computing a convex hull
	 * @param inputReal List<Point2D> representing the set of points.
	 * @param container MyPanel representing a container. If null, no animation will be drawn.
	 * @return List<Integer> representing the indices of the points on the convex hull.
	 * @throws Exception
	 */
	public static List<Integer> dncHull(List<Point2D> inputReal, MyPanel container) throws Exception {
		int size = inputReal.size();
		if (size < 3) {
			throw new Exception("Too few points to compute\nDivide and Conquer");
		}
		
		
		List<Integer> output = new ArrayList<Integer>();
		Collections.sort(inputReal, inputReal.get(0).new SortByX());
		pntarrScale = toScale(inputReal);
		output = dncHulling(inputReal, inputReal, container);
		
		if (container != null) {
			List<Point2D> outputS = new ArrayList<Point2D>();
			for (int i = 0; i < output.size(); i++) {
				outputS.add(pntarrScale.get(output.get(i)));
			}
			
			for (int i = 0; i < linarr.size(); i++) {
				if (indexOf(outputS, linarr.get(i).getStart()) < 0 || indexOf(outputS, linarr.get(i).getEnd()) < 0) {
					linarr.remove(i);
					i--;
				}
			}
		}
		
		return output;
	}
	
	 /**
	  * Helper function for dncHull. Does actual work.
	  * @param set List<Point2D> representing the current set of points.
	  * @param inputReal List<Point2D> representing the whole set of points.
	  * @param container MyPanel representing a container. If null, no animation will be drawn.
	  * @return List<Integer> representing the indices of the points on the convex hull.
	  */
	private static List<Integer> dncHulling(List<Point2D> set, List<Point2D> inputReal,  MyPanel container) {
		int size = set.size();
		
		Graphics2D g2d;
		try {g2d = (Graphics2D) container.getGraphics();} catch(NullPointerException ex) {g2d = null;}
		boolean draw = container != null && g2d != null;
		
		if (draw && speed != 0) {
			container.paintComponent(g2d);
			g2d.setColor(BLUE);
			for (int i = 0; i < set.size(); i++) {
				drawPoint(g2d, toScale(set.get(i)));
			}
			runThread();
		}
		
		List<Integer> output = new ArrayList<Integer>();
		List<Integer> outL = new ArrayList<Integer>();
		List<Integer> outR = new ArrayList<Integer>();

		if (size > 3) {
//			System.out.println("Split with " + size + " points.");
			int leftSize = (int) Math.floor(size/2);
//			System.out.println("Sizes " + leftSize + " " + (size-leftSize));
			List<Point2D> setL = new ArrayList<Point2D>();
			List<Point2D> setR = new ArrayList<Point2D>();
			
			for (int i = 0; i < leftSize; i++) {
				setL.add(set.get(i));
			}
			for (int i = leftSize; i < size; i++) {
				setR.add(set.get(i));
			}
			
			if (draw && speed != 0) {
				double xavg = 0;
				xavg += setL.get(setL.size()-1).getX();
				xavg += setR.get(0).getX();
				xavg /= 2;
				g2d.setColor(Color.darkGray);
				drawLine(g2d, toScale(new Line2D(new Point2D(xavg, -100), new Point2D(xavg, windowHeight + 100))));
				runThread();
				
				
			}
			
			outL = dncHulling(setL, inputReal, container);
			outR = dncHulling(setR, inputReal, container);
			output = dncMerge(outL, outR, inputReal, container);
			
			List<Point2D> outputR = new ArrayList<Point2D>();
			
			for (int i = 0; i < outL.size(); i++) {
				outputR.add(pntarrScale.get(outL.get(i)));
			}
			for (int i = 0; i < outR.size(); i++) {
				outputR.add(pntarrScale.get(outR.get(i)));
			}
			
			List<Point2D> poly = new ArrayList<Point2D>();
			
			for (int i = 0; i < output.size(); i++) {
				poly.add(pntarrScale.get(output.get(i)));
			}
	
			for (int j = 0; j < linarr.size(); j++) {
				
				if ((indexOf(outputR, linarr.get(j).getStart()) >= 0 || indexOf(outputR, linarr.get(j).getEnd()) >= 0) && crosses(linarr.get(j), poly, NEITHER)) {
					linarr.remove(j);
					j--;
				} 
			}
			
		} else {
			
			if (size > 2) {
				
				// Add first three points in clockwise order
				output.add(inputReal.indexOf(set.get(0)));
				if (Vector2D.equals(Vector2D.above(new Line2D(set.get(0), set.get(1)), set.get(2)), 0)) {
					output.add(inputReal.indexOf(set.get(2)));
					linarr.add(new Line2D(pntarrScale.get(inputReal.indexOf(set.get(0))), pntarrScale.get(inputReal.indexOf(set.get(2)))));
					linarr.add(new Line2D(pntarrScale.get(inputReal.indexOf(set.get(2))), pntarrScale.get(inputReal.indexOf(set.get(0)))));
				} else if (Vector2D.greater(Vector2D.above(new Line2D(set.get(0), set.get(1)), set.get(2)), 0)) { 
					output.add(inputReal.indexOf(set.get(1)));
					output.add(inputReal.indexOf(set.get(2)));
					linarr.add(new Line2D(pntarrScale.get(inputReal.indexOf(set.get(0))), pntarrScale.get(inputReal.indexOf(set.get(1)))));
					linarr.add(new Line2D(pntarrScale.get(inputReal.indexOf(set.get(1))), pntarrScale.get(inputReal.indexOf(set.get(2)))));	
					linarr.add(new Line2D(pntarrScale.get(inputReal.indexOf(set.get(2))), pntarrScale.get(inputReal.indexOf(set.get(0)))));
				} else {
					output.add(inputReal.indexOf(set.get(2)));
					output.add(inputReal.indexOf(set.get(1)));
					linarr.add(new Line2D(pntarrScale.get(inputReal.indexOf(set.get(0))), pntarrScale.get(inputReal.indexOf(set.get(2)))));
					linarr.add(new Line2D(pntarrScale.get(inputReal.indexOf(set.get(2))), pntarrScale.get(inputReal.indexOf(set.get(1)))));	
					linarr.add(new Line2D(pntarrScale.get(inputReal.indexOf(set.get(1))), pntarrScale.get(inputReal.indexOf(set.get(0)))));
				}
				if (draw && speed != 0) {
					
					container.paintComponent(g2d);
					g2d.setColor(GREEN);
					for (int i = 0; i < output.size(); i++) {
						drawPoint(g2d, pntarrScale.get(output.get(i)));
					}
					runThread();
				}
			} else {
				output.add(inputReal.indexOf(set.get(0)));
				output.add(inputReal.indexOf(set.get(1)));
				linarr.add(new Line2D(pntarrScale.get(inputReal.indexOf(set.get(0))), pntarrScale.get(inputReal.indexOf(set.get(1)))));
				linarr.add(new Line2D(pntarrScale.get(inputReal.indexOf(set.get(1))), pntarrScale.get(inputReal.indexOf(set.get(0)))));
				if (draw && speed != 0) {
					
					container.paintComponent(g2d);
					g2d.setColor(GREEN);
					for (int i = 0; i < output.size(); i++) {
						drawPoint(g2d, pntarrScale.get(output.get(i)));
					}
					runThread();
				}
			}
			
			
			
						
		}
		
		
		
		return output;
	}
	
	/**
	 * Merges two convex hulls by finding upper and lower tangents and eliminating any extra points on the hull.
	 * @param left List<Integer> representing the left CH
	 * @param right List<Integer> representing the right CH
	 * @param inputReal List<Point2D> representing the entire set of points
	 * @return List<Integer> representing the new merged CH
	 */
	private static List<Integer> dncMerge(List<Integer> ileft, List<Integer> iright, List<Point2D> inputReal, MyPanel container) {
		List<Integer> left = new ArrayList<Integer>();
		List<Integer> right = new ArrayList<Integer>();
		
		left.addAll(ileft);
		right.addAll(iright);
		
		int lsize = left.size();
		int rsize = right.size();
		
		Graphics2D g2d;
		try {g2d = (Graphics2D) container.getGraphics();} catch(NullPointerException ex) {g2d = null;}
		boolean draw = container != null && g2d != null;
		

		Point2D curStart = inputReal.get(left.get(lsize-1));
		Point2D curEnd = inputReal.get(right.get(0));
		
		List<Point2D> curSetR = new ArrayList<Point2D>();
		List<Point2D> curSetL = new ArrayList<Point2D>();
		
		for (int i = 0; i < lsize; i++) {
			curSetL.add(inputReal.get(left.get(i)));
		}
		for (int i = 0; i < rsize; i++) {
			curSetR.add(inputReal.get(right.get(i)));
		}
		
		
		Line2D topLine = new Line2D(curStart, curEnd);
		Line2D botLine = new Line2D(curEnd, curStart);
		
		if (draw && speed != 0) {
			container.paintComponent(g2d);
			g2d.setColor(BLUE);
			drawLine(g2d, toScale(topLine));
			runThread();
		}
		while (crosses(topLine, curSetR, END) || crosses(topLine, curSetL, START)) {
			
			while (crosses(topLine, curSetR, END)) {
				// Change endpoint up
				
				if (draw && speed != 0) {
					g2d.setColor(RED);
					drawLine(g2d, toScale(topLine));
					runThread();
					container.paintComponent(g2d);
				}
				
				int nextInd = curSetR.indexOf(curEnd) + 1;
				if (nextInd >= curSetR.size()) {
					nextInd = 0;
				}
				topLine.set(curStart, curSetR.get(nextInd));
				curEnd = curSetR.get(nextInd);
				
				if (draw && speed != 0) {
					g2d.setColor(BLUE);
					drawLine(g2d, toScale(topLine));
					runThread();
					container.paintComponent(g2d);
				}
				
			}
			while (crosses(topLine, curSetL, START)) {
				// Change startpoint up
				if (draw && speed != 0) {
					g2d.setColor(RED);
					drawLine(g2d, toScale(topLine));
					runThread();
					container.paintComponent(g2d);
				}
				int nextInd = curSetL.indexOf(curStart) + 1;
				if (nextInd >= curSetL.size()) {
					nextInd = 0;
				}
				topLine.set(curSetL.get(nextInd), curEnd);
				curStart = curSetL.get(nextInd);
				
				if (draw && speed != 0) {
					g2d.setColor(BLUE);
					drawLine(g2d, toScale(topLine));
					runThread();
					container.paintComponent(g2d);
				}
			}
		}
		
		if (draw && speed != 0) {
			container.paintComponent(g2d);
			g2d.setColor(GREEN);
			drawLine(g2d, toScale(topLine));
			runThread();
		}
		
		if (draw && speed != 0) {
			container.paintComponent(g2d);
			g2d.setColor(BLUE);
			drawLine(g2d, toScale(botLine));
			g2d.setColor(GREEN);
			drawLine(g2d, toScale(topLine));
			runThread();
		}
		while (crosses(botLine, curSetR, START) || crosses(botLine, curSetL, END)) {
			
			while (crosses(botLine, curSetL, END)) {
				// Change endpoint down
				
				if (draw && speed != 0) {
					g2d.setColor(RED);
					drawLine(g2d, toScale(botLine));
					g2d.setColor(GREEN);
					drawLine(g2d, toScale(topLine));
					runThread();
					container.paintComponent(g2d);
				}
				
				int nextInd = curSetL.indexOf(curStart) - 1;
				if (nextInd < 0) {
					nextInd = curSetL.size()-1;
				}
				botLine.set(curEnd, curSetL.get(nextInd));
				curStart = curSetL.get(nextInd);
				
				if (draw && speed != 0) {
					g2d.setColor(BLUE);
					drawLine(g2d, toScale(botLine));
					g2d.setColor(GREEN);
					drawLine(g2d, toScale(topLine));
					runThread();
					container.paintComponent(g2d);
				}

			}
			while (crosses(botLine, curSetR, START)) {
				// Change startpoint down
				
				if (draw && speed != 0) {
					g2d.setColor(RED);
					drawLine(g2d, toScale(botLine));
					g2d.setColor(GREEN);
					drawLine(g2d, toScale(topLine));
					runThread();
					container.paintComponent(g2d);
				}
				
				int nextInd = curSetR.indexOf(curEnd) - 1;
				if (nextInd < 0) {
					nextInd = curSetR.size()-1;
				}
				botLine.set(curSetR.get(nextInd), curStart);
				curEnd = curSetR.get(nextInd);
				
				if (draw && speed != 0) {
					g2d.setColor(BLUE);
					drawLine(g2d, toScale(botLine));
					g2d.setColor(GREEN);
					drawLine(g2d, toScale(topLine));
					runThread();
					container.paintComponent(g2d);
				}

			}
		}
		if (draw && speed != 0) {
			container.paintComponent(g2d);
			g2d.setColor(GREEN);
			drawLine(g2d, toScale(botLine));
			drawLine(g2d, toScale(topLine));
			runThread();
		}
		
		
//		System.out.print("Tangents are " + inputReal.indexOf(topLine.getStart()) + " -> " + inputReal.indexOf(topLine.getEnd()) + " and ");
//		System.out.println(inputReal.indexOf(botLine.getStart()) + " -> " + inputReal.indexOf(botLine.getEnd()));
		
//		List<Point2D> removed = new ArrayList<Point2D>();
		
		// Removing points that are now inside the new merged convex hull.
		// Remove from left set
		int i = curSetL.indexOf(topLine.getStart())+1;
		if (i >= curSetL.size()) {
			i = 0;
		}
		
		while (!curSetL.get(i).equals(botLine.getEnd())) {
//			System.out.println("Left > " + i + " " + left.size());
			if (i >= curSetL.size()) {
				i = 0;
			}
			
//			removed.add(pntarrScale.get(inputReal.indexOf(curSetL.get(i))));
			left.remove((Integer) inputReal.indexOf(curSetL.remove(i)));
			
			if (i >= curSetL.size()) {
				i = 0;
			}
		}
		
		
		// Remove from right set
		i = curSetR.indexOf(botLine.getStart())+1;
		if (i >= curSetR.size()) {
			i = 0;
		}
		
		while(!curSetR.get(i).equals(topLine.getEnd())) {
//			System.out.println("Right > " + i + " " + right.size());
			if (i >= curSetR.size()) {
				i = 0;
			}
			
//			removed.add(pntarrScale.get(inputReal.indexOf(curSetR.get(i))));
			right.remove((Integer) inputReal.indexOf(curSetR.remove(i)));
			
			if (i >= curSetR.size()) {
				i = 0;
			}
		}
		
		
		linarr.add(new Line2D(pntarrScale.get(inputReal.indexOf(topLine.getStart())), pntarrScale.get(inputReal.indexOf(topLine.getEnd()))));
		linarr.add(new Line2D(pntarrScale.get(inputReal.indexOf(botLine.getStart())), pntarrScale.get(inputReal.indexOf(botLine.getEnd()))));
		
		// Collect points that have remained on the Convex Hull
		List<Integer> output = new ArrayList<Integer>();
		output = addInClockwiseOrder(left, right);
		
		return output;
	}
	
	/**
	 * Helper function for dncHull. Checks if a line is crossing a convex polygon.
	 * @param crosser Line2D representing checking line
	 * @param polygon List<Point2D> representing a convex polygon
	 * @param end int, END if endpoint is being changed, START if startpoint is being changed. If the user uses NEITHER as the value, magnitude checking will not come into effect.
	 * @return true if crosser crosses polygon, false otherwise.
	 */
	private static boolean crosses(Line2D crosser, List<Point2D> polygon, int end) {
		int asserter[] = new int[polygon.size()];
		
		for (int i = 0; i < asserter.length; i++) {
			if (Vector2D.greater(Vector2D.above(crosser, polygon.get(i)), 0)) {
				asserter[i] = 1;
			} else if (Vector2D.equals(Vector2D.above(crosser, polygon.get(i)), 0)) { // If collinear
				if (end == END) { // and the changing point is the end
					if (Vector2D.less(new Vector2D(crosser).getMag(), new Vector2D(crosser.getStart(), polygon.get(i)).getMag())) { // and the magnitude of the new line is greater than the current one
						return true; // Then the line crosses the polygon
					}
				} else if (end == START){ // and the changing point is the start
					if (Vector2D.less(new Vector2D(crosser).getMag(), new Vector2D(polygon.get(i), crosser.getEnd()).getMag())) { // and the magnitude of the new line is grater than the current one
						return true; // Then the line crosses the polygon
					}
				}
				// some of the previous conditions weren't met, so as far as we know the line isn't crossing the polygon
				asserter[i] = 1;
			
			} else {
				asserter[i] = 0;
			}
			
		}

		return !asserting(asserter);
	}
	
	/**
	 * Returns true if all the values in the int array are the same, false otheriwse.
	 * @param asserter int[] containing values
	 * @return true if all the values in the int array are the same, false otheriwse.
	 */
	private static boolean asserting(int[] asserter) {
		int base = asserter[0];
		
		for (int i = 0; i < asserter.length; i++) {
			if (base != asserter[i]) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Helper function that finds an index using logical equality instead of absolute equality
	 */
	private static int indexOf(List<Point2D> array, Point2D target) {
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i).equals(target)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Helper function for dncMerge. Helps with adding points so that each sub convex hull is sorted correctly so as to maintain clockwise order.
	 * @param left List<Integer> Representing the indexes of some points.
	 * @param right List<Integer> Representing the indexes of some points.
	 * @return List<Integer> that combines both inputs into one set all in clockwise order.
	 */
	private static List<Integer> addInClockwiseOrder(List<Integer> left, List<Integer> right) {
		double xS = 0;
		double yS = 0;
		List<Point2D> pntarr = new ArrayList<Point2D>();
		
		for (int i = 0; i < left.size(); i++) {
			xS += pntarrReal.get(left.get(i)).getX();
			yS += pntarrReal.get(left.get(i)).getY();
			pntarr.add(pntarrReal.get(left.get(i)));
		}
		for (int i = 0; i < right.size(); i++) {
			xS += pntarrReal.get(right.get(i)).getX();
			yS += pntarrReal.get(right.get(i)).getY();
			pntarr.add(pntarrReal.get(right.get(i)));
		}
		
		
		Point2D center = new Point2D(xS / (left.size()+right.size()), yS / (left.size()+right.size()));
		
		pntarr = Vector2D.sort(center, pntarr, false);
		List<Integer> output = new ArrayList<Integer>();
		for (int i = 0; i < pntarr.size(); i++) {
			output.add(pntarrReal.indexOf(pntarr.get(i)));
		}
		
		return output;
		
	}

	
	public static List<Integer> chanHull(List<Point2D> input, MyPanel container){
		List<Integer> output = new ArrayList<Integer>();
		// TODO get the convex hull
		return output;
	}

	
	/**
	 * Converts a list of points to their real value
	 * @param in a list of Point2D's assumed to be in scaled value
	 */
	@SuppressWarnings("unused")
	private static List<Point2D> toReal(List<Point2D> in) {
		int size = in.size();
		List<Point2D> out = new ArrayList<Point2D>();
		for (int i = 0; i < size; i++) {
			out.add(new Point2D((in.get(i).getX()-offsetX)/scaleX, (in.get(i).getY()-offsetY)/scaleY));
//			in.get(i).set((in.get(i).getX()-offsetX)/scaleX, (in.get(i).getY()-offsetY)/scaleY);
		}
		
		return out;
	}
	
	
	/**
	 * Converts a list of points to their scaled value
	 * @param in a list of Point2D's assumed to be in real value
	 */
	private static List<Point2D> toScale(List<Point2D> in) {
		int size = in.size();
		List<Point2D> out = new ArrayList<Point2D>();
		for (int i = 0; i < size; i++) {
			out.add(new Point2D(in.get(i).getX()*scaleX+offsetX, Math.floor(in.get(i).getY()*scaleY+offsetY)));
//			in.get(i).set(Math.floor(in.get(i).getX()*scaleX+offsetX), Math.floor(in.get(i).getY()*scaleY+offsetY));
		}
		
		return out;
		
	}
	
	private static List<Line2D> toRealLines(List<Line2D> in) {
		int size = in.size();
		List<Line2D> out = new ArrayList<Line2D>();
		for (int i = 0; i < size; i++) {
			out.add(toReal(in.get(i)));
		}
		return out;
	}
	
	private static List<Line2D> toScaleLines(List<Line2D> in) {
		int size = in.size();
		List<Line2D> out = new ArrayList<Line2D>();
		for (int i = 0; i < size; i++) {
			out.add(toScale(in.get(i)));
		}
		return out;
	}
	
	private static Line2D toReal(Line2D in) {
		return new Line2D(toReal(in.getStart()), toReal(in.getEnd()));
	}
	
	public static Line2D toScale(Line2D in) {
		return new Line2D(toScale(in.getStart()), toScale(in.getEnd()));
	}
	
	private static Point2D toReal(Point2D in) {
		return new Point2D((in.getX()-offsetX)/scaleX, (in.getY()-offsetY)/scaleY);
	}
	
	public static Point2D toScale(Point2D in) {
		return new Point2D((in.getX()*scaleX)+offsetX, (in.getY()*scaleY)+offsetY);
	}
	
	/**
	 * Reads a .csv file and turns it into scaled points to be drawn
	 * @param inputF .csv file to be read
	 * @return a list of scaled points to later be drawn
	 */
	private static List<Point2D> csvRead(File inputF) {
		List<Point2D> retval = new ArrayList<Point2D>();
		
		try {
			Scanner scan = new Scanner(inputF);
			while (scan.hasNextLine()) {
				retval.add(parsePoint(scan.nextLine()));
			}
			scan.close();
		} catch (FileNotFoundException e) {System.out.println("No such file '" + inputF.getName() + "'");}
		toScale(retval);
		return retval;
	}
	
	/**
	 * Given a string representing a Point2D, returns that Point2D
	 * @param str String representing a Point2D, in the format x,y
	 * @return Point2D represented by str
	 */
	private static Point2D parsePoint(String str) {
		String d = ",";
		String a[] = str.split(d);
		return new Point2D(Double.parseDouble(a[0]), Double.parseDouble(a[1]));
	}

	
	private static void scale() {

		double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
		
		for (Point2D i : pntarrReal) {
//			System.out.println(i);
			if (Vector2D.less(i.getX(), minX)) {
				minX = i.getX();
			} 
			if (Vector2D.greater(i.getX(), maxX)){
				maxX = i.getX();
			}
			if (Vector2D.less(i.getY(), minY)) {
				minY = i.getY();
			} 
			if (Vector2D.greater(i.getY(), maxY)) {
				maxY = i.getY();
			}
		}
		
		double xdenom;
		double ydenom;
		double windowOffsetX;
		double windowOffsetY;
		if (maxX == minX) {
			xdenom = 1;
			windowOffsetX = windowWidth/2;
		} else {
			xdenom = maxX-minX;
			windowOffsetX = windowWidth-25;
		}
		if (maxY == minY) {
			ydenom = 1;
			windowOffsetY = windowHeight/2;
		} else {
			ydenom = maxY-minY;
			windowOffsetY = windowHeight-123;
		}
		
		scaleX = (windowWidth-50)/(xdenom);
		scaleY = (windowHeight-188)/(ydenom);
//		System.out.println(scaleX);
//		System.out.println(scaleY);
		
		
		offsetX = (windowOffsetX) - (((windowWidth-50)*maxX) / (xdenom));
		offsetY = (windowOffsetY) - (((windowHeight-188)*maxY) / (ydenom));
//		System.out.println(offsetX);
//		System.out.println(offsetY);
		
	}
	
	public static void drawPoint(Graphics2D g2d, Point2D point) {
		g2d.fillOval((int) point.getX()-5, (int) point.getY()-5, 10, 10);
	}
	
	public static void drawLine(Graphics2D g2d, Line2D line) {
		g2d.drawLine((int) line.getStart().getX(), (int) line.getStart().getY(), (int) line.getEnd().getX(), (int) line.getEnd().getY());
		drawPoint(g2d, line.getStart());
		drawPoint(g2d, line.getEnd());
		drawArrowHead(g2d, line);
	}
	
	private static void drawArrowHead(Graphics2D g2d, Line2D line) {
		AffineTransform tx = new AffineTransform();
		tx.setToIdentity();
	    double angle = Math.atan2(line.getEnd().getY()-line.getStart().getY(), line.getEnd().getX()-line.getStart().getX());
	    tx.translate((line.getStart().getX() + line.getEnd().getX()) / 2, (line.getStart().getY() + line.getEnd().getY())/2);
	    tx.rotate((angle-Math.PI/2d));
	    
	    
		Polygon arrowHead = new Polygon();  
		arrowHead.addPoint( 0,5);
		arrowHead.addPoint( -5, -5);
		arrowHead.addPoint( 5,-5);
		
		Graphics2D g = (Graphics2D) g2d.create();
		g.setTransform(tx);   
	    g.fill(arrowHead);
	    g.dispose();
	}
	
	private static void drawAngle(Graphics2D g2d, Line2D l1, Line2D l2) {
		double s = 30;
		double x = l1.getEnd().getX();
		double y = l1.getEnd().getY();
		
		double relativeX1 = l1.getStart().getX() - x;
		double relativeY1 = -(l1.getStart().getY() - y);
		
		double relativeX2 = -(x - l2.getEnd().getX());
		double relativeY2 = y - l2.getEnd().getY();
		
		double startAngle = Math.toDegrees(Math.atan2(relativeY1, relativeX1));
		double endAngle = Math.toDegrees(Math.atan2(relativeY2, relativeX2));
		
		if (startAngle < 0) {
			startAngle += 360;
		}
		if (endAngle < 0) {
			endAngle += 360;
		}
		
		if (startAngle < endAngle) {
			endAngle -= 360;
		}
		
		double angleExtent = endAngle-startAngle;
		
		g2d.draw(new Arc2D.Double(x-s, y-s, 2*s, 2*s, startAngle, angleExtent, Arc2D.OPEN));
		
	}

}
