package pk.edu.pucit.assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> level = new ArrayList<>();
    ArrayList<String> info = new ArrayList<>();
    ArrayList<String> imagid= new ArrayList<>();
    ArrayList<String> bookUrl= new ArrayList<>();

    //String  json=loadJSONFromAsset();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rv;
        rv = findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        try {
            // get JSONObject from JSON file
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            // fetch JSONArray named users
            JSONArray userArray = obj.getJSONArray("books");

            // implement for loop for getting users list data
            for (int i = 0; i < userArray.length(); i++) {
                // create a JSONObject for fetching single user data
                JSONObject userDetail = userArray.getJSONObject(i);
                // fetch email and name and store it in arraylist
                title.add(userDetail.getString("title"));
                //Log.e("title",title.get(i));
                level.add(userDetail.getString("level"));
                info.add(userDetail.getString("info"));
                imagid.add(userDetail.getString("cover"));
                bookUrl.add(userDetail.getString("url"));
                // create a object for getting contact data from JSONObject
                //JSONObject contact = userDetail.getJSONObject("contact");
                // fetch mobile number and store it in arraylist
               // mobileNumbers.add(contact.getString("mobile"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Integer[] imageids = {R.drawable.a, R.drawable.b};
        RVAdapter adapter = new RVAdapter(this, title, level,info, imagid,bookUrl);

        rv.setAdapter(adapter);
    }
    public String loadJSONFromAsset() {
        String json = "";
        try {

            InputStream is = getResources().openRawResource(R.raw.data);
            byte[] data = new byte[is.available()];

            //loop will read data into byte[] data
            while (is.read(data)!=-1){
                //empty as nothing needs to be done here
            }
            json = new String(data);
            Log.i("data.json","length => "+ json.length());
            Log.i ("data.json","last 100 chars => " + json.substring(json.length()-100));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

//    public class DownloadFilesTask extends AsyncTask<Object, Integer, Bitmap> {
//        protected Long doInBackground(Object object) {
//
//            //do the networking and parse json here
//
//            try {
//                JSONObject obj = new JSONObject(loadJSONFromAsset());
//                // fetch JSONArray named users
//                JSONArray userArray = obj.getJSONArray("books");
//                for (int i = 0; i < userArray.length(); i++) {
//                    // create a JSONObject for fetching single user data
//                    JSONObject userDetail = userArray.getJSONObject(i);
//                    String getImage = userDetail.getString("cover");
//                    Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(getImage).getContent());
//                }
//                //return bitmap;
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }catch(JSONException e)
//            {
//
//            }
//            return null;
//        }
//
//        protected void onPostExecute(Bitmap bmp) {
//
//            //set the bitmap to image here
//
//            ImageView one = (ImageView)findViewById(R.id.one);
//            one.setImageBitmap(bmp);
//        }
//    }
}
