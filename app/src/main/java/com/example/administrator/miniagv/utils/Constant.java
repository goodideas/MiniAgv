package com.example.administrator.miniagv.utils;

/**
 * Created by Administrator
 * on 2016/7/8.
 */
public class Constant {

    public Constant(){}

    public static final String CMD_SEARCH_RESPOND = "0080";//搜索设备
    public static final String CMD_SHAKE_RESPOND = "0180";//握手指令
    public static final String CMD_QUERY_RESPOND = "0280";//查询状态
    public static final String CMD_UNLOCK_RESPOND = "0380";//急停或者解除急停
    public static final String CMD_MANUAL_RESPOND = "0480";//手动控制
    public static final String CMD_TRACK_RESPOND = "0580";//循迹行驶
    public static final String CMD_SETTING_LOC_RESPOND = "0680";//设置地标卡动作
    public static final String CMD_LIGHTING_RESPOND = "0780";//灯光
    public static final String CMD_BUZZER_RESPOND = "0880";//蜂鸣器
    public static final String CMD_STOP_LOC_RESPOND = "0980";//停在指定位置
    public static final String CMD_ERROR_STATU_RESPOND = "0181";//错误状态（自动返回指令）
    public static final String CMD_RFID_CAR_RESPOND = "0281";//监测到RFID卡（自动返回指令）
    public static final String CMD_STOP_FINISH_RESPOND = "0381";//停靠完成（自动返回指令）


    public static final int REMOTE_PORT = 5678;//远程端口

    public static final int SEARCH_WAIT_DIALOG_TIME = 5000;//搜索设备时间
    public static final int CONNECT_WAIT_DIALOG_MAX_TIME = 2000;//连接设备时间
    public static final int UNLOCK_WAIT_DIALOG_MAX_TIME = 2000;//解锁设备时间

    /**
     * FFAA  0000000000000000  0100  0100      00    00   FF55
     * 头    MAC               CMD   内容长度   内容  校验  尾
     */
    public static final int DATA_CMD_START = 20; //CMD开始位
    public static final int DATA_CMD_END = 24; //CMD结束位

    public static final int DATA_MAC_START = 4; //MAC开始位
    public static final int DATA_MAC_END = 20; //MAC结束位

    public static final int DATA_CONTENT_LENGTH_START_0 = 24; //内容长度低开始位
    public static final int DATA_CONTENT_LENGTH_END_0 = 26; //内容长度低结束位

    public static final int DATA_CONTENT_LENGTH_START_1 = 26; //内容长度高开始位
    public static final int DATA_CONTENT_LENGTH_END_1 = 28; //内容长度高结束位


    public static final int DATA_CONTENT_START = 28;//内容开始位

    /**
     * 内容结束位
     */
    public static int DATA_CONTENT_END(int dataLength) {
        return DATA_CONTENT_START + dataLength;
    }


    public static final String KEY_MAIN_TO_UNLOCK = "agvBean";//页面跳转 key
    public static final String INTENT_NAME = "finish";//页面传值 name
    public static final int AUTO_MODE_TO_FUNCTION_MENU_RESULT_CODE = 1;//resultCode;
    public static final int FUNCTION_REQUEST_CODE = 2;//request;
    public static final int INTENT_VALUE = 1;//resultCode;



//    public static final String SEND_DATA_SEARCH =           "FFAA 0000000000000000 0000 0000 00 FF55";//广播数据
//    public static final String SEND_DATA_SHAKE =            "FFAA 0000000000000000 0100 0000 00 FF55";//握手数据
//    public static final String SEND_DATA_QUERY =            "FFAA 0000000000000000 0200 0000 00 FF55";//查询数据
//    public static final String SEND_DATA_UNLOCK =           "FFAA 0000000000000000 0300 0100 00 00 FF55";//解除急停数据
//    public static final String SEND_DATA_LOCK =             "FFAA 0000000000000000 0300 0100 01 01 FF55";//急停数据
//    public static final String SEND_DATA_SPEED =            "FFAA 0000000000000000 0400 0200 0000 00 FF55";//手动模式速度数据
//    public static final String SEND_DATA_START_TRACK_S1 =   "FFAA 0000000000000000 0500 0200 0101 02 FF55";//开始循迹（速度1）数据
//    public static final String SEND_DATA_START_TRACK_S2 =   "FFAA 0000000000000000 0500 0200 0102 03 FF55";//开始循迹（速度2）数据
//    public static final String SEND_DATA_STOP_TRACK =       "FFAA 0000000000000000 0500 0200 0000 00 FF55";//停止循迹数据
//    public static final String SEND_DATA_SETTING_RFID =     "FFAA 0000000000000000 0600 0B00 0000000000000000000000 00 FF55";//设置地标卡数据
//    public static final String SEND_DATA_COLOR =             "FFAA 0000000000000000 0700 0400 00000000 00 FF55";//灯光数据
//    public static final String SEND_DATA_BUZZER =            "FFAA 0000000000000000 0800 0200 0000 00 FF55";//蜂鸣器数据
//    public static final String SEND_DATA_STOP_LOC =         "FFAA 0000000000000000 0900 0100 00 00 FF55";//停在指定位置数据




    public static final String SEND_DATA_SEARCH = "FFAA 0000000000000000 0000 0000 00 FF55";//广播数据


    public static String SEND_DATA_SHAKE(String agvMac){
        return "FFAA " + agvMac + " 0100 0000 00 FF55";
    }

    public static String SEND_DATA_QUERY(String agvMac){
        return "FFAA " + agvMac + " 0200 0000 00 FF55";//查询数据
    }
    public static String SEND_DATA_UNLOCK(String agvMac){
        return "FFAA " + agvMac + " 0300 0100 00 00 FF55";
    }

    public static String SEND_DATA_LOCK(String agvMac){
        return "FFAA " + agvMac + " 0300 0100 01 01 FF55";
    }
    public static String SEND_DATA_SPEED(String agvMac){
        return "FFAA " + agvMac + " 0400 0200 0000 00 FF55";
    }
    public static String SEND_DATA_START_TRACK_S1(String agvMac){
        return "FFAA " + agvMac + " 0500 0200 0101 02 FF55";
    }
    public static String SEND_DATA_START_TRACK_S2(String agvMac){
        return "FFAA " + agvMac + " 0500 0200 0102 03 FF55";
    }
    public static String SEND_DATA_STOP_TRACK(String agvMac){
        return "FFAA " + agvMac + " 0500 0200 0000 00 FF55";
    }
    public static String SEND_DATA_SETTING_RFID(String agvMac){
        return "FFAA " + agvMac + " 0600 0B00 0000000000000000000000 00 FF55";
    }
    public static String SEND_DATA_COLOR(String agvMac){
        return "FFAA " + agvMac + " 0700 0400 00000000 00 FF55";
    }
    public static String SEND_DATA_BUZZER(String agvMac){
        return "FFAA " + agvMac + " 0800 0200 0000 00 FF55";
    }
    public static String SEND_DATA_STOP_LOC(String agvMac){
        return "FFAA " + agvMac + " 0900 0100 00 00 FF55";
    }





//    FFAA 31A4FD34FE180000 0280 12000000000000000000000F00000000000000000FFF55





}
