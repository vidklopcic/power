package com.amir_p.power

import android.content.Context
import android.os.BatteryManager
import android.os.PowerManager
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** PowerPlugin */
public class PowerPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will handle communication between Flutter and native Android
    private lateinit var channel: MethodChannel
    private lateinit var context: Context

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "com.amir_p/power")
        context = flutterPluginBinding.applicationContext
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "getPowerMode" -> {
                val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                val powerSaveMode = powerManager.isPowerSaveMode
                result.success(powerSaveMode)
            }
            "getBatteryLevel" -> {
                val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
                val batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                result.success(batteryLevel)
            }
            "getChargingStatus" -> {
                val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
                result.success(batteryManager.isCharging)
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}