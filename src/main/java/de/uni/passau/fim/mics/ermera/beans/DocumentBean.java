package de.uni.passau.fim.mics.ermera.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class DocumentBean {
    List<PageBean> pages = new ArrayList<> ();

    public void addPage(PageBean page) {
        pages.add(page);
    }

    public void sortBlocks(int pageNumber, String[] items) {
        pages.get(pageNumber).sort(items);
    }

    public void removeBlock(int pageNumber, String item) {
        pages.get(pageNumber).removeBlock(item);
    }

    // Getter & Setter

    public List<PageBean> getPages() {
        return pages;
    }
}