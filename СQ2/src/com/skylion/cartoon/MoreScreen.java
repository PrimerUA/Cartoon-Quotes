package com.skylion.cartoon;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.skylion.cartoon.util.PreferencesLoader;
import com.skylion.cartoon.R;

public class MoreScreen extends Activity {

	private LinearLayout quotesLayout;
	private LinearLayout truthLayout;
	private LinearLayout quarterLayout;
	private LinearLayout blogLayout;
	private LinearLayout vkLayout;
	private LinearLayout brainLayout;
	private LinearLayout archangelLayout;
	private LinearLayout quezzleLayout;

	private LinearLayout contentLayout;
	private Button vkButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_screen);

		activityInit();
	}

	private void activityInit() {
		contentLayout = (LinearLayout) findViewById(R.id.MoreScreen_contentLayout);

		quotesLayout = (LinearLayout) findViewById(R.id.MoreScreen_quotesLayout);
		truthLayout = (LinearLayout) findViewById(R.id.MoreScreen_truthLayout);
		quarterLayout = (LinearLayout) findViewById(R.id.MoreScreen_quarterLayout);
		blogLayout = (LinearLayout) findViewById(R.id.MoreScreen_blogLayout);
		vkLayout = (LinearLayout) findViewById(R.id.MoreScreen_vkLayout);
		brainLayout = (LinearLayout) findViewById(R.id.MoreScreen_brainLayout);
		archangelLayout = (LinearLayout) findViewById(R.id.MoreScreen_archangelLayout);
		quezzleLayout = (LinearLayout) findViewById(R.id.MoreScreen_quezzleLayout);
		
		vkButton = (Button) findViewById(R.id.MoreScreen_vkButton);

		quotesLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				visitUrl("https://play.google.com/store/apps/details?id=top.quotes.pkg");
			}
		});
		
		truthLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				visitUrl("https://play.google.com/store/apps/details?id=com.primerworldapps.truthgame");
			}
		});

		brainLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				visitUrl("https://play.google.com/store/apps/details?id=com.primerworldapps.brainbreak");
			}
		});

		quarterLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				visitUrl("https://play.google.com/store/apps/details?id=top.comic.pkg");
			}
		});

		blogLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				visitUrl("http://suit-up-primer.blogspot.com/");
			}
		});

		vkLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				visitUrl("http://vk.com/liveindroid");
			}
		});
		
		archangelLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				visitUrl("http://arhangel-studio.com.ua/");
			}
		});
		
		quezzleLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				visitUrl("https://play.google.com/store/apps/details?id=com.skylion.quezzle");
			}
		});
		
		vkButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				visitUrl("http://vk.com/top_quotes_apps");
			}
		});

		if (PreferencesLoader.getInstance().getTheme() == 0) {
			contentLayout.setBackgroundResource(R.color.theme_red);
		} else if (PreferencesLoader.getInstance().getTheme() == 1) {
			contentLayout.setBackgroundResource(R.color.theme_green);
		} else {
			contentLayout.setBackgroundResource(R.color.theme_yellow);
		}
	}

	private void visitUrl(String url) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}
}