package eelu.osproject.algorithms;

public class Process {
    public static int count = 0;
    public static double avgWaitingTime;
    public static double avgTurnaroundTime;
    public static double throughput;
    private String processID;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private int responseTime = -1;
    private int turnaroundTime;
    private int completionTime;
    private int waitingTime;

    public Process() {

    }

    public String getProcessID() {
        return processID;
    }

    public void setProcessID(String processID) {
        if (processID == null || processID.isEmpty()) {
            this.processID = "P" + count++;
        } else {
            if (processID.contains("p") || processID.contains("P")) {
                this.processID = processID;
            } else this.processID = "P" + processID;
        }
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }
}
