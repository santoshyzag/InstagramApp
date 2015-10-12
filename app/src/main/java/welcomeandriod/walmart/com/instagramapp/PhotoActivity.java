package welcomeandriod.walmart.com.instagramapp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotoActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "191b28d7eed8413f8f7da1904f9aceab";
    public ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    ListView lvPhotos;
    // Adding swipeRefresh layout
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram);

        photos = new ArrayList<>();
        aPhotos = new InstagramPhotosAdapter(PhotoActivity.this, photos);
        lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
        fetchPopularPhotos();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                photos = new ArrayList<>();
                aPhotos = new InstagramPhotosAdapter(PhotoActivity.this, photos);
                lvPhotos = (ListView) findViewById(R.id.lvPhotos);
                lvPhotos.setAdapter(aPhotos);
                fetchTimelineAsync(0);

            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


 }

    public void fetchTimelineAsync(int page) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        aPhotos.clear();
        client.get(url, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJson = null;

                try {

                    photosJson = response.getJSONArray("data");

                    for (int i = 0; i < photosJson.length(); i++) {

                        JSONObject photoJson = photosJson.getJSONObject(i);

                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJson.getJSONObject("user").getString("username");
                        photo.caption = photoJson.getJSONObject("caption").getString("text");
                        photo.imageurl = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likescount = photoJson.getJSONObject("likes").getInt("count");

                        photos.add(photo);


                    }


                } catch (JSONException e) {

                    e.printStackTrace();

                }
                swipeContainer.setRefreshing(false);
                aPhotos.notifyDataSetChanged();

            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });
    }


    public void fetchPopularPhotos(){

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJson = null;

                try {

                    photosJson = response.getJSONArray("data");

                    for (int i = 0; i < photosJson.length(); i++) {

                        JSONObject photoJson = photosJson.getJSONObject(i);

                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJson.getJSONObject("user").getString("username");
                        photo.caption = photoJson.getJSONObject("caption").getString("text");
                        photo.imageurl = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likescount = photoJson.getJSONObject("likes").getInt("count");

                        photos.add(photo);


                    }


                } catch (JSONException e) {

                    e.printStackTrace();

                }
                aPhotos.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_instagram, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





}
