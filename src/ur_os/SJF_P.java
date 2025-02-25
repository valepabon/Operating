package ur_os;

import java.util.PriorityQueue;

public class SJF_P extends Scheduler {

    private PriorityQueue<Process> colaListos;

    SJF_P(OS os) {
        super(os);
        colaListos = new PriorityQueue<>((p1, p2) -> Integer.compare(
            p1.getPBL().getRemainingTimeInCurrentBurst(),
            p2.getPBL().getRemainingTimeInCurrentBurst()
        ));
    }

    @Override
    public void getNext(boolean cpuEmpty) {
        if (!colaListos.isEmpty() && cpuEmpty) {
            Process p = colaListos.poll();
            System.out.println("[SCHEDULER] Proceso " + p.getPid() + " asignado a la CPU.");
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, p);
        } else if (cpuEmpty) {
            System.out.println("[SCHEDULER] CPU inactiva, no hay procesos en cola.");
            os.interrupt(InterruptType.SCHEDULER_CPU_EMPTY, null); // Indica CPU inactiva
        }
    }

    @Override
    public void newProcess(boolean cpuEmpty) {
        if (!processes.isEmpty()) {
            System.out.println("[SCHEDULER] Nuevos procesos añadidos a la cola de listos.");
            colaListos.addAll(processes);
            processes.clear();
            getNext(cpuEmpty);
        }
    }

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
        if (!processes.isEmpty()) {
            System.out.println("[SCHEDULER] Procesos regresando de I/O añadidos a la cola.");
            colaListos.addAll(processes);
            processes.clear();
            getNext(cpuEmpty);
        }
    }
}

