package com.example.asynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView textStatus, textPercent;
    ProgressBar pBar;
    Button start, cancel;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textStatus = findViewById(R.id.statusText);
        textPercent = findViewById(R.id.percent);
        pBar = findViewById(R.id.pBar);
        start = findViewById(R.id.startButton);
        cancel = findViewById(R.id.cancelButton);
        start.setOnClickListener(view -> new MyAsyncTask().execute());
        cancel.setOnClickListener(view -> {
            new MyAsyncTask().cancel(true);
            textStatus.setText(R.string.waiting);
        });
    }



    class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... params) throws NullPointerException {
            int progress = pBar.getMax();
            for (int i = 0; i < progress; i++) {

                try {
                    long millis = Long.parseLong(editText.getText().toString());
                    Log.d("TAG", "try " + millis);
                    Thread.sleep(millis / 100);
                } catch (InterruptedException | NullPointerException e) {
                    e.printStackTrace();
                }

                if (isCancelled()) {
                    Log.d("TAG", "isCancelled");
                    break;
                }
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            TextView textStatus = findViewById(R.id.statusText);
            TextView textPercent = findViewById(R.id.percent);
            ProgressBar pBar = findViewById(R.id.pBar);
            String percent = (String) getResources().getText(R.string.percent);
            String number = String.valueOf(progress);
            String res = number + " " + percent;
            pBar.setProgress(progress);
            textPercent.setText(res);
            textStatus.setText(getResources().getText(R.string.running));

            if (progress == 100){
                editText.setVisibility(View.VISIBLE);
                res = "0 %";
                textStatus.setText(getResources().getText(R.string.finished));
                textPercent.setText(res);
                pBar.setProgress(0);
            }

            Log.d("TAG", "onProgressUpdate " + progress);
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("TAG", "onPostExecute");
        }

        @Override
        protected void onPreExecute() {
            editText.setVisibility(View.GONE);
            editText = findViewById(R.id.editText);
            Log.d("TAG", "onPreExecute");
            super.onPreExecute();
        }

        @Override
        protected void onCancelled(Void unused) {
            Log.d("TAG", "onCancelled");
            super.onCancelled(unused);
        }
    }


}