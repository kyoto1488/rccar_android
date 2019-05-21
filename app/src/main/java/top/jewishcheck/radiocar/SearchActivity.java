package top.jewishcheck.radiocar;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import me.aflak.bluetooth.interfaces.BluetoothCallback;
import me.aflak.bluetooth.interfaces.DeviceCallback;
import me.aflak.bluetooth.interfaces.DiscoveryCallback;
import top.jewishcheck.radiocar.classes.Bluetooth;
import top.jewishcheck.radiocar.constants.Search;


public class SearchActivity extends AppCompatActivity  {
    final private String TAG = "SEARCH_ACTIVITY";

    private String targetDeviceMacAddr = "";
    private String targetDevicePin = "";

    private boolean isFoundTargetDevice = false;
    private Bluetooth bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        bluetooth = new Bluetooth(this);

        bluetooth.setBluetoothCallback(bluetoothCallback);
        bluetooth.setDeviceCallback(deviceCallback);
        bluetooth.setDiscoveryCallback(discoveryCallback);

        try {
            targetDevicePin = "" + Utils.getIntMetadata(getApplicationContext(), "target_device_pin", 1234);
            targetDeviceMacAddr = Utils.getStringMetadata(getApplicationContext(), "target_device_mac", "");
        } catch (Exception exception) {
            Log.d(TAG, "---------- EXCEPTION ----------");
            Log.d(TAG, "Message: Exception get metadata from manifest. Failed get data target device");
            Log.d(TAG, "------------- END -------------");

            Utils.showLongToast(getApplicationContext(), "Sorry! Exception get meta data target device");
            finish();
        }
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "---------- ACTIVITY ----------");
        Log.i(TAG, "Method: onStart");
        Log.i(TAG, "------------ END -------------");

        super.onStart();
        bluetooth.onStart();

        if (bluetooth.isEnabled()) {
            bluetooth.startScanning();
        } else {
            bluetooth.enable();
        }
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "---------- ACTIVITY ----------");
        Log.i(TAG, "Method: onStop");
        Log.i(TAG, "------------ END -------------");

        super.onStop();
        bluetooth.onStop();
    }

    /**
     * Finish activity with result canceled and code
     *
     * @param code
     */
    private void canceledWithResultCode(final int code) {
        Intent intent = new Intent();
        intent.putExtra("resultCode", code);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private BluetoothCallback bluetoothCallback = new BluetoothCallback() {
        @Override
        public void onBluetoothTurningOn() {
            Log.i(TAG, "---------- BLUETOOTH ----------");
            Log.i(TAG, "Method: onBluetoothTurningOn");
            Log.i(TAG, "------------- END -------------");
        }

        @Override
        public void onBluetoothOn() {
            Log.i(TAG, "---------- BLUETOOTH ----------");
            Log.i(TAG, "Method: onBluetoothOn");
            Log.i(TAG, "------------- END -------------");

            bluetooth.startScanning();
        }

        @Override
        public void onBluetoothTurningOff() {
            Log.i(TAG, "---------- BLUETOOTH ----------");
            Log.i(TAG, "Method: onBluetoothTurningOff");
            Log.i(TAG, "------------- END -------------");
        }

        @Override
        public void onBluetoothOff() {
            Log.i(TAG, "---------- BLUETOOTH ----------");
            Log.i(TAG, "Method: onBluetoothOff");
            Log.i(TAG, "------------- END -------------");

            canceledWithResultCode(Search.RESULT_BLUETOOTH_OFF);
        }

        @Override
        public void onUserDeniedActivation() {
            Log.i(TAG, "---------- BLUETOOTH ----------");
            Log.i(TAG, "Method: onUserDeniedActivation");
            Log.i(TAG, "------------- END -------------");

            canceledWithResultCode(Search.RESULT_USER_DENIED_ACTIVATION);
        }
    };
    private DeviceCallback deviceCallback = new DeviceCallback() {
        @Override
        public void onDeviceConnected(BluetoothDevice device) {
            Log.i(TAG, "---------- BLUETOOTH ----------");
            Log.i(TAG, "Method: onDeviceConnected");
            Log.i(TAG, "Device: " + device.getName() + " " + device.getAddress());
            Log.i(TAG, "------------- END -------------");
            Log.i(TAG, "BLUETOOTH: On device connected");

            if (device.getAddress().equals(targetDeviceMacAddr)) {
                bluetooth.stopScanning();

                // Code here...
            } else {
                canceledWithResultCode(Search.CONNECTED_TO_UNKOWN_DEVICE);
            }
        }

        @Override
        public void onDeviceDisconnected(BluetoothDevice device, String message) {
            Log.i(TAG, "---------- BLUETOOTH ----------");
            Log.i(TAG, "Method: onDeviceDisconnected");
            Log.i(TAG, "Device: " + device.getName() + " " + device.getAddress());
            Log.i(TAG, "Message: " + message);
            Log.i(TAG, "------------- END -------------");
        }

        @Override
        public void onMessage(String message) {
            Log.i(TAG, "---------- BLUETOOTH ----------");
            Log.i(TAG, "Method: onMessage");
            Log.i(TAG, "Message: " + message);
            Log.i(TAG, "------------- END -------------");
        }

        @Override
        public void onError(int errorCode) {
            Log.i(TAG, "---------- BLUETOOTH ----------");
            Log.i(TAG, "Method: onError");
            Log.i(TAG, "Error code: " + errorCode);
            Log.i(TAG, "------------- END -------------");
        }

        @Override
        public void onConnectError(BluetoothDevice device, String message) {
            Log.i(TAG, "---------- BLUETOOTH ----------");
            Log.i(TAG, "Method: onConnectError");
            Log.i(TAG, "Device: " + device.getName() + " " + device.getAddress());
            Log.i(TAG, "Message: " + message);
            Log.i(TAG, "------------- END -------------");

            if (device.getAddress().equals(targetDeviceMacAddr)) {
                isFoundTargetDevice = false;
                bluetooth.startScanning();
            }

        }
    };
    private DiscoveryCallback discoveryCallback = new DiscoveryCallback() {
        @Override
        public void onDiscoveryStarted() {
            Log.i(TAG, "---------- BLUETOOTH ----------");
            Log.i(TAG, "Method: onDiscoveryStarted");
            Log.i(TAG, "------------- END -------------");
        }

        @Override
        public void onDiscoveryFinished() {
            Log.i(TAG, "---------- BLUETOOTH ----------");
            Log.i(TAG, "Method: onDiscoveryFinished");
            Log.i(TAG, "------------- END -------------");

            if (!isFoundTargetDevice) {
                bluetooth.startScanning();
            }
        }

        @Override
        public void onDeviceFound(BluetoothDevice device) {
            Log.i(TAG, "---------- BLUETOOTH ----------");
            Log.i(TAG, "Method: onDeviceFound");
            Log.i(TAG, "Device: " + device.getName() + " " + device.getAddress());
            Log.i(TAG, "------------- END -------------");

            if (device.getAddress().equals(targetDeviceMacAddr)) {
                isFoundTargetDevice = true;

                if (!bluetooth.isBounded(device)) {
                    device.setPin(targetDevicePin.getBytes());
                    bluetooth.pair(device);
                } else {
                    bluetooth.connectToDevice(device);
                }
            }
        }

        @Override
        public void onDevicePaired(BluetoothDevice device) {
            Log.i(TAG, "---------- BLUETOOTH ----------");
            Log.i(TAG, "Method: onDevicePaired");
            Log.i(TAG, "Device: " + device.getName() + " " + device.getAddress());
            Log.i(TAG, "------------- END -------------");

            if (device.getAddress().equals(targetDeviceMacAddr)) {
                bluetooth.connectToDevice(device);
            } else {
                canceledWithResultCode(Search.PAIRED_WITH_UNKNOWN_DEVICE);
            }
        }

        @Override
        public void onDeviceUnpaired(BluetoothDevice device) {
            Log.i(TAG, "---------- BLUETOOTH ----------");
            Log.i(TAG, "Method: onDeviceUnpaired");
            Log.i(TAG, "Device: " + device.getName() + " " + device.getAddress());
            Log.i(TAG, "------------- END -------------");
        }

        @Override
        public void onError(int errorCode) {
            Log.i(TAG, "---------- BLUETOOTH ----------");
            Log.i(TAG, "Method: onError");
            Log.i(TAG, "Error code: " + errorCode);
            Log.i(TAG, "------------- END -------------");
        }
    };
}