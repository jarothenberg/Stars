package stars;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Stars extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; // WARNING: manually adjusting size of JPanel results in every star
														// changing color randomly
	private static int scale = 4; // changes scale of the star's size
	private static int sidesNum = 20; // number of the sides of the polygon the star is inscribed in.
										// WARNING: entering a large number (over 1000) may result in the program
										// lagging or failing due to the computer running low on memory.
										// Optimal number range: [5, 6) U (6, 100] 5 to 100 inclusive, not including 6

	int slideNum;
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Polygon p = new Polygon();
		int[] x = new int[sidesNum];
		int[] y = new int[sidesNum];
		for (int i = 0; i < sidesNum; i++) {
			x[i] = (int) (50 * scale + 50 * Math.cos(i * 2 * Math.PI / sidesNum) * scale);
			y[i] = (int) (50 * scale + 50 * Math.sin(i * 2 * Math.PI / sidesNum) * scale);
		}
		for (int i = 0; i < sidesNum; i++) {
			p.addPoint(x[i], y[i]);
		}
		int count = 0;
		for (int i = 2; i < sidesNum; i++) {
			if (sidesNum % i != 0 && i * 2 < sidesNum) {
				count++;
			}
		}
		int[] possible = new int[count];
		count = 0;
		for (int i = 2; i < sidesNum; i++) {
			if (sidesNum % i != 0 && i * 2 < sidesNum) {
				possible[count] = i;
				count++;
			}
		}
		JSlider slide = new JSlider();
		if (JFrame.getFrames().length == 1) {
			String title = "STARS";
			int width = 100;
			int height = 100;
			JFrame frame = new JFrame();
			Object[] rtrn = new Object[2];
			frame.setTitle(title);
			frame.setSize(width * scale, height * scale + 100);
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			slide.setLocation(height * scale / 10, height * scale);
			slide.setSize(400, 100);

			count = 0;
			for (int i = 2; i < sidesNum; i++) {
				if (sidesNum % i != 0 && i * 2 < sidesNum) {
					count++;
				}
			}

			slide.setMaximum(count);
			slide.setMinimum(1);
			slide.setValue(0);
			
			slide.addChangeListener(new ChangeListener(){
			          public void stateChanged(ChangeEvent e) {
			        	  rtrn[1] = slide.getValue();
			        	  slideNum = slide.getValue();//not updating outside of here
			        	  System.out.println("1: " + slideNum);
			          }
			    });
			frame.add(slide);
			Container contentPane = frame.getContentPane();
			contentPane.add(new Stars());
			frame.setVisible(true);
			rtrn[0] = frame;
		}
		System.out.println("2: " + this.slideNum);
		for (int i = 0; i < possible.length; i++) {
			Random rand = new Random();
			float red = rand.nextFloat();
			float green = rand.nextFloat();
			float blue = rand.nextFloat();
			Color rnd = new Color(red, green, blue);
			int numSkip = possible[slideNum]; //change i to whatever star is being selected
			//int numSkip = possible[i];
			for (int j = 0; j < sidesNum; j += numSkip) {
				
				g.setColor(rnd);
				if (j + numSkip > sidesNum) {
					g.drawLine(x[j], y[j], x[j + numSkip - sidesNum], y[j + numSkip - sidesNum]);
					j = j + numSkip - sidesNum;
				} else if (j + numSkip == sidesNum) {
					g.drawLine(x[j], y[j], x[0], y[0]);
					break;
				}
				g.drawLine(x[j], y[j], x[j + numSkip], y[j + numSkip]);
			}
		}
		g.setColor(Color.BLACK);
		g.drawPolygon(p);
	}

	public static void createFrame(String title, int width, int height) {
		JFrame frame = new JFrame();
		Container contentPane = frame.getContentPane();
		contentPane.add(new Stars());
		frame.setVisible(true);
		frame.setSize(0,0);
	}

	public static void main(String[] args) {
		createFrame("STARS", 100, 100);
	}
}