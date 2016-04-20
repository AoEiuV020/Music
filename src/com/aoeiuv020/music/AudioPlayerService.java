/* ***************************************************
	^> File Name: AudioPlayerService.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/12 - 23:55:58
*************************************************** */
package com.aoeiuv020.music;
import com.aoeiuv020.tool.Logger;
import android.app.*;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.*;
import android.content.*;
import android.view.*;
import android.net.*;
import android.media.*;
import java.lang.reflect.*;
import java.io.*;
public class AudioPlayerService extends Service implements MediaPlayer.OnCompletionListener
{
	private MediaPlayer mMediaPlayer=new MediaPlayer();
	private MediaMetadataRetriever mMediaMetadataRetriever=new MediaMetadataRetriever();
	private BroadcastReceiver mReceiver=null;
	private Uri mDataUri=null;
	private Thread mProgressThread=null;
	@Override
	public void onCreate()
	{
		Logger.v("AudioPlayerService onCreate()");
		super.onCreate();
		initReceiver();
		mMediaPlayer.setOnCompletionListener(this);
	}
	private void initReceiver()
	{
		mReceiver=new ControlReceiver(this);
		IntentFilter filter=new IntentFilter();
		filter.addAction(BroadcastReceiverConstants.CONTROL);
		/*
		try
		{
			filter.addDataType(BroadcastReceiverConstants.TYPE);
		}
		catch(IntentFilter.MalformedMimeTypeException e)
		{
			//不可到达，
			Logger.e(e);
		}
		*/
		registerReceiver(mReceiver,filter);
	}
	@Override
	public void onCompletion(MediaPlayer mp)
	{
		pause();
	}
	@Override
	public int onStartCommand(Intent intent,int flags,int startId)
	{
		Logger.v("AudioPlayerService onStartCommand() intent=%s",intent);
		int result=super.onStartCommand(intent,flags,startId);
		if(intent==null)
			return result;
		play(intent);
		return result;
	}
	public void sendStatus()
	{
		if(mMediaPlayer.isPlaying())
			sendStatus(BroadcastReceiverConstants.START);
		else
			sendStatus(BroadcastReceiverConstants.PAUSE);
	}
	private void sendStatus(int status)
	{
		Logger.v("sendStatus "+status);
		Intent intent=new Intent();
		intent.setAction(BroadcastReceiverConstants.STATUS);
		intent.putExtra(BroadcastReceiverConstants.STATUS,status);
		sendBroadcast(intent);
	}
	public void startOrPause()
	{
		Logger.v("startOrPause "+mMediaPlayer.isPlaying());
		if(mMediaPlayer.isPlaying())
			pause();
		else
			start();
	}
	public void start()
	{
		if(!mMediaPlayer.isPlaying())
			mMediaPlayer.start();
		sendStatus();
	}
	public void stop()
	{
		mMediaPlayer.stop();
		sendStatus();
	}
	public void pause()
	{
		if(mMediaPlayer.isPlaying())
			mMediaPlayer.pause();
		sendStatus();
	}
	public void play(Intent intent)
	{
		if(intent==null)
			return ;
		Uri uri=intent.getData();
		if(uri==null)
			return;
		mDataUri=uri;
		try
		{
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(this,uri);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		}
		catch(IOException e)
		{
			Logger.e(e);
		}
		sendMetadata();
		sendStatus();
		if(mProgressThread!=null)
		{
			mProgressThread.interrupt();
		}
		mProgressThread=new ProgressThread(this,mMediaPlayer);
		mProgressThread.start();
		MusicProvider.add(this,uri);
	}
	public void seekTo(int progress)
	{
		if(mMediaPlayer.isPlaying())
			mMediaPlayer.seekTo(progress);
	}
	public void sendMetadata()
	{
		Intent intent=new Intent();
		intent.setAction(BroadcastReceiverConstants.METADATA);
		intent.setDataAndType(mDataUri,BroadcastReceiverConstants.TYPE);
		sendBroadcast(intent);
		Logger.v("sendMetadata "+intent+mDataUri);
	}
	@Override
	public IBinder onBind(Intent intent)
	{
		Logger.v("AudioPlayerService onBind()"+intent);
		return null;
	}
	@Override
	public void onDestroy()
	{
		Logger.v("AudioPlayerService onDestroy()");
		if(mMediaPlayer.isPlaying())
		{
			mMediaPlayer.stop();
		}
		if(mProgressThread!=null)
		{
			mProgressThread.interrupt();
			mProgressThread=null;
		}
		mMediaPlayer.release();
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
}
class ControlReceiver extends BroadcastReceiver
{
	private AudioPlayerService mAudioPlayerService=null;
	public ControlReceiver(AudioPlayerService service)
	{
		mAudioPlayerService=service;
	}
	public void onReceive(Context context,Intent intent)
	{
		Logger.v("ControlReceiver onReceive "+intent);
		switch(intent.getIntExtra(BroadcastReceiverConstants.CONTROL,-1))
		{
			case BroadcastReceiverConstants.SEEK:
				mAudioPlayerService.seekTo(intent.getIntExtra("current",0));
				break;
			case BroadcastReceiverConstants.METADATA_REQUIRE:
				mAudioPlayerService.sendMetadata();
				break;
			case BroadcastReceiverConstants.STATUS_REQUIRE:
				mAudioPlayerService.sendStatus();
				break;
			case BroadcastReceiverConstants.START_OR_PAUSE:
				mAudioPlayerService.startOrPause();
				break;
			case BroadcastReceiverConstants.PAUSE:
				mAudioPlayerService.pause();
				break;
			case BroadcastReceiverConstants.START:
				mAudioPlayerService.start();
				break;
			case BroadcastReceiverConstants.STOP:
				mAudioPlayerService.stop();
				break;
			case BroadcastReceiverConstants.PLAY:
				mAudioPlayerService.play(intent);
				break;
			case 0:
			default:
		}
	}
}
class ProgressThread extends Thread
{
	private MediaPlayer mMediaPlayer=null;
	private Context mContext=null;
	public ProgressThread(Context context,MediaPlayer mp)
	{
		mMediaPlayer=mp;
		mContext=context;
	}
	@Override
	public void run()
	{
		try
		{
			while(true)
			{
				Thread.sleep(1000);
				boolean isPlaying=mMediaPlayer.isPlaying();
				Logger.v("ProgressThread %s",isPlaying);
				if(!isPlaying)
					continue;
				int current=mMediaPlayer.getCurrentPosition();
				int duration=mMediaPlayer.getDuration();
				Intent intent=new Intent();
				intent.setAction(BroadcastReceiverConstants.PROCESS);
				intent.putExtra("current",current);
				intent.putExtra("duration",duration);
				mContext.sendBroadcast(intent);
			}
		}
		catch(InterruptedException e)
		{
			Logger.v("ProgressThread InterruptedException "+e);
		}
	}
}
