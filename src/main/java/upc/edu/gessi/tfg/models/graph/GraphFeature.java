package upc.edu.gessi.tfg.models.graph;

public class GraphFeature  extends GraphNode {

    private String name;

    public GraphFeature(String nodeId, String name) {
        super(nodeId);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}