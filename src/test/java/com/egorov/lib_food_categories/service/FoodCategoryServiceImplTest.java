package com.egorov.lib_food_categories.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.egorov.lib_food_categories.dto.exception.FoodCategoryNotFoundException;
import com.egorov.lib_food_categories.dto.request.FoodCategoryRequest;
import com.egorov.lib_food_categories.dto.response.FoodCategoryTreeDto;
import com.egorov.lib_food_categories.model.FoodCategory;
import com.egorov.lib_food_categories.repository.FoodCategoryRepository;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class FoodCategoryServiceImplTest {

  @Mock
  private FoodCategoryRepository foodCategoryRepository;

  @InjectMocks
  private FoodCategoryServiceImpl foodCategoryServiceImpl;

  @Test
  void create_ShouldSaveNewCategoryWithoutParent() {
    // Arrange
    FoodCategory newCategory = new FoodCategory();
    newCategory.setName("Овощи");
    when(foodCategoryRepository.save(any(FoodCategory.class))).thenReturn(newCategory);

    // Act
    FoodCategory result = foodCategoryServiceImpl.create(newCategory);

    // Assert
    assertNotNull(result);
    assertEquals("Овощи", result.getName());
    verify(foodCategoryRepository).save(newCategory);
  }

  @Test
  void create_ShouldSaveNewCategoryWithParent() {
    // Arrange
    FoodCategory parentCategory = new FoodCategory();
    parentCategory.setId(1L);
    parentCategory.setName("Продукты");

    FoodCategory newCategory = new FoodCategory();
    newCategory.setName("Овощи");
    newCategory.setParentId(parentCategory.getId());

    when(foodCategoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));
    when(foodCategoryRepository.save(any(FoodCategory.class))).thenReturn(newCategory);

    // Act
    FoodCategory result = foodCategoryServiceImpl.create(newCategory);

    // Assert
    assertNotNull(result);
    assertEquals("Овощи", result.getName());
    assertEquals(parentCategory.getId(), result.getParentId());
    verify(foodCategoryRepository).save(newCategory);
  }

  @Test
  void findById_ShouldReturnCategoryWhenExists() {
    // Arrange
    FoodCategory category = new FoodCategory();
    category.setId(1L);
    category.setName("Фрукты");
    when(foodCategoryRepository.findById(1L)).thenReturn(Optional.of(category));

    // Act
    FoodCategory result = foodCategoryServiceImpl.findById(1L);

    // Assert
    assertNotNull(result);
    assertEquals("Фрукты", result.getName());
  }

  @Test
  void findById_ShouldThrowExceptionWhenNotExists() {
    // Arrange
    when(foodCategoryRepository.findById(1L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(FoodCategoryNotFoundException.class, () -> foodCategoryServiceImpl.findById(1L));
  }

  @Test
  void findAll_ShouldReturnPageOfCategories() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    List<FoodCategory> categories = List.of(
        createTestCategory(1L, "Овощи", false),
        createTestCategory(2L, "Фрукты", true)
    );
    Page<FoodCategory> page = new PageImpl<>(categories, pageable, categories.size());
    when(foodCategoryRepository.findAll(pageable)).thenReturn(page);

    // Act
    Page<FoodCategory> result = foodCategoryServiceImpl.findAll(pageable);

    // Assert
    assertEquals(2, result.getTotalElements());
    verify(foodCategoryRepository).findAll(pageable);
  }

  @Test
  void getCategoryTree_ShouldReturnTreeStructure() {
    // Arrange
    FoodCategory rootCategory = createTestCategory(1L, "Продукты", false);
    FoodCategory childCategory = createTestCategory(2L, "Овощи", true);
    childCategory.setParentId(rootCategory.getParentId());

    when(foodCategoryRepository.findByParentIdIsNull()).thenReturn(List.of(rootCategory));
    when(foodCategoryRepository.findAllByParentId(1L))
        .thenReturn(List.of(childCategory));

    // Act
    List<FoodCategoryTreeDto> result = foodCategoryServiceImpl.getCategoryTree();

    // Assert
    assertEquals(1, result.size());
    assertEquals("Продукты", result.get(0).name());
    assertEquals(1, result.get(0).subCategories().size());
    assertEquals("Овощи", result.get(0).subCategories().get(0).name());
  }

  @Test
  void update_ShouldUpdateCategoryData() {
    // Arrange
    FoodCategory existingCategory = createTestCategory(1L, "Старое название", true);
    FoodCategory parentCategory = createTestCategory(2L, "Родительская", true);

    when(foodCategoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
    when(foodCategoryRepository.findById(2L)).thenReturn(Optional.of(parentCategory));
    when(foodCategoryRepository.save(any(FoodCategory.class))).thenReturn(existingCategory);

    // Act
    FoodCategory result = foodCategoryServiceImpl.update(1L,
        new FoodCategoryRequest(2L, "Новое название", "Описание", true));

    // Assert
    assertEquals("Новое название", result.getName());
    assertEquals("Описание", result.getDescription());
    assertTrue(result.getIsFinal());
    assertEquals(parentCategory.getId(), result.getParentId());
    verify(foodCategoryRepository).save(existingCategory);
  }

  @Test
  void delete_ShouldDeleteExistingCategory() {
    // Arrange
    FoodCategory category = createTestCategory(1L, "Удаляемая", true);
    when(foodCategoryRepository.findById(1L)).thenReturn(Optional.of(category));
    doNothing().when(foodCategoryRepository).delete(category);

    // Act
    foodCategoryServiceImpl.delete(1L);

    // Assert
    verify(foodCategoryRepository).delete(category);
  }

  private FoodCategory createTestCategory(Long id, String name, Boolean isFinal) {
    FoodCategory category = new FoodCategory();
    category.setId(id);
    category.setName(name);
    category.setIsFinal(isFinal);
    category.setCreatedAt(ZonedDateTime.now());
    return category;
  }
}