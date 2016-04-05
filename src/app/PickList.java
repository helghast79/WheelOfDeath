package app;


import java.util.HashSet;
import java.util.Set;


/**
 * This class holds the properties of each round to be used in the wheel of death class
 *
 * @author  Miguel Chambel
 *
 */
public class PickList {

    private String name;
    private Set<String> chooseList;
    private Set<String> ignoreList;

    public PickList(String name){
        this.name = name;
        this.chooseList = new HashSet<String>();
        this.ignoreList = new HashSet<String>();
    }

    public String getName() {
        return name;
    }

    public Set<String> getChooseList() {
        return chooseList;
    }

    public void addToChooseList(String value) {
        chooseList.add(value);
    }

    public boolean removeFromChooseList(String value) {
        return chooseList.remove(value);
    }

    public Set<String> getIgnoreList() {
        return ignoreList;
    }

    public void addToIgnoreList(String value) {
        ignoreList.add(value);
    }

    public boolean removeFromIgnoreList(String value) {
        return ignoreList.remove(value);
    }
}
