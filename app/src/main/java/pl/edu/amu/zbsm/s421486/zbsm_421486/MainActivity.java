package pl.edu.amu.zbsm.s421486.zbsm_421486;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.edu.amu.zbsm.s421486.zbsm_421486.services.DataService;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.button_login)
    Button buttonLogin;

    @BindView(R.id.password)
    TextView textViewPassword;

    DataService dataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dataService = new DataService(this);
        dataService.Init();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
       // showMessage("Hasło psie!");
        this.finishAffinity();
    }

    @OnClick(R.id.button_login)
    public void buttonOnClick(View v) {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        String savedKey = dataService.loadSettings(DataService.KEY);
        String key = dataService.GetMd5Hash(textViewPassword.getText().toString());
        if( savedKey == null || savedKey.equals(key) )
        {
            DataService.PlainPassword = textViewPassword.getText().toString();
            finish();
        }
        else if( key.isEmpty())
        {
            showMessage("Haslo psie!");
        }
        else
        {
            showMessage("Błedne hasło");
        }
    }

    private void showMessage(String message){
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
