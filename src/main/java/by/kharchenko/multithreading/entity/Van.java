package by.kharchenko.multithreading.entity;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Van extends Thread {
    private static final Logger logger = LogManager.getLogger(Van.class);
    private final boolean perishableProducts;
    private Terminal terminal;
    private EnumState state;
    private Process process;

    public int getCustomPriority() {
        return priority;
    }

    public void setCustomPriority(int priority) {
        this.priority = priority;
    }


    private int priority;

    public Van(boolean perishableProducts, String name, String processName) {
        super(name);
        if (perishableProducts) {
            priority = 2;
        } else {
            priority = 1;
        }
        if (processName.equals("load")) process = Process.LOAD;
        else process = Process.UNLOAD;
        this.perishableProducts = perishableProducts;
    }

    public enum EnumState {
        CREATED, WAITING, PROCESSING, COMPLETED
    }

    public enum Process {
        LOAD, UNLOAD
    }

    public boolean isPerishableProducts() {
        return perishableProducts;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public EnumState getEnumState() {
        return state;
    }

    public void setEnumState(EnumState state) {
        this.state = state;
    }

    public void setTerminal(Terminal myTerminal) {
        this.terminal = myTerminal;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }


    @Override
    public void run() {
        try {
            this.setEnumState(EnumState.CREATED);
            logger.log(Level.INFO, this.getName() + " started. Perishable products: " + this.isPerishableProducts());
            Base base = Base.getInstance();
            base.acceptVanBehindTheBase(this);
            base.workInTerminal(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Van{" +
                "perishableProducts=" + perishableProducts +
                ", terminal=" + terminal +
                ", state=" + state +
                ", process=" + process +
                ", priority=" + priority +
                '}';
    }
}
