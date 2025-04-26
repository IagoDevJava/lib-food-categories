package com.egorov.lib_food_categories.dto.exception;

/**
 * Исключение, выбрасываемое при попытке доступа к несуществующей категории продуктов.
 *
 * @author i.egorov
 */
public class FoodCategoryNotFoundException extends RuntimeException {
  public FoodCategoryNotFoundException(Long id) {
    super("Food category not found with id: " + id);
  }
}