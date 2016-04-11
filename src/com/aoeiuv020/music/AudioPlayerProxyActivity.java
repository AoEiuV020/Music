/* ***************************************************
	^> File Name: AudioPlayerProxyActivity.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/11 - 22:25:12
*************************************************** */
package com.aoeiuv020.music;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
public class AudioPlayerProxyActivity extends Activity
{
	public void onCreate(Bundle save)
	{
		super.onCreate(save);
		Intent dataIntent=getIntent();
		Uri dataUri=dataIntent.getData();
		Intent intent=new Intent();
		intent.setClass(this,Main.class);
		intent.setData(dataUri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}
}
