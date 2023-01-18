package amsi.dei.estg.ipleiria.evo_menu.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.evo_menu.Model.Restaurante;

public class RestauranteJsonParser
{
    public static ArrayList<Restaurante> parserJsonRestaurante(JSONArray resposta)
    {
        ArrayList<Restaurante> lista = new ArrayList<>();
        try {
            for(int i = 0; i < resposta.length(); i++)
            {
                JSONObject jsonRestaurante = (JSONObject) resposta.get(i);
                int id = jsonRestaurante.getInt("id");
                String nome = jsonRestaurante.getString("nome");
                String email = jsonRestaurante.getString("email");
                int lotacao_max = jsonRestaurante.getInt("lotacao_max");
                String telemovel = jsonRestaurante.getString("telemovel");
                int id_ementa = jsonRestaurante.getInt("id_ementa");
                int id_horario = jsonRestaurante.getInt("id_horario");
                Restaurante restaurante = new Restaurante(id, nome, email, lotacao_max, telemovel, id_ementa, id_horario);
                lista.add(restaurante);
            }


        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return lista;

    }

    public static Restaurante parserJsonRestaurante(String resposta)
    {
        Restaurante restaurante = null;
        try {
            JSONObject jsonRestaurante = new JSONObject(resposta);
            int id = jsonRestaurante.getInt("id");
            String nome = jsonRestaurante.getString("nome");
            String email = jsonRestaurante.getString("email");
            int lotacao_max = jsonRestaurante.getInt("lotacao_max");
            String telemovel = jsonRestaurante.getString("telemovel");
            int id_ementa = jsonRestaurante.getInt("id_ementa");
            int id_horario = jsonRestaurante.getInt("id_horario");

            restaurante = new Restaurante(id, nome, email, lotacao_max, telemovel, id_ementa, id_horario);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return restaurante;

    }


    public static boolean isConnectionInternet(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
