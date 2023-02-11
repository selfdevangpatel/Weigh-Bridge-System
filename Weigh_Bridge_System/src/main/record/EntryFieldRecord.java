package main.record;

import java.sql.Date;
import java.sql.Time;

public record EntryFieldRecord(String vehicleNumber, int charge, String party, String containerNumber,
                               String paymentMode, String material, int tareWeight, Date tareDate, Time tareTime,
                               String tareManual, int grossWeight, Date grossDate, Time grossTime, String grossManual,
                               int netWeight) {}