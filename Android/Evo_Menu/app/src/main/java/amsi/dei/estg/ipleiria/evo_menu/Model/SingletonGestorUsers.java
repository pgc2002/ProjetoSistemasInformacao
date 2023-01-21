package amsi.dei.estg.ipleiria.evo_menu.Model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//Class unica, nao se repete mas pode ser acedida.
/*import amsi.dei.estg.ipleiria.evo_menu.Listeners.LivroListener;
import amsi.dei.estg.ipleiria.evo_menu.Listeners.LivrosListener;
import amsi.dei.estg.ipleiria.evo_menu.Listeners.LoginListener;*/

import amsi.dei.estg.ipleiria.evo_menu.Model.Listeners.UserListener;
import amsi.dei.estg.ipleiria.evo_menu.Model.Listeners.UsersListener;
import amsi.dei.estg.ipleiria.evo_menu.R;
import amsi.dei.estg.ipleiria.evo_menu.Utils.UserJsonParser;

//import amsi.dei.estg.ipleiria.evo_menu.Views.DetalhesLivroActivity;

public class SingletonGestorUsers {
    private final static String ip = "192.168.1.87";
    private final static String mUrlAPIuser = "http://"+ ip +"/ProjetoEvoMenus/projetofinal/backend/web/api/user";
    private UserBdHelper usersBD = null;
    private static SingletonGestorUsers instancia = null;
    private ArrayList<User> users;
    private static RequestQueue volleyQueue = null;
    private String validacao;
    private UsersListener usersListener;
    private UserListener userListener;

    //Verificar se ja existe ou nao
    public static synchronized SingletonGestorUsers getInstance(Context contexto) {
        if (instancia == null) {
            instancia = new SingletonGestorUsers(contexto);
            volleyQueue = Volley.newRequestQueue(contexto);
        }
        return instancia;
    }

    private SingletonGestorUsers(Context contexto) {
        users = new ArrayList<>();
        usersBD = new UserBdHelper(contexto);
        validacao = "";
    }

    public ArrayList<User> getUsersBD() {
        return users = usersBD.getAllUsersBD();
    }

    //Buscar os users do ficheiro criado para a lista
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public User getUser(long id) {
        for (User user : users) {
            return user;
        }
        return null;
    }

    public void adicionarUsersBD(ArrayList<User> users) {
        usersBD.removerAllUsersBD();
        for (User user : users) {
            adicionarUserBD(user);
        }
    }

    //CRUD
    public void adicionarUserBD(User user) {
        usersBD.adicionarUserBD(user);
    }

    public void removerUserBD(long id) {
        User user = getUser(id);
        if (user != null) {
            usersBD.removerUserBD(user.getId());
        }

    }

    public void editarUserBD(User dadosUser) {
        User user = getUser(dadosUser.getId());
        if (user != null) {
            usersBD.editarUserBD(dadosUser);

        }
    }

