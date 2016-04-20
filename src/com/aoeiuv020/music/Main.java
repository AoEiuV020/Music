package com.aoeiuv020.music;
import com.aoeiuv020.tool.Logger;
import com.aoeiuv020.widget.SimpleDialog;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.content.*;
import android.view.*;
import android.net.*;
import android.media.*;
import java.lang.reflect.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;
public class Main extends Activity implements View.OnClickListener,MediaPlayer.OnCompletionListener,StatusReceiver.Listener,MetadataReceiver.Listener,ProgressReceiver.Listener,SeekBar.OnSeekBarChangeListener
{
	private AudioPlayerNotification mAudioPlayerNotification=null;
	private boolean isPlaying=false;
	private boolean showNotification=false;
	private Handler mHandler=null;
	private ListView lvLyrics=null;
	TextView tvTitle=null;
	TextView tvName=null;
	TextView tvAuthor=null;
	TextView tvTime=null;
	SeekBar pbProgress=null;
	ImageButton ibPlay=null;
	ImageButton ibList=null;
	MediaMetadataRetriever mMediaMetadataRetriever=new MediaMetadataRetriever();
	View vOpen=null;
	Intent mServiceIntent=new Intent();
	StatusReceiver mStatusReceiver=null;
	IntentFilter mStatusFilter=null;
	MetadataReceiver mMetadataReceiver=null;
	IntentFilter mMetadataFilter=null;
	ProgressReceiver mProgressReceiver=null;
	IntentFilter mProgressFilter=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		findView();
		set();
		mAudioPlayerNotification=new AudioPlayerNotification(this);
		initReceiver();
		Intent intent=getIntent();
		if(intent!=null)
			onNewIntent(intent);
    }
	private void initReceiver()
	{
		mStatusReceiver=new StatusReceiver(this);
		mStatusFilter=StatusReceiver.getFilter();
		mMetadataReceiver=new MetadataReceiver(this);
		mMetadataFilter=MetadataReceiver.getFilter();
		mProgressReceiver=new ProgressReceiver(this);
		mProgressFilter=ProgressReceiver.getFilter();
	}
	@Override
	public void onStart()
	{
		Logger.v("Main onStart()");
		super.onStart();
		mAudioPlayerNotification.setShow(showNotification);
		mAudioPlayerNotification.hide();
		showNotification=true;
		registerReceiver(mStatusReceiver,mStatusFilter);
		registerReceiver(mMetadataReceiver,mMetadataFilter);
		registerReceiver(mProgressReceiver,mProgressFilter);
	}
	@Override
	public void onStop()
	{
		Logger.v("Main onStop()");
		super.onStop();
		mAudioPlayerNotification.setShow(showNotification);
		mAudioPlayerNotification.show();
		showNotification=false;
		unregisterReceiver(mStatusReceiver);
		unregisterReceiver(mMetadataReceiver);
		unregisterReceiver(mProgressReceiver);
	}
	@Override
	public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser)
	{
		Logger.v("onProgressChanged %d,%s",progress,fromUser);
		if(!fromUser)
			return;
		setTime(progress,seekBar.getMax());
	}
	@Override
	public void onStartTrackingTouch (SeekBar seekBar)
	{
	}
	@Override
	public void onStopTrackingTouch (SeekBar seekBar)
	{
		int progress=seekBar.getProgress();
		Intent intent=new Intent();
		intent.setAction(BroadcastReceiverConstants.CONTROL);
		intent.putExtra(BroadcastReceiverConstants.CONTROL,BroadcastReceiverConstants.SEEK);
		intent.putExtra("current",progress);
		sendBroadcast(intent);
	}
	private void set()
	{
		mServiceIntent.setClass(this,AudioPlayerService.class);
		ibPlay.setOnClickListener(this);
		ibList.setOnClickListener(this);
		vOpen.setOnClickListener(this);
		pbProgress.setOnSeekBarChangeListener(this);
	}
	@Override
	public void onSeek(int current,int duration)
	{
		Logger.v("onSeek %d/%d",current,duration);
		pbProgress.setMax(duration);
		pbProgress.setProgress(current);
		setTime(current,duration);
	}
	private void setTime(int current,int duration)
	{
		final SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");
		String time=String.format("[%s/%s]",sdf.format(new Date(current)),sdf.format(new Date(duration)));
		tvTime.setText(time);
	}
	@Override
	public void onCompletion(MediaPlayer mp)
	{
		pause();
	}
	@Override
	public void onNewIntent(Intent intent)
	{
		if(intent==null)
			return;
		mServiceIntent.setData(intent.getData());
		startService(mServiceIntent);
		/*
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
		remoteViews.setTextViewText(R.id.notification_name,""+name);
		tvAuthor.setText(""+author);
		*/
	}
	private void showLyrics(String name)
	{
		Logger.v("showLyrics %s",name);
		ShowLyricsAsyncTask task=new ShowLyricsAsyncTask(lvLyrics,name);
		task.execute();
	}
    @Override
	public void showMetadata(Uri uri)
	{
		if(uri==null)
			return;
		mMediaMetadataRetriever.setDataSource(this,uri);
		String name=mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		String author=mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		tvAuthor.setText(""+author);
		tvName.setText(""+name);
		showLyrics(name);
	}
	@Override
	public void onClick(View view)
	{
		Intent intent=null;
		switch(view.getId())
		{
			case R.id.play:
				intent=new Intent();
				intent.setAction(BroadcastReceiverConstants.CONTROL);
				intent.putExtra(BroadcastReceiverConstants.CONTROL,BroadcastReceiverConstants.START_OR_PAUSE);
				sendBroadcast(intent);
				break;
			case R.id.open:
				intent=new Intent();
				intent.setType("audio/");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent,0);
				break;
			case R.id.list:
				intent=new Intent();
				intent.setClass(this,PlayList.class);
				startActivityForResult(intent,0);
				break;
		}
	}
	@Override
	public void onActivityResult(int reqCode,int resultCode,Intent data)
	{
		onNewIntent(data);
	}
	@Override
	public void pause()
	{
		ibPlay.setImageResource(android.R.drawable.ic_media_play);
		isPlaying=false;
	}
    @Override
	public void start()
	{
		ibPlay.setImageResource(android.R.drawable.ic_media_pause);
		isPlaying=true;
	}
	private void findView()
	{
		lvLyrics=(ListView)findViewById(R.id.lyrics);
		tvTitle=(TextView)findViewById(R.id.title);
		tvName=(TextView)findViewById(R.id.name);
		tvAuthor=(TextView)findViewById(R.id.author);
		tvTime=(TextView)findViewById(R.id.time);
		ibPlay=(ImageButton)findViewById(R.id.play);
		ibList=(ImageButton)findViewById(R.id.list);
		vOpen=findViewById(R.id.open);
		pbProgress=(SeekBar)findViewById(R.id.progress);
	}
	@Override
	public void onDestroy()
	{
		Logger.v("Main onDestroy()");
		super.onDestroy();
	}
	@Override
	public void onBackPressed()
	{
		if(isPlaying&&SimpleDialog.show(this,"退出","是否后台播放？"))
		{
			Logger.v("deamon");
			showNotification=true;
		}
		else
		{
			showNotification=false;
			stopService(mServiceIntent);
		}
		// */
		super.onBackPressed();
		//finish();
	}
}
