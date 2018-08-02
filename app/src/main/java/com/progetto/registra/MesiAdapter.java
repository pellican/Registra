    package com.progetto.registra;

    import android.content.Context;
    import android.content.res.Resources;
    import android.database.Cursor;
    import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

    import com.progetto.registra.database.DbAdapterMesi;


    public class MesiAdapter extends BaseAdapter {
        Context mContext;

        int anno;
        String[] mesi;
        Resources res;
               // ={"Gennaio","Febbraio","Marzo","Aprile","Maggio","Giugno","Luglio","Agosto","Settembre","Ottobre","Novembre","Dicembre"};
        String[] ore;
        String[] min;
        String[] orePagate;
        String[] resto;
        String[] totale;
        public MesiAdapter(Context c,int annoCalendar) {
            anno=annoCalendar;
            mContext = c;
            res=c.getResources();
            mesi=new String[]{res.getString(R.string.gennaio),res.getString(R.string.febbraio),res.getString(R.string.marzo),
                    res.getString(R.string.aprile),res.getString(R.string.maggio),res.getString(R.string.giugno),
                    res.getString(R.string.luglio),res.getString(R.string.agosto),res.getString(R.string.settembre),
                    res.getString(R.string.ottobre),res.getString(R.string.novembre),res.getString(R.string.dicembre)};
            Ore(anno);

        }
        public int getCount() {
            return mesi.length;
        }
        public long getItemId(int position) {
            return 0;
        }
        public Object getItem(int position) {
            return null;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            TextView txmesi,txore,txmin,txpagate,txresto,tx;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                LayoutInflater vis = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vis.inflate(R.layout.mesi_item, null);
            }
          //  tx=(TextView)v.findViewById(R.id.tx);


            txmesi=(TextView)v.findViewById(R.id.txmesi);
            txore=(TextView)v.findViewById(R.id.txOraMesi);
            txore.setVisibility(View.INVISIBLE);
            txmin=(TextView)v.findViewById(R.id.txMinMesi);
            txpagate=(TextView)v.findViewById(R.id.texorepag);
            txresto=(TextView)v.findViewById(R.id.texresto);

            if (ore[position] != null){
                txore.setVisibility(View.VISIBLE);
            }

            txmesi.setText(mesi[position]);
            txore.setText(ore[position]+":");
            txmin.setText(min[position]);
            txpagate.setText(orePagate[position]);
            txresto.setText(resto[position]);
            return v;
        }
        public void Ore(int ann){
            ore=new String[mesi.length];
            min=new String[mesi.length];
            orePagate=new String[mesi.length];
            resto=new String[mesi.length];
            DbAdapterMesi db=new DbAdapterMesi(mContext);
            db.open();Cursor c;Cursor t;
            t=db.fetchAllContacts();
            if (t.getCount() != 0 && t.moveToFirst()){
                totale=new String[t.getCount()];
                for (int a=0;a<t.getCount();a++){
                    totale[a]=t.getString(6);
                    t.moveToNext();
                }
            }

            String annos=""+ann;
            for(int i=0;i<mesi.length;i++){
                String m=""+i;
                c=db.fetchContactsByMese(m,annos);
                if(c.moveToFirst()){
                    ore[i]=c.getString(3);
                    min[i]=c.getString(4);
                    orePagate[i]=c.getString(5);
                    resto[i]=c.getString(6);
                }
            }
        }
        public String[] getOreM(){
            return ore;
        }
        public String[] getMinM(){
            return min;
        }
        public String[] getOrePagate(){
            return orePagate;
        }
        public String[] getResto(){
            return  resto;
        }
        public String[] getTotale(){ return totale;}

    }