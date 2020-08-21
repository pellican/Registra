package com.progetto.registra;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.progetto.registra.database.DbAdapterMesi;

import java.util.Calendar;

import static java.lang.Math.abs;


public class Frammento_Anno extends Fragment{
    Dialog d;
    DrawerLayout drawerLayout;
    MesiAdapter adapter;
    GridView gridView;
    TextView annoT,lavT,pagT,resT,anniPt,totT;
    ImageView menuI,mesiI;
    Calendar calendar;
    int anno, height;
    public ViewFlipper vf;
    RelativeLayout barbottom;
    boolean giu=true;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View calView = inflater.inflate(R.layout.anno_frammento, container, false);
        calendar = Calendar.getInstance();
        anno=calendar.get(Calendar.YEAR);
        annoT=(TextView)calView.findViewById(R.id.textAnno);
        annoT.setText(""+anno);
        lavT=(TextView)calView.findViewById(R.id.textVoralavA);
        pagT=(TextView)calView.findViewById(R.id.textVpagA);
        resT=(TextView)calView.findViewById(R.id.textVrestoA);
        anniPt=(TextView)calView.findViewById(R.id.restoannoP);
        totT=(TextView)calView.findViewById(R.id.restototale);
        vf = (ViewFlipper) calView.findViewById(R.id.viewflipperA);
        annoT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogData();
            }
        });
        menuI=(ImageView)calView.findViewById(R.id.imageMenuMesi);
        menuI.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        mesiI=(ImageView)calView.findViewById(R.id.imageMese);
        mesiI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Frammento_Mese nextFrag= new Frammento_Mese();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frameContainer, nextFrag).commit();
            }
        });
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerLayout);
        gridView=(GridView)calView.findViewById(R.id.gridViewMesi);
        adapter=new MesiAdapter(getActivity(),anno);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView ore = (TextView) view.findViewById(R.id.txOraMesi);
                TextView orePaT = (TextView) view.findViewById(R.id.texorepag);
                TextView mesi = (TextView) view.findViewById(R.id.txmesi);
                String mesiS = mesi.getText().toString();
                String oreS = ore.getText().toString();
                String orePag = orePaT.getText().toString();
                DialogMese(mesiS, oreS, orePag, position);
            }
        });
        gridView.setOnTouchListener(new OnSwipeTouchListener(){
            @Override
            public boolean onSwipeRight() {
                anno--;
                Refresh();
                Totale();
                vf.setInAnimation(getActivity(), R.anim.slide_in_left);
                vf.setOutAnimation(getActivity(), R.anim.slide_out_right);
                vf.showPrevious();
                return true;
            }

            public boolean onSwipeLeft() {
                anno++;
                Refresh();
                Totale();
                vf.setInAnimation(getActivity(), R.anim.slide_in_right);
                vf.setOutAnimation(getActivity(), R.anim.slide_out_left);
                vf.showNext();
                return true;
            }

        });
        barbottom=(RelativeLayout)calView.findViewById(R.id.barbottom);
        barbottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (giu){
                    SpostaSu(barbottom);
                    giu=false;
                }else {
                    SpostaGiu(barbottom);
                    giu=true;
                }
            }
        });
        return  calView;
    }

    private void SpostaSu( View v) {

        height=v.getHeight();
        double alt=height*2.5;
        ValueAnimator mAnimator = slideAnimator(height,(int)alt , v);
        mAnimator.setDuration(250);
        mAnimator.start();
    }
    private void SpostaGiu( View v) {

        int finalHeight = v.getHeight();
        ValueAnimator mAnimator = slideAnimator(finalHeight,height, v);
        mAnimator.setDuration(200);
        mAnimator.start();
    }
    private ValueAnimator slideAnimator(int start, int end, final View summary) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = summary.getLayoutParams();
                layoutParams.height = value;
                summary.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public  void DialogMese(final String mesi,String ore,String orepagate, final int posizion){
        final Dialog m= new Dialog(getActivity());
        m.setContentView(R.layout.dialog_setmese);
        m.setTitle(mesi + " " + anno);
        Button cal=(Button)m.findViewById(R.id.buttonmes);
        Button ann=(Button)m.findViewById(R.id.buttonann);
        Button ok=(Button)m.findViewById(R.id.buttonok);
        TextView oreT=(TextView)m.findViewById(R.id.textVidimeOre);


        final TextView orePag=(TextView)m.findViewById(R.id.textVidimeOrepag);
        final ImageView x=(ImageView)m.findViewById(R.id.imageX);
        final EditText ed=(EditText)m.findViewById(R.id.editText);
        final ImageView mati=(ImageView)m.findViewById(R.id.imageMatita);
        final TextView MontoreRest=(TextView)m.findViewById(R.id.textRestFinOre);
        final TextView restoOre= (TextView)m.findViewById(R.id.textRestoOre);
        final DbAdapterMesi db = new DbAdapterMesi(getActivity());
        db.open();
        Cursor _c = db.fetchContactsByMese("" + posizion, "" + anno);
        long _id =0;
        if (_c.moveToFirst()) {
            _id = _c.getLong(0);
        }
        if (_id != 0){
            Cursor b = db.CercaFinoAlMeseId(_id);
            if (b.getCount() != 0 && b.moveToFirst()){
                String [] monteOreS = new String[b.getCount()];
                int [] monteOreI = new int[b.getCount()];
                int monteOre = 0;
                for (int i =0;i < b.getCount();i++){
                    monteOreS [i] = b.getString(6);
                    monteOreI [i] = Integer.parseInt(monteOreS[i]);
                    b.moveToNext();
                    monteOre=monteOre+monteOreI[i];
                }
                MontoreRest.setText(String.valueOf(monteOre));
            }

        }

        if (ore != null && ore != "") {
            oreT.setText(ore);
            if (orepagate != null && orepagate !=""){
              int oreI= Integer.parseInt(ore);
              int oreP= Integer.parseInt(orepagate);
                int oret=oreI-oreP;
                restoOre.setText(String.valueOf(oret));
            }
        }

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orePag.setVisibility(View.INVISIBLE);
                restoOre.setVisibility(View.INVISIBLE);
                x.setVisibility(View.INVISIBLE);
                mati.setVisibility(View.VISIBLE);
            }
        });
        mati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed.setVisibility(View.VISIBLE);
                ed.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(ed, InputMethodManager.SHOW_IMPLICIT);
             
                mati.setVisibility(View.INVISIBLE);
            }
        });
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().setAnno(anno);
                Singleton.getInstance().setMese(posizion);
                Frammento_Mese nextFrag = new Frammento_Mese();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frameContainer, nextFrag).commit();
                m.dismiss();
            }
        });
        ann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = db.fetchContactsByMese("" + posizion, "" + anno);
                long id = 0;
                if (c.moveToFirst()) {
                    id = c.getLong(0);
                }
                if (id != 0) {
                    if (!ed.getText().toString().equals("")) {
                        int o= Integer.parseInt(c.getString(3));
                        int p= Integer.parseInt(ed.getText().toString());
                        int r;
                        if (p ==0 ){r=0;}
                        else r=o-p;
                        db.updateContact(id, c.getString(1), c.getString(2), c.getString(3), c.getString(4), ed.getText().toString(), ""+r);
                    }
                }
                Refresh();
                Totale();
                m.dismiss();
            }
        });
        if (!orepagate.equals("0") ){
            mati.setVisibility(View.INVISIBLE);
            x.setVisibility(View.VISIBLE);
            orePag.setVisibility(View.VISIBLE);
            orePag.setText(orepagate);
        }
        m.show();

    }

    public void DialogData() {
        final Dialog d = new Dialog(getActivity());
        d.setContentView(R.layout.dialog_anno);
      // d.getWindow().getAttributes().width=700;
        d.setTitle(getResources().getString(R.string.anno));
        RelativeLayout ann = (RelativeLayout) d.findViewById(R.id.dial_ann);
        RelativeLayout ok = (RelativeLayout) d.findViewById(R.id.dial_ok);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        np.setMaxValue(2050);
        np.setMinValue(2000);
        np.setValue(anno);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anno=np.getValue();
                Refresh();
                Totale();
                d.dismiss();{

                }
            }
        });
        ann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    public void Refresh(){
        adapter.Ore(anno);
        adapter.notifyDataSetChanged();
        annoT.setText("" + anno);
    }
    public void Totale(){
        String[] getore=adapter.getOreM();
        String[] getmin=adapter.getMinM();
        String[] getpag=adapter.getOrePagate();
        String[] getres=adapter.getResto();
        String[] getrestot=adapter.getTotale();
        int[] ore = new int[getore.length];
        int[] min = new int[getore.length];
        int[] pag = new int[getore.length];
        int[] res = new int[getore.length];


        int oret=0;int mint=0;int pagt=0;int rest=0;
        for (int i=0;i<getore.length;i++){
            if (getore[i] !=null){
                ore[i] = Integer.parseInt(getore[i]);
            }
            if (getmin[i] !=null){
                min[i] = Integer.parseInt(getmin[i]);
            }
            if (getpag[i] !=null){
                pag[i] = Integer.parseInt(getpag[i]);
            }
            if (getres[i] !=null){
                res[i] = Integer.parseInt(getres[i]);
            }
            oret=oret+ore[i];
            mint=mint+min[i];
            pagt=pagt+pag[i];
            rest=rest+res[i];
        }
        if (mint >= 60) {
            float z = (((float) mint) / 60.0f) + ((float) oret);
            float v = (z - ((float) ((int) z))) * 60.0f;
            oret = (int) z;
            mint = (int) v;
        }
        int restotT=0;
        if (getrestot != null){
            int[] restot = new int[getrestot.length];
            for (int a=0;a<restot.length;a++){
                if (getrestot[a] != null){
                    restot[a]= Integer.parseInt(getrestot[a]);
                }
                restotT=restotT+restot[a];
            }
        }

        lavT.setText(oret+":"+mint);
        pagT.setText(""+pagt);
        resT.setText(""+rest);
        int diff=restotT-rest;
        anniPt.setText(""+diff);
        totT.setText(""+restotT);

    }

    public void onResume(){
        super.onResume();
        Refresh();
        Totale();
    }
}