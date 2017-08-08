package com.example.saurabhkumar.downz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.downzlibrary.DataTypes.BitMap;
import com.example.downzlibrary.DataTypes.Type;
import com.example.downzlibrary.DownZ;
import com.example.downzlibrary.ListnerInterface.HttpListener;
import com.example.downzlibrary.Utilities.CacheManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static android.R.attr.bitmap;

public class UserActivity extends AppCompatActivity {
    Toolbar mToolbar;
    String UrlforUpload;
    String UrlforProfilepic;
    String NameofUser;
    String UserName;
    String Categories;
    String NumberOfLikes;
    ImageView UploadedImage;
    de.hdodenhof.circleimageview.CircleImageView ProfilePic;
    TextView NumberofLikesTextView;
    TextView Tags;
    TextView NameofUserTextView;
    TextView UserNameTextView;
    int ImageLoadflag;
    Type<Bitmap> Upload;
    BitMap bitmaptosend;
    BitMap ProfilePicBitmap;
    CacheManager<Bitmap> bitmapCacheManager;
    ProgressBar progressBar;
    int fabFlag;
    LinearLayout FabLayout;
    FloatingActionButton MainButton;
    RelativeLayout Overlay;
    Animation animforoverlay;
    Animation animforoverlayGone;
    Animation animforVisible;
    Animation animforFabRotate;
    Animation animforInvisible;
    Boolean canSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mToolbar = findViewById(R.id.toolbar);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        /* To get all the values from previous activity */

        Bundle extras = getIntent().getExtras();
        UrlforUpload = extras.getString("Upload_Url");
        UrlforProfilepic = extras.getString("Profile_Pic");
        NameofUser = "by " + extras.getString("Name_Of_User");
        UserName = extras.getString("User_Name");
        Categories = extras.getString("Categories");
        NumberOfLikes = extras.getString("No_Of_Likes");
        bitmapCacheManager = new CacheManager<>(40 * 1024 * 1024);
        /* Setting Up Layout */
        UploadedImage = findViewById(R.id.Upload);
        UploadedImage.setDrawingCacheEnabled(true);
        UploadedImage.buildDrawingCache();
        ProfilePic = findViewById(R.id.profile_image);
        Tags = findViewById(R.id.Tags);
        NumberofLikesTextView = findViewById(R.id.NumOfLikes);
        UserNameTextView = findViewById(R.id.UserNameinNew);
        NameofUserTextView = findViewById(R.id.NameOfUserinNew);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        canSend = false;
        // layout containing all FAB buttons

        FabLayout = findViewById(R.id.OptionsLayout);
        MainButton = findViewById(R.id.editImage);
        Overlay = findViewById(R.id.overlay);
        FloatingActionButton Loadimage = findViewById(R.id.LoadImage);
        FloatingActionButton CancelLoad = findViewById(R.id.cancelLoad);
        FloatingActionButton RemoveImage = findViewById(R.id.clearImage);

        //Setting Visiblity
        FabLayout.setVisibility(View.GONE);
        MainButton.setImageResource(R.drawable.ic_edit);
        Overlay.setVisibility(View.GONE);

        //Setting up animation
        animforoverlay = AnimationUtils.loadAnimation(UserActivity.this,
                R.anim.fade_in);
        animforoverlayGone = AnimationUtils.loadAnimation(UserActivity.this,
                R.anim.fade_out);
        animforVisible = AnimationUtils.loadAnimation(UserActivity.this,
                R.anim.slide_in_right);
        animforInvisible = AnimationUtils.loadAnimation(UserActivity.this,
                R.anim.slide_out_left);
        animforFabRotate = AnimationUtils.loadAnimation(UserActivity.this,
                R.anim.rotate);
        //Setting Up Content
        Tags.setText(Categories);
        NameofUserTextView.setText(NameofUser);
        UserNameTextView.setText(UserName);
        NumberofLikesTextView.setText(NumberOfLikes);

        //Loading Uploaded Image
        ImageLoadFunction();

        //Loading Profile Picture

        ProfilePicBitmap = DownZ
                .from(UserActivity.this)
                .load(DownZ.Method.GET, UrlforProfilepic)
                .asBitmap()
                .setCacheManager(bitmapCacheManager)
                .setCallback(new HttpListener<Bitmap>() {
                    @Override
                    public void onRequest() {

                    }

                    @Override
                    public void onResponse(Bitmap data) {
                        ProfilePic.setImageBitmap(data);
                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
        MainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabFlag == 0) {
                    DisplayFab();
                } else {
                    RemoveFab();
                }
            }
        });
        Loadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImageLoadflag != 1) {
                    ImageLoadFunction();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Image Already Exists", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        CancelLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Upload != null) {
                    Upload.cancel();
                    Snackbar.make(findViewById(android.R.id.content), "Cancelled", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        RemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageLoadflag = 0;
                UploadedImage.setImageResource(0);
            }
        });


    }

    public void ImageLoadFunction() {
        ImageLoadflag = 1;
        Upload = DownZ
                .from(UserActivity.this)
                .load(DownZ.Method.GET, UrlforUpload)
                .asBitmap()
                .setCacheManager(bitmapCacheManager)
                .setCallback(new HttpListener<Bitmap>() {
                    @Override
                    public void onRequest() {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(Bitmap data) {
                        progressBar.setVisibility(View.GONE);
                        UploadedImage.setImageBitmap(data);
                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onCancel() {

                    }
                });


    }

    public void DisplayFab() {
        /*function to show Fab Buttons*/
        fabFlag = 1;
        FabLayout.setVisibility(View.VISIBLE);
        FabLayout.startAnimation(animforVisible);
        Overlay.setVisibility(View.VISIBLE);
        Overlay.startAnimation(animforoverlay);
        MainButton.startAnimation(animforFabRotate);
        MainButton.setImageResource(R.drawable.ic_close);

    }

    public void RemoveFab() {
        /*function to remove Fab Buttons*/
        fabFlag = 0;
        FabLayout.setVisibility(View.GONE);
        FabLayout.startAnimation(animforInvisible);
        Overlay.setVisibility(View.GONE);
        Overlay.startAnimation(animforoverlayGone);
        MainButton.startAnimation(animforFabRotate);
        MainButton.setImageResource(R.drawable.ic_edit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.itemmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Performs function associated with a menu items
        switch (item.getItemId()) {
            case R.id.action_send:
                if (ImageLoadflag == 1) {
                    Bitmap sendbitmap = UploadedImage.getDrawingCache();
                    int MyVersion = Build.VERSION.SDK_INT;
                    if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                        if (!checkIfAlreadyhavePermission()) {
                            requestForSpecificPermission();
                        }

                    }
                    if (canSend) {
                        // Stores the file so that it can be shared on various apps
                        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), sendbitmap, "title", null);
                        Uri bitmapUri = Uri.parse(bitmapPath);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/png");
                        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                        startActivity(Intent.createChooser(intent, "Share"));
                    }

                }

                break;
            case R.id.action_download:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(UrlforUpload));
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            canSend = true;
            return true;

        } else {
            canSend = false;
            return false;
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    canSend = true;
                } else {
                    canSend = false;
                    //not granted
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
