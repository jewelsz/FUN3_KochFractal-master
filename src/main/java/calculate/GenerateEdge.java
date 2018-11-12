package calculate;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class GenerateEdge extends Task<ArrayList<Edge>> implements Observer
{
    public KochFractal koch;
    public KochManager manager;

    public ArrayList<Edge> edges;

    public EdgeType type;

    public int count;
    public int maxCount;


    public GenerateEdge(KochManager ma, int nxt, EdgeType type)
    {
        edges = new ArrayList<>();
        koch = new KochFractal(ma);

        this.manager = ma;
        this.type = type;
        
        koch.setLevel(nxt);
        koch.addObserver(this);

        maxCount = koch.getNrOfEdges() / 3;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        edges.add((Edge)arg);
        count++;

        updateProgress(count, maxCount);
    }

    @Override
    protected ArrayList<Edge> call() throws Exception
    {
        switch (type)
        {
            case LEFT:
                koch.generateLeftEdge();
                break;

            case RIGHT:
                koch.generateRightEdge();
                break;

            case BOTTOM:
                koch.generateBottomEdge();
                break;
        }

        return this.edges;
    }

    @Override
    protected void done()
    {
        super.done();
        if(!manager.isCancelled())
        {
            manager.addAllEdges(edges);
        }
    }

    @Override
    protected void cancelled()
    {
        super.cancelled();
        koch.cancel();
    }
}
