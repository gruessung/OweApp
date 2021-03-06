package de.gvisions.oweapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author http://www.androidsnippets.com/prompt-engaged-users-to-rate-your-app-in-the-android-market-appirater
 *
 */

public class AppRater {

	private static String APP_TITLE=" this App";
    private final static String APP_PNAME = "de.gvisions.oweapp";
    
    private final static int DAYS_UNTIL_PROMPT = 0;
    private final static int LAUNCHES_UNTIL_PROMPT = 4;
    
    
    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }
        
        //TODO  add resource name from your strings.xml
        APP_TITLE = mContext.getResources().getString(R.string.app_name);
       
        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        
        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + 
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }
        
        editor.commit();
    }   
    
    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle(APP_TITLE);

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        
        TextView tv = new TextView(mContext);
        //TODO  add resource name from your strings.xml
        tv.setText( mContext.getResources().getString(R.string.rate_txt1) +" " + APP_TITLE + mContext.getResources().getString(R.string.rate_txt2));
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);
        
        Button b1 = new Button(mContext);
        //TODO  add resource name from your strings.xml
        b1.setText(mContext.getResources().getString(R.string.rate_btn_rate)+" " + APP_TITLE);
        b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try{
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                } catch(android.content.ActivityNotFoundException anfe){
                    //ignore
                }
                dialog.dismiss();
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
            }
        });        
        ll.addView(b1);

        Button b2 = new Button(mContext);
        //TODO  add resource name from your strings.xml
        b2.setText(mContext.getResources().getString(R.string.rate_btn_later));
        b2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }

            
        });
        ll.addView(b2);

        Button b3 = new Button(mContext);
        //TODO  add resource name from your strings.xml
        b3.setText(mContext.getResources().getString(R.string.rate_btn_no));
        b3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);        
        dialog.show();        
    }
	
}
