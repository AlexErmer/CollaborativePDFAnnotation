package de.uni.passau.fim.mics.ermera.beans;

import java.util.SortedSet;
import java.util.TreeSet;

public class DocumentBean {
    SortedSet<PageBean> pages = new TreeSet<>();

    public SortedSet<PageBean> getPages() {
        return pages;
    }

    public void addPage(PageBean page) {
        pages.add(page);
    }

    public void sortBlocks(String[] items) {
        for (PageBean page : pages) {
            page.sort(items);
        }
    }
}