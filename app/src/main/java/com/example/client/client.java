package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class client extends AppCompatActivity {

    EditText tb_serverIP, tb_serverPORT, tb_clientPORT;
    CheckBox check_age, check_date, check_time, check_os;
    Button btn_getData;
    DatagramSocket socket;
    Handler handler;

    String checkBoxData = "";
    boolean indicator;
    ArrayList<String> arrayList = new ArrayList<>();

    ListView listView;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }
    private void init(){
        tb_serverIP = findViewById(R.id.tb_IP);
        tb_serverPORT = findViewById(R.id.tb_serverPORT);
        tb_clientPORT = findViewById(R.id.tb_clientPORT);

        check_age = findViewById(R.id.check_age);
        check_date = findViewById(R.id.check_date);
        check_time = findViewById(R.id.check_time);
        check_os = findViewById(R.id.check_os);

        btn_getData = findViewById(R.id.btn_getData);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView = findViewById(R.id.id_listView);
        listView.setAdapter(adapter);
        handler = new Handler(Looper.getMainLooper());




    }

    public void onClickSendAndGet(View view) {
        indicator = true;

        if (tb_clientPORT.getText().toString().equals(""))
            Toast.makeText(this, "Введите порт сокета клиента!", Toast.LENGTH_LONG).show();
        else {
            try {
                socket = new DatagramSocket(Integer.parseInt(tb_clientPORT.getText().toString()));
                System.out.println("Сокет открыт :]");
            } catch (Exception e) {
                System.out.println("Сокет не открыт :(");
                e.printStackTrace();
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendMessage(socket);
                } catch (Exception e) {
                    System.out.println("Ошибка отправления данных!");
                    e.printStackTrace();
                }

                while (indicator) {
                    try {
                        getMessage(socket, handler);
                    } catch (Exception e) {
                        System.out.println("Ошибка получения данных!");
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }


    public void onClickStop(View view) {
        indicator = false;
    }



   public void sendMessage(DatagramSocket socket) throws Exception{
       if (tb_serverIP.getText().toString().equals("") | tb_serverPORT.getText().toString().equals("")) {
           Toast.makeText(this, "Введите все данные!", Toast.LENGTH_LONG).show();
       } else {
           String str = getCheckBoxData();
           if (!str.equals("")) {
               str = str + "YES"; // YES - индикатор подписки на получение данных
               byte[] bytes = str.getBytes();
               InetAddress address = InetAddress.getByName(tb_serverIP.getText().toString());
               DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, Integer.parseInt(tb_serverPORT.getText().toString()));
               socket.send(packet);
               System.out.println("Данные отправлены :]");
           } else {
               Toast.makeText(this, "Выберите нужные вам данные :]", Toast.LENGTH_LONG).show();
           }

       }
   }


    public void getMessage(DatagramSocket socket, Handler handler) throws Exception {
        byte[] bytes1 = new byte[1024];
        DatagramPacket packet = new DatagramPacket(bytes1, bytes1.length);
        socket.receive(packet);
        System.out.println("Данные получены");
        String str = new String(packet.getData(), 0, packet.getLength());
        System.out.println(str);
        if (str.equals("Живой")) {
            String strDa = "ДА";
            byte[] bytes = strDa.getBytes();
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            DatagramPacket packet1 = new DatagramPacket(bytes, bytes.length, address, port);
            socket.send(packet1);
            System.out.println("ДА");
        } else {
            arrayList.clear();
            handler.post(() -> {
                String[] s = str.split("/");
                for(int d = 0; d < s.length; d++) {
                    if(!s[d].equals("")) {
                        arrayList.add(s[d]);
                    }
                }
                adapter.notifyDataSetChanged();
            });
        }
    }

    public String getCheckBoxData() {
        checkBoxData = "";
        String s = "/";
        if(check_age.isChecked()) checkBoxData = checkBoxData + "AGE" + s;
        if(check_date.isChecked()) checkBoxData = checkBoxData + "DATE" + s;
        if(check_time.isChecked()) checkBoxData = checkBoxData + "TIME" + s;
        if(check_os.isChecked()) checkBoxData = checkBoxData + "OS" + s;



        return checkBoxData;


    }


}