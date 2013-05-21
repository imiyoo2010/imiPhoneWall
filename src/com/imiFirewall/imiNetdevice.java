package com.imiFirewall;

import com.imiFirewall.util.imiUtil;


public class imiNetdevice
{

    public imiNetdevice()
    {
    }

    public static String mobile(String s)
    {
        String s1;
        s1 = "";
        String as[][];
        int j;
        s = imiUtil.stringBlank(s);
        as = DEVICES;
        j = as.length;
        for(int i=0;i<j;i++)
        {
          String as1[]=as[i];
          if(as1[0].equals(s)){
          s1=as1[1];
          } 
        }
          return s1;
    }

    public static long rx_bytes(String s)
    {
        long l_rx = 0L;
        try
        {
            l_rx = Long.parseLong(imiUtil.read((new StringBuilder("/sys/class/net/")).append(s).append("/statistics/rx_bytes").toString()));
        }
        catch(Exception exception) { }
        return l_rx;
    }

    public static long tx_bytes(String s)
    {
        long l_tx = 0L;
        try
        {
            l_tx = Long.parseLong(imiUtil.read((new StringBuilder("/sys/class/net/")).append(s).append("/statistics/tx_bytes").toString()));
        }
        catch(Exception exception) { }
        return l_tx;
    }

    //获取wifi设备的名称
    public static String wifi(String s)
    {
        String s1;
        s1 = "";
        String as[][];
        int j;
        s = imiUtil.stringBlank(s);
        as = DEVICES;
        j = as.length;
       for(int i=0;i<j;i++)
       {
         String as1[]=as[i];
         if(as1[0].equals(s)){
             s1=as1[2];
         }
        }
       return s1;  

    }

    public static final String DEVICES[][];
    public static final String DEVICE_MOBILE = "rmnet0";
    public static final String DEVICE_WIFI = "tiwlan0";

