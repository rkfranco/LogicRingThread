import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class LogicRing extends Thread {

    private final int CREATE_PROCESS_TIMER = 30000;
    private final int CREATE_REQUISITION_TIMER = 25000;
    private final int INATIVATE_PROCESS_TIMER = 80000;
    private final int INATIVATE_COORDENATOR_TIMER = 100000;

    private List<Process> processes;

    public LogicRing() {
        processes = new ArrayList<>();
    }

    private void createProcess() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(CREATE_PROCESS_TIMER);
                    addProcess(new Process(createProcessId()));
                    System.out.println("\n---> Criação de um novo processo");
                    printProcesses();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void sendRequisition() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(CREATE_REQUISITION_TIMER);
                    System.out.println("\n---> Requisição para o coordenador");

                    if (!hasProcesses()) {
                        System.out.println("Requisição cancelada! Não há processos");
                        continue;
                    }
                    if (getProcesses().stream().anyMatch(Process::isCoordenator)) {
                        System.out.println("Requisição cancelada! Coordenador ainda operante");
                        continue;
                    }

                    Requisition requisition = getProcesses().get((int) (Math.random() * getProcesses().size())).createRequisition();
                    getProcesses().forEach(p -> p.receiveRequisition(requisition));
                    getProcesses().stream().filter(p -> p.getId() == requisition.getId()).findAny().ifPresent(Process::setCoordenator);

                    System.out.println("Requisição enviada! Novo coordenador operante");
                    printProcesses();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void inativateCoordenator() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(INATIVATE_COORDENATOR_TIMER);
                    System.out.println("\n---> Inativação do coordenador");
                    setProcesses(getProcesses().stream().filter(Process::isNotCoordenator).collect(Collectors.toList()));
                    System.out.println("Coordenador inativado");
                    printProcesses();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void inativateProcess() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(INATIVATE_PROCESS_TIMER);
                    System.out.println("\n---> Inativação de um processo");
                    Process p = getProcesses().remove((int) (getProcesses().size() * Math.random()));
                    System.out.println(String.format("Processo %s inativado", p.getId()));
                    printProcesses();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    public void addProcess(Process process) {
        this.processes.add(process);
    }

    private int createProcessId() {
        Random generator = new Random();
        int newID;
        do {
            newID = generator.nextInt(1000);
        } while (isInvalidId(newID));
        return newID;
    }

    private boolean isInvalidId(int id) {
        return id == 0 || getProcesses().stream().anyMatch(p -> p.getId() == id);
    }

    private boolean hasProcesses() {
        return nonNull(getProcesses()) && !getProcesses().isEmpty();
    }

    private void printProcesses() {
        String msg = getProcesses().stream().map(Process::toString).collect(Collectors.joining(", "));
        System.out.println("Processos: [" + msg + "]");
    }

    @Override
    public void run() {
        System.out.println("Algoritmo de eleição - Anel Lógico");
        this.createProcess();
        this.sendRequisition();
        this.inativateCoordenator();
        this.inativateProcess();
    }
}
