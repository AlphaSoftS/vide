package com.intellicoder.videodownloader;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellicoder.videodownloader.adapters.ListAllProfilePostsInstagramUserAdapter;
import com.intellicoder.videodownloader.databinding.ActivityBulkDownloaderProfileBinding;
import com.intellicoder.videodownloader.models.bulkdownloader.EdgeInfo;
import com.intellicoder.videodownloader.models.bulkdownloader.UserProfileDataModelBottomPart;
import com.intellicoder.videodownloader.utils.SharedPrefsForInstagram;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.intellicoder.videodownloader.R.string.youneedloginfirest;

@Keep
public class BulkDownloader_ProfileActivity extends AppCompatActivity {

    private String myUserName;
    private String myUserPKID;
    private ActivityBulkDownloaderProfileBinding binding;
    public static String INSTAGRAM_endcursorval = "";
    private List<EdgeInfo> storyModelInstaItemList;

    ListAllProfilePostsInstagramUserAdapter listAllProfilePostsInstagramUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBulkDownloaderProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        storyModelInstaItemList = new ArrayList<>();


        listAllProfilePostsInstagramUserAdapter = new ListAllProfilePostsInstagramUserAdapter(BulkDownloader_ProfileActivity.this, storyModelInstaItemList);
        binding.recyclerviewProfileLong.setLayoutManager(new GridLayoutManager(BulkDownloader_ProfileActivity.this, 3));
        binding.recyclerviewProfileLong.setAdapter(listAllProfilePostsInstagramUserAdapter);

        if (getIntent() != null) {
            myUserName = getIntent().getStringExtra("username");
            myUserPKID = getIntent().getStringExtra("pkid");
            loadAllprofileData(myUserName, myUserPKID);
        }


        binding.recyclerviewProfileLong.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    binding.floatingloadmore.setVisibility(View.VISIBLE);

