package de.uni.passau.fim.mics.ermera.beans;

public class BlockBean {
    String id;
    int left;
    int top;
    int width;
    int height;
    String cssClass;
    TooltipBean tooltipBean;
    String text;
    boolean selectedBlock;
    int order;

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

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public TooltipBean getTooltipBean() {
        return tooltipBean;
    }

    public void setTooltipBean(TooltipBean tooltipBean) {
        this.tooltipBean = tooltipBean;
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
