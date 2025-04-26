package com.egorov.lib_food_categories.service;

import com.egorov.lib_food_categories.dto.exception.FoodCategoryNotFoundException;
import com.egorov.lib_food_categories.dto.request.FoodCategoryRequest;
import com.egorov.lib_food_categories.dto.response.FoodCategoryTreeDto;
import com.egorov.lib_food_categories.model.FoodCategory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Интерфейс сервиса для работы с категориями продуктов. Определяет контракт для CRUD-операций и
 * работы с иерархией категорий.
 *
 * @author i.egorov
 */
public interface FoodCategoryService {

  /**
   * Создает новую категорию продуктов.
   *
   * @param foodCategory данные новой категории
   * @return созданная категория
   * @throws FoodCategoryNotFoundException если родительская категория не найдена
   */
  FoodCategory create(FoodCategory foodCategory);

  /**
   * Находит категорию по идентификатору.
   *
   * @param id идентификатор категории
   * @return найденная категория
   * @throws FoodCategoryNotFoundException если категория не найдена
   */
  FoodCategory findById(Long id);

  /**
   * Возвращает страницу со всеми категориями продуктов.
   *
   * @param pageable параметры пагинации и сортировки
   * @return страница категорий
   */
  Page<FoodCategory> findAll(Pageable pageable);

  /**
   * Возвращает иерархическое дерево категорий.
   *
   * @return список корневых категорий с вложенными подкатегориями
   */
  List<FoodCategoryTreeDto> getCategoryTree();

  /**
   * Обновляет данные категории.
   *
   * @param id      идентификатор категории
   * @param request новые данные категории
   * @return обновленная категория
   * @throws FoodCategoryNotFoundException если категория или родительская категория не найдены
   */
  FoodCategory update(Long id, FoodCategoryRequest request);

  /**
   * Удаляет категорию по идентификатору.
   *
   * @param id идентификатор категории
   * @throws FoodCategoryNotFoundException если категория не найдена
   */
  @Transactional
  void delete(Long id);
}