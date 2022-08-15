package com.intellicoder.videodownloader.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;

import androidx.core.app.NotificationCompat;

import com.intellicoder.videodownloader.R;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private  onGetItemList onGetItemList;

    public MyBroadcastReceiver (){}

    public MyBroadcastReceiver(onGetItemList onGetItemList){
        this.onGetItemList = onGetItemList;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Battery Status (Charging state)

        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
        }
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        // How are we charging?
        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;


//        if(!(usbCharge) && !(acCharge)) {
//            homeBatteryChargeStatusText.setText("UnPlugged" + "" + "");
//            homeBatteryChargeStatus.setText("UnPlugged" + "" + "");
//        }else{
//            homeBatteryChargeStatusText.setText("Plugged In" + "" + "");
//            homeBatteryChargeStatus.setText("Plugged In" + "" + "");
//        }
//                if(usbCharge) powerSource.setText("USB" + "" + "");
//                if(acCharge) powerSource.setText("AC" + "" + "");

        // Monitor Significant Changes in Battery Level
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale;
        int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
//        if(isCharging)
//        {
//            //iconStatus.setImageResource(R.drawable.ic_battery_charging_full_black_24dp);
//            homeBatteryTimeChargeStatus.setText("Charging" + "" + "");
//            homeBatteryTimeChargeStatus.setVisibility(View.VISIBLE);
//
//        }
//        else if(batteryPct == 100) {
//            homeBatteryTimeChargeStatus.setText("Fully Charged" + "" + "");
//            homeBatteryTimeChargeStatus.setVisibility(View.VISIBLE);
//        }
//
//        else
//        {
//            // iconStatus.setImageResource(R.drawable.ic_battery_full_black_24dp);
//            homeBatteryTimeChargeStatus.setText("Not Charging" + "" + "");
//            homeBatteryTimeChargeStatus.setVisibility(View.VISIBLE);
//        }

//        if(batteryPct*100 >= 80.0) homeBatteryPercentageText.setTextColor(Color.rgb(0, 220, 85));
//        else if(batteryPct*100 >= 60.0 && batteryPct*100 < 80.0) homeBatteryPercentageText.setTextColor(Color.rgb(120, 205, 35));
//        else if(batteryPct*100 >= 40.0 && batteryPct*100 < 60.0) homeBatteryPercentageText.setTextColor(Color.rgb(230, 187, 17));
//        else if(batteryPct*100 >= 20.0 && batteryPct*100 < 40.0) homeBatteryPercentageText.setTextColor(Color.rgb(228, 100, 15));
//        else if(batteryPct*100 >=  0.0 && batteryPct*100 < 20.0) homeBatteryPercentageText.setTextColor(Color.rgb(227, 15, 15));
//        int b = (int) (batteryPct*100);
//        homeBatteryPercentageText.setText(b + "%" + "");



        // Monitor Battery Health
//                int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
//                switch(health)
//                {
//                    case BatteryManager.BATTERY_HEALTH_COLD:
//                        batHealth.setTextColor(Color.CYAN);
//                        batHealth.setText("Cold" + "" + "");
//                        break;
//
//                    case BatteryManager.BATTERY_HEALTH_DEAD:
//                        batHealth.setTextColor(Color.rgb(227, 15, 15));
//                        iconHealth.setImageResource(R.drawable.ic_battery_health_bad);
//                        batHealth.setText("Dead" + "" + "");
//                        break;
//
//                    case BatteryManager.BATTERY_HEALTH_GOOD:
//                        batHealth.setTextColor(Color.rgb(0, 220, 85));
//                        iconHealth.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_battery_health_good));
//                        iconHealth.setImageResource(R.drawable.ic_battery_health_good);
//                        batHealth.setText("Good" + "" + "");
//                        break;
//
//                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
//                        batHealth.setTextColor(Color.rgb(227, 15, 15));
//                        batHealth.setText("Overvoltage" + "" + "");
//                        break;
//
//                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
//                        batHealth.setTextColor(Color.rgb(227, 15, 15));
//                        batHealth.setText("Overheat" + "" + "");
//                        break;
//
//                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
//                        batHealth.setTextColor(Color.DKGRAY);
//                        batHealth.setText("Unknown" + "" + "");
//                        break;
//
//                    case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
//                        batHealth.setTextColor(Color.rgb(227, 15, 15));
//                        iconHealth.setImageResource(R.drawable.ic_battery_alert_black_24dp);
//                        batHealth.setText("Failure" + "" + "");
//                        break;
//
//                    default:
//                        break;
//                }

        // Monitor Battery Voltage
        // Monitor Battery Temperature
        int temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
        onGetItemList.onGetItem(isCharging,usbCharge, acCharge, batteryPct, voltage,temp, health );

        if(batteryPct >=100){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"274")
                    .setSmallIcon(R.drawable.exo_notification_small_icon)
                    .setContentTitle("BatterApp")
                    .setContentText("Fully Charged ")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .setSound(alert)
                    .setOngoing(true);

        }






    }


    public interface onGetItemList{
        void onGetItem(boolean isCharging , boolean usbCharge,boolean acCharge,float batteryPct,int voltage,int temp,int health );

    }

}

