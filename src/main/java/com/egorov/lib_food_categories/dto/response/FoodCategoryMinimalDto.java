package com.egorov.lib_food_categories.dto.response;

/**
 * Минимальное DTO для представления категории продуктов.
 * Содержит только базовую информацию о категории.
 *
 * @author i.egorov
 * @param id уникальный идентификатор категории
 * @param name название категории
 */
public record FoodCategoryMinimalDto(
    Long id,
    String name
) {

}