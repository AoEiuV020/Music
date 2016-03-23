package com.aoeiuv020.music;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;

public class Main extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		mainUI();
    }
	void mainUI()
	{
		LinearLayout mainView=null;
		mainView=new LinearLayout(this);
		mainView.setOrientation(LinearLayout.VERTICAL);
			LinearLayout headLayout=new LinearLayout(this);
			headLayout.setOrientation(LinearLayout.HORIZONTAL);
		mainView.addView(headLayout);
			LinearLayout pictureLayout=new LinearLayout(this);
			pictureLayout.setOrientation(LinearLayout.HORIZONTAL);
		mainView.addView(pictureLayout);
			ScrollView lyricsScrollView=new ScrollView(this);
				LinearLayout lyricsLayout=new LinearLayout(this);
				lyricsLayout.setOrientation(LinearLayout.HORIZONTAL);
			lyricsScrollView.addView(lyricsLayout);
		mainView.addView(lyricsScrollView);
			LinearLayout controlLayout=new LinearLayout(this);
			controlLayout.setOrientation(LinearLayout.HORIZONTAL);
			controlLayout.setGravity(Gravity.CENTER);
				ImageView delete=new ImageButton(this);
				delete.setImageResource(android.R.drawable.ic_menu_delete);
			controlLayout.addView(delete);
				ImageView star=new ImageButton(this);
				star.setImageResource(android.R.drawable.btn_star_big_off);
			controlLayout.addView(star);
				ImageView start=new ImageButton(this);
				start.setImageResource(android.R.drawable.ic_media_play);
			controlLayout.addView(start);
				ImageView next=new ImageButton(this);
				next.setImageResource(android.R.drawable.ic_media_next);
			controlLayout.addView(next);
				ImageView share=new ImageButton(this);
				share.setImageResource(android.R.drawable.ic_menu_share);
			controlLayout.addView(share);
		mainView.addView(controlLayout);
        setContentView(mainView);
	}
}
