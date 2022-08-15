package com.intellicoder.videodownloader.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intellicoder.videodownloader.R;
import com.intellicoder.videodownloader.receiver.MyBroadcastReceiver;


public class BatterySaverFragment extends Fragment implements MyBroadcastReceiver.onGetItemList{
    private TextView homeBatteryChargeStatus;
    private TextView homeBatteryTimeChargeStatus;
    private TextView homeBatteryPercentageText;
    private TextView homeBatteryVoltageText;
    private TextView homeBatteryChargeStatusText;
    private TextView homeBatteryTemperatureText;
    private TextView homeBatteryHealthText;
    private IntentFilter iFilter;

    private MyBroadcastReceiver myReceiver;

    private Handler progressBarbHandler = new Handler();




    public BatterySaverFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_batter_saver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        myReceiver = new MyBroadcastReceiver((MyBroadcastReceiver.onGetItemList) this);
        getActivity().registerReceiver(myReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        homeBatteryChargeStatus = view.findViewById(R.id.home_battery_charge_status_text);
        homeBatteryTimeChargeStatus = view.findViewById(R.id.home_battery_time_charge_status);
        homeBatteryPercentageText = view.findViewById(R.id.home_battery_charge_percentage_text);
        homeBatteryVoltageText = view.findViewById(R.id.home_voltage_text);
        homeBatteryChargeStatusText = view.findViewById(R.id.home_charge_status_text);
        homeBatteryTemperatureText = view.findViewById(R.id.alarm_setting_temperature_text);
        homeBatteryHealthText = view.findViewById(R.id.alarm_setting_health_text);









    }


    @Override
    public void onGetItem(boolean isCharging, boolean usbCharge, boolean acCharge, float batteryPct, int voltage, int temp, int health) {

        if(!(usbCharge) && !(acCharge)) {
            homeBatteryChargeStatusText.setText("UnPlugged" + "" + "");
            homeBatteryChargeStatus.setText("UnPlugged" + "" + "");
        }else{
            homeBatteryChargeStatusText.setText("Plugged In" + "" + "");
            homeBatteryChargeStatus.setText("Plugged In" + "" + "");
        }
//                if(usbCharge) powerSource.setText("USB" + "" + "");
//                if(acCharge) powerSource.setText("AC" + "" + "");

        // Monitor Significant Changes in Battery Level


        if(isCharging)
        {

            homeBatteryTimeChargeStatus.setText("Charging" + "" + "");
            homeBatteryTimeChargeStatus.setVisibility(View.VISIBLE);

        }
        else if(batteryPct == 100) {
            homeBatteryTimeChargeStatus.setText("Fully Charged" + "" + "");
            homeBatteryTimeChargeStatus.setVisibility(View.VISIBLE);
        }

        else
        {

            homeBatteryTimeChargeStatus.setText("Not Charging" + "" + "");
            homeBatteryTimeChargeStatus.setVisibility(View.VISIBLE);
        }

        if(batteryPct*100 >= 80.0) homeBatteryPercentageText.setTextColor(Color.rgb(0, 220, 85));
        else if(batteryPct*100 >= 60.0 && batteryPct*100 < 80.0) homeBatteryPercentageText.setTextColor(Color.rgb(120, 205, 35));
        else if(batteryPct*100 >= 40.0 && batteryPct*100 < 60.0) homeBatteryPercentageText.setTextColor(Color.rgb(230, 187, 17));
        else if(batteryPct*100 >= 20.0 && batteryPct*100 < 40.0) homeBatteryPercentageText.setTextColor(Color.rgb(228, 100, 15));
        else if(batteryPct*100 >=  0.0 && batteryPct*100 < 20.0) homeBatteryPercentageText.setTextColor(Color.rgb(227, 15, 15));
        int b = (int) (batteryPct*100);
        homeBatteryPercentageText.setText(b + "%" + "");

        homeBatteryTemperatureText.setText(temp/10 +" "+ (char) 0x00B0 + " C" + "");



//                // Monitor Battery Health

                switch(health)
                {
                    case BatteryManager.BATTERY_HEALTH_COLD:
                        homeBatteryHealthText.setTextColor(Color.CYAN);
                        homeBatteryHealthText.setText("Cold" + "" + "");
                        break;

                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        homeBatteryHealthText.setTextColor(Color.rgb(227, 15, 15));
                        homeBatteryHealthText.setText("Dead" + "" + "");
                        break;

                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        homeBatteryHealthText.setTextColor(Color.rgb(0, 220, 85));
                        homeBatteryHealthText.setText("Good" + "" + "");
                        break;

                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        homeBatteryHealthText.setTextColor(Color.rgb(227, 15, 15));
                        homeBatteryHealthText.setText("Over voltage" + "" + "");
                        break;

                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        homeBatteryHealthText.setTextColor(Color.rgb(227, 15, 15));
                        homeBatteryHealthText.setText("Overheat" + "" + "");
                        break;

                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                        homeBatteryHealthText.setTextColor(Color.DKGRAY);
                        homeBatteryHealthText.setText("Unknown" + "" + "");
                        break;

                    case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                        homeBatteryHealthText.setTextColor(Color.rgb(227, 15, 15));
                        homeBatteryHealthText.setText("Failure" + "" + "");
                        break;

                    default:
                        break;
                }

        // Monitor Battery Voltage

        homeBatteryVoltageText.setText(voltage/1000 + " V" + "");

        // Monitor Battery Temperature


    }
}