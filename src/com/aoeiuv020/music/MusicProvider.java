/* ***************************************************
	^> File Name: MusicProvider.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/21 - 02:21:28
*************************************************** */
package com.aoeiuv020.music;
import com.aoeiuv020.tool.Logger;
import android.content.Context;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import java.util.*;
import java.io.*;
public class MusicProvider extends ContentProvider
{
	private static final Set<Uri> mSet=new LinkedHashSet<Uri>();
	public static Set<Uri> getDefaultList(Context context)
	{
		return mSet;
	}
	public static void add(Context context,Uri uri)
	{
		mSet.add(uri);
	}
	@Override
	public boolean onCreate()
	{
		return true;
	}
	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		return uri;
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		return 0;
	}
	@Override
	public String getType(Uri uri)
	{
		return null;
	}
	@Override
	public  Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		return null;
	}
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		return 0;
	}
}
