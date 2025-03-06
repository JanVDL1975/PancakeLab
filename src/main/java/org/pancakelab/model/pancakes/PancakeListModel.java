package org.pancakelab.model.pancakes;

import org.pancakelab.repository.PancakeRepository;

import javax.swing.DefaultListModel;
import java.sql.SQLException;
import java.util.List;

public class PancakeListModel extends DefaultListModel<Pancake> {
    private final PancakeRepository pancakeRepository;
    private final List<String> pancakeNames;
    private final List<Pancake>  pancakeList;

    public List<Pancake> getPancakeList() {
        return pancakeList;
    }



    public List<String> getPancakeNames() {
        return pancakeNames;
    }

    public PancakeListModel() throws SQLException {
        pancakeRepository = new PancakeRepository();
        this.pancakeNames = pancakeRepository.getAllPancakesByName();
        this.pancakeList = pancakeRepository.getAllPancakes();
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

