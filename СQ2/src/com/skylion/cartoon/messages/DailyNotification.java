package com.skylion.cartoon.messages;

import com.skylion.cartoon.DailyScreen;
import com.skylion.cartoon.constants.ConstantsFacade;
import com.skylion.cartoon.data.DailyQuote;
import com.skylion.cartoon.util.Rating;
import com.skylion.cartoon.util.providers.QuoteViewsProvider;
import com.skylion.cartoons.R;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

public class DailyNotification {

	public static void show(Context context) {
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent dailyQuoteIntent = new Intent(context, DailyScreen.class);
		dailyQuoteIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		DailyQuote dailyQuote = QuoteViewsProvider.getDailyQuote();
		dailyQuoteIntent.putExtra(ConstantsFacade.QUOTE_TITLE, dailyQuote.getShowTitle());
		dailyQuoteIntent.putExtra(ConstantsFacade.QUOTE_TEXT, dailyQuote.getText());
		Rating rating = dailyQuote.getRating();
		if (rating == null) {
			dailyQuoteIntent.putExtra(ConstantsFacade.QUOTE_RATING, Rating.NONE.toString());
		} else {
			dailyQuoteIntent.putExtra(ConstantsFacade.QUOTE_RATING, rating.toString());
		}

		PendingIntent pIntent = PendingIntent.getActivity(context, ConstantsFacade.NOTIFICATION_ID, dailyQuoteIntent, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		NotificationCompat.Builder mBuilder = null;

		mBuilder = new NotificationCompat.Builder(context).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).setContentIntent(pIntent)
				.setSmallIcon(R.drawable.ic_launcher).setContentTitle(context.getString(R.string.daily_screen_title) + ": " + dailyQuote.getShowTitle())
				.setContentText(dailyQuote.getText()).setAutoCancel(true);
		notificationManager.notify(ConstantsFacade.NOTIFICATION_ID, mBuilder.build());
	}

}
