package com.egorov.lib_food_categories.util;

import com.egorov.lib_food_categories.dto.request.FoodCategoryRequest;
import com.egorov.lib_food_categories.dto.response.FoodCategoryDto;
import com.egorov.lib_food_categories.model.FoodCategory;
import org.mapstruct.Mapper;

/**
 * Маппер для преобразования между сущностью FoodCategory и DTO. Использует MapStruct для
 * автоматической генерации реализации.
 *
 * @author i.egorov
 */
@Mapper(componentModel = "spring")
public interface FoodCategoryMapper {

  /**
   * Преобразует сущность FoodCategory в DTO.
   *
   * @param entity сущность FoodCategory
   * @return соответствующий DTO объект
   */
  FoodCategoryDto toDto(FoodCategory entity);

  /**
   * Преобразует DTO запроса в сущность FoodCategory.
   *
   * @param dto DTO запроса
   * @return соответствующая сущность FoodCategory
   */
  FoodCategory toEntity(FoodCategoryRequest dto);
}