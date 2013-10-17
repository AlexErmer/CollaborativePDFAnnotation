package de.uni.passau.fim.mics.ermera;

import de.uni.passau.fim.mics.ermera.beans.DocumentBean;

import javax.servlet.http.HttpServletRequest;

public class Storage {
    private HttpServletRequest request;

    //TODO append real storage without request dependency
    public Storage(HttpServletRequest request) {
        this.request = request;
    }

    public DocumentBean load(String id) {
        return (DocumentBean) request.getSession().getAttribute("pdf_" + id);
    }

    public void store(String id, DocumentBean documentBean) {
        request.getSession().setAttribute("pdf_" + id, documentBean);
    }
}
