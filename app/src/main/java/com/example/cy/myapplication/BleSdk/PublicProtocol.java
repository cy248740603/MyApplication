package com.example.cy.myapplication.BleSdk;

import android.os.SystemClock;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by CY on 2017/11/2.
 */

public class PublicProtocol {

    private final static String TAG = PublicProtocol.class.getSimpleName();

    private final static String ADDRESS = "01";

    public final static int SOI = 0x7E;
    public final static int EOI = 0x0D;
    public final static String VER = "20";
    public final static String CID1 = "D0";
    public final static int SYNTHESIZE_DATAFLAG = 0;
    public final static int SYNTHESIZE_STATUS   = 0;
    public final static int SYNTHESIZE_ANALOG_SIZE = 150 - 2;
    public final static int SYNTHESIZE_SWITCH_SIZE = 190 - 151;
    public final static String SYNTHESIZE_FLOAT_ZREO  = "00000000";
    public final static String SYNTHESIZE_SWITCH_ZREO = "00";
    public final static String SYNTHESIZE_SWITCH_ONE  = "01";
    //开门 指令 CID2
    public final static String SET_OPEN_DOOR = "92";

    private String mInfoData;

    /**
     * String求和
     */
    public static int stringSum(String str) {
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            sum += Integer.parseInt(str.substring(i, i + 1), 16);
        }
        return sum;
    }

    /**
     * String求ASCII和
     */
    public static int stringToASCIISum(String str) {
        int sum = 0;
        byte[] bs = str.getBytes();
        for (byte mByte : bs) {
            sum += mByte;
        }
        return sum;
    }
    public static byte[] packagePublicProtocol(PublicDataBean mPublicDataBean){

        String mLength = String.format("%03X", mPublicDataBean.getInfo().length());
        int mLCheckSum = ~(stringSum(mLength) % 16) + 1;
        mLength = Integer.toHexString(mLCheckSum & 0xF) + mLength;
        String mCommandData = VER + mPublicDataBean.getAddress() + CID1 + mPublicDataBean.getRTN() + mLength + mPublicDataBean.getInfo();
        return packageData(mCommandData);
    }

    private static byte[] packageData(String data) {
        /*CHKSUM的计算是除SOI、EOI和CHKSUM外，其他字符按ASCII码值累加求和，所得结果模65536余数取反加1*/
        int mCheckSum = ~(stringToASCIISum(data)) + 1;
        String mCheckSumString = data + String.format("%04X", mCheckSum & 0xFFFF);
        byte[] mBytes = new byte[mCheckSumString.length() + 2];
        mBytes[0] = SOI;
        mBytes[mBytes.length - 1] = EOI;
        System.arraycopy(mCheckSumString.getBytes(), 0, mBytes, 1, mCheckSumString.getBytes().length);
        return mBytes;
    }

    public static PublicDataBean setOpenDoorBean(String id){
        PublicDataBean dataBean = new PublicDataBean();
        dataBean.setAddress(ADDRESS);
        dataBean.setCommandID2(SET_OPEN_DOOR);
        dataBean.setRTN(SET_OPEN_DOOR);
        dataBean.setInfo(setOpenDoorInfo(id));
        return dataBean;
    }

    private static String setOpenDoorInfo(String id){
        StringBuilder sb = new StringBuilder();
        sb.append("45");
        sb.append(id);
        sb.append("0015");
        sb.append("42");
        sb.append("01");
        return sb.toString();
    }

    static Process createSuProcess() throws IOException  {
        File rootUser = new File("/system/xbin/ru");
        if(rootUser.exists()) {
            return Runtime.getRuntime().exec(rootUser.getAbsolutePath());
        } else {
            return Runtime.getRuntime().exec("su");
        }
    }
    static Process createSuProcess(String cmd) throws IOException {

        DataOutputStream os = null;
        Process process = createSuProcess();

        try {
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit $?\n");
        } finally {
            if(os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }

        return process;
    }

    static void requestPermission() throws InterruptedException, IOException {
        createSuProcess("chmod 666 /dev/alarm").waitFor();
    }

    private static String getSystemTime(){
        Calendar time = Calendar.getInstance();
        time.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return "0" + Integer.toHexString(time.get(Calendar.YEAR)) + intToHexString(time.get(Calendar.MONTH) + 1) + intToHexString(time.get(Calendar.DAY_OF_MONTH )) +
                intToHexString(time.get(Calendar.DAY_OF_WEEK)) +intToHexString(time.get(Calendar.HOUR_OF_DAY)) +intToHexString(time.get(Calendar.MINUTE)) +
                intToHexString(time.get(Calendar.SECOND));
    }

    private static void setSystemTime(String info)throws IOException, InterruptedException{
        Calendar time = Calendar.getInstance();
        requestPermission();
        time.set(Calendar.YEAR,Integer.parseInt(info.substring(0,4),16)) ;
        time.set(Calendar.MONTH,Integer.parseInt(info.substring(4,6),16) - 1);
        time.set(Calendar.DAY_OF_MONTH,Integer.parseInt(info.substring(6,8),16));
       // time.set(Calendar.DAY_OF_WEEK,Integer.parseInt(info.substring(8,10),16));
        time.set(Calendar.HOUR_OF_DAY,Integer.parseInt(info.substring(10,12),16));
        time.set(Calendar.MINUTE,Integer.parseInt(info.substring(12,14),16));
        time.set(Calendar.SECOND,Integer.parseInt(info.substring(14,16),16));

        long when = time.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE) {
            SystemClock.setCurrentTimeMillis(when);
        }
    }

    //实例化SharedPreferences对象（第一步）
