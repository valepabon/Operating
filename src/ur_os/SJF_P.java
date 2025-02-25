/package ur_os;

import java.util.*;

public class SJF_P {
    public static void main(String[] args) {
        // Ejecutar con initSimulationQueueSimpler()
        List<Process> procesos1 = SystemOS.initSimulationQueueSimpler();
        List<Integer> ciclos1 = ejecutarSJFPreemptivo(procesos1);
        System.out.println("Salida para initSimulationQueueSimpler:");
        System.out.println(ciclos1);

        // Ejecutar con initSimulationQueueSimpler2()
        List<Process> procesos2 = SystemOS.initSimulationQueueSimpler2();
        List<Integer> ciclos2 = ejecutarSJFPreemptivo(procesos2);
        System.out.println("Salida para initSimulationQueueSimpler2:");
        System.out.println(ciclos2);
    }

    public static List<Integer> ejecutarSJFPreemptivo(List<Process> procesos) {
        PriorityQueue<Process> colaListos = new PriorityQueue<>(Comparator.comparingInt(p -> p.tiempoRestante));
        Queue<Process> colaBloqueados = new LinkedList<>();
        procesos.sort(Comparator.comparingInt(p -> p.tiempoLlegada));

        int tiempoActual = 0;
        int procesosTerminados = 0;
        int n = procesos.size();
        List<Integer> ciclos = new ArrayList<>();

        while (procesosTerminados < n) {
            // Revisar si algún proceso bloqueado por I/O ya terminó y debe volver a la cola de listos
            Iterator<Process> it = colaBloqueados.iterator();
            while (it.hasNext()) {
                Process p = it.next();
                if (p.tiempoRetornoIO == tiempoActual) {
                    p.avanzarRafaga(); // Pasar a la siguiente ráfaga de CPU
                    colaListos.add(p);
                    it.remove(); // Sacar de la cola de bloqueados
                }
            }

            // Agregar nuevos procesos a la cola de listos si llegan en este tiempo
            for (Process p : procesos) {
                if (p.tiempoLlegada == tiempoActual) {
                    colaListos.add(p);
                }
            }

            if (!colaListos.isEmpty()) {
                Process procesoActual = colaListos.poll();
                ciclos.add(procesoActual.id); // Agrega el ID del proceso en ejecución

                procesoActual.tiempoRestante--;

                if (procesoActual.tiempoRestante > 0) {
                    colaListos.add(procesoActual);
                } else {
                    if (procesoActual.tieneMasRafagas()) {
                        procesoActual.avanzarRafaga(); // Mueve a la siguiente ráfaga

                        if (procesoActual.bursts.get(procesoActual.indiceRafaga).tipo == ProcessBurstType.IO) {
                            procesoActual.tiempoRetornoIO = tiempoActual + procesoActual.tiempoRestante;
                            colaBloqueados.add(procesoActual);
                        } else {
                            colaListos.add(procesoActual);
                        }
                    } else {
                        procesosTerminados++;
                    }
                }
            } else {
                ciclos.add(-1); // CPU ociosa
            }

            tiempoActual++;
        }

        return ciclos;
    }
}
