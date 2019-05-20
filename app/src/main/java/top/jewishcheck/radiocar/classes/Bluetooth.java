package top.jewishcheck.radiocar.classes;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.util.UUID;

public class Bluetooth extends me.aflak.bluetooth.Bluetooth {
    /**
     * Init Bluetooth object. Default UUID will be used.
     *
     * @param context Context to be used.
     */
    public Bluetooth(Context context) {
        super(context);
    }

    /**
     * Init Bluetooth object.
     *
     * @param context Context to be used.
     * @param uuid    Socket UUID to be used.
     */
    public Bluetooth(Context context, UUID uuid) {
        super(context, uuid);
    }

    /**
     * Check bounded device by device
     *
     * @param device
     * @return
     */
    public boolean isBounded(BluetoothDevice device) {
        for (BluetoothDevice _device : getPairedDevices()) {
            if (device.getAddress().equals(_device.getAddress())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Is bounded by address
     *
     * @param address
     * @return
     */
    public boolean isBounded(String address) {
        for (BluetoothDevice device : getPairedDevices()) {
            if (device.getAddress().equals(address)) {
                return true;
            }
        }

        return false;
    }
}
