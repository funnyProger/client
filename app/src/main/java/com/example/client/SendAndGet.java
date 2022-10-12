package com.example.client;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendAndGet {

    CheckBox chb_ServerIP, chb_ServerPORT;
    String checkBoxData = "";
    String s = "/";

    public SendAndGet(CheckBox chb_ServerIP, CheckBox chb_ServerPORT) {
        this.chb_ServerIP = chb_ServerIP;
        this.chb_ServerPORT = chb_ServerPORT;
    }

    public void sendMessage(EditText tb_serverIP, EditText tb_serverPORT, DatagramSocket socket, Context context, String str) {
        if (tb_serverIP.getText().toString().equals("") | tb_serverPORT.getText().toString().equals("")) {
            Toast.makeText(context, "Введите все данные!", Toast.LENGTH_LONG).show();
        } else {
            try {
                byte[] bytes = str.getBytes();
                InetAddress address = InetAddress.getByName(tb_serverIP.getText().toString());
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, Integer.parseInt(tb_serverPORT.getText().toString()));
                socket.send(packet);

                System.out.println("Данные отправлены :]");
            } catch (Exception e) {
                Toast.makeText(context, "Ошибка отправления данных :(", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    public void getMessage(EditText tb_serverIP, EditText tb_serverPORT, DatagramSocket socket,TextView tb_serverData, Context context){
        byte[] bytes1 = new byte[1024];
        DatagramPacket packet1 = new DatagramPacket(bytes1, bytes1.length);
        try {
            socket.receive(packet1);
            String str1 = new String(packet1.getData(), 0, packet1.getLength());
            if(str1.equals("1")) {
                sendMessage(tb_serverIP, tb_serverPORT, socket, context, str1);
            } else {
                tb_serverData.setText(str1);
                //прием конечных данных

            }
        } catch (Exception e) {
            Toast.makeText(context, "Рассылка не получена :(", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public String getCheckBoxData() {
        if(chb_ServerIP.isChecked()) checkBoxData = checkBoxData + "AGE" + s;
        else checkBoxData = checkBoxData + "";
        return checkBoxData;
    }



}
