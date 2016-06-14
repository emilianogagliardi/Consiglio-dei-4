package client.strutturedati;

/*
lista circolare:
contiene un intero che rappresenta l'elemento corrente, le uniche operazione permesse sono
accesso all'ememento puntato, spostamento di una posizione (destra o sinistra) del puntatore.
alla creazione prende un insieme di elementi, ed è poi immutabile. Il cursore è inizialmente
posto a 0
 */

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.ArrayList;
import java.util.Arrays;

@Immutable
public class ListaCircolare <T>{
    private ArrayList<T> lista;
    private int i;

    public ListaCircolare(T... elementi){
        lista = new ArrayList<T>();
        Arrays.stream(elementi).forEach((T elemento) -> lista.add(elemento));
    }

    public void scorriDestra() {
        i = (i+1)%lista.size();
    }
    public void scorriSinistra() {
        i = (i-1);
        if (i < 0) i = lista.size()-1;
    }

    public T getCorrente(){
        return lista.get(i);
    }
}