package com.alican.graduationproject1;

public class PostBuilder {

    private Post post;

    private PostBuilder() {
        this.post = new Post();
    }

    public static PostBuilder builder() {
        return new PostBuilder();
    }

    public PostBuilder postId(String postId) {
        this.post.setPostId(postId);
        return this;
    }
    public PostBuilder imageUrl(String imageUrl) {
        this.post.setImageUrl(imageUrl);
        return this;
    }
    public PostBuilder title(String title) {
        this.post.setTitle(title);
        return this;
    }
    public PostBuilder category(String category) {
        this.post.setCategory(category);
        return this;
    }
    public PostBuilder explanation(String explanation) {
        this.post.setExplanation(explanation);
        return this;
    }
    public PostBuilder date(String date) {
        this.post.setWorkDate(date);
        return this;
    }
    public PostBuilder start_time(String start_time) {
        this.post.setStartTime(start_time);
        return this;
    }
    public PostBuilder finish_time(String finish_time) {
        this.post.setFinishTime(finish_time);
        return this;
    }
    public PostBuilder payment(String payment) {
        this.post.setPayment(payment);
        return this;
    }
    public PostBuilder address(String address) {
        this.post.setAddress(address);
        return this;
    }
    public PostBuilder latitude(String latitude) {
        this.post.setLatitude(latitude);
        return this;
    }
    public PostBuilder longitude(String longitude) {
        this.post.setLongitude(longitude);
        return this;
    }

    public Post build() {
        return this.post;
    }
}
