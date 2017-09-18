package com.example.xunhu.wifisensor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    IntentFilter filter;
    private WifiManager wifi;
    ListView listView;
    Button button;
    WifiP2pDevice device;
    WifiP2pConfig config = new WifiP2pConfig();
    ArrayAdapter adapter;
    OutputStream ServerOut = null;
    InputStream ServerIn = null;
    OutputStream ClientOut = null;
    InputStream ClientIn = null;
    private List peers = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.btnScan);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, peers);
        listView = (ListView) findViewById(R.id.lvPeers);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                connectToPeer(i);
            }
        });
        listView.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScanPeers();
            }
        });
        // create an instance of WiFip2pManager
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        // return a channel object used to connect your app to wifi p2p framework
        mChannel = mManager.initialize(this, getMainLooper(), null);
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        filter = new IntentFilter();
        //Broadcast when Wi-Fi P2P is enabled or disabled on the device.
        filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        //Broadcast when you call discoverPeers(). You usually want to call requestPeers() to get an updated list of peers if you handle this intent in your application.
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        //Broadcast when the state of the device's Wi-Fi connection changes.
        filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        //Broadcast when a device's details have changed, such as the device's name
        filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);
    }

    public void startScanPeers() {
        //start to scan whether there are any peer devices
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                System.out.println("@ successfully");
            }

            @Override
            public void onFailure(int i) {
                System.out.println("@ fail");
            }
        });
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                // Check to see if Wi-Fi is enabled and notify appropriate activity
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                } else {
                    wifi.setWifiEnabled(true);
                }
                System.out.println("@ WIFI_P2P_PEERS_CHANGED_ACTION");
            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                // Call WifiP2pManager.requestPeers() to get a list of current peers
                if (mManager != null) {
                    mManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                            peers.clear();
                            peers.addAll(wifiP2pDeviceList.getDeviceList());
                            adapter.notifyDataSetChanged();
                            for (int i = 0; i < peers.size(); i++) {
                                System.out.println("@ " + peers);
                            }
                            if (peers.size() == 0) {
                                System.out.println("@ none");
                                return;
                            }

                        }
                    });
                }
                System.out.println("@ WIFI_P2P_PEERS_CHANGED_ACTION");
            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                // Respond to new connection or disconnections
                if (mManager == null) {
                    return;
                }
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if (networkInfo.isConnected()) {
                    mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
                        @Override
                        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
                            final String groupOwnerAddress = wifiP2pInfo.groupOwnerAddress.getHostAddress();
                            System.out.println("@ isConnected " + groupOwnerAddress);
                            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                                System.out.println("@ groupOwner");
                                //create a server thread if the current device is a group owner
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            ServerSocket serverSocket = new ServerSocket(8888);
                                            String line = "";
                                            while (true) {
                                                System.out.println("@ w8 for connection");
                                                Socket client = serverSocket.accept();
                                                System.out.println("@ server connection successfully");
                                                InputStream inputStream = client.getInputStream();
                                                OutputStream outputStream = client.getOutputStream();
                                                ServerOut = outputStream;
                                                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                                                while ((line = br.readLine()) != null) {
                                                    System.out.println("@ message" + line);
                                                }
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            System.out.println("@ connection fail " + e.getMessage());
                                        }
                                    }
                                }).start();
                            } else if (wifiP2pInfo.groupFormed) {
                                System.out.println("@ not groupOwner");
                                //crreate a client thread if the current device is not a group owner
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String line = "";
                                        Socket socket = new Socket();
                                        try {
                                            socket.bind(null);
                                            socket.connect(new InetSocketAddress(groupOwnerAddress, 8888), 5000);
                                            System.out.println("@ start connecting to sever " + groupOwnerAddress);
                                            System.out.println("@ client connection successfully");
                                            InputStream inputStream = socket.getInputStream();
                                            OutputStream outputStream = socket.getOutputStream();
                                            ClientOut = outputStream;
                                            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                                            while ((line = br.readLine()) != null) {
                                                System.out.println("@ message" + line);
                                            }

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            System.out.println("@ connection fail " + e.getMessage());
                                        }
                                    }
                                }).start();
                            }

                        }
                    });
                    //mManager.requestConnectionInfo(mChannel, connectionListener);
                }
                System.out.println("@ WIFI_P2P_CONNECTION_CHANGED_ACTION");
            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                // Respond to this device's wifi state changing
                System.out.println("@ WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
            }
        }
    };

    public void connectToPeer(int position) {
        // retrieve the information of discovered peer devices
        device = (WifiP2pDevice) peers.get(position);
        WifiP2pConfig config = new WifiP2pConfig();
        // retrieve mac address
        config.deviceAddress = device.deviceAddress;

        // connect to that discovered peer device
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(MainActivity.this, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
