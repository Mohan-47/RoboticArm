package com.example.rawis

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.slider.RangeSlider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID
import java.io.OutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothSocket: BluetoothSocket

    private val requestEnableBluetoothLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            CoroutineScope(Dispatchers.IO).launch {
                connectToBluetoothDevice()
            }
        } else {
            Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show()
        }
    }
    private var isRunning = false // Variable to track the running state
    private val MIN_SLIDER_VALUE = 0.0 // Minimum value of the range slider
    private val MAX_SLIDER_VALUE = 1.0 // Maximum value of the range slider
    private val MIN_SERVO_POSITION = 0 // Minimum position for the servo motor
    private val MAX_SERVO_POSITION = 180 // Maximum position for the servo motor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //different slider controlled by servo motors
        val waistSlider = findViewById<RangeSlider>(R.id.waistSlider)!!
        val shoulderSlider = findViewById<RangeSlider>(R.id.shoulderSlider)!!
        val elbowSlider: RangeSlider = findViewById(R.id.elbowSlider)
        val wristRollSlider: RangeSlider = findViewById(R.id.wristRollSlider)
        val wristPitchSlider: RangeSlider = findViewById(R.id.wristPitchSlider)
        val gripSlider: RangeSlider = findViewById(R.id.gripSlider)

//        servo1PPos = 90; -- waist slider
//        servo2PPos = 150; -- shoulder
//        servo3PPos = 35; -- elbow
//        servo4PPos = 140; -- wrist Roll
//        servo5PPos = 85; -- wrist pitch
//        servo6PPos = 80; -- grip

        waistSlider.setValues(0.5F)
        shoulderSlider.setValues(0.833f)
        elbowSlider.setValues(0.194f)
        wristRollSlider.setValues(0.77f)
        wristPitchSlider.setValues(0.472f)
        gripSlider.setValues(0.444f)

        val saveButton = findViewById<Button>(R.id.Save)
        val connectButton = findViewById<Button>(R.id.connectBtn)
        val disconnectButton = findViewById<Button>(R.id.disconnectBtn)

        val runButton = findViewById<Button>(R.id.Run)

        val resetButton = findViewById<Button>(R.id.Reset)

//        rangeSlider1.addOnChangeListener { slider, value, fromUser ->
//            // Do something with the slider values for rangeSlider1.
//        }
//        rangeSlider2.addOnChangeListener { slider, value, fromUser ->
//            // Do something with the slider values for rangeSlider2.
//        }
        //initialise bluetooth adapter
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.getAdapter()

        connectButton.setOnClickListener {
            if (isBluetoothEnabled()) {
                CoroutineScope(Dispatchers.IO).launch {
                    connectToBluetoothDevice()
                }
            } else {
                requestEnableBluetooth()
            }
        }

        //popup message if bluetooth is off
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show()
            return
        }

        //enable bluetooth if not
        if (!bluetoothAdapter.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestEnableBluetoothLauncher.launch(enableBluetoothIntent)
        }
