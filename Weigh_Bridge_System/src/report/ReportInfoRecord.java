package report;

public record ReportInfoRecord(int fromSerialNumber, int toSerialNumber, String fromDate, String toDate,
                               String vehicleNumber, String paymentMode, String entryStatus, String party) {}