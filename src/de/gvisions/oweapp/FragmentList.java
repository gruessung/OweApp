package de.gvisions.oweapp;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;

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

public class FragmentList extends ListFragment {
	
	SQLiteOpenHelper database;
	SQLiteDatabase connection;
	
	//Array mit ListElements
	ArrayList<ListElements> listElements;
	
	//ListAdapter
	private MyListAdapter myAdapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
        database = new DatabaseHelper(getActivity().getApplicationContext());
        connection = database.getReadableDatabase();
        
        listElements = new ArrayList<FragmentList.ListElements>();

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
        
        Cursor result = connection.rawQuery("select * from owe", null);
        while(result.moveToNext ())
        {
        	if (result.getString(5).equals("0"))
        	{
        		typeString = getString(R.string.show_hase_owe);
        	}
        	else
        	{
        		typeString = getString(R.string.show_owe_to);
        	}

        	for (int i = 0; i < result.getColumnCount(); i++)
        	{
        		Log.d("DB", result.getColumnName(i) + " -- " + result.getString(i) );
        	}
        	
        	
        	this.listElements.add(
					new ListElements(
							result.getInt(0), //ID
							result.getString(4),
							result.getString(result.getColumnIndex(result.getColumnName(3))), //Name
							result.getString(result.getColumnIndex(result.getColumnName(2))), //Objekt
							result.getString(result.getColumnIndex(result.getColumnName(6))), //Datum
							typeString, //Leihrichtung
							result.getString(1)//Kontakt f�r Bagde
							) 
					);	
        	}
        


      }


    
    /**
     * ListElements
     * @author agruessu
     *
     */
    class ListElements {
    	
    	/**
    	 * ID in DB
    	 */
    	public int id;

    	/**
    	 * Beschreibung
    	 */
    	public String beschreibung;
    	
    	/**
    	 * Der Name des Jenigen
    	 */
    	public String name;
    	
    	/**
    	 * Das Objekt (zB Buch), um das es geht
    	 */
    	public String objekt;
    	
    	/**
    	 * Datum der R�ckgabe
    	 */
    	public String datum;
    	
    	/**
    	 * Richtung des Leihens
    	 * "Ich leihe dir..." <-> "Ich habe mir ausgeliehen..."
    	 */
    	public String leihRichtung;
    	
    	/**
    	 * Kontakt f�r ContactBadge
    	 */
    	public String kontakt;
    	
    	/**
    	 * Konstruktor
    	 */
    	public ListElements(int id, String b, String name, String objekt, String datum, String leihRichtung, String kontakt)
    	{
    		this.beschreibung = b;
    		this.id = id;
    		this.name = name;
    		this.objekt = objekt;
    		this.datum = datum;
    		this.leihRichtung = leihRichtung;
    		this.kontakt = kontakt;
    	}
    	

    }
    
    
    class MyListAdapter extends BaseAdapter implements OnItemClickListener {

  		private final LayoutInflater mInflater;
  		
  		public MyListAdapter() {
  			mInflater = (LayoutInflater) FragmentList.this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  		}

  		public int getCount() {
  			return listElements.size();
  		}

  		public ListElements getItem(int position) {
  			return listElements.get(position);
  		}

  		public long getItemId(int position) {
  			return (long) position;
  		}

  		public View getView(int position, View convertView, ViewGroup parent) {
  			LinearLayout itemView = (LinearLayout) mInflater.inflate(R.layout.list_item, parent, false);
  			bindView(itemView, position);
  			return itemView;
  		}
  		
  		private void bindView(LinearLayout view, int position) {
  			ListElements datensatz = getItem(position);
  			view.setId((int) getItemId(position));
  			
  			// View Elemente suchen und belegen
  			TextView name = (TextView) view.findViewById(R.id.fromTo);
  			TextView objekt = (TextView) view.findViewById(R.id.what);
  			TextView datum = (TextView) view.findViewById(R.id.datum);
  			TextView leihRichtung = (TextView) view.findViewById(R.id.desc);
  			
  			QuickContactBadge contactBadge = (QuickContactBadge) view.findViewById(R.id.quickContactBadge1);
  			
  			//Daten �bergeben an View Elemente
  			name.setText(datensatz.name);
  			objekt.setText(datensatz.objekt);
  			datum.setText(datensatz.datum);
  			leihRichtung.setText(datensatz.leihRichtung);
  			
  			
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
  			
  			FragmentDetail fragment = (FragmentDetail)getFragmentManager().findFragmentById(R.id.fragment_detail);
  	      
  			ListElements item = getItem(position);
  	      
		      if (fragment != null && fragment.isInLayout()) {
		
		      	fragment.setType(item.leihRichtung);
		
		      	fragment.setContact(item.name);
		      	fragment.setWas(item.objekt);
		      	fragment.setDesc(item.beschreibung);  
		      	fragment.setDatum(item.datum);
		          
		           
		
		      } else {
		        
		        Intent intent = new Intent(getActivity().getApplicationContext(), ShowItem.class);   
		        intent.putExtra("leihrichtung", item.leihRichtung);
		        intent.putExtra("name", item.name);
		        intent.putExtra("objekt", item.objekt);
		        intent.putExtra("beschreibung", item.beschreibung);
		        intent.putExtra("datum", item.datum);
		        intent.putExtra("id", item.id);
		        intent.putExtra("kontakt", item.kontakt);
		        startActivity(intent); 
		
		      }
  		}


  	}

}
