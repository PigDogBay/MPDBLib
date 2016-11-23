package com.pigdogbay.lib.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Mark on 02/09/2016.
 * Based on code from
 *
 * https://developer.android.com/training/printing/html-docs.html
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class HtmlPrinting
{
    private final Activity activity;
    //Temporary reference
    private WebView mWebView;

    public HtmlPrinting(Activity activity){

        this.activity = activity;
    }

    public void print(String htmlDocument){
        // Create a WebView object specifically for printing
        WebView webView = new WebView(activity);
        webView.setWebViewClient(new WebViewClient() {

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                createWebPrintJob(view);
                mWebView = null;
            }
        });

        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);

        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView;


    }
    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) activity
                .getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            printAdapter = webView.createPrintDocumentAdapter();
        } else {
            printAdapter = webView.createPrintDocumentAdapter("Report");
        }

        // Create a print job with name and adapter instance
        String jobName = "Document";
        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());

        // Save the job object for later status checking
//        mPrintJobs.add(printJob);
    }

}
