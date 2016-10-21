package br.com.guilhermecastello.songbook.enumeration;

import java.util.Arrays;
import java.util.List;

import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.SongbookApplication;

/**
 * Created by guilherme-castello on 28/09/2016.
 */

public enum ImportStatusEnum {
    PENDING(new Short("1"), SongbookApplication.getContext().getString(R.string.import_status_enum_pending)),
    IMPORTED(new Short("2"), SongbookApplication.getContext().getString(R.string.import_status_enum_imported)),
    FAIL(new Short("3"), SongbookApplication.getContext().getString(R.string.import_status_enum_fail));

    Short codigo;

    String  descricao;

    ImportStatusEnum(Short cod, String descr) {
        this.codigo = cod;
        this.descricao = descr;
    }

    public Short getCodigo() {
        return codigo;
    }

    public void setCodigo(Short codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public static List<ImportStatusEnum> getAll() {
        return Arrays.asList(ImportStatusEnum.values());
    }

    public static ImportStatusEnum get(String descricao) {
        if (descricao != null && descricao.length() != 0) {
            for (ImportStatusEnum obj : getAll()) {
                if (obj.descricao.equals(descricao)) {
                    return obj;
                }
            }
        }
        return null;
    }

    public static ImportStatusEnum get(Short codigo) {
        if (codigo != null) {
            for (ImportStatusEnum obj : getAll()) {
                if (obj.codigo.equals(codigo)) {
                    return obj;
                }
            }
        }
        return null;
    }


}
