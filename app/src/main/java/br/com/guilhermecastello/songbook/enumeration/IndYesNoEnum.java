package br.com.guilhermecastello.songbook.enumeration;

import java.util.Arrays;
import java.util.List;

import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.SongbookApplication;

/**
 * Created by guilherme-castello on 28/09/2016.
 */

public enum IndYesNoEnum {
    YES(new Byte("1"), SongbookApplication.getContext().getString(R.string.ind_yesno_enum_yes)),
    NO(new Byte("2"), SongbookApplication.getContext().getString(R.string.ind_yesno_enum_no));

    Byte codigo;

    String  descricao;

    IndYesNoEnum(Byte cod, String descr) {
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

    public static List<IndYesNoEnum> getAll() {
        return Arrays.asList(IndYesNoEnum.values());
    }

    public static IndYesNoEnum get(String descricao) {
        if (descricao != null && descricao.length() != 0) {
            for (IndYesNoEnum obj : getAll()) {
                if (obj.descricao.equals(descricao)) {
                    return obj;
                }
            }
        }
        return null;
    }

    public static IndYesNoEnum get(Byte codigo) {
        if (codigo != null) {
            for (IndYesNoEnum obj : getAll()) {
                if (obj.codigo.equals(codigo)) {
                    return obj;
                }
            }
        }
        return null;
    }


}
