package br.com.guilhermecastello.songbook.util;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.SongbookApplication;

public class Util {

    //public static String ABORDAGEM_KEY = "keytoopenabmfile";

    public static Object invocaMetodo(Object rn, String nomeMetodo, Object parametro) {
        Object retorno = null;
        Class classes[];
        Object param[];

        try {
            if (parametro != null) {
                classes = new Class[1];
                classes[0] = parametro.getClass();

                param = new Object[1];
                param[0] = parametro;
            } else {
                classes = new Class[0];
                param = new Object[0];
            }

            // Pega metodo
            Method metodo = rn.getClass().getMethod(nomeMetodo, classes);

            // Executa metodo
            retorno = metodo.invoke(rn, param);

        } catch (InvocationTargetException ex) {
            Throwable th = ex.getTargetException();
            if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new RuntimeException(th);
            }
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
        return retorno;
    }

    public static String samdToDate(int samd) {

        String retorno = "";
        SimpleDateFormat df = null;
        try {
            if (samd > 0) {
                df = new SimpleDateFormat("yyyyMMdd");
                df.setLenient(false);
                java.util.Date date = df.parse(String.valueOf(samd));

                df = new SimpleDateFormat("dd/MM/yyyy");
                retorno = df.format(date);
            }
        } catch (Exception exc) {
            throw new RuntimeException(exc.toString(), exc);
        }
        return retorno;
    }

    public static Calendar samdToCalendar(int samd) {

        Calendar calendar = null;
        try {
            if (samd > 0) {
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                df.setLenient(false);

                calendar = Calendar.getInstance();
                calendar.setTime(df.parse(String.valueOf(samd)));
            }
        } catch (Exception exc) {
            throw new RuntimeException(exc.toString(), exc);
        }
        return calendar;
    }

    public static int calendarToSamd(Calendar data) {
        String year = data.get(Calendar.YEAR) + "";
        int monthI = data.get(Calendar.MONTH) + 1;
        String month = (monthI < 10) ? "0" + monthI : monthI + "";
        int dayI = data.get(Calendar.DAY_OF_MONTH);
        String day = (dayI < 10) ? "0" + dayI : dayI + "";
        return Integer.parseInt(year + month + day);
    }

    public static String getStringValue(Cursor rs, String columnName) {
        return rs.getString(rs.getColumnIndex(columnName));
    }

    public static Byte getByteValue(Cursor rs, String columnName) {
        Byte retorno = null;
        String value = rs.getString(rs.getColumnIndex(columnName));
        if (value != null)
            retorno = Byte.valueOf(value);
        return retorno;
    }

    public static Short getShortValue(Cursor rs, String columnName) {
        Short retorno = null;
        String value = rs.getString(rs.getColumnIndex(columnName));
        if (value != null)
            retorno = Short.valueOf(value);
        return retorno;
    }

    public static Integer getIntegerValue(Cursor rs, String columnName) {
        Integer retorno = null;
        String value = rs.getString(rs.getColumnIndex(columnName));
        if (value != null)
            retorno = Integer.valueOf(value);
        return retorno;
    }

    public static Long getLongValue(Cursor rs, String columnName) {
        Long retorno = null;
        String value = rs.getString(rs.getColumnIndex(columnName));
        if (value != null)
            retorno = Long.valueOf(value);
        return retorno;
    }

    public static Double getDoubleValue(Cursor rs, String columnName) {
        Double retorno = null;
        String value = rs.getString(rs.getColumnIndex(columnName));
        if (value != null)
            retorno = Double.valueOf(value);
        return retorno;
    }

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId().replace(" ", "").replace("-", "").replace("/", "").replace("\\", "").replace(".", "");
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean isConnectivityWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean validaCpf(Long cpf) {
        String oCpf = cpf.toString();

        return validaCpf("00000000000".substring(oCpf.length()) + oCpf);
    }

