package com.boredxgames.tictactoeclient.domain.managers.navigation;

@FunctionalInterface
public interface NavigationParameterAware {
    void setNavigationParameter(Object parameter);
}