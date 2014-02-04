package de.uni.passau.fim.mics.ermera.controller.actions;

import de.uni.passau.fim.mics.ermera.controller.actions.impl.*;
import de.uni.passau.fim.mics.ermera.controller.actions.impl.doc.RemoveBlockAction;
import de.uni.passau.fim.mics.ermera.controller.actions.impl.doc.SortBlocksAction;
import de.uni.passau.fim.mics.ermera.controller.actions.impl.doc.ToggleHeadlineAction;
import de.uni.passau.fim.mics.ermera.controller.actions.impl.doc.ToggleNewParagraphAction;


//TODO: einbinden und damit die ActionFactory sparen
// aber: ExportAction hat eine Redirection auf "../brat/brat/index.xhtml" das lässt sich so leider nicht mehr abbilden :(
public enum Views {
    HOMEPAGE("homepage", new HomepageAction(), null),
    LOGIN("login", new LoginAction(), null),
    LOGOUT("logout", new LogoutAction(), null),
    UPLOAD("upload", new UploadAction(), new UploadAction()),
    EXTRACT("extract", new ExtractAction(), null),
    DISPLAY("display", new DisplayAction(), null),
    IMAGE("image", new ImageAction(), null),
    DOCUMENT_SORT("document_sort", new SortBlocksAction(), null),
    DOCUMENT_REMOVEBLOCK("document_removeBlock", new RemoveBlockAction(), null),
    DOCUMENT_TOGGLEBLOCKHEADLINE("document_toggleBlockHeadline", new ToggleHeadlineAction(), null),
    DOCUMENT_TOGGLEBLOCKNEWPARAGRAPH("document_toggleBlockNewParagraph", new ToggleNewParagraphAction(), null),
    ANNOTATE("annotate", new AnnotateAction(), null),
    EXPORT("export", new ExportAction(), null),
    MODEL_CREATE("modelCreate", new ModelCreateAction(), null),
    MODEL_USE("modelUse", new ModelUseAction(), null),
    NLP("nlp", new NLPAction(), null),
    EVALUATION("evaluation", new EvaluationAction(), new EvaluationSaveAction());


    private String viewName;
    private Action get;
    private Action post;

    Views(String viewName, Action get, Action post) {
        this.viewName = viewName;
        this.get = get;
        this.post = post;
    }

    public String getViewName() {
        return viewName;
    }

    public Action getGet() {
        return get;
    }

    public Action getPost() {
        return post;
    }
}
