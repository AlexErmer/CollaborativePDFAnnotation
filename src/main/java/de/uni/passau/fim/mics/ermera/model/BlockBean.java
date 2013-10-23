package de.uni.passau.fim.mics.ermera.model;

import java.io.Serializable;

public class BlockBean implements Comparable<BlockBean>, Serializable {
    private String id;
    private int left;
    private int top;
    private int width;
    private int height;
    private String cssClass;
    private boolean headline;
    private boolean newParagraph;
    private String text;
    private boolean selectedBlock;
    private int order;

    /**
     * Compare with other {@code BlockBean} by order number.
     * @param o other {@code BlockBean}
     * @return 0, if orders are equal;
     * 1, if this order is lower then other;
     * -1, else
     */
    public int compareTo(BlockBean o) {
        if (this.order < o.getOrder()) {
            return -1;
        } else {
            return 1;
        }
    }

    // Getter & Setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
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

    public boolean isHeadline() {
        return headline;
    }

    public void setHeadline(boolean headline) {
        this.headline = headline;
    }

    public boolean isNewParagraph() {
        return newParagraph;
    }

    public void setNewParagraph(boolean newParagraph) {
        this.newParagraph = newParagraph;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelectedBlock() {
        return selectedBlock;
    }

    public void setSelectedBlock(boolean selectedBlock) {
        this.selectedBlock = selectedBlock;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
