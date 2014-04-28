package com.example.listviewhorizontal;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;

public class MainActivity extends Activity {
	private ProgressDialog pDialog;

	// URL to get contacts JSON
	private static String url = "http://www.kazakhstanzaman.kz/?json=get_posts&include=thumbnail,title,content";
	// JSON Node names
	private static final String TAG_POSTS = "posts";
	private static final String TAG_TITLE = "title";
	private static final String TAG_CONTENT = "content";
	private static final String TAG_THUMBNAIL = "thumbnail";
	boolean take=true;
	ProgressBar pb;
	ArrayList<String> arrList;
	HorizontalListView listview;
	// contacts JSONArray
	ArrayList<String> arrTitle,arrImage;
	ArrayList<Integer> suret;
	ItemAdapter itemAdapter;
	HorizontalListView bt,bt2,bt3,bt4,bt5;
	JSONArray contacts = null;
	LinearLayout ll; 
	String cont;
	int suret2[]={R.drawable.icons,R.drawable.sec,R.drawable.third,
			R.drawable.icons,R.drawable.sec,R.drawable.third,
			R.drawable.icons,R.drawable.sec,R.drawable.third,R.drawable.third};
	// Hashmap for ListView
	ArrayList<HashMap<String, String>> contactList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.listviewdemo); 
		contactList = new ArrayList<HashMap<String, String>>();
		//		cont=(TextView) findViewById(R.id.email);
		//		url.replaceAll("<strong>", "");
		//		lv = getListView();
		arrList=new ArrayList<String>();
		arrTitle=new ArrayList<String>();
		arrImage=new ArrayList<String>();
		ll = (LinearLayout) findViewById(R.id.main);
		suret=new ArrayList<Integer>();
		listview = (HorizontalListView) findViewById(R.id.listview);
		new GetContacts(this).execute();
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String name = ((TextView) view.findViewById(R.id.title))
						.getText().toString();
				//				String cost = ((TextView) view.findViewById(R.id.email))
				//						.getText().toString();

				// Starting single contact activity
				Intent in = new Intent(getApplicationContext(),
						SingleActivity.class);
				in.putExtra(TAG_CONTENT, name);
				in.putExtra(TAG_TITLE, arrList.get(position));
				//				in.putExtra(TAG_THUMBNAIL, image.toString());
				//                in.putExtra(TAG_PHONE_MOBILE, description);
				startActivity(in);

			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar actions click
		Intent in=new Intent(this,Setting.class);
		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(in);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	//	private static String[] dataObjects = new String[]{ "Text #1",
	//		"Text #2",
	//		"Text #3" ,"Hello","HTC","iPhone","Football*"}; 




	private class GetContacts extends AsyncTask<Void, Void, Void> {
		Activity activity;
		public GetContacts(MainActivity mainActivity){
			activity = mainActivity;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("myFilter", "preEx");
			// Showing progress dialog
			pDialog = new ProgressDialog(MainActivity.this);
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
					contacts = jsonObj.getJSONArray("posts");

					// looping through All Contacts
					for (int i = 0; i < contacts.length(); i++) {
						JSONObject c = contacts.getJSONObject(i);
						String url2="noThumbnail";
						if (c.has("thumbnail")) {
							url2 = c.getString("thumbnail");
						}
						arrImage.add(url2);
						Log.d("Kel", arrImage+"");
						String id = c.getString(TAG_CONTENT);//uzyn zhazular
						id = id.replaceAll("<(.*?)\\>"," ");
						arrList.add(id);
						String title = c.getString(TAG_TITLE);
						arrTitle.add(title);
						HashMap<String, String> contact = new HashMap<String, String>();
						take=false;
						// adding each child node to HashMap key => value
						contact.put(TAG_CONTENT, id);
						contact.put(TAG_TITLE, title);
						contact.put(TAG_THUMBNAIL, url2);
						//						String suret2[]=getResources().getStringArray(R.array.images);
						//						TypedArray image=getResource().
						suret.add(suret2[i]);
						//						GetXMLTask task = new GetXMLTask();
						// Execute the task
						//						task.execute(new String[] { arrImage.get(i) });
						// adding contact to contact list
						contactList.add(contact);
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
			itemAdapter=new ItemAdapter(activity,arrTitle,arrImage,suret);
			ItemAdapter sd = new ItemAdapter(activity, arrTitle, arrImage, suret);
			bt= new HorizontalListView(activity, null);
			bt2= new HorizontalListView(activity, null);
			bt3= new HorizontalListView(activity, null);
//			bt4= new HorizontalListView(activity, null);
//			bt5= new HorizontalListView(activity, null);
			//LinearLayout ll = (LinearLayout)activity.findViewById(R.id.main);
//			bt.setLayoutParams(new LinearLayout.LayoutParams(1, 180, LayoutParams.MATCH_PARENT));
//			bt.setLayoutParams(new LinearLayout.LayoutParams(4, 180, LayoutParams.MATCH_PARENT));
			ll.addView(bt,LayoutParams.MATCH_PARENT, 360);
			ll.addView(bt2, LayoutParams.MATCH_PARENT, 360);
			ll.addView(bt3, LayoutParams.MATCH_PARENT, 360);
//			ll.addView(bt4);
//			ll.addView(bt5);
			bt.setAdapter(itemAdapter);
			bt2.setAdapter(sd);
			bt3.setAdapter(itemAdapter);
//			bt4.setAdapter(itemAdapter);
//			bt5.setAdapter(itemAdapter);
			listview.setAdapter(itemAdapter);
			Log.d("kellll", arrImage+"");
			//			pb.setVisibility(View.INVISIBLE);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			/*ListAdapter adapter = new SimpleAdapter(
								MainActivity.this, contactList,
								R.layout.list_item, new String[] {TAG_TITLE, TAG_THUMBNAIL}
								, new int[] { R.id.name,R.id.email});*/
			//			itemData=new ArrayList<ItemRow>();
			//			itemAdapter=new ItemAdapter(this,arrTitle,arrImage);
			//setListAdapter(adapter);
			//			lv.setAdapter(itemAdapter);
			//			cont.setVisibility(View.INVISIBLE);
			//			Intent a=new Intent();
			//			a.putExtra("takyryp", arrTitle);
			//			a.putExtra("suret", arrImage);
			//			startActivity(a);
			//			itemData=new ArrayList<ItemRow>();
		}
	}
}
