package ru.stqa.java_studying.sandbox;

public class Point {

    public double p1;
    public double p2;

    public Point(double p1, double p2) {
        this.p1 = p1;
        this.p2 = p2;
    }


    public double distance(Point p) {

        return Math.sqrt((p.p2 - this.p1)*(p.p2 - this.p1)+(p.p2 - this.p1)*(p.p2 - this.p1));

    }

}
