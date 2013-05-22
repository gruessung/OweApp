package de.gvisions.oweapp;

import android.app.ListFragment;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MainFragmentList extends ListFragment {
	
	SQLiteOpenHelper database;
	SQLiteDatabase connection;
	
	//Array mit ListElements
	ArrayList<MainListElements> mainListElements;
	
	//ListAdapter
	private MyListAdapter myAdapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
        database = new DatabaseHelper(getActivity().getApplicationContext());
        connection = database.getReadableDatabase();
        
        mainListElements = new ArrayList<MainListElements>();

        buildData(); //Daten aus DB holen und in ArrayList speichern
        
		myAdapter = new MyListAdapter();
		myAdapter.notifyDataSetChanged();
		setListAdapter(myAdapter);
		getListView().setOnItemClickListener(myAdapter);

        
	}
	
	/**
	 * L�dt die Daten aus der DB und speichert diese in eizelnen ListElements im Array
	 */
    private void buildData() {
        String typeString = "";
        int anzahlVerliehen = 0, anzahlGeliehen = 0;

        Cursor result = connection.rawQuery("SELECT sum(case when type = '0' then 1 else 0 end) geliehen, sum(case when type = '1' then 1 else 0 end) verliehen, fromTo, contacturi, type FROM owe GROUP BY fromTo;", null);
        while(result.moveToNext ())
        {
            for (int i = 0; i < result.getColumnCount(); i++)
            {
                Log.d("TAG", result.getColumnName(i) + " -- " + result.getString(i));
            }

            anzahlGeliehen = result.getInt(0);

        	anzahlVerliehen = result.getInt(1);


            Log.d("TAGGV", String.valueOf(anzahlGeliehen) );
            Log.d("TAGGV", String.valueOf(anzahlVerliehen));
        	
        	this.mainListElements.add(
					new MainListElements(
							anzahlGeliehen,
                            anzahlVerliehen,
                            result.getString(2),
							result.getString(3)//Kontakt f�r Bagde
							) 
					);	
        	}
        


      }

    class MyListAdapter extends BaseAdapter implements OnItemClickListener {

  		private final LayoutInflater mInflater;
  		
  		public MyListAdapter() {
  			mInflater = (LayoutInflater) MainFragmentList.this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  		}

  		public int getCount() {
  			return mainListElements.size();
  		}

  		public MainListElements getItem(int position) {
  			return mainListElements.get(position);
  		}

  		public long getItemId(int position) {
  			return (long) position;
  		}

  		public View getView(int position, View convertView, ViewGroup parent) {
  			LinearLayout itemView = (LinearLayout) mInflater.inflate(R.layout.main_list_item, parent, false);
  			bindView(itemView, position);
  			return itemView;
  		}
  		
  		private void bindView(LinearLayout view, int position) {
  			MainListElements datensatz = getItem(position);
  			view.setId((int) getItemId(position));
  			
  			// View Elemente suchen und belegen
  			TextView name = (TextView) view.findViewById(R.id.name);
  			TextView anzahlV = (TextView) view.findViewById(R.id.anzahlV);
  			TextView anzahlAusg = (TextView) view.findViewById(R.id.anzahlAusg);
  			
  			QuickContactBadge contactBadge = (QuickContactBadge) view.findViewById(R.id.quickContactBadge1);
  			
  			//Daten �bergeben an View Elemente
  			name.setText(datensatz.name);
  			anzahlV.setText("Sie/Er schuldet dir noch " + String.valueOf(datensatz.anzahlVerliehen) + " Objekte");
  			anzahlAusg.setText("Du schuldest ihm/ihr noch " + String.valueOf(datensatz.anzahlAusgeliehen) + " Objekte");
  			
  			
  			//ContactBadge
  			String contactID = null;
  			Uri contactUri = Uri.parse(datensatz.kontakt);
  			Log.d("KONTAKT", datensatz.name + " -- " +contactUri);
  			if (URLUtil.isValidUrl(datensatz.kontakt))
  			{
	  			Cursor cursorID = getActivity().getContentResolver().query(contactUri, new  String[]{ContactsContract.Contacts._ID}, null, null, null);
	            if (cursorID.moveToFirst()) {
	                      contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
	            }                 
	            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getActivity().getContentResolver(),
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

  		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


//  			FragmentDetail fragment = (FragmentDetail)getFragmentManager().findFragmentById(R.id.fragment_list);
  	      
  			MainListElements item = getItem(position);
  	      /*
		      if (fragment != null && fragment.isInLayout()) {
		
		      	fragment.setType(item.leihRichtung);
		
		      	fragment.setContact(item.name);
		      	fragment.setWas(item.objekt);
		      	fragment.setDesc(item.beschreibung);  
		      	fragment.setDatum(item.datum);
		          
		           
		
		      } else {*/
		        
		        Intent intent = new Intent(getActivity().getApplicationContext(), ListActivity.class);
		        intent.putExtra("kontakt", item.kontakt);
		        startActivity(intent); 
		
		     // }

  		}


  	}

}
