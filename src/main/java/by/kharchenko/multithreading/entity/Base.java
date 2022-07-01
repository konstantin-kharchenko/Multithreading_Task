package by.kharchenko.multithreading.entity;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Base {
    private static final Logger logger = LogManager.getLogger(Base.class);
    private static final int COUNT_TERMINALS = 3;
    private static final int COUNT_PARKING_PLACE = 5;
    private static final int MINIMUM_LOAD_TIME = 2000;
    private static final int MAXIMUM_LOAD_TIME = 10000;
    private static final ReentrantLock lockGetInstance = new ReentrantLock(true);
    private static final ReentrantLock lockForQueueBehindTheBase = new ReentrantLock(true);
    private static final ReentrantLock lockForNonPerishableProducts = new ReentrantLock(true);
    private static final ReentrantLock lockGetTerminal = new ReentrantLock(true);
    private static final AtomicBoolean isCreate = new AtomicBoolean(false);
    private static Base instance;
    private final AtomicInteger countPerishableProductsBehindTheBase;
    private final AtomicInteger countPerishableProductsInBase;
    private final AtomicInteger countParkingPlace;
    private final Deque<Terminal> terminals;
    private final int randomTime = (int) (Math.random() * (MAXIMUM_LOAD_TIME - MINIMUM_LOAD_TIME + 1) + MINIMUM_LOAD_TIME);

    public static Base getInstance() {
        if (!isCreate.get()) {
            try {
                lockGetInstance.lock();
                if (instance == null) {
                    instance = new Base();
                    isCreate.set(true);
                }
            } finally {
                lockGetInstance.unlock();
            }
        }
        return instance;
    }

    private Base() {
        terminals = new ArrayDeque<>();
        countParkingPlace = new AtomicInteger(COUNT_PARKING_PLACE);
        countPerishableProductsBehindTheBase = new AtomicInteger(0);
        countPerishableProductsInBase = new AtomicInteger(0);
        for (int i = 0; i < COUNT_TERMINALS; i++) {
            Terminal terminal = new Terminal();
            terminals.add(terminal);
        }
    }

    public void acceptVanBehindTheBase(Van van) throws InterruptedException {
        van.setEnumState(Van.EnumState.WAITING);
        logger.log(Level.INFO, van.getName() + " arrived in line behind the base. Perishable products: " + van.isPerishableProducts());
        if (van.isPerishableProducts()) {
            lockForQueueBehindTheBase.lock();
            countPerishableProductsBehindTheBase.set(countPerishableProductsBehindTheBase.get() + 1);
        } else {
            while (countPerishableProductsBehindTheBase.get() != 0) ;
            lockForQueueBehindTheBase.lock();

        }
        getTerminal(van);
    }

    private void getTerminal(Van van) throws InterruptedException {
        try {
            while (countParkingPlace.get() == 0) ;
            countParkingPlace.set(countParkingPlace.get() - 1);
            logger.log(Level.INFO, van.getName() + " went to the base. Perishable products: " + van.isPerishableProducts()
                    + "\n" + van.getName() + " number of free parking spaces: " + countParkingPlace.get());
            if (van.isPerishableProducts()) {
                countPerishableProductsBehindTheBase.set(countPerishableProductsBehindTheBase.get() - 1);
                countPerishableProductsInBase.set(countPerishableProductsInBase.get() + 1);
            }
        } finally {
            lockForQueueBehindTheBase.unlock();
        }
        logger.log(Level.INFO, van.getName() + " waiting for a terminal. Perishable products: " + van.isPerishableProducts());
        if (!van.isPerishableProducts()) {
            try {
                lockForNonPerishableProducts.lock();
                while (countPerishableProductsInBase.get() != 0) {
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            } finally {
                lockForNonPerishableProducts.unlock();
            }
        }
        try {
            lockGetTerminal.lock();

            Terminal terminal;
            while ((terminal = terminals.poll()) == null) {
                TimeUnit.MILLISECONDS.sleep(10);
            }
            van.setTerminal(terminal);
            logger.log(Level.INFO, van.getName() + " terminal received. Perishable products: " + van.isPerishableProducts());
        } finally {
            lockGetTerminal.unlock();
        }
    }

    public void workInTerminal(Van van) throws InterruptedException {
        logger.log(Level.INFO, van.getName() + " went to the terminal. Perishable products: " + van.isPerishableProducts());
        if (van.isPerishableProducts()) {
            countPerishableProductsInBase.set(countPerishableProductsInBase.get() - 1);
        }
        van.setEnumState(Van.EnumState.PROCESSING);
        if (van.getProcess() == Van.Process.LOAD) {
            logger.log(Level.INFO, van.getName() + " started downloading. Perishable products: " + van.isPerishableProducts());
        } else {
            logger.log(Level.INFO, van.getName() + " started unloading. Perishable products: " + van.isPerishableProducts());
        }
        TimeUnit.MILLISECONDS.sleep(randomTime);
        logger.log(Level.INFO, van.getName() + " worked. Perishable products: " + van.isPerishableProducts());
        van.setEnumState(Van.EnumState.COMPLETED);
        countParkingPlace.set(countParkingPlace.get() + 1);
        terminals.add(van.getTerminal());
    }
}