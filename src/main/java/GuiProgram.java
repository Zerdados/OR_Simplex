import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiProgram extends Frame {

    public GuiFunctionRow[] functions;
    private int res;
    private int var;
    private int pos;
    GridBagConstraints c = new GridBagConstraints();

    /**
     * Constructor for a GuiProgram to solve a linear optimization problem. <br>
     * Initially only displays a function with one variable and one restriction. <br>
     * Later versions should add functionality to add additional variables and restrictions during runtime.
     */
    public GuiProgram(){
        setLayout(new GridBagLayout());
        functions = new GuiFunctionRow[8];
        functions[0] = new GuiFunctionRow(0, 0);
        functions[1] = new GuiFunctionRow(100, 1);
        functions[2] = new GuiFunctionRow(200, 3);

        addFunctionRows();

        setSize(500, 300);
        setVisible(true);

    }

    /**
     * Constructor for a GuiProgram to solve a linear optimization problem. <br>
     * Sets the amount of functions needed based on the amount of variables and restrictions. <br>
     * Automatically displays functions.
     * @param var amount of variables
     * @param res amount of restrictions
     */
    public GuiProgram(int var, int res){
        setLayout(new GridBagLayout());
        functions = new GuiFunctionRow[res + 2];
        functions[0] = new GuiFunctionRow(0, 0);
        functions[1] = new GuiFunctionRow(100, var, res, 1);
        for(int i = 2; i < (res+2); i++){
            functions[i] = new GuiFunctionRow(i*100, var, i-1, 2);
        }
        pos = 100*res + 100;

        addFunctionRows();

        setSize(400, 200);
        setVisible(true);

    }

    /**
     * Displays a String-Matrix in the GUI.
     * @param inMatrix String-Matrix to be displayed
     */
    public void printStringMatrix(String[][] inMatrix){
        c.gridy = pos;
        add(new Label(""), c);
        pos += 100;
        for(String[] s : inMatrix){
            c.gridy = pos;
            pos += 100;
            for(String s1 : s){
                add(new Label(s1), c);
            }
        }
    }

    /**
     * Adds all GuiFunctionRows to the currently displayed GUI.
     * Chooses specific method for display using a switch statement
     */
    public void addFunctionRows(){
        for(GuiFunctionRow f : functions){
            switch(f.type){
                case 0:
                    add(f.buttons[0], f.c);
                    f.buttons[0].addActionListener(new BtnCalculateListener(this));
                    break;
                case 1:
                    showMaxFunction(f);
                    break;
                case 2:
                    showRestriction(f);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Adds a restriction to the currently displayed GUI
     * @param f GuiFunctionRow to be displayed
     */
    private void showRestriction(GuiFunctionRow f){
        add(f.labels[0], f.c);
        for(int i = 0; i < f.textFields.length - 1; i++){
            add(f.textFields[i], f.c);
            add(f.labels[i+1], f.c);
        }
        add(f.labels[f.labels.length - 1], f.c);
        add(f.textFields[f.textFields.length - 1], f.c);
    }

    /**
     * Adds max function to the currently displayed GUI
     * @param f GuiFunctionRow to be displayed
     */
    private void showMaxFunction(GuiFunctionRow f){
        add(f.labels[0], f.c);
        for(int i = 0; i < f.textFields.length; i++){
            add(f.textFields[i], f.c);
            add(f.labels[i+1], f.c);
        }
        if(f.buttons != null) {
            add(f.buttons[0], f.c);
        }
    }

    /**
     * This method was supposed to add an additional restriction.
     * It is currently not usable
     */
    private void addRes(){
        functions[2 + res] = new GuiFunctionRow(200 + res*100, var, res+1, 2);
        res++;
    }

    /**
     * This method was supposed to add an additional variable to all functions in the GUI
     * It is currently not usable
     */
    private void addVar(){
        var++;
        for(GuiFunctionRow f : functions){
            if(f.type == 1 || f.type == 2){
                System.out.println();
            }
        }
    }

    /**
     * Whenever this event is called, the simplex method in SimpleSimplex is called
     */
    private static class BtnCalculateListener implements ActionListener{

        GuiProgram gui;
        public BtnCalculateListener(GuiProgram g){
            gui = g;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SimpleSimplex.simplex();
        }
    }

}
