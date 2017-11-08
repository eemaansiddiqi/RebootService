package micronet.com.rebootservice;

import android.util.Log;

public class DeviceManager {

    private static final String POWER_MGM_DEVICE_ON_IGNITION_TRIGGER = "Ignition Trigger";
    private static final String POWER_MGM_DEVICE_ON_WIGGLE_TRIGGER = "Wiggle Trigger";
    private static final String POWER_MGM_DEVICE_ARM_LOCKUP = "Arm lockup";
    private static final String POWER_MGM_DEVICE_WATCHDOG_RESET = "Watchdog reset";
    private static final String POWER_MGM_DEVICE_SW_RESET_REQ = "Software Reset Request";
    private static final String TAG = "DeviceManager" ;

    private static String devicePowerOnReason = "";
    private static int powerOnReason = 0;

    /**
     * Returns a string that contains the power on reason
     * To get the reason for the A8/CPU power up, the following command can be sent. The response is a bit field:
     *   #define POWER_MGM_DEVICE_ON_IGNITION_TRIGGER		(1 << 0)
     *   #define POWER_MGM_DEVICE_ON_WIGGLE_TRIGGER			(1 << 1)
     *   #define POWER_MGM_DEVICE_ARM_LOCKUP				(1 << 2)
     *   #define POWER_MGM_DEVICE_WATCHDOG_RESET			(1 << 3)
     *   #define POWER_MGM_DEVICE_SW_RESET_REQ			    (1 << 4)
     */
    public String getPowerOnReason() {

        devicePowerOnReason = "";
        powerOnReason = getDevicePowerOn();
        Log.d(TAG, "Integer Value rxd = " + powerOnReason);

        if(getBit(powerOnReason, 0) == 1) {
            devicePowerOnReason = POWER_MGM_DEVICE_ON_IGNITION_TRIGGER;
        }
        if(getBit(powerOnReason, 1) == 1 ){
            devicePowerOnReason = devicePowerOnReason + ", " + POWER_MGM_DEVICE_ON_WIGGLE_TRIGGER;
        }
        if(getBit(powerOnReason, 2) == 1) {
            devicePowerOnReason = devicePowerOnReason + ", " + POWER_MGM_DEVICE_ARM_LOCKUP;
        }
        if(getBit(powerOnReason, 3) == 4 ) {
            devicePowerOnReason = devicePowerOnReason + ", " + POWER_MGM_DEVICE_WATCHDOG_RESET;
        }
        if(getBit(powerOnReason, 4) == 1) {
            devicePowerOnReason = devicePowerOnReason + ", " + POWER_MGM_DEVICE_SW_RESET_REQ;
        }
        return devicePowerOnReason;
    }

    int getBit(int n, int k) {
        return (n >> k) & 1;
    }

    public native int getDevicePowerOn();

    static {
        System.loadLibrary("mctl");
    }
}

