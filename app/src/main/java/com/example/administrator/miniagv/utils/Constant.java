package com.example.administrator.miniagv.utils;

/**
 * Created by Administrator
 * on 2016/7/8.
 */
public class Constant {

    public static final String SEARCH_RESPOND = "8000";//搜索设备
    public static final String SHAKE_RESPOND = "8001";//握手指令
    public static final String QUERY_RESPOND = "8002";//查询状态
    public static final String STOP_RESPOND = "8003";//急停或者解除急停
    public static final String MANUAL_RESPOND = "8004";//手动控制
    public static final String TRACK_RESPOND = "8005";//循迹行驶
    public static final String SETTING_LOC_RESPOND = "8006";//设置地标卡动作
    public static final String LIGHTING_RESPOND = "8007";//灯光
    public static final String BUZZER_RESPOND = "8008";//蜂鸣器
    public static final String STOP_LOC_RESPOND = "8009";//停在指定位置
    public static final String ERROR_STATU_RESPOND = "8101";//错误状态（自动返回指令）
    public static final String RFID_CAR_RESPOND = "8102";//监测到RFID卡（自动返回指令）
    public static final String STOP_FINISH_RESPOND = "8103";//停靠完成（自动返回指令）



    public static final String KEY_MAIN_TO_UNLOCK = "agvBean";//页面跳转 key




}
