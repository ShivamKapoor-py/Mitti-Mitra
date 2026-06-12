package com.example.mittimitra;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothService {

    private static final String TAG = "BluetoothService";
    private static final UUID SPP_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final Activity activity;
    private final Callback callback;
    private final BluetoothAdapter adapter;

    private BluetoothSocket socket;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;

    public interface Callback {
        void onConnectionState(boolean connected, String message);
        void onNewReading(SensorReading reading);
    }

    public BluetoothService(Activity activity, Callback callback) {
        this.activity = activity;
        this.callback = callback;
        this.adapter = BluetoothAdapter.getDefaultAdapter();
    }

    public BluetoothAdapter getAdapter() {
        return adapter;
    }

    /** Find paired device by name */
    public BluetoothDevice findPairedDeviceByName(String nameFragment) {
        if (adapter == null) return null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT)
                        != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "Missing BLUETOOTH_CONNECT permission");
            return null;
        }

        Set<BluetoothDevice> paired = adapter.getBondedDevices();
        for (BluetoothDevice d : paired) {
            String name = d.getName();
            if (name != null && name.toLowerCase().contains(nameFragment.toLowerCase())) {
                return d;
            }
        }
        return null;
    }

    /** Connect using MAC address or device with optional fallback */
    public void connectWithFallback(BluetoothDevice device) {
        cancel();
        connectThread = new ConnectThread(device, true);
        connectThread.start();
    }

    public void cancel() {
        try {
            if (connectThread != null) {
                connectThread.cancel();
                connectThread = null;
            }
            if (connectedThread != null) {
                connectedThread.cancel();
                connectedThread = null;
            }
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            Log.e(TAG, "cancel error", e);
        }
    }

    private void manageConnectedSocket(BluetoothSocket sock) {
        socket = sock;
        connectedThread = new ConnectedThread(sock);
        connectedThread.start();
        if (callback != null) callback.onConnectionState(true, "Connected");
    }

    private class ConnectThread extends Thread {
        private final BluetoothDevice device;
        private final boolean useFallback;
        private BluetoothSocket mmSocket;

        ConnectThread(BluetoothDevice device, boolean useFallback) {
            this.device = device;
            this.useFallback = useFallback;
        }

        public void run() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT)
                            != PackageManager.PERMISSION_GRANTED) {
                if (callback != null) callback.onConnectionState(false, "Missing BLUETOOTH_CONNECT permission");
                return;
            }

            try {
                mmSocket = device.createRfcommSocketToServiceRecord(SPP_UUID);
                if (adapter != null) adapter.cancelDiscovery();

                try {
                    mmSocket.connect();
                } catch (IOException e) {
                    Log.w(TAG, "Standard connect failed, trying fallback", e);
                    if (useFallback) {
                        try {
                            java.lang.reflect.Method m = device.getClass().getMethod("createRfcommSocket", int.class);
                            mmSocket = (BluetoothSocket) m.invoke(device, 1);
                            mmSocket.connect();
                        } catch (Exception e2) {
                            Log.e(TAG, "Fallback connect failed", e2);
                            if (callback != null) callback.onConnectionState(false, "Connection failed");
                            return;
                        }
                    } else {
                        if (callback != null) callback.onConnectionState(false, "Connection failed");
                        return;
                    }
                }
                manageConnectedSocket(mmSocket);
            } catch (Exception e) {
                Log.e(TAG, "ConnectThread error", e);
                if (callback != null) callback.onConnectionState(false, "Connection failed");
            }
        }

        public void cancel() {
            try { if (mmSocket != null) mmSocket.close(); } catch (IOException ignored) {}
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream inStream;
        private final OutputStream outStream;
        private BufferedReader reader;

        ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { Log.e(TAG, "Getting streams", e); }

            inStream = tmpIn;
            outStream = tmpOut;

            if (inStream != null) reader = new BufferedReader(new InputStreamReader(inStream));
        }

        public void run() {
            String line;
            try {
                while (reader != null && (line = reader.readLine()) != null) {
                    Log.d(TAG, "Received: " + line);
                    SensorReading r = parseLine(line);
                    if (r != null && callback != null) callback.onNewReading(r);
                }
            } catch (IOException e) {
                Log.e(TAG, "Disconnected", e);
                if (callback != null) callback.onConnectionState(false, "Disconnected");
            }
        }

        public void write(byte[] bytes) {
            try { if (outStream != null) outStream.write(bytes); } catch (IOException e) { Log.e(TAG, "Write error", e); }
        }

        public void cancel() {
            try { if (inStream != null) inStream.close(); if (outStream != null) outStream.close(); } catch (IOException ignored) {}
        }
    }

    /** Parse ESP32 sensor string */
    private SensorReading parseLine(String line) {
        if (line == null) return null;
        line = line.trim();
        SensorReading r = new SensorReading();

        try {
            // allow either | or , as separators
            String[] pairs = line.split("\\s*[|,]\\s*");
            for (String p : pairs) {
                String[] kv = p.split("\\s*:\\s*");
                if (kv.length < 2) continue;
                String k = kv[0].trim().toLowerCase();
                String v = kv[1].trim();

                // strip any units like "ppm"
                v = v.replaceAll("[^0-9.]+", "");

                if (k.contains("moisture")) r.moisture = v;
                else if (k.contains("tds")) r.tds = v;
                else if (k.contains("humidity")) r.humidity = v;
                else if (k.contains("ph")) r.ph = v;
                else if (k.contains("rain")) r.rain = v;
            }
            return r;
        } catch (Exception e) {
            Log.e(TAG, "Parse error", e);
            return null;
        }
    }


    public static class SensorReading {
        public String moisture = "";
        public String tds = "";
        public String humidity = "";
        public String ph = "";
        public String rain = "";
    }
}
