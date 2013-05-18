package de.gvisions.oweapp;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.gvisions.oweapp.R;


public class FragmentDetail extends Fragment {

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	   Bundle savedInstanceState) {
	  View view = inflater.inflate(R.layout.fragment_detail, container, false);
	  return view;
	 }

	 public void setWas(String item) {
	  TextView view = (TextView) getView().findViewById(R.id.was);
	  view.setText(item);
	 }
	 
	 
	 public void setContact(String item) {
		  TextView view = (TextView) getView().findViewById(R.id.wer);
		  view.setText(item);
	 }
	
	 public void setType(String item) {
		  TextView view = (TextView) getView().findViewById(R.id.status);
		  view.setText(item);
	 }	 
	 
	 public void setDesc(String item) {
		  TextView view = (TextView) getView().findViewById(R.id.desc);
		  view.setText(item);
	 }
	 
	 public void setDatum(String item) {
		  TextView view = (TextView) getView().findViewById(R.id.wann);
		  view.setText(item);
	 }
	 
}
