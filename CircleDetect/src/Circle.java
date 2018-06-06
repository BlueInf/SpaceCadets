import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Circle implements Comparable<Circle>{

    private int x;
    private int y;
    private int r;



    public Circle(int r){
        this.x=0;
        this.y=0;
        this.r=r;
    }
    public Circle(int x,int y,int r){
        this.x=x;
        this.y=y;
        this.r=r;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setR(int r) {
        this.r = r;
    }

    /**
    @Override
    public int compare(Circle o1, Circle o2) {



        return o1.getR()-o2.getR();
    }
    */

    //...
    @Override
    public int compareTo(Circle otherCircle) {
        return (this.getR() - otherCircle.getR());
    }



    public static void main(String[] args){

        List<Circle> circles=new ArrayList<Circle>();
        circles.add(new Circle(6));
        circles.add(new Circle(3));
        circles.add(new Circle(10));

        Collections.sort(circles);

        for(Circle c:circles) {

            System.out.println(c.getR()+" ");

        }




    }

}

