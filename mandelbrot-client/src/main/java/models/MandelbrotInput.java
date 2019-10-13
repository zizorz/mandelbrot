package models;

import java.util.ArrayList;
import java.util.List;

public class MandelbrotInput {

    private Complex minC;
    private Complex maxC;

    private int width;
    private int height;

    private int maxNrOfIterations;
    private int divisions;
    private List<String> servers;

    public MandelbrotInput() {
        servers = new ArrayList<String>();
    }

    public Complex getMinC() {
        return minC;
    }

    public void setMinC(Complex minC) {
        this.minC = minC;
    }

    public Complex getMaxC() {
        return maxC;
    }

    public void setMaxC(Complex maxC) {
        this.maxC = maxC;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMaxNrOfIterations() {
        return maxNrOfIterations;
    }

    public void setMaxNrOfIterations(int maxNrOfIterations) {
        this.maxNrOfIterations = maxNrOfIterations;
    }

    public int getDivisions() {
        return divisions;
    }

    public void setDivisions(int divisions) {
        this.divisions = divisions;
    }

    public void addServer(String server) {
        this.servers.add(server);
    }

    public List<String> getServers() {
        return this.servers;
    }
}
