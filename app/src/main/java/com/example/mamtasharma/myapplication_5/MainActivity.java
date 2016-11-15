package com.example.mamtasharma.myapplication_5;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.service.quicksettings.Tile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.parser.Tag;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button btnHit;
    TextView txtJson,Head;
    ProgressDialog pd;
    String line = "";
    String s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHit = (Button) findViewById(R.id.button);
        txtJson = (TextView) findViewById(R.id.textView);
        Head=(TextView)findViewById(R.id.abc);
        if(savedInstanceState!=null)
        {
            line=savedInstanceState.getString("Result");
            s=savedInstanceState.getString("Heading");
            txtJson.setText(line);
            Head.setText(s);
        }

        btnHit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new JsonTask().execute("https://www.iiitd.ac.in/about");

            }
        });


    }



    private class JsonTask extends AsyncTask<String, String, String> {


        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();



                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();


                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }
                line=buffer.toString();


                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }


            s=Jsoup.parse(result).text();
            Head.setText(s);
            txtJson.setText(result);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.d("My activity","inside saved instance function");
        savedInstanceState.putString("Result",line);
        savedInstanceState.putString("Heading",s);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}