//    private static SharedPreferences sharedPreferences = AccumulatorApplication.getInstance().getSharedPreferences("Address", Context.MODE_PRIVATE);

//    private static void setAddress(String ADDRESS) {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("Address", ADDRESS);
//        editor.commit();
//    }

//    private static String getAddress(){
//        return sharedPreferences.getString("Address",ADDRESS);
//    }

    /**
     * 解析处理上位机返回的数据
     * @param mPublicDataBean
     * @throws Exception
     */
//    private static void dealCommandCallBack(PublicDataBean mPublicDataBean) throws Exception {
////        Log.e(TAG,"CID2:" + mPublicDataBean.getCommandID2());
//        switch (mPublicDataBean.getCommandID2()) {
//            case GET_METHOD_SYNTHESIZE:
//                String synthesize = new String(packageSynthesizeData());
//                mPublicDataBean.setInfo(synthesize);
//                break;
////            case GET_METHOD_INFORMATION:
////                mPublicDataBean.getInfo();
////                break;
//            case GET_METHOD_ADDRESS:
//                mPublicDataBean.setAddress(getAddress());
//                break;
//            case SET_METHOD_ADDRESS:
//                setAddress(mPublicDataBean.getInfo());
//                break;
//            case GET_METHOD_TIME:
//                mPublicDataBean.setInfo(getSystemTime());
//                break;
//            case SET_METHOD_TIME:
//                setSystemTime(mPublicDataBean.getInfo());
//                mPublicDataBean.setInfo("");
//                break;
//            case SET_METHOD_CONTROL:
//                Log.e(TAG,"CID2:" + mPublicDataBean.getCommandID2());
//                dealControlInfo(mPublicDataBean.getInfo());
//                mPublicDataBean.setInfo("");
//                break;
//            case GET_METHOD_CONFIG:
//                if (CONFIG_NUM.equals(mPublicDataBean.getInfo().substring(0,2)))
//                    mPublicDataBean.setInfo(ConfigurationParameterBean.packageConfiguration());
//                else{
//                    mPublicDataBean.setInfo("");
//                    mPublicDataBean.setRTN("0f");
//                }
//                break;
//            case SET_METHOD_CONFIG:
//                Log.e(TAG,"CID2:" + mPublicDataBean.getCommandID2());
//                ConfigurationParameterBean.configurationParameterToLocalConfig(
//                        ConfigurationParameterBean.infoToConfigurationParameter(mPublicDataBean.getInfo()));
//                mPublicDataBean.setInfo("");
//                break;
//            default:
//                Log.e(TAG,"unknown cid2");
//                mPublicDataBean.setRTN("04");
//                mPublicDataBean.setInfo("");
//                break;
//        }
//        ControllerLordDataSource.getInstance().setPublicSendData(mPublicDataBean); //设置应答数据
//    }

    /**
     * 　RTN的定义
     * 00H	正常
     * 01H	VER错
     * 02H	CHKSUM错
     * 03H	LCHKSUM错
     * 04H	CID2无效
     * 05H	命令格式错
     * 06H	无效数据
     * 80H	红外学习成功
     * 81H	红外学习失败
     * 82H  红外指令长度过长无法学习(自定义)
     */
