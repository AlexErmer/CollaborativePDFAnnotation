package de.uni.passau.fim.mics.ermera.beans;

import java.util.ArrayList;
import java.util.List;

public class PageBean {
    List<BlockBean> blocks = new ArrayList<BlockBean>();
    int width;
    int height;
    String imagefilename;
    int number;
    List<LineBean> lines = new ArrayList<LineBean>();

    public List<BlockBean> getBlocks() {
        return blocks;
    }

    public void addBlock(BlockBean block) {
        blocks.add(block);
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

    public String getImagefilename() {
        return imagefilename;
    }

    public void setImagefilename(String imagefilename) {
        this.imagefilename = imagefilename;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<LineBean> getLines() {
        return lines;
    }

    public void addLine(LineBean lineBean) {
        this.lines.add(lineBean);
    }
}
