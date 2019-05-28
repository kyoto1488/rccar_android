package top.jewishcheck.radiocar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    final public int REQUEST_IMPORTANT_PERMISSIONS = 0x01;
    final public String[] importantPermissions = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_PRIVILEGED,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (hasImportantPermissions()) {
            startNextActivity();
        } else {
            requestImportantPermissions();
        }
    }

    /**
     * Request permission result
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_IMPORTANT_PERMISSIONS) {
            if (!hasImportantPermissions()) {
                startNextActivity();
                return;
            }

            requestImportantPermissions();
        }
    }

    /**
     * Check important permissions
     *
     * @return
     */
    private boolean hasImportantPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission : importantPermissions) {
                if (checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                    return false;
                }
            }

            return true;
        }

        return true;
    }

    /**
     * Start next activity
     */
    private void startNextActivity() {
        startActivity(
                new Intent(getApplicationContext(), SearchActivity.class)
        );
    }

    /**
     * Request permissions
     */
    private void requestImportantPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(importantPermissions, REQUEST_IMPORTANT_PERMISSIONS);
        }
    }
}
