package com.progetto.registra;




import java.util.Calendar;


import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.progetto.registra.database.DbAdapterDay;
import com.progetto.registra.database.DbAdapterSet;


public class GiornoActivity extends Activity{
	int orain,minin,orpaM,mipaM,oraFiM,minFiM;
	int oraInP,minInP,orapaP,minpaP,oraFin,minFin;
	int oratotM,mintotM,oratotP,mintotP,oraT,minT;
	Calendar data=Calendar.getInstance();
	long iD,idM;
	Boolean s=true;
	Boolean errore = false;
	String ze="00:00";
	String[] oreS ={"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
	String[] minS ={"00","15","30","45"};
	String sett1,sett2,dataI,verifica;
	ImageView modif,salva,canc;
	TextView txdata,txorainM,txpauseM,txorafiM,texsalva;
	TextView txorainP,txpauseP,txorafin,txtotale;
	AdView mAdView;
	Vibrator vibrator;
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_giorno);
		vibrator= (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
	    txdata=(TextView)findViewById(R.id.textData);	   
	    dataI= getIntent().getStringExtra("date");
        verifica=getIntent().getStringExtra("verifica");
	    txdata.setText(dataI);
	    if (dataI==null){
	    	txdata.setText(android.text.format.DateFormat.format("dd-MMMM-yyyy",data));
	    }
	    ImageView ind=(ImageView)findViewById(R.id.imWind);
	    ind.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {GiornoActivity.this.finish();		}
		});
		texsalva=(TextView)findViewById(R.id.textsalva);
        salva=(ImageView)findViewById(R.id.imWsalva);
	    salva.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (errore){
					Errore();

				}else{
					conferma();
				}

			}
		
		});
		canc=(ImageView)findViewById(R.id.imWcanc);

	    txorainM=(TextView)findViewById(R.id.txorainM);
	    txorainM.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {FinestraIn("in",txorainM,1,2);		}
		});
	    txpauseM=(TextView)findViewById(R.id.textPausM);
	    txpauseM.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {FinestraPa("paMa",txpauseM);	}
		});
	    txorafiM=(TextView)findViewById(R.id.txorafiM);
	    txorafiM.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {FinestraIn("fiMa",txorafiM,3,4);	}
		});
	    txorainP=(TextView)findViewById(R.id.txorainP);
	    txorainP.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {FinestraIn("inP",txorainP,5,6);	}
		});
	    txpauseP=(TextView)findViewById(R.id.txoraPaPo);
	    txpauseP.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {FinestraPa("paPo",txpauseP);	}
		});
	    txorafin=(TextView)findViewById(R.id.txoraFin);
	    txorafin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FinestraIn("fin", txorafin, 7, 8);
			}
		});
        modif=(ImageView)findViewById(R.id.imWmodif);
        txtotale=(TextView)findViewById(R.id.textotale);
        Totale();
        if (verifica.equals("salvato")){
            salva.setVisibility(View.INVISIBLE);
			texsalva.setVisibility(View.INVISIBLE);
            DbAdapterDay db= new DbAdapterDay(getApplication());
            db.open();
            Cursor cur= db.fetchContactsByData(dataI);
            if (cur.moveToFirst()){
                idM=cur.getLong(0);
                txorainM.setText(cur.getString(2));txorainM.setTextColor(getResources().getColor(color.black));txorainM.setClickable(false);
                txpauseM.setText(cur.getString(3));txpauseM.setTextColor(getResources().getColor(color.black));txpauseM.setClickable(false);
                txorafiM.setText(cur.getString(4));txorafiM.setTextColor(getResources().getColor(color.black));txorafiM.setClickable(false);
                txorainP.setText(cur.getString(5));txorainP.setTextColor(getResources().getColor(color.black));txorainP.setClickable(false);
                txpauseP.setText(cur.getString(6));txpauseP.setTextColor(getResources().getColor(color.black));txpauseP.setClickable(false);
                txorafin.setText(cur.getString(7));txorafin.setTextColor(getResources().getColor(color.black));txorafin.setClickable(false);
                txtotale.setText(cur.getString(8));
            }
            db.close();
            modif.setVisibility(View.VISIBLE);
			canc.setVisibility(View.VISIBLE);

        }
        modif.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txorainM.setClickable(true);
				orain = Integer.parseInt(txorainM.getText().toString().substring(0, 2));
				minin = Integer.parseInt(txorainM.getText().toString().substring(3, 5));
				txpauseM.setClickable(true);
				orpaM = Integer.parseInt(txpauseM.getText().toString().substring(0, 2));
				mipaM = Integer.parseInt(txpauseM.getText().toString().substring(3, 5));
				txorafiM.setClickable(true);
				oraFiM = Integer.parseInt(txorafiM.getText().toString().substring(0, 2));
				minFiM = Integer.parseInt(txorafiM.getText().toString().substring(3, 5));
				txorainP.setClickable(true);
				oraInP = Integer.parseInt(txorainP.getText().toString().substring(0, 2));
				minInP = Integer.parseInt(txorainP.getText().toString().substring(3, 5));
				txpauseP.setClickable(true);
				orapaP = Integer.parseInt(txpauseP.getText().toString().substring(0, 2));
				minpaP = Integer.parseInt(txpauseP.getText().toString().substring(3, 5));
				txorafin.setClickable(true);
				oraFin = Integer.parseInt(txorafin.getText().toString().substring(0, 2));
				minFin = Integer.parseInt(txorafin.getText().toString().substring(3, 5));
				salva.setVisibility(View.VISIBLE);
				texsalva.setVisibility(View.VISIBLE);
				modif.setVisibility(View.INVISIBLE);
				Toast.makeText(getApplication(), R.string.modifica, Toast.LENGTH_SHORT).show();
			}
		});
		canc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(GiornoActivity.this)
					.setTitle(R.string.vuoi_eliminare)
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							DbAdapterDay db =new DbAdapterDay(getApplication());
							db.open();
							db.delete_byData(dataI);
							db.close();
							dialog.dismiss();
							finish();
						}
					})
					.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();

			}
		});
		mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
}

	private void Errore() {
		new Builder(this)
				.setTitle(R.string.errore)
				.setMessage(R.string.controlla_i_dati)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.create()
				.show();
	}

	protected void FinestraPa(final String id,final TextView tx) {
		LayoutInflater popup =(LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		 View popupView = popup.inflate(R.layout.popup_pa, null);
		 final PopupWindow   popupWindow = new PopupWindow(
			      popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		 Button p15 =(Button)popupView.findViewById(R.id.Button15);
		 Button p30 =(Button)popupView.findViewById(R.id.Button30);
		 Button p45 =(Button)popupView.findViewById(R.id.Button45);
		 Button ann =(Button)popupView.findViewById(R.id.butAnnP);
		 Button ok=(Button)popupView.findViewById(R.id.butOkP);
		final NumberPicker ore=(NumberPicker)popupView.findViewById(R.id.number_oreP);
		final NumberPicker min=(NumberPicker)popupView.findViewById(R.id.number_minP);
		ore.setMinValue(0);ore.setMaxValue(24);ore.setDisplayedValues(oreS);
		min.setMinValue(0);min.setMaxValue(3); min.setDisplayedValues(minS);

		 p15.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {min.setValue(1);ore.setValue(0);	}		});
		 p30.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {min.setValue(2);ore.setValue(0);	}		});
		 p45.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {min.setValue(3);ore.setValue(0);	}		});
		 ann.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {popupWindow.dismiss();		}		});
		 ok.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				vibrator.vibrate(100);
				tx.setText(oreS[ore.getValue()]+":"+minS[min.getValue()]);
				tx.setTextColor(getResources().getColor(color.black));

				if (id.equals("paMa")){orpaM=ore.getValue();mipaM=Integer.parseInt(minS[min.getValue()]);}
				else if (id.equals("paPo")){orapaP=ore.getValue();minpaP=Integer.parseInt(minS[min.getValue()]);}
				popupWindow.dismiss();	
				Totale();
			}
		});
		   popupWindow.setBackgroundDrawable (new BitmapDrawable());
		   popupWindow.setFocusable(false);
		   popupWindow.setOutsideTouchable(true);	
		   popupWindow.showAsDropDown(tx, 0, -150);
	}
	
	
	protected void FinestraIn(final String id,final TextView tx,final int x1,final int x2){
		LayoutInflater popup =(LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		 View popupView = popup.inflate(R.layout.popup_in, null);
		 final PopupWindow   popupWindow = new PopupWindow(
			      popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			final NumberPicker ore=(NumberPicker)popupView.findViewById(R.id.number_ore);
			final NumberPicker min=(NumberPicker)popupView.findViewById(R.id.number_min);
			ore.setMinValue(0);ore.setMaxValue(24);ore.setDisplayedValues(oreS);
			min.setMinValue(0);min.setMaxValue(3); min.setDisplayedValues(minS);
		   final Button set1=(Button) popupView.findViewById(R.id.tmP1);
		   final Button set2=(Button) popupView.findViewById(R.id.tmP2);
		   Button set =(Button) popupView.findViewById(R.id.butSet)	;
		   Button ann=(Button) popupView.findViewById(R.id.butAnn);
		   Button ok=(Button) popupView.findViewById(R.id.butOk);
		    final DbAdapterSet db=new DbAdapterSet(getApplicationContext());
		   db.open(); Cursor leggi = db.fetchAllContacts();
		   final String[] data= new String[9];
		   if(leggi.moveToFirst()){               
	           for(int i=0;i<9;i++) {	            
	        	   String uname = leggi.getString(i);
	        	    data[i] = uname; }	  }		   
		   set1.setText(data[x1]);set2.setText(data[x2]);
		   set.setOnClickListener(new OnClickListener() {			
			@Override
				public void onClick(View v) {				
				String o=oreS[ore.getValue()];String m=minS[min.getValue()];
				if (o.length()==1){	o="0"+o;}
				if (m.length()==1){ m="0"+m;}
				if (s){ sett1=o+":"+m;set1.setText(sett1);data[x1]=sett1;
					db.updateContact(iD,data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8]);}
				if (!s){sett2=o+":"+m;set2.setText(sett2);data[x2]=sett2;
					db.updateContact(iD,data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8]);}
				if (s){s=false;}else{s=true;}
			}		   		});
		   ok.setOnClickListener(new OnClickListener() {			
			@Override
				public void onClick(View v) {
				vibrator.vibrate(100);
				tx.setText(oreS[ore.getValue()]+":"+minS[min.getValue()]);
				tx.setTextColor(getResources().getColor(color.black));
				int or=ore.getValue();
			//	or = (or == 0) ? 24 : or;
				if (id.equals("in")){orain=ore.getValue();minin=Integer.parseInt(minS[min.getValue()]);}
				else if (id.equals("fiMa")){oraFiM=ore.getValue();minFiM=Integer.parseInt(minS[min.getValue()]);}
				else if (id.equals("inP")){oraInP=ore.getValue();minInP=Integer.parseInt(minS[min.getValue()]);}
				else if (id.equals("fin")){oraFin=or;minFin=Integer.parseInt(minS[min.getValue()]);	}
				Totale();
                db.close();
				popupWindow.dismiss();
			}				});
		   ann.setOnClickListener(new OnClickListener() {			
			@Override
				public void onClick(View v) {
                db.close();
				popupWindow.dismiss();				
			}				});
		   set1.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				String t1=set1.getText().toString();			
				int or=Integer.parseInt(t1.substring(0,2));

				String mi=t1.substring(3,5);
				ore.setValue(or);
				for (int a=0;a<minS.length;a++){
					if (mi.equals( minS[a])){
						min.setValue(a);
					}
				}

			}				});
		   set2.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				String t1=set2.getText().toString();			
				int or=Integer.parseInt(t1.substring(0,2));

				String mi=t1.substring(3,5);
				ore.setValue(or);
				for (int a=0;a<minS.length;a++){
					if (mi.equals( minS[a])){
						min.setValue(a);
					}
				}
			}				});	
		   popupWindow.setBackgroundDrawable (new BitmapDrawable());
		   popupWindow.setFocusable(false);
		   popupWindow.setOutsideTouchable(true);
            if (id.equals("fin")){
                popupWindow.showAsDropDown(tx, 0, -250);
            }else{  popupWindow.showAsDropDown(tx, 0, -150);}
		    
	}
	
	public void Totale(){
		oratotM=(oraFiM-orain)-orpaM;

		mintotM=(minFiM-minin)-mipaM;
		float m = ((float) this.mintotM) / 60.0f;
        if (m < 0.0f) {
            float a = m + ((float) this.oratotM);
            float b = (a - ((float) ((int) a))) * 60.0f;
            this.oratotM = (int) a;
            this.mintotM = (int) b;
        }
        oratotP=(oraFin-oraInP)-orapaP;
        mintotP=(minFin-minInP)-minpaP;
        float p = ((float) this.mintotP) / 60.0f;
        if (p < 0.0f) {
            float c = p + ((float) this.oratotP);
            float d = (c - ((float) ((int) c))) * 60.0f;
            this.oratotP = (int) c;
            this.mintotP = (int) d;
        }
        oraT=oratotM+oratotP;
        minT=mintotM+mintotP;
        if (this.minT >= 60) {
            float z = (((float) this.minT) / 60.0f) + ((float) this.oraT);
            float v = (z - ((float) ((int) z))) * 60.0f;
            this.oraT = (int) z;
            this.minT = (int) v;
        }

        String oT=""+oraT;String mT=""+minT;
        if (oT.length()==1){
        	oT="0"+oT;
        }
        if (mT.length()==1){
        	mT="0"+mT;
        }

		final float scale = getResources().getDisplayMetrics().density;
		int padd_12 = (int) (12 * scale + 0.5f);
		int padd_4 =(int) (4 *scale + 0.5f);
		if (oraT <0 || minT <0 ){
			txtotale.setText(ze);
			txorafiM.setPadding(padd_12,padd_4,0,padd_4);
			txorafin.setPadding(padd_12,padd_4,0,padd_4);
			txorafiM.setError("");
			txorafin.setError("");
			errore=true;
		}else {
			txtotale.setText(oT + ":" + mT);
			txorafiM.setPadding(padd_12,padd_4,padd_12,padd_4);
			txorafin.setPadding(padd_12,padd_4,padd_12,padd_4);
			txorafiM.setError(null);
			txorafin.setError(null);
			errore=false;
		}
	}
	
	protected void conferma() {
        Builder alertDialog2 = new Builder(this);
        alertDialog2.setTitle(txdata.getText() + " ore: " + txtotale.getText());
        if (verifica.equals("salvato")){
            alertDialog2.setMessage(R.string.vioi_modificare);
        }else {alertDialog2.setMessage(R.string.vuoi_salvare);}
        alertDialog2.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	DbAdapterDay sl=new DbAdapterDay(getApplicationContext());
				sl.open();
                if(verifica.equals("salvato")){
                    sl.updateContact(idM,txdata.getText().toString(), txorainM.getText().toString(), txpauseM.getText().toString(),
                            txorafiM.getText().toString(), txorainP.getText().toString(), txpauseP.getText().toString(),
                            txorafin.getText().toString(), txtotale.getText().toString());
					Singleton.getInstance().setSalvato(true);
                    Toast.makeText(getApplication(), R.string.dati_salvati,Toast.LENGTH_SHORT).show();
                }
				else if (verifica.equals("bianco")) {
                    sl.createContact(txdata.getText().toString(), txorainM.getText().toString(), txpauseM.getText().toString(),
                            txorafiM.getText().toString(), txorainP.getText().toString(), txpauseP.getText().toString(),
                            txorafin.getText().toString(), txtotale.getText().toString());
					Singleton.getInstance().setSalvato(true);
                    Toast.makeText(getApplication(),R.string.dati_salvati,Toast.LENGTH_SHORT).show();
                }else {Toast.makeText(getApplication(),"Errore...",Toast.LENGTH_SHORT).show();}
                sl.close();
                GiornoActivity.this.finish();


            }
        });
        alertDialog2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog2.show();
    }

	@Override
	protected void onResume() {
		super.onResume();
		DbAdapterSet db =new DbAdapterSet(getApplicationContext());
		db.open(); Cursor leggi = db.fetchAllContacts();
		iD=0;
		if (leggi.moveToLast()) {  iD = leggi.getLong(0);}
		if (iD == 0) { db.createContact(ze,ze,ze,ze,ze,ze,ze,ze );     }
		db.close();
		if (mAdView != null) {
			mAdView.resume();
		}

	}

	@Override
	public void onDestroy() {
		if (mAdView != null) {
			mAdView.destroy();
		}
		super.onDestroy();
	}

}