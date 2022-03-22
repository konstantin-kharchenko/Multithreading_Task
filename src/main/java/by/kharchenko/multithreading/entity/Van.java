package by.kharchenko.multithreading.entity;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Van extends Thread {
    private static final Logger logger = LogManager.getLogger(Van.class);
    private static final int MINIMUM_LOAD_TIME = 2000;
    private static final int MAXIMUM_LOAD_TIME = 10000;
    private final int randomTime;
    private final boolean perishableProducts;
    private Terminal terminal;
    private EnumState state;
    private Process process;

    public Van(boolean perishableProducts, String name) {
        super(name);
        if (perishableProducts) {
            this.setPriority(Thread.MAX_PRIORITY);
        } else {
            this.setPriority(Thread.MIN_PRIORITY);
        }
        randomTime = (int) (Math.random() * (MAXIMUM_LOAD_TIME - MINIMUM_LOAD_TIME + 1) + MINIMUM_LOAD_TIME);
        this.perishableProducts = perishableProducts;
    }

    public enum EnumState {
        CREATED, WAITING, PROCESSING, COMPLETED
    }

    public enum Process {
        LOAD, UNLOAD
    }

    public int getRandomTime() {
        return randomTime;
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
}
