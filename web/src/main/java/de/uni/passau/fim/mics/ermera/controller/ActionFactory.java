package de.uni.passau.fim.mics.ermera.controller;

import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.controller.actions.impl.*;
import de.uni.passau.fim.mics.ermera.controller.actions.impl.docModActions.RemoveBlockAction;
import de.uni.passau.fim.mics.ermera.controller.actions.impl.docModActions.SortBlocksAction;
import de.uni.passau.fim.mics.ermera.controller.actions.impl.docModActions.ToggleHeadlineAction;
import de.uni.passau.fim.mics.ermera.controller.actions.impl.docModActions.ToggleNewParagraphAction;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ActionFactory {
    private static Map<String, Action> actions;

    // private constructor to make class not instancable
    private ActionFactory() {
    }

    static {
        actions = new HashMap<>();
        actions.put("GET/login", new LoginAction());
        actions.put("GET/logout", new LogoutAction());

        actions.put("GET/homepage", new HomepageAction());
        actions.put("GET/upload", new UploadAction());
        actions.put("POST/upload", new UploadAction());
        actions.put("GET/extract", new ExtractAction());
        actions.put("GET/display", new DisplayAction());
        actions.put("GET/image", new ImageAction());
        actions.put("GET/document_sort", new SortBlocksAction());
        actions.put("GET/document_removeBlock", new RemoveBlockAction());
        actions.put("GET/document_toggleBlockHeadline", new ToggleHeadlineAction());
        actions.put("GET/document_toggleBlockNewParagraph", new ToggleNewParagraphAction());
        actions.put("GET/annotate", new AnnotateAction());
        actions.put("GET/export", new ExportAction());
        actions.put("GET/modelCreate", new ModelCreateAction());
        actions.put("GET/modelUse", new ModelUseAction());
        actions.put("POST/nlp", new NLPAction());
        actions.put("GET/evaluation", new EvaluationAction());
        actions.put("POST/evaluation", new EvaluationSaveAction());
    }

    public static Action getAction(HttpServletRequest request) {
        System.out.println("Routing: " + request.getMethod() + request.getPathInfo());
        return actions.get(request.getMethod() + request.getPathInfo());
    }
}
