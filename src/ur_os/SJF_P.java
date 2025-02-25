package ur_os;

import java.util.PriorityQueue;

public class SJF_P extends Scheduler {

    private PriorityQueue<Process> colaListos;

    SJF_P(OS os) {
        super(os);
        colaListos = new PriorityQueue<>((p1, p2) -> Integer.compare(p1.getRemainingTimeInCurrentBurst(), p2.getRemainingTimeInCurrentBurst()));
    }

    @Override
    public void getNext(boolean cpuEmpty) {
        if (!colaListos.isEmpty() && cpuEmpty) {
            Process p = colaListos.poll();
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, p);
        }
    }

    @Override
    public void newProcess(boolean cpuEmpty) {
        if (!processes.isEmpty()) {
            colaListos.addAll(processes);
            processes.clear();
            getNext(cpuEmpty);
        }
    }

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
        if (!processes.isEmpty()) {
            colaListos.addAll(processes);
            processes.clear();
            getNext(cpuEmpty);
        }
    }
}
