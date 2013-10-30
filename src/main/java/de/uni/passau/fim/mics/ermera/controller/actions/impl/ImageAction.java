package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.controller.actions.Action;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;

public class ImageAction implements Action {
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String imageFile = request.getParameter("file");
        File f = new File(imageFile);
        String mt = new MimetypesFileTypeMap().getContentType(f);
        response.setContentType(mt);

        BufferedImage bi = ImageIO.read(f);
        OutputStream out = response.getOutputStream();
        ImageIO.write(bi, "png", out);
        out.close();

        return null;
    }
}
