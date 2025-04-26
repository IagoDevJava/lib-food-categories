package com.egorov.lib_food_categories.dto.response;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;

/**
 * DTO для представления категории продуктов в виде дерева.
 * Содержит информацию о категории и ее подкатегориях в иерархической структуре.
 *
 * @author i.egorov
 * @param id уникальный идентификатор категории
 * @param name название категории
 * @param description описание категории (может быть null)
 * @param isFinal флаг, указывающий является ли категория конечной (не имеет подкатегорий)
 * @param subCategories список подкатегорий (дочерних категорий)
 * @param createdAt дата и время создания категории
 */
@Builder
public record FoodCategoryTreeDto(
    Long id,
    String name,
    String description,
    boolean isFinal,
    List<FoodCategoryTreeDto> subCategories,
    ZonedDateTime createdAt
) {

}