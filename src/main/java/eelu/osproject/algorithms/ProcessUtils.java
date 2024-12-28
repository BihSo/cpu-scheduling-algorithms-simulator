package eelu.osproject.algorithms;
public class ProcessUtils {
    public static void calculateAverages(Process[] processes) {
        if (processes.length == 0) {
            Process.avgTurnaroundTime = 0;
            Process.avgWaitingTime = 0;
            return;
        }
        Process.avgWaitingTime /= processes.length;
        Process.avgTurnaroundTime /= processes.length;
    }
    public static void initializeAverages() {
        Process.avgTurnaroundTime = 0;
        Process.avgWaitingTime = 0;
        Process.throughput = 0;
    }
    public static void updateProcessTimes(Process process, int currentTime) {
        process.setCompletionTime(currentTime);
        process.setTurnaroundTime(currentTime - process.getArrivalTime());
        process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
        Process.avgTurnaroundTime += process.getTurnaroundTime();
        Process.avgWaitingTime += process.getWaitingTime();
    }
    public static void calcThroughput(Process... processes) {
        int minArrivalTime = Integer.MAX_VALUE;
        int maxCompletionTime = Integer.MIN_VALUE;
        for (Process p : processes) {
            minArrivalTime = Math.min(minArrivalTime, p.getArrivalTime());
            maxCompletionTime = Math.max(maxCompletionTime, p.getCompletionTime());
        }
        int totalTime = maxCompletionTime - minArrivalTime;
        Process.throughput = (double) processes.length / totalTime;
    }
}
