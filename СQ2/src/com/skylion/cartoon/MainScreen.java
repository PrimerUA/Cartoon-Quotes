package com.skylion.cartoon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.skylion.cartoon.constants.ConstantsFacade;
import com.skylion.cartoon.core.FragmentsGenerator;
import com.skylion.cartoon.data.ShowsList;
import com.skylion.cartoon.entity.User;
import com.skylion.cartoon.fragments.MainFragment;
import com.skylion.cartoon.messages.ExitDialog;
import com.skylion.cartoon.util.AppRater;
import com.skylion.cartoon.util.PreferencesLoader;
import com.skylion.cartoon.util.controllers.BackgroundController;
import com.skylion.cartoon.util.controllers.DailyNotificationController;
import com.skylion.cartoon.util.providers.ConnectionProvider;
import com.skylion.cartoon.util.providers.QuoteRatingsProvider;
import com.startad.lib.SADView;

public class MainScreen extends SherlockFragmentActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mShowsTitles;

	private AdView adView;
	protected SADView sadView;

	private static int selectedItem;

	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);

		PreferencesLoader.getInstance().initPrefs(this);
		QuoteRatingsProvider.initRatings(this);
		ShowsList.initShows(this);
		DailyNotificationController.initNotification(this);
		AppRater.app_launched(this);

		initScreen();
		setDrawerPanel(savedInstanceState);
		setActionBar();

		getSupportFragmentManager().beginTransaction().add(R.id.content_frame, new MainFragment()).commit();

		adView = new AdView(this, AdSize.SMART_BANNER, getString(R.string.admob_publisher_id));
		((LinearLayout) findViewById(R.id.content_frame)).addView(adView);
		// adView.loadAd(new AdRequest());

		this.sadView = new SADView(this, getString(R.string.sad_publisher_id));
		((LinearLayout) findViewById(R.id.content_frame)).addView(this.sadView);
		this.sadView.loadAd(SADView.LANGUAGE_RU);

		SharedPreferences.Editor editor = prefs.edit();
		try {
			int currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			if (currentVersion > prefs.getInt("version", 0)) {
				editor.putInt("version", currentVersion);
				editor.commit();
				startActivity(new Intent(this, NewsScreen.class));
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		if (isFirstLauch()) {
			editor.putBoolean("firstLaunch", false);
			editor.putBoolean("loggedIn", false);
			editor.commit();
			startActivity(new Intent(this, WelcomeScreen.class));
		}

		if ((!User.getInstance().isLoggedIn() || User.getInstance().getId() == -1) && ConnectionProvider.isConnectionAvailable(this)) {
			startActivity(new Intent(this, AuthScreen.class));
		}
	}

	private boolean isFirstLauch() {
		return prefs.getBoolean("firstLaunch", true);
	}

	private void setActionBar() {
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Context context = getSupportActionBar().getThemedContext();
		// ArrayAdapter<CharSequence> listAdapter =
		// ArrayAdapter.createFromResource(context, R.array.language_list,
		// R.layout.sherlock_spinner_item);
		// listAdapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		// getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		// getSupportActionBar().setListNavigationCallbacks(listAdapter, new
		// OnNavigationListener() {
		//
		// @Override
		// public boolean onNavigationItemSelected(int itemPosition, long
		// itemId) {
		// if (itemPosition == 0) {
		// LanguageController.setCurrentLanguage(LanguageController.RUS);
		// } else {
		// LanguageController.setCurrentLanguage(LanguageController.ENG);
		// }
		// return true;
		// }
		// });
	}

	private void initScreen() {

		mTitle = getString(R.string.fragment_stream);
		mDrawerTitle = getString(R.string.menu_label);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mShowsTitles = getResources().getStringArray(R.array.shows_array_rus);

		selectedItem = 0;

		prefs = getSharedPreferences("topquotes", 0);
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		SherlockFragment fragment = null;

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			fragment = selectItem(position);
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

			if (position != 0 && position != 1) {
				BackgroundController.changeBackground(position - ConstantsFacade.MENU_ITEMS_NOT_SHOWS, mDrawerLayout);
			} else {
				BackgroundController.changeBackground(-1, mDrawerLayout);
			}

			mDrawerList.setItemChecked(position, true);
			setTitle(mShowsTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);

			selectedItem = position;
		}
	}

	private void setDrawerPanel(Bundle savedInstanceState) {
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		if (android.os.Build.VERSION.SDK_INT >= 11) {
			mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mShowsTitles));
		} else {
			mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item_compat, mShowsTitles));
		}

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(selectedItem);
		}
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings: {
			showPreferencesScreen();
			break;
		}
		default: {
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	private void showPreferencesScreen() {
		Intent intent = new Intent(this, PreferencesScreen.class);
		startActivity(intent);
	}

	private SherlockFragment selectItem(int position) {
		return FragmentsGenerator.generateFragment(position);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			ExitDialog.showExitDialog(this);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.actionbar_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public DrawerLayout getDrawerLayout() {
		return mDrawerLayout;
	}

	public ListView getDrawerList() {
		return mDrawerList;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (PreferencesLoader.getInstance().getTheme() == 0) {
			mDrawerList.setBackgroundColor(Color.parseColor(getString(R.color.theme_red)));
		} else if (PreferencesLoader.getInstance().getTheme() == 1) {
			mDrawerList.setBackgroundColor(Color.parseColor(getString(R.color.theme_green)));
		} else {
			mDrawerList.setBackgroundColor(Color.parseColor(getString(R.color.theme_yellow)));
		}
	}

	@Override
	public void onDestroy() {
		if (adView != null)
			adView.destroy();
		if (this.sadView != null)
			this.sadView.destroy();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			showPreferencesScreen();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}