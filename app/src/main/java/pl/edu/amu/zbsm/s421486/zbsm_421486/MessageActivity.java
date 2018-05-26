package pl.edu.amu.zbsm.s421486.zbsm_421486;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.edu.amu.zbsm.s421486.zbsm_421486.services.DataService;

public class MessageActivity extends AppCompatActivity {

    DataService dataService;

    @BindView(R.id.editText2)
    TextView textViewNote;

    @BindView(R.id.password)
    TextView textViewPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        dataService = new DataService(this);
        ButterKnife.bind(this);

        String savedKey = dataService.loadSettings(DataService.KEY);
        if (savedKey != null && !savedKey.isEmpty()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            loadData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }

    private void loadData() {
        String text = dataService.GetCipher(dataService.loadSettings(DataService.TEXT));
        if (text != null) {
            textViewNote.setText(text);
        }
    }

    @OnClick(R.id.button_save)
    public void saveDataClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        try {

            dataService.SetCihperSkip( textViewPass.getText().toString().length() );
            dataService.saveSettings(DataService.TEXT, dataService.MakeCipher( textViewNote.getText().toString()));

            if (!textViewPass.getText().toString().isEmpty())
            {
                dataService.saveSettings(DataService.KEY, dataService.GetMd5Hash( textViewPass.getText().toString()));
                showMessage("Zapisano dane");
            }
            else
            {
                dataService.saveSettings(DataService.KEY, textViewPass.getText().toString());
                showMessage("Nie ustawiles hasla. Dane zostaly zapisane.");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
