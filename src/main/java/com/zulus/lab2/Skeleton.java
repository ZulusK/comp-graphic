package com.zulus.lab2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.List;

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
    private double[][] bigPolygonCoords = {{0, 0.1}, {0.1, 0.5}, {0, 0.9}, {0.4, 0.5}};
    private double[][] smallPolygonCoords = {{1, 0.4}, {0.85, 0.5}, {1, 0.6}, {0.95, 0.5}};
    private double[] circleCenter = {0.25, 0.5};
    private double circleWidth = 0.15;
    private double circleHeigth = 0.15;
    private double[][] stringCenters = {{0.23, 0.5}, {0.93, 0.5}};
    private int stringsCount = 7;
    private double stringSpace = 0.01;
    private double dax0;
    private double dax1;
    private double day0;
    private double day1;
    private double daw;
    private double dah;

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

    private GeneralPath createPolygon(List<double[]> points) {
        GeneralPath poly = new GeneralPath();
        poly.moveTo(points.get(0)[0], points.get(0)[1]);
        for (var point : points) {
            poly.lineTo(point[0], point[1]);
        }
        poly.closePath();
        return poly;
    }

    List<double[]> processPolyCoords(double[][] initialCoords) {
        return Arrays
                .stream(initialCoords)
                .map((coords) -> new double[]{
                        dax0 + daw * coords[0],
                        day0 + dah * coords[1],

                })
                .collect(Collectors.toList());
    }


    private void drawGuitar(Graphics2D g2d) {
        GradientPaint gp = new GradientPaint(
                0, 0, new Color(255, 50, 0, (int) (transparency * 255)),
                (int) (50 * scale), (int) (50 * scale), new Color(0, 0, 255, (int) (transparency * 255)),
                true);
        g2d.setPaint(gp);
        var smallScaledPolyCoors = processPolyCoords(this.smallPolygonCoords);
        var bigScaledPolyCoors = processPolyCoords(this.bigPolygonCoords);
        g2d.fill(createPolygon(smallScaledPolyCoors));
        g2d.fill(createPolygon(bigScaledPolyCoors));
        g2d.setColor(new Color(255, 0, 0, (int) (255 * this.transparency)));
        g2d.fillOval(
                (int) (dax0 + daw * circleCenter[0] - (circleWidth * daw) / 2),
                (int) (day0 + dah * circleCenter[1] - (circleHeigth * dah) / 2),
                (int) (circleWidth * daw),
                (int) (circleHeigth * dah)
        );
        g2d.setColor(new Color(0, 0, 0, (int) (255 * this.transparency)));
        int stringsStartX = (int) (dax0 + daw * this.stringCenters[0][0]);
        int stringsStartY = (int) (day0 + dah * this.stringCenters[0][1] - dah * stringSpace * stringsCount / 2);
        int stringsEndX = (int) (dax0 + daw * this.stringCenters[1][0]);
        int stringsEndY = (int) (day0 + dah * this.stringCenters[1][1] - dah * stringSpace * stringsCount / 2);
        for (int i = 0; i < this.stringsCount; i++) {
            g2d.drawLine(stringsStartX, (int) (stringsStartY + dah * stringSpace * i), stringsEndX, (int) (stringsEndY + dah * stringSpace * i));
        }
    }

    private void drawBorder(Graphics2D g2d) {
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

    private void recalculateDrawArea() {
        dax0 = 20 + maxHeight / 2 * (1 - scale);
        dax1 = maxWidth - dax0;
        day0 = 20 + maxHeight / 2 * (1 - scale);
        day1 = maxHeight - day0;
        daw = dax1 - dax0;
        dah = day1 - day0;
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
        recalculateDrawArea();

        repaint();
    }
}
