package de.gvisions.oweapp;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;

import de.gvisions.oweapp.cards.CardOnClickListener;
import de.gvisions.oweapp.cards.ContactCard;
import de.gvisions.oweapp.cards.MyCard;
import de.gvisions.oweapp.enums.ListElements;


public class ListActivity extends Activity {

	SQLiteOpenHelper database;
	SQLiteDatabase connection;
	
	CardUI mCardView;
	
	//Array mit ListElements
	ArrayList<ListElements> listElements;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity); 

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        
        //Nachricht neue bedinung
        SharedPreferences localSharedPreferences = getSharedPreferences("de.gvisions.oweapp", 0);
        Object localObject = "";
        if (!Boolean.valueOf(localSharedPreferences.getBoolean("neue_bedienung", false)).booleanValue())
        {
          AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
          localBuilder.setTitle(getString(R.string.new_text));
          localBuilder.setMessage(getString(R.string.new_swipe)).setPositiveButton("OK", null);
          localBuilder.create().show();
          localSharedPreferences.edit().putBoolean("neue_bedienung", true).commit();
          
        }
        
		  mCardView = (CardUI) findViewById(R.id.cardsview);
		  mCardView.setSwipeable(true);
		  
		  database = new DatabaseHelper(this.getApplicationContext());
	      connection = database.getReadableDatabase();
		  
	      this.listElements = new  ArrayList<ListElements>();
	      
	      SharedPreferences prefs = 
	    		  getSharedPreferences(
	    	      "de.gvisions.oweapp", Context.MODE_PRIVATE);
	      
	 
	      boolean f = prefs.getBoolean("de.gvisions.oweapp.firstAfterUpdate2", false);
	      if (f == false)
	      {
	    	  Toast.makeText(this,getString(R.string.deleteCardInfo), Toast.LENGTH_LONG).show();
	    	  prefs.edit().putBoolean("de.gvisions.oweapp.firstAfterUpdate2", true).commit();
	      }
	      
	      buildData();
	      buildCards();
        
    }
    
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	buildData();
    	buildCards();
    }
    
    public SQLiteDatabase getConnection()
    {
    	return this.connection;
    }
    
private void buildCards() {
	
	
		mCardView.clearCards();
		//Erste Karte für Daten des Kontakts
		
		if (this.listElements.size() > 0)
		{
			ListElements first = this.listElements.get(0);
		    ContactCard firstCard = new ContactCard(first.name, first.kontakt, this,"");
		    mCardView.addCard(firstCard);
		}
		
		CardStack stackOweTo = new CardStack();
		CardStack stackHaseOwe = new CardStack();
		stackOweTo.setTitle(getString(R.string.show_owe_to));
		stackHaseOwe.setTitle(getString(R.string.show_hase_owe));
		
		int countOweTo = 0;
		int countHaseOwe = 0;
		
		for (ListElements element : this.listElements) {
			
			if (element.leihRichtung.equals(getString(R.string.show_owe_to)))
			{
				MyCard card = new MyCard(element.objekt , "#FF0000", this);
				card.setData(element.id);
				card.setText(element.beschreibung);
				card.setDatum(element.datum);
				card.setOnClickListener(new CardOnClickListener(card, this));
				countOweTo++;
		    	stackOweTo.add(card);	
			}	
			else
			{
				MyCard card = new MyCard(element.objekt , "#FF0000", this);
				card.setData(element.id);
				card.setText(element.beschreibung);
				card.setDatum(element.datum);
				card.setOnClickListener(new CardOnClickListener(card, this));
				countHaseOwe++;
		    	stackHaseOwe.add(card);				
			}
		}
		
		if (countOweTo == 0)
		{
			stackOweTo.add(new ContactCard(getString(R.string.noData), null, this,""));
		}
		else if (countHaseOwe==0)
		{
			stackHaseOwe.add(new ContactCard(getString(R.string.noData),null, this,""));

		}
		
		
    	mCardView.addStack(stackOweTo);
    	mCardView.addStack(stackHaseOwe);
    	mCardView.refresh();
		
		
	}







	/**
	 * L�dt die Daten aus der DB und speichert diese in eizelnen ListElements im Array
	 */
private void buildData() {
    String typeString = "";
    this.listElements.clear();
    Cursor result = connection.rawQuery("select * from owe WHERE contacturi = '"+getIntent().getExtras().get("kontakt")+"'", null);
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

    
    
    
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.activity_main, menu);
      return true;
  }




    @Override
  public boolean onOptionsItemSelected(MenuItem item) 
  {    
     switch (item.getItemId()) 
     {
         case android.R.id.home:
             	finish();
             return true;

         case R.id.menu_add:
           Intent intent2 = new Intent(this, NewItem.class);
           startActivity(intent2);
           return true;     

           
        default:            
           return super.onOptionsItemSelected(item);    
     }
  }



  
  
}

