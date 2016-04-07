package com.secureme.transparentbackgroung;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class MainActivity extends AppCompatActivity {
    private ViewSwitcher viewSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new LoadViewTask().execute();


    }
    private class LoadViewTask extends AsyncTask<Void,Integer,Void>
    {

        private TextView tv_progress;
        private ProgressBar pb_progressBar;


        @Override
        protected void onPreExecute()
        {

            viewSwitcher = new ViewSwitcher(MainActivity.this);

            viewSwitcher.addView(ViewSwitcher.inflate(MainActivity.this, R.layout.activity_main, null));


            tv_progress = (TextView) viewSwitcher.findViewById(R.id.tv_progress);
            pb_progressBar = (ProgressBar) viewSwitcher.findViewById(R.id.pb_progressbar);

            pb_progressBar.setMax(100);


            setContentView(viewSwitcher);
        }

        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params)
        {

            try
            {
                //Get the current thread's token
                synchronized (this)
                {
                    //Initialize an integer (that will act as a counter) to zero
                    int counter = 0;
                    //While the counter is smaller than four
                    while(counter <= 4)
                    {
                        //Wait 850 milliseconds
                        this.wait(850);
                        //Increment the counter
                        counter++;
                        //Set the current progress.
                        //This value is going to be passed to the onProgressUpdate() method.
                        publishProgress(counter*25);
                    }
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values)
        {

            if(values[0] <= 100)
            {
                tv_progress.setText("Progress: " + Integer.toString(values[0]) + "%");
                pb_progressBar.setProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Void result)
        {

            viewSwitcher.addView(ViewSwitcher.inflate(MainActivity.this, R.layout.activity_main2, null));

            viewSwitcher.showNext();


        }
    }

    //Override the default back key behavior
    @Override
    public void onBackPressed()
    {
        if(viewSwitcher.getDisplayedChild() == 0)
        {

            return;
        }
        else
        {

            super.onBackPressed();
        }
    }

}
