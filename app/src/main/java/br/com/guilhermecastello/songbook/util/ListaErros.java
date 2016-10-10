package br.com.guilhermecastello.songbook.util;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by guilherme-castello on 27/09/2016.
 */

public class ListaErros implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> mLista = null;

    public ListaErros() {
        mLista = new LinkedList<String>();
    }

    public void add(String descricao) {
        mLista.add(descricao);
    }

    public void clear() {
        mLista.clear();
    }

    public String get(int position) {
        return mLista.get(position);
    }

    public int size() {
        return mLista.size();
    }

}
