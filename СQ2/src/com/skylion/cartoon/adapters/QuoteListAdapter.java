package com.skylion.cartoon.adapters;

import java.util.ArrayList;
import java.util.List;

import com.skylion.cartoon.data.Quote;
import com.skylion.cartoon.util.PreferencesLoader;
import com.skylion.cartoon.util.providers.QuoteRatingsProvider;
import com.skylion.cartoon.R;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class QuoteListAdapter extends BaseAdapter {

	private Context context;
	private View view;

	private LayoutInflater inflater = null;

	private List<String> titlesList;
	private List<Quote> quotesList;

	public QuoteListAdapter(Context context, ArrayList<String> titlesList, ArrayList<Quote> quotesList) {
		this.context = context;
		this.titlesList = titlesList;
		this.quotesList = quotesList;
		if (context != null)
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return quotesList.size();
	}

	@Override
	public Object getItem(int position) {
		return quotesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		view = convertView;
		if (view == null)
			view = inflater.inflate(R.layout.quote_item, null);

		TextView quoteText = (TextView) view.findViewById(R.id.DailyQuote_quoteText);
		TextView showTitle = (TextView) view.findViewById(R.id.DailyQuote_showTitle);
		RatingBar ratingBar = (RatingBar) view.findViewById(R.id.DailyQuote_ratingBar);
		ImageButton shareButton = (ImageButton) view.findViewById(R.id.DailyQuote_shareButton);
		LinearLayout contentLayout = (LinearLayout) view.findViewById(R.id.DailyQuote_contentLayout);

		if (PreferencesLoader.getInstance().getTheme() == 0) {
			contentLayout.setBackgroundResource(R.drawable.quote_border_pink);
		} else if (PreferencesLoader.getInstance().getTheme() == 1) {
			contentLayout.setBackgroundResource(R.drawable.quote_border_white);
		} else {
			contentLayout.setBackgroundResource(R.drawable.quote_border_orange);
		}

		final Quote quote = quotesList.get(position);
		quoteText.setText(quote.getText());
		showTitle.setText(titlesList.get(position));
		ratingBar.setRating(QuoteRatingsProvider.getFloatRating(quote.getRating()));
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				quote.setRating(QuoteRatingsProvider.getRatingFromFloat(rating));
				QuoteRatingsProvider.setQuoteRating(quote.getText(), quote.getRating());
			}
		});
		shareButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				shareQuote(context, titlesList.get(position), quote.getText());
			}
		});

		return view;
	}

	private static void shareQuote(Context context, String title, String quote) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = quote + " " + context.getString(R.string.share_text) + " - " + title;
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.share_with)));
	}
}
