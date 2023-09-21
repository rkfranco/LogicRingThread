public class Process {
    private int id;
    private boolean isCoordenator;

    public Process(int id) {
        setId(id);
        setCoordenator(false);
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

    public boolean isNotCoordenator() {
        return !isCoordenator;
    }

    public void setCoordenator(boolean coordenator) {
        isCoordenator = coordenator;
    }

    public void setCoordenator() {
        setCoordenator(true);
    }

    public Requisition createRequisition() {
        return new Requisition(getId());
    }

    public void receiveRequisition(Requisition requisition) {
        requisition.setId(Math.max(requisition.getId(), getId()));
    }

    @Override
    public String toString() {
        return String.valueOf(this.getId());
    }
}
