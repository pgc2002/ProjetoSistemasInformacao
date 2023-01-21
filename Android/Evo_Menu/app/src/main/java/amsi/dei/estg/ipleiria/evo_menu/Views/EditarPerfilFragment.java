package amsi.dei.estg.ipleiria.evo_menu.Views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import amsi.dei.estg.ipleiria.evo_menu.Model.Morada;
import amsi.dei.estg.ipleiria.evo_menu.Model.SingletonGestorUsers;
import amsi.dei.estg.ipleiria.evo_menu.Model.User;
import amsi.dei.estg.ipleiria.evo_menu.R;

public class EditarPerfilFragment extends Fragment
{
    public EditarPerfilFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_perfil, container, false);

        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etPassword = view.findViewById(R.id.etPassword);
        EditText etNome = view.findViewById(R.id.etNome);
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etTelemovel = view.findViewById(R.id.etTelemovel);
        EditText etNif = view.findViewById(R.id.etNif);

        EditText etRua = view.findViewById(R.id.etRua);
        EditText etCodPostal = view.findViewById(R.id.etCodPost);
        EditText etPais = view.findViewById(R.id.etPais);
        EditText etCidade = view.findViewById(R.id.etCidade);

        Button btn = view.findViewById(R.id.btConcluirEdicao);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(20, etUsername.getText().toString(), etPassword.getText().toString(), etNome.getText().toString(), etEmail.getText().toString(), etTelemovel.getText().toString(), etNif.getText().toString());
                Morada morada = new Morada(1, 20, etPais.getText().toString(), etCidade.getText().toString(), etRua.getText().toString(), etCodPostal.getText().toString());

                SingletonGestorUsers.getInstance(getContext()).editarUserAPI(user, getContext());
                SingletonGestorUsers.getInstance(getContext()).editarMoradaAPI(morada, getContext());


                Fragment verPerfil = new VerPerfilFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentFragment, verPerfil);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}