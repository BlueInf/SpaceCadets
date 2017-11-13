import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.awt.image.BufferedImage;


public class ImageLoader {
    private JFrame frame;
    private String nameImage = "";
    private ImageIcon imageIcon;
    private JLabel labelPicture;

    private float[][] gX;
    private float[][] gY;
    private float[][] gXY;

    private int houghMatrix[][];


    /**
     * Constructor that Loads the Image and Loads the Frame
     *
     * @param nameImage
     */
    public ImageLoader(String nameImage) {

        this.nameImage = nameImage;

        frame = new JFrame();
        frame.setTitle("Circle Detection");
        imageIcon = new ImageIcon(getClass().getResource(nameImage));
        labelPicture = new JLabel(imageIcon);
        frame.getContentPane().add(labelPicture);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    //Mutator method
    public void setNameImage(String nameImage) {
        this.nameImage = nameImage;
    }

    //Accessor method
    public String getNameImage() {
        return nameImage;
    }


    /**
     * Method that converts the original image to a more dynamic greyscale
     * We multiply red by 0.299 , geen by 0.587 and blue by 0.114 to get a grayscale that is more accurate
     * for the human eye
     */
    public void convertToGreyscale() {
        try {

            BufferedImage image;
            int width;
            int height;


            image = ImageIO.read(getClass().getResource(nameImage));
            width = image.getWidth();
            height = image.getHeight();

            for (int i = 0; i < height; i++) {

                for (int j = 0; j < width; j++) {

                    Color c = new Color(image.getRGB(j, i));
                    int red = (int) (c.getRed() * 0.299);
                    int green = (int) (c.getGreen() * 0.587);
                    int blue = (int) (c.getBlue() * 0.114);

                    Color newColor = new Color(red + green + blue,

                            red + green + blue, red + green + blue);

                    image.setRGB(j, i, newColor.getRGB());
                }
            }

            File output = new File("grayscale.jpg");
            ImageIO.write(image, "jpg", output);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Another variaton of the sobel operator algorithm
     *
     * @throws Exception
     */
    public void sobelOperatorTwo() throws Exception {

        BufferedImage originalImage = ImageIO.read(getClass().getResource("grayscale.jpg"));

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage sobelXImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        BufferedImage sobelYImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        BufferedImage sobelXYImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);


        for (int x = 1; x < width - 1; x++) {

            for (int y = 1; y < height - 1; y++) {

                float sumX = 0;
                float sumY = 0;
                float sumXY = 0;

                Color topLeft = new Color(originalImage.getRGB(x - 1, y - 1));
                Color middleLeft = new Color(originalImage.getRGB(x - 1, y));
                Color bottomLeft = new Color(originalImage.getRGB(x - 1, y + 1));

                Color topRight = new Color(originalImage.getRGB(x + 1, y - 1));
                Color middleRight = new Color(originalImage.getRGB(x + 1, y));
                Color bottomRight = new Color(originalImage.getRGB(x + 1, y + 1));

                Color topMiddle = new Color(originalImage.getRGB(x, y - 1));
                Color bottomMiddle = new Color(originalImage.getRGB(x, y + 1));

                sumX += topLeft.getRed() * (-1) + middleLeft.getRed() * (-2) + bottomLeft.getRed() * (-1);
                sumX += topRight.getRed() + middleRight.getRed() * (2) + bottomRight.getRed();

                sumY += topLeft.getRed() * (-1) + topMiddle.getRed() * (-2) + topRight.getRed() * (-1);
                sumY += bottomLeft.getRed() + bottomMiddle.getRed() * 2 + bottomRight.getRed();

                sumXY = (int) Math.round(Math.sqrt(sumX * sumX + sumY * sumY));

                sobelXImage.setRGB(x, y, 0xff000000 | (int) sumX << 16 | (int) sumX << 8 | (int) sumX);
                sobelYImage.setRGB(x, y, 0xff000000 | (int) sumY << 16 | (int) sumY << 8 | (int) sumY);

                if (sumXY > 20) {
                    sobelXYImage.setRGB(x, y, 0xff000000 | (int) sumXY << 16 | (int) sumXY << 8 | (int) sumXY);
                    System.out.println(sumXY + " ");
                } else {
                    sumXY = 0;
                    sobelXYImage.setRGB(x, y, 0xff000000 | (int) sumXY << 16 | (int) sumXY << 8 | (int) sumXY);
                }


            }
            System.out.println();
        }


        for (int x = 1; x < width - 1; x++) {

            for (int y = 1; y < height - 1; y++) {


            }
        }
        File sobelX = new File("sobelX.jpg");
        ImageIO.write(sobelXImage, "jpg", sobelX);

        File sobelY = new File("sobelY.jpg");
        ImageIO.write(sobelYImage, "jpg", sobelY);

        File sobelXY = new File("sobelXY.jpg");
        ImageIO.write(sobelXYImage, "jpg", sobelXY);

    }


    /**
     * Find circles method- finds all the circles in the image
     *
     * @throws Exception
     */
    public void findCircles() throws Exception {

        BufferedImage edgeDetectedImage = ImageIO.read(getClass().getResource("sobelXYoperator.jpg"));
        BufferedImage outputImage = ImageIO.read(getClass().getResource(nameImage));

        int width = edgeDetectedImage.getWidth();
        int height = edgeDetectedImage.getHeight();

        int max = 0;
        int maxr = 0;


        BufferedImage testImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = testImage.createGraphics();
        graphics.setPaint(Color.black);
        graphics.fillRect(0, 0, width, height);

        graphics.setPaint(Color.WHITE);

        int r;
        int maxRadius = (int) Math.sqrt(height * height + width * width) - 2;


        houghMatrix = new int[width + 2][height + 2];

       // for (r = 100; r < maxRadius - 2; r++) {

            for (int x = 0; x < width; x++) {

                for (int y = 0; y < height; y++) {


                    Color c = new Color(edgeDetectedImage.getRGB(x, y));

                    if (c.getRed() == 255) {
                        // System.out.println("x= "+x);
                        // System.out.println("y= "+y);

                        for (double i = 0; i < 360; i++) {

                            int x1 = (int) Math.round(x +110 * Math.cos(i));
                            int y1 = (int) Math.round(y +110 * Math.sin(i));

                            if (x1 >= 0 && y1 >= 0 && x1 <= width && y1 <= height) {

                                //graphics.drawOval(x1, y1, 1, 1);
                                //System.out.println("x1 is " + x1);
                                //System.out.println("y1 is " + y1);
                                //System.out.println("R i s " + r);
                                houghMatrix[x1][y1]++;
                                if (max < houghMatrix[x1][y1]) {

                                    max = houghMatrix[x1][y1];

                                }

                            }
                        }
                    }

                }

            }
        //}

        File resultFile = new File("edgeDetectedPicture.jpg");
        Graphics2D grr = outputImage.createGraphics();
        grr.setPaint(Color.RED);

     //   for (r = 100; r < maxRadius; r++) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (houghMatrix[i][j] == max) {
                        grr.drawOval(i, j, 110, 110);

                    }
            //    }
            }
        }
        //System.out.println("it is finish");
        ImageIO.write(outputImage, "jpg", resultFile);

