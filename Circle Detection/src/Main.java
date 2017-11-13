public class Main{

    public static void main(String[] args)throws Exception{

        ImageLoader image=new ImageLoader("coins.gif");
        //image.convertToGreyscale();
        //image.sobelOperator();
        image.findCircles();

    }
}
