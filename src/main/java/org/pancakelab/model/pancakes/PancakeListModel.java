package org.pancakelab.model.pancakes;

import org.pancakelab.repository.impl.PancakeRepositoryImpl;

import javax.swing.DefaultListModel;
import java.sql.SQLException;
import java.util.List;

public class PancakeListModel extends DefaultListModel<Pancake> {
    private final PancakeRepositoryImpl pancakeRepositoryImpl;
    private final List<String> pancakeNames;
    private final List<Pancake>  pancakeList;

    public List<Pancake> getPancakeList() {
        return pancakeList;
    }



    public List<String> getPancakeNames() {
        return pancakeNames;
    }

    public PancakeListModel() throws SQLException {
        pancakeRepositoryImpl = new PancakeRepositoryImpl();
        this.pancakeNames = pancakeRepositoryImpl.getAllPancakesByName();
        this.pancakeList = pancakeRepositoryImpl.getAllPancakes();
    }

    public void addPancake(Pancake pancake) {
        addElement(pancake); // Adds the pancake to the list and notifies listeners
    }

    public void removePancake(int index) {
        if (index >= 0 && index < getSize()) {
            remove(index); // Removes the pancake and updates the UI
        }
    }


}

