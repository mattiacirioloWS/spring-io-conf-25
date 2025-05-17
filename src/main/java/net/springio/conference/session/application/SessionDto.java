package net.springio.conference.session.application;

import java.math.BigDecimal;
import java.util.UUID;

public class SessionDto {
    private UUID id;
    private String title;
    private String speakers;

    private BigDecimal price;

    SessionDto(UUID id, String title, String speakers, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.speakers = speakers;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpeakers() {
        return speakers;
    }

    public void setSpeakers(String speakers) {
        this.speakers = speakers;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
