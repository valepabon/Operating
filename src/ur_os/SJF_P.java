package ur_os;

import java.util.PriorityQueue;

public class SJF_P extends Scheduler {

    // Cola de prioridad basada en la ráfaga de CPU más corta
    private PriorityQueue<Process> colaListos;

    // Constructor
    SJF_P(OS os) {
        super(os);
        colaListos = new PriorityQueue<>((p1, p2) -> Integer.compare(p1.getRemainingCPUBurst(), p2.getRemainingCPUBurst()));
    }

    // Método para seleccionar el siguiente proceso a ejecutar
    @Override
    public void getNext(boolean cpuEmpty) {
        if (!colaListos.isEmpty() && cpuEmpty) {
            Process p = colaListos.poll(); // Sacar el proceso con menor ráfaga
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, p); // Asignar a la CPU
        }
    }

    // Manejo de nuevos procesos que entran al sistema
    @Override
    public void newProcess(boolean cpuEmpty) {
        if (!processes.isEmpty()) {
            colaListos.addAll(processes); // Agregar procesos nuevos a la cola de prioridad
            processes.clear(); // Limpiar la lista de procesos listos, ya están en la cola de prioridad
            getNext(cpuEmpty); // Verificar si se puede ejecutar un nuevo proceso
        }
    }

    // Manejo de procesos que retornan de I/O
    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
        if (!processes.isEmpty()) {
            colaListos.addAll(processes); // Agregar procesos que regresan de I/O
            processes.clear();
            getNext(cpuEmpty);
        }
    }
}
