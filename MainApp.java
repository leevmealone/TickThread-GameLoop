package TickSystemThread;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class MainApp extends Application{
    TickThread gameThread;
    Map<Integer,MethodRunner> stepsMethods = new HashMap<>();
    Map<Integer,Integer> msStep = new HashMap<>();

    int updateCount = 0;
    int drawCount = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.show();
        setupThread();
        this.gameThread.start();
    }
    public void stop(){
        this.gameThread.stopIt();
        System.out.println("Update Count : "+updateCount);
        System.out.println("Draw Count : "+drawCount);
        System.out.println("Runtime : "+this.gameThread.runtime/1000000000+" s");
    }

    public void setupThread(){
        MethodRunner mRUpdate = new MethodRunner("TickSystemThread.MainApp","update",null,this,null);
        MethodRunner mRDraw = new MethodRunner("TickSystemThread.MainApp","draw",null,this,null);
        stepsMethods.put(0,mRUpdate);
        msStep.put(0,1000/60);
        stepsMethods.put(1,mRDraw);
        msStep.put(1,1000/120);
        this.gameThread = new TickThread(msStep, stepsMethods);
    }

    public void update() {
        updateCount++;
    }
    public void draw() {
        drawCount++;
    }
}
