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
     *
     * @param o other {@code BlockBean}
     * @return 0, if orders are equal;
     *         +, if this order is lower then other;
     *         -, else
     */
    @Override
    public int compareTo(BlockBean o) {
        return this.order - o.order;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || (this.getClass() != obj.getClass())) {
            return false;
        }
        BlockBean other = (BlockBean) obj;
        return (this.id.equals(other.id))
                && (this.text.equals(other.text))
                && (this.order == other.order);
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + order;

        return result;
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
