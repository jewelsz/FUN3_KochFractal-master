/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import fun3kochfractalfx.FUN3KochFractalFX;
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


    public GenerateBottomEdge bottomEdge;
    public GenerateLeftEdge leftEdge;
    public GenerateRightEdge rightEdge;

    //private KochFractal koch;
    private ArrayList<Edge> edges;
    private FUN3KochFractalFX application;
    private TimeStamp tsCalc;
    private TimeStamp tsDraw;

    public static boolean abort = false;

    public KochManager(FUN3KochFractalFX application)
    {
        this.edges = new ArrayList<Edge>();
        //this.koch = new KochFractal(this);
        this.application = application;
        this.tsCalc = new TimeStamp();
        this.tsDraw = new TimeStamp();
    }

    public void changeLevel(int nxt)
    {
        edges.clear();
        tsCalc.init();
        tsCalc.setBegin("Begin calculating");

        //Instantiate classes
        bottomEdge = new GenerateBottomEdge(this, nxt);
        leftEdge = new GenerateLeftEdge(this, nxt);
        rightEdge = new GenerateRightEdge(this, nxt);

        //Instantiate Threads
        thread1 = new Thread(bottomEdge);
        thread2 = new Thread(leftEdge);
        thread3 = new Thread(rightEdge);

        //Start Threads
        thread1.start();
        thread2.start();
        thread3.start();

        edges.addAll(leftEdge.edges);
        edges.addAll(bottomEdge.edges);
        edges.addAll(rightEdge.edges);

        tsCalc.setEnd("End calculating");
        application.setTextCalc(tsCalc.toString());

        drawEdges();

        thread1 = null;
        thread2 = null;
        thread3 = null;
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

    public void addEdge(Edge e) {
        edges.add(e);
    }

}
