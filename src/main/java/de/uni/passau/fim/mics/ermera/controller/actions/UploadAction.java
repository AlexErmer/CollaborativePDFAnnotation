package de.uni.passau.fim.mics.ermera.controller.actions;

import de.uni.passau.fim.mics.ermera.dao.ContentRepositoryDao;
import de.uni.passau.fim.mics.ermera.dao.ContentRepositoryDaoImpl;
import de.uni.passau.fim.mics.ermera.dao.ContentRepositoryException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

public class UploadAction implements Action {

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // get filestream
        Part filePart = request.getPart("pdfFile");
        String filename = getFilename(filePart);
        InputStream filecontent = filePart.getInputStream();

        // create id
        String id = filename.replaceAll("\\s", "");

        // store pdf
        try {
            ContentRepositoryDao contentRepositoryDao = new ContentRepositoryDaoImpl();
            contentRepositoryDao.store(id, filecontent);
        } catch (ContentRepositoryException e) {
            request.setAttribute("errorMessage", "Error while handling FileStreams: " + e.getMessage());
        } finally {
            try {
                if (filecontent != null) {
                    filecontent.close();
                }
            } catch (IOException e) {
                request.setAttribute("errorMessage", "Error while closing FileStreams: " + e.getMessage());
            }
        }

        // forward to extract
        return "extract?id=" + id;
    }

    /**
     * Credit goes to BAUKE SCHOLTZ alias BalusC
     * http://stackoverflow.com/questions/2422468/how-to-upload-files-to-server-using-jsp-servlet#2424824
     * http://balusc.blogspot.de/2009/12/uploading-files-in-servlet-30.html
     */
    private static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }
}