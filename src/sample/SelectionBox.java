package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class SelectionBox {
    static String selection;

    public static String display(String title, String message,String firstButton, String secondButton){

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label(message);

        Button button1 = new Button(firstButton);
        Button button2 = new Button(secondButton);

        if(secondButton == null)
            button2.setDisable(true);

        button1.setOnAction(e -> {
            selection = firstButton;
            window.close();
        });
        button2.setOnAction(e -> {
            selection = secondButton;
            window.close();
        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,button1, button2);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        return selection;
    }
}
