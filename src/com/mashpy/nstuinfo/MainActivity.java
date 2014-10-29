package com.mashpy.nstuinfo;

import android.os.Bundle;
import java.util.ArrayList;
import android.app.ListActivity;
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
    private static final String DB_NAME = "yourdb.sqlite3";
    //������� ��������� �������� ������� ���� ����� �� �����������
	private static final String TABLE_NAME = "friends";
	private static final String FRIEND_ID = "_id";
	private static final String FRIEND_NAME = "name";
    
	private SQLiteDatabase database;
	private ListView listView;
	private ArrayList<String> friends;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //��� �������� ������
        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(this, DB_NAME);
        database = dbOpenHelper.openDataBase();
        //���, ���� �������!
        fillFreinds();
        setUpList();      
    }

	private void setUpList() {
		//��������� ����������� ������� � layout �������� ��� ���������
		setListAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, friends));
		listView = getListView();
		
		//������� ���� ����, ��� ����
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
								int position,long id) {
				Toast.makeText(getApplicationContext(),
							((TextView) view).getText().toString(),
							 Toast.LENGTH_SHORT).show();				
			}
		});
	}
	
	//���������� �������� �� ���� ������
	private void fillFreinds() {
		friends = new ArrayList<String>();
		Cursor friendCursor = database.query(TABLE_NAME,
											 new String[] 
											 {FRIEND_ID, FRIEND_NAME},
											 null, null, null, null
											 , FRIEND_NAME);
		friendCursor.moveToFirst();
		if(!friendCursor.isAfterLast()) {
			do {
				String name = friendCursor.getString(1);
				friends.add(name);
			} while (friendCursor.moveToNext());
		}
		friendCursor.close();
	}
}