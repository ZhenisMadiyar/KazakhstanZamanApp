package com.example.listviewhorizontal;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleActivity  extends Activity {
	
	// XML node keys
	static final String KEY_TITLE = "title";
	static final String KEY_CONTENT = "content";
//	static final String KEY_MOBILE = "mobile";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get XML values from previous intent
        String cont = in.getStringExtra(KEY_TITLE);
        String name = in.getStringExtra(KEY_CONTENT);
//        String description = in.getStringExtra(KEY_MOBILE);
        
        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblCont = (TextView) findViewById(R.id.cont_label);
//        TextView lblDesc = (TextView) findViewById(R.id.description_label);
        
        lblName.setText(name);
        lblCont.setText(cont);
//        lblDesc.setText(description);
    }
}
