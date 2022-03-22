package by.kharchenko.multithreading.entity;

import by.kharchenko.multithreading.util.IDGenerator;

public class Terminal {

    private int idTerminal;

    public Terminal() {
        this.idTerminal = IDGenerator.generate();
    }

    public int getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal(int idTerminal) {
        this.idTerminal = idTerminal;
    }

}
