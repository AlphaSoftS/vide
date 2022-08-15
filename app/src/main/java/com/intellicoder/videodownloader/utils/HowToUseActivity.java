package com.intellicoder.videodownloader.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.intellicoder.videodownloader.MainActivity;
import com.intellicoder.videodownloader.R;
import com.intellicoder.videodownloader.adapters.SliderAdapter;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;


public class HowToUseActivity extends AppCompatActivity {
    private Button useLetsGoButton;
    private SliderView sliderView;
    private SliderAdapter adapter;
    private final int[] images = {R.drawable.bwa,
            R.drawable.downloaderdrawer,
            R.drawable.dailymotion,
            R.drawable.facebook,
            R.drawable.gallerydrawer};
    private final String[] imageDescription = {"Image 1","Image 2","Image 3","Image 4","Image 5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpnactivity);
        Log.d("f","f");

        useLetsGoButton = findViewById(R.id.how_to_use_back_button);
        useLetsGoButton.setOnClickListener(view -> startActivity(new Intent(HowToUseActivity.this, MainActivity.class)));

        sliderView = findViewById(R.id.imageSlider);


        adapter = new SliderAdapter(this,images,imageDescription);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();


        sliderView.setOnIndicatorClickListener(position -> Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition()));
    }
}