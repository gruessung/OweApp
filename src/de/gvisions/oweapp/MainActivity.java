package de.gvisions.oweapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;




public class MainActivity extends Activity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        AppRater.app_launched(this);
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
        case R.id.menu_add:            
           Intent intent = new Intent(this, NewItem.class);            
           startActivity(intent);            
           return true;     
        case R.id.menuOptions:
        	Intent t = new Intent(MainActivity.this, PreferenceScreen.class);
        	startActivity(t);

        	return true;
           
        default:            
           return super.onOptionsItemSelected(item);    
     }
  }



  
  
}

