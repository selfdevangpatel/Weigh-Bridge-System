package main.record;

public record EntryRecord(int serialNumber, String vehicleNumber, int charge, String party, String paymentMode,
                          String containerNumber, String material, String tareWeight, String tareDate, String tareTime,
                          String tareManual, String grossWeight, String grossDate, String grossTime, String grossManual,
                          String netWeight, String entryDate, String entryTime, String insertOperator,
                          String updateOperator) {}