    public static boolean validaCpf(String cpf) {
        int soma = 0;

        if (cpf.length() == 11) {
            for (int i = 0; i < 9; i++) {
                soma += (10 - i) * (cpf.charAt(i) - '0');
            }
            soma = 11 - (soma % 11);
            if (soma > 9) {
                soma = 0;
            }
            if (soma == (cpf.charAt(9) - '0')) {
                soma = 0;
                for (int i = 0; i < 10; i++) {
                    soma += (11 - i) * (cpf.charAt(i) - '0');
                }
                soma = 11 - (soma % 11);
                if (soma > 9) {
                    soma = 0;
                }
                if (soma == (cpf.charAt(10) - '0')) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void showWarning(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(SongbookApplication.getContext().getString(R.string.alert_box_title));
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //  public static boolean deveAtualizarVersao(String versao, PackageInfo pi) {
    //    boolean retorno = false;
    //    int versaoLocal = 0;
    //    int versaoServidor = 0;
    //
    //    if (versao != null && versao.length() > 0) {
    //      versaoLocal = Integer.parseInt(pi.versionName.replace(".", ""));
    //      versaoServidor = Integer.parseInt(versao.replace(".", ""));
    //
    //      if (versaoLocal < versaoServidor) {
    //        retorno = true;
    //      }
    //    }
    //    return retorno;
    //  }

    //  public static void mostraNovidades(Context context, int resXmlId) {
    //    mostraNovidades(context, resXmlId, false);
    //  }
    //
    //  public static void mostraNovidades(Context context, int resXmlId, boolean verifica) {
    //    try {
    //      boolean mostra = false;
    //
    //      if (verifica) {
    //        // Obtem ultima versao verificada
    //        SharedPreferences settings = context.getSharedPreferences("PrUtilPrefs", 0);
    //        String lastVersion = settings.getString("lastVersion", null);
    //
    //        // Obtem versao atual do app
    //        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
    //        String versaoAtual = pInfo.versionName;
    //
    //        // Verifica se deve mostrar
    //        if (lastVersion == null) {
    //          mostra = true;
    //        } else {
    //          // Verifica se mudou a versão
    //          if (!versaoAtual.equals(lastVersion)) {
    //            mostra = true;
    //          }
    //        }
    //
    //        if (mostra) {
    //          // Salva nova versão
    //          SharedPreferences.Editor editor = settings.edit();
    //          editor.putString("lastVersion", versaoAtual);
    //          editor.commit();
    //        }
    //
    //      } else {
    //        mostra = true;
    //      }
    //      if (mostra) {
    //        PrNovidadesDialog dialog = new PrNovidadesDialog(context, resXmlId);
    //        dialog.show();
    //      }
    //    } catch (Exception exc) {
    //      throw new RuntimeException(exc);
    //    }
    //  }


    public static File findPathFiles(Context context, String appName) throws Exception {
        File pathFiles = null;

        SongbookApplication prApp = (SongbookApplication) context.getApplicationContext();

        StringBuilder path = new StringBuilder();
        path.append(Environment.DIRECTORY_DOCUMENTS);
        path.append(File.separator);
        path.append(appName);
        path.append(File.separator);
        path.append("NewSongs");

        pathFiles = Environment.getExternalStoragePublicDirectory(path.toString());

        // Cria se não existir
        if (!pathFiles.exists()) {
            pathFiles.mkdirs();
        }

        return pathFiles;
    }


    //  public static View getView(Context context, int resource, int idView) {
    //    FrameLayout linearLayout = (FrameLayout) LayoutInflater.from(context).inflate(resource, null);
    //    View input = (View) linearLayout.findViewById(idView);
    //
    //    ViewGroup parent = (ViewGroup) input.getParent();
    //    if (parent != null) {
    //      parent.removeView(input);
    //    }
    //
    //    return input;
    //  }
    //
    //  public static String montaObservacoes(List<String> lista) {
    //    StringBuilder observacoes = new StringBuilder();
    //    for (String obs : lista) {
    //      if (observacoes.length() > 0) {
    //        observacoes.append("; ");
    //      }
    //      observacoes.append(obs);
    //    }
    //    return observacoes.toString();
    //  }
    //
    //  public static String[] obterPermissoesApp(Context context) {
    //    List<String> permissoesNecessarias = new ArrayList<String>();
    //
    //    try {
    //
    //      // Nao verifica permissoes para versoes inferiores a 6.0
    //      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
    //        return new String[]{};
    //      }
    //
    //      PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
    //
    //      if (info.requestedPermissions != null) {
    //        for (String permissao: info.requestedPermissions) {
    //          if (ContextCompat.checkSelfPermission(context, permissao) != PackageManager.PERMISSION_GRANTED) {
    //
    //            if (Manifest.permission.WRITE_SETTINGS.equals(permissao)) {
    //              boolean possuiPermissaoWriteSettings = checkSystemWritePermission(context);
    //              if (!possuiPermissaoWriteSettings) {
    //                return null;
    //              }
    //              continue;
    //            }
    //
    //            permissoesNecessarias.add(permissao);
    //          }
    //        }
    //      }
    //    } catch (Exception exc) {
    //      throw new RuntimeException(exc);
    //    }
    //
    //
    //    String[] permissoes = permissoesNecessarias.toArray(new String[0]);
    //
    //    return permissoes;
    //  }
    //
    //  @TargetApi(23)
    //  private static boolean checkSystemWritePermission(Context context) {
    //    boolean retVal = true;
    //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    //        retVal = Settings.System.canWrite(context);
    //        if(!retVal){
    //            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
    //            intent.setData(Uri.parse("package:" + context.getPackageName()));
    //            context.startActivity(intent);
    //        }
    //    }
    //    return retVal;
    //  }

}