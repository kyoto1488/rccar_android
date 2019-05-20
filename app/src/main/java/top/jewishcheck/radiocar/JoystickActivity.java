package top.jewishcheck.radiocar;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.erz.joysticklibrary.JoyStick;
import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.interfaces.BluetoothCallback;
import me.aflak.bluetooth.interfaces.DeviceCallback;
import me.aflak.bluetooth.interfaces.DiscoveryCallback;
import top.jewishcheck.radiocar.constants.Search;

public class JoystickActivity extends AppCompatActivity {
    private final String TAG = "JOYSTICK_ACTIVITY";
    private final int REQUEST_SEARCH_DEVICE = 0x0001;

    private boolean deviceConnected = false;

    private Bluetooth bluetooth;
    private JoyStick controlJoystickY;
    private JoyStick controlJoystickX;
    private ImageView buttonSwitchLed;
    private boolean ledOn = false;

    private JoyStick.JoyStickListener joyStickListenerX = new JoyStick.JoyStickListener() {
        @Override
        public void onMove(JoyStick joyStick, double angle, double power, int direction) {
            double _power = Math.ceil(Math.cos(angle) * power);
//            Log.i(TAG, "--------------- JoyStick listener X ---------------");
//            Log.i(TAG, "Angle: " + angle);
//            Log.i(TAG, "Power: " + power);
//            Log.i(TAG, "Direction: " + direction);
//            Log.i(TAG, "----------------------- END -----------------------");
            System.out.println("\"cmd:sr; data:" + angle + "," + power + "," + direction + ";\",");
        }

        @Override
        public void onTap() {

        }

        @Override
        public void onDoubleTap() {

        }
    };
    private JoyStick.JoyStickListener joyStickListenerY = new JoyStick.JoyStickListener() {
        @Override
        public void onMove(JoyStick joyStick, double angle, double power, int direction) {
            double _power = Math.ceil(Math.sin(angle) * power);

            System.out.println("_POWER: " + _power);
//            Log.i(TAG, "--------------- JoyStick listener Y ---------------");
//            Log.i(TAG, "Angle: " + angle);
//            Log.i(TAG, "Power: " + power);
//            Log.i(TAG, "Direction: " + direction);
//            Log.i(TAG, "----------------------- END -----------------------");

            //Log.i(TAG, "\"cmd:er; data:" + angle + "," + power + "," + direction + ";\"");
            System.out.println("\"cmd:er; data:" + angle + "," + power + "," + direction + ";\",");
        }

        @Override
        public void onTap() {

        }

        @Override
        public void onDoubleTap() {

        }
    };

    private BluetoothCallback bluetoothCallback = new BluetoothCallback() {
        @Override
        public void onBluetoothTurningOn() {

        }

        @Override
        public void onBluetoothOn() {

        }

        @Override
        public void onBluetoothTurningOff() {

        }

        @Override
        public void onBluetoothOff() {

        }

        @Override
        public void onUserDeniedActivation() {

        }
    };
    private DiscoveryCallback discoveryCallback = new DiscoveryCallback() {
        @Override
        public void onDiscoveryStarted() {

        }

        @Override
        public void onDiscoveryFinished() {

        }

        @Override
        public void onDeviceFound(BluetoothDevice device) {

        }

        @Override
        public void onDevicePaired(BluetoothDevice device) {

        }

        @Override
        public void onDeviceUnpaired(BluetoothDevice device) {

        }

        @Override
        public void onError(int errorCode) {

        }
    };
    private DeviceCallback deviceCallback = new DeviceCallback() {
        @Override
        public void onDeviceConnected(BluetoothDevice device) {

        }

        @Override
        public void onDeviceDisconnected(BluetoothDevice device, String message) {

        }

        @Override
        public void onMessage(String message) {

        }

        @Override
        public void onError(int errorCode) {

        }

        @Override
        public void onConnectError(BluetoothDevice device, String message) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);

        bluetooth = new Bluetooth(this);

        controlJoystickY = findViewById(R.id.controlJoystickY);
        controlJoystickY.enableStayPut(false);
        controlJoystickY.setType(JoyStick.TYPE_8_AXIS);
        controlJoystickY.setListener(joyStickListenerY);

        controlJoystickX = findViewById(R.id.controlJoystickX);
        controlJoystickX.enableStayPut(false);
        controlJoystickX.setType(JoyStick.TYPE_8_AXIS);
        controlJoystickX.setListener(joyStickListenerX);

        buttonSwitchLed = findViewById(R.id.buttonSwitchLed);

        buttonSwitchLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ledOn = !ledOn;

                buttonSwitchLed.setImageResource(ledOn ? R.drawable.ic_led_on : R.drawable.ic_led_off);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bluetooth.onStart();

        if (!deviceConnected) {
            //startActivitySearchDevice();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetooth.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SEARCH_DEVICE) {
            if (resultCode == RESULT_OK) {
                deviceConnected = true;
            } else if (resultCode == RESULT_CANCELED) {
                int resultCodeFromIntent = data.getIntExtra("resultCode", 0);

                switch (resultCodeFromIntent) {
                    case Search.RESULT_BLUETOOTH_OFF:
                        Utils.showLongToast(getApplicationContext(), "Bluetooth turning off! Good by!");
                        break;
                    case Search.RESULT_USER_DENIED_ACTIVATION:
                        Utils.showLongToast(getApplicationContext(), "Bluetooth device denied activation");
                        break;
                    case Search.PAIRED_WITH_UNKNOWN_DEVICE:
                        Utils.showLongToast(getApplicationContext(), "Paired with unknown device");
                        break;
                    case Search.CONNECTED_TO_UNKOWN_DEVICE:
                        Utils.showLongToast(getApplicationContext(), "Connected to unknown device");
                        break;
                }

                finish();
            }
        }
    }

    public void startActivitySearchDevice() {
        Intent searchActivityIntent = new Intent(this, SearchActivity.class);
        startActivityForResult(searchActivityIntent, REQUEST_SEARCH_DEVICE);
    }
}
