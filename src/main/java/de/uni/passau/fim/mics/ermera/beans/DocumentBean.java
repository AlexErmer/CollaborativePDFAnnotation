package de.uni.passau.fim.mics.ermera.beans;

import java.util.ArrayList;
import java.util.List;

public class DocumentBean {
    List<PageBean> pages = new ArrayList<>();

    public void addPage(PageBean page) {
        pages.add(page);
    }

    /**
     * Takes one great stringarray of blockids to reorder all pages at once.
     *
     * @param items String array filled with blockIds of all pages.
     */
    public void reorderAllPageBeans(String[] items) {
        for (PageBean page : pages) {
            page.reorder(items);
        }
    }

    /**
     * Delegates stringarray of blockids to correct {@code PageBean}, which is found by parameter {@code pageNumber}, to reorder its blocks.
     *
     * @param pageNumber the pagenumber
     * @param items      String array filled with blockIds.
     */
    public void reorderPageBean(int pageNumber, String[] items) {
        pages.get(pageNumber).reorder(items);
    }

    /**
     * Delegates blockid to all {@code PageBean}s to remove this block.
     *
     * @param item the blockid to remove.
     */
    public void unselectBlock(String item) {
        for (PageBean page : pages) {
            page.unselectBlock(item);
        }
    }

    /**
     * Delegates blockid to correct {@code PageBean}s, which is found by parameter {@code pageNumber}, to remove this block.
     *
     * @param pageNumber the pagenumber
     * @param item the blockid to remove.
     */
    public void unselectBlock(int pageNumber, String item) {
        pages.get(pageNumber).unselectBlock(item);
    }

    // Getter & Setter

    public List<PageBean> getPages() {
        return pages;
    }
}