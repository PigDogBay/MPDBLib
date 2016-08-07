package com.pigdogbay.lib.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

/**
 * 
 * Based on the equivalent iOS code
 * 
 * This class handles the data and logic for performing a word pattern search.
 * Anagram Solver and Crossword Solver previously used presenters to handle the logic, 
 * this class now can used instead of the presenters*.
 * 
 * The class uses a state machine and listeners (such as fragments) can register themselves
 * to listen for state changes. This design allows the UI to be independent of the logic, the UI
 * only needs to update itself to reflect each state.
 * 
 * The code is multithreaded, long winded tasks such as loading the dictionary and searches are
 * performed in an AsyncTask. State changes and the match found callback are fired on the UI thread
 *
 * *The old presenters contained too much domain data and logic, had the same life cycle
 * as the views, mirrored their views too closely and MVP on top of MVC is just adding extra boiler plate
 * and unnecessary abstraction. A multithreaded state machine with call backs, is easily tested and more reusable.  
 * Views can now pop in and out of existence (eg rotations) without affecting a search running in the background.
 * 
 */
public class WordSolver
{
	public static final int TABLE_MAX_COUNT_TO_RELOAD = 40;
	public static final int DEFAULT_RESULTS_LIMIT = 500;
	public static String getWordURL(String word)
	{
		word = removeMissingLetters(word);
		return "https://www.google.com/search?q=define:"+word;
	}
	public static String removeMissingLetters(String word){
		//Removes formatting such as missing letters
		int index = word.indexOf(" (");
		return  index==-1 ? word : word.substring(0,index);
	}

	public enum States
	{
		uninitialized, loading, ready, searching, finished, loadError
	}
	private WordList wordList;
	private WordSearch wordSearch;
	private String query;
	public List<String> matches;
	public ObservableProperty<States> stateObservable;
	public ObservableProperty<String> matchObservable;

	public String getQuery()
	{
		return query;
	}
	public WordSolver() 
	{
		wordList = new WordList();
		wordList.SetResultLimit(DEFAULT_RESULTS_LIMIT);
		wordSearch = new WordSearch(wordList);
		wordSearch.setFindSubAnagrams(true);
		query = "";
		matches = new ArrayList<>();
		stateObservable = new ObservableProperty<>(States.uninitialized);
		matchObservable = new ObservableProperty<>("");
	}
	
	public void loadDictionary(Context context, int resourceId)
	{
		loadDictionary(context, resourceId, -1);
	}
	public void loadDictionary(Context context, int stdId, int proId)
	{
		States state = stateObservable.getValue();
		switch (state){

			case uninitialized:
			case ready:
			case finished:
				this.new LoadTask(context, stdId, proId).execute();
				break;
			default:
				break;
		}

	}
	public boolean setAndValidateQuery(String rawQuery)
	{
		this.query = this.wordSearch.clean(rawQuery);
		return query.length()>0;
	}
	public void prepareToSearch()
	{
		matches.clear();
	}
	public void search()
	{
		this.new SearchTask().execute("");
	}
	public void stop()
	{
		this.wordList.stop();
	}
	public boolean isReady()
	{
		if (States.finished == stateObservable.getValue())
		{
			stateObservable.setValueWithoutNotification(States.ready);
		}
		return States.ready == stateObservable.getValue();
	}
	public String Share(String title)
	{
		StringBuilder sbuff = new StringBuilder();
		sbuff.append("-").append(title).append("-\n\nQuery:\n");
		sbuff.append(query);
		sbuff.append("\n\nMatches:\n");
		for (String result : this.matches) {
			sbuff.append(result);
			sbuff.append('\n');
		}
		return sbuff.toString();
	}

	private class SearchTask extends AsyncTask<String, String, Void> implements WordListCallback
	{
		/* 
		 * @see com.pigdogbay.lib.utils.WordListCallback#Update(java.lang.String)
		 */
		@Override
		public void Update(String result) {
			publishProgress(result);
		}
		
		@Override
		protected void onPreExecute() {
			WordSolver.this.stateObservable.setValue(States.searching);
		}
		@Override
		protected Void doInBackground(String... params) {
			String processedQuery = query;
			processedQuery = WordSolver.this.wordSearch.preProcessQuery(processedQuery);
			WordSearch.SearchType searchType = WordSolver.this.wordSearch.getQueryType(processedQuery);
			processedQuery = WordSolver.this.wordSearch.postProcessQuery(processedQuery, searchType);
			WordSolver.this.wordSearch.runQuery(processedQuery, searchType, this);
			return null;
		}
		@Override
		protected void onProgressUpdate(String... values) {
			//The array will be used in the views array adapter
			//this means the array must be updated only from the UI thread
			matches.add(values[0]);
			//only update the UI for the first page or so of results
			if (matches.size()<=TABLE_MAX_COUNT_TO_RELOAD)
			{
				matchObservable.setValue(values[0]);
			}
		}
		@Override
		protected void onPostExecute(Void result) {
			WordSolver.this.stateObservable.setValue(States.finished);
		}
	}
	/*
	 * Dictionary takes around 1000ms/2300ms to load on Nexus 7,
	 * 2600ms/8000ms on ACE phone. (Measured on 11 Sept 2013 with prior GC)
	 * Need to load this on a worker thread in the background to
	 * avoid the application freezing when starting up.
	 */
	private class LoadTask extends AsyncTask<Void, Void, Void> {
		
		Context context;
		int standandWordListId, proWordListId;
		boolean isLoadError = false;

		public LoadTask(Context context, int stdId, int proId)
		{
			this.context = context;
			this.standandWordListId = stdId;
			this.proWordListId = proId;
		}

		@Override
		protected void onPreExecute() {
			stateObservable.setValue(States.loading);
		}
		@Override
		protected Void doInBackground(Void... params) {
			try 
			{
				List<String> words = new ArrayList<>();
				words = LineReader.Read(context, standandWordListId);
				if (proWordListId!=-1){
					List<String> proWords = LineReader.Read(context, proWordListId);
					words.addAll(proWords);
					Collections.sort(words, new StringUtils.sizeThenAtoZComparator());
				}
				wordList.SetWordList(words);
			} 
			catch (Exception e) 
			{
				isLoadError = true;
			}
			context=null;
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (isLoadError)
			{
				stateObservable.setValue(States.loadError);
			}
			else
			{
				stateObservable.setValue(States.ready);
			}
		}
	}
}
