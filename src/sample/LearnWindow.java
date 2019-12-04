package sample;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LearnWindow {

    public static void display() {
        Stage window = new Stage();

        final String data = "Assignment - 4 \n" +
                "- By Dhananjay Nikam\n" +
                "The grid has even number of horizontal and vertical edges\n"+
                "A closed path is to be walked so that every turn is 90 to the right\n"+
                "The path has exactly one horizonatal segment on each row and one vertical segment on each column\n"+
                "Following these rules the row and column permutations formed follow the alternating permutation rules\n"+
                "Example: 1, 3, 2, 4 because 1 < 3 > 2 < 4\n"+
                "The program displays the following features:\n"+
                "1:Factoradic number of each row nad column permutation\n"+
                "2:Permutation Graph\n"+
                "3:Tangle to transform column to row permutation\n"+
                "Steps:\n"+
                "Step1: Enter the size of the grid\n"+
                "Step2: Follow the rules to make the grid\n"+
                "Step3: Press \"SHOW DETAILS\" button to view all values\n"+
                "Step4: Press the permutation graph and tangle\n"+
                "Use CLEAR and HOME button to clear the canvas and go to start page respectively.";

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("LEARN MORE");
       // window.setMaxWidth(800);
      //  window.setMaxHeight(800);

        Label label = new Label(data);
        label.setWrapText(true);
        label.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 20; -fx-text-fill: darkred;");
        StackPane layout = new StackPane();
        layout.getChildren().add(label);

        window.setScene(new Scene(layout,900,700));
        window.showAndWait();



    }
}
