package com.example.listviewhorizontal;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;



public class ItemAdapter extends BaseAdapter {

	//	List<ItemRow> data; 
	Context context;
	int layoutResID;
	private  Context activity;
	ArrayList<String> artitle;
	ArrayList<String> arimage;
	public TextView title,urls;
	ArrayList<Integer> suret;
	//	StoreDatabase timedb;
	//	SQLiteDatabase sqdb;
	public ImageView icon;
	//	ImageView icon;
	public ItemAdapter(Activity getContacts,ArrayList<String> arrTitle, ArrayList<String> arrImage,
			ArrayList<Integer> suret){
		this.activity = getContacts;
		this.artitle=arrTitle;
		this.arimage=arrImage;
		this.suret=suret;
		Log.d("DELLL", arrTitle+"");

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return artitle.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return artitle.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	public class ViewHolder
	{

		public TextView title;
		public TextView urls;
		public ImageView icon;
		public Button btn;
		public ProgressBar pb;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder view;
		LayoutInflater inflator = ((MainActivity) activity).getLayoutInflater();
		if(convertView==null)
		{
			view = new ViewHolder();
			convertView = inflator.inflate(R.layout.viewitem, null);
			view.title = (TextView)convertView.findViewById(R.id.title);
			//			view.btn = (Button)convertView.findViewById(R.id.clickbutton);
			view.icon=(ImageView)convertView.findViewById(R.id.image);
			view.pb=(ProgressBar)convertView.findViewById(R.id.progressBar1);
			convertView.setTag(view);
		}
		else
		{
			view = (ViewHolder) convertView.getTag();
		}

		view.title.setText(artitle.get(position));
		//		view.icon.setBackgroundResource(suret.get(position));
		Log.d("SURETT", suret.get(position)+"");
		//		view.urls.setText(arimage.get(position))
		(new GetXMLTask(view.icon,view.pb)).execute(arimage.get(position));	
		return convertView;
	}
	class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
		ImageView iv;
		ProgressBar pb;
		public GetXMLTask(ImageView iv,ProgressBar pb) {
			this.iv = iv;
			this.pb=pb;
		}


		@Override
		protected Bitmap doInBackground(String... urls) {
			Bitmap map = downloadImage(urls[0]);
			return map;
		}

		// Sets the Bitmap returned by doInBackground
		@Override
		protected void onPostExecute(Bitmap result) {
			if(result == null){
				pb.setVisibility(View.VISIBLE);
			}
			else
				pb.setVisibility(View.INVISIBLE);
				iv.setImageBitmap(result);
		}

		// Creates Bitmap from InputStream and returns it
		private Bitmap downloadImage(String url) {
			Bitmap bitmap = null;
			InputStream stream = null;
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inSampleSize = 1;

			try {
				stream = getHttpConnection(url);
				bitmap = BitmapFactory.
						decodeStream(stream, null, bmOptions);
				stream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return bitmap;
		}

		// Makes HttpURLConnection and returns InputStream
		private InputStream getHttpConnection(String urlString)
				throws IOException {
			InputStream stream = null;
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();

			try {
				HttpURLConnection httpConnection = (HttpURLConnection) connection;
				httpConnection.setRequestMethod("GET");
				httpConnection.connect();

				if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					stream = httpConnection.getInputStream();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return stream;
		}
	}
}