                    Toast.makeText(BulkDownloader_ProfileActivity.this, R.string.taptoloadmore, Toast.LENGTH_SHORT).show();

                }
            }
        });


        binding.floatingloadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.floatingloadmore.setVisibility(View.GONE);
                loadAllPostsData();


            }
        });
    }


    void loadAllprofileData(String username, String pkid) {

        SharedPrefsForInstagram sharedPrefsFor = new SharedPrefsForInstagram(BulkDownloader_ProfileActivity.this);
        Map<String, String> map = sharedPrefsFor.getPreference(SharedPrefsForInstagram.PREFERENCE);
        if (map != null) {
            String cookie = "ds_user_id=" + map.get(SharedPrefsForInstagram.PREFERENCE_USERID) + "; sessionid=" + map.get(SharedPrefsForInstagram.PREFERENCE_SESSIONID);


            AndroidNetworking.get("https://www.instagram.com/{username}/?__a=1")
                    .addPathParameter("username", username)
                    .setPriority(Priority.LOW)
                    .addHeaders("Cookie", cookie)
                    .addHeaders(
                            "User-Agent",
                            "\"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+\""
                    )
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            //   binding.dfsadasdas.setText(response.toString());
                            System.out.println("hvjksdhfhdkd userpkid 00 " + pkid + " username " + username);
                            // System.out.println("hvjksdhfhdkd usernamedata click  idis" + response.toString());
                            //4162923872
                            //3401888503

                            try {


                                JSONObject userdata = response.getJSONObject("graphql").getJSONObject("user");
                                binding.profileFollowersNumberTextview.setText(userdata.getJSONObject("edge_followed_by").getString("count"));
                                binding.profileFollowingNumberTextview.setText(userdata.getJSONObject("edge_follow").getString("count"));
                                binding.profilePostNumberTextview.setText(userdata.getJSONObject("edge_owner_to_timeline_media").getString("count"));
                                binding.profileLongIdTextview.setText(userdata.getString("username"));
//                                ObjectMapper om = new ObjectMapper();
//                                UserProfileTopData root = om.readValue(response.toString(), UserProfileTopData.class);
//                                System.out.println("hvjksdhfhdkd followed by  " + root.getGraphql().getUser().getEdge_followed_by().getCount());

                                //   binding.profileLongIdTextview.setText(root.getGraphql().getUser().getUsername());
                                //  binding.profileFollowersNumberTextview.setText(root.getGraphql().getUser().getEdge_followed_by().getCount() + "");
                                //    binding.profileFollowingNumberTextview.setText(root.getGraphql().getUser().getEdge_follow().getCount() + "");
                                //  binding.profilePostNumberTextview.setText(root.getGraphql().getUser().getEdge_owner_to_timeline_media().getCount() + "");

                                if (userdata.getBoolean("is_verified")) {
                                    binding.profileLongApprovedImageview.setVisibility(View.VISIBLE);
                                } else {
                                    binding.profileLongApprovedImageview.setVisibility(View.GONE);

                                }

                                if (userdata.getBoolean("is_private")) {
                                    binding.rowSearchPrivateImageviewPrivate.setVisibility(View.VISIBLE);
                                } else {
                                    binding.rowSearchPrivateImageviewPrivate.setVisibility(View.GONE);

                                }

                                Glide.with(BulkDownloader_ProfileActivity.this).load(userdata.getString("profile_pic_url")).into(binding.profileLongCircle);

//                                System.out.println("hvjksdhfhdkd endcursoris= " + root.getLogging_page_id());
//                                System.out.println("hvjksdhfhdkd endcursoris img = " + root.getGraphql().getUser().getProfile_pic_url());


                            } catch (Exception e) {

                                System.out.println("hvjksdhfhdkd eeee errr 00 " + e.getMessage());


                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onError(ANError anError) {
                            System.out.println("hvjksdhfhdkd eeee" + anError.getResponse());

                        }
                    });
            loadAllPostsData();


        } else {
            Toast.makeText(this, youneedloginfirest, Toast.LENGTH_LONG).show();
        }


    }


    void loadAllPostsData() {
        binding.profileLongProgress.setVisibility(View.VISIBLE);
        SharedPrefsForInstagram sharedPrefsFor = new SharedPrefsForInstagram(BulkDownloader_ProfileActivity.this);
        Map<String, String> map = sharedPrefsFor.getPreference(SharedPrefsForInstagram.PREFERENCE);
        if (map != null) {
            String cookie = "ds_user_id=" + map.get(SharedPrefsForInstagram.PREFERENCE_USERID) + "; sessionid=" + map.get(SharedPrefsForInstagram.PREFERENCE_SESSIONID);

            AndroidNetworking.get("https://instagram.com/graphql/query/?query_id=17888483320059182&id={userid}&first=20&after={end_cursor}")
                    .addPathParameter("userid", myUserPKID)
                    .addPathParameter("end_cursor", INSTAGRAM_endcursorval)//QVFCX1U4TzBuSXhyZTNyMzBZSmpXYVljazN2UHo1NEhqYko5NDF1R0FMSWU3U25hcVVPcjdkVnVuT3cxb2drZ2VRbFFuVXV4eGIzRGZ3RFM0QkJwOW1wMg==
                    .setPriority(Priority.LOW)
                    .addHeaders("Cookie", cookie)
                    .addHeaders(
                            "User-Agent",
                            "\"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+\""
                    )
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            binding.profileLongProgress.setVisibility(View.GONE);


                            System.out.println("hvjksdhfhdkd profiledataaaaa " + response.toString());
                            System.out.println("hvjksdhfhdkd userpkid " + myUserPKID + " username");
                            //  System.out.println("hvjksdhfhdkd model click  idis" + response.toString());
                            //4162923872
                            //3401888503
                            // binding.dfsadasdas.setText(response.toString());

                            try {
                                ObjectMapper om = new ObjectMapper();
                                UserProfileDataModelBottomPart root = om.readValue(response.toString(), UserProfileDataModelBottomPart.class);

                                storyModelInstaItemList.addAll(root.getData().getUser().getEdge_owner_to_timeline_media().getEdges());


                                INSTAGRAM_endcursorval = root.getData().getUser().getEdge_owner_to_timeline_media().getPage_info().getEnd_cursor();


                            } catch (Exception e) {
                                System.out.println("hvjksdhfhdkd eeee errrrrrrr" + e.getMessage());
                            }
                            listAllProfilePostsInstagramUserAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onError(ANError anError) {
                            binding.profileLongProgress.setVisibility(View.GONE);

                            System.out.println("hvjksdhfhdkd eeee" + anError.getResponse());

                        }
                    });

        } else {
            binding.profileLongProgress.setVisibility(View.GONE);

            Toast.makeText(this, youneedloginfirest, Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onBackPressed() {
        INSTAGRAM_endcursorval = "";
        //startActivity(new Intent(BulkDownloader_ProfileActivity.this,InstagramBulkDownloader.class));
        finish();
    }
}