package magdalena.pl.asyntask;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.content.LocalBroadcastManager;

public class ProgressBarService extends Service {

    private int interval;
    private Intent intent;
    private LocalBroadcastManager broadcastManager;
    public final static String PERCENTAGE_STAMP = "percent";
    public final static String DOWNLOAD_ACTION ="magdalena.pl.asyntask.download";

    public ProgressBarService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            interval = intent.getIntExtra("interval", 0);
        }
        new UpdateProgressBar().execute();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class UpdateProgressBar extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            intent = new Intent(DOWNLOAD_ACTION);
            intent.putExtra(PERCENTAGE_STAMP, values[0]);
            broadcastManager.sendBroadcast(intent);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            for (int i = interval; i >= 0; i--) {
                publishProgress(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            new UpdateProgressBar().execute();
        }
    }

}
