package calculate;

import java.util.ArrayList;

public class GenerateRightEdge implements Runnable
{
    public KochFractal koch;
    public KochManager manager;
    public ArrayList<Edge> edges;

    public boolean done;

    public GenerateRightEdge(KochManager ma, int nxt)
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
        koch.generateRightEdge();
        if(edges != null)
        {
            manager.addAllEdges(edges);
        }
        done = true;
    }
}
