
#include <Servo.h>

Servo servo01;
Servo servo02;
Servo servo03;
Servo servo04;
Servo servo05;
Servo servo06;


int servo1Pos, servo2Pos, servo3Pos, servo4Pos, servo5Pos, servo6Pos; // current position
int servo1PPos, servo2PPos, servo3PPos, servo4PPos, servo5PPos, servo6PPos; // previous position
int servo01SP[50], servo02SP[50], servo03SP[50], servo04SP[50], servo05SP[50], servo06SP[50]; // for storing positions/steps
int speedDelay = 20;
int index = 0;
String dataIn = "";
String dataPos = "";
int lastIndex = 0;

void setup() {
  Serial.begin(9600);
  Serial1.begin(9600);
  servo01.attach(5);
  servo02.attach(6);
  servo03.attach(7);
  servo04.attach(8);
  servo05.attach(9);
  servo06.attach(10);
  delay(500);

  delay(20);
  // Robot arm initial position
  servo1PPos = 0;
  servo2PPos = 0;
  servo3PPos = 0;
  servo4PPos = 0;
  servo5PPos = 0;
  servo6PPos = 0;



  Serial.println("setup complete");
}

void loop() {
  // Check for incoming data
  if (Serial1.available() > 0) {
    Serial.println("Bluetooth connected");
    dataPos = Serial1.readString();  // Read the data as string
    Serial.print("Recieved data:");
    Serial.println(String(dataPos));



    // If "Waist" slider has changed value - Move Servo 1 to position
    if (dataPos.startsWith("s1")) {

      lastIndex = dataPos.lastIndexOf('s');
      if ( lastIndex != -1) {
        dataIn  = dataPos.substring(lastIndex);
      }
      Serial.println(String(dataIn));
      String dataInS = dataIn.substring(2, dataIn.length()); // Extract only the number. E.g. from "s1120" to "120"
      servo1Pos = dataInS.toInt();  // Convert the string into integer
      Serial.print("Position:");
      Serial.println(String(servo1Pos));
      // We use for loops so we can control the speed of the servo
      servo01.write(servo1Pos);
      
      servo1PPos = servo1Pos; // set current position as previous position
      dataPos = "";
      delay(100);
    }

    // Move Servo 2
    if (dataPos.startsWith("s2")) {

      lastIndex = dataPos.lastIndexOf('s');
      if ( lastIndex != -1) {
        dataIn  = dataPos.substring(lastIndex);
      }
      Serial.println(String(dataIn));
      String dataInS = dataIn.substring(2, dataIn.length());
      servo2Pos = dataInS.toInt();
      Serial.print("Position:");
      Serial.println(String(servo2Pos));

      if (servo2PPos > servo2Pos) {
        for ( int j = servo2PPos; j >= servo2Pos; j--) {
          servo02.write(j);
          delay(50);
        }
      }
      if (servo2PPos < servo2Pos) {
        for ( int j = servo2PPos; j <= servo2Pos; j++) {
          servo02.write(j);
          delay(50);
        }
      }
      servo2PPos = servo2Pos;
      dataPos = "";
      delay(100);
    }
    // Move Servo 3
    if (dataPos.startsWith("s3")) {

      lastIndex = dataPos.lastIndexOf('s');
      if ( lastIndex != -1) {
        dataIn  = dataPos.substring(lastIndex);
      }
      Serial.println(String(dataIn));
      String dataInS = dataIn.substring(2, dataIn.length());
      servo3Pos = dataInS.toInt();
      Serial.print("Position:");
      Serial.println(String(servo3Pos));
      if (servo3PPos > servo3Pos) {
        for ( int j = servo3PPos; j >= servo3Pos; j--) {
          servo03.write(j);
          delay(50);
        }
      }
      if (servo3PPos < servo3Pos) {
        for ( int j = servo3PPos; j <= servo3Pos; j++) {
          servo03.write(j);
          delay(50);
        }
      }
      servo3PPos = servo3Pos;
      dataPos = "";
      delay(100);
    }
    // Move Servo 4
    if (dataPos.startsWith("s4")) {

      lastIndex = dataPos.lastIndexOf('s');
      if ( lastIndex != -1) {
        dataIn  = dataPos.substring(lastIndex);
      }
      Serial.println(String(dataIn));
      String dataInS = dataIn.substring(2, dataIn.length());
      servo4Pos = dataInS.toInt();
      Serial.print("Position:");
      Serial.println(String(servo4Pos));
      if (servo4PPos > servo4Pos) {
        for ( int j = servo4PPos; j >= servo4Pos; j--) {
          servo04.write(j);
          delay(50);
        }
      }
      if (servo4PPos < servo4Pos) {
        for ( int j = servo4PPos; j <= servo4Pos; j++) {
          servo04.write(j);
          delay(50);
        }
      }
      servo4PPos = servo4Pos;
      dataPos = "";
      delay(100);
    }
    // Move Servo 5
    if (dataPos.startsWith("s5")) {

      lastIndex = dataPos.lastIndexOf('s');
      if ( lastIndex != -1) {
        dataIn  = dataPos.substring(lastIndex);
      }
      Serial.println(String(dataIn));
      String dataInS = dataIn.substring(2, dataIn.length());
      servo5Pos = dataInS.toInt();
      Serial.print("Position:");
      Serial.println(String(servo5Pos));
      if (servo5PPos > servo5Pos) {
        for ( int j = servo5PPos; j >= servo5Pos; j--) {
          servo05.write(j);
          delay(50);
        }
      }
      if (servo5PPos < servo5Pos) {
        for ( int j = servo5PPos; j <= servo5Pos; j++) {
          servo05.write(j);
          delay(50);
        }
      }
      servo5PPos = servo5Pos;
      dataPos = "";
      delay(100);
    }
    // Move Servo 6
    if (dataPos.startsWith("s6")) {

      lastIndex = dataPos.lastIndexOf('s');
      if ( lastIndex != -1) {
        dataIn  = dataPos.substring(lastIndex);
      }
      Serial.println(String(dataIn));
      String dataInS = dataIn.substring(2, dataIn.length());
      servo6Pos = dataInS.toInt();
      Serial.print("Position:");
      Serial.println(String(servo6Pos));

      servo06.write(servo6Pos);
      servo6PPos = servo6Pos;
      dataPos = "";
      delay(100);
    }
    // If button "SAVE" is pressed
    if (dataPos.startsWith("SAVE")) {
      servo01SP[index] = servo1PPos;  // save position into the array
      servo02SP[index] = servo2PPos;
      servo03SP[index] = servo3PPos;
      servo04SP[index] = servo4PPos;
      servo05SP[index] = servo5PPos;
      servo06SP[index] = servo6PPos;
      index++;   // Increase the array index

      Serial.println("saved");
    }
    // If button "RUN" is pressed
    if (dataPos.startsWith("RUN")) {
      Serial.println("Run");
      runservo();  // Automatic mode - run the saved steps
    }
    // If button "RESET" is pressed
    if ( dataPos.startsWith("RESET")) {
      memset(servo01SP, 0, sizeof(servo01SP)); // Clear the array data to 0
      memset(servo02SP, 0, sizeof(servo02SP));
      memset(servo03SP, 0, sizeof(servo03SP));
      memset(servo04SP, 0, sizeof(servo04SP));
      memset(servo05SP, 0, sizeof(servo05SP));
      memset(servo06SP, 0, sizeof(servo06SP));
      index = 0;  // Index to 0
      Serial.println("Reset");
    }
  }
  dataPos = "";
}

// Automatic mode custom function - run the saved steps
void runservo() {
  while (!dataPos.startsWith("RESET")) {
    if (Serial1.available() > 0) {
      dataPos = Serial1.readString();
      if (dataPos == "PAUSE") {
        Serial.println("Paused");
        while (dataPos != "RUN" && !dataPos.startsWith("RESET")) {
          if (Serial1.available() > 0) {
            dataPos = Serial1.readString();
          }
        }
      } else if (dataPos == "RUN") {
        Serial.println("Run clicked");
        for (int i = 0; i <= index - 1; i++) {
          // Set servo angles based on saved positions
          servo01.write(servo01SP[i]);
          servo02.write(servo02SP[i]);
          servo03.write(servo03SP[i]);
          servo04.write(servo04SP[i]);
          servo05.write(servo05SP[i]);
          servo06.write(servo06SP[i]);

          delay(1000);  // Delay between each step, adjust as needed
        }
      } else if (dataPos.startsWith("RESET")) {
        break;
      }
    }
  }
}
