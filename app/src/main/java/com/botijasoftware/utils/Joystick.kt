package com.botijasoftware.utils

import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent

import java.util.ArrayList

class Joystick(private val deviceid: Int) {

    private var enabled = false
    private var analogic = false
    private var joy_left = false
    private var joy_right = false
    private var joy_up = false
    private var joy_down = false
    private var btn_a = false
    private var btn_b = false
    private var btn_c = false
    private var btn_x = false
    private var btn_y = false
    private var btn_z = false
    private var btn_start = false
    private var btn_select = false
    private var btn_l1 = false
    private var btn_r1 = false
    private var btn_l2 = false
    private var btn_r2 = false
    private var btn_mode = false
    private var btn_thumbleft = false
    private var btn_thumbright = false

    private var trigger_right = 0.0f
    private var trigger_left = 0.0f
    var thumb_left_x = 0.0f
    var thumb_left_y = 0.0f
    private var thumb_right_x = 0.0f
    private val thumb_right_y = 0.0f

    init {

        val dev = InputDevice.getDevice(deviceid)
        val sources = dev.sources

        if (sources and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD) {
            enabled = true
            if (sources and InputDevice.SOURCE_JOYSTICK == InputDevice.SOURCE_JOYSTICK) {
                analogic = true
            }

        }
    }


    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        if (!enabled) return false

        if (event.deviceId == deviceid) {

            when (keyCode) {
                KeyEvent.KEYCODE_BUTTON_L1 -> btn_l1 = true
                KeyEvent.KEYCODE_BUTTON_R1 -> btn_r1 = true
                KeyEvent.KEYCODE_BUTTON_L2 -> btn_l2 = true
                KeyEvent.KEYCODE_BUTTON_R2 -> btn_r2 = true
                KeyEvent.KEYCODE_BUTTON_THUMBR -> btn_thumbright = true
                KeyEvent.KEYCODE_BUTTON_THUMBL -> btn_thumbleft = true
                KeyEvent.KEYCODE_DPAD_LEFT -> joy_left = true
                KeyEvent.KEYCODE_DPAD_RIGHT -> joy_right = true
                KeyEvent.KEYCODE_DPAD_UP -> joy_up = true
                KeyEvent.KEYCODE_DPAD_DOWN -> joy_down = true
                KeyEvent.KEYCODE_BUTTON_START -> btn_start = true
                KeyEvent.KEYCODE_BUTTON_SELECT -> btn_select = true
                KeyEvent.KEYCODE_BUTTON_MODE -> btn_mode = true
                KeyEvent.KEYCODE_BUTTON_A -> btn_a = true
                KeyEvent.KEYCODE_BUTTON_B -> btn_b = true
                KeyEvent.KEYCODE_BUTTON_C -> btn_c = true
                KeyEvent.KEYCODE_BUTTON_X -> btn_x = true
                KeyEvent.KEYCODE_BUTTON_Y -> btn_y = true
                KeyEvent.KEYCODE_BUTTON_Z -> btn_z = true
                else -> return false
            }
        }
        return false
    }

    fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {

        if (!enabled) return false

        if (event.deviceId == deviceid) {

            when (keyCode) {
                KeyEvent.KEYCODE_BUTTON_L1 -> btn_l1 = false
                KeyEvent.KEYCODE_BUTTON_R1 -> btn_r1 = false
                KeyEvent.KEYCODE_BUTTON_L2 -> btn_l2 = false
                KeyEvent.KEYCODE_BUTTON_R2 -> btn_r2 = false
                KeyEvent.KEYCODE_BUTTON_THUMBR -> btn_thumbright = false
                KeyEvent.KEYCODE_BUTTON_THUMBL -> btn_thumbleft = false
                KeyEvent.KEYCODE_DPAD_LEFT -> joy_left = false
                KeyEvent.KEYCODE_DPAD_RIGHT -> joy_right = false
                KeyEvent.KEYCODE_DPAD_UP -> joy_up = false
                KeyEvent.KEYCODE_DPAD_DOWN -> joy_down = false
                KeyEvent.KEYCODE_BUTTON_START -> btn_start = false
                KeyEvent.KEYCODE_BUTTON_SELECT -> btn_select = false
                KeyEvent.KEYCODE_BUTTON_MODE -> btn_mode = false
                KeyEvent.KEYCODE_BUTTON_A -> btn_a = false
                KeyEvent.KEYCODE_BUTTON_B -> btn_b = false
                KeyEvent.KEYCODE_BUTTON_C -> btn_c = false
                KeyEvent.KEYCODE_BUTTON_X -> btn_x = false
                KeyEvent.KEYCODE_BUTTON_Y -> btn_y = false
                KeyEvent.KEYCODE_BUTTON_Z -> btn_z = false
                else -> return false
            }
        }
        return false
    }

    fun onGenericMotionEvent(event: MotionEvent): Boolean {

        if (!enabled || !analogic) return false

        if (event.deviceId == deviceid) {
            trigger_right = event.getAxisValue(MotionEvent.AXIS_RTRIGGER)
            trigger_left = event.getAxisValue(MotionEvent.AXIS_LTRIGGER)

            thumb_left_x = event.x
            thumb_left_y = event.y

            thumb_right_x = event.getAxisValue(MotionEvent.AXIS_RZ)
            thumb_right_x = event.getAxisValue(MotionEvent.AXIS_Z)
            return true
        }

        return false
    }

    companion object {

        // Verify that the device has gamepad buttons, control sticks, or both.
        // This device is a game controller. Store its device ID.
        val gameControllerIds: ArrayList<Int>
            get() {
                val gameControllerDeviceIds = ArrayList<Int>()
                val deviceIds = InputDevice.getDeviceIds()
                for (deviceId in deviceIds) {
                    val dev = InputDevice.getDevice(deviceId)
                    val sources = dev.sources
                    if (sources and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD || sources and InputDevice.SOURCE_JOYSTICK == InputDevice.SOURCE_JOYSTICK) {
                        if (!gameControllerDeviceIds.contains(deviceId)) {
                            gameControllerDeviceIds.add(deviceId)
                        }
                    }
                }
                return gameControllerDeviceIds
            }
    }


}