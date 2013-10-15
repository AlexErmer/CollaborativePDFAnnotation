package de.uni.passau.fim.mics.ermera;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ActionFactory {

    private static Map<String, Action> actions;

    private ActionFactory(){};

    static {
        actions = new HashMap<String, Action>();
        actions.put("GET/testentry", new TestentryAction());
        actions.put("GET/extract", new ExtractAction());
        actions.put("GET/image", new ImageAction());
    }

    public static Action getAction(HttpServletRequest request) {
        System.out.println("Routing: " + request.getMethod() + request.getPathInfo());
        return actions.get(request.getMethod() + request.getPathInfo());
    }
}
