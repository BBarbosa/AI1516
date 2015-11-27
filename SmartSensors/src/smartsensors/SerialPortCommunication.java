package smartsensors;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

public class SerialPortCommunication {

  //SerialPort
  public SerialPort serialPort;
  //The port we're normally going to use.
  private static final String PORT_NAMES[] = {
      "/dev/cu.usbmodem1421"
      //"/dev/tty.usbmodem"
      //"/dev/tty.usbserial-A9007UX1" // Mac OS X
      //"/dev/ttyUSB0", // Linux
      //"COM4", // Windows
  };
  //input and output Stream
  private BufferedReader input;
  private OutputStream output;
  //Milliseconds to block while waiting for port open
  public static final int TIME_OUT = 1000;
  //Default bits per second for COM port.
  public static final int DATA_RATE = 9600;
  
  
  public void initialize() {
      
    CommPortIdentifier portId = null;
    Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
    //First, Find an instance of serial port as set in PORT_NAMES.
    while (portEnum.hasMoreElements()) {
       CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
       for (String portName : PORT_NAMES) {
         if (currPortId.getName().equals(portName)) {
            portId = currPortId;
            System.out.println( "Connected on port" + currPortId.getName() );
            break;
         }
       }
    }
    if (portId == null) {
       System.out.println("Could not find port.");
       return;
    }
    try {
       // open serial port, and use class name for the appName.
       serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
       // set port parameters
       serialPort.setSerialPortParams(DATA_RATE,
            SerialPort.DATABITS_8,
            SerialPort.STOPBITS_1,
            SerialPort.PARITY_NONE);
       // open the streams for reading and writing in the serial port
       input = new BufferedReader(new 
               InputStreamReader(serialPort.getInputStream()));
       output = serialPort.getOutputStream();
    } catch (Exception e) {
      System.err.println(e.toString());
    }
    }
  
    
    public void writeData(int data) {
        try {
            //write data in the serial port
            //output.write(data.getBytes());
            output.write(data);
        } catch (Exception e) {
            System.out.println("could not write to port");
        }
    }
  

    public String getData() {
        String data = null;
        try {
            //read data from the serial port
            data = input.readLine();
        } catch (Exception e) {
            return e.getMessage();
        }
        return data;
    }
}
