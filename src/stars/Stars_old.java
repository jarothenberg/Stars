package stars;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Stars_old extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; // WARNING: manually adjusting size of JPanel results in every star
														// changing color randomly
	private static int scale = 15; // changes scale of the star's size
	private static int sidesNum = 100; // number of the sides of the polygon the star is inscribed in.
										// WARNING: entering a large number (over 1000) may result in the program
										// lagging or failing due to the computer running low on memory.
										// Optimal number range: [5, 6) U (6, 100] 5 to 100 inclusive, not including 6

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
		for (int i = 0; i < possible.length; i++) {
			Random rand = new Random();
			float red = rand.nextFloat();
			float green = rand.nextFloat();
			float blue = rand.nextFloat();
			Color rnd = new Color(red, green, blue);
			int numSkip = possible[i];
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

	public static JFrame createFrame(String title, int width, int height) {
		JFrame frame = new JFrame();
		frame.setTitle(title);
		frame.setSize(width * scale, height * scale);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		Container contentPane = frame.getContentPane();
		contentPane.add(new Stars_old());
		frame.setVisible(true);
		return frame;
	}

	public static void main(String[] args) {
		createFrame("STARS", 100, 100);
	}
}