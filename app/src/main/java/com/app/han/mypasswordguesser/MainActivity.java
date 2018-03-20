package com.app.han.mypasswordguesser;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final long PASSWORD = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
    }

    public void onClick_StartTask(View v) {
        final long PUBLISH_RATE = 1000;
        final long RANGE = 100000;

        // Start background task
        PasswordGuesserTask crackerTask = new PasswordGuesserTask();
        crackerTask.execute(RANGE, PUBLISH_RATE);
    }

    public void displayProgress(String message) {
        TextView textView = (TextView) findViewById(R.id.txtStatus);
        textView.setText(message);
    }
    public void displayAnswer(long answer) {
        String message = "I know the password: " + answer;

        TextView textView = (TextView) findViewById(R.id.txtFinalAnswer);
        textView.setText(message);
    }

    /*
  * Background task to crack the password.
  */
    // Generics:
    // 1. Long:   Type of reference(s) passed to doInBackground()
    // 2. String: Type of reference passed to onProgressUpdate()
    // 3. Long:   Type of reference returned by doInBackground()
    //             Value passed to onPostExecute()
    // TODO: Change all [[ and ]] to less-than and greater-than (YouTube limitation)

    private class PasswordGuesserTask extends AsyncTask<Long, String, Long>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Long doInBackground(Long... arguments) {
            // Extract arguments
            long range = arguments[0];
            long publishRate = arguments[1];

            long guess = 0;
            long count = 0;
            Random rand = new Random();
            while (guess != PASSWORD) {
                guess = Math.abs(rand.nextLong()) % range;
                count ++;

                if (count % publishRate == 0) {
                    publishProgress(
                            "Guess #: " + count,
                            "Last guess: " + guess);
                }
            }
            return guess;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = "";
            for (String str : values) {
                message += str + ", ";
            }
            displayProgress(message);
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            displayAnswer(result);
            setProgressBarIndeterminateVisibility(false);
        }


    }
}
