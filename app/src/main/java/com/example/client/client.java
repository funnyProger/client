package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class client extends AppCompatActivity {

    EditText tb_serverData, tb_IP, tb_serverPORT, tb_clientPORT, tb_message;
    Button btn_send;
    DatagramSocket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        tb_serverData = findViewById(R.id.tb_serverData);
        tb_IP = findViewById(R.id.tb_IP);
        tb_serverPORT = findViewById(R.id.tb_serverPORT);
        tb_clientPORT = findViewById(R.id.tb_clientPORT);
        tb_message = findViewById(R.id.tb_message);
        btn_send = findViewById(R.id.btn_send);
    }

    public void onClickSend(View view) throws Exception {
        if(tb_clientPORT.getText().toString().equals("")) Toast.makeText(this, "Введите порт сокета клиента!", Toast.LENGTH_LONG).show();
        else {
            try {
                socket = new DatagramSocket(Integer.parseInt(tb_clientPORT.getText().toString()));
                System.out.println("Сокет открыт :]");
            } catch (Exception e) {
                System.out.println("Сокет не открыт :(");
                e.printStackTrace();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sendMessage(view);
                    } catch (IOException e) {
                        System.out.println("Ошибка отпрвления данных :(");
                        e.printStackTrace();
                    }
                    try {
                        getMessage();
                    } catch (IOException e) {
                        System.out.println("Ошибка получения данных :(");
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    public void sendMessage(View view) throws IOException {
        if (tb_message.getText().toString().equals("") | tb_IP.getText().toString().equals("") | tb_serverPORT.getText().toString().equals("")) {
            Snackbar.make(view, "Введите все данные :]", Snackbar.LENGTH_SHORT).show();
        } else {
            String str = tb_message.getText().toString();
            byte[] bytes = str.getBytes();
            InetAddress address = InetAddress.getByName(tb_IP.getText().toString());
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, Integer.parseInt(tb_serverPORT.getText().toString()));
            socket.send(packet);
            System.out.println("Данные отправлены :]");
        }

    }

    public void getMessage() throws IOException {
        byte[] bytes1 = new byte[2048];
        DatagramPacket packet1 = new DatagramPacket(bytes1, bytes1.length);
        socket.receive(packet1);
        System.out.println("Данные получены");
        String str1 = new String(packet1.getData(), 0, packet1.getLength());
        if(!tb_serverData.getText().toString().equals(""))
        {
            tb_serverData.setText("");
            tb_serverData.setText(str1);
            socket.close();
        } else {
            tb_serverData.setText(str1);
            socket.close();
        }
    }


    public void onClickClear(View view) {
        switch (view.getId()) {
            case R.id.btn_clearIP: {
                if(tb_IP.getText().toString().equals("")) tb_IP.setText("");
            }
            case R.id.btn_clearServerPORT: {
                if(tb_serverPORT.getText().toString().equals("")) tb_serverPORT.setText("");
            }
            case R.id.btn_clearMessage: {
                if(tb_message.getText().toString().equals("")) tb_message.setText("");
            }
            case R.id.btn_clearClientPORT: {
                if(tb_clientPORT.getText().toString().equals("")) tb_clientPORT.setText("");
            }
        }
    }

}