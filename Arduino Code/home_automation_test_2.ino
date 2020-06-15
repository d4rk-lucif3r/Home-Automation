#define RELAY_ON 0
#define RELAY_OFF 1

#define RELAY_1  8
#define RELAY_2  9
#define RELAY_3  10
#define RELAY_4  11
char data = 0;

void setup() {

// Set pin as output.

pinMode(RELAY_1, OUTPUT);
pinMode(RELAY_2, OUTPUT);
pinMode(RELAY_3, OUTPUT);
pinMode(RELAY_4, OUTPUT);

// Initialize RELAYs = off. So that on reset it would be off by default

digitalWrite(RELAY_1, RELAY_OFF);
digitalWrite(RELAY_2, RELAY_OFF);
digitalWrite(RELAY_3, RELAY_OFF);
digitalWrite(RELAY_4, RELAY_OFF);
Serial.begin(9600);

Serial.print("Home Automation By Arsh Anwar");

}

void loop() {

 if (Serial.available() > 0) {

data = Serial.read();      //Read the incoming data and store it into variable data

  Serial.print(data);        //Print Value inside data in Serial monitor

Serial.print("\n");        //New line

if(data == '1'){

digitalWrite(RELAY_1, RELAY_ON);

Serial.println("Device 1 is on");

}
if(data == '2'){

digitalWrite(RELAY_2, RELAY_ON);

Serial.println("Device 2 is on");

}
if(data == '3'){

digitalWrite(RELAY_3, RELAY_ON);

Serial.println("Device 3 is on");

}
if(data == '4'){

digitalWrite(RELAY_4, RELAY_ON);

Serial.println("Device 4 is on");

}

else if(data == 'a')
{

digitalWrite(RELAY_1, RELAY_OFF);

Serial.println("Device 1 is off");
}
else if(data == 'b')
{

digitalWrite(RELAY_2, RELAY_OFF);

Serial.println("Device 2 is off");
}
else if(data == 'c')
{

digitalWrite(RELAY_3, RELAY_OFF);

Serial.println("Device 3 is off");
}
else if(data == 'd')
{

digitalWrite(RELAY_4, RELAY_OFF);

Serial.println("Device 4 is off");
}
else if(data == '0')
{
digitalWrite(RELAY_1, RELAY_OFF);
digitalWrite(RELAY_2, RELAY_OFF);  
digitalWrite(RELAY_3, RELAY_OFF);
digitalWrite(RELAY_4, RELAY_OFF);

Serial.println("Devices are off");
}
}

}
