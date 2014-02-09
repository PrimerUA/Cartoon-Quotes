package com.skylion.cartoon.core;

import com.actionbarsherlock.app.SherlockFragment;
import com.skylion.cartoon.constants.ConstantsFacade;
import com.skylion.cartoon.fragments.MainFragment;
import com.skylion.cartoon.fragments.QuizFragment;
import com.skylion.cartoon.fragments.ShowsFragment;
import com.skylion.cartoon.fragments.TopFragment;

import android.os.Bundle;

public class FragmentsGenerator {

    public static SherlockFragment generateFragment(int position) {
	if (position == 0) {
	    return new MainFragment();
	} else if (position == 1) {
	    return new QuizFragment();
	} else if (position == 2) {
	    return new TopFragment();
	} else {
	    SherlockFragment showsFragment = new ShowsFragment();
	    Bundle args = new Bundle();
	    args.putInt(ConstantsFacade.ARG_SHOWS_NUMBER, position - ConstantsFacade.MENU_ITEMS_NOT_SHOWS);
	    showsFragment.setArguments(args);
	    return showsFragment;
	}
    }
}
