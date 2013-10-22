package de.uni.passau.fim.mics.ermera.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadAction implements Action {

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = "pud$WCk6ELUhksQwpuhscw==";

        // get filestream
        // validate mediatype
        // create id
        // store pdf

        // redirect to extract for extraction and displaying
        return "extract?id=" + id;
    }
}