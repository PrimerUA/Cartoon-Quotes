package com.skylion.cartoon.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.skylion.cartoon.NewQuoteScreen;
import com.skylion.cartoon.adapters.QuoteListAdapter;
import com.skylion.cartoon.adapters.UserQuoteListAdapter;
import com.skylion.cartoon.core.CoreFragment;
import com.skylion.cartoon.data.Quote;
import com.skylion.cartoon.data.Show;
import com.skylion.cartoon.data.ShowsList;
import com.skylion.cartoon.entity.UserQuote;
import com.skylion.cartoon.util.controllers.LanguageController;
import com.skylion.cartoon.util.controllers.LanguageController.LanguageChanger;
import com.skylion.cartoon.util.providers.ConnectionProvider;
import com.skylion.cartoon.R;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainFragment extends CoreFragment implements OnClickListener {

	private View rootView;

	private ListView contentList;

	private ImageView drawerImage;

	private List<ParseObject> userQuotesList;
	private List<Quote> quotesList;
	private List<String> titlesList;

	private Button newQuoteButton;
	private ImageButton refreshButton;

	private boolean isEnd;

	private int itemsQuantity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_main, container, false);
		initFragment();

		getSherlockActivity().setTitle(getString(R.string.fragment_stream));

//		LanguageController.setOnLanguageChangedListener(new LanguageChanger() {
//
//			@Override
//			public void onLanguageChanged() {
//				updateContent();
//			}
//		});

		updateContent();
		return rootView;
	}

	@Override
	protected void updateContent() {
		if (getTheme() == 0) {
			drawerImage.setBackgroundColor(Color.parseColor(getString(R.color.theme_red)));
			newQuoteButton.setBackgroundResource(R.drawable.quote_selector_pink);
			refreshButton.setBackgroundResource(R.drawable.quote_selector_pink);
		} else if (getTheme() == 1) {
			drawerImage.setBackgroundColor(Color.parseColor(getString(R.color.theme_green)));
			newQuoteButton.setBackgroundResource(R.drawable.quote_selector_white);
			refreshButton.setBackgroundResource(R.drawable.quote_selector_white);
		} else {
			drawerImage.setBackgroundColor(Color.parseColor(getString(R.color.theme_yellow)));
			newQuoteButton.setBackgroundResource(R.drawable.quote_selector_orange);
			refreshButton.setBackgroundResource(R.drawable.quote_selector_orange);
		}
		addQuotesOnScreen();
	}

	@Override
	protected void initFragment() {
		isEnd = false;

		boolean tabletSize = getActivity().getResources().getBoolean(R.bool.isTablet);
		if (tabletSize) {
			itemsQuantity = 25;
		} else {
			itemsQuantity = 10;
		}

		quotesList = new ArrayList<Quote>();
		userQuotesList = new ArrayList<ParseObject>();
		titlesList = new ArrayList<String>();

		newQuoteButton = (Button) rootView.findViewById(R.id.MainFragment_newQuoteButton);
		newQuoteButton.setOnClickListener(this);
		refreshButton = (ImageButton) rootView.findViewById(R.id.MainFragment_refreshButton);
		refreshButton.setOnClickListener(this);

		contentList = (ListView) rootView.findViewById(R.id.MainFragment_contentList);

		drawerImage = (ImageView) rootView.findViewById(R.id.MainFragment_drawerImage);

		if (getTheme() == 0) {
			drawerImage.setBackgroundColor(Color.parseColor(getString(R.color.theme_red)));
		} else if (getTheme() == 1) {
			drawerImage.setBackgroundColor(Color.parseColor(getString(R.color.theme_green)));
		} else {
			drawerImage.setBackgroundColor(Color.parseColor(getString(R.color.theme_yellow)));
		}

		//getSherlockActivity().getSupportActionBar().setSelectedNavigationItem(getLanguage().ordinal());

//		contentList.setOnScrollListener(new OnScrollListener() {
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//			}
//
//			private int lastSavedFirst = -1;
//
//			@Override
//			public void onScroll(final AbsListView view, final int first, final int visible, final int total) {
//				if (!isEnd && (visible < total) && (first + visible == total) && (first != lastSavedFirst)) {
//					lastSavedFirst = first;
//					addItemsOnScreen();
//				}
//			}
//		});
	}

	@Override
	protected void addQuotesOnScreen() {
		if (ConnectionProvider.isConnectionAvailable(getActivity())) {
			((LinearLayout) rootView.findViewById(R.id.MainFragment_onlineLayout)).setVisibility(View.VISIBLE);
			final ProgressDialog myProgressDialog = ProgressDialog.show(getActivity(), getString(R.string.connection),
					getString(R.string.connection_loading_quote), true);
			ParseQuery<ParseObject> query = ParseQuery.getQuery("UserQuote");
			query.whereEqualTo("type", 1);
			if (getLanguage() == LanguageController.RUS)
				query.whereEqualTo("language", 0);
			else
				query.whereEqualTo("language", 1);
			query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> quotesList, ParseException e) {
					if (e == null) {
						myProgressDialog.dismiss();
						userQuotesList = quotesList;
						Collections.reverse(userQuotesList);
						contentList.setAdapter(new UserQuoteListAdapter(getActivity(), (ArrayList<ParseObject>) userQuotesList));
					} else {
						Log.d("quotes", "Error: " + e.getMessage());
					}
				}
			});
		} else {
			doShowContent();
		}
	}

	private void doShowContent() {
		((LinearLayout) rootView.findViewById(R.id.MainFragment_onlineLayout)).setVisibility(View.GONE);
		quotesList.clear();
		updateQuoteList(itemsQuantity);
		contentList.setAdapter(new QuoteListAdapter(getActivity(), (ArrayList<String>) titlesList, (ArrayList<Quote>) quotesList));
	}


	private void updateQuoteList(int itemsQuantity) {
		for (int i = 0; i < itemsQuantity; i++) {
			Show show = ShowsList.getList().get(new Random().nextInt(ShowsList.SHOWS_LIST_SIZE));
			titlesList.add(show.getTitle(getLanguage()));
			Quote quote = show.getQuote(new Random().nextInt(ShowsList.SHOWS_LIST_SIZE), LanguageController.getCurrentLanguage());
			if (!quotesList.contains(quote))
				quotesList.add(quote);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.MainFragment_newQuoteButton:
			startActivity(new Intent(getActivity(), NewQuoteScreen.class));
			break;
		case R.id.MainFragment_refreshButton:
			addQuotesOnScreen();
			break;
		}
	}

}