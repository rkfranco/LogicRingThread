import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class LogicRing extends Thread {

    private final int CREATE_PROCESS_TIMER = 30000;
    private final int CREATE_REQUISITION_TIMER = 25000;
    private final int INATIVATE_PROCESS_TIMER = 100000;
    private final int INATIVATE_COORDENATOR_TIMER = 80000;

    private List<Process> processes;

    public LogicRing() {
        processes = new LinkedList<>();
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

    private void createProcess() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(CREATE_PROCESS_TIMER);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                int newID;
                do {
                    newID = Math.round(Math.round(Math.random() * 999));
                } while (isInvalidId(newID));
                addProcess(new Process(newID));
                System.out.println(this.getProcesses().size());
            }
        }).start();
    }

    private boolean isInvalidId(int id) {
        return id == 0 || getProcesses().stream().anyMatch(p -> p.getId() == id);
    }

    private void sendRequisition() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(CREATE_REQUISITION_TIMER);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (getProcesses().stream().anyMatch(Process::isCoordenator)) {
                    System.out.println("Coordenador ainda operante!");
                    break;
                }
                Requisition requisition = getProcesses().stream().findFirst().orElse(new Process(0)).createRequisition();
                getProcesses().forEach(p -> p.receiveRequisition(requisition));
                getProcesses().stream().filter(p -> p.getId() == requisition.getId()).findFirst().ifPresent(Process::setCoordenator);
                System.out.println("Requisicao enviada!");
            }
        }).start();
    }

    private void inativateCoordenator() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(INATIVATE_COORDENATOR_TIMER);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                getProcesses().stream().filter(Process::isCoordenator).findFirst().ifPresent(Process::desactivate);
                removeInactiveProcesses();
                System.out.println("Coordenador removido!");
            }
        }).start();
    }

    private void inativateProcess() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(INATIVATE_PROCESS_TIMER);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                getProcesses().stream().findFirst().ifPresent(Process::desactivate);
                removeInactiveProcesses();
                System.out.println("Processo encerrado");
            }
        }).start();
    }

    private void removeInactiveProcesses() {
        setProcesses(getProcesses().stream().filter(Process::isActive).collect(Collectors.toList()));
    }

    @Override
    public void run() {
        this.createProcess();
        this.sendRequisition();
        this.inativateCoordenator();
        this.inativateProcess();
    }
}
