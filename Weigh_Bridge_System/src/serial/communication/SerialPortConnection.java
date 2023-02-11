package serial.communication;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import database.SerialPortSettings;

public class SerialPortConnection {

    private static SerialPort[] serialPorts;
    static SerialPort selectedSerialPort;
    private static final char DELIMITER = '=';

    public static boolean serialPortConnected = false;
    private static Integer scaleWeight;

    public static boolean connectSerialPort(int serialPortIndex, int baudRate, int dataBits, int stopBits, int parity) {

        selectedSerialPort = serialPorts[serialPortIndex];

        selectedSerialPort.openPort();

        try { Thread.sleep(2000); }
        catch (InterruptedException e) { throw new RuntimeException(e); }

        if (selectedSerialPort.isOpen()) {

            selectedSerialPort.setComPortParameters(baudRate, dataBits, stopBits, parity);

            selectedSerialPort.addDataListener(new SerialPortDataListener() {

                @Override
                public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }

                // SerialEvent for receiving weight data (Integer) from YH-T3 (RS232C) Weighing Indicator device
                @Override
                public void serialEvent(SerialPortEvent serialPortEvent) {

                    byte[] newData = serialPortEvent.getReceivedData();
                    char[] weightDigits = new char[7]; // Length = 7 (newData.length - 1(delimiter))

                    for (int delimiterIndex = newData.length - 1; delimiterIndex >= 0; delimiterIndex--) {

                        if ((char) newData[delimiterIndex] == DELIMITER) {

                            switch (delimiterIndex) {

                                case 7 -> {

                                    weightDigits[0] = (char) newData[6];
                                    weightDigits[1] = (char) newData[5];
                                    weightDigits[2] = (char) newData[4];
                                    weightDigits[3] = (char) newData[3];
                                    weightDigits[4] = (char) newData[2];
                                    weightDigits[5] = (char) newData[1];
                                    weightDigits[6] = (char) newData[0];
                                }

                                case 6 -> {

                                    weightDigits[0] = (char) newData[5];
                                    weightDigits[1] = (char) newData[4];
                                    weightDigits[2] = (char) newData[3];
                                    weightDigits[3] = (char) newData[2];
                                    weightDigits[4] = (char) newData[1];
                                    weightDigits[5] = (char) newData[0];
                                    weightDigits[6] = (char) newData[7];
                                }

                                case 5 -> {

                                    weightDigits[0] = (char) newData[4];
                                    weightDigits[1] = (char) newData[3];
                                    weightDigits[2] = (char) newData[2];
                                    weightDigits[3] = (char) newData[1];
                                    weightDigits[4] = (char) newData[0];
                                    weightDigits[5] = (char) newData[7];
                                    weightDigits[6] = (char) newData[6];
                                }

                                case 4 -> {

                                    weightDigits[0] = (char) newData[3];
                                    weightDigits[1] = (char) newData[2];
                                    weightDigits[2] = (char) newData[1];
                                    weightDigits[3] = (char) newData[0];
                                    weightDigits[4] = (char) newData[7];
                                    weightDigits[5] = (char) newData[6];
                                    weightDigits[6] = (char) newData[5];
                                }

                                case 3 -> {

                                    weightDigits[0] = (char) newData[2];
                                    weightDigits[1] = (char) newData[1];
                                    weightDigits[2] = (char) newData[0];
                                    weightDigits[3] = (char) newData[7];
                                    weightDigits[4] = (char) newData[6];
                                    weightDigits[5] = (char) newData[5];
                                    weightDigits[6] = (char) newData[4];
                                }

                                case 2 -> {

                                    weightDigits[0] = (char) newData[1];
                                    weightDigits[1] = (char) newData[0];
                                    weightDigits[2] = (char) newData[7];
                                    weightDigits[3] = (char) newData[6];
                                    weightDigits[4] = (char) newData[5];
                                    weightDigits[5] = (char) newData[4];
                                    weightDigits[6] = (char) newData[3];
                                }

                                case 1 -> {

                                    weightDigits[0] = (char) newData[0];
                                    weightDigits[1] = (char) newData[7];
                                    weightDigits[2] = (char) newData[6];
                                    weightDigits[3] = (char) newData[5];
                                    weightDigits[4] = (char) newData[4];
                                    weightDigits[5] = (char) newData[3];
                                    weightDigits[6] = (char) newData[2];
                                }

                                case 0 -> {

                                    weightDigits[0] = (char) newData[7];
                                    weightDigits[1] = (char) newData[6];
                                    weightDigits[2] = (char) newData[5];
                                    weightDigits[3] = (char) newData[4];
                                    weightDigits[4] = (char) newData[3];
                                    weightDigits[5] = (char) newData[2];
                                    weightDigits[6] = (char) newData[1];
                                }
                            }
                        }
                    }

                    try { scaleWeight = Integer.parseInt(new String(weightDigits)); }
                    catch (NumberFormatException nfe) { scaleWeight = null; }
                }
            });

            return true;
        }

        return false;
    }

    public static boolean closeSerialPort() { return selectedSerialPort.closePort(); }

    public static SerialPort[] getSerialPorts() { return serialPorts; }

    public static void setSerialPorts(SerialPort[] serialPorts) { SerialPortConnection.serialPorts = serialPorts; }

    public static Integer getScaleWeight() { return scaleWeight; }

    public static boolean saveSerialPortSettings(int selectedPortIndex, int baudRate, int dataBits, int stopBits,
                                                 int parity) {

        return SerialPortSettings.saveSerialPortSettings(selectedPortIndex, baudRate, dataBits, stopBits, parity);
    }
}