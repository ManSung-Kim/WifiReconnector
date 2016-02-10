package jactlab.wifireconnector;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by KIMMANSUNG-NOTE on 2016-02-04.
 */
public class WManager {

    public static ArrayList<HashMap<String,String>> getScanData(Context context) {
        WifiManager lmWManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        List<ScanResult> lmListScanRst = lmWManager.getScanResults();

        ArrayList<HashMap<String,String>> lmApList = new ArrayList< HashMap<String,String> >();
        for(ScanResult result: lmListScanRst) {
            HashMap<String,String> item = new HashMap<String,String>();
            item.put(StaticData.AP_INFO_BSSID,      result.BSSID);
            item.put(StaticData.AP_INFO_SSID,       result.SSID);
            item.put(StaticData.AP_INFO_CONFIG,     result.capabilities);
            item.put(StaticData.AP_INFO_FREQ,       result.frequency+"");
            item.put(StaticData.AP_INFO_LEVEL,      result.level+"");
            lmApList.add(item);
        }

        Utils.logd(lmListScanRst+"");
        return lmApList;
    }

    public static boolean connectWifi(Context context, String ssid, String password, String capablities) {
        WifiConfiguration wfc = new WifiConfiguration();

        wfc.SSID = "\"".concat( ssid ).concat("\"");
        wfc.status = WifiConfiguration.Status.DISABLED;
        wfc.priority = 40;

        if(capablities.contains("WEP") == true ){
            wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wfc.wepKeys[0] = "\"".concat(password).concat("\"");
            wfc.wepTxKeyIndex = 0;
        }else if(capablities.contains("WPA") == true ) {
            wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wfc.preSharedKey = "\"".concat(password).concat("\"");
        }else if(capablities.contains("WPA2") == true ) {
            wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wfc.preSharedKey = "\"".concat(password).concat("\"");
        }else if(capablities.contains("OPEN") == true ) {
            wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wfc.allowedAuthAlgorithms.clear();
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        } else { // 수정필요
            wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wfc.allowedAuthAlgorithms.clear();
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        }


        WifiManager wfMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int networkId = wfMgr.addNetwork(wfc);

        WifiManager delwfMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration delId = new WifiConfiguration();
        //delId.SSID = "hihihi";
        delId.SSID = ssid;
        boolean isDel  = wfMgr.disableNetwork(delwfMgr.addNetwork(delId));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Utils.logd("isDel? "+isDel);

        boolean connection = false;
        if (networkId != -1) {
            Toast.makeText(context, "Connecting WIFI\n"+ssid, Toast.LENGTH_SHORT).show();
            connection = wfMgr.enableNetwork(networkId, true);
        }
        return connection;
    }
}
