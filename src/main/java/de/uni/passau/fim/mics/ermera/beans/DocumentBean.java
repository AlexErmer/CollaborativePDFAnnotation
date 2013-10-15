package de.uni.passau.fim.mics.ermera.beans;

import java.util.ArrayList;
import java.util.List;

public class DocumentBean {
    List<PageBean> pages = new ArrayList<PageBean>();

    public List<PageBean> getPages() {
        return pages;
    }

    public void addPage(PageBean page) {
        pages.add(page);
    }
}