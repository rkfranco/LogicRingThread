public class Process {
    private int id;
    private boolean isCoordenator;
    private boolean isActive;

    public Process(int id) {
        setId(id);
        setCoordenator(false);
        setActive(true);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCoordenator() {
        return isCoordenator;
    }

    public void setCoordenator(boolean coordenator) {
        isCoordenator = coordenator;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setCoordenator() {
        System.out.println(toString());
        setCoordenator(true);
    }

    public void desactivate() {
        this.setActive(false);
    }

    public Requisition createRequisition() {
        return new Requisition(getId());
    }

    public void receiveRequisition(Requisition requisition) {
        requisition.setId(Math.max(requisition.getId(), getId()));
    }

    @Override
    public String toString() {
        return "ID: " + this.getId() + "\n" + "COORDENADOR: " + this.isCoordenator() + "\n" + "Ativo: " + this.isActive() + "\n";
    }
}
