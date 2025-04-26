package com.egorov.lib_food_categories.controller;

import com.egorov.lib_food_categories.dto.request.FoodCategoryRequest;
import com.egorov.lib_food_categories.dto.response.FoodCategoryDto;
import com.egorov.lib_food_categories.dto.response.FoodCategoryTreeDto;
import com.egorov.lib_food_categories.model.FoodCategory;
import com.egorov.lib_food_categories.service.FoodCategoryServiceImpl;
import com.egorov.lib_food_categories.util.FoodCategoryMapper;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Контроллер для работы с категориями продуктов. Предоставляет REST API для CRUD операций с
 * категориями продуктов.
 *
 * @author i.egorov
 */
@RestController
@RequestMapping("/api/food-categories")
@RequiredArgsConstructor
public class FoodCategoryController {

  private final FoodCategoryServiceImpl foodCategoryServiceImpl;
  private final FoodCategoryMapper mapper;

  /**
   * Создает новую категорию продуктов.
   *
   * @param request DTO с данными для создания категории
   * @return ResponseEntity с созданной категорией и HTTP статусом 201 (Created)
   */
  @PostMapping
  @CacheEvict(value = {"foodCategories", "foodCategoriesTree"}, allEntries = true)
  public ResponseEntity<FoodCategoryDto> create(
      @RequestBody @Valid FoodCategoryRequest request) {

    FoodCategory created = foodCategoryServiceImpl.create(mapper.toEntity(request));
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(created.getId())
        .toUri();

    return ResponseEntity.created(location).body(mapper.toDto(created));
  }

  /**
   * Получает категорию продуктов по идентификатору.
   *
   * @param id идентификатор категории
   * @return ResponseEntity с найденной категорией и HTTP статусом 200 (OK)
   */
  @GetMapping("/{id}")
  @Cacheable(value = "foodCategories", key = "#id")
  public ResponseEntity<FoodCategoryDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(
        mapper.toDto(foodCategoryServiceImpl.findById(id)));
  }

  /**
   * Получает список категорий продуктов с пагинацией и сортировкой.
   *
   * @param page номер страницы (по умолчанию 0)
   * @param size размер страницы (по умолчанию 20)
   * @param sort параметры сортировки (по умолчанию "name,asc")
   * @return ResponseEntity с пагинированным списком категорий и HTTP статусом 200 (OK)
   */
  @GetMapping
  public ResponseEntity<Page<FoodCategoryDto>> getAll(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "name,asc") String[] sort) {

    Sort sorting = getSorting(sort);
    Page<FoodCategory> categories = foodCategoryServiceImpl.findAll(
        PageRequest.of(page, size, sorting));

    return ResponseEntity.ok(categories.map(mapper::toDto));
  }

  /**
   * Получает дерево категорий продуктов с пагинацией и сортировкой.
   *
   * @return ResponseEntity с деревом категорий и HTTP статусом 200 (OK)
   */
  @GetMapping("/tree")
  @Cacheable("foodCategoriesTree")
  public ResponseEntity<List<FoodCategoryTreeDto>> getCategoryTree() {

    return ResponseEntity.ok(foodCategoryServiceImpl.getCategoryTree());
  }

  /**
   * Обновляет существующую категорию продуктов.
   *
   * @param id      идентификатор категории для обновления
   * @param request DTO с обновленными данными категории
   * @return ResponseEntity с обновленной категорией и HTTP статусом 200 (OK)
   */
  @PutMapping("/{id}")
  @CacheEvict(value = {"foodCategories", "foodCategoriesTree"}, allEntries = true)
  public ResponseEntity<FoodCategoryDto> update(
      @PathVariable Long id,
      @RequestBody @Valid FoodCategoryRequest request) {

    return ResponseEntity.ok(
        mapper.toDto(foodCategoryServiceImpl.update(id, request)));
  }

  /**
   * Удаляет категорию продуктов по идентификатору.
   *
   * @param id идентификатор категории для удаления
   */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @CacheEvict(value = {"foodCategories", "foodCategoriesTree"}, allEntries = true)
  public void delete(@PathVariable Long id) {
    foodCategoryServiceImpl.delete(id);
  }

  /**
   * Создает объект Sort на основе параметров сортировки.
   *
   * @param sort массив строк с параметрами сортировки
   * @return объект Sort
   */
  static Sort getSorting(String[] sort) {
    return Sort.by(sort[0].split(",")[1].equals("desc")
            ? Sort.Direction.DESC
            : Sort.Direction.ASC,
        sort[0].split(",")[0]);
  }
}