//    public static PublicDataBean analysePackage(String data) throws Exception {
//        Log.w(TAG, data);
//        int mCheckSum = ~(CommunicationDataConvert.stringToASCIISum(data.substring(0, data.length() - 4)) % 65536) + 1;
//        String mCheckSumString = Integer.toHexString(mCheckSum & 0xFFFF);
//        String checkSum = data.substring(data.length() - 4, data.length());
//        if (checkSum.toUpperCase().equals(mCheckSumString.toUpperCase())) {
//            String version = data.substring(0, 2);
//            String address = data.substring(2, 4);
//            String commandID1 = data.substring(4, 6);
//            String commandID2 = data.substring(6, 8);
//            String length = data.substring(8, 12);
//            String info = data.substring(12, data.length() - 4);
//            String RTN = "00";
//            PublicDataBean mPublicDataBean = new PublicDataBean
//                    (version, address, commandID1, commandID2, length, info, checkSum, RTN);
//
//            int mLCheckSum = ~(CommunicationDataConvert.stringSum(length.substring(1, 4)) % 16) + 1;
//            String mLCheckSumString = Integer.toHexString(mLCheckSum & 0xF);
//            if (length.substring(0, 1).toUpperCase().equals(mLCheckSumString.toUpperCase())
//                    && Integer.parseInt(length.substring(1, 4), 16) == info.length()) {
//                if(getAddress().equals(mPublicDataBean.getAddress()) || GET_METHOD_ADDRESS.equals(mPublicDataBean.getCommandID2()))
//                    dealCommandCallBack(mPublicDataBean);
//                else {
//                    mPublicDataBean.setRTN("01");//地址错误
//                    mPublicDataBean.setInfo("");
//                    ControllerLordDataSource.getInstance().setPublicSendData(mPublicDataBean); //设置应答数据
//                }
//                return mPublicDataBean;
//            } else {
//                //throw new Exception("信息长度校验错误");
//                mPublicDataBean.setRTN("03");
//                mPublicDataBean.setInfo("");
//                ControllerLordDataSource.getInstance().setPublicSendData(mPublicDataBean); //设置应答数据
//                return mPublicDataBean;
//            }
//        } else {
//            throw new Exception("数据包校验错误");
//        }
//    }

