package com.example.listviewhorizontal;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Setting extends Activity implements OnClickListener{
	private ProgressDialog pDialog;
	private static String url = "http://www.kazakhstanzaman.kz/?json=get_category_index";
	ArrayList<HashMap<String, String>> contactList;
	JSONArray contacts = null;
	String cont;
	ListView lv;
	ProgressBar pb;
	ArrayList<String> arrList,ar;
	SharedPreferences sPref;
	String names[];
	String hello[]={"hello","jek"};
	ArrayAdapter adapter;
	String cat = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		//		sPref = getPreferences(MODE_PRIVATE);
		//	    String savedText = sPref.getString("saved_text", "");
		arrList=new ArrayList<String>();
		ar=new ArrayList<String>();
		lv=(ListView)findViewById(R.id.list);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		new GetCategories(this).execute();
		names = getResources().getStringArray(R.array.names);
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				SparseBooleanArray sbArray = lv.getCheckedItemPositions();
				for (int i = 0; i < sbArray.size(); i++) {
					int key = sbArray.keyAt(i);
					if (sbArray.get(key)){
						Log.d("LIST", arrList.get(key));
						sPref = getPreferences(MODE_PRIVATE);
						Editor ed = sPref.edit();
						ed.putString("saved_text", names[key]);
						ed.commit();
						Toast.makeText(getApplicationContext(), "Text saved", Toast.LENGTH_SHORT).show();
					}
				}
			}

		});
	}
	public void onClick(View v) {
		// пишем в лог выделенные элементы
	}
	private class GetCategories extends AsyncTask<Void, Void, Void> {
		Activity activity;
		public GetCategories(Setting setting){
			activity = setting;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("myFilter", "preEx");
			// Showing progress dialog
			pDialog = new ProgressDialog(Setting.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			Log.d("myFilter",  jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					contacts = jsonObj.getJSONArray("categories");
					// looping through All Contacts
					for (int i = 0; i < contacts.length(); i++) {
						JSONObject c = contacts.getJSONObject(i);
						cat=c.getString("title");
						Log.d("JSON", cat);
						arrList.add(cat);
						Log.d("Cats:", arrList.get(i));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			adapter = new ArrayAdapter(Setting.this,android.R.layout.simple_list_item_multiple_choice, arrList);
			lv.setAdapter(adapter);
			if (pDialog.isShowing())
				pDialog.dismiss();
		}
	}
}
