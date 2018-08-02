package com.progetto.registra;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;


import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.progetto.registra.database.Backupdb;
import com.progetto.registra.database.DbAdapterMesi;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;


public class Frammento_Mese extends Fragment{

public Calendar calend;
public Calendar calver;
public String dataI,oreS,minutiS,zero="0";
public CalendarAdapter adapter;
public TextView title, data, txore, totale;
public ImageView annoI,menuI,stanpa;
public ViewFlipper vf;
public int mAnno, mMese;

    DrawerLayout drawerLayout;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        final View calView =inflater.inflate(R.layout.mese_frammento, container, false);
        calend = Calendar.getInstance();
        calver = Calendar.getInstance();

        stanpa =(ImageView)calView.findViewById(R.id.stampa);
        data = (TextView) calView.findViewById(R.id.date);
        txore = (TextView) calView.findViewById(R.id.txOre);
        vf = (ViewFlipper) calView.findViewById(R.id.viewflipper);
        title = (TextView) calView.findViewById(R.id.title);
        title.setText(DateFormat.format("MMMM yyyy", calend));
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showDialog(DIALOG);
            }
        });
        GridView gridview = (GridView) calView.findViewById(R.id.gridview);
        adapter = new CalendarAdapter(getActivity(), calend);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView date = (TextView) v.findViewById(R.id.date);
                TextView orario = (TextView) v.findViewById(R.id.txOrario);
                if (date !=null && !date.getText().equals("")) {

                    String day = date.getText().toString();
                    int giorno = Integer.parseInt(day);
                    if (day.length() == 1) {
                        day = "0" + day;
                    }
                    dataI = day + "-" + DateFormat.format("MMMM-yyyy", calend);
                    if (calend.get(Calendar.YEAR) < calver.get(Calendar.YEAR) ||
                            calend.get(Calendar.YEAR) == calver.get(Calendar.YEAR) && calend.get(Calendar.MONTH) < calver.get(Calendar.MONTH) ||
                            calend.get(Calendar.MONTH) == calver.get(Calendar.MONTH) && giorno <= calver.get(Calendar.DAY_OF_MONTH)  ) {
                        if (orario !=null && !orario.getText().equals("")) {
                            Giorno("salvato");
                            Toast.makeText(getActivity(), R.string.gia_salvato, Toast.LENGTH_SHORT).show();
                        } else {
                            Giorno("bianco");
                        }
                    }

                }


            }
        });
        gridview.setOnTouchListener(new OnSwipeTouchListener() {
            @Override
            public boolean onSwipeRight() {
                mAnno = calend.get(Calendar.YEAR);
                mMese = calend.get(Calendar.MONTH);
                mMese--;
                calend.set(mAnno, mMese, 1);
                Resresh();
                Totale();
                vf.setInAnimation(getActivity(), R.anim.slide_in_left);
                vf.setOutAnimation(getActivity(), R.anim.slide_out_right);
                vf.showPrevious();
                return true;
            }

            public boolean onSwipeLeft() {
                mAnno = calend.get(Calendar.YEAR);
                mMese = calend.get(Calendar.MONTH);
                mMese++;
                calend.set(mAnno, mMese, 1);
                Resresh();
                Totale();
                vf.setInAnimation(getActivity(), R.anim.slide_in_right);
                vf.setOutAnimation(getActivity(), R.anim.slide_out_left);
                vf.showNext();
                return true;
            }
        });
        annoI = (ImageView) calView.findViewById(R.id.imageAnn);
        annoI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Frammento_Anno nextFrag= new Frammento_Anno();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frameContainer, nextFrag).commit();
            }
        });
        totale = (TextView) calView.findViewById(R.id.textcaltotale);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerLayout);

        menuI=(ImageView)calView.findViewById(R.id.imageMenu);
        menuI.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                drawerLayout.openDrawer(Gravity.LEFT);
        }

        });
        stanpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stampa(calView);
            }
        });
        Totale();

       return  calView;
    }
    public void Stampa (View view){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_stampa);
        dialog.setTitle(R.string.vuoi_salvare);
        ImageView image=(ImageView)dialog.findViewById(R.id.imagestampa);
        Button ok=(Button)dialog.findViewById(R.id.buttonokst);
        Button ann=(Button)dialog.findViewById(R.id.buttonannulla);
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        image.setImageBitmap(bitmap);
        ann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Salva(bitmap);
                dialog.dismiss();

            }
        });



        dialog.show();
    }
    public void Salva(Bitmap bitmap){
        File cart= new File(Environment.getExternalStorageDirectory(),"/Agenda ore");
        if (!cart.exists()){
            boolean crea=cart.mkdir();
        }
        File cart2=new File(Environment.getExternalStorageDirectory()+"/Agenda ore","/Stampe");
        if (!cart2.exists()){
            boolean crea2=cart2.mkdirs();
        }
        if (cart2.exists()) {
            String path = "/Agenda ore/Stampe/"+DateFormat.format("MMMM yyyy", calend)+".jpg";
            File stampa =new File(Environment.getExternalStorageDirectory(),path);
            try {
                FileOutputStream out = new FileOutputStream(stampa);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
                out.flush();
                out.close();
                Toast.makeText(getActivity(),getResources().getString(R.string.salvato_nella)+"\n - Agenda ore/Stampe -",Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getActivity(),"ERRORE nessun file salvato..",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void Giorno(String ver) {
        Intent giorno = new Intent(getActivity(), GiornoActivity.class);
        giorno.putExtra("date", dataI);
        giorno.putExtra("verifica",ver);
        startActivity(giorno);
    }



    public void Resresh() {
        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        title.setText(DateFormat.format("MMMM yyyy", calend));
    }

    public void Totale() {
        String[] getore = adapter.getore();
        int[] ore = new int[getore.length];
        int[] min = new int[getore.length];
        int oret = 0;
        int mint = 0;
        for (int i = 0; i < getore.length; i++) {
            if (getore[i] != null) {
                ore[i] = Integer.parseInt(getore[i].substring(0, 2));
                min[i] = Integer.parseInt(getore[i].substring(3, 5));
            }
            oret = oret + ore[i];
            mint = mint + min[i];
        }
        if (mint >= 60) {
            float z = (((float) mint) / 60.0f) + ((float) oret);
            float v = (z - ((float) ((int) z))) * 60.0f;
            oret = (int) z;
            mint = (int) v;
        }
        oreS=""+oret;minutiS=""+mint;
        totale.setText(oret + ":" + mint);
    }
    public void Mese(){

            DbAdapterMesi me = new DbAdapterMesi(getActivity());
            me.open();
            String mese = "" + calend.get(Calendar.MONTH);
            String anno = "" + calend.get(Calendar.YEAR);
            Cursor cursor = me.fetchContactsByMese(mese, anno);
            long id = 0;
            if (cursor.moveToFirst()) {
                id = cursor.getLong(0);
            }

            if (id == 0) {
                backupDialog();
                me.createContact(mese, anno, oreS, minutiS, zero, oreS);
            } else {
                me.updateContact(id, mese, anno, oreS, minutiS, cursor.getString(5), oreS);
            }
            cursor.close();
            me.close();
            Singleton.getInstance().setSalvato(false);

    }

    private void backupDialog() {
        final String path="data/data/com.progetto.registra/databases/";

        AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(getActivity());
        alertDialog3.setTitle(getString(R.string.backup));
        alertDialog3.setMessage(getString(R.string.crea_backup));
        alertDialog3.setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                File destinationFolder = new File(Environment.getExternalStorageDirectory()+"/Agenda ore");
                if (!destinationFolder.exists()) {
                   Boolean successo = destinationFolder.mkdir();
                }
                Resources res = getResources();
                Backupdb.backupDatabase(path + "daydatabase.db", "Agenda ore/backup_database.db", getActivity(), res);
                Backupdb.backupDatabase(path + "mesidatabase.db", "Agenda ore/backup_mesi.db", getActivity(), res);
                Backupdb.backupDatabase(path + "setdatabase.db", "Agenda ore/backup_set.db", getActivity(), res);

            }
        });
        alertDialog3.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog3.show();
    }

    public void Clickmesi(){
        calend.set(Singleton.getInstance().getAnno(), Singleton.getInstance().getMese(), 1);
        Resresh();
        Totale();
        Singleton.getInstance().setAnno(0);
        Singleton.getInstance().setMese(0);
}
    @Override
    public void onResume() {
        super.onResume();
        Resresh();
        Totale();
        if (Singleton.getInstance().getSalvato()) {
            Mese();
        }
        if (Singleton.getInstance().getAnno() !=0) {
            Clickmesi();
        }
    }
}
