package com.progetto.registra;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;

import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.progetto.registra.database.Backupdb;
import com.progetto.registra.database.DbAdapterDay;
import com.progetto.registra.database.DbAdapterMesi;
import com.progetto.registra.database.DbAdapterSet;

import java.io.File;


public class Frammento_Backup extends Fragment {
    DrawerLayout drawerLayout;
    RelativeLayout backup,restore,cancella;
    String path="data/data/com.progetto.registra/databases/";
    String sd=Environment.getExternalStorageDirectory().getAbsolutePath();
    ImageView menu;
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View calView = inflater.inflate(R.layout.backup_frammento, container, false);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerLayout);
        menu=(ImageView)calView.findViewById(R.id.imageMenuBack);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        backup=(RelativeLayout)calView.findViewById(R.id.RelativeLayoutBackup);
        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Backup();
            }
        });
        restore=(RelativeLayout)calView.findViewById(R.id.RelativeLayoutRipris);
        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restore();
            }
        });
        cancella=(RelativeLayout)calView.findViewById(R.id.RelativeLayoutCanc);
        cancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cancella();
            }
        });


        return calView;
    }

    private void Restore() {
        AlertDialog alertDiaolog = new AlertDialog.Builder(getActivity()).create();
        alertDiaolog.setTitle(getString(R.string.ripristina_dati));
        alertDiaolog.setMessage(getString(R.string.vuoi_ripristinare));
        alertDiaolog.setButton(getString(R.string.si), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Resources res = getResources();
                Backupdb.restoreDatabase(path+"daydatabase.db", sd+"/Agenda ore/backup_database.db", getActivity(), res);
                Backupdb.restoreDatabase(path+"mesidatabase.db", sd+"/Agenda ore/backup_mesi.db", getActivity(), res);
                Backupdb.restoreDatabase(path+"setdatabase.db", sd+"/Agenda ore/backup_set.db", getActivity(), res);
            }
        });
        alertDiaolog.setButton2("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

            }
        });
        alertDiaolog.show();
    }
    public void Cancella(){
        final Dialog ca = new Dialog(getActivity());
       ca.setContentView(R.layout.dialog_cancella);
        ca.setTitle(R.string.cancella_tutto);
        Button set=(Button)ca.findViewById(R.id.set);
        Button gio=(Button)ca.findViewById(R.id.gio);
        Button mes=(Button)ca.findViewById(R.id.mes);
        Button ann=(Button)ca.findViewById(R.id.ann);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbAdapterSet db = new DbAdapterSet(getActivity());
                db.open();db.removeAll();db.close();
                Toast.makeText(getActivity(), R.string.dati_cancellati, Toast.LENGTH_LONG).show();
                ca.dismiss();
            }
        });
        gio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbAdapterDay dbd= new DbAdapterDay(getActivity());
                dbd.open();dbd.removeAll();dbd.close();
                Toast.makeText(getActivity(),R.string.dati_cancellati , Toast.LENGTH_LONG).show();
                ca.dismiss();
            }
        });
        mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbAdapterMesi dbm = new DbAdapterMesi(getActivity());
                dbm.open();
                dbm.removeAll();
                dbm.close();
                Toast.makeText(getActivity(), R.string.dati_cancellati, Toast.LENGTH_LONG).show();
                ca.dismiss();
            }
        });
        ann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ca.dismiss();
            }
        });


        ca.show();
    }

    private void Backup() {
        File destinationFolder = new File(Environment.getExternalStorageDirectory()+"/Agenda ore");
        Boolean successo = false;
        if (!destinationFolder.exists()) {
            successo = destinationFolder.mkdir();
            Resources res = getResources();
            Backupdb.backupDatabase(path + "daydatabase.db", "Agenda ore/backup_database.db", getActivity(), res);
            Backupdb.backupDatabase(path + "mesidatabase.db", "Agenda ore/backup_mesi.db", getActivity(), res);
            Backupdb.backupDatabase(path + "setdatabase.db", "Agenda ore/backup_set.db", getActivity(), res);
        }
        if (!successo) {
                BkConferma();
            }

    }

    private void BkConferma() {


        AlertDialog alertDialog3 = new AlertDialog.Builder(getActivity()).create();
        alertDialog3.setTitle(getString(R.string.database_esistente));
        alertDialog3.setMessage(getString(R.string.vuoi_aggiornare));
        alertDialog3.setButton(getString(R.string.si), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Resources res = getResources();
                Backupdb.backupDatabase(path + "daydatabase.db", "Agenda ore/backup_database.db", getActivity(), res);
                Backupdb.backupDatabase(path + "mesidatabase.db", "Agenda ore/backup_mesi.db", getActivity(), res);
                Backupdb.backupDatabase(path + "setdatabase.db", "Agenda ore/backup_set.db", getActivity(), res);

            }
        });
        alertDialog3.setButton2("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog3.show();
    }
}
