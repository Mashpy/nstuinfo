package com.mashpy.nstuinfo;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.*;

public class GridAdapter extends BaseAdapter
{

	@Override
	public Object getItem(int p1)
	{
		return gridText.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		return p1;
	}
	
	ArrayList<String> gridText;
	Context gContext;
	LayoutInflater li;
	
	public GridAdapter(Context context ,ArrayList<String> arrayList)
	{
		this.gContext=context;
		this.gridText=arrayList;
	}
	
	@Override
	public int getCount()
	{
		return gridText.size();
	}


	@Override
	public View getView(int p1, View p2, ViewGroup p3)
	{
		li=(LayoutInflater)gContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		p2=li.inflate(R.layout.simple_list_item_1,null);
		((TextView)p2.findViewById(R.id.simplelistitem1TextView1)).setText(gridText.get(p1));
		((ImageView)p2.findViewById(R.id.simplelistitem1ImageView1)).
		setImageResource(gContext.getResources().getIdentifier("icon"+p1,"drawable",gContext.getPackageName()));
		return p2;
	}
	
}
