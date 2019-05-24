package ro.pub.cs.systems.eim.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {
    Button startServer;
    Button sendCommand;

    EditText serverPort;
    EditText clientCommand;
    EditText myTextView;

    ServerThread serverThread;
    ClientThread clientThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        startServer = findViewById(R.id.startServer);
        sendCommand = findViewById(R.id.sendClient);

        serverPort = findViewById(R.id.portServer);
        clientCommand = findViewById(R.id.commandClient);
        myTextView = findViewById(R.id.mytext);

        startServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String port = serverPort.getText().toString();
                Log.v("connect", port);
                serverThread = new ServerThread(Integer.parseInt(port));
                serverThread.start();
            }
        });

        sendCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = clientCommand.getText().toString();
                String port = serverPort.getText().toString();

                clientThread = new ClientThread(command, Integer.parseInt(port), myTextView);
                clientThread.start();
            }
        });

        myTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Toast.makeText(PracticalTest02MainActivity.this, myTextView.getText().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
