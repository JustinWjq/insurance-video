package com.txt.video.common.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.media.AudioManager;

/**
 * author ：Justin
 * time ：2022/10/28.
 * des ：
 */
class CommonUtils {
    public int getHeadSetStatus(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE); // 获取声音管理器
        if (audioManager.isWiredHeadsetOn()) { // 有限耳机是否连接
            return 1;
        }
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // 蓝牙耳机
        if (bluetoothAdapter == null) { // 若蓝牙耳机无连接
            return -1;
        } else if (bluetoothAdapter.isEnabled()) {
            int a2dp = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP); // 可操控蓝牙设备，如带播放暂停功能的蓝牙耳机
            int headset = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET); // 蓝牙头戴式耳机，支持语音输入输出
            int health = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH); // 蓝牙穿戴式设备

            // 查看是否蓝牙是否连接到三种设备的一种，以此来判断是否处于连接状态还是打开并没有连接的状态
            int flag = -1;
            if (a2dp == BluetoothProfile.STATE_CONNECTED) {
                flag = a2dp;
            } else if (headset == BluetoothProfile.STATE_CONNECTED) {
                flag = headset;
            } else if (health == BluetoothProfile.STATE_CONNECTED) {
                flag = health;
            }
            // 说明连接上了三种设备的一种
            if (flag != -1) {
                return 2;
            }
        }
        return -2;
    }
}
