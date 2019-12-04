package sample;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class Main extends Application {
    private Stage window = new Stage();
    Scene startPageScene;
    private int height = 600;
    private int width = 800;
    private ArrayList<PositionId> reversePoints = new ArrayList<>();
    private HashMap<Integer,Integer> hashMapRowCount = new HashMap<>();
    private HashMap<Integer,Integer> hashMapColumnCount = new HashMap<>();
    private ArrayList<Integer> rowCycle = new ArrayList<>();
    private ArrayList<Integer> columnCycle = new ArrayList<>();
    private CircularList points = new CircularList();
    private HashMap<Integer, ArrayList<Integer>> permutationRowGraph = new HashMap<>();
    private HashMap<Integer, ArrayList<Integer>> permutationColGraph = new HashMap<>();
    private boolean isFirstClick = true;
    private boolean isRowGreater = true;
    private boolean isColGreater = true;
    private int gridSize =0;
    private boolean isGridComplete = false;
    PositionId startingPoint;
    Point2D startingPointPixel;


    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        window.setScene(getStartPageScene());
        window.setTitle("Assignment 4");
        window.show();
    }

    private Scene getStartPageScene() {
        BackgroundFill backgroundFill = new BackgroundFill(Color.PALETURQUOISE,new CornerRadii(0),new Insets(0,0,0,0));
        Background bck = new Background(backgroundFill);
        MenuBar menuBar = new MenuBar();

        Menu startMenu = new Menu("START");
        MenuItem gameMenu = new MenuItem("GRIDS GAME");
        Menu moreAbout = new Menu("More about project");
        MenuItem learn = new MenuItem("LEARN");
        startMenu.getItems().add(gameMenu);
        moreAbout.getItems().addAll(learn);
        menuBar.getMenus().addAll(startMenu,moreAbout);
        menuBar.setStyle("-fx-font-size: 1.2em; -fx-background-color: #ffffff;-fx-border-style: solid inside;-fx-border-width: 2;-fx-border-insets: 5;-fx-border-radius: 5; -fx-border-color: blue; ");

        Scene gameScene = getGameScene();
        gameMenu.setOnAction(e -> {
            window.setScene(gameScene);
        });

        Label introLabel = new Label("Assignment - 4");
        introLabel.setStyle("-fx-font-size: 4em; -fx-background-color: #ffffff; ");
        VBox centreStartScene = new VBox();
        centreStartScene.setAlignment(Pos.CENTER);
        centreStartScene.setPadding(new Insets(20,10,10,10));
        introLabel.setWrapText(true);
        centreStartScene.getChildren().add(introLabel);

        //BorderPane layout to get all the layouts together
        BorderPane startPageLayout = new BorderPane();
        startPageLayout.setTop(menuBar);
        startPageLayout.setCenter(centreStartScene);
        startPageLayout.setBackground(bck);

        startPageScene = new Scene(startPageLayout,width,height);
        return startPageScene;
    }

    private Scene getGameScene() {
        Scene gameScene;
        HashMap<Point2D,PositionId> pixelToPid = new HashMap<>();
        ArrayList<Point2D> pixelList = new ArrayList<>();
        Label rowCycleLabel = new Label("Row cycle:");
        Label colCycleLable = new Label("Column cycle:");
        Label factoradicRow = new Label("Factoradic of row cycle:");
        Label factoradicCol = new Label("Factoradic of column cycle:");
        HBox gridSizeHBox = new HBox(10);
        Label gridSizeTextLabel = new Label("Enter an even size of the grid (4-12)");
        TextField gridSizeText = new TextField();
        gridSizeText.setPrefWidth(50);
        gridSizeText.setPromptText("4-12");
        gridSizeHBox.getChildren().addAll(gridSizeTextLabel,gridSizeText);
        Button gridComplete = new Button("SHOW DETAILS");
        Button permutationGraph = new Button("PERMUTATION GRAPH");
        Button tangle = new Button("TANGLE");
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(rowCycleLabel,colCycleLable,factoradicRow,factoradicCol,gridSizeHBox,gridComplete,permutationGraph,tangle);
        Insets insets = new Insets(10,10,10,10);
        Pane wrapperPane = new Pane();
        Canvas canvas = new Canvas(325,325);
        wrapperPane.setMaxHeight(325);
        wrapperPane.setMaxWidth(325);
        wrapperPane.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY,Insets.EMPTY)));
        draw(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        wrapperPane.getChildren().addAll(canvas);

        Pane wrapperPane2 = new Pane();
        Canvas canvas2 = new Canvas(325,325);
        wrapperPane2.setMaxHeight(325);
        wrapperPane2.setMaxWidth(325);
        wrapperPane2.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY,Insets.EMPTY)));
        draw(canvas2);
        GraphicsContext gc2 = canvas.getGraphicsContext2D();
        wrapperPane2.getChildren().addAll(canvas2);
        ArrayList<Label> labelList1 = new ArrayList<>();
        ArrayList<Label> labelList2 = new ArrayList<>();
        plotGrid(canvas,labelList1,gridSize);
        plotGrid(canvas2,labelList2,gridSize);
        BorderPane border = new BorderPane();
        border.setTop(wrapperPane);
        border.setLeft(wrapperPane2);
        border.setCenter(vbox);
        BorderPane.setMargin(border.getLeft(),insets);
        BorderPane.setMargin(border.getTop(),insets);
        BorderPane.setMargin(border.getCenter(),insets);

        gridSizeText.setOnAction(e -> {
            if(validateGridSize(gridSizeText.getText())){
                gridSize = Integer.parseInt(gridSizeText.getText());
                setPixelToPid(pixelToPid,pixelList,gridSize);
                plotGrid(canvas,labelList1,gridSize);
                plotGrid(canvas2,labelList2,gridSize);
                wrapperPane.getChildren().addAll(labelList1);
                wrapperPane2.getChildren().addAll(labelList2);
            }else {
                SelectionBox.display("ERROR","Enter a valid number between 4 & 12", "OK", null);
            }
        });

        canvas.setOnMouseClicked(e-> {
            Point2D temp = new Point2D(e.getX(),e.getY());
            if(!isGridComplete) {
                for (Point2D item : pixelList) {
                    if (temp.distance(item) <= 10) {
                        PositionId tempPos = pixelToPid.get(item);
                        if (isFirstClick) {
                            points.add(tempPos);
                            rowCycle.add(tempPos.row);
                            columnCycle.add(tempPos.col);
                            hashMapColumnCount.put(tempPos.col, 1);
                            hashMapRowCount.put(tempPos.row, 1);
                            gc.setFill(Color.RED);
                            gc.fillOval(item.getX() - 5, item.getY() - 5, 15, 15);
                            gc.setFill(Color.BLACK);
                            isFirstClick = false;
                            startingPoint = tempPos;
                            startingPointPixel = getPreviousPointPixel(startingPoint, pixelToPid);
                            break;
                        } else {
                            if (validatePoint(tempPos)) {
                                points.add(tempPos);
                                rowCycle.add(tempPos.row);
                                columnCycle.add(tempPos.col);
                                Point2D prev = getPreviousPointPixel(points.get(points.size() - 2), pixelToPid);
                                gc.setFill(Color.RED);
                                gc.setStroke(Color.RED);
                                gc.fillOval(item.getX() - 5, item.getY() - 5, 15, 15);
                                gc.strokeLine(item.getX(), item.getY(), prev.getX(), prev.getY());
                                if((tempPos.col == startingPoint.col || tempPos.row == startingPoint.row) && points.size() > 2) {
                                    gc.strokeLine(item.getX(), item.getY(), startingPointPixel.getX(), startingPointPixel.getY());
                                    isGridComplete = true;
                                }
                                gc.setFill(Color.BLACK);
                                gc.setStroke(Color.BLACK);
                                break;
                            }else {
                                SelectionBox.display("ERROR","Wrong move. Can move only to the right, each column and row can have only 1 segment", "OK",null);
                            }
                        }
                    }
                }
            }
        });

        gridComplete.setOnAction(e -> {
            if(isGridComplete){
                makeRowAndColumnCycles();
                rowCycleLabel.setText("Row cycle= "+arraysListToString(rowCycle));
                colCycleLable.setText("Column cycle= "+arraysListToString(columnCycle));
                getReversePoints();
                plotReverseGrid(canvas2,pixelToPid);
                factoradicRow.setText("Factoradic of row cycle= " + getFactoradic(rowCycle));
                factoradicCol.setText("Factoradic of column cycle= " + getFactoradic(columnCycle));
            }else {
                SelectionBox.display("ERROR","Complete all the steps", "OK",null);
            }
        });

        permutationGraph.setOnAction(e -> {
            getPermutationGraph();
            DisplayGraphAndTangle.displayGraph(permutationRowGraph,permutationColGraph,rowCycle);
        });

        tangle.setOnAction(e -> {
            ArrayList<Integer> tempColumn = new ArrayList<>();
            tempColumn.addAll(columnCycle);
            DisplayGraphAndTangle.displayTangle(rowCycle,tempColumn);
        });

        gameScene = new Scene(border,800,690);
        return gameScene;
    }

    private String arraysListToString(ArrayList<Integer> list){
        StringBuilder string = new StringBuilder();
        for(int item : list){
            string.append(item);
            string.append(" ");
        }
        return string.toString();
    }

    private void makeRowAndColumnCycles() {
        rowCycle = (ArrayList<Integer>) rowCycle.stream().distinct().collect(Collectors.toList());
        columnCycle = (ArrayList<Integer>) columnCycle.stream().distinct().collect(Collectors.toList());
    }

    private void getReversePoints() {
        for(int i = 0; i< rowCycle.size(); i++){
            reversePoints.add(new PositionId(columnCycle.get(i),rowCycle.get(i)));
        }
        reversePoints.add(reversePoints.get(0));
        for(int i =0; i< reversePoints.size()-1; i=i+2){
            PositionId temp = new PositionId(reversePoints.get(i).row, reversePoints.get(i+1).col);
            reversePoints.add(i+1,temp);
        }
    }

    private void plotReverseGrid(Canvas canvas, HashMap<Point2D, PositionId> pixelToPosition){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for(int i =0; i<reversePoints.size()-1; i++){
            Point2D first = getPreviousPointPixel(reversePoints.get(i),pixelToPosition);
            Point2D second = getPreviousPointPixel(reversePoints.get(i+1),pixelToPosition);
            gc.setFill(Color.RED);
            gc.setStroke(Color.RED);
            gc.fillOval(first.getX()-5,first.getY()-5,15,15);
            gc.fillOval(second.getX()-5,second.getY()-5,15,15);
            gc.strokeLine(first.getX(),first.getY(),second.getX(),second.getY());
            gc.setFill(Color.BLACK);
            gc.setStroke(Color.BLACK);
        }
    }
    private Point2D getPreviousPointPixel(PositionId positionId,HashMap<Point2D,PositionId> pixelToPid) {
        Point2D pixel = new Point2D(0,0);
        for(Map.Entry entry : pixelToPid.entrySet()){
            if(entry.getValue().equals(positionId)){
                pixel = (Point2D) entry.getKey();
            }
        }
        return pixel;
    }

    private boolean validateGridSize(String text) {
        try{
            int number = Integer.parseInt(text);
            if(number >= 4 && number <= 12 && number % 2 == 0){
                return true;
            }
        }catch (NumberFormatException e){
            return false;
        }
        return false;
    }

    private void draw(Canvas canvas){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.strokeRect(0,0,canvas.getWidth(),canvas.getHeight());
    }

    private void plotGrid(Canvas canvas, ArrayList<Label> labelList, int gridSize){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for(int x = 25; x<=gridSize*25 ; x=x+25){
            gc.strokeLine(x,25,x,gridSize*25);
        }

        for(int y = 25; y<=gridSize*25 ; y=y+25){
            gc.strokeLine(25,y,gridSize*25,y);
        }

        for(int i =1,x=20,y=10; i<= gridSize; i++,x=x+25){
            Label temp = new Label(Integer.toString(i));
            temp.setLayoutX(x);
            temp.setLayoutY(y);
            labelList.add(temp);
        }
        for(int i =1,x=10,y=20; i<= gridSize; i++,y=y+25){
            Label temp = new Label(Integer.toString(i));
            temp.setLayoutX(x);
            temp.setLayoutY(y);
            labelList.add(temp);
        }
    }

    private void setPixelToPid(HashMap<Point2D,PositionId> pixelToPid,ArrayList<Point2D> pixelList, int gridSize){
        int k =1,l=1;
        for(double i=25; i<= gridSize*25; i = i+25,k++){
            for(double j = 25; j<= gridSize*25; j = j+25,l++){
                pixelToPid.put(new Point2D(i,j),new PositionId(l,k));
                pixelList.add(new Point2D(i,j));
            }
            l = 1;
        }
    }

    private boolean validatePoint(PositionId position){
        if(position.row != rowCycle.get(rowCycle.size()-1) && position.col != columnCycle.get(columnCycle.size()-1)){
            return false;
        }
        if(isRowGreater){
            if(position.row >= rowCycle.get(rowCycle.size()-1)){
                if(hashMapRowCount.containsKey(position.row)){
                    if(hashMapRowCount.get(position.row) == 2){
                        return false;
                    }else{
                        int tempVal = hashMapRowCount.get(position.row);
                        hashMapRowCount.put(position.row,tempVal+1);
                        if(position.row != rowCycle.get(rowCycle.size()-1))
                            isRowGreater = false;
                    }
                }else{
                    hashMapRowCount.put(position.row,1);
                    if(position.row != rowCycle.get(rowCycle.size()-1))
                        isRowGreater = false;
                }
            }else {
                return false;
            }
        }else{
            if(position.row <= rowCycle.get(rowCycle.size()-1)){
                if(hashMapRowCount.containsKey(position.row)){
                    if(hashMapRowCount.get(position.row) == 2){
                        return false;
                    }else{
                        int tempVal = hashMapRowCount.get(position.row);
                        hashMapRowCount.put(position.row,tempVal+1);
                        if(position.row != rowCycle.get(rowCycle.size()-1))
                            isRowGreater = true;
                    }
                }else{
                    hashMapRowCount.put(position.row,1);
                    if(position.row != rowCycle.get(rowCycle.size()-1))
                        isRowGreater = true;
                }
            }else {
                return false;
            }
        }


        if(isColGreater){
            if(position.col >= columnCycle.get(rowCycle.size()-1)){
                if(hashMapColumnCount.containsKey(position.col)){
                    if(hashMapColumnCount.get(position.col) == 2){
                        return false;
                    }else{
                        int tempVal = hashMapColumnCount.get(position.col);
                        hashMapColumnCount.put(position.col,tempVal+1);
                        if(position.col != columnCycle.get(columnCycle.size()-1))
                            isColGreater = false;
                    }
                }else{
                    hashMapColumnCount.put(position.col,1);
                    if(position.col != columnCycle.get(columnCycle.size()-1))
                        isColGreater = false;
                }
            }else {
                return false;
            }
        }else {
            if(position.col <= columnCycle.get(rowCycle.size()-1)){
                if(hashMapColumnCount.containsKey(position.col)){
                    if(hashMapColumnCount.get(position.col) == 2){
                        return false;
                    }else{
                        int tempVal = hashMapColumnCount.get(position.col);
                        hashMapColumnCount.put(position.col,tempVal+1);
                        if(position.col != columnCycle.get(columnCycle.size()-1))
                            isColGreater = true;
                    }
                }else{
                    hashMapColumnCount.put(position.col,1);
                    if(position.col != columnCycle.get(columnCycle.size()-1))
                        isColGreater = true;
                }
            }else {
                return false;
            }
        }
        return true;
    }

    private String getFactoradic(ArrayList<Integer> cycle){
        ArrayList<Integer> temp = new ArrayList<>();
        int[] factoradic = new int[cycle.size()];
        for(int i = cycle.size()-1; i >=0; i--){
            temp.add(cycle.get(i));
            Collections.sort(temp);
            factoradic[i]= temp.indexOf(cycle.get(i))+1;
        }
        ArrayList<Integer> facto = new ArrayList<>();
        for(int i =0; i < factoradic.length;i++){
            facto.add(factoradic[i]);
        }
        return arraysListToString(facto);
    }

    private void getPermutationGraph(){
        for(int i = 0; i < rowCycle.size()-1;i++){
            for(int j =i+1; j<rowCycle.size(); j++){
                if(rowCycle.get(j) < rowCycle.get(i)){
                    if(permutationRowGraph.containsKey(rowCycle.get(i))){
                        permutationRowGraph.get(rowCycle.get(i)).add(rowCycle.get(j));
                    }else {
                        ArrayList<Integer> temp = new ArrayList<>();
                        temp.add(rowCycle.get(j));
                        permutationRowGraph.put(rowCycle.get(i),temp);
                    }
                }
            }
        }

        for(int i = 0; i < columnCycle.size()-1;i++){
            for(int j =i+1; j<columnCycle.size(); j++){
                if(columnCycle.get(j) < columnCycle.get(i)){
                    if(permutationColGraph.containsKey(columnCycle.get(i))){
                        permutationColGraph.get(columnCycle.get(i)).add(columnCycle.get(j));
                    }else {
                        ArrayList<Integer> temp = new ArrayList<>();
                        temp.add(columnCycle.get(j));
                        permutationColGraph.put(columnCycle.get(i),temp);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
