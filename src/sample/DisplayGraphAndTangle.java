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

import java.util.*;

public class DisplayGraphAndTangle {

    public static void displayGraph(HashMap<Integer, ArrayList<Integer>> permutationRowGraph, HashMap<Integer, ArrayList<Integer>> permutationColGraph, ArrayList<Integer> rowCycle) {
        Stage window = new Stage();
        ArrayList<Point> points = new ArrayList<>();
        //window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Permutation Graph");
        Pane wrapperPane = new Pane();
        wrapperPane.setMaxWidth(300);
        wrapperPane.setMaxHeight(300);
        Canvas canvas = new Canvas(300, 300);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        wrapperPane.getChildren().addAll(canvas);
        int sides = rowCycle.size();
        double centerX = 150;
        double centerY = 150;
        double radius = 130;
        double[] XPoints = new double[sides];
        double[] YPoints = new double[sides];
        final double angleStep = Math.PI * 2 / sides;
        double angle = 0;
        for (int i = 0; i < sides; i++, angle -= angleStep) {
            XPoints[i] = Math.sin(angle) * radius + centerX; // x coordinate of the corner
            YPoints[i] = Math.cos(angle) * radius + centerY; // y coordinate of the corner
            points.add(new Point(new Point2D(XPoints[i], YPoints[i]), rowCycle.get(i)));
        }
        drawPoints(XPoints, YPoints, gc);

        for (Point point : points) {
            Label temp = new Label(Integer.toString(point.getId()));
            temp.setLayoutX(point.getCenter().getX() - 5);
            temp.setLayoutY(point.getCenter().getY() - 5);
            wrapperPane.getChildren().addAll(temp);
        }

        drawGraph(permutationRowGraph, permutationColGraph, points, gc);
        final String label = "ROW permutation graph is RED\nCOLUMN permutation graph is green";
        Label indexLabel = new Label(label);
        indexLabel.setLayoutX(0);
        indexLabel.setLayoutY(460);
        wrapperPane.getChildren().addAll(indexLabel);
        Scene scene = new Scene(wrapperPane, 300, 500);
        window.setScene(scene);
        window.show();
    }

    private static void drawPoints(double[] XPoints, double[] YPoints, GraphicsContext gc) {
        for (int i = 0; i < XPoints.length; i++) {
            gc.strokeOval(XPoints[i] - 10, YPoints[i] - 10, 20, 20);
        }
    }

    private static void drawGraph(HashMap<Integer, ArrayList<Integer>> permutationRowGraph, HashMap<Integer, ArrayList<Integer>> permutationColGraph, ArrayList<Point> points, GraphicsContext gc) {
        gc.setLineWidth(3);
        gc.setStroke(Color.RED);
        for (Map.Entry entry : permutationRowGraph.entrySet()) {
            ArrayList<Integer> connections = (ArrayList<Integer>) entry.getValue();
            int id = (int) entry.getKey();
            Point2D idPixel = new Point2D(0, 0);
            for (Point tempPoint : points) {
                if (tempPoint.getId() == id) {
                    idPixel = tempPoint.getCenter();
                    break;
                }
            }
            for (int temp : connections) {
                Point2D tempPixel = new Point2D(0, 0);
                for (Point tempPoint : points) {
                    if (tempPoint.getId() == temp) {
                        tempPixel = tempPoint.getCenter();
                        break;
                    }
                }
                gc.strokeLine(idPixel.getX() + 3, idPixel.getY() + 3, tempPixel.getX() + 3, tempPixel.getY() + 3);
            }
        }

        gc.setStroke(Color.GREEN);
        for (Map.Entry entry : permutationColGraph.entrySet()) {
            ArrayList<Integer> connections = (ArrayList<Integer>) entry.getValue();
            int id = (int) entry.getKey();
            Point2D idPixel = new Point2D(0, 0);
            for (Point tempPoint : points) {
                if (tempPoint.getId() == id) {
                    idPixel = tempPoint.getCenter();
                    break;
                }
            }
            for (int temp : connections) {
                Point2D tempPixel = new Point2D(0, 0);
                for (Point tempPoint : points) {
                    if (tempPoint.getId() == temp) {
                        tempPixel = tempPoint.getCenter();
                        break;
                    }
                }
                gc.strokeLine(idPixel.getX() - 3, idPixel.getY() - 3, tempPixel.getX() - 3, tempPixel.getY() - 3);
            }
        }
    }

