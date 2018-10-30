package calculate;

import java.util.ArrayList;

public class GenerateLeftEdge implements Runnable
{
    public KochFractal koch;
    public KochManager manager;
    public ArrayList<Edge> edges;

    public boolean done;

    public GenerateLeftEdge(KochManager ma, int nxt)
    {
        edges = new ArrayList<>();
        edges = null;
        koch = new KochFractal(ma);
        koch.setLevel(nxt);

        done = false;
    }

    @Override
    public void run()
    {
        koch.generateLeftEdge();
        if(edges != null)
        {
            manager.addAllEdges(edges);
        }
        done = true;
    }
}
