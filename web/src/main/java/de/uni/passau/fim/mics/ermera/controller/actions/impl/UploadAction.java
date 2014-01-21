package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import com.mendeley.oapi.schema.Profile;
import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.dao.content.ContentRepositoryDao;
import de.uni.passau.fim.mics.ermera.dao.content.ContentRepositoryDaoImpl;
import de.uni.passau.fim.mics.ermera.dao.content.ContentRepositoryException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

public class UploadAction implements Action {

    private static final Logger LOGGER = Logger.getLogger(UploadAction.class);

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        MessageUtil mu = (MessageUtil) session.getAttribute(MessageUtil.NAME);
        Profile profile = (Profile) session.getAttribute("profile");
        String userid = profile.getMain().getProfileId();

        // get filestream
        Part filePart = null;
        try {
            filePart = request.getPart("pdfFile");
        } catch (ServletException e) {
            //nothing to do
            LOGGER.info("nothing to do.. just catching a servletException", e);
        }
        if (filePart != null) {
            String filename = getFilename(filePart);
            InputStream filecontent = filePart.getInputStream();

            // create id
            String id = filename.replaceAll("\\s", "");

            // store pdf
            try {
                ContentRepositoryDao contentRepositoryDao = new ContentRepositoryDaoImpl();
                contentRepositoryDao.store(userid, id, filecontent);
            } catch (ContentRepositoryException e) {
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

            // forward to extract
            return "extract?type=knowminer&id=" + id;
        } else {
            return "upload";
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