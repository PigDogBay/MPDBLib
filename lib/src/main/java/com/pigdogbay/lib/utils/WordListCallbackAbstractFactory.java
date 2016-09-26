package com.pigdogbay.lib.utils;

/**
 * Created by Mark on 26/09/2016.
 * Interface for creating a chain of word filters
 */
public interface WordListCallbackAbstractFactory {
    WordListCallback createChainedCallback(WordListCallback lastCallback);

    public class Null implements WordListCallbackAbstractFactory {
        @Override
        public WordListCallback createChainedCallback(WordListCallback lastCallback) {
            return lastCallback;
        }
    }
}
