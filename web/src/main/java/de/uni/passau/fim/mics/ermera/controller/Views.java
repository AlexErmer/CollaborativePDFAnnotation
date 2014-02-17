package de.uni.passau.fim.mics.ermera.controller;

import de.uni.passau.fim.mics.ermera.controller.actions.impl.*;
import de.uni.passau.fim.mics.ermera.controller.actions.impl.doc.RemoveBlockAction;
import de.uni.passau.fim.mics.ermera.controller.actions.impl.doc.SortBlocksAction;
import de.uni.passau.fim.mics.ermera.controller.actions.impl.doc.ToggleHeadlineAction;
import de.uni.passau.fim.mics.ermera.controller.actions.impl.doc.ToggleNewParagraphAction;

public enum Views {
    HOMEPAGE("homepage", HomepageAction.class, null),
    LOGIN("login", LoginAction.class, null),
    LOGOUT("logout", LogoutAction.class, null),
    CONFIG("config", ConfigAction.class, ConfigAction.class),
    UPLOAD("upload", UploadAction.class, UploadAction.class),
    EXTRACT("extract", ExtractAction.class, null),
    DISPLAY("display", DisplayAction.class, null),
    IMAGE("image", ImageAction.class, null),
    DOCUMENT_SORT("document_sort", SortBlocksAction.class, null),
    DOCUMENT_REMOVEBLOCK("document_removeBlock", RemoveBlockAction.class, null),
    DOCUMENT_TOGGLEBLOCKHEADLINE("document_toggleBlockHeadline", ToggleHeadlineAction.class, null),
    DOCUMENT_TOGGLEBLOCKNEWPARAGRAPH("document_toggleBlockNewParagraph", ToggleNewParagraphAction.class, null),
    ANNOTATE("annotate", AnnotateAction.class, null),
    EXPORT("export", ExportAction.class, null),
    MODELCREATE("modelCreate", ModelCreateAction.class, null),
    MODELUSE("modelUse", ModelUseAction.class, null),
    NLP("nlp", null, NLPAction.class),
    EVALUATION("evaluation", EvaluationAction.class, EvaluationSaveAction.class),
    EVALUATIONGROUPED("evaluationGrouped", EvaluationGroupedAction.class, EvaluationSaveAction.class);

    private String viewName;
    private Class get;
    private Class post;

    Views(String viewName, Class get, Class post) {
        this.viewName = viewName;
        this.get = get;
        this.post = post;
    }

    public Class getAction(String method) {
        if (method.equals("GET")) {
            return this.get;
        } else if (method.equals("POST")) {
            return this.post;
        }
        return null;
    }

    @Override
    public String toString() {
        return viewName;
    }
}
