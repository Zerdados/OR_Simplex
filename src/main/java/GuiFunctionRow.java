import javax.swing.*;
import java.awt.*;

public class GuiFunctionRow{

    public Label[] labels;
    public Button[] buttons;
    public TextField[] textFields;
    public int type;
    private int pos;
    GridBagConstraints c = new GridBagConstraints();

    public GuiFunctionRow(int pos, int p){
        switch(p){
            case 0:
                buttons = new Button[1];
                buttons[0] = new Button("Calculate");
                type = 0;
                c.gridy = pos;
                break;
            case 1:
                labels = new Label[2];
                buttons = new Button[1];
                textFields = new TextField[1];
                labels[0] = new Label("F: ");
                labels[1] = new Label("x1");
                buttons[0] = new Button("Add Variable");
                textFields[0] = new TextField("", 5);
                type = 1;
                c.gridy = pos;
                break;
            case 3:
                buttons = new Button[1];
                buttons[0] = new Button("Add Row");
                type = 3;
                c.gridy = pos;
                break;
            default:
                break;
        }
    }

    public GuiFunctionRow(int pos, int v, int r, int par){
        c.gridy = pos;
        switch (par){
            case 1:
                labels = new Label[v + 1];
                labels[0] = new Label("F:");
                textFields = new TextField[v];
                for(int i = 1; i < (labels.length); i++){
                    labels[i] = new Label("x" + i);
                    textFields[i-1] = new TextField("", 5);
                }
                type = 1;
                break;
            case 2:
                labels = new Label[v + 2];
                labels[0] = new Label("R" + r + ":");
                textFields = new TextField[v + 1];
                for(int i = 1; i < (labels.length - 1); i++){
                    labels[i] = new Label("x" + i);
                    textFields[i-1] = new TextField("", 5);
                }
                labels[v + 1] = new Label("<=");
                textFields[v] = new TextField("", 5);
                type = 2;
                break;
        }

    }

    public void GuiResAddVar(int v){
        addLabel();
        addTextField();
        Label temp = labels[labels.length-2];
        labels[labels.length-1] = temp;
        labels[labels.length-2] = new Label("x" + (labels.length-2));
        //textFields
    }

    public void GuiFunAddVar(){
        addLabel();
        addTextField();
        labels[labels.length-1] = new Label("x" + (labels.length - 1));
        textFields[textFields.length-1] = new TextField("", 5);
    }

    private void addLabel(){
        Label[] temp = new Label[labels.length];
        for(int i = 0; i < labels.length; i++){
            temp[i] = labels[i];
        }
        labels = new Label[temp.length + 1];
        for(int i = 0; i < temp.length; i++){
            labels[i] = temp[i];
        }
    }

    private void addTextField(){
        TextField[] temp = new TextField[buttons.length];
        for(int i = 0; i < textFields.length; i++){
            temp[i] = textFields[i];
        }
        textFields = new TextField[temp.length + 1];
        for(int i = 0; i < temp.length; i++){
            textFields[i] = temp[i];
        }
    }
}
