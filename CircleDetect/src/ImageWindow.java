import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class ImageWindow extends JFrame {


    private File file;
    private BufferedImage image;
    private JLabel label;

    private int[][] sobelMatrix;


    public ImageWindow(File file) throws IOException {

        this.file = file;
        image = ImageIO.read(file);

        sobelMatrix = new int[image.getWidth()][image.getHeight()];



        label = new JLabel(new ImageIcon(image));
        convertToGreyscale();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(label);
        this.pack();
        this.setVisible(true);



    }


    /**
     * Method that converts the original image to a more dynamic greyscale
     */
    public void convertToGreyscale() {
        try {

            BufferedImage image;
            int width;
            int height;


            image = ImageIO.read(file);
            width = image.getWidth();
            height = image.getHeight();

            for (int i = 0; i < height; i++) {

                for (int j = 0; j < width; j++) {

                    Color c = new Color(image.getRGB(j, i));
                    int red = (int) (c.getRed() * 0.2126);
                    int green = (int) (c.getGreen() * 0.7152);
                    int blue = (int) (c.getBlue() * 0.0722);

                    Color newColor = new Color(red + green + blue,

                            red + green + blue, red + green + blue);

                    image.setRGB(j, i, newColor.getRGB());
                }
            }

            File output = new File("grayscale.jpg");
            ImageIO.write(image, "jpg", output);
            Sobel(output);
            circleDetection(image);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Applying the sobelOperator to a given picture
     *
     * @param f
     * @throws IOException
     */
    public void Sobel(File f) throws IOException {

        BufferedImage image;
        int width;
        int height;

        image = ImageIO.read(f);
        width = image.getWidth();
        height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);

        for (int i = 1; i < width - 1; i++) {

            for (int j = 1; j < height - 1; j++) {

                int val00 = new Color(image.getRGB(i - 1, j - 1)).getRed();
                int val01 = new Color(image.getRGB(i - 1, j)).getRed();
                int val02 = new Color(image.getRGB(i - 1, j + 1)).getRed();

                int val10 = new Color(image.getRGB(i, j - 1)).getRed();
                int val11 = new Color(image.getRGB(i, j)).getRed();
                int val12 = new Color(image.getRGB(i, j + 1)).getRed();

                int val20 = new Color(image.getRGB(i + 1, j - 1)).getRed();
                int val21 = new Color(image.getRGB(i + 1, j)).getRed();
                int val22 = new Color(image.getRGB(i + 1, j + 1)).getRed();


                int gx = ((-1 * val00) + (0 * val01) + (1 * val02))
                        + ((-2 * val10) + (0 * val11) + (2 * val12))
                        + ((-1 * val20) + (0 * val21) + (1 * val22));

                int gy = ((-1 * val00) + (-2 * val01) + (-1 * val02))
                        + ((0 * val10) + (0 * val11) + (0 * val12))
                        + ((1 * val20) + (2 * val21) + (1 * val22));

                double gval = Math.sqrt((gx * gx) + (gy * gy));


                int edge = (int) gval;


                if (edge > 200) {
                    result.setRGB(i, j, (edge << 16 | edge << 8 | edge));
                    sobelMatrix[i][j] = edge;
                }
            }
        }
        File outputfile = new File("sobel.png");
        ImageIO.write(result, "png", outputfile);


    }

    private void circleDetection(BufferedImage image) throws Exception {
        //sets the radius relative to 1/6 of the smallest side of the image, helps reduce space taken in memory during
        //runtime
        int radius;
        if (image.getHeight() < image.getWidth()) {
            radius = image.getHeight() / 5;
        } else {
            radius = image.getWidth() / 5;
        }
        //sets a 3D space array of ints to hold 'hits' in x, y, and r planes
        int[][][] A = new int[image.getWidth()][image.getHeight()][radius];

        BufferedImage newImage = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        Graphics g=newImage.getGraphics();
        g.drawImage(image,0,0,null);

        for (int rad = 0; rad < radius; rad++) {
            for (int x = 0; x < newImage.getWidth(); x++) {
                for (int y = 0; y < newImage.getHeight(); y++) {
                    //if the given pixel is above the threshold, a circle will be drawn at radius rad around it and if it
                    //is a valid coordinate it will be accumulated in the A array and plotted in the pointSpace image
                    if (sobelMatrix[x][y]>10) {
                        for (int t = 0; t <= 360; t++) {
                            Integer a = (int) Math.floor(x - rad * Math.cos(t * Math.PI / 180));
                            Integer b = (int) Math.floor(y - rad * Math.sin(t * Math.PI / 180));
                            if (!((0 > a || a > newImage.getWidth() - 1) || (0 > b || b > newImage.getHeight() - 1))) {

                                if (!(a.equals(x) && b.equals(y))) {
                                    A[a][b][rad] += 1;
                                    //newImage.setRGB(a, b, new Color(rad,rad,rad).getRGB());
                                }
                            }
                        }
                    }
                }
            }
        }
        g.setColor(Color.RED);
        int maxX=0;
        int maxY=0;
        //iterates to find the max value in the A array
        for (int rad = 0; rad < radius; rad++) {
            int max=0;
            for (int x = 0; x < newImage.getWidth(); x++) {
                for (int y = 0; y < newImage.getHeight(); y++) {
                    if(max<A[x][y][rad]){
                        max=A[x][y][rad];
                        maxX=x;
                        maxY=y;
                    }
                }
            }
            if(max>0.5*(int)Math.ceil(3.14*2*rad)) {

                g.drawOval(maxX - rad, maxY - rad, rad * 2, rad * 2);
            }
        }

        g.dispose();
        File newfile = new File("pointSpace.png");
        ImageIO.write(newImage, "png", newfile);

        label = new JLabel(new ImageIcon(newfile.getAbsolutePath()));

    }

}

