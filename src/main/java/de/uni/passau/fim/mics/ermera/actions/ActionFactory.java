package de.uni.passau.fim.mics.ermera.actions;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ActionFactory {

    private static Map<String, Action> actions;

    // private constructor to make class not instancable
    private ActionFactory(){}

    static {
        actions = new HashMap<>();
        actions.put("GET/contact", new ContactAction());
        actions.put("GET/extract", new ExtractAction());
        actions.put("GET/image", new ImageAction());

        actions.put("POST/upload", new UploadAction());
    }

    public static Action getAction(HttpServletRequest request) {
        System.out.println("Routing: " + request.getMethod() + request.getPathInfo());
        return actions.get(request.getMethod() + request.getPathInfo());
    }
}
