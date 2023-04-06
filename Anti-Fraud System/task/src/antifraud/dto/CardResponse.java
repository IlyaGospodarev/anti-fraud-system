package antifraud.dto;

import antifraud.model.Card;

public class CardResponse {
    private Long id;
    private String number;

    public CardResponse(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public CardResponse(Card card) {
        this.id = card.getId();
        this.number = card.getNumber();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
