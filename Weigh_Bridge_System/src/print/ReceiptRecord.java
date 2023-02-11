package print;

import java.sql.Date;
import java.sql.Time;

public record ReceiptRecord(int serialNumber, String vehicleNumber, int charge, String party, String containerNumber,
                            int tareWeight, Date tareDate, Time tareTime, int grossWeight, Date grossDate,
                            Time grossTime, int netWeight) {}