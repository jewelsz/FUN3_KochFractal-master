/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.*;
import java.util.concurrent.*;

import fun3kochfractalfx.FUN3KochFractalFX;
import javafx.application.Platform;
import timeutil.TimeStamp;

/**
 *
 * @author Nico Kuijpers
 * Modified for FUN3 by Gertjan Schouten
 */
public class KochManager
{
    private Thread thread1;
    private Thread thread2;
    private Thread thread3;


    private GenerateEdge bottomEdge;
    private GenerateEdge leftEdge;
    private GenerateEdge rightEdge;

    private boolean isCancelled;

    private KochFractal koch;
    private ArrayList<Edge> edges;
    private FUN3KochFractalFX application;
    private TimeStamp tsCalc;
    private TimeStamp tsDraw;

    private ExecutorService pool;

    private int threadCount;

    public KochManager(FUN3KochFractalFX application)
    {
        this.koch = new KochFractal(this);
        this.application = application;
        pool = Executors.newFixedThreadPool(3);

        isCancelled = false;
        this.edges = new ArrayList<>();

        this.tsCalc = new TimeStamp();
        this.tsDraw = new TimeStamp();

        threadCount = 0;
    }

    public void changeLevel(int nxt)
    {
        this.isCancelled = false;
        koch.setLevel(nxt);
        edges.clear();
        tsCalc.init();
        tsCalc.setBegin("Begin calculating");

        //Instantiate classes
        bottomEdge = new GenerateEdge(this, nxt, EdgeType.BOTTOM);
        leftEdge = new GenerateEdge(this, nxt, EdgeType.LEFT);
        rightEdge = new GenerateEdge(this, nxt, EdgeType.RIGHT);

        pool.submit(bottomEdge);
        pool.submit(leftEdge);
        pool.submit(rightEdge);

    }

    public void drawEdges()
    {
        tsDraw.init();
        tsDraw.setBegin("Begin drawing");
        application.clearKochPanel();
        for (Edge e : edges) {
            application.drawEdge(e);
        }
        tsDraw.setEnd("End drawing");
        application.setTextDraw(tsDraw.toString());
    }


    public void cancel()
    {
        this.isCancelled = true;
        bottomEdge.cancel(true);
        leftEdge.cancel(true);
        rightEdge.cancel(true);
    }

    public boolean isCancelled()
    {
        return isCancelled;
    }

    public void shutDown()
    {
        pool.shutdown();
    }

    public synchronized void threadDone() throws ExecutionException, InterruptedException
    {
        threadCount++;

        if(threadCount % 3 ==0)
        {
            Platform.runLater(() ->
            {
                tsCalc.setEnd("End calculating");
                application.setTextNrEdges("" + koch.getNrOfEdges());
                application.setTextCalc(tsCalc.toString());

                drawEdges();

            });
        }
    }

    public synchronized void addAllEdges(ArrayList<Edge>edgesList)
    {
        edges.addAll(edgesList);

        if(leftEdge.isDone() && rightEdge.isDone() && bottomEdge.isDone() &&!isCancelled)
        {
            cancel();
            Platform.runLater(() ->
            {
                tsCalc.setEnd("End calculating");
                application.setTextNrEdges("" + koch.getNrOfEdges());
                application.setTextCalc(tsCalc.toString());
                application.requestDrawEdges();

            });
        }
    }

}




