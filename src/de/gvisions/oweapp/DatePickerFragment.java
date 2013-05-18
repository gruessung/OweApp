package de.gvisions.oweapp;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
    	
        EditText date = (EditText) view.findViewById(R.id.etDate);
        Log.d("DATE", String.valueOf(4));
        
        //date.setText(""+day+"-"+(month+1)+"-"+year);
        Log.d("DATE", String.valueOf(year));
        Log.d("DATE", String.valueOf(month));
        Log.d("DATE", String.valueOf(day));
        //date.setText("HALLO");
        //Log.d("DATE", date.getText().toString());
        
    }
   
	
}
