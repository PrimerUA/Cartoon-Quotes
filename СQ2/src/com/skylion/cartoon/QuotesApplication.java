package com.skylion.cartoon;

import com.parse.Parse;
import com.parse.PushService;

import android.app.Application;

public class QuotesApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "riTpY2BtmzyuWajsGR8cfCHlaorPHgSfK3wtCHWI", "ZN5hFhX0eKaPTGPGGdA2WE2xYjXKjCsMg9YGjIOS");
		PushService.setDefaultPushCallback(this, MainScreen.class);
	}

}