//        else {
//            connectToBluetoothDevice()
//        }
        //Set up Range Slidera
        waistSlider.addOnChangeListener { slider, value, fromUser ->
            val servoPosition = ((value - MIN_SLIDER_VALUE) / (MAX_SLIDER_VALUE - MIN_SLIDER_VALUE) * (MAX_SERVO_POSITION - MIN_SERVO_POSITION)).toInt() + MIN_SERVO_POSITION
            val position = servoPosition.toInt()
            sendServoPosition("s1", position)
        }
        shoulderSlider.addOnChangeListener { slider, value, fromUser ->
            val servoPosition = ((value - MIN_SLIDER_VALUE) / (MAX_SLIDER_VALUE - MIN_SLIDER_VALUE) * (MAX_SERVO_POSITION - MIN_SERVO_POSITION)).toInt() + MIN_SERVO_POSITION
            val position = servoPosition.toInt()
            sendServoPosition("s2", position)
        }
        elbowSlider.addOnChangeListener { slider, value, fromUser ->
            val servoPosition = ((value - MIN_SLIDER_VALUE) / (MAX_SLIDER_VALUE - MIN_SLIDER_VALUE) * (MAX_SERVO_POSITION - MIN_SERVO_POSITION)).toInt() + MIN_SERVO_POSITION
            val position = servoPosition.toInt()
            sendServoPosition("s3", position)
        }
        wristRollSlider.addOnChangeListener { slider, value, fromUser ->
            val servoPosition = ((value - MIN_SLIDER_VALUE) / (MAX_SLIDER_VALUE - MIN_SLIDER_VALUE) * (MAX_SERVO_POSITION - MIN_SERVO_POSITION)).toInt() + MIN_SERVO_POSITION
            val position = servoPosition.toInt()
            sendServoPosition("s4", position)
        }
        wristPitchSlider.addOnChangeListener { slider, value, fromUser ->
            val servoPosition = ((value - MIN_SLIDER_VALUE) / (MAX_SLIDER_VALUE - MIN_SLIDER_VALUE) * (MAX_SERVO_POSITION - MIN_SERVO_POSITION)).toInt() + MIN_SERVO_POSITION
            val position = servoPosition.toInt()
            sendServoPosition("s5", position)
        }
        gripSlider.addOnChangeListener { slider, value, fromUser ->
            val servoPosition = ((value - MIN_SLIDER_VALUE) / (MAX_SLIDER_VALUE - MIN_SLIDER_VALUE) * (MAX_SERVO_POSITION - MIN_SERVO_POSITION)).toInt() + MIN_SERVO_POSITION
            val position = servoPosition.toInt()
            sendServoPosition("s6", position)
        }

        saveButton.setOnClickListener {
            sendServoPosition("SAVE", 0) // Send "SAVE" command to Arduino
        }

        runButton.setOnClickListener {
            if (isRunning) {
                isRunning = false // Set the running state to false
                runButton.text = getString(R.string.run) // Change the button text to "Run"
                sendServoPosition("PAUSE", 0)
            } else {
                isRunning = true // Set the running state to true
                runButton.text = getString(R.string.pause) // Change the button text to "Pause"
                sendServoPosition("RUN",0)
            }
        }

        resetButton.setOnClickListener {
            sendServoPosition("RESET",0)
            waistSlider.setValues(0.5F)
            shoulderSlider.setValues(0.833f)
            elbowSlider.setValues(0.194f)
            wristRollSlider.setValues(0.77f)
            wristPitchSlider.setValues(0.472f)
            gripSlider.setValues(0.444f)
        }


        disconnectButton.setOnClickListener {
            disconnectBluetoothDevice()
        }
    }

    private fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter.isEnabled
    }

    private fun requestEnableBluetooth() {
        val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        requestEnableBluetoothLauncher.launch(enableBluetoothIntent)
    }

    private fun sendServoPosition(servoNumber: String, position: Int) {
        val outputStream: OutputStream
        try {
            if (servoNumber == "SAVE" || servoNumber == "RUN" || servoNumber=="PAUSE" || servoNumber == "RESET") {
                val message = servoNumber // Only send the command without position for saving
                val bytes = message.encodeToByteArray()
                outputStream = bluetoothSocket.outputStream
                outputStream.write(bytes)
            } else {
                val message = "$servoNumber$position" // Format the message to include servo number and position
                val bytes = message.encodeToByteArray()
                outputStream = bluetoothSocket.outputStream
                outputStream.write(bytes)
            }
        } catch (e: IOException) {
            Toast.makeText(this, "Failed to send position", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private suspend fun connectToBluetoothDevice(){
            // Get the Bluetooth device with HC-05's MAC address
        withContext(Dispatchers.Main) {
            Toast.makeText(this@MainActivity,"Connecting to Bluetooth device...",Toast.LENGTH_LONG)
        }
            val device = bluetoothAdapter.getRemoteDevice(HC05_MAC_ADDRESS)
        if (ActivityCompat.checkSelfPermission(
                        this,
//                        Manifest.permission.BLUETOOTH_CONNECT
                        Manifest.permission.BLUETOOTH
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    //    ActivityCompat#requestPermissions
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.BLUETOOTH),
                        REQUEST_ENABLE_BLUETOOTH
                    )
                    return
                }
        // Check if the Bluetooth device is available
        if (device == null) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "Bluetooth device not found", Toast.LENGTH_SHORT).show()
                return@withContext
            }
        }

        // Create a Bluetooth socket
            withContext(Dispatchers.IO){
                try {
                    bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID)
                    bluetoothSocket.connect()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Connected to HC-05", Toast.LENGTH_SHORT).show()
                    }
                    // Start sending/receiving data through the Bluetooth connection
                    // You can use the input/output streams of the Bluetooth socket to send/receive data
                } catch (e: IOException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Failed to connect to HC-05", Toast.LENGTH_SHORT).show()
                    }
                    e.printStackTrace()
                }
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // The permission was granted, so you can connect to the Bluetooth device
                try {
                    CoroutineScope(Dispatchers.IO).launch {
                        connectToBluetoothDevice()
                    }
                } catch (e:IOException) {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            } else {
                // The permission was denied, so you cannot connect to the Bluetooth device
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun disconnectBluetoothDevice() {
        try {
            bluetoothSocket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Close the Bluetooth socket when the activity is destroyed
        try {
            bluetoothSocket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    companion object {
        private const val REQUEST_ENABLE_BLUETOOTH = 1
        private const val HC05_MAC_ADDRESS = "" // Replace with your HC-05 MAC address
        private val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // Standard UUID for SPP
    }
}