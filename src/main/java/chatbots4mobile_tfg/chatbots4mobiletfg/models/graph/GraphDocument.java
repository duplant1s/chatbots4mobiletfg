package chatbots4mobile_tfg.chatbots4mobiletfg.models.graph;

public class GraphDocument  extends GraphNode {

    private String text;
    private String disambiguatingDescription;

    public GraphDocument(String nodeId, String text, String disambiguatingDescription) {
        super(nodeId);
        this.text = text;
        this.disambiguatingDescription = disambiguatingDescription;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDisambiguatingDescription() {
        return disambiguatingDescription;
    }

    public void setDisambiguatingDescription(String disambiguatingDescription) {
        this.disambiguatingDescription = disambiguatingDescription;
    }
}