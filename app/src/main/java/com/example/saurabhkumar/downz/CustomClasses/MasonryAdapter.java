package com.example.saurabhkumar.downz.CustomClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.downzlibrary.DownZ;
import com.example.downzlibrary.ListnerInterface.HttpListener;
import com.example.downzlibrary.Utilities.CacheManager;
import com.example.saurabhkumar.downz.R;
import com.example.saurabhkumar.downz.UserActivity;

import java.util.List;

/**
 * Created by saurabhkumar on 07/08/17.
 */

public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryViewHolder> {

    Context context;
    List<User> users;
    CacheManager<Bitmap> bitmapCacheManager;

    public MasonryAdapter(Context context) {
        this.context = context;
        this.bitmapCacheManager = new CacheManager<>(40 * 1024 * 1024);

    }

    public void setUsers(List<User> items) {
        this.users = items;
    }


    public void clear() {
        this.users.clear();
        notifyDataSetChanged();
    }


    @Override
    public MasonryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.masonrylayoutitem, parent, false);
        MasonryViewHolder masonryView = new MasonryViewHolder(layoutView);
        return masonryView;
    }

    @Override
    public void onBindViewHolder(final MasonryViewHolder holder, int position) {
        final User current = users.get(position);

        if (current == null) {
            return;
        }
        // for Name of User

        holder.NameOfUser.setText(current.getName());

        // for username

        holder.UserName.setText(current.getUserName());

        // for number of likes
        String likes = current.getNumberOfLikes() + "";
        holder.NumberOfLikes.setText(likes);

        //for uploaded picture

        DownZ
                .from(context)
                .load(DownZ.Method.GET, current.getUploadedImageUrl())
                .asBitmap()
                .setCacheManager(bitmapCacheManager)
                .setCallback(new HttpListener<Bitmap>() {
                    @Override
                    public void onRequest() {
                        holder.loading.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(Bitmap data) {
                        if (data != null) {
                            holder.loading.setVisibility(View.GONE);
                            holder.Uploadedphoto.setImageBitmap(data);
                        }

                    }

                    @Override
                    public void onError() {
                        holder.loading.setVisibility(View.GONE);
                        Log.e(String.valueOf(R.string.TAG), "On Load Error");
                    }

                    @Override
                    public void onCancel() {
                        holder.loading.setVisibility(View.GONE);

                    }
                });

        // for profile picture

        DownZ
                .from(context)
                .load(DownZ.Method.GET, current.getProfilePicUrl())
                .asBitmap()
                .setCacheManager(bitmapCacheManager)
                .setCallback(new HttpListener<Bitmap>() {
                    @Override
                    public void onRequest() {
                        holder.loading.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(Bitmap data) {
                        if (data != null) {
                            holder.loading.setVisibility(View.GONE);
                            holder.ProfileImage.setImageBitmap(data);
                        }

                    }

                    @Override
                    public void onError() {
                        holder.loading.setVisibility(View.GONE);
                        Log.e(String.valueOf(R.string.TAG), "On Load Error");
                    }

                    @Override
                    public void onCancel() {
                        holder.loading.setVisibility(View.GONE);

                    }
                });
        //for Onclick listner
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String NameofUser = current.getName();
                String UserName = current.getUserName();
                String UploadUrl = current.getUploadedImageUrl();
                String ProfilepicUrl = current.getProfilePicUrl();
                String NumberofLikes = current.getNumberOfLikes() + "";
                String Tags = "Tags: ";
                List<String> categories = current.getCategories();
                for (int i = 0; i < categories.size(); i++) {
                    Tags = Tags + categories.get(i) + ", ";
                }
                Context context = holder.mView.getContext();
                Intent intent = new Intent(context, UserActivity.class);
                Bundle extras = new Bundle();
                extras.putString("Name_Of_User", NameofUser);
                extras.putString("User_Name", UserName);
                extras.putString("Upload_Url", UploadUrl);
                extras.putString("Profile_Pic", ProfilepicUrl);
                extras.putString("Categories", Tags);
                extras.putString("No_Of_Likes", NumberofLikes);
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MasonryViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView Uploadedphoto;
        TextView NameOfUser;
        TextView UserName;
        TextView NumberOfLikes;
        de.hdodenhof.circleimageview.CircleImageView ProfileImage;
        ProgressBar loading;


        public MasonryViewHolder(View itemView) {
            super(itemView);
            mView = itemView;


            Uploadedphoto = itemView.findViewById(R.id.uploadedImage);
            NameOfUser = itemView.findViewById(R.id.NameOfUser);
            UserName = itemView.findViewById(R.id.UserName);
            NumberOfLikes = itemView.findViewById(R.id.NumOfLikes);
            ProfileImage = itemView.findViewById(R.id.profile_image);
            loading = itemView.findViewById(R.id.loadingbar);
        }

    }
}
