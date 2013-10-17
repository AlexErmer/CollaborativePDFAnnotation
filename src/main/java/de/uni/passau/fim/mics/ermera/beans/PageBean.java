package de.uni.passau.fim.mics.ermera.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageBean implements Comparable<PageBean> {
    private final int NOT_SORTED = -10;

    private int number;
    private int width;
    private int height;
    private String imagefilename;
    private List<BlockBean> blocks = new ArrayList<>();
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
     * Reorders the blocks by a simple String array filled with blockIds.
     *
     * @param items String array filled with blockIds.
     */
    public void reorder(String[] items) {
        for (BlockBean block : blocks) {
            block.setSelectedBlock(false);
            block.setOrder(NOT_SORTED);

            int sortCount = 0;
            for (String item : items) {
                sortCount++;
                if (item.equals(block.getId())) {
                    block.setSelectedBlock(true);
                    block.setOrder(sortCount);
                    break;
                }
            }
        }
        Collections.sort(blocks);
        createLines();
    }

    public void addBlock(BlockBean block) {
        blocks.add(block);
        Collections.sort(blocks);
        createLines();
    }

    public void unselectBlock(String item) {
        for (BlockBean block : blocks) {
            if (item.equals(block.getId())) {
                block.setSelectedBlock(false);
                block.setOrder(NOT_SORTED);
            }
        }
        createLines();
    }

    /**
     * helper to create {@code LineBean}s from current blocks.
     */
    private void createLines() {
        BlockBean prev = null;

        lines = new ArrayList<>();
        for (BlockBean block : blocks) {
            if (block.isSelectedBlock()) {
                if (prev != null) {
                    LineBean lineBean = new LineBean();
                    lineBean.setX1(prev.getLeft() + prev.getWidth() / 2);
                    lineBean.setY1(prev.getTop() + prev.getHeight() / 2);
                    lineBean.setX2(block.getLeft() + block.getWidth() / 2);
                    lineBean.setY2(block.getTop() + block.getHeight() / 2);
                    lines.add(lineBean);
                }
                prev = block;
            }
        }
    }

    // Getter & Setter

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

    public List<BlockBean> getBlocks() {
        return blocks;
    }

    public List<LineBean> getLines() {
        return lines;
    }
}
