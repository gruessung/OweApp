package de.gvisions.oweapp;

import java.io.BufferedInputStream;
import java.io.InputStream;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.widget.QuickContactBadge;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

public class ShowItem extends Activity {

	int itemID;
	
	SQLiteOpenHelper database;
	SQLiteDatabase connection;
	
	TextView tvShow1, tvShow2, tvShow3, tvShow4, tvDatum;
	String what, contact, description, type, idLong, datum;
	
	String name, leihr, beschreibung, kontakt, datum2, objekt;
	
	private ShareActionProvider mShareActionProvider;
	
	
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.show_item);
        
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        database = new DatabaseHelper(this);
        connection = database.getWritableDatabase();
        
        Bundle e = getIntent().getExtras();
        
        itemID = getIntent().getExtras().getInt("id");
        name = e.getString("name");
        kontakt = e.getString("kontakt");
        beschreibung = e.getString("beschreibung");
        leihr = e.getString("leihrichtung");
        datum2 = e.getString("datum");
        objekt = e.getString("objekt");
        
        tvShow1 = (TextView) findViewById(R.id.tvShow1);
        tvShow2 = (TextView) findViewById(R.id.tvShow2);
        tvShow3 = (TextView) findViewById(R.id.tvShow3);
        tvShow4 = (TextView) findViewById(R.id.tvShow4);
        tvDatum = (TextView) findViewById(R.id.tvDatum);
        
        	
        tvShow2.setText(leihr);
    	tvShow1.setText(name);
    	tvShow3.setText(objekt);    
    	tvShow4.setText(beschreibung);  
    	tvDatum.setText( getString(R.string.date) + ": " + datum2);
    	
		QuickContactBadge contactBadge = (QuickContactBadge) findViewById(R.id.contact);

    	
    	//ContactBadge
		String contactID = null;
		Uri contactUri = Uri.parse(kontakt);
		if (URLUtil.isValidUrl(kontakt))
		{
		Cursor cursorID = getContentResolver().query(contactUri, new  String[]{ContactsContract.Contacts._ID}, null, null, null);
        if (cursorID.moveToFirst()) {
                  contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }                 
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));
       
        BufferedInputStream buf =new BufferedInputStream(input);
        Bitmap my_btmp = BitmapFactory.decodeStream(buf);
        
        if(my_btmp != null)
            contactBadge.setImageBitmap(my_btmp);
        else
        	contactBadge.setImageResource(R.drawable.ic_default); 
        contactBadge.assignContactUri(contactUri); 
        contactBadge.setMode(ContactsContract.QuickContact.MODE_LARGE);  
		}
        
        
    }
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.activity_show, menu);

        /*
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/*");
        
        if (type.equals("0"))
    	{
        	
        	shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mailtext_has_owe, contact, what, datum));
    		tvShow1.setText(getString(R.string.show_hase_owe));
    	}
    	else
    	{
    		shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mailtext_owe_to, what, datum));
    		tvShow1.setText(getString(R.string.show_owe_to));
    	}
        
        setShareIntent(shareIntent);
        */
        
        // Return true to display menu
        return true;
    }
    
    
    /*
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
    */
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {    
       switch (item.getItemId()) 
       {        
          case android.R.id.home:            
             Intent intent = new Intent(this, ListActivity.class);
             intent.putExtra("kontakt", kontakt);
             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
             startActivity(intent);            
             return true;  
          case R.id.menu_delete:            
              //Löschen
        	  connection.delete("owe", "id IN ('"+itemID+"')", null);
        	  Toast.makeText(this, getString(R.string.delete), Toast.LENGTH_SHORT).show();
              
        	  Intent intent2 = new Intent(this, MainActivity.class);            
              intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
              startActivity(intent2);  
        	  
        	  return true; 

          default:            
             return super.onOptionsItemSelected(item);    
       }
    }
    

}
