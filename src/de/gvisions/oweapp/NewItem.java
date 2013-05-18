package de.gvisions.oweapp;


import java.io.InputStream;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewItem extends Activity {

	Spinner spinner;
	EditText what;
	EditText fromTo;
	EditText Desc;
	EditText date;
	
	Button select;
	
	CheckBox calendar;
	
	String id = null; //Kontakt ID
	String idLong = null;
	Uri contactUri = null;
	
	int month, year, day;
	
	SQLiteOpenHelper database;
	SQLiteDatabase connection;
	
	
	static final int DATE_DIALOG_ID = 999;
	
	private static final int CONTACT_PICKER_RESULT = 1001;  
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
		   // set date picker as current date
		   return new DatePickerDialog(this, datePickerListener, 
                         year, month,day);
		}
		return null;
	}
 
	private DatePickerDialog.OnDateSetListener datePickerListener 
                = new DatePickerDialog.OnDateSetListener() {
 
		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
 
			// set selected date into textview
			date.setText(new StringBuilder().append(day)
			   .append(".").append(month + 1).append(".").append(year)
			   .append(" "));

 
		}
	};
	
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.new_item);
        
        SharedPreferences prefs = PreferenceManager
		        .getDefaultSharedPreferences(getBaseContext());
        
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        select = (Button) findViewById(R.id.button1);
        date = (EditText) findViewById(R.id.etDate);
        calendar = (CheckBox) findViewById(R.id.cbCalendar);
        
        Boolean cbCal = prefs.getBoolean("defaultKalender",true);
        if (cbCal == false)
        {
        	calendar.setChecked(false);
        }
        else
        {
        	calendar.setChecked(true);
        }
        
        
        final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
        

        date.setClickable(true);
        date.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
        
        
        select.setOnClickListener(new OnClickListener() {
		
			public void onClick(View v) {
				Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,  
			            Contacts.CONTENT_URI);  
			    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);  
				
			}
		});
        
        
      
        
        
        spinner = (Spinner) findViewById(R.id.spinnerType);
        what = (EditText) findViewById(R.id.etWhat);
        fromTo = (EditText) findViewById(R.id.etContact);
        Desc = (EditText) findViewById(R.id.etDesc);
        
        database = new DatabaseHelper(this);
        connection = database.getWritableDatabase();

    }
    
   
    
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {  
    	if (resultCode == RESULT_OK) {  
            switch (requestCode) {  
            case CONTACT_PICKER_RESULT:  
            	Cursor cursor = null; 
            	String name;

            	id = data.getData().getLastPathSegment();  
            	idLong = data.getData().toString();
            	contactUri = data.getData();    
            	
            	Log.d("ICH", contactUri.toString());
            	

            	
            	// TODO auf meinem handy wird immer thleistner@gmx.de gewählt statt dem Namen des eigentlichen Kontakts!
            	//KontaktURI wird aber richtig gesprichert, d.h. das korrekte Bild wird in FragmentList geladen
            	//Eventuell darüber an namen kommen?
//            	String whereName = ContactsContract.Data.MIMETYPE + " = ?";
//                String[] whereNameParams = new String[]{ ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE };
//                
//                Cursor nameCur = getContentResolver().query(
//                		ContactsContract.Data.CONTENT_URI, 
//                		null, 
//                		whereName, 
//                		whereNameParams, 
//                		ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME_ALTERNATIVE);
//                while (nameCur.moveToNext()) {
//                    String display = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME_ALTERNATIVE
//                    		));
//                    name = display;  
//                    Log.v("ICH", "Got name: " + name);  
//                    fromTo.setText(name);
//                }
//                nameCur.close(); 
            	
            	String id, name1, phone, hasPhone;
            	int idx;
            	Cursor cursor1 = getContentResolver().query(contactUri, null, null, null, null);
            	if (cursor1.moveToFirst()) {
            	    idx = cursor1.getColumnIndex(ContactsContract.Contacts._ID);
            	    id = cursor1.getString(idx);

            	    idx = cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            	    name1 = cursor1.getString(idx);

            	    idx = cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
            	    hasPhone = cursor1.getString(idx);
            	    
            	    name = name1;
            	    Log.v("ICH", "Got name: " + name);  
            	    fromTo.setText(name);
            	}
            	
            	
                
//                int NameIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);  
//                // let's just get the first email  
//                if (cursor.moveToFirst()) {  
//                    name = cursor.getString(NameIdx);  
//                    Log.v("ICH", "Got name: " + name);  
//                    fromTo.setText(name);
//                } else {  
//                    Toast.makeText(this, "Kein Name gefunden, bitte manuell eingeben.", Toast.LENGTH_SHORT).show();  
//                } 
            	
                break;  
            }  
        } else {  
            // gracefully handle failure  
            Log.w("ICH", "Warning: activity result not ok");  
        }  
    }

	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_new, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {    
       switch (item.getItemId()) 
       {        
          case android.R.id.home:            
             Intent intent = new Intent(this, MainActivity.class);            
             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
             startActivity(intent);            
             return true;  
          case R.id.menu_save:            
              //Daten sammeln
        	  int spinnerPos = spinner.getSelectedItemPosition();
        	  String sWhat = what.getText().toString();
        	  String contact = fromTo.getText().toString();
        	  String sDesc = Desc.getText().toString();
        	  
        	  if (sWhat.isEmpty() || contact.isEmpty() || date.getText().toString().isEmpty())
        	  {
        		  Toast.makeText(this, getString(R.string.empty), Toast.LENGTH_SHORT).show();
        	  }
        	  else
        	  {
        		 
        		  //DB schreiben
        		  Log.d("SPINNER", String.valueOf(spinnerPos));
	        	  connection.execSQL("insert into owe(deadline, type, what, fromto, desc, contacturi) values (\'"+date.getText().toString()+"\',\'"+spinnerPos+"\', \'"+sWhat+"\', \'"+contact+"\', \'"+sDesc+"\', \'"+contactUri+"\');");
	        	  Toast.makeText(this, getString(R.string.saved) + " " + date.getText().toString(), Toast.LENGTH_SHORT).show();
	        	  Intent intent2 = new Intent(this, MainActivity.class);            
	              intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	              startActivity(intent2); 
	              
	              
	              
	              //Alarm setzen
        		  if (calendar.isChecked())
        		  {
//        			  Date dateNotif = null;
//        			  dateNotif = new Date (year, month, day, 11, 51);
//        			  
//        			  	//Create an offset from the current time in which the alarm will go off.
//        		        Calendar cal = Calendar.getInstance();
//        		        cal.add(Calendar.SECOND, 5);
//        		        //cal.add(Calendar.DATE, dateNotif.getDate());
//        		 
//        		        //Create a new PendingIntent and add it to the AlarmManager
//        		        Intent intent_ = new Intent(this, AlarmReceiverActivity.class);
//        		        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//        		            123456, intent_, PendingIntent.FLAG_CANCEL_CURRENT);
//        		        AlarmManager am = 
//        		            (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
//        		        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
//        		                pendingIntent);
        			  
        		        Calendar cal = Calendar.getInstance();    
        		        String[] d = date.getText().toString().split(".");
        		        Log.i("STRING",date.getText().toString());
        		        Log.i("STRING",String.valueOf(d.length));

        		        cal.set(year, month, day);
        		        
        		        
        		        String share = "";
        		        String datum = date.getText().toString();
        		        if (String.valueOf(spinnerPos).equals("0"))
        		    	{
        		        	
        		        	share = getString(R.string.mailtext_has_owe, contact, what.getText().toString(), datum);
        		    	}
        		    	else
        		    	{
        		    		share = getString(R.string.mailtext_owe_to, what.getText().toString(), datum);
        		    	}
        		        
//        		        String[] projection = new String[] { "_id", "name" };
//        		        Uri calendars = Uri.parse("content://com.android.calendar/calendars");
//        		             
//        		        Cursor managedCursor =
//        		           managedQuery(calendars, projection,
//        		           null, null, null);
//        		        if (managedCursor.moveToFirst()) {
//        		        	 String calName; 
//        		        	 
//        		        	 String calId; 
//        		        	 int nameColumn = managedCursor.getColumnIndex("name"); 
//        		        	 int idColumn = managedCursor.getColumnIndex("_id");
//        		        	 do {
//        		        	    calName = managedCursor.getString(nameColumn);
//        		        	    Log.i("KALENDER", calName);
//        		        	    calId = managedCursor.getString(idColumn);
//        		        	    Log.i("KALENDER", calId);
//        		        	 } while (managedCursor.moveToNext());
        		        	 
//        		        	 ContentValues event = new ContentValues();
//             		         event.put("calendar_id", 1);
//             		         event.put("title", "Event Title");
//             		         event.put("description", "Event Desc");
//             		       	 event.put("eventLocation", "Event Location");
//	             		     long startTime = new Date(year, month, day).getTime();
//	             		     long endTime = new Date(year, month, day).getTime();
//	             		     event.put("dtstart", startTime);
//	             		     event.put("dtend", endTime);
//	             		     event.put("allDay", 1);   // 0 for false, 1 for true
//	             		     event.put("hasAlarm", 1); // 0 for false, 1 for true
//	             		     event.put("eventTimezone", "Europe/London");
//	             		     Uri eventsUri = Uri.parse("content://com.android.calendar/events");
//	             		     Uri url = getContentResolver().insert(eventsUri, event);
	             		     
	             		     
        		        	// Construct event details
        		        	 long startMillis = 0;
        		        	 long endMillis = 0;
        		        	 Calendar beginTime = Calendar.getInstance();
        		        	 beginTime.set(year, month, day);
        		        	 startMillis = beginTime.getTimeInMillis();
        		        	 Calendar endTime = Calendar.getInstance();
        		        	 endTime.set(year, month, day);
        		        	 endMillis = endTime.getTimeInMillis();

        		        	 // Insert Event
        		        	 ContentResolver cr = getContentResolver();
        		        	 ContentValues values = new ContentValues();
        		        	 values.put(CalendarContract.Events.DTSTART, startMillis);
        		        	 values.put(CalendarContract.Events.DTEND, endMillis);
        		        	 values.put(CalendarContract.Events.TITLE, share);
        		        	 values.put(CalendarContract.Events.DESCRIPTION, contact + " -- " + sDesc);
        		        	 values.put(CalendarContract.Events.CALENDAR_ID, 1);
        		        	 values.put("eventTimezone", "Europe/London");
        		        	 Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        		        	 // Retrieve ID for new event
        		        	 String eventID = uri.getLastPathSegment();
	             		     
	             		     
	             		     
	             		     
	             		     //Log.i("URL", url.toString());
//        		        }
//        		        else
//        		        {
//            		        Intent intentCal = new Intent(Intent.ACTION_EDIT);
//            		        intentCal.setType("vnd.android.cursor.item/event");
//            		        intentCal.putExtra("beginTime", cal.getTimeInMillis());
//            		        intentCal.putExtra("allDay", true);
//            		        //intent.putExtra("rrule", "FREQ=ONE");
//            		        intentCal.putExtra("endTime",cal.getTimeInMillis()); //cal.getTimeInMillis()+60*60*1000
//            		        intentCal.putExtra("title", share);
//            		        startActivity(intentCal);
//            		        
//            		        Toast.makeText(this, "Kehren Sie mit dem Zurück-Button nach Erstellen des Termines zurück in die App.", Toast.LENGTH_LONG).show();
//            			  
//        		        }
        		        
        		        
        		        
        		        

        			  //Log.d("ICH", String.valueOf(dateNotif.getTime()));
        		  }
	              
	              
        	  }
        	  
              return true; 
          default:            
             return super.onOptionsItemSelected(item);    
       }
    }
    

    
}