    //pedidos a api
    public void adicionarUserAPI(final User user, final String pais, final String cidade, final String rua, final String codpost,  final Context contexto)
    {
        if (!UserJsonParser.isConnectionInternet(contexto)) {
            Toast.makeText(contexto, R.string.no_internet, Toast.LENGTH_SHORT);
            return;
        }
        StringRequest request = new StringRequest(Request.Method.POST, mUrlAPIuser + "/criar?username=" + user.getUsername() + "&nome=" + user.getNome() + "&password=" + user.getPass() + "&email=" + user.getEmail() + "&telemovel=" + user.getTelemovel() + "&nif=" + user.getNif() + "&pais=" + pais + "&cidade=" + cidade + "&rua=" + rua + "&codpost=" + codpost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                adicionarUserBD(UserJsonParser.parserJsonUser(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contexto, error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        });
        volleyQueue.add(request);
    }

    public void getAllUsersAPI(final Context contexto)
    {
        if(!UserJsonParser.isConnectionInternet(contexto))
        {
            Toast.makeText(contexto, R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, mUrlAPIuser, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                users = UserJsonParser.parserJsonUsers(response);
                adicionarUsersBD(users);
                //Ativar o listener
                if(userListener!=null)
                {
                    usersListener.onRefreshListaUsers(users);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contexto, error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        });
        volleyQueue.add(req);
    }

    public void removerUserAPI(final User user, final Context contexto)
    {
        if(!UserJsonParser.isConnectionInternet(contexto))
        {
            Toast.makeText(contexto, R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest req = new StringRequest(Request.Method.DELETE, mUrlAPIuser + '/' + user.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                removerUserBD(user.getId());
                //ativar listener
                /*if(livroListener != null)
                {
                    livroListener.onRefreshDetalhes(DetalhesLivroActivity.OP_CODE_APAGAR);
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contexto, error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        });
        volleyQueue.add(req);
    }

    public void editarUserAPI(final User user, Context contexto)
    {
        if(!UserJsonParser.isConnectionInternet(contexto))
        {
            Toast.makeText(contexto, R.string.no_internet, Toast.LENGTH_SHORT);
            return;
        }
        StringRequest request = new StringRequest(Request.Method.PUT, mUrlAPIuser + "/alterarperfil" +
                "?idUser=" + user.getId() + "&username=" + user.getUsername() + "&nome=" + user.getNome() +
                "&password=" + user.getPass() + "&email=" + user.getEmail() + "&telemovel=" + user.getTelemovel() +
                "&nif=" + user.getNif()
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //editarUserBD(user);
                //ativar o listener...
                /*if(livroListener != null)
                {
                    livroListener.onRefreshDetalhes(DetalhesLivroActivity.OP_CODE_EDITAR);
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contexto, error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        });
        /*{
            @Nullable
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", "" + user.getId());
                params.put("username", user.getUsername());
                params.put("nome", user.getNome());
                params.put("password", user.getPass());
                params.put("email", user.getEmail());
                params.put("telemovel", user.getTelemovel());
                params.put("nif", user.getNif());
                params.put("tipo", user.getTipo());
                params.put("nome", user.getNome());
                params.put("idRestaurante", "" + user.getId_restaurante());
                params.put("idMorada", "" + user.getId_morada());
                params.put("idMesa", "" + user.getId_mesa());
                return params;

            };
        }*/
        volleyQueue.add(request);
    }

    public void validacaoPassAPI(final String username, final String pass, Context contexto){
        if(!UserJsonParser.isConnectionInternet(contexto))
        {
            Toast.makeText(contexto, R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        StringRequest req = new StringRequest(Request.Method.GET, mUrlAPIuser  + "/validar?username="+ username +"&password="+pass, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    validacao = UserJsonParser.parserJsonValidacao(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Log.d("testeValidacao", error.getMessage());
                return;
            }
        });
        volleyQueue.add(req);
    }

    public void editarMoradaAPI(final Morada morada, Context contexto)
    {
        if(!UserJsonParser.isConnectionInternet(contexto))
        {
            Toast.makeText(contexto, R.string.no_internet, Toast.LENGTH_SHORT);
            return;
        }
        StringRequest req = new StringRequest(Request.Method.PUT, mUrlAPIuser + "/alterarmorada"+
        "?idUser=" + morada.getIdUser() + "&pais=" + morada.getPais() + "&cidade=" + morada.getCidade() + "&rua=" + morada.getRua() + "&codpost=" + morada.getCodpost()
        , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                //editarUserBD(morada);
                //ativar o listener...
                /*if(livroListener != null)
                {
                    livroListener.onRefreshDetalhes(DetalhesLivroActivity.OP_CODE_EDITAR);
                }*/
        }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contexto, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("testeValidacao", error.getMessage());
                return;
            }
        });
        volleyQueue.add(req);
    }

    public String getValidacao(){
        return validacao;
    }

    /*
    public void setLivrosListener(LivrosListener livrosListener) {
        this.livrosListener = livrosListener;
                return;
            }
        });
        volleyQueue.add(request);
    }

    public void setUsersListener(UsersListener usersListener) {
        this.usersListener = usersListener;
    }

    public void setUserListener(UserListener userListener) {
        this.userListener = userListener;
    }*/
}