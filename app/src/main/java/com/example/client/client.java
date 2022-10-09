package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class client extends AppCompatActivity {

    EditText tb_message;
    Button btn_send;
    EditText tb_serverData;
    DatagramSocket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            socket = new DatagramSocket(1234);
            System.out.println("Сокет открыт :]");
        }
        catch (SocketException e) {
            System.out.println("Сокет не открыт :(");
            e.printStackTrace();
        }
        init();
    }

    private void init(){
        btn_send = findViewById(R.id.btn_send);
        tb_serverData = findViewById(R.id.tb_serverData);
        tb_message = findViewById(R.id.tb_message);
    }

    public void onClickSend(View view) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                String str = tb_message.getText().toString();
                byte[] bytes = str.getBytes();
                InetAddress address = InetAddress.getByName("192.168.56.1");
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, 5040);
                socket.send(packet);
                System.out.println("Данные отправлены :]");
                tb_message.setText("");

                byte[] bytes1 = new byte[1024];
                DatagramPacket packet1 = new DatagramPacket(bytes1, bytes.length);
                socket.receive(packet1);
                System.out.println("Данные получены");
                String str1 = new String(packet1.getData(), 0, packet1.getLength());
                tb_serverData.setText(str1);
                socket.close();

                } catch (IOException e) {
                    System.out.println("Ошибка получения или отпрвления данных :(");
                    e.printStackTrace();
                }
            }
        }).start();

    }
}