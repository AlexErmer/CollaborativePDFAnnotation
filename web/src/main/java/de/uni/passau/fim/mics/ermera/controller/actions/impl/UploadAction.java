package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.controller.Views;
import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.dao.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoException;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoImpl;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class UploadAction extends AbstractAction {
    private static final Logger LOGGER = Logger.getLogger(UploadAction.class);

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        Collection<Part> parts = null;
        try {
            parts = request.getParts();
        } catch (ServletException e) {
            //nothing to do
            LOGGER.info("nothing to do.. just catching a servletException", e);
        } catch (IOException e) {
            throw new ActionException("Fehler beim Lesen der hochgeladenen Datei", e);
        }
        if (parts != null) {
            for (Part filePart : parts) {
                String filename = getFilename(filePart);
                if ("".equals(filename)) {
                    mu.addMessage(MessageTypes.ERROR, "Keine Datei zum hochladen ausgewählt");
                    continue;
                }
                if (!"application/pdf".equals(filePart.getContentType())) {
                    mu.addMessage(MessageTypes.ERROR, "Bitte wählen Sie eine PDF Datei");
                    continue;
                }

                InputStream filecontent;
                try {
                    filecontent = filePart.getInputStream();
                } catch (IOException e) {
                    mu.addMessage(MessageTypes.ERROR, "Fehler beim Lesen der hochgeladenen Datei");
                    LOGGER.error("Fehler beim Lesen der hochgeladenen Datei", e);
                    continue;
                }

                // store pdf
                storePDF(filecontent, filename);
            }
        }
        return Views.UPLOAD.toString();
    }

    private void storePDF(InputStream filecontent, String filename) {
        try {
            DocumentDao documentDao = new DocumentDaoImpl();
            documentDao.storePDF(userid, filename, filecontent);

            mu.addMessage(MessageTypes.SUCCESS, "Datei " + filename + " erfolgreich hochgeladen.");
        } catch (DocumentDaoException e) {
            LOGGER.error("Error while handling FileStreams", e);
            mu.addMessage(MessageTypes.ERROR, "Error while handling FileStreams: " + e.getMessage());
        } finally {
            try {
                if (filecontent != null) {
                    filecontent.close();
                }
            } catch (IOException e) {
                LOGGER.error("IO Error while closing FileStreams", e);
                mu.addMessage(MessageTypes.ERROR, "Error while closing FileStreams: " + e.getMessage());
            }
        }
    }

    /**
     * Credit goes to BAUKE SCHOLTZ alias BalusC
     * http://stackoverflow.com/questions/2422468/how-to-upload-files-to-server-using-jsp-servlet#2424824
     * http://balusc.blogspot.de/2009/12/uploading-files-in-servlet-30.html
     *
     * @param part part
     * @return the filename
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