package app;

import custom.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;


/**
 * @author Miguel Chambel
 */
public class WheelOfDeath {

    private PickList currentList = null;


    private JFrame jFrame;
    private JPanel jPanel;
    private JPanel jLeftPanel;
    private JMenu menu1_list, menu2_spin, menu3_help;
    private JMenuBar menuBar;

    // private JLabel infoLbl;


    //menu 1 has a item which contains the current list name, it's index is needed for updating menu 1 when list changes
    private final int MENU1_LIST_NAME_INDEX = 7;
    private final int MENU2_MODE_INIT_INDEX = 0;


    //circular pie
    double pieAngle;

    //Settings
    //background color
    Color backgroundColor = new Color(40, 40, 40);
    //size of the arrow
    Dimension arrowDimension = new Dimension(50, 35);
    //path to arrow image
    String arrowFilePath = "/arrow.png";
    //path to names list
    String namesFilePath = "/names.txt";


    public static void main(String[] args) {
        WheelOfDeath wheelOfDeath = new WheelOfDeath();
        wheelOfDeath.init();

    }


    private void init() {
        createFrame();
        createMenu();

        createLeftPanel();
        createPanel_empty();

        menuBar.setVisible(true);
        jFrame.setVisible(true);

        loadDataFromResource();


        createPanel_Pie();
    }


    private void start() {

        //wheel of death
        if (((JRadioButtonMenuItem) (menu2_spin.getMenuComponent(MENU2_MODE_INIT_INDEX))).isSelected()) {
            createPanel_Pie();
        }

        //death board
        if (((JRadioButtonMenuItem) (menu2_spin.getMenuComponent(MENU2_MODE_INIT_INDEX + 1))).isSelected()) {
            createPanel_board();
        }


    }//end start


    //load data
    private void loadDataFromFile(String filePath) {
        loadData(new ReadFile(filePath));

    }//end init

