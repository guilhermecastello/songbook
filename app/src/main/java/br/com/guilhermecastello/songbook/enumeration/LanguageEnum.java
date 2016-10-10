package br.com.guilhermecastello.songbook.enumeration;

import java.util.Arrays;
import java.util.List;

import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.SongbookApplication;

/**
 * Created by guilherme-castello on 28/09/2016.
 */

public enum LanguageEnum {
    PORTUGUESE(new Short("1"), SongbookApplication.getContext().getString(R.string.language_enum_portuguese), "pt"),
    ENGLISH(new Short("2"), SongbookApplication.getContext().getString(R.string.language_enum_english), "en"),
    SPANISH(new Short("3"), SongbookApplication.getContext().getString(R.string.language_enum_spanish), "sp");

    Short codigo;

    String  descricao;

    String language;

    LanguageEnum(Short cod, String descr, String language) {
        this.codigo = cod;
        this.descricao = descr;
        this.language = language;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public static List<LanguageEnum> getAll() {
        return Arrays.asList(LanguageEnum.values());
    }

    public static LanguageEnum get(String descricao) {
        if (descricao != null && descricao.length() != 0) {
            for (LanguageEnum obj : getAll()) {
                if (obj.descricao.equals(descricao)) {
                    return obj;
                }
            }
        }
        return null;
    }

    public static LanguageEnum get(Short codigo) {
        if (codigo != null) {
            for (LanguageEnum obj : getAll()) {
                if (obj.codigo.equals(codigo)) {
                    return obj;
                }
            }
        }
        return null;
    }

    public static LanguageEnum getByLanguage(String language) {
        if (language != null && language.length() != 0) {
            for (LanguageEnum obj : getAll()) {
                if (obj.language.equals(language)) {
                    return obj;
                }
            }
        }
        return null;
    }


}
