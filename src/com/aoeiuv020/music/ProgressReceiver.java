/* ***************************************************
	^> File Name: ProgressReceiver.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/20 - 01:34:44
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
public class ProgressReceiver extends BroadcastReceiver
{
	private Listener mListener=null;
	public ProgressReceiver(Listener listener)
	{
		mListener=listener;
	}
	public static IntentFilter getFilter()
	{
		IntentFilter filter=new IntentFilter();
		filter.addAction(BroadcastReceiverConstants.PROCESS);
		return filter;
	}
	public void onReceive(Context context,Intent intent)
	{
		Logger.v("ProgressReceiver onReceive "+intent);
		int current=intent.getIntExtra("current",0);
		int duration=intent.getIntExtra("duration",0);
		mListener.onSeek(current,duration);
	}
	public interface Listener
	{
		public void onSeek(int current,int duration);
	}
}
