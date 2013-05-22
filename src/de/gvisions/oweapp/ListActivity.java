package de.gvisions.oweapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class ListActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
             Intent intent = new Intent(this, MainActivity.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             startActivity(intent);
             return true;

         case R.id.menu_add:
           Intent intent2 = new Intent(this, NewItem.class);
           startActivity(intent2);
           return true;     
        case R.id.menuOptions:
        	Intent t = new Intent(ListActivity.this, PreferenceScreen.class);
        	startActivity(t);
        	return true;
           
        default:            
           return super.onOptionsItemSelected(item);    
     }
  }



  
  
}

