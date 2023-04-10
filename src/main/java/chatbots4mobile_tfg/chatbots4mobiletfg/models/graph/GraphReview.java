package chatbots4mobile_tfg.chatbots4mobiletfg.models.graph;

public class GraphReview extends GraphNode {

    private Integer reviewRating;
    private String reviewBody;

    public GraphReview(String nodeId, Integer reviewRating, String reviewBody) {
        super(nodeId);
        this.reviewRating = reviewRating;
        this.reviewBody = reviewBody;
    }

    public Integer getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(Integer reviewRating) {
        this.reviewRating = reviewRating;
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }
}