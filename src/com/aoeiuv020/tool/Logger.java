/* ***************************************************
	^> File Name: Logger.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/11 - 02:53:47
*************************************************** */
package com.aoeiuv020.tool;
import android.util.Log;
public class Logger
{
	public static final String TAG="aoeiuv020 Logger";
	public static final boolean DEBUG=true;
	private Logger()
	{
	}
	public static void v(String format,Object... parms)
	{
		if(DEBUG)
			Log.v(TAG,String.format(format+"\n",parms));
	}
	public static void e(Throwable throwable)
	{
		if(DEBUG)
			throw new RuntimeException(throwable);
	}
}
