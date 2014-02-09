package com.skylion.cartoon.util.providers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.skylion.cartoon.data.Quote;
import com.skylion.cartoon.util.controllers.FilesController;
import com.skylion.cartoon.R;
import android.annotation.SuppressLint;
import android.content.Context;

public class ShowsProvider {

    private static Map<Integer, Integer> listFiles;
    private static Map<Integer, Integer> listBackgrounds;

    public static ArrayList<Quote> getQuotesList(Context context, int showId) {
	ArrayList<Quote> quotes = new ArrayList<Quote>();
	ArrayList<String> texts = readQuoteFiles(context, listFiles.get(showId));
	for (int i = 0; i < texts.size(); i++) {
	    Quote quote = new Quote();
	    quote.setText(texts.get(i));
	    quote.setRating(QuoteRatingsProvider.getQuoteRating(quote.getText()));
	    quotes.add(quote);
	}
	return quotes;
    }

    public static Integer getShowBackgroundResourceId(int showId) {
	return listBackgrounds.get(showId);
    }

    @SuppressWarnings("finally")
    private static ArrayList<String> readQuoteFiles(Context context, Integer show) {
	ArrayList<String> result = new ArrayList<String>();
	try {
	    BufferedReader in = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(show)));

	    String line;
	    String buff = new String();
	    while ((line = in.readLine()) != null) {
		if (FilesController.process(line,"***")) {
		    result.add(buff);
		    buff = new String();
		} else
		    buff = buff + line + "\n";
	    }
	    in.close();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    return result;
	}
    }

    @SuppressLint("UseSparseArrays")
    public static void quotesCollectionInit() {
	listFiles = new HashMap<Integer, Integer>();
	listBackgrounds = new HashMap<Integer, Integer>();

	listFiles.put(1, R.raw.family_guy);
	listBackgrounds.put(1, R.drawable.fg);

	listFiles.put(2, R.raw.futurama);
	listBackgrounds.put(2, R.drawable.futurama);

	listFiles.put(3, R.raw.simpsons);
	listBackgrounds.put(3, R.drawable.simpsons);

	listFiles.put(4, R.raw.south_park);
	listBackgrounds.put(4, R.drawable.south_park);

	listFiles.put(5, R.raw.freeman);
	listBackgrounds.put(5, R.drawable.freeman);

	listFiles.put(6, R.raw.domovenok);
	listBackgrounds.put(6, R.drawable.cartoons_wall);

	listFiles.put(7, R.raw.gena);
	listBackgrounds.put(7, R.drawable.cartoons_wall);

	listFiles.put(8, R.raw.prosto);
	listBackgrounds.put(8, R.drawable.cartoons_wall);

	listFiles.put(9, R.raw.vinni);
	listBackgrounds.put(9, R.drawable.cartoons_wall);

	listFiles.put(10, R.raw.karlson);
	listBackgrounds.put(10, R.drawable.cartoons_wall);

	listFiles.put(11, R.raw.walle);
	listBackgrounds.put(11, R.drawable.movies_wall);

	listFiles.put(12, R.raw.ice_age);
	listBackgrounds.put(12, R.drawable.movies_wall);

	listFiles.put(13, R.raw.madagaskar);
	listBackgrounds.put(13, R.drawable.movies_wall);

	listFiles.put(14, R.raw.megamind);
	listBackgrounds.put(14, R.drawable.movies_wall);

	listFiles.put(15, R.raw.panda);
	listBackgrounds.put(15, R.drawable.movies_wall);

    }
}
