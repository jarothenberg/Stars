package stars;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Stars_new extends JPanel {
    private static final long serialVersionUID = 1L;
    private int sidesNum = 100;
    private boolean drawAllStars = false;
    private int slideNum;
    private JSlider slide;
    private JRadioButton button;
    private JTextField sidesField;
    private JButton updateButton;
    private double scale;

    public Stars_new() {
        String title = "STARS";
        JFrame frame = new JFrame();
        frame.setTitle(title);
        frame.setSize(800, 600); // Initial size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        createSlider();
        createButton();
        createSidesField();
        createUpdateButton();

        // Create a panel for the slider, button, and sidesField with FlowLayout
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new FlowLayout());
        controlsPanel.add(slide);
        controlsPanel.add(button);
        controlsPanel.add(sidesField);
        controlsPanel.add(updateButton);

        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        frame.add(controlsPanel, BorderLayout.SOUTH);

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                updateGeometry();
            }
        });

        frame.setVisible(true);
        updateGeometry(); // Call updateGeometry to set the initial scale
    }

    private void createUpdateButton() {
        updateButton = new JButton("Update Sides");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateSides();
            }
        });
    }

    private void createButton() {
        button = new JRadioButton("Draw All Stars", drawAllStars);
        button.setRolloverEnabled(false); // Disable rollover effect
        button.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                drawAllStars = button.isSelected();
                slide.setEnabled(!drawAllStars); // Enable/disable slider based on radio button selection
                repaint();
            }
        });
    }

    private void createSlider() {
        int count = 0;
        for (int i = 2; i < sidesNum; i++) {
            if (sidesNum % i != 0 && i * 2 < sidesNum) {
                count++;
            }
        }

        slide = new JSlider();
        int maxSkipIndex = count - 1; // Maximum index for possible skips
        slide.setMaximum(maxSkipIndex);
        slide.setMinimum(0);
        slide.setValue(0);

        slide.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                slideNum = slide.getValue();
                repaint();
            }
        });
    }

    private void createSidesField() {
        sidesField = new JTextField(5);
        sidesField.setText(Integer.toString(sidesNum));
    }

    private void updateSides() {
        try {
            int newSidesNum = Integer.parseInt(sidesField.getText());
            if (newSidesNum > 0) {
                sidesNum = newSidesNum;
                slide.setMaximum(calculateMaxSkipIndex());
                slide.setValue(0);
                repaint();
            }
        } catch (NumberFormatException ex) {
            // Handle invalid input
        }
    }

    private int calculateMaxSkipIndex() {
        int count = 0;
        for (int i = 2; i < sidesNum; i++) {
            if (sidesNum % i != 0 && i * 2 < sidesNum) {
                count++;
            }
        }
        return count - 1;
    }

    private void updateGeometry() {
        // Recalculate the scale factor based on the new dimensions
        scale = Math.min((double) getWidth() / 800, (double) getHeight() / 600)*5;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Polygon p = new Polygon();
        int[] x = new int[sidesNum];
        int[] y = new int[sidesNum];

        for (int i = 0; i < sidesNum; i++) {
            x[i] = (int) (getWidth() / 2 + 50 * scale * Math.cos(i * 2 * Math.PI / sidesNum));
            y[i] = (int) (getHeight() / 2 + 50 * scale * Math.sin(i * 2 * Math.PI / sidesNum));
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

        if (slideNum >= 0 && slideNum < possible.length) {
            if (drawAllStars == true) {
                for (int k = 0; k < possible.length; k++) {
                    g.setColor(getRandomColor());
                    int numSkip = possible[k];
                    for (int i = 0; i < sidesNum; i++) {
                        int j = (i + numSkip) % sidesNum;
                        g.drawLine(x[i], y[i], x[j], y[j]);
                    }
                }
            } else {
                g.setColor(getRandomColor());
                int numSkip = possible[slideNum];
                for (int i = 0; i < sidesNum; i++) {
                    int j = (i + numSkip) % sidesNum;
                    g.drawLine(x[i], y[i], x[j], y[j]);
                }
            }
        }

        g.setColor(Color.BLACK);
        g.drawPolygon(p);
    }

    private Color getRandomColor() {
        return new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Stars_new();
        });
    }
}
