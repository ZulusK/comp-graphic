package com.zulus.lab2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;

@SuppressWarnings("serial")
public class Skeleton extends JPanel implements ActionListener {
    private static int maxWidth;
    private static int maxHeight;
    private double scale = 1;
    private int scaleDirection = 1;
    private double scaleStep = 0.01;
    private double transparency = 0;
    private double transparencyStep = 0.01;
    private int transparencyDirection = 1;
    private Timer timer;
    private int[][] bigPolygonCoords = {{-180, -150}, {-20, 0}, {-180, 150}, {-140, 0}};
    private int[][] smallPolygonCoords = {{150, -50}, {50, 0}, {150, 50}, {120, 0}};
    private int[] circleCenter = {-90, 0};
    private int[][] stringCenters = {{-90, 0}, {100, 0}};
    private int circleRadius = 60;
    private int stringsCount = 7;
    private double stringSpace = 0.01;

    public Skeleton() {
        timer = new Timer(10, this);
        timer.start();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hello, lab2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(new Skeleton());
        frame.setVisible(true);
        Dimension size = frame.getSize();
        Insets insets = frame.getInsets();
        maxWidth = size.width - insets.left - insets.right - 1;
        maxHeight = size.height - insets.top - insets.bottom - 1;
    }

    private GeneralPath createPolygon(int[][] points) {
        GeneralPath poly = new GeneralPath();
        poly.moveTo(points[0][0], points[0][1]);
        for (var point : points) {
            poly.lineTo(point[0], point[1]);
        }
        poly.closePath();
        return poly;
    }

    private void drawGuitar(Graphics2D g2d) {
        GradientPaint gp = new GradientPaint(
                0, 0, new Color(255, 50, 0, (int) (transparency * 255)),
                50, 50, new Color(0, 0, 255, (int) (transparency * 255)),
                true);
        g2d.setPaint(gp);
        // big polygon
        g2d.translate(maxWidth / 2, maxHeight / 2);
        g2d.scale(scale, scale);
        g2d.fill(createPolygon(bigPolygonCoords));
        // small polygon
        g2d.setColor(new Color(0, 0, 255, (int) (255 * this.transparency)));
        g2d.fill(createPolygon(smallPolygonCoords));
        // circle
        g2d.setColor(new Color(255, 0, 0, (int) (255 * this.transparency)));
        g2d.fillOval(circleCenter[0] - circleRadius / 2, circleCenter[1] - circleRadius / 2, circleRadius, circleRadius);
        // lines
        g2d.setColor(new Color(0, 0, 0, (int) (255 * this.transparency)));
        int stringsStartX = stringCenters[0][0];
        int stringsStartY = (int) (this.stringCenters[0][1] - maxWidth * stringSpace * stringsCount / 2);
        int stringsEndX = this.stringCenters[1][0];
        int stringsEndY = (int) (this.stringCenters[1][1] - maxWidth * stringSpace * stringsCount / 2);
        BasicStroke basicStroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        g2d.setStroke(basicStroke);
        for (int i = 0; i < this.stringsCount; i++) {
            g2d.drawLine(stringsStartX, (int) (stringsStartY + maxHeight * stringSpace * i), stringsEndX, (int) (stringsEndY + maxHeight * stringSpace * i));
        }
    }

    private void drawBorder(Graphics2D g2d) {
        BasicStroke basicStroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        g2d.setStroke(basicStroke);
        g2d.drawRect(5, 5, maxWidth - 10, maxHeight - 10);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
        g2d.setBackground(Color.yellow);
        g2d.clearRect(0, 0, maxWidth, maxHeight);
        this.drawBorder(g2d);
        this.drawGuitar(g2d);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.transparency += this.transparencyStep * this.transparencyDirection;

        if (this.transparency > 1) {
            this.transparency = 1;
            this.transparencyDirection = -1;
        } else if (this.transparency < 0) {
            this.transparency = 0;
            this.transparencyDirection = 1;
        }

        this.scale += this.scaleStep * this.scaleDirection;
        if (this.scale > 1) {
            this.scale = 1;
            this.scaleDirection = -1;
        } else if (this.scale < 0.2) {
            this.scale = 0.2;
            this.scaleDirection = 1;
        }

        repaint();
    }
}
