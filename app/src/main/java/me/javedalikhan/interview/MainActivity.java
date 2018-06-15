package me.javedalikhan.interview;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

   private ListView lv;

   ImageView image;

    private String TAG = MainActivity.class.getSimpleName();

    private static String url = "https://learncodeonline.in/api/android/datastructure";

    ArrayList<HashMap<String, String>> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionList = new ArrayList<>();

        lv = findViewById(R.id.list);

        image = findViewById(R.id.image_view);

        image.setImageResource(R.drawable.banner);

        new GetQuestions().execute();

        }


        public void url(View view){
        Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.learncodeonline.in"));
                startActivity(browseIntent);
        }

        private class GetQuestions extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                HttpHandler sh = new HttpHandler();

                String jsonStr = sh.makeServiceCall(url);

                Log.e(TAG, "Response from url: " + jsonStr);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        JSONArray questions = jsonObj.getJSONArray("questions");

                        for (int i = 0; i < questions.length(); i++) {
                            JSONObject c = questions.getJSONObject(i);

                            String name = c.getString("question");
                            String ans = c.getString("Answer");

                            HashMap<String, String> contact = new HashMap<>();

                            contact.put("question", name);
                            contact.put("Answer",ans);

                            questionList.add(contact);

                        }
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        });
                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                ListAdapter adapter = new SimpleAdapter(
                        MainActivity.this, questionList,
                        R.layout.list_item, new String[]{"question","Answer"}, new int[]{R.id.question, R.id.answer});

                lv.setAdapter(adapter);

            }
        }
    }