//    private static void dealControlInfo(String info) throws Exception {
//        int group = Integer.parseInt(info.substring(0,2),16);
//        int data = Integer.parseInt(info.substring(2,4),16);
//        ControllerDataSource controllerDataSource = ControllerDataSource.getInstance();
//        Log.e(TAG,"info" + info);
//        switch (group){
//            case 0:
//                if (data == 0) {
//                    controllerDataSource.setButtonStatus(BOTH_NORMAL);
//                }else if (data == 1){
//                    AccumulatorApplication.getInstance().setFirstManageFlag(true);
//                    AccumulatorApplication.getInstance().setSecondManageFlag(false);
//                    controllerDataSource.setButtonStatus(FIRST_GROUP_DISCHARGE);
//                }else if (data == 2){
//                    AccumulatorApplication.getInstance().setSecondManageFlag(true);
//                    AccumulatorApplication.getInstance().setFirstManageFlag(false);
//                    controllerDataSource.setButtonStatus(SECOND_GROUP_DISCHARGE);
//                }else {
//                    EventBus.getDefault().post(new Exception("未知操作"));
//                }
//                break;
//            case 1:
//                if (data == 0) {
//                    controllerDataSource.setButtonStatus(FIRST_GROUP_DISCHARGE);
//                }else if (data == 1){
//                    controllerDataSource.setButtonStatus(FIRST_GROUP_WAIT);
//                }else if (data == 2){
//                    controllerDataSource.setButtonStatus(FIRST_GROUP_CHARGE);
//                }else {
//                    EventBus.getDefault().post( new Exception("未知操作"));
//                }
//                break;
//            case 2:
//                if (data == 0) {
//                    controllerDataSource.setButtonStatus(SECOND_GROUP_DISCHARGE);
//                }else if (data == 1){
//                    controllerDataSource.setButtonStatus(SECOND_GROUP_WAIT);
//                }else if (data == 2){
//                    controllerDataSource.setButtonStatus(SECOND_GROUP_CHARGE);
//                }else {
//                    EventBus.getDefault().post(new Exception("未知操作"));
//                }
//                break;
//            case 207:
//                //安卓主板重启
//                HardwareControl.WatchDogEnable(true);
//                HardwareControl.StarWatchDog();
//                EventBus.getDefault().post(new Exception("主板重启"));
//                break;
//            case 208:
//                if (data == 0) {
//                    controllerDataSource.setAutoControl(true);
//                }else if (data == 1){
//                    controllerDataSource.setAutoControl(false);
//                }else {
//                    EventBus.getDefault().post(new Exception("未知操作"));
//                }
//                break;
//            case 209:
//            case 212:
//                controllerDataSource.setButtonStatus(CHANNEL_NUMBER[group+1%10], data == 0 ? "01" : "00");
//                break;
//            case 210:
//            case 211:
//            case 213:
//            case 214:
//            case 215:
//            case 216:
//            case 217:
//            case 218:
//                controllerDataSource.setButtonStatus(CHANNEL_NUMBER[group+1%10], data == 1 ? "01" : "00");
//                break;
//            default:
//                break;
//        }
//    }
    /**
     * float转化4个byte
     * @param f
     * @return
     */
    private static byte[] floatToBytes(Float f){
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putFloat(f);
        return buf.array();
    }

    /**
     * byte数组数值反传
     * @param data
     * @return
     */
    private static byte[] dataValueRollback(byte[] data) {
        ArrayList<Byte> al = new ArrayList<Byte>();
        for (int i = data.length - 1; i >= 0; i--) {
            al.add(data[i]);
        }

        byte[] buffer = new byte[al.size()];
        for (int i = 0; i <= buffer.length - 1; i++) {
            buffer[i] = al.get(i);
        }
        return buffer;
    }

    /**
     * stringToStringBuilder
     * @param list
     * @return
     */
    private static StringBuilder stringToStringBuilder(List<String> list){
        StringBuilder stringBuilder = new StringBuilder();
        if (list == null) {
            for (int i = 0; i < 24; i++)
                stringBuilder.append(SYNTHESIZE_FLOAT_ZREO);
            return stringBuilder;
        }
        for (String s :list) {
            stringBuilder.append(floatToHexString(Float.parseFloat(s)));
        }
        return stringBuilder;
    }

    /**
     * intToHexString
     * @param i
     * @return
     */
    private static String intToHexString(int i){
        String hex = Integer.toHexString(i & 0xFF);
        if (hex.length() == 1)
            hex = '0'+ hex;
        return hex;
    }

    /**
     * byte[] printHexString
     * @param b
     * @return
     */
    private static StringBuilder printHexString( byte[] b) {
        StringBuilder a = new StringBuilder();
        for (byte i : b) {
            a.append(intToHexString(i));
        }
        return a;
    }

    /**
     * floatToHexString
     * @param f
     * @return
     */
    private static StringBuilder floatToHexString(float f){
       return printHexString(dataValueRollback(floatToBytes(f)));
    }

    /**
     *  打包 41 info数据
     * @return
     */
