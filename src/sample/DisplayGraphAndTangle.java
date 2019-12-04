package sample;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisplayGraphAndTangle {

    public static void displayGraph(HashMap<Integer, ArrayList<Integer>> permutationRowGraph, HashMap<Integer, ArrayList<Integer>> permutationColGraph,ArrayList<Integer> rowCycle){
        Stage window = new Stage();
        ArrayList<Point> points = new ArrayList<>();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Permutation Graph");
        Pane wrapperPane = new Pane();
        wrapperPane.setMaxWidth(300);
        wrapperPane.setMaxHeight(300);
        Canvas canvas = new Canvas(300,300);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        wrapperPane.getChildren().addAll(canvas);
        int sides = 4;
        double centerX = 150;
        double centerY = 150;
        double radius = 80;
        double[] XPoints = new double[sides];
        double[] YPoints = new double[sides];
        final double angleStep = Math.PI*2 / sides;
        double angle = 0;
        for (int i = 0; i < sides; i++, angle -= angleStep) {
            XPoints[i] = Math.sin(angle) * radius + centerX; // x coordinate of the corner
            YPoints[i] = Math.cos(angle) * radius + centerY; // y coordinate of the corner
            points.add(new Point(new Point2D(XPoints[i],YPoints[i]),rowCycle.get(i)));
        }
        drawPoints(XPoints,YPoints,gc);

        for(Point point : points){
            Label temp = new Label(Integer.toString(point.getId()));
            temp.setLayoutX(point.getCenter().getX()-5);
            temp.setLayoutY(point.getCenter().getY()-5);
            wrapperPane.getChildren().addAll(temp);
        }

        drawGraph(permutationRowGraph,permutationColGraph,points,gc);
        final String label = "ROW permutation graph is RED\nCOLUMN permutation graph is green";
        Label indexLabel = new Label(label);
        indexLabel.setLayoutX(0);
        indexLabel.setLayoutY(260);
        wrapperPane.getChildren().addAll(indexLabel);
        Scene scene = new Scene(wrapperPane, 300, 300);
        window.setScene(scene);
        window.show();
    }

    private static void drawPoints(double[] XPoints, double[] YPoints, GraphicsContext gc){
        for(int i =0; i<XPoints.length;i++){
            gc.strokeOval(XPoints[i]-10,YPoints[i]-10,20,20);
        }
    }

    private static void drawGraph(HashMap<Integer, ArrayList<Integer>> permutationRowGraph,HashMap<Integer, ArrayList<Integer>> permutationColGraph, ArrayList<Point> points, GraphicsContext gc){
        gc.setLineWidth(3);
        gc.setStroke(Color.RED);
        for(Map.Entry entry : permutationRowGraph.entrySet()){
            ArrayList<Integer> connections = (ArrayList<Integer>) entry.getValue();
            int id = (int) entry.getKey();
            Point2D idPixel = new Point2D(0,0);
            for(Point tempPoint : points){
                if(tempPoint.getId() == id){
                    idPixel = tempPoint.getCenter();
                    break;
                }
            }
            for(int temp : connections){
                Point2D tempPixel = new Point2D(0,0);
                for(Point tempPoint : points){
                    if(tempPoint.getId() == temp){
                        tempPixel = tempPoint.getCenter();
                        break;
                    }
                }
                gc.strokeLine(idPixel.getX()+3,idPixel.getY()+3,tempPixel.getX()+3,tempPixel.getY()+3);
            }
        }

        gc.setStroke(Color.GREEN);
        for(Map.Entry entry : permutationColGraph.entrySet()){
            ArrayList<Integer> connections = (ArrayList<Integer>) entry.getValue();
            int id = (int) entry.getKey();
            Point2D idPixel = new Point2D(0,0);
            for(Point tempPoint : points){
                if(tempPoint.getId() == id){
                    idPixel = tempPoint.getCenter();
                    break;
                }
            }
            for(int temp : connections){
                Point2D tempPixel = new Point2D(0,0);
                for(Point tempPoint : points){
                    if(tempPoint.getId() == temp){
                        tempPixel = tempPoint.getCenter();
                        break;
                    }
                }
                gc.strokeLine(idPixel.getX()-3,idPixel.getY()-3,tempPixel.getX()-3,tempPixel.getY()-3);
            }
        }
    }

    public static void displayTangle(ArrayList<Integer> rowCycle,ArrayList<Integer> columnCycle){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Tangle");
        Pane wrapperPane = new Pane();
        wrapperPane.setMaxWidth(300);
        wrapperPane.setMaxHeight(300);
        Canvas canvas = new Canvas(300,300);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        wrapperPane.getChildren().addAll(canvas);
    }
}
