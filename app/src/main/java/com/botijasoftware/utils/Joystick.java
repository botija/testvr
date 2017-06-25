package com.botijasoftware.utils;

import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;

public class Joystick {

    private boolean enabled = false;
    private boolean analogic = false;
    private boolean joy_left = false;
    private boolean joy_right = false;
    private boolean joy_up = false;
    private boolean joy_down = false;
    private boolean btn_a = false;
    private boolean btn_b = false;
    private boolean btn_c = false;
    private boolean btn_x = false;
    private boolean btn_y = false;
    private boolean btn_z = false;
    private boolean btn_start = false;
    private boolean btn_select = false;
    private boolean btn_l1 = false;
    private boolean btn_r1 = false;
    private boolean btn_l2 = false;
    private boolean btn_r2 = false;
    private boolean btn_mode = false;
    private boolean btn_thumbleft = false;
    private boolean btn_thumbright = false;

    private float trigger_right = 0.0f;
    private float trigger_left = 0.0f;
    public float thumb_left_x = 0.0f;
    public float thumb_left_y = 0.0f;
    private float thumb_right_x = 0.0f;
    private float thumb_right_y = 0.0f;

    private int deviceid;

    public Joystick(int deviceid) {
        this.deviceid = deviceid;

        InputDevice dev = InputDevice.getDevice(deviceid);
        int sources = dev.getSources();

        if ((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) {
            enabled = true;
            if ((sources & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK) {
                analogic = true;
            }

        }
    }

    public static ArrayList getGameControllerIds() {
        ArrayList<Integer> gameControllerDeviceIds = new ArrayList<>();
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();

            // Verify that the device has gamepad buttons, control sticks, or both.
            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                    || ((sources & InputDevice.SOURCE_JOYSTICK)
                    == InputDevice.SOURCE_JOYSTICK)) {
                // This device is a game controller. Store its device ID.
                if (!gameControllerDeviceIds.contains(deviceId)) {
                    gameControllerDeviceIds.add(deviceId);
                }
            }
        }
        return gameControllerDeviceIds;
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (!enabled) return false;

        if (event.getDeviceId() == deviceid) {

            switch (keyCode) {
                case KeyEvent.KEYCODE_BUTTON_L1:
                    btn_l1 = true;
                    break;
                case KeyEvent.KEYCODE_BUTTON_R1:
                    btn_r1 = true;
                    break;
                case KeyEvent.KEYCODE_BUTTON_L2:
                    btn_l2 = true;
                    break;
                case KeyEvent.KEYCODE_BUTTON_R2:
                    btn_r2 = true;
                    break;
                case KeyEvent.KEYCODE_BUTTON_THUMBR:
                    btn_thumbright = true;
                    break;
                case KeyEvent.KEYCODE_BUTTON_THUMBL:
                    btn_thumbleft = true;
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    joy_left = true;
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    joy_right = true;
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    joy_up = true;
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    joy_down = true;
                    break;
                case KeyEvent.KEYCODE_BUTTON_START:
                    btn_start = true;
                    break;
                case KeyEvent.KEYCODE_BUTTON_SELECT:
                    btn_select = true;
                    break;
                case KeyEvent.KEYCODE_BUTTON_MODE:
                    btn_mode = true;
                    break;
                case KeyEvent.KEYCODE_BUTTON_A:
                    btn_a = true;
                    break;
                case KeyEvent.KEYCODE_BUTTON_B:
                    btn_b = true;
                    break;
                case KeyEvent.KEYCODE_BUTTON_C:
                    btn_c = true;
                    break;
                case KeyEvent.KEYCODE_BUTTON_X:
                    btn_x = true;
                    break;
                case KeyEvent.KEYCODE_BUTTON_Y:
                    btn_y = true;
                    break;
                case KeyEvent.KEYCODE_BUTTON_Z:
                    btn_z = true;
                    break;
                default:
                    return false;
            }
        }
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (!enabled) return false;

        if (event.getDeviceId() == deviceid) {

            switch (keyCode) {
                case KeyEvent.KEYCODE_BUTTON_L1:
                    btn_l1 = false;
                    break;
                case KeyEvent.KEYCODE_BUTTON_R1:
                    btn_r1 = false;
                    break;
                case KeyEvent.KEYCODE_BUTTON_L2:
                    btn_l2 = false;
                    break;
                case KeyEvent.KEYCODE_BUTTON_R2:
                    btn_r2 = false;
                    break;
                case KeyEvent.KEYCODE_BUTTON_THUMBR:
                    btn_thumbright = false;
                    break;
                case KeyEvent.KEYCODE_BUTTON_THUMBL:
                    btn_thumbleft = false;
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    joy_left = false;
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    joy_right = false;
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    joy_up = false;
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    joy_down = false;
                    break;
                case KeyEvent.KEYCODE_BUTTON_START:
                    btn_start = false;
                    break;
                case KeyEvent.KEYCODE_BUTTON_SELECT:
                    btn_select = false;
                    break;
                case KeyEvent.KEYCODE_BUTTON_MODE:
                    btn_mode = false;
                    break;
                case KeyEvent.KEYCODE_BUTTON_A:
                    btn_a = false;
                    break;
                case KeyEvent.KEYCODE_BUTTON_B:
                    btn_b = false;
                    break;
                case KeyEvent.KEYCODE_BUTTON_C:
                    btn_c = false;
                    break;
                case KeyEvent.KEYCODE_BUTTON_X:
                    btn_x = false;
                    break;
                case KeyEvent.KEYCODE_BUTTON_Y:
                    btn_y = false;
                    break;
                case KeyEvent.KEYCODE_BUTTON_Z:
                    btn_z = false;
                    break;
                default:
                    return false;
            }
        }
            return false;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {

        if (!enabled || !analogic ) return false;

        if (event.getDeviceId() == deviceid) {
            trigger_right = event.getAxisValue(MotionEvent.AXIS_RTRIGGER);
            trigger_left = event.getAxisValue(MotionEvent.AXIS_LTRIGGER);

            thumb_left_x = event.getX();
            thumb_left_y = event.getY();

            thumb_right_x = event.getAxisValue(MotionEvent.AXIS_RZ);
            thumb_right_x = event.getAxisValue(MotionEvent.AXIS_Z);
            return true;
        }

        return false;
    }





}