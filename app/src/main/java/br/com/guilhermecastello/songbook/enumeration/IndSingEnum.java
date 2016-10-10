package br.com.guilhermecastello.songbook.enumeration;

import android.content.res.Resources;

import java.util.Arrays;
import java.util.List;

import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.SongbookApplication;

/**
 * Created by guilherme-castello on 28/09/2016.
 */

public enum IndSingEnum {
    MAN(new Byte("1"), SongbookApplication.getContext().getString(R.string.ind_sing_enum_man)),
    WOMAN(new Byte("2"), SongbookApplication.getContext().getString(R.string.ind_sing_enum_woman)),
    ALL(new Byte("3"), SongbookApplication.getContext().getString(R.string.ind_sing_enum_all));

    Byte codigo;

    String  descricao;

    IndSingEnum(Byte cod, String descr) {
        this.codigo = cod;
        this.descricao = descr;
    }

    public Byte getCodigo() {
        return codigo;
    }

    public void setCodigo(Byte codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public static List<IndSingEnum> getAll() {
        return Arrays.asList(IndSingEnum.values());
    }

    public static IndSingEnum get(String descricao) {
        if (descricao != null && descricao.length() != 0) {
            for (IndSingEnum obj : getAll()) {
                if (obj.descricao.equals(descricao)) {
                    return obj;
                }
            }
        }
        return null;
    }

    public static IndSingEnum get(Byte codigo) {
        if (codigo != null) {
            for (IndSingEnum obj : getAll()) {
                if (obj.codigo.equals(codigo)) {
                    return obj;
                }
            }
        }
        return null;
    }


}