//    private static byte[] packageSynthesizeData() {
//        StringBuilder stringBuilder = new StringBuilder();
//        SurveySynthesizeBean synthesizeBean = AccumulatorApplication.getInstance().getSurveySynthesizeBean();
//        //通道0,DATAFLAG
//        stringBuilder.append(intToHexString(SYNTHESIZE_DATAFLAG));
//        //通道1,采集器状态
//        stringBuilder.append(intToHexString(SYNTHESIZE_STATUS));
//        //通道2,模拟量个数
//        stringBuilder.append(intToHexString(SYNTHESIZE_ANALOG_SIZE));
//        //通道3,第一组求和计算总电压 V
//        stringBuilder.append(floatToHexString(synthesizeBean.getFirstVoltageSum()));
//        //通道4,第一组总电流 A
//        stringBuilder.append(floatToHexString(synthesizeBean.getFirstElectricitySum()));
//        //通道5~28,第一组单电压01－24 V
//        stringBuilder.append(stringToStringBuilder(synthesizeBean.getFirstSingleVoltage()));
//        //通道29~52,第一组单温度01-24 ℃
//        stringBuilder.append(stringToStringBuilder(synthesizeBean.getFirstSingleTemperature()));
//        //通道53,第一组环境温度 ℃
//        stringBuilder.append(floatToHexString(synthesizeBean.getFirstTemperatureInfo()));
//        //通道54,第一组模块实测总电压 V
//        stringBuilder.append(floatToHexString(synthesizeBean.getFirstVoltageTotal()));
//        //通道55,第一组电压最大值
//        SumAndPeak tmp = new SumAndPeak(synthesizeBean.getFirstSingleVoltage());
//        stringBuilder.append(floatToHexString(tmp.max));
//        //通道56,第一组电压最小值
//        stringBuilder.append(floatToHexString(tmp.min));
//        //通道57,第一组温度最大值
//        tmp = new SumAndPeak(synthesizeBean.getFirstSingleTemperature());
//        stringBuilder.append(floatToHexString(tmp.max));
//        //通道58,第一组温度最小值
//        stringBuilder.append(floatToHexString(tmp.min));
//        //通道59,第二组求和计算总电压 V
//        stringBuilder.append(floatToHexString(synthesizeBean.getSecondVoltageSum()));
//        //通道60,第二组总电流 A
//        stringBuilder.append(floatToHexString(synthesizeBean.getSecondElectricitySum()));
//        //通道61~84,第二组单电压01－24 V
//        stringBuilder.append(stringToStringBuilder(synthesizeBean.getSecondSingleVoltage()));
//        //通道85~108,第二组单温度01－24 ℃
//        stringBuilder.append(stringToStringBuilder(synthesizeBean.getSecondSingleTemperature()));
//        //通道109,第二组环境温度 ℃
//        stringBuilder.append(floatToHexString(synthesizeBean.getSecondTemperatureInfo()));
//        //通道110,第二组模块实测总电压 ℃
//        stringBuilder.append(floatToHexString(synthesizeBean.getSecondVoltageTotal()));
//        //通道111,第二组电压最大值
//        tmp = new SumAndPeak(synthesizeBean.getSecondSingleVoltage());
//        stringBuilder.append(floatToHexString(tmp.max));
//        //通道112,第二组电压最小值
//        stringBuilder.append(floatToHexString(tmp.min));
//        //通道113,第二组温度最大值
//        tmp = new SumAndPeak(synthesizeBean.getSecondSingleTemperature());
//        stringBuilder.append(floatToHexString(tmp.max));
//        //通道114,第二组温度最小值
//        stringBuilder.append(floatToHexString(tmp.min));
//        //通道115,实际已放电的时长 min
//        stringBuilder.append(floatToHexString(synthesizeBean.getDischargeTime()));
//        //通道116,放电时的放出容量
//        stringBuilder.append(floatToHexString(AccumulatorApplication.getInstance().getDischargeCapacity()/3600f));
//
//        if (AccumulatorApplication.getInstance().getControlSwitchesBean().getAccumulatorStatus().equals(FIRST_GROUP_DISCHARGE)) {
//            //通道117,放电组总电压 V
//            stringBuilder.append(floatToHexString(synthesizeBean.getFirstVoltageTotal()));
//            //通道118,放电组电流 A
//            stringBuilder.append(floatToHexString(synthesizeBean.getFirstElectricitySum()));
//        }else if (AccumulatorApplication.getInstance().getControlSwitchesBean().getAccumulatorStatus().equals(SECOND_GROUP_DISCHARGE)){
//            stringBuilder.append(floatToHexString(synthesizeBean.getSecondVoltageTotal()));
//            stringBuilder.append(floatToHexString(synthesizeBean.getSecondElectricitySum()));
//        }else {
//            stringBuilder.append(SYNTHESIZE_FLOAT_ZREO);
//            stringBuilder.append(SYNTHESIZE_FLOAT_ZREO);
//        }
//
//        if (synthesizeBean.getStartDischargeTime() != null) {
//            //通道119,开始放电时间—年
//            stringBuilder.append(floatToHexString(synthesizeBean.getStartDischargeTime().getYear()));
//            //通道120,开始放电时间—月
//            stringBuilder.append(floatToHexString(synthesizeBean.getStartDischargeTime().getMonth()));
//            //通道121,开始放电时间—日
//            stringBuilder.append(floatToHexString(synthesizeBean.getStartDischargeTime().getDay()));
//            //通道122,开始放电时间—时
//            stringBuilder.append(floatToHexString(synthesizeBean.getStartDischargeTime().getHour()));
//            //通道123,开始放电时间—分
//            stringBuilder.append(floatToHexString(synthesizeBean.getStartDischargeTime().getMin()));
//            //通道124,开始放电时间—秒
//            stringBuilder.append(floatToHexString(synthesizeBean.getStartDischargeTime().getSecond()));
//        }else {
//            stringBuilder.append(SYNTHESIZE_FLOAT_ZREO + SYNTHESIZE_FLOAT_ZREO + SYNTHESIZE_FLOAT_ZREO +
//                    SYNTHESIZE_FLOAT_ZREO + SYNTHESIZE_FLOAT_ZREO + SYNTHESIZE_FLOAT_ZREO);
//        }
//
//        if (synthesizeBean.getEndDischargeTime() != null) {
//            //通道125,结束放电时间—年
//            stringBuilder.append(floatToHexString(synthesizeBean.getEndDischargeTime().getYear()));
//            //通道126,结束放电时间—月
//            stringBuilder.append(floatToHexString(synthesizeBean.getEndDischargeTime().getMonth()));
//            //通道127,结束放电时间—日
//            stringBuilder.append(floatToHexString(synthesizeBean.getEndDischargeTime().getDay()));
//            //通道128,结束放电时间—时
//            stringBuilder.append(floatToHexString(synthesizeBean.getEndDischargeTime().getHour()));
//            //通道129,结束放电时间—分
//            stringBuilder.append(floatToHexString(synthesizeBean.getEndDischargeTime().getMin()));
//            //通道130,结束放电时间—秒
//            stringBuilder.append(floatToHexString(synthesizeBean.getEndDischargeTime().getSecond()));
//        }else {
//            stringBuilder.append(SYNTHESIZE_FLOAT_ZREO + SYNTHESIZE_FLOAT_ZREO + SYNTHESIZE_FLOAT_ZREO +
//                    SYNTHESIZE_FLOAT_ZREO + SYNTHESIZE_FLOAT_ZREO + SYNTHESIZE_FLOAT_ZREO);
//        }
//        //通道131~150,备用
//        for (int i = 131;i < 151; i++) {
//            stringBuilder.append(SYNTHESIZE_FLOAT_ZREO);
//        }
//        //通道151,开关量个数
//        stringBuilder.append(intToHexString(SYNTHESIZE_SWITCH_SIZE));
//        //通道152~161,无线终输入开关量1－10
//        ControlSwitchesBean controlSwitchesBean = AccumulatorApplication.getInstance().getControlSwitchesBean();
//        String accumulatorStatus = controlSwitchesBean.getAccumulatorStatus();
//        for (int i = 0;i < 8;i++)
//            stringBuilder.append('0' + accumulatorStatus.substring(i ,i + 1));
//        if (controlSwitchesBean.isK1())
//            stringBuilder.append(SYNTHESIZE_SWITCH_ONE);
//        else
//            stringBuilder.append(SYNTHESIZE_SWITCH_ZREO);
//        if (controlSwitchesBean.isK2())
//            stringBuilder.append(SYNTHESIZE_SWITCH_ONE);
//        else
//            stringBuilder.append(SYNTHESIZE_SWITCH_ZREO);
//        //通道162,电池组状态
//        switch (accumulatorStatus){
//            case FIRST_GROUP_DISCHARGE:
//                stringBuilder.append(SYNTHESIZE_SWITCH_FIRST_GROUP_DISCHARGE);
//                break;
//            case SECOND_GROUP_DISCHARGE:
//                stringBuilder.append(SYNTHESIZE_SWITCH_SECOND_GROUP_DISCHARGE);
//                break;
//            case FIRST_GROUP_CHARGE:
//                stringBuilder.append(SYNTHESIZE_SWITCH_FIRST_GROUP_CHARGE);
//                break;
//            case SECOND_GROUP_CHARGE:
//                stringBuilder.append(SYNTHESIZE_SWITCH_SECOND_GROUP_CHARGE);
//                break;
//            default:
//                stringBuilder.append(SYNTHESIZE_SWITCH_ZREO);
//                break;
//        }
//        //通道163,停止放电原因
//        stringBuilder.append(intToHexString(synthesizeBean.getStopDischargeReason()));
//        //通道164,停止充电原因
//        stringBuilder.append(intToHexString(synthesizeBean.getStopChargeReason()));
//        //通道165,控制模式
//        ControllerDataSource source = ControllerDataSource.getInstance();
//        if (source.isAutoControl())
//            stringBuilder.append(SYNTHESIZE_SWITCH_ZREO);
//        else
//            stringBuilder.append(SYNTHESIZE_SWITCH_ONE);
//        //通道166,第一组3100模块通信状态
//        stringBuilder.append(intToHexString(synthesizeBean.getFirstVoltageAndCurrentStatus()));
//        //通道167,第二组3101模块通信状态
//        stringBuilder.append(intToHexString(synthesizeBean.getSecondVoltageAndCurrentStatus()));
//        //通道168,第一组温度模块通信状态
//        stringBuilder.append(intToHexString(synthesizeBean.getFirstBatteryTemperatureStatus()));
//        //通道169,第二组温度模块通信状态
//        stringBuilder.append(intToHexString(synthesizeBean.getSecondBatteryTemperatureStatus()));
//        //通道170,无线终端通信状态
//        stringBuilder.append(intToHexString(synthesizeBean.getButtonInfoStatus()));
//        //通道171,总电压异常
//        stringBuilder.append(intToHexString(synthesizeBean.getAlarmTotalVoltage()));
//        //通道172,单电压异常
//        stringBuilder.append(intToHexString(synthesizeBean.getAlarmSingleVoltage()));
//        //通道173,温度异常
//        stringBuilder.append(intToHexString(synthesizeBean.getAlarmTemperature()));
//        //通道174,充电电流异常
//        stringBuilder.append(intToHexString(synthesizeBean.getAlarmChargeElectricity()));
//        //通道175,放电电流异常
//        stringBuilder.append(intToHexString(synthesizeBean.getAlarmDischargeElectricity()));
//        //通道176,总压差异常
//        stringBuilder.append(intToHexString(synthesizeBean.getAlarmTotalVoltageDiff()));
//        //通道177,市电停电
//        stringBuilder.append(intToHexString(synthesizeBean.getAlarmUtilityFailure()));
//        //通道178,通信异常
//        stringBuilder.append(intToHexString(synthesizeBean.getAlarmCommunicationBroke()));
//        //通道179,硬件故障
//        stringBuilder.append(intToHexString(synthesizeBean.getAlarmHardwareFailure()));
//        //备用180,操作组别
//        stringBuilder.append(intToHexString(synthesizeBean.getTestGroup()));
//        //通道181~190,备用
//        for (int i = 181;i < 191; i++) {
//            stringBuilder.append(SYNTHESIZE_SWITCH_ZREO);
//        }
//        return stringBuilder.toString().getBytes();
//    }

//    static class SumAndPeak{
//        float sum;
//        float min;
//        float max;
//
//        public SumAndPeak (List<String> list){  //返回 最大值 最小值 總和
//            float min = Float.MAX_VALUE, max = Float.MIN_VALUE, sum = 0;
//            if(list != null)
//                for (String str : list){
//                    float tmp = Float.parseFloat(str);
//                    sum = sum + tmp;
//                    if (tmp > max ) {
//                        max = tmp;
//                    }
//                    if (tmp < min ) {
//                        min = tmp;
//                    }
//                }
//            this.max = max;
//            this.min = min;
//            this.sum = sum;
//        }
//    }
}
