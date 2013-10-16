package de.uni.passau.fim.mics.ermera.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageBean implements Comparable<PageBean> {
    private List<BlockBean> blocks = new ArrayList<>();
    private int width;
    private int height;
    private String imagefilename;
    private int number;
    private List<LineBean> lines = new ArrayList<>();

    /**
     * Compare with other {@code PageBean} by page number
     *
     * @param o other {@code PageBean}
     * @return 0, if page numbers are equal;
     *         1, if this page numbers is higher then other;
     *         -1, else
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

    /**
     * Sorts the {@code SortedSet} {@code blocks} by a simple String array filled with blockIds.
     * @param items String array filled with blockIds.
     */
    public void sort(String[] items) {
        int sortCounter = 10;
        for (String item : items) {
            for (BlockBean block : blocks) {

                if (item.equals(block.getId())) {
                    block.setOrder(sortCounter);
                    sortCounter += 10;
                    break;
                }
            }
        }
        Collections.sort(blocks);
    }

    public List<BlockBean> getBlocks() {
        return blocks;
    }

    public void addBlock(BlockBean block) {
        blocks.add(block);
        Collections.sort(blocks);
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
