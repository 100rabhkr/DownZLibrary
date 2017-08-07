package com.example.saurabhkumar.downz.CustomClasses;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.saurabhkumar.downz.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saurabhkumar on 07/08/17.
 */

public class MasonryView extends RecyclerView.ViewHolder {

    ImageView Uploadedphoto;
    TextView NameOfUser;
    TextView UserName;
    TextView NumberOfLikes;
    de.hdodenhof.circleimageview.CircleImageView ProfileImage;
    ProgressBar loading;


    public MasonryView(View itemView) {
        super(itemView);
        Uploadedphoto = itemView.findViewById(R.id.uploadedImage);
        NameOfUser = itemView.findViewById(R.id.NameOfUser);
        UserName = itemView.findViewById(R.id.UserName);
        NumberOfLikes = itemView.findViewById(R.id.NumOfLikes);
        ProfileImage = itemView.findViewById(R.id.profile_image);
        loading = itemView.findViewById(R.id.loadingbar);
    }

}
