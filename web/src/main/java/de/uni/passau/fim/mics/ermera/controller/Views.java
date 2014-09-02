package de.uni.passau.fim.mics.ermera.controller;

import de.uni.passau.fim.mics.ermera.controller.actions.impl.*;
import de.uni.passau.fim.mics.ermera.controller.actions.impl.doc.RemoveBlockAction;
import de.uni.passau.fim.mics.ermera.controller.actions.impl.doc.SortBlocksAction;
import de.uni.passau.fim.mics.ermera.controller.actions.impl.doc.ToggleHeadlineAction;
import de.uni.passau.fim.mics.ermera.controller.actions.impl.doc.ToggleNewParagraphAction;

public enum Views {
    HOMEPAGE(ViewNames.HOMEPAGE, HomepageAction.class, null),
    LOGIN(ViewNames.LOGIN, LoginAction.class, null),
    LOGOUT(ViewNames.LOGOUT, LogoutAction.class, null),
    CONFIG(ViewNames.CONFIG, ConfigAction.class, ConfigAction.class),
    UPLOAD(ViewNames.UPLOAD, UploadAction.class, UploadAction.class),
    EXTRACT(ViewNames.EXTRACT, ExtractAction.class, null),
    DISPLAY(ViewNames.DISPLAY, DisplayAction.class, null),
    IMAGE(ViewNames.IMAGE, ImageAction.class, null),
    DOCUMENT_SORT(ViewNames.DOCUMENT_SORT, SortBlocksAction.class, null),
    DOCUMENT_REMOVEBLOCK(ViewNames.DOCUMENT_REMOVEBLOCK, RemoveBlockAction.class, null),
    DOCUMENT_TOGGLEBLOCKHEADLINE(ViewNames.DOCUMENT_TOGGLEBLOCKHEADLINE, ToggleHeadlineAction.class, null),
    DOCUMENT_TOGGLEBLOCKNEWPARAGRAPH(ViewNames.DOCUMENT_TOGGLEBLOCKNEWPARAGRAPH, ToggleNewParagraphAction.class, null),
    ANNOTATE(ViewNames.ANNOTATE, AnnotateAction.class, null),
    EXPORT(ViewNames.EXPORT, ExportAction.class, null),
    MODELCREATE(ViewNames.MODELCREATE, ModelCreateAction.class, null),
    MODELUSE(ViewNames.MODELUSE, ModelUseAction.class, null),
    NLP(ViewNames.NLP, null, NLPAction.class),
    EVALUATION(ViewNames.EVALUATION, EvaluationAction.class, EvaluationSaveAction.class),
    EVALUATIONGROUPED(ViewNames.EVALUATIONGROUPED, EvaluationGroupedAction.class, EvaluationSaveAction.class);

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