    public static void displayTangle(ArrayList<Integer> rowCycle, ArrayList<Integer> sourcePerm) {
        Stage window = new Stage();
        //window.initModality(Modality.APPLICATION_MODAL);
        ArrayList<ArrayList<Integer>> tangle = new ArrayList<>();
        window.setTitle("Tangle");
        Pane wrapperPane = new Pane();
        wrapperPane.setMaxWidth(300);
        wrapperPane.setMaxHeight(400);
        Canvas canvas = new Canvas(300, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        wrapperPane.getChildren().addAll(canvas);

        ArrayList<Integer> diff = new ArrayList<>();
        ArrayList<Integer> temp1 = new ArrayList<>();
        temp1.addAll(sourcePerm);
        tangle.add(temp1);
        calculateDiff(diff, rowCycle, sourcePerm);

        while (Collections.max(diff) > 0) {
            int positionsToMove = Collections.max(diff);
            int numberToMove = diff.indexOf(positionsToMove);

            for (int i = 1; i <= positionsToMove; i++) {
                Collections.swap(sourcePerm, sourcePerm.indexOf(numberToMove), sourcePerm.indexOf(numberToMove) + 1);
                ArrayList<Integer> temp = new ArrayList<>();
                temp.addAll(sourcePerm);
                tangle.add(temp);
            }
            calculateDiff(diff, rowCycle, sourcePerm);
        }
        ArrayList<Point> points = new ArrayList<>();

        for (int i = 0, y = 20; i < tangle.size(); i++, y = y + 20) {
            ArrayList<Integer> temp = tangle.get(i);
            for (int j = 0, x = 20; j < temp.size(); j++, x = x + 20) {
                Point tempPoint = new Point(new Point2D(x, y), temp.get(j));
                points.add(tempPoint);
            }
        }

        HashMap<Integer, ArrayList<Point2D>> pointsHM = new HashMap<>();
        for (Point item : points) {
            int id = item.getId();
            Point2D pixel = item.getCenter();
            Label tempLabel = new Label(Integer.toString(id));
            tempLabel.setLayoutX(pixel.getX());
            tempLabel.setLayoutY(pixel.getY());
            wrapperPane.getChildren().add(tempLabel);
            if (pointsHM.containsKey(id)) {
                pointsHM.get(id).add(pixel);
            } else {
                ArrayList<Point2D> tempPointList = new ArrayList<>();
                tempPointList.add(pixel);
                pointsHM.put(id, tempPointList);
            }

        }
        gc.setLineWidth(1.5);
        for (Map.Entry item : pointsHM.entrySet()) {
            double red = myRandom(0, 1);
            double green = myRandom(0, 1);
            double blue = myRandom(0, 1);
            gc.setStroke(new Color(red, green, blue, 1));
            ArrayList<Point2D> list = (ArrayList<Point2D>) item.getValue();
            for (int i = 0; i < list.size() - 1; i++) {
                Point2D curr = list.get(i);
                Point2D next = list.get(i + 1);
                gc.strokeLine(curr.getX(), curr.getY(), next.getX(), next.getY());
            }
        }

        Scene scene = new Scene(wrapperPane,300,400);
        window.setScene(scene);
        window.show();
    }

    private static double myRandom(double min, double max) {
        Random r = new Random();
        return (r.nextInt((int) ((max - min) * 10 + 1)) + min * 10) / 10.0;
    }

    private static void calculateDiff(ArrayList<Integer> diff, ArrayList<Integer> dest, ArrayList<Integer> source) {
        diff.clear();
        diff.add(0);
        for (int i = 1; i <= dest.size(); i++) {
            diff.add(dest.indexOf(i) - source.indexOf(i));
        }
    }
}