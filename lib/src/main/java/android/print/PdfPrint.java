package android.print;

import android.os.Build;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import androidx.annotation.RequiresApi;
import android.util.Log;

import java.io.File;

/**
 * Based on code from
 * http://www.annalytics.co.uk/android/pdf/2017/04/06/Save-PDF-From-An-Android-WebView/
 *
 * Note LayoutResultCallback is package private to android.print
 *
 * Created by Mark on 19/01/2018.
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class PdfPrint {

    private static final String TAG = PdfPrint.class.getSimpleName();
    private final PrintAttributes printAttributes;

    public PdfPrint(PrintAttributes printAttributes) {
        this.printAttributes = printAttributes;
    }

    public void print(PrintDocumentAdapter printAdapter, final File file) {
        printAdapter.onLayout(null, printAttributes, null, new PrintDocumentAdapter.LayoutResultCallback() {
            @Override
            public void onLayoutFinished(PrintDocumentInfo info, boolean changed) {
                printAdapter.onWrite(new PageRange[]{PageRange.ALL_PAGES}, getOutputFile(file), new CancellationSignal(), new PrintDocumentAdapter.WriteResultCallback() {
                    @Override
                    public void onWriteFinished(PageRange[] pages) {
                        super.onWriteFinished(pages);
                    }
                });
            }
        }, null);
    }

    private ParcelFileDescriptor getOutputFile(File file) {
        try {
            if (file.createNewFile()) {
                return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to open ParcelFileDescriptor", e);
        }
        return null;
    }
}