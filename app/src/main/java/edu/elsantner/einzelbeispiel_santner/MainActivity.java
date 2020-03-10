package edu.elsantner.einzelbeispiel_santner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity  {

    private Button btnSend, btnCalc;
    private TextView lblOutput;
    private EditText txtMNr;
    private TCPClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViews();
        setOnClickListeners();
        setupTCPClient();
    }

    private void setupTCPClient() {
        client = new TCPClient();
    }

    private void setOnClickListeners() {
        setupSendClickListener();
        setupCalculationListener();
    }

    private void setupCalculationListener() {
        this.btnCalc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                lblOutput.setText(String.format(getResources().getString(R.string.calc_description),
                        CalculationHelper.sortAndFilterPrimes(txtMNr.getText().toString())));
            }
        });
    }

    private void setupSendClickListener() {
        this.btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    client.send(txtMNr.getText().toString())
                            .subscribeOn(Schedulers.io())               // execute "send()" in thread of unbounded thread pool (Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())  // execute "subscribe()" + methods of Observer on mainThread
                            .subscribe(new Observer<String>() {         // For RxJava see: https://proandroiddev.com/understanding-rxjava-subscribeon-and-observeon-744b0c6a41ea
                                @Override
                                public void onSubscribe(Disposable d) {

                                }
                                @Override
                                public void onNext(String msg) {
                                    Log.d("MainActivity", "Response: " + msg);
                                    lblOutput.setText(msg);
                                }
                                @Override
                                public void onError(Throwable e) {
                                    Log.e("MainActivity", "Error sending message: ", e);
                                    lblOutput.setText(String.format(getResources().getString(R.string.error_placeholder), e.getMessage()));
                                }
                                @Override
                                public void onComplete() {

                                }
                            });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getViews() {
        this.btnSend = findViewById(R.id.btnSend);
        this.btnCalc = findViewById(R.id.btnCalc);
        this.lblOutput = findViewById(R.id.lblOutput);
        this.txtMNr = findViewById(R.id.editTextNumber);
    }
}
