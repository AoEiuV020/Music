/* ***************************************************
	^> File Name: AudioPlayerNotification.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/13 - 03:26:01
*************************************************** */
package com.aoeiuv020.music;
import com.aoeiuv020.tool.Logger;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.content.*;
import android.view.*;
import android.net.*;
import android.media.*;
import java.lang.reflect.*;
import java.io.*;
public class AudioPlayerNotification implements StatusReceiver.Listener,MetadataReceiver.Listener
{
	private boolean mShow=false;
	private int NOTIFICATION=0;
	Notification mNotification=null;
	MediaMetadataRetriever mMediaMetadataRetriever=new MediaMetadataRetriever();
	NotificationManager mNotificationManager=null;
	RemoteViews mRemoteViews=null;
	PendingIntent mPendingIntent=null;
	Context mContext=null;
	public AudioPlayerNotification(Context context)
	{
		mContext=context;
		mNotificationManager=(NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mRemoteViews=new RemoteViews(mContext.getPackageName(),R.layout.layout_notification);
		mRemoteViews.setImageViewResource(R.id.notification_image,R.drawable.ic_launcher);
		mRemoteViews.setOnClickPendingIntent(R.id.notification_play,getClickPendingIntent());
		Intent intent=new Intent(mContext,Main.class);
		mPendingIntent=PendingIntent.getActivity(mContext,0,intent,0);
		mContext.registerReceiver(new StatusReceiver(this),StatusReceiver.getFilter());
		mContext.registerReceiver(new MetadataReceiver(this),MetadataReceiver.getFilter());
	}
    @Override
	public void showMetadata(Uri uri)
	{
		if(uri==null)
			return;
		mMediaMetadataRetriever.setDataSource(mContext,uri);
		String name=mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		String author=mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		mRemoteViews.setTextViewText(R.id.notification_name,name);
		mRemoteViews.setTextViewText(R.id.notification_author,author);
		show();
	}
	private PendingIntent getClickPendingIntent()
	{
		Intent intent=new Intent();
		intent.setAction(BroadcastReceiverConstants.CONTROL);
		intent.putExtra(BroadcastReceiverConstants.CONTROL,BroadcastReceiverConstants.START_OR_PAUSE);
		PendingIntent pi=PendingIntent.getBroadcast(mContext,0,intent,0);
		return pi;
	}
	private Intent getClickIntent()
	{
		Intent intent=new Intent();
		intent.setAction(BroadcastReceiverConstants.CONTROL);
		intent.putExtra(BroadcastReceiverConstants.CONTROL,BroadcastReceiverConstants.START_OR_PAUSE);
		return intent;
	}
	public void setShow(boolean s)
	{
		mShow=s;
	}
	public void hide()
	{
		Logger.v("hide "+mShow);
		mNotificationManager.cancel(NOTIFICATION);
	}
	public void show()
	{
		Logger.v("show "+mShow);
		if(!mShow)
			return;
		/*
		 * .setSmallIcon.setContentTitle.setContentText
		 * 这些没用，但是好像不设置不行，
		 */
		mNotification=new Notification.Builder(mContext)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle("title")
			.setContentText("text")
			.setOngoing(true)
			.setContentIntent(mPendingIntent)
			.setContent(mRemoteViews)
			.build();
		//mNotification.flags=Notification.FLAG_ONGOING_EVENT;
		//mNotification.contentIntent=mPendingIntent;
		//mNotification.contentView=mRemoteViews;
		mNotificationManager.notify(NOTIFICATION,mNotification);
	}
	@Override
	public void pause()
	{
		mRemoteViews.setImageViewResource(R.id.notification_play,android.R.drawable.ic_media_play);
		show();
	}
	@Override
	public void start()
	{
		mRemoteViews.setImageViewResource(R.id.notification_play,android.R.drawable.ic_media_pause);
		show();
	}
}
