package com.egorov.lib_food_categories.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.egorov.lib_food_categories.dto.request.FoodCategoryRequest;
import com.egorov.lib_food_categories.dto.response.FoodCategoryDto;
import com.egorov.lib_food_categories.dto.response.FoodCategoryTreeDto;
import com.egorov.lib_food_categories.model.FoodCategory;
import com.egorov.lib_food_categories.service.FoodCategoryService;
import com.egorov.lib_food_categories.util.FoodCategoryMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class FoodCategoryControllerTest {

  @Mock
  private FoodCategoryService foodCategoryService;

  @Mock
  private FoodCategoryMapper mapper;

  @InjectMocks
  private FoodCategoryController foodCategoryController;

  @Test
  void create_ShouldReturnCreatedResponse() {
    // Arrange
    FoodCategoryRequest request = new FoodCategoryRequest(null, "Fruits", "Fresh fruits", false);
    FoodCategory category = new FoodCategory(1L, null, "Fruits", "Fresh fruits", false, null, null);
    FoodCategoryDto dto = new FoodCategoryDto(1L, null, "Fruits", "Fresh fruits", false, null,
        null);

    when(mapper.toEntity(request)).thenReturn(category);
    when(foodCategoryService.create(category)).thenReturn(category);
    when(mapper.toDto(category)).thenReturn(dto);

    // Act
    ResponseEntity<FoodCategoryDto> response = foodCategoryController.create(request);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(dto, response.getBody());

    verify(mapper).toEntity(request);
    verify(foodCategoryService).create(category);
    verify(mapper).toDto(category);
  }

  @Test
  void getById_ShouldReturnCategory() {
    // Arrange
    Long id = 1L;
    FoodCategory category = new FoodCategory(id, null, "Vegetables", "Fresh vegetables", false,
        null, null);
    FoodCategoryDto dto = new FoodCategoryDto(id, null, "Vegetables", "Fresh vegetables", false,
        null, null);

    when(foodCategoryService.findById(id)).thenReturn(category);
    when(mapper.toDto(category)).thenReturn(dto);

    // Act
    ResponseEntity<FoodCategoryDto> response = foodCategoryController.getById(id);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(dto, response.getBody());

    verify(foodCategoryService).findById(id);
    verify(mapper).toDto(category);
  }

  @Test
  void getAll_ShouldReturnPaginatedCategories() {
    // Arrange
    int page = 0;
    int size = 10;
    String[] sort = {"name,asc"};

    FoodCategory category = new FoodCategory(1L, null, "Fruits", null, false, null, null);
    Page<FoodCategory> categoryPage = new PageImpl<>(List.of(category));
    FoodCategoryDto dto = new FoodCategoryDto(1L, null, "Fruits", null, false, null, null);

    when(foodCategoryService.findAll(any(PageRequest.class))).thenReturn(categoryPage);
    when(mapper.toDto(category)).thenReturn(dto);

    // Act
    ResponseEntity<Page<FoodCategoryDto>> response = foodCategoryController.getAll(page, size,
        sort);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().getContent().size());
    assertEquals(dto, response.getBody().getContent().get(0));

    verify(foodCategoryService).findAll(
        PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name")));
    verify(mapper).toDto(category);
  }

  @Test
  void getCategoryTree_ShouldReturnCategoryTree() {
    // Arrange
    FoodCategoryTreeDto treeDto = new FoodCategoryTreeDto(1L, "Fruits", null, false, List.of(),
        null);

    when(foodCategoryService.getCategoryTree()).thenReturn(List.of(treeDto));

    // Act
    ResponseEntity<List<FoodCategoryTreeDto>> response =
        foodCategoryController.getCategoryTree();

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals(treeDto, response.getBody().get(0));

    verify(foodCategoryService).getCategoryTree();
  }

  @Test
  void update_ShouldReturnUpdatedCategory() {
    // Arrange
    Long id = 1L;
    FoodCategoryRequest request = new FoodCategoryRequest(null, "Updated", "Desc", true);
    FoodCategory updatedCategory = new FoodCategory(id, null, "Updated", "Desc", true, null, null);
    FoodCategoryDto dto = new FoodCategoryDto(id, null, "Updated", "Desc", true, null, null);

    when(foodCategoryService.update(id, request)).thenReturn(updatedCategory);
    when(mapper.toDto(updatedCategory)).thenReturn(dto);

    // Act
    ResponseEntity<FoodCategoryDto> response = foodCategoryController.update(id, request);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(dto, response.getBody());

    verify(foodCategoryService).update(id, request);
    verify(mapper).toDto(updatedCategory);
  }

  @Test
  void delete_ShouldInvokeServiceDelete() {
    // Arrange
    Long id = 1L;

    // Act
    foodCategoryController.delete(id);

    // Assert
    verify(foodCategoryService).delete(id);
  }

  @Test
  void getSorting_ShouldReturnCorrectSort() {
    // Arrange
    String[] sortAsc = {"name,asc"};
    String[] sortDesc = {"name,desc"};

    // Act
    Sort resultAsc = FoodCategoryController.getSorting(sortAsc);
    Sort resultDesc = FoodCategoryController.getSorting(sortDesc);

    // Assert
    assertEquals(Sort.Direction.ASC, resultAsc.getOrderFor("name").getDirection());
    assertEquals(Sort.Direction.DESC, resultDesc.getOrderFor("name").getDirection());
  }
}