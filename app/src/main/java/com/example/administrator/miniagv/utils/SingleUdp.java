package com.example.administrator.miniagv.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by Administrator
 * on 2016/7/5.
 */
public class SingleUdp {

    private static final String TAG = "SingleUdp";
    public static final int ONE_KB = 1024;
    public static final int HALF_KB = 512;
    public static final int RECEIVED_DATA = 0x123;
    private String ipAddress;
    private int udpLocalPort = -1;
    private int udpRemotePort = -1;
    private DatagramSocket udpSocket;
    private DatagramPacket udpReceivePacket;
    private DatagramPacket udpSendPacket;
    private InetAddress inetAddress;
    private static byte[] udpReceiveBytes;
    private OnReceiveListen onReceiveListen;//接收监听
    private Thread udpReceiveThread;
    private static SingleUdp UdpInstance;
    private Myhandler myhandler;
    //私有构造器
    private SingleUdp() {
        init();
    }

    //单例
    public static synchronized SingleUdp getUdpInstance() {
        if (UdpInstance == null) {
            UdpInstance = new SingleUdp();
        }
        return UdpInstance;
    }

    //设置监听
    public void setOnReceiveListen(OnReceiveListen receiveListen) {
        this.onReceiveListen = receiveListen;
    }

    //设置Udp的IP
    public void setUdpIp(String ip) {
        this.ipAddress = ip;
    }

    //设置Udp的本地端口
    public void setUdpLocalPort(int port) {
        this.udpLocalPort = port;
    }

    //设置Udp的远程端口
    public void setUdpRemotePort(int port) {
        this.udpRemotePort = port;
    }

    //初始化
    private void init() {
        udpReceiveBytes = new byte[HALF_KB];
        udpReceivePacket = new DatagramPacket(udpReceiveBytes, HALF_KB);
        myhandler = new Myhandler();
    }

    //启动udp
    public void start() {

        try {
            inetAddress = InetAddress.getByName(ipAddress);
        //    if (udpLocalPort != -1) {
        //        udpSocket = new DatagramSocket(udpLocalPort);
       //     } else {
                udpSocket = udpSocket == null?new DatagramSocket():udpSocket;
      //      }
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }

        receiveUdp();
    }

    //关闭udp
    public void stop() {
        udpReceiveThread.interrupt();
        if (udpSocket != null) {
            udpSocket.close();
        }
        UdpInstance = null;
    }

    //发送
    public void send(byte[] data){

        Log.e(TAG,"发送的数据="+Util.bytes2HexString(data,data.length));
        if(udpSendPacket==null){
            udpSendPacket = new DatagramPacket(data, data.length, inetAddress, udpRemotePort);
        }else{
            udpSendPacket.setData(data);
            udpSendPacket.setLength(data.length);
        }


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if (udpSocket != null) {
//                        udpSocket.send(udpSendPacket);
//
//                        Log.e(TAG, "udp发送成功！");
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e(TAG, "udp发送失败！");
//                }
//            }
//        }, 30);

        new Thread() {
            @Override
            public void run() {
                try {
                    if (udpSocket != null) {
                        udpSocket.send(udpSendPacket);
                        Log.e(TAG, "ip=" + udpSendPacket.getAddress().toString());
                        Log.e(TAG, "udp发送成功！");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "udp发送失败！"+e.toString());
                }
            }
        }.start();
    }



    //接收线程
    private void receiveUdp() {

        udpReceiveThread = new Thread() {
            public void run() {
                while (!udpReceiveThread.isInterrupted()) {
                    try {
                        //初始化赋值 (byte)0x00

                        //会阻塞
                        udpSocket.receive(udpReceivePacket);
                        int len = udpReceivePacket.getLength();
                        if (len > 0) {
                            if (onReceiveListen != null) {
                                onReceiveListen.onReceiveData(udpReceiveBytes,len,null);
                                myhandler.sendEmptyMessage(RECEIVED_DATA);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        udpReceiveThread.start();

    }


    static class Myhandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == RECEIVED_DATA){
                Arrays.fill(udpReceiveBytes, (byte) 0x00);
            }
        }
    }


}




//FFAA58C8CF34FE18000000800200030003FF55 广播
//192.168.0.158
//FFAA58C8CF34FE1800000380000000FF55 解锁

