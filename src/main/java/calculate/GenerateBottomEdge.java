package calculate;

import java.util.ArrayList;

public class GenerateBottomEdge implements Runnable
{
    public KochFractal koch;
    public KochManager manager;
    public ArrayList<Edge> edges;

    public boolean done;

    public GenerateBottomEdge(KochManager ma, int nxt)
    {
        edges = new ArrayList<>();
        manager = ma;
        koch = new KochFractal(ma);
        koch.setLevel(nxt);

        done = false;
    }

    @Override
    public void run()
    {
        koch.generateBottomEdge();
        done = true;
    }
}
