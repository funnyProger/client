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
import java.util.ArrayList;

public class client extends AppCompatActivity {

    EditText tb_message;
    Button btn_send;
    TextView tb_data;
    DatagramSocket socket;
    String strSendDatagram, strGetDatagram; //перменнты которые будут хранить запросы и ответы

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            socket = new DatagramSocket(1234);
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        init();
    }

    private void init() {
        btn_send = findViewById(R.id.btn_send);
        tb_data = findViewById(R.id.tb_data);
        tb_message = findViewById(R.id.tb_message);


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        strSendDatagram = tb_message.getText().toString();
                        sendMsg(strSendDatagram);
                        tb_message.setText("");
                        try {
                            receiveMsg();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tb_data.setText(strGetDatagram);
                    }
                });
            }
        });

    }


    public void sendMsg(String strSendDatagram) {
        try {
            socket = new DatagramSocket();
            InetAddress server = InetAddress.getByName("192.168.56.1");
            byte[] data = strSendDatagram.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, server, 5040);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveMsg() throws IOException {
        byte[] data = new byte[1024];
        DatagramPacket packet2 = new DatagramPacket(data, data.length);
        socket.receive(packet2);
        strGetDatagram = new String(packet2.getData(), 0, packet2.getLength());
        System.out.println(strGetDatagram);
    }
}