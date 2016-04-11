package com.aoeiuv020.music;
import com.aoeiuv020.tool.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.content.*;
import android.view.*;
import android.net.*;
import android.media.*;
import java.lang.reflect.*;
import java.io.*;

public class Main extends Activity implements View.OnClickListener,MediaPlayer.OnCompletionListener
{
	TextView tvTitle=null;
	TextView tvName=null;
	TextView tvAuthor=null;
	MediaPlayer mMediaPlayer=new MediaPlayer();
	ImageButton ibPlay=null;
	MediaMetadataRetriever mMediaMetadataRetriever=new MediaMetadataRetriever();
	View vOpen=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		findView();
		set();
		Intent intent=getIntent();
		if(intent!=null)
			onNewIntent(intent);
    }
	private void set()
	{
		ibPlay.setOnClickListener(this);
		vOpen.setOnClickListener(this);
		mMediaPlayer.setOnCompletionListener(this);
	}
	@Override
	public void onCompletion(MediaPlayer mp)
	{
		callOnPause();
	}
	@Override
	public void onNewIntent(Intent intent)
	{
		if(intent==null)
			return;
		mMediaPlayer.reset();
		Uri uri=intent.getData();
		if(uri==null)
			return;
		start(uri);
		mMediaMetadataRetriever.setDataSource(this,uri);
		String name=mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		String author=mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		tvName.setText(""+name);
		tvAuthor.setText(""+author);
		/*
		try
		{
			Field[] mmrFields=MediaMetadataRetriever.class.getFields();
			Method extract=MediaMetadataRetriever.class.getMethod("extractMetadata",int.class);
			for(Field f:mmrFields)
			{
				String fName=f.getName();
				String value=(String)extract.invoke(mMediaMetadataRetriever,f.getInt(mMediaMetadataRetriever));
				Logger.v("field=%s,value=%s;",fName,value);
			}
		}
		catch(Exception e)
		{
			Logger.e(e);
		}
		*/
	}
	@Override
	public void onClick(View view)
	{
		switch(view.getId())
		{
			case R.id.play:
				if(mMediaPlayer.isPlaying())
				{
					pause();
				}
				else
				{
					start();
				}
				break;
			case R.id.open:
				Intent intent=new Intent();
				intent.setType("audio/");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent,0);
				break;
		}
	}
	@Override
	public void onActivityResult(int reqCode,int resultCode,Intent data)
	{
		onNewIntent(data);
	}
	private void start(Uri uri)
	{
		tvTitle.setText(uri.toString());
		try
		{
			mMediaPlayer.setDataSource(this,uri);
			mMediaPlayer.prepare();
			start();
		}
		catch(IOException e)
		{
			Logger.e(e);
		}
	}
	private void pause()
	{
		mMediaPlayer.pause();
		callOnPause();
	}
	private void callOnPause()
	{
		ibPlay.setImageResource(android.R.drawable.ic_media_play);
	}
	private void start()
	{
		mMediaPlayer.start();
		ibPlay.setImageResource(android.R.drawable.ic_media_pause);
	}
	private void findView()
	{
		tvTitle=(TextView)findViewById(R.id.title);
		tvName=(TextView)findViewById(R.id.name);
		tvAuthor=(TextView)findViewById(R.id.author);
		ibPlay=(ImageButton)findViewById(R.id.play);
		vOpen=findViewById(R.id.open);
	}
	@Override
	public void onDestroy()
	{
		if(mMediaPlayer.isPlaying())
			mMediaPlayer.stop();
		mMediaPlayer.release();
		super.onDestroy();
	}
}
