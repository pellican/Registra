package com.progetto.registra;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

import com.progetto.registra.database.DbAdapterDay;

public class CalendarAdapter extends BaseAdapter {
	static final int FIRST_DAY_OF_WEEK =1; // Sunday = 0, Monday = 1
	
	 // references to our items
    public String[] days;
    public String[] orario;
	private Context mContext;
			String mese,data;
    private Calendar month;
    private Calendar selectedDate;
    private ArrayList<String> items;
    
    public CalendarAdapter(Context c, Calendar monthCalendar) {
    	month = monthCalendar;
    	selectedDate = (Calendar)monthCalendar.clone();
    	mContext = c;
        month.set(Calendar.DAY_OF_MONTH, 1);
        this.items = new ArrayList<String>();
        refreshDays();
       
    }
    
    public void setItems(ArrayList<String> items) {
    	for(int i = 0;i != items.size();i++){
    		if(items.get(i).length()==1) {
    		items.set(i, "0" + items.get(i));
    		}
    	}
    	this.items = items;
    }
    

    public int getCount() {
        return days.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
    	TextView dayView,txore;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.calendar_item, null);
        	
        }
        dayView = (TextView)v.findViewById(R.id.date);
        txore =(TextView)v.findViewById(R.id.txOrario);
        v.setVisibility(View.VISIBLE);
        // disable empty days from the beginning
        if(days[position].equals("")) {
        	dayView.setClickable(false);
        	dayView.setFocusable(false);
        	v.setVisibility(View.INVISIBLE);        	
        }
        else {
        	// mark current day as focused
        	if(month.get(Calendar.YEAR)== selectedDate.get(Calendar.YEAR) && month.get(Calendar.MONTH)== selectedDate.get(Calendar.MONTH) && days[position].equals(""+selectedDate.get(Calendar.DAY_OF_MONTH))) {
        		v.setBackgroundColor(Color.parseColor("#d9dbdb"));
        	}
        	else {
        		v.setBackgroundColor(Color.WHITE);
        		
        	if (position==6){v.setBackgroundColor(Color.parseColor("#ffdcdc"));}
        	else if(position==13){v.setBackgroundColor(Color.parseColor("#ffdcdc"));}
        	else if(position==20){v.setBackgroundColor(Color.parseColor("#ffdcdc"));}
        	else if(position==27){v.setBackgroundColor(Color.parseColor("#ffdcdc"));}
        	else if(position==34){v.setBackgroundColor(Color.parseColor("#ffdcdc"));}
        	}
        }
        dayView.setText(days[position]);
        
        txore.setText(orario[position]);
        
     
        return v;
    }
    
    public void refreshDays()
    {
    	// clear items
    	items.clear();    	
    	int lastDay = month.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = (int)month.get(Calendar.DAY_OF_WEEK);
        
        // figure size of the array
        if(firstDay==1){
        	days = new String[lastDay+(FIRST_DAY_OF_WEEK*6)];
        }
        else {
        	days = new String[lastDay+firstDay-(FIRST_DAY_OF_WEEK+1)];
        }
        
        int j=FIRST_DAY_OF_WEEK;
        
        // populate empty days before first real day
        if(firstDay>1) {
	        for(j=0;j<firstDay-FIRST_DAY_OF_WEEK;j++) {
	        	days[j] = "";
	        }
        }
	    else {
	    	for(j=0;j<FIRST_DAY_OF_WEEK*6;j++) {
	        	days[j] = "";
	        }
	    	j=FIRST_DAY_OF_WEEK*6+1; // sunday => 1, monday => 7
	    }
        
        // populate days
        int dayNumber = 1;
        for(int i=j-1;i<days.length;i++) {
        	days[i] = ""+dayNumber;
        	dayNumber++;
        }
  //      Orario();
  //  }

  //  public void Orario(){
    	orario =new String[days.length];
    	DbAdapterDay db=new DbAdapterDay(mContext);
    	db.open(); Cursor cur;
    	mese=(String) DateFormat.format("MMMM-yyyy", month);
    	for (int i=0;i<days.length;i++){
    		String day=days[i];
    				if (day.length()==1){day="0"+day;}
    				data=day+"-"+mese;
    		  cur = db.fetchContactsByData(data);
    		if (cur.moveToFirst()){    		
    			orario[i]=cur.getString(8);    			
    		}    							
    	}
    	
    }
    public String[] getore(){
    	return orario;
    }
}
