package com.progetto.registra;


public class Singleton {
    private static Singleton mInstance;
    int anno=0;
    int mese=0;
    Boolean salvato=false;

    static {
        mInstance = null;
    }

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (mInstance == null) {
            mInstance = new Singleton();
        }
        return mInstance;
    }
    public  Boolean getSalvato(){
        return salvato;
    }
    public void setSalvato(Boolean setsalvato){
        salvato=setsalvato;
    }
    public int getAnno(){
        return anno;
    }
    public int getMese(){
        return mese;
    }
    public void setAnno(int setanno){
        anno=setanno;
    }
    public void setMese(int setmese){
        mese=setmese;
    }
}
