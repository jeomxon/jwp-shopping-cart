package cart.controller.dto;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

public class ProductRequest {

    @NotBlank(message = "상품 이름은 공백을 입력할 수 없습니다.")
    private final String name;
    @NotBlank(message = "상품 사진의 url은 공백을 입력할 수 없습니다.")
    private final String imageUrl;
    @Range(min = 0, max = 100_000_000, message = "상품 금액은 0원 이상 100,000,000원 이하만 가능합니다.")
    private final int price;

    public ProductRequest(final String name, final String imageUrl, final int price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getPrice() {
        return price;
    }
}
