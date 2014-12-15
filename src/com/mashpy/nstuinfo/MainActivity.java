package com.mashpy.nstuinfo;

import android.os.Bundle;
import java.util.ArrayList;

import com.winsontan520.wversionmanager.library.WVersionManager;

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

public class MainActivity extends ListActivity {
    private static final String DB_NAME = "nstuinfodb1.2";
    
	private static final String TABLE_NAME = "nstuinfo_first";
	private static final String NSTUINFO_ID = "_id";
	private static final String NSTUINFO_NAME = "name";
	private SQLiteDatabase database;
	private ListView listView;
	private ArrayList<String> nstuinfofirst;
	private ArrayList<String> nstuinfodetails;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WVersionManager versionManager = new WVersionManager(this);
        versionManager.setVersionContentUrl("https://raw.githubusercontent.com/Mashpy/nstuinfo/master/version.txt"); // your update content url, see the response format below
        versionManager.checkVersion();
        
        versionManager.setUpdateNowLabel("Update Now (Click here)");
        versionManager.setRemindMeLaterLabel("");
        versionManager.setIgnoreThisVersionLabel("");
        versionManager.setUpdateUrl("http://nstuinfo.github.com"); // this is the link will execute when update now clicked. default will go to google play based on your package name.
        versionManager.setReminderTimer(10); // this mean checkVersion() will not take effect within 10 minutes

        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(this, DB_NAME);
        database = dbOpenHelper.openDataBase();
        fillNstuinfo();
        setUpList();      
    }

	private void setUpList() {
		setListAdapter(new ArrayAdapter<String>(this,
		R.layout.simple_list_item_1, nstuinfofirst));
		listView = getListView();
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long offset) {	
			Intent intent = new Intent(getApplicationContext(), NstuInfoDetails.class);
			String mashpy= ((TextView) view).getText().toString();
			fillNstuinfodetails(mashpy);
			String s = nstuinfodetails.get(0);
			intent.putExtra("New_Topic", s);
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
											 , null);
		friendCursor.moveToFirst();
		if(!friendCursor.isAfterLast()) {
			do {
				String name = friendCursor.getString(1);
				nstuinfofirst.add(name);
			} while (friendCursor.moveToNext());
		}
		friendCursor.close();
}
	public void fillNstuinfodetails(String mashpy) {
	nstuinfodetails = new ArrayList<String>();
	String selectQueryString= "select details from nstuinfo_first where name= '"+mashpy+"'";
	Cursor friendCursordetails = database.rawQuery(selectQueryString, null);
	friendCursordetails.moveToFirst();
	if(!friendCursordetails.isAfterLast()) {
		do {
			String name = friendCursordetails.getString(0);
			nstuinfodetails.add(name);
		} while (friendCursordetails.moveToNext());
	}
	friendCursordetails.close();
	}	
	
}