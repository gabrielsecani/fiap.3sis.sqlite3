package solutions.plural.sqlite.sqliteapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import solutions.plural.sqlite.sqliteapp.sqlite.Login;
import solutions.plural.sqlite.sqliteapp.sqlite.LoginDAO;

public class MainActivity extends AppCompatActivity {

    private TextView edtUsuario;
    private TextView edtSenha;
    private CheckBox chkConectado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtUsuario = (TextView) findViewById(R.id.edtUsuario);
        edtSenha = (TextView) findViewById(R.id.edtSenha);
        chkConectado = (CheckBox) findViewById(R.id.chkConectado);
    }

    public void logar(View v) {
        Login login = new Login();
        login.setUsuario(edtUsuario.getText().toString());
        login.setSenha(edtSenha.getText().toString());

        LoginDAO dao = new LoginDAO(this);

        if (dao.existsLogin(login)) {
            Toast.makeText(this, "Login realizado", Toast.LENGTH_LONG).show();
        } else {
            dao.insert(login);
            Toast.makeText(this, "Login cadastrado", Toast.LENGTH_LONG).show();
        }

    }
}