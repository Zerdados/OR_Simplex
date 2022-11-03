import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiProgram extends Frame {

    public Label[] rows;
    public GuiFunctionRow[] functions;
    public boolean running = true;
    private int res;
    private int var;
    private int pos;
    GridBagConstraints c = new GridBagConstraints();

    public GuiProgram(){
        setLayout(new GridBagLayout());
        functions = new GuiFunctionRow[8];
        functions[0] = new GuiFunctionRow(0, 0);
        functions[1] = new GuiFunctionRow(100, 1);
        //functions[2] = new GuiFunctionRow(200, 1, 1);
        functions[2] = new GuiFunctionRow(200, 3);

        //add(functions[0]);
        //add(functions[1]);

        addFunctionRows();

        setSize(500, 300);
        setVisible(true);

    }
    public GuiProgram(int var, int res){
        setLayout(new GridBagLayout());
        functions = new GuiFunctionRow[res + 2];
        functions[0] = new GuiFunctionRow(0, 0);
        functions[1] = new GuiFunctionRow(100, var, res, 1);
        for(int i = 2; i < (res+2); i++){
            functions[i] = new GuiFunctionRow(i*100, var, i-1, 2);
        }
        pos = 100*res + 100;
        //c.gridy = pos;
        //functions[2] = new GuiFunctionRow(200, 1, 1);
        //functions[2] = new GuiFunctionRow(200, 3);

        //add(functions[0]);
        //add(functions[1]);

        addFunctionRows();

        setSize(400, 200);
        setVisible(true);

    }

    public void printMatrix(double[][] inMatrix){

        c.gridy = pos;
        add(new Label(""), c);
        //System.out.print(" ");
        pos = pos+100;
        for(int i = 0; i < inMatrix.length; i++){
            c.gridy = pos;
            pos = pos+100;
            for(int j = 0; j < inMatrix[i].length; j++){
                add(new Label(String.valueOf(inMatrix[i][j])), c);
               // System.out.print("A");
            }

        }

    }

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

    public GuiProgram(int lbl_amount){
        setLayout(new FlowLayout());
        rows = new Label[lbl_amount];
        setSize(300, 100);
        setVisible(true);
    }

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

    private void showRestriction(GuiFunctionRow f){
        add(f.labels[0], f.c);
        for(int i = 0; i < f.textFields.length - 1; i++){
            add(f.textFields[i], f.c);
            add(f.labels[i+1], f.c);
        }
        add(f.labels[f.labels.length - 1], f.c);
        add(f.textFields[f.textFields.length - 1], f.c);
    }

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

    private void addRes(){
        functions[2 + res] = new GuiFunctionRow(200 + res*100, var, res+1, 2);
        res++;
    }

    private void addVar(){
        var++;
        for(GuiFunctionRow f : functions){
            if(f.type == 1 || f.type == 2){

            }
        }
    }

    private void addRes(int amount){

        Label[] temp = new Label[rows.length];
        for(int i = 0; i < rows.length; i++){

            temp[i] = rows[i];

        }
        rows = new Label[temp.length + amount];
        for(int i = 0; i < temp.length; i++){

            rows[i] = temp[i];

        }

    }

    private class BtnCalculateListener implements ActionListener{

        GuiProgram gui;
        public BtnCalculateListener(GuiProgram g){
            gui = g;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SimpleSimplex.simplex();
            //System.out.println("Button Press");
        }
    }

}
