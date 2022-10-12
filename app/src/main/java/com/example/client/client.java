package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramSocket;

public class client extends AppCompatActivity {

    EditText tb_serverIP, tb_serverPORT, tb_clientPORT;
    DatagramSocket socket;
    SendAndGet sendAndGet;
    CheckBox chb_age, chb_ServerPORT;
    TextView tb_serverData;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        try {
            connectToServer();
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка подключения к серверу", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void init(){
        tb_serverIP = findViewById(R.id.tb_IP);
        tb_serverPORT = findViewById(R.id.tb_serverPORT);
        tb_clientPORT = findViewById(R.id.tb_clientPORT);
        chb_age = findViewById(R.id.chb_age);
        context = tb_serverIP.getContext();
        tb_serverData = findViewById(R.id.tb_Serverdata);
        sendAndGet = new SendAndGet(chb_age, chb_ServerPORT);
    }


    public void connectToServer() throws Exception{
        if(tb_clientPORT.getText().toString().equals("")) Toast.makeText(this, "Введите порт сокета клиента!", Toast.LENGTH_LONG).show();
        else {
            socket = new DatagramSocket(Integer.parseInt(tb_clientPORT.getText().toString()));
            System.out.println("Сокет открыт :]");
            boolean TF = true;
            while (TF) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String str = sendAndGet.getCheckBoxData();
                        if(str.equals("")) {
                            try {
                                connectToServer();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            sendAndGet.sendMessage(tb_serverIP, tb_serverPORT, socket, context, str);
                            sendAndGet.getMessage(tb_serverIP, tb_serverPORT, socket, tb_serverData, context);
                        }
                    }
                }).start();
            }
        }
    }

    /*

   public void sendMessage() {
        if (tb_serverIP.getText().toString().equals("") | tb_serverPORT.getText().toString().equals("")) {
            Toast.makeText(this, "Введите все данные!", Toast.LENGTH_LONG).show();
        } else {
            try {


            byte[] bytes = str.getBytes();
            InetAddress address = InetAddress.getByName(tb_serverIP.getText().toString());
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, Integer.parseInt(tb_serverPORT.getText().toString()));

                socket.send(packet);
                System.out.println("Данные отправлены :]");
            } catch (Exception e) {
                Toast.makeText(this, "Ошибка отправления данных :(", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

    }

    public void getMessage(){
        byte[] bytes1 = new byte[2048];
        DatagramPacket packet1 = new DatagramPacket(bytes1, bytes1.length);
        try {
            socket.receive(packet1);
            System.out.println("Данные получены");
            String str1 = new String(packet1.getData(), 0, packet1.getLength());
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка получения данных :(", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

     */
}