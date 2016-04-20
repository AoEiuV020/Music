/* ***************************************************
	^> File Name: PlayList.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/21 - 01:38:07
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
import java.util.*;
import java.io.*;
public class PlayList extends Activity implements AdapterView.OnItemClickListener
{
	@Override
	public void onCreate(Bundle save)
	{
		super.onCreate(save);
		setContentView(R.layout.layout_playlist);
		ListView list=(ListView)findViewById(R.id.playlist_listview);
		PlayListAdapter adapter=new PlayListAdapter(this);
		adapter.addAll(getData());
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	}
	public List<Item> getData()
	{
		List<Item> list=new LinkedList<Item>();
		int i=0;
		for(Uri uri:MusicProvider.getDefaultList(this))
		{
			Item item=new Item();
			item.uri=uri;
			item.name=""+uri;
			list.add(item);
			Logger.v("getData %s",item);
		}
		return list;
	}
	@Override
	public void onItemClick(AdapterView<?> parent,View view,int position,long id)
	{
		Item item=(Item)parent.getAdapter().getItem(position);
		Uri uri=item.uri;
		Intent intent=new Intent();
		intent.setDataAndType(uri,"audio/");
		setResult(RESULT_OK,intent);
		finish();
	}
}
class PlayListAdapter extends BaseAdapter
{
	List<Item> mList=null;
	private Context mContext=null;
	private LayoutInflater mInflater=null;
	public PlayListAdapter(Context context)
	{
		mList=new LinkedList<Item>();
		mContext=context;
		mInflater=LayoutInflater.from(context);
	}
	public void add(Item item)
	{
		mList.add(item);
	}
	public void addAll(List<Item> list)
	{
		mList.addAll(list);
	}
	@Override
	public int getCount()
	{
		return mList.size();
	}
	@Override
	public Object getItem(int position)
	{
		return mList.get(position);
	}
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	@Override
	public View getView(int position,View convertView,ViewGroup parent)
	{
		View view=null;
		ViewHolder holder=null;
		if(convertView==null)
		{
			view=mInflater.inflate(R.layout.layout_playlist_item,null);
			holder=new ViewHolder();
			holder.name=(TextView)view.findViewById(R.id.item_name);
			view.setTag(holder);
		}
		else
		{
			view=convertView;
			holder=(ViewHolder)view.getTag();
		}
		holder.name.setText(""+mList.get(position).name);
		return view;
	}
	public class ViewHolder
	{
		public TextView name;
	}
}
class Item
{
	String name=null;
	Uri uri=null;
	public String toString()
	{
		return String.format("{name=%s,uri=%s}",name,uri);
	}
}
