package com.company;

public class Node {
    int x;
    int y;
    int xs;
    int ys;
    Node parent;
    double F;
    double G;
    double H;
    int index;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getXs() {
        return xs;
    }

    public void setXs(int xs) {
        this.xs = xs;
    }

    public int getYs() {
        return ys;
    }

    public void setYs(int ys) {
        this.ys = ys;
    }

    public double getF() {
        return F;
    }

    public void setF(double f) {
        F = f;
    }

    public double getG() {
        return G;
    }

    public void setG(double g) {
        G = g;
    }

    public double getH() {
        return H;
    }

    public void setH(double h) {
        H = h;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    @Override
    public String toString() {
        return "x= " + x +
                ", y= " + y;
    }
}