    private void loadDataFromResource() {

        //load file from resources or from within jar (creating temporary file)
        File file = null;
        String resource = namesFilePath;
        URL res = getClass().getResource(resource);

        if (res.toString().startsWith("jar:")) {
            try {
                InputStream input = getClass().getResourceAsStream(resource);
                file = File.createTempFile("tempfile", ".tmp");
                OutputStream out = new FileOutputStream(file);
                int read;
                byte[] bytes = new byte[1024];

                while ((read = input.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                file.deleteOnExit();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            //this will work in IDE, but not from a JAR
            file = new File(res.getFile());
        }

        loadData(new ReadFile(file));

    }//end init

    private void loadData(ReadFile fileRead) {

        //initialize arraylist of rounds
        ArrayList<PickList> pickLists = new ArrayList<PickList>();

        String s;


        while ((s = fileRead.nextLine()) != null) {

            //move to where the round is declared in the file
            if (s.startsWith(">")) {

                //create and initializes the round object with the name taken from the file
                PickList thisPickList = new PickList(s.substring(1));

                //add each name in the file name to the round object
                String[] names = fileRead.nextLine().split("/");
                for (String n : names) {
                    thisPickList.addToChooseList(n);
                }

                //end of file check
                if (fileRead.peekNextLine() != null) {

                    //checks if next line has names to add to ignore list
                    if (!fileRead.peekNextLine().startsWith(">") && !fileRead.peekNextLine().startsWith(" ")) {
                        String[] ignoreNames = fileRead.nextLine().split("/");
                        for (String n : ignoreNames) {
                            thisPickList.addToIgnoreList(n);
                        }
                    }
                }

                pickLists.add(thisPickList);

            }

        }


        //remove rounds that are closed (no more names left)
        for (PickList r : pickLists) {
            if (r.getChooseList().size() == r.getIgnoreList().size()) {
                pickLists.remove(r);
            }
        }


        //analyse rounds list and create the menu checkboxes or do nothing if the rounds is empty
        if (pickLists.size() == 0) {
            //no list - do nothing
        } else if (pickLists.size() == 1) {
            currentList = pickLists.get(0);
            createRoundMenuCheckBoxes();

        } else {

            //file retrieved more than 1 list
            String[] listNames = new String[pickLists.size()];
            for (int i = 0; i < pickLists.size(); i++) {
                listNames[i] = pickLists.get(i).getName();
            }

            //give the user option to choose which list to load
            //return -1 means user canceled operation so don't do nothing
            int listPick = showDialogChooseList(listNames);

            if (listPick != -1) {
                currentList = pickLists.get(listPick);
                createRoundMenuCheckBoxes();
            }
        }
    }


    private void createFrame() {
        jFrame = new JFrame("All but me...");
        jFrame.setUndecorated(false);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setSize(300, 500);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

    private void createMenu() {

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu1_list = new JMenu("List");
        menu2_spin = new JMenu("Modes");
        menu3_help = new JMenu("About");


        setMenu1Items();
        setMenu2Items();
        setMenu3Items();


        menuBar.add(menu1_list);
        menuBar.add(menu2_spin);
        menuBar.add(menu3_help);


        jFrame.add(menuBar, BorderLayout.PAGE_START);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void createLeftPanel() {


        //JOptionPane.showMessageDialog(jFrame,resource.getPath());
        //ImageIcon imageIcon = new ImageIcon(arrowFilePath);

        ImageIcon imageIcon = new ImageIcon(this.getClass().getResource(arrowFilePath));


        Image img = imageIcon.getImage();
        Image newimg = img.getScaledInstance((int) arrowDimension.getWidth(), (int) arrowDimension.getHeight(), java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);

        JLabel label = new JLabel("", newIcon, JLabel.CENTER);

        jLeftPanel = new JPanel(new BorderLayout());
        jLeftPanel.add(label, BorderLayout.CENTER);
        jLeftPanel.setBackground(backgroundColor);
        jLeftPanel.setPreferredSize(arrowDimension);
        jLeftPanel.setVisible(true);

        jFrame.add(jLeftPanel, BorderLayout.WEST);
    }


    private void createPanel_empty() {

        jPanel = new JPanel();
        jPanel.setLayout(null);
        jPanel.setBackground(backgroundColor);

        jPanel.setPreferredSize(new Dimension(400, 400));
        jPanel.setVisible(true);


        jFrame.add(jPanel, BorderLayout.CENTER);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    //wheel of death
    private void createPanel_Pie() {
        //list is empty
        if (currentList.getChooseList().size() <= 0) {
            //log("Current pick list is empty");
            return;
        }

        //remove jPanel if it exists
        if (jPanel != null) {
            jPanel.removeAll();
            jFrame.remove(jPanel);
        }

        //RotatingPanel costumPanel = new RotatingPanel();
        //jPanel = costumPanel.getPanel();
        jPanel = new RotatingCircularPanel();
        jPanel.setLayout(new PieLayout());
        jPanel.setBackground(backgroundColor);

        createComponets_PiePanels();

        jPanel.setPreferredSize(new Dimension(400, 400));
        jPanel.setVisible(true);


        jFrame.add(jPanel, BorderLayout.CENTER);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        jLeftPanel.setVisible(true);


    }

    private void createComponets_PiePanels() {
        Set<String> chooseList = new HashSet<String>();
        int totalMenuComponents = menu1_list.getMenuComponentCount();

        //add elements to the picker
        for (int i = MENU1_LIST_NAME_INDEX + 2; i < totalMenuComponents; i++) {
            if (((JCheckBoxMenuItem2) (menu1_list.getMenuComponent(i))).getState() == true) {
                chooseList.add(((JCheckBoxMenuItem2) (menu1_list.getMenuComponent(i))).getText());

            }

        }


        Object[] chooseNames = chooseList.toArray();


        int totalNames = chooseNames.length;
        pieAngle = 360.0d / (totalNames);


        for (int i = 0; i < totalNames; i++) {
            //custom component
            double start = i * pieAngle;
            double finish = start + pieAngle;

            //MyPiePanel pie = new MyPiePanel(colors[i], start, finish, chooseNames[i].toString());
            MyPiePanel pie = new MyPiePanel(new Color(rand(0, 255, 1), rand(0, 255, 1), rand(0, 255, 1)), start, finish, chooseNames[i].toString());
            pie.setName(chooseNames[i].toString());


            pie.setOpaque(false);
            pie.setVisible(true);
            jPanel.add(pie);

        }


        //set angle so that the min rotation is exacly the angle between names
        ((RotatingCircularPanel) (jPanel)).setPieAngle(pieAngle);

        //when the list has even numbers the arrow points to the middle of two names
        if (checkIfNumberIsEven(totalNames)) {
            ((RotatingCircularPanel) (jPanel)).rotate(pieAngle / 2);
        }

    }


    //death board
    private void createPanel_board() {


        //list is empty
        if (currentList.getChooseList().size() <= 0) {
            //log("Current pick list is empty");
            return;
        }

        //remove jPanel if it exists
        if (jPanel != null) {
            jPanel.removeAll();
            jFrame.remove(jPanel);
        }

        jPanel = new DeathBoardPanel();


        jPanel.setBackground(backgroundColor);

        createComponets_Squares();

        jPanel.setPreferredSize(new Dimension(400, 400));
        jPanel.setVisible(true);


        jFrame.add(jPanel, BorderLayout.CENTER);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jLeftPanel.setVisible(false);
    }

    private void createComponets_Squares() {
        Set<String> chooseList = new HashSet<String>();
        int totalMenuComponents = menu1_list.getMenuComponentCount();

        //add elements to the picker
        for (int i = MENU1_LIST_NAME_INDEX + 2; i < totalMenuComponents; i++) {
            if (((JCheckBoxMenuItem2) (menu1_list.getMenuComponent(i))).getState() == true) {
                chooseList.add(((JCheckBoxMenuItem2) (menu1_list.getMenuComponent(i))).getText());

            }

        }


        Object[] chooseNames = chooseList.toArray();


        int totalNames = chooseNames.length;

        int itemsPerRow = (int) Math.ceil(Math.sqrt(totalNames));
        int itemsPerCol = (int) Math.ceil(Math.sqrt(totalNames));
        Dimension squareSize = new Dimension((int) Math.round(400 * 0.9 / itemsPerCol), (int) Math.round(400 * 0.9 / itemsPerRow));

        //now that we have the dimension of the items, we can set the layout
        jPanel.setLayout(new GridLayout(itemsPerRow, itemsPerCol));


        for (int i = 0; i < totalNames; i++) {

            JLabel label = new JLabel(chooseNames[i].toString());
            label.setPreferredSize(squareSize);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);
            label.setBackground(new Color(rand(0, 255, 1), rand(0, 255, 1), rand(0, 255, 1)));
            label.setName(chooseNames[i].toString());
            label.setVisible(true);
            jPanel.add(label);


        }


    }


    private void setMenu1Items() {

        //declare JMenuItems
        JMenuItem menu1Item1_new, menu1Item2_open, menu1Item3_save, menu1Item4_name, menu1Item5_add, menu1Item6_remove;

        //Instantiate JMenuItems on each item to be added
        menu1Item1_new = new JMenuItem("Create new list");
        menu1Item2_open = new JMenuItem("Load list from file");
        menu1Item3_save = new JMenuItem("Save current list");
        menu1Item4_name = new JMenuItem("no list selected");
        menu1Item4_name.setEnabled(false);
        menu1Item5_add = new JMenuItem("Add name to list");
        menu1Item6_remove = new JMenuItem("remove name from list");


        //add action listeners
        menu1Item1_new.addActionListener(new ActionListener() { //new list
            public void actionPerformed(ActionEvent e) {
                click_new_list();
            }//end actionperformed

        });//new list ---

        menu1Item2_open.addActionListener(new ActionListener() { //open file
            public void actionPerformed(ActionEvent e) {
                click_open_file();
            }//end actionperformed --
        });//open file ---

        menu1Item3_save.addActionListener(new ActionListener() { //save file
            public void actionPerformed(ActionEvent e) {
                click_save_file();
            }//end actionperformed --

        });//save file ----

        menu1Item5_add.addActionListener(new ActionListener() { //add name to list
            public void actionPerformed(ActionEvent e) {
                click_add_name();
            }//end actionperformed --

        });//add name to list ----

        menu1Item6_remove.addActionListener(new ActionListener() { //remove name
            public void actionPerformed(ActionEvent e) {
                click_remove_name();
            }//end actionperformed --

        });//remove name  ----

        //add the items including separators to the menu1
        menu1_list.add(menu1Item1_new);
        menu1_list.add(menu1Item2_open);
        menu1_list.add(menu1Item3_save);
        menu1_list.addSeparator();
        menu1_list.add(menu1Item5_add);
        menu1_list.add(menu1Item6_remove);
        menu1_list.addSeparator();
        menu1_list.add(menu1Item4_name);
        menu1_list.addSeparator();
    }

    private void setMenu2Items() {

        //declare JMenuItems
        //JCheckBoxMenuItem2 menu2Item1_enableRepetition;

        //radio button group to choose mode
        ButtonGroup group = new ButtonGroup();

        JRadioButtonMenuItem rbModeWheelOfDeath;
        rbModeWheelOfDeath = new JRadioButtonMenuItem("Wheel of death");
        rbModeWheelOfDeath.setSelected(true);
        group.add(rbModeWheelOfDeath);
        rbModeWheelOfDeath.addActionListener(new ActionListener() { //start random picker wheel
            public void actionPerformed(ActionEvent e) {
                //start random picker wheel
                click_start_wheel();
            }

        });


        JRadioButtonMenuItem rbModeDeathBoard;
        rbModeDeathBoard = new JRadioButtonMenuItem("Death Board");
        rbModeDeathBoard.setSelected(true);
        group.add(rbModeDeathBoard);
        rbModeDeathBoard.addActionListener(new ActionListener() { //start random picker wheel
            public void actionPerformed(ActionEvent e) {
                //start random picker wheel
                click_start_wheel();
            }

        });


        //add the items including separators to the menu1
        //menu2_spin.add(menu2Item1_enableRepetition);
        //menu2_spin.addSeparator();
        menu2_spin.add(rbModeWheelOfDeath);
        menu2_spin.add(rbModeDeathBoard);
        //menu2_spin.add(rbModeCircleOfDeath);
        //menu2_spin.addSeparator();
        //menu2_spin.add(menu2Item3_start);
    }

    private void setMenu3Items() {

        //declare JMenuItems
        JMenuItem menu3Item1_about;

        //Instantiate JMenuItems on each item to be added
        menu3Item1_about = new JMenuItem("Author");

        //add action listeners
        menu3Item1_about.addActionListener(new ActionListener() { //new list
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(jFrame,
                        " ---------- Written by: ---------- \n\n" +
                                "          Miguel Chambel\n\n" +
                                "              05/04/2016\n\n");

                //splash screen help
            }

        });

        //add the items including separators to the menu1
        menu3_help.add(menu3Item1_about);
    }


    //Menu 1 - item 1 - new list click event
    private void click_new_list() {

        String listName = JOptionPane.showInputDialog(jFrame, "List Name:", "New List", JOptionPane.QUESTION_MESSAGE);

        //check no input
        if (listName == null) {
            return;
        }

        if (listName.equals("")) {
            return;
        }


        String allNames = JOptionPane.showInputDialog(jFrame, "Enter names:\n(separated by '/')", listName, JOptionPane.QUESTION_MESSAGE);

        String[] names = allNames.split("/");

        if (names.length > 1) {
            PickList thisList = new PickList(listName);

            for (String n : names) {
                thisList.addToChooseList(n);
            }

            currentList = thisList;
            createRoundMenuCheckBoxes();
            start();


        }

        //JOptionPane.showMessageDialog(jFrame, "Under Construction...");
    }

    //Menu 1 - item 2 - open file click event
    private void click_open_file() {

        JFileChooser chooser = new JFileChooser();

        //add file filter
        FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("Text files only", "txt");
        chooser.addChoosableFileFilter(txtFilter);
        chooser.setFileFilter(txtFilter);

        if (chooser.showOpenDialog(jFrame) == JFileChooser.APPROVE_OPTION) {
            // load from file
            loadDataFromFile(chooser.getSelectedFile().getPath());
            start();
        }
    }

    //Menu 1 - item 3 - save file click event
    private void click_save_file() {

        JFileChooser chooser = new JFileChooser();

        //add file filter
        FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("Text files only", "txt");
        chooser.addChoosableFileFilter(txtFilter);
        chooser.setFileFilter(txtFilter);

        chooser.showSaveDialog(jFrame);

        //get file extension
        File file = chooser.getSelectedFile();
        String extension = "";

        //break this method if no file is selected
        if (file == null) {
            return;
        }

        int i = file.getName().lastIndexOf('.');
        int p = Math.max(file.getName().lastIndexOf('/'), file.getName().lastIndexOf('\\'));

        if (i > p) {
            extension = file.getName().substring(i + 1);
        }


        //force file to have a txt extension
        if (extension.equals("txt")) {
            // filename is OK as-is

        } else {
            file = new File(file.toString() + ".txt");

        }
        saveListToFile(file);

    }

    //Menu 1 - item 4 - add name click event
    private void click_add_name() {

        String name = JOptionPane.showInputDialog(jFrame, "Enter name:", "Add Name to " + currentList.getName(), JOptionPane.QUESTION_MESSAGE);

        //check no input
        if (name == null) {
            return;
        }

        if (name.equals("")) {
            return;
        }


        currentList.addToChooseList(name);

        createRoundMenuCheckBoxes();
        start();

    }

    //Menu 1 - item 5 - remove name click event
    private void click_remove_name() {

        Object o = JOptionPane.showInputDialog(jFrame,
                "Choose name:",
                "Remove name from " + currentList.getName(),
                JOptionPane.QUESTION_MESSAGE,
                null, currentList.getChooseList().toArray(), null);
        //check no input
        if (o == null) {
            return;
        }

        String name = o.toString();

        if (name.equals("")) {
            return;
        }


        currentList.removeFromChooseList(name);
        createRoundMenuCheckBoxes();
        start();

    }


    //Menu 2 - item 2 - save file click event
    private void click_start_wheel() {
        start();
    }


    //clears previous menu1_list items below index 6 which is where the names start
    //create new items based on the current list selected
    private void createRoundMenuCheckBoxes() {

        //remove all menu 1 items - method remove doesn't seem to work properly
        menu1_list.removeAll();

        //remake the menu1 items
        setMenu1Items();

        //change menu item at index= MENU1_LIST_NAME_INDEX which is the round name
        //cast is needed because getcomponent dosen't guarantee to be a JmenuItem
        ((JMenuItem) (menu1_list.getMenuComponent(MENU1_LIST_NAME_INDEX))).setText(currentList.getName());

        //get ignore list
        Set<String> roundIgnoreList = currentList.getIgnoreList();

        //get names list iterator
        Iterator roundChoseListIterator = currentList.getChooseList().iterator();

        //iterate through names in choselist and add jcheckboxmenuitems with name to the hashmap
        while (roundChoseListIterator.hasNext()) {
            String name = (String) roundChoseListIterator.next();

            //create checkboxmenuitem with name and selected state based on whether it figures in ignore list or not
            JCheckBoxMenuItem2 jcb = new JCheckBoxMenuItem2(name, null, !roundIgnoreList.contains(name));

            //add action listener - in this case when state changes a refresh of the wheel graphics is called
            jcb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    actionListCheckBoxChangedState(e.getSource());
                }
            });


            //add the checkboxmenuitem to the menu2_round
            menu1_list.add(jcb);
            jcb.setVisible(true);


        }
    }

    private void actionListCheckBoxChangedState(Object source) {

        JCheckBoxMenuItem2 justClicked = (JCheckBoxMenuItem2) source;

        //remove/add to ignore list based on checkbox value
        if (justClicked.getState()) {
            currentList.removeFromIgnoreList(justClicked.getText());

        } else {
            currentList.addToIgnoreList(justClicked.getText());

        }
        start();

    }


    private int showDialogChooseList(String[] names) {

        String input = (String) JOptionPane.showInputDialog(null, "List picker",
                "Choose one of the following lists", JOptionPane.QUESTION_MESSAGE, null, names, names[0]);

        if (input == null) return -1;

        for (int i = 0; i < names.length; i++) {
            if (input.equals(names[i])) {
                return (i);
            }
        }

        //nothing was selected, return -1
        return -1;
    }

    private void saveListToFile(File file) {

        //construct the string to save to file
        String newContent = "";

        //1º the name
        String listName = ">" + currentList.getName();

        //2º the list of choices
        String listChoices = "";
        Iterator<String> cIterator = currentList.getChooseList().iterator();
        while (cIterator.hasNext()) {
            listChoices += cIterator.next() + (cIterator.hasNext() ? "/" : "");
        }

        //3º the ignore list
        String listIgnores = "";
        Iterator<String> iIterator = currentList.getIgnoreList().iterator();
        while (iIterator.hasNext()) {
            listIgnores += iIterator.next() + (iIterator.hasNext() ? "/" : "");
        }

        //finally the full string
        newContent = listName +
                (listChoices.equals("") ? "" : System.lineSeparator() + listChoices) +
                (listIgnores.equals("") ? "" : System.lineSeparator() + listIgnores);


        //if file exists search for the list name and if it exists replace following line (or lines)
        //if not append the list in the end of the file
        if (file.exists()) {
            Charset charset = StandardCharsets.UTF_8;

            String content = "";

            int listFoundIndex = -1;
            int nextListIndex = -1;
            try {
                //read all file to a string
                content = new String(Files.readAllBytes(file.toPath()), charset);

                listFoundIndex = content.indexOf(listName);//get the index of the current list we're saving
                nextListIndex = content.indexOf(">", listFoundIndex + 1); //get the index of the next list

                //list name found
                if (listFoundIndex >= 0) {
                    //put the file text until the founded list name and append the new list info
                    newContent = content.substring(0, listFoundIndex) + newContent + System.lineSeparator();

                    //append the rest of the file text after the list name
                    if (nextListIndex >= 0) {
                        newContent += content.substring(nextListIndex);
                    }

                    //list name not found
                } else {
                    newContent = content + System.lineSeparator() + newContent;

                }

                //write the file, no need for close because "Files" takes care of closing after all bytes are written
                Files.write(file.toPath(), newContent.getBytes(charset));
                return;

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }


        }//end if file exists


        //if file does not exist create one and write new content to it
        try {
            Files.write(file.toPath(), newContent.getBytes(StandardCharsets.UTF_8));

        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }


    }


    private int rand(int min, int max, int interval) {

        return (int) (Math.round(((Math.random() * 1000 * (Math.floor((max - min) / interval))) / 1000)) * interval) + min;

    }

    private double randDouble(double min, double max, double interval) {

        String text = Double.toString(Math.abs(interval));
        int integerPlaces = text.indexOf('.');
        int decimalPlaces_interval = text.length() - integerPlaces - 1;

        text = Double.toString(Math.abs(min));
        integerPlaces = text.indexOf('.');
        int decimalPlaces_min = text.length() - integerPlaces - 1;

        text = Double.toString(Math.abs(max));
        integerPlaces = text.indexOf('.');
        int decimalPlaces_max = text.length() - integerPlaces - 1;

        int decimalPlaces = Math.max(decimalPlaces_interval, decimalPlaces_max);
        decimalPlaces = Math.max(decimalPlaces, decimalPlaces_min);


        double a = Math.round(Math.random() * Math.pow(10.0d, decimalPlaces));

        a = a * Math.floor(((max - min) / interval));

        double b = Math.round(a / (Math.pow(10.0d, decimalPlaces)));

        return (b * interval) + min;

    }

    private boolean checkIfNumberIsEven(int x) {
        return ((x % 2) == 0);
    }


    /**
     * This is a standard checkbox menu item but selecting it does not close the menu
     */
    public class JCheckBoxMenuItem2 extends JCheckBoxMenuItem {

        public JCheckBoxMenuItem2() {
        }

        public JCheckBoxMenuItem2(Icon icon) {
            super(icon);
        }

        public JCheckBoxMenuItem2(String text) {
            super(text);
        }

        public JCheckBoxMenuItem2(Action a) {
            super(a);
        }

        public JCheckBoxMenuItem2(String text, Icon icon) {
            super(text, icon);
        }

        public JCheckBoxMenuItem2(String text, boolean b) {
            super(text, b);
        }

        public JCheckBoxMenuItem2(String text, Icon icon, boolean b) {
            super(text, icon, b);
        }

        @Override
        protected void processMouseEvent(MouseEvent evt) {
            if (evt.getID() == MouseEvent.MOUSE_RELEASED && contains(evt.getPoint())) {
                doClick();
                setArmed(true);
            } else {
                super.processMouseEvent(evt);
            }
        }


    }


}





