package utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PlotUtils {

    /**
     * Visualizza i punti in 2D con colori RGB derivati dalle responsabilità (soft EM).
     * Supporta fino a 3 cluster (r=cluster1, g=cluster2, b=cluster3).
     */
    public static void plot2DSoftClusters(List<double[]> points, double[][] responsibilities) {
        if (points.isEmpty() || points.get(0).length != 2) {
            System.err.println("plot2DSoftClusters: supportato solo per dati 2D.");
            return;
        }

        int n = points.size();
        int k = responsibilities[0].length;
        if (k < 2 || k > 3) {
            System.err.println("Supportati solo 2 o 3 cluster per color mixing RGB.");
            return;
        }

        // Trova min/max per normalizzare coordinate nel canvas
        double minX = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
        for (double[] p : points) {
            minX = Math.min(minX, p[0]);
            maxX = Math.max(maxX, p[0]);
            minY = Math.min(minY, p[1]);
            maxY = Math.max(maxY, p[1]);
        }

        int width = 800, height = 600;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);

        // Disegna i punti con colori miscelati in RGB
        for (int i = 0; i < n; i++) {
            double[] p = points.get(i);
            double r = responsibilities[i][0];
            double g = (k > 1) ? responsibilities[i][1] : 0;
            double b = (k > 2) ? responsibilities[i][2] : 0;

            double norm = Math.max(1e-12, r + g + b);
            r /= norm;
            g /= norm;
            b /= norm;

            Color color = new Color((float) r, (float) g, (float) b);

            int xPix = (int) ((p[0] - minX) / (maxX - minX) * (width - 1));
            int yPix = height - 1 - (int) ((p[1] - minY) / (maxY - minY) * (height - 1));

            g2.setColor(color);
            g2.fillOval(xPix - 3, yPix - 3, 6, 6);
        }

        g2.dispose();

        // Mostra con XChart come immagine
        JLabel label = new JLabel(new ImageIcon(img));
        JFrame frame = new JFrame("Soft EM Clustering (RGB mix)");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
    }
}
