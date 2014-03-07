package de.gvisions.oweapp;





import java.util.ArrayList;

import com.fima.cardsui.views.CardUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.gvisions.oweapp.cards.AdCard;
import de.gvisions.oweapp.cards.ContactCard;
import de.gvisions.oweapp.enums.MainListElements;
import de.gvisions.oweapp.services.XMLBuilder;

public class MainActivity extends Activity
{
	
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    
    private String[] drawerTitles;
    
    private CardUI mCardView;
	SQLiteOpenHelper database;
	SQLiteDatabase connection;
	
	String tmp;
	
	//Array mit ListElements
	ArrayList<MainListElements> mainListElements;

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	    buildData(); //Daten aus DB holen und in ArrayList speichern
	    buildCards();
	}
	
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    
    
    setContentView(R.layout.nav_drawer_main_activity);
    
	mCardView = (CardUI) findViewById(R.id.cardsview);
	mCardView.setSwipeable(false);
			
    database = new DatabaseHelper(this);
    connection = database.getReadableDatabase();
    
    mainListElements = new ArrayList<MainListElements>();

    buildData(); //Daten aus DB holen und in ArrayList speichern
    buildCards();
    
    

    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);


    // Set the list's click listener
    mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    
    mTitle = mDrawerTitle = getTitle();

    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);


    // set a custom shadow that overlays the main content when the drawer opens
    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    // set up the drawer's list view with items and click listener
    
    SharedPreferences localSharedPreferences = getSharedPreferences("de.gvisions.oweapp", 0);
    
    //Drawer Einträge
    //Erst Titel
    //Dann Subtitle
    //Dann Icon
    
  //"Login",
    drawerTitles = new String[]
    		{
    		getString(R.string.survey),
			getString(R.string.contact),
			getString(R.string.settings)/*,
    			"Login"*/
    		};

	//localSharedPreferences.getString("username", "nicht eingeloggt"),

    String[] drawerSubtitles = new String[]
    		{
    			getString(R.string.survey_u),
    			getString(R.string.contact_u),
    			getString(R.string.settings_u)/*,
    			localSharedPreferences.getString("username", "nicht eingeloggt"),*/
    		};
	//R.drawable.ic_action_device_access_accounts,
    int[] drawerIcons = new int[] 
    		{
    			R.drawable.ic_action_edit,
    			R.drawable.ic_action_action_help,
    	
    			R.drawable.ic_action_action_settings,
    			R.drawable.ic_action_device_access_accounts,
    		};
    MenuListAdapter mMenuAdapter = new MenuListAdapter(this, drawerTitles, drawerSubtitles, drawerIcons);

    mDrawerList.setAdapter(mMenuAdapter);
    
    mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    // enable ActionBar app icon to behave as action to toggle nav drawer
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setHomeButtonEnabled(true);

    // ActionBarDrawerToggle ties together the the proper interactions
    // between the sliding drawer and the action bar app icon
    mDrawerToggle = new ActionBarDrawerToggle(
            this,                  /* host Activity */
            mDrawerLayout,         /* DrawerLayout object */
            R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */ 
            0, 
            0

            ) {
        public void onDrawerClosed(View view) {
            getActionBar().setTitle(mTitle);
            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }

        public void onDrawerOpened(View drawerView) {
            getActionBar().setTitle(mDrawerTitle);
            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }
    };
    mDrawerLayout.setDrawerListener(mDrawerToggle);

    if (paramBundle == null) {
        //selectItem(0);
    }
    

    Object localObject = "";
    try
    {
      String str = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
      localObject = str;
      if (!Boolean.valueOf(localSharedPreferences.getBoolean("version_" + (String)localObject, false)).booleanValue())
      {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setTitle(getString(R.string.version_new) + (String)localObject);
        localBuilder.setMessage(getString(R.string.version_text)).setPositiveButton("OK", null);
        localBuilder.create().show();
        localSharedPreferences.edit().putBoolean("version_" + (String)localObject, true).commit();
        
      }
      AppRater.app_launched(this);
      return;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      while (true)
        localNameNotFoundException.printStackTrace();
    }
  }
  
  
  
  private void buildData() {
	  this.mainListElements.clear();
      String typeString = "";
      int anzahlVerliehen = 0, anzahlGeliehen = 0;

      Cursor result = connection.rawQuery("SELECT sum(case when type = '0' then 1 else 0 end) geliehen, sum(case when type = '1' then 1 else 0 end) verliehen, fromTo, contacturi, type FROM owe GROUP BY fromTo;", null);
      while(result.moveToNext ())
      {
          anzahlGeliehen = result.getInt(0);

      	anzahlVerliehen = result.getInt(1);
          
      	this.mainListElements.add(
					new MainListElements(
							anzahlGeliehen,
                          anzahlVerliehen,
                          result.getString(2), //name
							result.getString(3)//Kontakt fuer Bagde
							) 
					);	
      	}
    }
  
  private void buildCards()
  {
	  mCardView.clearCards();
  	for (MainListElements element : this.mainListElements) {
			final ContactCard card = new ContactCard(element.name, element.kontakt, this, getString(R.string.card1, element.anzahlAusgeliehen) + "\n"+ getString(R.string.card2, element.anzahlVerliehen));
			card.setData(element.kontakt);

  		this.tmp = element.kontakt;
  		
			card.setOnClickListener(new OnClickListener() {
				
				public void onClick(View arg0) {
					Intent i = new Intent(arg0.getContext(), ListActivity.class);
					i.putExtra("kontakt", card.getData().toString());
					startActivity(i);
				}
			});
			
			mCardView.addCard(card);
			this.tmp = null;
		}
  	
  	//Werbung
      AdCard adcard = new AdCard(this);
      mCardView.addCard(adcard);
		
  	mCardView.refresh();    	
  }

  
  
  
  
  
  /* The click listner for ListView in the navigation drawer */
  private class DrawerItemClickListener implements ListView.OnItemClickListener {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          selectItem(position);
      }
  }

  private void selectItem(int position) {
      // update the main content by replacing fragments
	  
	  switch(position)
	  {
	  //Nutzerumfrage
	  case 0:
		  startActivity(new Intent(this, WebView.class).putExtra("url", "http://goo.gl/g0XlsA"));
		  break;
	  //Nutzerumfrage
	  case 1:
		  startActivity(new Intent(this, WebView.class).putExtra("url", "http://projects.gvisions.de/index.php?p=trouble&project=ild"));
		  break;
	  //Einstellungen	  
	  case 2:
		  startActivity(new Intent(this, PreferenceScreen.class));
		  break;
		  
	  case 3:
		  //startActivity(new Intent(this, WebView.class).putExtra("url", "http://accounts.gvisions.de/login.html"));
		  startActivity(new Intent(this, LoginView.class));
		  break;
		  
	  }
	  


      // update selected item and title, then close the drawer
      mDrawerList.setItemChecked(position, true);
      setTitle(drawerTitles[position]);
      mDrawerLayout.closeDrawer(mDrawerList);
  }
  
  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
      super.onPostCreate(savedInstanceState);
      // Sync the toggle state after onRestoreInstanceState has occurred.
      mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      // Pass any configuration change to the drawer toggls
      mDrawerToggle.onConfigurationChanged(newConfig);
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(R.menu.activity_main, paramMenu);
    return true;
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
      // The action bar home/up action should open or close the drawer.
      // ActionBarDrawerToggle will take care of this.
     if (mDrawerToggle.onOptionsItemSelected(paramMenuItem)) {
         return true;
     }
    switch (paramMenuItem.getItemId())
    {
    
    default:
      super.onOptionsItemSelected(paramMenuItem);
      return true;
    case R.id.menu_add:
      startActivity(new Intent(this, NewItem.class));
      return true;
   

      
    case R.id.bewerten:
		Uri uriUrl3 = Uri.parse("https://play.google.com/store/apps/details?id=de.gvisions.oweapp");  
        Intent launchBrowser3 = new Intent(Intent.ACTION_VIEW, uriUrl3); 
        startActivity(launchBrowser3);  
    	return true;
    	
    
    case R.id.backup:
    	if (Boolean.valueOf(new XMLBuilder(new DatabaseHelper(getApplicationContext()).getReadableDatabase()).backup()).booleanValue())
        {
          Toast.makeText(this, getString(R.string.backupSuccess), 1).show();
        }
    	else
    	{
    		Toast.makeText(this, getString(R.string.backupFailed), 1).show();
    	}
      return true;
    case R.id.restore:  
    if (new XMLBuilder(new DatabaseHelper(getApplicationContext()).getReadableDatabase()).restore().booleanValue())
      {
        Toast.makeText(this, getString(R.string.restoreSuccess), 1).show();
        buildData();
        buildCards();
      }
      else
      {
	      Toast.makeText(this, getString(R.string.restoreFailed), 1).show();
      }
      return true;
  }
}
}





