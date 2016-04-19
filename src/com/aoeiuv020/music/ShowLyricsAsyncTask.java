/* ***************************************************
	^> File Name: ShowLyricsAsyncTask.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/20 - 00:22:29
*************************************************** */
package com.aoeiuv020.music;
import android.os.AsyncTask;
import android.widget.*;
import java.util.*;
public class ShowLyricsAsyncTask extends AsyncTask<Void,Integer,String>
{
	private ListView mListView=null;
	private String mName=null;
	public ShowLyricsAsyncTask(ListView view,String name)
	{
		mListView=view;
		mName=name;
	}
	@Override
	protected void onPreExecute()
	{
	}
	@Override
	protected String doInBackground(Void... parms)
	{
		String lyrics=null;
		return lyrics;
	}
	@Override
	protected void onPostExecute(String lyrics)
	{
	}
}
