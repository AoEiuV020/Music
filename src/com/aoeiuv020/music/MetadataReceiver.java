/* ***************************************************
	^> File Name: MetadataReceiver.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/13 - 03:12:04
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
public class MetadataReceiver extends BroadcastReceiver
{
	private Listener mListener=null;
	public MetadataReceiver(Listener listener)
	{
		mListener=listener;
	}
	public static IntentFilter getFilter()
	{
		IntentFilter filter=new IntentFilter();
		filter.addAction(BroadcastReceiverConstants.METADATA);
		try
		{
			filter.addDataType(BroadcastReceiverConstants.TYPE);
		}
		catch(IntentFilter.MalformedMimeTypeException e)
		{
			Logger.e(e);
		}
		return filter;
	}
	public void onReceive(Context context,Intent intent)
	{
		Uri uri=intent.getData();
		Logger.v("onReceive "+intent+uri);
		mListener.showMetadata(uri);
	}
	public interface Listener
	{
		public void showMetadata(Uri uri);
	}
}
