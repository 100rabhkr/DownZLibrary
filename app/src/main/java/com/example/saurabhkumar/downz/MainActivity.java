package com.example.saurabhkumar.downz;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.downzlibrary.DownZ;
import com.example.downzlibrary.ListnerInterface.HttpListener;
import com.example.downzlibrary.Utilities.CacheManager;
import com.example.saurabhkumar.downz.CustomClasses.MasonryAdapter;
import com.example.saurabhkumar.downz.CustomClasses.SpacesItemDecoration;
import com.example.saurabhkumar.downz.CustomClasses.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    MasonryAdapter adapter;
    CacheManager<JSONArray> jsonArrayCacheManager;
    String Url;
    boolean refresh = false;
    StaggeredGridLayoutManager mLayoutmanager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler = new Handler();
    private final Runnable refreshing = new Runnable() {
        public void run() {
            try {
                if (isRefreshing()) {
                    // RE-Run after 1 Second
                    handler.postDelayed(this, 1000);
                } else {
                    // Stop the animation once we are done fetching data.
                    swipeRefreshLayout.setRefreshing(false);
                    /**
                     * You can add code to update your list with the new data.
                     **/
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Url = String.valueOf(R.string.URL);
        jsonArrayCacheManager = new CacheManager<>(40 * 1024 * 1024); // 40mb
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mToolbar = findViewById(R.id.toolbar);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("DownZtrest");
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewUser);
        mLayoutmanager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(mLayoutmanager);
        //mRecyclerView.setAdapter(adapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        adapter = new MasonryAdapter(this);
        populatelayout();

        // Swipe to refresh

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

            }
        });


    }

    public void populatelayout() {
        DownZ
                .from(MainActivity.this)
                .load(DownZ.Method.GET, "http://pastebin.com/raw/wgkJgazE")
                .asJsonArray()
                .setCacheManager(jsonArrayCacheManager)
                .setCallback(new HttpListener<JSONArray>() {
                    @Override
                    public void onRequest() {
                        startSync();

                    }

                    @Override
                    public void onResponse(JSONArray data) {


                        if (data != null) {
                            int lengthofdata = data.length();

                            List<User> UserList = new ArrayList<User>();

                            for (int i = 0; i <= lengthofdata; i++) {
                                try {
                                    //Toast.makeText(MainActivity.this,"here",Toast.LENGTH_SHORT).show();
                                    JSONObject jUser = data.getJSONObject(i);
                                    JSONObject ObjectforNames = jUser.getJSONObject("user");
                                    JSONArray CategoryArray = jUser.getJSONArray("categories");
                                    JSONObject ObjectforUrls = jUser.getJSONObject("urls");
                                    JSONObject ObjectforProfilePic = ObjectforNames.getJSONObject("profile_image");
                                    String nameofuser = ObjectforNames.get("name").toString();
                                    String username = "@" + ObjectforNames.get("username").toString();
                                    List<String> categories = new ArrayList<String>();
                                    int lengthofcategories = CategoryArray.length();
                                    for (int j = 0; j <= lengthofcategories; j++) {
                                        try {
                                            JSONObject Category = CategoryArray.getJSONObject(j);
                                            String CategoryName = Category.getString("title");
                                            categories.add(CategoryName);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    String uploadedphotourl = ObjectforUrls.getString("regular");
                                    String urltosend = ObjectforUrls.getString("full");
                                    String profilepicurl = ObjectforProfilePic.getString("large");
                                    boolean islikedbyuser = jUser.getBoolean("liked_by_user");
                                    int numberOfLikes = jUser.getInt("likes");
                                    User newUser = new User(nameofuser, profilepicurl, uploadedphotourl, islikedbyuser, username, numberOfLikes, categories, urltosend);
                                    UserList.add(newUser);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            adapter.setUsers(UserList);
                            mRecyclerView.setAdapter(adapter);
                            Snackbar.make(findViewById(android.R.id.content), "Click on the images to know more", Snackbar.LENGTH_LONG).show();
                            refresh = false;
                        }
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                        refresh = false;
                    }

                    @Override
                    public void onCancel() {
                        refresh = false;
                    }
                });
    }

    public void startSync() {
        refresh = true;
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
        handler.post(refreshing);
    }

    boolean isRefreshing() {
        return refresh;
    }
}
