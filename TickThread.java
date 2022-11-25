package TickSystemThread;

import java.util.Collections;
import java.util.Map;

public class TickThread extends Thread {
    private volatile boolean running = true;
    public long runtime = 0;
    private long loopTime = 0;
    private long lastLoopTime = 0;

    private Map<Integer,Integer> msSteps;
    private Map<Integer,MethodRunner> stepsMethods;

    // TickThread function who accept multiples steps dynamically.
    // we pass to the class a map of steps in milliseconds and a map of methods, calls with the class MethodRunner.
    // The thread loop take the Integer map key to correlate steps and methods.
    // Actually there's a gap of 1200 nanoseconds (it's 1,2 microsecond and 0,0000012 seconds) cause sometimes,
    // the loop doesn't start at the exact time, so without this gap, he will pass out and doesn't trigger the event.
    // 1200 was the best I've found to have precision. For some reasons, more the steps are short, more precise they are.
    // I have 99.9% of precision at 60/s in 1 min.
    public TickThread(Map<Integer,Integer> msSteps, Map<Integer,MethodRunner> stepsMethods){
        this.msSteps = msSteps;
        this.stepsMethods = stepsMethods;
        if (this.msSteps.size() != this.stepsMethods.size()
            || !this.msSteps.keySet().equals(this.stepsMethods.keySet())
            || this.msSteps.isEmpty()){
            throw new RuntimeException("TickThread Runtime Exception : milliseconds steps and " +
                    "methods steps maps must have equivalent size." +
                    "The thread call specified method at specified time in correlation with key values. They have to be equal.");
        }
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();
        long startLoop = System.nanoTime();
        while(running){

            this.runtime = System.nanoTime() - startTime;
            this.loopTime = System.nanoTime() - startLoop;
            int x = 0;

            for(Map.Entry<Integer,Integer> entry : this.msSteps.entrySet()){
                x++;
                if (entry.getValue()*1000000 <= this.loopTime+1200
                    && entry.getValue()*1000000 >= this.loopTime-1200){
                    stepsMethods.get(entry.getKey()).run();
                }
                else if((entry.getValue()*1000000)*x <= this.loopTime+1200
                        && (entry.getValue()*1000000)*x >= this.loopTime-1200){
                    stepsMethods.get(entry.getKey()).run();
                }
            }
            if(this.loopTime >= Collections.max(this.msSteps.values())*1000000){
                startLoop = System.nanoTime();
            }
        }
        System.out.println("The TickThread loop is stopped");
        System.out.println("Runtime : "+this.runtime+" ns");
    }
    public void stopIt(){
        this.running = false;
    }
}