        File newFile = new File("houghTransformedImage.jpg");
        ImageIO.write(testImage, "jpg", newFile);
    }


    public void sobelOperator() throws Exception {


        BufferedImage inputImage = ImageIO.read(getClass().getResource("grayscale.jpg"));


        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        gXY = new float[width][height];

        sobelX();
        sobelY();

        float max = 0;

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {

                gXY[x][y] = Math.round(Math.sqrt((double) gX[x][y] * (double) gX[x][y] + (double) gY[x][y] * (double) gY[x][y]));
                if (max < gXY[x][y]) max = gXY[x][y];

            }
        }

        float ratio = (float) (0xff & (int) max) / 255;
        System.out.println("Ratio is " + ratio);
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                float sum = gXY[x][y];
                //float sum1=((0xff & (int)gXY[x][y])/ratio);

                //System.out.println(0xff& (int) sum);

                //outputImage.setRGB(x, y, 0xff000000 | ((int) sum1 << 16 | (int) sum1 << 8 | (int) sum1));

                /*
                if ((0xFF & (int) sum) < 150) {
                    outputImage.setRGB(x, y, 0);
                } else outputImage.setRGB(x, y, 0xff000000 | ((int) sum << 16 | (int) sum << 8 | (int) sum));
                */

                if ((0xff & (int) sum) > 160) {
                    Color c = new Color(255, 255, 255);
                    outputImage.setRGB(x, y, c.getRGB());
                } else outputImage.setRGB(x, y, new Color(0, 0, 0).getRGB());


                // outputImage.setRGB(x, y, 0xff000000 | ((int) sum1 << 16 | (int) sum1 << 8 | (int) sum1));

            }
        }
        File resultImage = new File("sobelXYoperator.jpg");
        ImageIO.write(outputImage, "jpg", resultImage);


    }


    public void sobelX() throws Exception {
        BufferedImage inputImage = ImageIO.read(getClass().getResource("grayscale.jpg"));

        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        gX = new float[width][height];

        float[][] kernelX = new float[][]{
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {

                float sum = 0;

                Color topLeft = new Color(inputImage.getRGB(x - 1, y - 1));
                Color middleLeft = new Color(inputImage.getRGB(x - 1, y));
                Color bottomLeft = new Color(inputImage.getRGB(x - 1, y + 1));

                Color topRight = new Color(inputImage.getRGB(x + 1, y - 1));
                Color middleRight = new Color(inputImage.getRGB(x + 1, y));
                Color bottomRight = new Color(inputImage.getRGB(x + 1, y + 1));


                sum += topLeft.getRed() * kernelX[0][0] + middleLeft.getRed() * kernelX[1][0] + bottomLeft.getRed() * kernelX[2][0];
                sum += topRight.getRed() * kernelX[0][2] + middleRight.getRed() * kernelX[1][2] + bottomRight.getRed() * kernelX[2][2];

                gX[x][y] = sum;
                outputImage.setRGB(x, y, 0xff000000 | (int) gX[x][y] << 16 | (int) gX[x][y] << 8 | (int) gX[x][y]);
                //outputImage.setRGB(x, y, (int)sum);

            }
        }

        File resultFile = new File("secondSobelX.jpg");
        ImageIO.write(outputImage, "jpg", resultFile);

    }

    public void sobelY() throws Exception {
        BufferedImage inputImage = ImageIO.read(getClass().getResource("grayscale.jpg"));

        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        gY = new float[width][height];

        float[][] kernelY = new float[][]{
                {-1, -2, -1},
                {0, 0, 0},
                {1, 2, 1}
        };

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {

                float sum = 0;

                Color topLeft = new Color(inputImage.getRGB(x - 1, y - 1));
                Color middleTop = new Color(inputImage.getRGB(x, y - 1));
                Color bottomLeft = new Color(inputImage.getRGB(x - 1, y + 1));

                Color topRight = new Color(inputImage.getRGB(x + 1, y - 1));
                Color bottomMiddle = new Color(inputImage.getRGB(x, y + 1));
                Color bottomRight = new Color(inputImage.getRGB(x + 1, y + 1));

                sum += topLeft.getRed() * kernelY[0][0] + middleTop.getRed() * kernelY[0][1] + topRight.getRed() * kernelY[0][2];
                sum += bottomLeft.getRed() * kernelY[2][0] + bottomMiddle.getRed() * kernelY[2][1] + bottomRight.getRed() * kernelY[2][2];

                gY[x][y] = sum;
                outputImage.setRGB(x, y, 0xff000000 | (int) sum << 16 | (int) sum << 8 | (int) sum);
                //outputImage.setRGB(x, y, (int)sum);

            }
        }

        File resultFile = new File("secondSobelY.jpg");
        ImageIO.write(outputImage, "jpg", resultFile);

    }

    public void run() throws Exception {
        this.convertToGreyscale();
        this.sobelOperator();
        this.findCircles();
    }

}

