package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.controller.ViewNames;
import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomepageAction extends AbstractAction {

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        return ViewNames.HOMEPAGE;
    }
}
