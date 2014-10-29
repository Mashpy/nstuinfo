package com.mashpy.nstuinfo;

import android.os.Bundle;
import java.util.ArrayList;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
    private static final String DB_NAME = "nstuinfodb.sqlite3";
    
	private static final String TABLE_NAME = "nstuinfo_first";
	private static final String NSTUINFO_ID = "_id";
	private static final String NSTUINFO_NAME = "name";
    
	private SQLiteDatabase database;
	private ListView listView;
	private ArrayList<String> nstuinfofirst;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(this, DB_NAME);
        database = dbOpenHelper.openDataBase();
        fillNstuinfo();
        setUpList();      
    }

	private void setUpList() {
		setListAdapter(new ArrayAdapter<String>(this,
		android.R.layout.simple_list_item_1, nstuinfofirst));
		listView = getListView();
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long offset) {	
			Intent intent = new Intent(getApplicationContext(), NstuInfoDetails.class);
			String mashpy= ((TextView) view).getText().toString();
			intent.putExtra("New_Topic", mashpy);
			startActivity(intent);
			}
			});
	}
	

	private void fillNstuinfo() {
		nstuinfofirst = new ArrayList<String>();
		Cursor friendCursor = database.query(TABLE_NAME,
											 new String[] 
											 {NSTUINFO_ID, NSTUINFO_NAME},
											 null, null, null, null
											 , NSTUINFO_NAME);
		friendCursor.moveToFirst();
		if(!friendCursor.isAfterLast()) {
			do {
				String name = friendCursor.getString(1);
				nstuinfofirst.add(name);
			} while (friendCursor.moveToNext());
		}
		friendCursor.close();
	}
}