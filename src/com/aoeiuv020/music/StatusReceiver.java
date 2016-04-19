/* ***************************************************
	^> File Name: StatusReceiver.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/13 - 03:13:59
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
public class StatusReceiver extends BroadcastReceiver
{
	private Listener mListener=null;
	public StatusReceiver(Listener listener)
	{
		mListener=listener;
	}
	public static IntentFilter getFilter()
	{
		IntentFilter filter=new IntentFilter();
		filter.addAction(BroadcastReceiverConstants.STATUS);
		return filter;
	}
	public void onReceive(Context context,Intent intent)
	{
		Logger.v("StatusReceiver onReceive "+intent);
		switch(intent.getIntExtra(BroadcastReceiverConstants.STATUS,0))
		{
			case BroadcastReceiverConstants.STOP:
			case BroadcastReceiverConstants.PAUSE:
				mListener.pause();
				break;
			case BroadcastReceiverConstants.START:
				mListener.start();
				break;
			case 0:
			default:
		}
	}
	public interface Listener
	{
		public void pause();
		public void start();
	}
}
