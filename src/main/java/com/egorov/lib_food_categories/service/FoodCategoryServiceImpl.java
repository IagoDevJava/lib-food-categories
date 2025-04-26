package com.egorov.lib_food_categories.service;

import com.egorov.lib_food_categories.dto.exception.FoodCategoryNotFoundException;
import com.egorov.lib_food_categories.dto.request.FoodCategoryRequest;
import com.egorov.lib_food_categories.dto.response.FoodCategoryTreeDto;
import com.egorov.lib_food_categories.model.FoodCategory;
import com.egorov.lib_food_categories.repository.FoodCategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для работы с категориями продуктов. Предоставляет CRUD-операции и методы для работы с
 * иерархией категорий.
 *
 * @author i.egorov
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"foodCategories", "foodCategoriesTree"})
public class FoodCategoryServiceImpl implements FoodCategoryService {

  private final FoodCategoryRepository foodCategoryRepository;

  /**
   * Создает новую категорию продуктов.
   *
   * @param foodCategory данные новой категории
   * @return созданная категория
   * @throws FoodCategoryNotFoundException если родительская категория не найдена
   * @author i.egorov
   */
  @Override
  @Transactional
  public FoodCategory create(FoodCategory foodCategory) {
    if (foodCategory.getParentId() != null) {
      FoodCategory parent = findById(foodCategory.getParentId());
      foodCategory.setParentId(parent.getParentId());
    }
    return foodCategoryRepository.save(foodCategory);
  }

  /**
   * Находит категорию по идентификатору.
   *
   * @param id идентификатор категории
   * @return найденная категория
   * @throws FoodCategoryNotFoundException если категория не найдена
   * @author i.egorov
   */
  @Override
  @Transactional(readOnly = true)
  public FoodCategory findById(Long id) {
    return foodCategoryRepository.findById(id)
        .orElseThrow(() -> new FoodCategoryNotFoundException(id));
  }

  /**
   * Возвращает страницу со всеми категориями продуктов.
   *
   * @param pageable параметры пагинации и сортировки
   * @return страница категорий
   * @author i.egorov
   */
  @Override
  @Transactional(readOnly = true)
  public Page<FoodCategory> findAll(Pageable pageable) {
    return foodCategoryRepository.findAll(pageable);
  }

  /**
   * Возвращает иерархическое дерево категорий.
   *
   * @return список корневых категорий с вложенными подкатегориями
   * @author i.egorov
   */
  @Override
  @Transactional(readOnly = true)
  public List<FoodCategoryTreeDto> getCategoryTree() {
    List<FoodCategory> rootCategories = foodCategoryRepository.findByParentIdIsNull();
    return rootCategories.stream()
        .map(this::convertToTreeDto)
        .collect(Collectors.toList());
  }

  /**
   * Обновляет данные категории.
   *
   * @param id      идентификатор категории
   * @param request новые данные категории
   * @return обновленная категория
   * @throws FoodCategoryNotFoundException если категория или родительская категория не найдены
   * @author i.egorov
   */
  @Override
  @Transactional
  public FoodCategory update(Long id, FoodCategoryRequest request) {
    FoodCategory existingCategory = findById(id);

    existingCategory.setName(request.name());
    existingCategory.setDescription(request.description());
    existingCategory.setIsFinal(request.isFinal());

    if (request.parentId() != null) {
      FoodCategory parent = findById(request.parentId());
      existingCategory.setParentId(parent.getParentId());
    } else {
      existingCategory.setParentId(null);
    }

    return foodCategoryRepository.save(existingCategory);
  }

  /**
   * Удаляет категорию по идентификатору.
   *
   * @param id идентификатор категории
   * @throws FoodCategoryNotFoundException если категория не найдена
   * @author i.egorov
   */
  @Override
  @Transactional
  public void delete(Long id) {
    FoodCategory category = findById(id);
    foodCategoryRepository.delete(category);
  }

  /**
   * Преобразует категорию в DTO для дерева категорий.
   *
   * @param category преобразуемая категория
   * @return DTO категории с вложенными подкатегориями
   * @author i.egorov
   */
  private FoodCategoryTreeDto convertToTreeDto(FoodCategory category) {
    List<FoodCategoryTreeDto> children
        = foodCategoryRepository.findAllByParentId(category.getId())
        .stream()
        .map(this::convertToTreeDto)
        .collect(Collectors.toList());

    return new FoodCategoryTreeDto(
        category.getId(),
        category.getName(),
        category.getDescription(),
        category.getIsFinal(),
        children,
        category.getCreatedAt()
    );
  }
}