package at.ac.tuwien.mns.group3.mnsg3e3.util;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Activity myContext;

    public ExceptionHandler(Activity myContext) {
        this.myContext = myContext;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (throwable instanceof net.sqlcipher.database.SQLiteException) {
            Handler handler = new Handler(myContext.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(myContext, "Invalid Password. Closing App!", Toast.LENGTH_LONG).show();
                    myContext.finish();
                }
            });
        }
        throw new RuntimeException(throwable);

    }
}
