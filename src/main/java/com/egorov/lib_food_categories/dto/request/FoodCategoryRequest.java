package com.egorov.lib_food_categories.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * DTO для создания или обновления категории продуктов. Содержит основные данные, необходимые для
 * работы с категориями.
 *
 * @author i.egorov
 */
@Builder
public record FoodCategoryRequest(
    Long parentId,

    @NotBlank
    @Size(max = 100)
    String name,

    String description,

    boolean isFinal
) {

}