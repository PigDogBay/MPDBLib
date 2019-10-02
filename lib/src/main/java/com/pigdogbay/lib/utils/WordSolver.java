package com.pigdogbay.lib.utils;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

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
	static final int TABLE_MAX_COUNT_TO_RELOAD = 40;
	public static final WordListCallbackAbstractFactory.Null NULL_WLC_FACTORY = new WordListCallbackAbstractFactory.Null();
	public static String getWordURL(String word)
	{
		return "https://www.google.com/search?q=dictionary:"+word;
	}
	public static String getGoogleDefineURL(String word)
	{
		return "https://www.google.com/search?q=define:"+word;
	}
	public static String getMerriamWebsterURL(String word)
	{
		return "https://www.merriam-webster.com/dictionary/"+word;
	}
	public static String getMerriamWebsterThesaurusURL(String word)
	{
		return "https://www.merriam-webster.com/thesaurus/"+word;
	}
	public static String getCollinsUrl(String word){
		String processed = word.replace(" ", "-");
		return "https://www.collinsdictionary.com/dictionary/english/"+processed;
	}
	public static String getLexicoUrl(String word){
		return "https://www.lexico.com/en/definition/"+word;
	}

	public static String getDictionaryCom(String word)
	{
		return "https://www.dictionary.com/browse/"+word;
	}
	public static String getThesaurusCom(String word)
	{
		return "https://www.thesaurus.com/browse/"+word;
	}
	public static String getWikipediaURL(String word)
	{
		String processed = word.replace(" ", "_");
		return "https://en.wikipedia.org/wiki/"+processed;
	}
	public static String getWordGameDictionaryURL(String word)
	{
		return "https://www.wordgamedictionary.com/dictionary/word/"+word;
	}
	public static String getChambersUrl(String word){
		String processed = word.replace(" ", "+");
		return "https://chambers.co.uk/search/?query="+processed+"&title=21st";
	}
	public static String getWikionaryUrl(String word){
		String processed = word.replace(" ", "_");
		return "https://en.wiktionary.org/wiki/"+processed;
	}
	public static String getCambridgeUrl(String word){
		String processed = word.replace(" ", "-");
		return "https://dictionary.cambridge.org/dictionary/english/"+processed;
	}

	public void setResultsLimit(int resultsLimit) {
		this.resultsLimit = resultsLimit;
	}

	public void setShowSubanagrams(boolean show){
		this.wordSearch.setFindSubAnagrams(show);
	}

	public enum States
	{
		uninitialized, loading, ready, searching, finished, loadError
	}
	private int resultsLimit = 5000;
	private int resultsCount;

	private WordList wordList;
	private WordSearch wordSearch;
	private String query;
	public ObservableProperty<States> stateObservable;
	public ObservableProperty<String> matchObservable;
	public WordMatches wordMatches;
	public WordListCallbackAbstractFactory wordListCallbackFactory = NULL_WLC_FACTORY;

	public String getQuery()
	{
		return query;
	}
	public WordSolver() 
	{
		wordList = new WordList();
		wordSearch = new WordSearch(wordList);
		wordSearch.setFindSubAnagrams(true);
		query = "";
		wordMatches = new WordMatches();
		stateObservable = new ObservableProperty<>(States.uninitialized);
		matchObservable = new ObservableProperty<>("");
	}
	
	public void loadDictionary(Context context, int resourceId)
	{
		States state = stateObservable.getValue();
		switch (state){

			case uninitialized:
			case ready:
			case finished:
				this.new LoadTask(context, resourceId).execute();
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
		wordMatches.getMatches().clear();
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
		for (String result : this.wordMatches.getMatches()) {
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
			resultsCount++;
			if (resultsCount == resultsLimit){
				WordSolver.this.stop();
			}
		}
		
		@Override
		protected void onPreExecute() {
			WordSolver.this.stateObservable.setValue(States.searching);
		}
		@Override
		protected Void doInBackground(String... params) {
			resultsCount = 0;
			String processedQuery = query;
			processedQuery = WordSolver.this.wordSearch.preProcessQuery(processedQuery);
			WordSearch.SearchType searchType = WordSolver.this.wordSearch.getQueryType(processedQuery);
			processedQuery = WordSolver.this.wordSearch.postProcessQuery(processedQuery, searchType);
			wordMatches.newSearch(processedQuery,searchType);
			WordListCallback filterPipeline = WordSolver.this.wordListCallbackFactory.createChainedCallback(this);
			WordSolver.this.wordSearch.runQuery(processedQuery, searchType, filterPipeline);
			return null;
		}
		@Override
		protected void onProgressUpdate(String... values) {
			//The array will be used in the views array adapter
			//this means the array must be updated only from the UI thread
			wordMatches.getMatches().add(values[0]);
			//only update the UI for the first page or so of results
			if (wordMatches.getMatches().size()<=TABLE_MAX_COUNT_TO_RELOAD)
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
		
		final Context context;
		final int wordListId;
		boolean isLoadError = false;

		LoadTask(Context context, int wordListId)
		{
			this.context = context.getApplicationContext();
			this.wordListId = wordListId;
		}

		@Override
		protected void onPreExecute() {
			stateObservable.setValue(States.loading);
		}
		@Override
		protected Void doInBackground(Void... params) {
			try 
			{
				List<String> words = LineReader.Read(context, wordListId);
				wordList.SetWordList(words);
			} 
			catch (Exception e) 
			{
				isLoadError = true;
			}
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
