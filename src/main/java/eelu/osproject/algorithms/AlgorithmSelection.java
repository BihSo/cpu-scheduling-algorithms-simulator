package eelu.osproject.algorithms;

import java.util.*;

public class AlgorithmSelection {
    public static Process[] FCFS(Process... processes) {
        Arrays.sort(processes, (o1, o2) -> o1.getArrivalTime() - o2.getArrivalTime());
        Queue<Process> inputQueue = new ArrayDeque<>(Arrays.asList(processes));
        Queue<Process> outputQueue = new ArrayDeque<>();

        int currentTime = 0;
        ProcessUtils.initializeAverages();

        while (!inputQueue.isEmpty()) {
            Process process = inputQueue.poll();

            if (process.getArrivalTime() > currentTime) {
                currentTime = process.getArrivalTime();
            }
            currentTime += process.getBurstTime();
            ProcessUtils.updateProcessTimes(process,currentTime);
            process.setResponseTime(process.getWaitingTime());
            outputQueue.add(process);
        }
        ProcessUtils.calculateAverages(processes);
        return outputQueue.toArray(new Process[0]);
    }

    public static Process[] SJFNonPreemptive(Process... processes) {
        Arrays.sort(processes, (o1, o2) -> o1.getArrivalTime() - o2.getArrivalTime());
        Queue<Process> inputQueue = new ArrayDeque<>(Arrays.asList(processes));
        Queue<Process> outputQueue = new ArrayDeque<>();
        int currentTime = 0;
        ProcessUtils.initializeAverages();

        while (!inputQueue.isEmpty()) {
            List<Process> processList = new ArrayList<>();
            for (Process process : inputQueue) {
                if (process.getArrivalTime() <= currentTime) {
                    processList.add(process);
                }
            }

            if (processList.isEmpty()) {
                currentTime = inputQueue.peek().getArrivalTime();
                continue;
            }

            processList.sort((o1, o2) -> o1.getBurstTime() - o2.getBurstTime());
            Process process = processList.get(0);
            currentTime += process.getBurstTime();
            ProcessUtils.updateProcessTimes(process,currentTime);
            process.setResponseTime(process.getWaitingTime());
            inputQueue.remove(process);
            outputQueue.add(process);
        }
        ProcessUtils.calculateAverages(processes);
        return outputQueue.toArray(new Process[0]);
    }

    public static Process[] PriorityNonPreemptive(Process... processes) {
        Arrays.sort(processes, (o1, o2) -> o1.getArrivalTime() - o2.getArrivalTime());
        Queue<Process> inputQueue = new ArrayDeque<>(Arrays.asList(processes));
        Queue<Process> outputQueue = new ArrayDeque<>();
        int currentTime = 0;
        ProcessUtils.initializeAverages();

        while (!inputQueue.isEmpty()) {
            List<Process> processList = new ArrayList<>();
            for (Process process : inputQueue) {
                if (process.getArrivalTime() <= currentTime) {
                    processList.add(process);
                }
            }

            if (processList.isEmpty()) {
                currentTime = inputQueue.peek().getArrivalTime();
                continue;
            }

            processList.sort((o1, o2) -> o1.getPriority() - o2.getPriority());
            Process process = processList.get(0);
            currentTime += process.getBurstTime();
            ProcessUtils.updateProcessTimes(process,currentTime);
            process.setResponseTime(process.getWaitingTime());
            inputQueue.remove(process);
            outputQueue.add(process);
        }
        ProcessUtils.calculateAverages(processes);
        return outputQueue.toArray(new Process[0]);
    }

    public static Process[] RoundRobin(int timeQuantum, Process... processes) {
        Arrays.sort(processes, (o1, o2) -> o1.getArrivalTime() - o2.getArrivalTime());
        Queue<Process> inputQueue = new ArrayDeque<>(Arrays.asList(processes));
        Queue<Process> outputQueue = new ArrayDeque<>();
        int currentTime = 0;
        ProcessUtils.initializeAverages();

        Map<Process, Integer> remainingBurstTime = new HashMap<>();
        for (Process process : processes) {
            remainingBurstTime.put(process, process.getBurstTime());
        }

        while (!inputQueue.isEmpty()) {
            Process process = inputQueue.poll();
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }
            if (process.getResponseTime() == -1) {
                process.setResponseTime(currentTime - process.getArrivalTime());
            }
            int remainingBurst = remainingBurstTime.get(process);
            if (remainingBurst <= timeQuantum) {
                currentTime += remainingBurst;
                ProcessUtils.updateProcessTimes(process,currentTime);
                outputQueue.add(process);
                remainingBurstTime.remove(process);
            } else {
                currentTime += timeQuantum;
                remainingBurstTime.put(process, remainingBurst - timeQuantum);
                inputQueue.add(process);
            }
        }
        ProcessUtils.calculateAverages(processes);
        return outputQueue.toArray(new Process[0]);
    }
}