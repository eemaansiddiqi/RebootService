package micronet.com.rebootservice;

/**
 * Created by eemaan.siddiqi on 11/7/2017.
 */

public class DeviceManager {

    private static final String POWER_MGM_DEVICE_ON_IGNITION_TRIGGER = "Ignition Trigger";
    private static final String POWER_MGM_DEVICE_ON_WIGGLE_TRIGGER = "Wiggle Trigger";
    private static final String POWER_MGM_DEVICE_ARM_LOCKUP = "Arm lockup";
    private static final String POWER_MGM_DEVICE_WATCHDOG_RESET = "Watchdog reset";
    private static final String POWER_MGM_DEVICE_SW_RESET_REQ = "Software Reset Request";

    private static String devicePowerOnReason = "";
    private int powerOnReason = 0;

    /**
     * Returns a string that contains the power on reason
     * To get the reason for the A8/CPU power up, the following command can be sent. The response is a bit field:
     *   #define POWER_MGM_DEVICE_ON_IGNITION_TRIGGER		(1 << 0)
     *   #define POWER_MGM_DEVICE_ON_WIGGLE_TRIGGER			(1 << 1)
     *   #define POWER_MGM_DEVICE_ARM_LOCKUP				(1 << 2)
     *   #define POWER_MGM_DEVICE_WATCHDOG_RESET			(1 << 3)
     *   #define POWER_MGM_DEVICE_SW_RESET_REQ			    (1 << 4)
     */
    public static String getPowerOnReason(){

        return devicePowerOnReason;
    }



}
