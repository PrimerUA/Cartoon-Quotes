package com.skylion.cartoon.util.controllers;

import com.skylion.cartoon.data.ShowsList;
import com.skylion.cartoons.R;
import android.support.v4.widget.DrawerLayout;

public class BackgroundController {
	
	public static void changeBackground(int showId, DrawerLayout layout) {
	    if (showId == -1) {
		layout.setBackgroundResource(R.drawable.w1);
	    } else {
		layout.setBackgroundResource(ShowsList.getBackgroundByShowId(showId));
	    }
	}
	
}
