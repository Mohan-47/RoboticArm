# Smartphone controlled - Robotic Arm

Robotic Arm with Smartphone Control - contains complete source for the Arduino and the android app.The project allows for the wireless control of a robotic arm using an Android application via Bluetooth communication with an Arduino microcontroller.

## Project Structure

- **Arduino**: Contains the code for controlling the robotic arm using an Arduino microcontroller.
- **Android_App**: Contains the Android application code that interfaces with the Arduino to control the robotic arm.

## Arduino Code

The Arduino code uses several servo motors to control the robotic arm's movement. The servos are controlled via Bluetooth using an HC-05 Bluetooth module.

### Dependencies

- Servo library
- SoftwareSerial library

### Setup

1. Connect the servo motors to the Arduino as per the pin configuration in the code.
2. Pair the HC-05 Bluetooth module with your Android device.
3. Upload the Arduino code to your Arduino board.

### Usage

Once the Arduino code is uploaded and the Bluetooth module is paired with your Android device, you can control the robotic arm using the Android application.

## Android Application

The Android application allows you to control the robotic arm wirelessly via Bluetooth.
The android app is written in Kotlin and the source code can be found in MainActivity.kt which can be found in RAwiS_App\app\src\main\java\com\example\rawis. 

### Dependencies

- Android SDK
- Bluetooth permissions in AndroidManifest.xml

### Usage

Use the sliders and buttons in the Android application to control the servos of the robotic arm. The application communicates with the Arduino via Bluetooth to send control commands.

## Acknowledgments
Thanks to the open-source community for providing libraries and resources that made this project possible.
