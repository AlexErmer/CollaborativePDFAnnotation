package de.uni.passau.fim.mics.ermera.model;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageBean implements Comparable<PageBean>, Serializable {
    private static final int NOT_SORTED = -10;
    private static final long serialVersionUID = -8440771047061751365L;

    private int number;
    private int width;
    private int height;
    private String imagefilename;
    private List<BlockBean> blocks = new ArrayList<>();
    private transient List<LineBean> lines = new ArrayList<>();

    /**
     * Compare with other {@code PageBean} by page number
     *
     * @param o other {@code PageBean}
     * @return 0, if page numbers are equal;
     *         +, if this page numbers is higher then other;
     *         -, else
     */
    @Override
    public int compareTo(PageBean o) {
        return this.number - o.number;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || (this.getClass() != obj.getClass())) {
            return false;
        }
        PageBean other = (PageBean) obj;
        return (this.number == other.number)
                && (this.imagefilename.equals(other.imagefilename));
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + number;
        result = 31 * result + (imagefilename != null ? imagefilename.hashCode() : 0);

        return result;
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
                break;
            }
        }
        createLines();
    }

    public void toggleHeadline(String item) {
        for (BlockBean block : blocks) {
            if (item.equals(block.getId())) {
                block.setHeadline(!block.isHeadline());
                break;
            }
        }
    }

    public void toggleNewParagraph(String item) {
        for (BlockBean block : blocks) {
            if (item.equals(block.getId())) {
                block.setNewParagraph(!block.isNewParagraph());
                break;
            }
        }
    }

    /**
     * helper to create {@code LineBean}s from current blocks.
     */
    public void createLines() {
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

    /**
     * Converts the blocklist into a String for brat.
     *
     * @return converted string.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (BlockBean block : blocks) {
            if (block.isSelectedBlock()) {
                if (sb.length() != 0 && block.isHeadline()) {
                    sb.append(System.lineSeparator());
                    sb.append(System.lineSeparator());
                } else if (block.isNewParagraph()) {
                    sb.append(System.lineSeparator());
                }
                sb.append(StringEscapeUtils.unescapeHtml(block.getText()));
                if (block.isHeadline()) {
                    sb.append(System.lineSeparator());
                } else  {
                    sb.append(" ");
                }
            }
        }
        return sb.toString();
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
