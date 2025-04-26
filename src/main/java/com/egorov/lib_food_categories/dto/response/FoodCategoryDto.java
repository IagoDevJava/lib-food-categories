package com.egorov.lib_food_categories.dto.response;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для представления категории продуктов.
 * Содержит основную информацию о категории, включая связь с родительской категорией.
 *
 * @author i.egorov
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodCategoryDto {

  /**
   * Уникальный идентификатор категории
   */
  private Long id;

  /**
   * Идентификатор родительской категории.
   * Может быть null, если категория является корневой.
   */
  private Long parentId;

  /**
   * Название категории.
   * Не должно быть пустым.
   */
  private String name;

  /**
   * Описание категории.
   * Может содержать дополнительную информацию о категории.
   */
  private String description;

  /**
   * Флаг, указывающий является ли категория конечной.
   * Конечные категории не могут иметь подкатегорий.
   */
  private boolean isFinal;

  /**
   * Дата и время создания категории.
   * Формат: ISO-8601 с учетом временной зоны.
   */
  private ZonedDateTime createdAt;

  /**
   * Дата и время последнего обновления категории.
   * Формат: ISO-8601 с учетом временной зоны.
   */
  private ZonedDateTime updatedAt;
}