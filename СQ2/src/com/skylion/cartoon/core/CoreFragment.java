package com.skylion.cartoon.core;

import com.actionbarsherlock.app.SherlockFragment;
import com.skylion.cartoon.util.PreferencesLoader;
import com.skylion.cartoon.util.controllers.LanguageController;


public abstract class CoreFragment extends SherlockFragment {

    private int currentTheme;

    protected abstract void initFragment();

    protected abstract void addQuotesOnScreen();

    protected abstract void updateContent();

    @Override
    public void onPause() {
	super.onPause();
	currentTheme = getTheme();
    }

    @Override
    public void onResume() {
	super.onResume();
	if (currentTheme != getTheme()) {
	    updateContent();
	}
    }

    public LanguageController getLanguage() {
	return LanguageController.getCurrentLanguage();
    }

    public int getTheme() {
	return PreferencesLoader.getTheme();
    }

}
