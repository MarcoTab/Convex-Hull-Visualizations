package convexhull;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.*;  

@SuppressWarnings("unused")
public class Tester {
	
	public static List<Point2D> pointMaker(int n) {
		List<Point2D> l = new ArrayList<Point2D>();
		Random rd = new Random();
		for (int i = 0; i < n; i++) {
			l.add(new Point2D(rd.nextFloat()*n, rd.nextFloat()*n));
		}
		return l;
	}
	
	public static void main(String args[]) {  
		Scanner scanner = new Scanner(System.in);
		System.out.print("Input size > ");
		int n = scanner.nextInt();
		System.out.print("Reps > ");
		int reps = scanner.nextInt();
		System.out.print("What algorithm? <s, g, q, j, i, d> ");
		char alg = scanner.next().charAt(0);
		scanner.close();
		
		long [] times = new long[reps];
		
		for (int i = 0; i < reps; i++) {
			System.out.println("Round " + (i+1) + " of " + reps);
			List<Point2D> l = pointMaker(n);
			
			long time = 0;
			
			if (alg == 's') {
				time = System.currentTimeMillis();
				try{MyPanel.slowHull(l, null);} catch (Exception e) {e.printStackTrace();}
				time = System.currentTimeMillis() - time;
				
			} else if (alg == 'g') {
				time = System.currentTimeMillis();
				try{MyPanel.grahamHull(l, null);} catch (Exception e) {e.printStackTrace();}
				time = System.currentTimeMillis() - time;

			} else if (alg == 'q') {
				time = System.currentTimeMillis();
				try{MyPanel.quickHull(l, null);} catch (Exception e) {e.printStackTrace();}
				time = System.currentTimeMillis() - time;

			} else if (alg == 'i') {
				time = System.currentTimeMillis();
				try{MyPanel.incrementalHull(l, null);} catch (Exception e) {e.printStackTrace();}
				time = System.currentTimeMillis() - time;

			} else if (alg == 'j') {
				time = System.currentTimeMillis();
				try{MyPanel.jarvisHull(l, null);} catch (Exception e) {e.printStackTrace();}
				time = System.currentTimeMillis() - time;

			} else if (alg == 'd') {
				time = System.currentTimeMillis();
				try{MyPanel.dncHull(l, null);} catch (Exception e) {e.printStackTrace();}
				time = System.currentTimeMillis() - time;
			}
			
			System.out.println("> " + time + "ms");
			times[i] = time;
		}
		float sum = 0;
		
		for (long num : times) {
			sum += num;
		}
		
		sum /= reps;
		FileWriter f = null;
		try{
		f = new FileWriter("time.dat");
		} catch(IOException e) {e.printStackTrace();};
		if (f != null)
			try {
				f.write("Performed " + reps + " multijoins on sets of size " + n + " w/ an average time of " + sum + "ms");
				f.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		
	}   
     
        
}  