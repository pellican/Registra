package com.progetto.registra;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class Main2Activity extends Activity {
    private DrawerLayout drawerLayout;
    private ListView drawerList;

    private Handler mHandler = new Handler();

    private boolean tastoindietro = false;
    String[] andoridVeriosnArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        andoridVeriosnArray = new String[]{getResources().getString(R.string.mese),getResources().getString(R.string.anno),
                getResources().getString(R.string.opzioni),"info"};

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerList = (ListView) findViewById(R.id.drawerList);
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, andoridVeriosnArray));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        if (savedInstanceState == null) {
            selectItem(0);
        }

    }



    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Frammento_Mese();
                break;
            case 1:
                fragment = new Frammento_Anno();
                break;
            case 2:
                fragment = new Frammento_Backup();
                break;
            case 3:
                Info();
                break;
            default:
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frameContainer, fragment).commit();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
            }, 200);

        }
    }


    public void Info() {
        final Dialog info = new Dialog(this);
        info.setContentView(R.layout.dialog_info);


        Button ok = (Button) info.findViewById(R.id.buttoninfo);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.dismiss();
            }
        });
        info.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        tastoindietro = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Singleton.getInstance().setMese(0);
        Singleton.getInstance().setAnno(0);
    }

    @Override
    public void onBackPressed() {
        if (tastoindietro) {
            super.onBackPressed();
        } else {
            tastoindietro = true;
            Fragment fragment = new Frammento_Mese();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frameContainer, fragment).commit();
            Toast.makeText(getApplication(), R.string.premi, Toast.LENGTH_SHORT).show();
        }
    }
}





