package convexhull;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;

public class MyFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public MyFrame() {
		this.setTitle("Convex Hull Algorithms");
		this.setLayout(null);
		this.setSize(1000, 1029);
		MyPanel panel = new MyPanel(this.getWidth(), this.getHeight()-29);
		this.getContentPane().add(panel);
//		this.setSize(panel.getWidth(), panel.getHeight()+29);;
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentResized(ComponentEvent e) {
	            // TODO handle the change
	        	panel.setSize(e.getComponent().getWidth(), e.getComponent().getHeight()-29);
	        	panel.repaint();
	        }
	    });
	}
	
	
	
}
	