    static 
    {
        String as[][] = new String[85][3];
        String s[] = new String[3];
        s[0] = "a1";
        s[1] = "rmnet0";
        s[2] = "eth0";
        as[0] = s;
        String s1[] = new String[3];
        s1[0] = "a50";
        s1[1] = "rmnet0";
        s1[2] = "1";
        as[1] = s1;
        String s2[] = new String[3];
        s2[0] = "A60";
        s2[1] = "rmnet0";
        s2[2] = "athwlan0";
        as[2] = s2;
        String s3[] = new String[3];
        s3[0] = "A688";
        s3[1] = "rmnet0";
        s3[2] = "athwlan0";
        as[3] = s3;
        String s4[] = new String[3];
        s4[0] = "A88";
        s4[1] = "rmnet0";
        s4[2] = "athwlan0";
        as[4] = s4;
        String s5[] = new String[3];
        s5[0] = "archer";
        s5[1] = "pdp0";
        s5[2] = "tiwlan0";
        as[5] = s5;
        String s6[] = new String[3];
        s6[0] = "bahamas";
        s6[1] = "rmnet0";
        s6[2] = "tiwlan0";
        as[6] = s6;
        String s7[] = new String[3];
        s7[0] = "brava";
        s7[1] = "ppp0";
        s7[2] = "eth0";
        as[7] = s7;
        String s8[] = new String[3];
        s8[0] = "bravo";
        s8[1] = "rmnet0";
        s8[2] = "eth0";
        as[8] = s8;
        String s9 []= new String[3];
        s9[0] = "calgary";
        s9[1] = "rmnet0";
        s9[2] = "eth0";
        as[9] = s9;
        String s10[] = new String[3];
        s10[0] = "choles";
        s10[1] = "rmnet0";
        s10[2] = "tiwlan0";
        as[10] = s10;
        String s11[] = new String[3];
        s11[0] = "CHT8000";
        s11[1] = "rmnet0";
        s11[2] = "eth0";
        as[11] = s11;
        String s12[] = new String[3];
        s12[0] = "desirec";
        s12[1] = "rmnet0";
        s12[2] = "tiwlan0";
        as[12] = s12;
        String s13[] = new String[3];
        s13[0] = "dream";
        s13[1] = "rmnet0";
        s13[2] = "tiwlan0";
        as[13] = s13;
        String s14[] = new String[3];
        s14[0] = "dream-open";
        s14[1] = "rmnet0";
        s14[2] = "tiwlan0";
        as[14] = s14;
        String s15[] = new String[3];
        s15[0] = "eeepc";
        s15[1] = "1";
        s15[2] = "wlan0";
        as[15] = s15;
        String s16[] = new String[3];
        s16[0] = "EVE";
        s16[1] = "rmnet0";
        s16[2] = "wlan0";
        as[16] = s16;
        String s17[] = new String[3];
        s17[0] = "FBW1_4";
        s17[1] = "ccinet0";
        s17[2] = "eth0";
        as[17] = s17;
        String s18[] = new String[3];
        s18[0] = "freerunner";
        s18[1] = "1";
        s18[2] = "eth0";
        as[18] = s18;
        String s19[] = new String[3];
        s19[0] = "g7a";
        s19[1] = "1";
        s19[2] = "tiwlan0";
        as[19] = s19;
        String s20[] = new String[3];
        s20[0] = "galaxy";
        s20[1] = "pdp0";
        s20[2] = "eth0";
        as[20] = s20;
        String s21[] = new String[3];
        s21[0] = "geeksphone-one";
        s21[1] = "rmnet0";
        s21[2] = "wlan0";
        as[21] = s21;
        String s22[] = new String[3];
        s22[0] = "generic";
        s22[1] = "1";
        s22[2] = "eth0";
        as[22] = s22;
        String s23[] = new String[3];
        s23[0] = "GT-I5700";
        s23[1] = "pdp0";
        s23[2] = "eth0";
        as[23] = s23;
        String s24[] = new String[3];
        s24[0] = "GT-I6500U";
        s24[1] = "pdp0";
        s24[2] = "eth0";
        as[24] = s24;
        String s25[] = new String[3];
        s25[0] = "GT-I7500";
        s25[1] = "pdp0";
        s25[2] = "eth0";
        as[25] = s25;
        String s26[] = new String[3];
        s26[0] = "hero";
        s26[1] = "rmnet0";
        s26[2] = "tiwlan0";
        as[26] = s26;
        String s27[] = new String[3];
        s27[0] = "hero-open";
        s27[1] = "rmnet0";
        s27[2] = "tiwlan0";
        as[27] = s27;
        String s28[] = new String[3];
        s28[0] = "heroc";
        s28[1] = "rmnet0";
        s28[2] = "tiwlan0";
        as[28] = s28;
        String s29[] = new String[3];
        s29[0] = "heroc-open";
        s29[1] = "rmnet0";
        s29[2] = "tiwlan0";
        as[29] = s29;
        String s30[] = new String[3];
        s30[0] = "HTC Hero";
        s30[1] = "rmnet0";
        s30[2] = "tiwlan0";
        as[30] = s30;
        String s31[] = new String[3];
        s31[0] = "I7500";
        s31[1] = "pdp0";
        s31[2] = "eth0";
        as[31] = s31;
        String s32[] = new String[3];
        s32[0] = "iDEN";
        s32[1] = "ppp0";
        s32[2] = "eth1";
        as[32] = s32;
        String s33[] = new String[3];
        s33[0] = "iM9815";
        s33[1] = "ppp0";
        s33[2] = "eth0";
        as[33] = s33;
        String s34[] = new String[3];
        s34[0] = "inc";
        s34[1] = "rmnet0";
        s34[2] = "eth0";
        as[34] = s34;
        String s35[] = new String[3];
        s35[0] = "JAX10";
        s35[1] = "hso0";
        s35[2] = "eth0";
        as[35] = s35;
        String s36[] = new String[3];
        s36[0] = "legend";
        s36[1] = "rmnet0";
        s36[2] = "tiwlan0";
        as[36] = s36;
        String s37[] = new String[3];
        s37[0] = "liberty";
        s37[1] = "rmnet0";
        s37[2] = "eth0";
        as[37] = s37;
        String s38[] = new String[3];
        s38[0] = "lucky";
        s38[1] = "ccinet1";
        s38[2] = "eth0";
        as[38] = s38;
        String s39[] = new String[3];
        s39[0] = "magic";
        s39[1] = "rmnet0";
        s39[2] = "tiwlan0";
        as[39] = s39;
        String s40[] = new String[3];
        s40[0] = "morrison";
        s40[1] = "rmnet0";
        s40[2] = "eth0";
        as[40] = s40;
        String s41[] = new String[3];
        s41[0] = "motus";
        s41[1] = "rmnet0";
        s41[2] = "eth0";
        as[41] = s41;
        String s42[] = new String[3];
        s42[0] = "msm7225_adq";
        s42[1] = "rmnet0";
        s42[2] = "wlan0";
        as[42] = s42;
        String s43[] = new String[3];
        s43[0] = "msm7627_surf";
        s43[1] = "rmnet0";
        s43[2] = "athwlan0";
        as[43] = s43;
        String s44[] = new String[3];
        s44[0] = "N19";
        s44[1] = "ppp0";
        s44[2] = "eth0";
        as[44] = s44;
        String s45[] = new String[3];
        s45[0] = "passion";
        s45[1] = "rmnet0";
        s45[2] = "eth0";
        as[45] = s45;
        String s46[] = new String[3];
        s46[0] = "qsd8250_surf";
        s46[1] = "rmnet0";
        s46[2] = "eth0";
        as[46] = s46;
        String s47[] = new String[3];
        s47[0] = "RBM2";
        s47[1] = "rmnet0";
        s47[2] = "eth0";
        as[47] = s47;
        String s48[] = new String[3];
        s48[0] = "robyn";
        s48[1] = "rmnet0";
        s48[2] = "tiwlan0";
        as[48] = s48;
        String s49[] = new String[3];
        s49[0] = "S812";
        s49[1] = "ppp0";
        s49[2] = "wlan0";
        as[49] = s49;
        String s50[] = new String[3];
        s50[0] = "S812_MDO";
        s50[1] = "ppp0";
        s50[2] = "wlan0";
        as[50] = s50;
        String s51[] = new String[3];
        s51[0] = "sapphire";
        s51[1] = "rmnet0";
        s51[2] = "tiwlan0";
        as[51] = s51;
        String s52[] = new String[3];
        s52[0] = "sapphire-3pei";
        s52[1] = "rmnet0";
        s52[2] = "tiwlan0";
        as[52] = s52;
        String s53[] = new String[3];
        s53[0] = "sapphire-open";
        s53[1] = "rmnet0";
        s53[2] = "tiwlan0";
        as[53] = s53;
        String s54[] = new String[3];
        s54[0] = "sapphire_sanpei";
        s54[1] = "rmnet0";
        s54[2] = "tiwlan0";
        as[54] = s54;
        String s55[] = new String[3];
        s55[0] = "SAX";
        s55[1] = "rmnet0";
        s55[2] = "athwlan0";
        as[55] = s55;
        String s56[] = new String[3];
        s56[0] = "SCH-i899";
        s56[1] = "ppp0";
        s56[2] = "eth0";
        as[56] = s56;
        String s57[] = new String[3];
        s57[0] = "sdkDemo";
        s57[1] = "1";
        s57[2] = "wlan0";
        as[57] = s57;
        String s58[] = new String[3];
        s58[0] = "SGH-T939";
        s58[1] = "pdp0";
        s58[2] = "eth0";
        as[58] = s58;
        String s59[] = new String[3];
        s59[0] = "sholes";
        s59[1] = "ppp0";
        s59[2] = "tiwlan0";
        as[59] = s59;
        String s60[] = new String[3];
        s60[0] = "sholest";
        s60[1] = "rmnet0";
        s60[2] = "tiwlan0";
        as[60] = s60;
        String s61[] = new String[3];
        s61[0] = "smdk6410";
        s61[1] = "1";
        s61[2] = "eth0";
        as[61] = s61;
        String s62[] = new String[3];
        s62[0] = "smdkc100";
        s62[1] = "1";
        s62[2] = "eth0";
        as[62] = s62;
        String s63[] = new String[3];
        s63[0] = "SnapperTD";
        s63[1] = "ppp0";
        s63[2] = "eth0";
        as[63] = s63;
        String s64[] = new String[3];
        s64[0] = "SonyEricssonE10a";
        s64[1] = "rmnet0";
        s64[2] = "tiwlan0";
        as[64] = s64;
        String s65[] = new String[3];
        s65[0] = "SonyEricssonE10i";
        s65[1] = "rmnet0";
        s65[2] = "tiwlan0";
        as[65] = s65;
        String s66[] = new String[3];
        s66[0] = "SonyEricssonSO-01B";
        s66[1] = "rmnet0";
        s66[2] = "athwlan0";
        as[66] = s66;
        String s67[] = new String[3];
        s67[0] = "SonyEricssonU20i";
        s67[1] = "rmnet0";
        s67[2] = "tiwlan0";
        as[67] = s67;
        String s68[] = new String[3];
        s68[0] = "SonyEricssonX10a";
        s68[1] = "rmnet0";
        s68[2] = "athwlan0";
        as[68] = s68;
        String s69[] = new String[3];
        s69[0] = "SonyEricssonX10i";
        s69[1] = "rmnet0";
        s69[2] = "athwlan0";
        as[69] = s69;
        String s70[] = new String[3];
        s70[0] = "SonyEricssonX10iv";
        s70[1] = "rmnet0";
        s70[2] = "athwlan0";
        as[70] = s70;
        String s71[] = new String[3];
        s71[0] = "SPH-M900";
        s71[1] = "ppp0";
        s71[2] = "eth0";
        as[71] = s71;
        String s72[] = new String[3];
        s72[0] = "spica";
        s72[1] = "pdp0";
        s72[2] = "eth0";
        as[72] = s72;
        String s73[] = new String[3];
        s73[0] = "swift";
        s73[1] = "rmnet0";
        s73[2] = "wlan0";
        as[73] = s73;
        String s74[] = new String[3];
        s74[0] = "titanium";
        s74[1] = "ppp0";
        s74[2] = "tiwlan0";
        as[74] = s74;
        String s75[] = new String[3];
        s75[0] = "titaniumskt";
        s75[1] = "rmnet0";
        s75[2] = "tiwlan0";
        as[75] = s75;
        String s76[] = new String[3];
        s76[0] = "trout";
        s76[1] = "rmnet0";
        s76[2] = "tiwlan0";
        as[76] = s76;
        String s77[] = new String[3];
        s77[0] = "U8110";
        s77[1] = "rmnet0";
        s77[2] = "eth0";
        as[77] = s77;
        String s78[] = new String[3];
        s78[0] = "U8220";
        s78[1] = "rmnet0";
        s78[2] = "eth0";
        as[78] = s78;
        String s79[] = new String[3];
        s79[0] = "U8220-6";
        s79[1] = "rmnet0";
        s79[2] = "eth0";
        as[79] = s79;
        String s80[] = new String[3];
        s80[0] = "U8230";
        s80[1] = "rmnet0";
        s80[2] = "eth0";
        as[80] = s80;
        String s81[] = new String[3];
        s81[0] = "umts_sholes";
        s81[1] = "rmnet0";
        s81[2] = "tiwlan0";
        as[81] = s81;
        String s82[] = new String[3];
        s82[0] = "voles";
        s82[1] = "ppp0";
        s82[2] = "tiwlan0";
        as[82] = s82;
        String s83[] = new String[3];
        s83[0] = "x2";
        s83[1] = "ppp0";
        s83[2] = "eth0";
        as[83] = s83;
        String s84[] = new String[3];
        s84[0] = "zeppelin";
        s84[1] = "rmnet0";
        s84[2] = "eth0";
        as[84] = s84;
        DEVICES = as;
    }
}
