package de.uni.passau.fim.mics.ermera.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class PageBean implements Comparable<PageBean> {
    private SortedSet<BlockBean> blocks = new TreeSet<BlockBean>();
    private int width;
    private int height;
    private String imagefilename;
    private int number;
    private List<LineBean> lines = new ArrayList<LineBean>();

    /**
     * Compare with other {@code PageBean} by page number
     * @param o other {@code PageBean}
     * @return 0, if page numbers are equal;
     * 1, if this page numbers is higher then other;
     * -1, else
     */
    public int compareTo(PageBean o) {
        if (this.number == o.number) {
            return 0;
        } else if (this.number > o.number) {
            return 1;
        } else {
            return -1;
        }
    }

    public SortedSet<BlockBean> getBlocks() {
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
