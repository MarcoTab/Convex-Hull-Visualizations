package convexhull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;  

@SuppressWarnings("unused")
public class Tester {  
	private JFrame f;
	private ThisPanel panel;
	private int size = 500;
	
	Tester() {  
		f = new JFrame("Panel Example");    
        panel=new ThisPanel();  
        panel.setSize(size, size);
        panel.setBackground(Color.WHITE); 
        f.setSize(size,size+27);
        f.setLayout(new GridBagLayout());  
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 5;
        c.gridheight = 40;
        f.add(panel, c); 
        f.setVisible(true);
    }  
    
	
	
	public static void main(String args[]) {  
		new Tester();
		Line2D line = new Line2D(180.0, 53.2, 14.9, 24.3);
		System.out.println(MyPanel.toScale(line));
		System.out.println(MyPanel.toScale(line.getStart()) + " -> " + MyPanel.toScale(line.getEnd()));
	}   
     
    class ThisPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g) {
    		super.paintComponent(g);
    	}
    	
    	
    }
        
}  