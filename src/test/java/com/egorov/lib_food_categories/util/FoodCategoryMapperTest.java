package com.egorov.lib_food_categories.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.egorov.lib_food_categories.dto.request.FoodCategoryRequest;
import com.egorov.lib_food_categories.dto.response.FoodCategoryDto;
import com.egorov.lib_food_categories.model.FoodCategory;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Тесты для {@link FoodCategoryMapper}.
 *
 * @author i.egorov
 */
@ExtendWith(MockitoExtension.class)
class FoodCategoryMapperTest {

  @Spy
  private FoodCategoryMapper mapper = new FoodCategoryMapperImpl();

  @Test
  void toDto_shouldMapEntityToDtoCorrectly() {
    // Arrange
    ZonedDateTime now = ZonedDateTime.now();
    FoodCategory entity = FoodCategory.builder()
        .id(1L)
        .name("Овощи")
        .description("Свежие овощи")
        .isFinal(true)
        .createdAt(now)
        .updatedAt(now)
        .build();

    // Act
    FoodCategoryDto dto = mapper.toDto(entity);

    // Assert
    assertNotNull(dto);
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getName(), dto.getName());
    assertEquals(entity.getDescription(), dto.getDescription());
    assertEquals(entity.getIsFinal(), dto.isFinal());
    assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
    assertEquals(entity.getUpdatedAt(), dto.getUpdatedAt());
  }

  @Test
  void toDto_shouldReturnNullWhenEntityIsNull() {
    // Act
    FoodCategoryDto dto = mapper.toDto(null);

    // Assert
    assertNull(dto);
  }

  @Test
  void toEntity_shouldMapRequestToEntityCorrectly() {
    // Arrange
    FoodCategoryRequest request = new FoodCategoryRequest(
        1L,
        "Фрукты",
        "Свежие фрукты",
        false
    );

    // Act
    FoodCategory entity = mapper.toEntity(request);

    // Assert
    assertNotNull(entity);
    assertEquals(request.name(), entity.getName());
    assertEquals(request.description(), entity.getDescription());
    assertEquals(request.isFinal(), entity.getIsFinal());
    assertNull(entity.getId()); // ID не должен маппиться из запроса
    assertNull(entity.getCreatedAt()); // Даты должны быть null
    assertNull(entity.getUpdatedAt());
  }

  @Test
  void toEntity_shouldHandleNullParentId() {
    // Arrange
    FoodCategoryRequest request = new FoodCategoryRequest(
        null,
        "Мясо",
        "Мясные продукты",
        true
    );

    // Act
    FoodCategory entity = mapper.toEntity(request);

    // Assert
    assertNotNull(entity);
    assertNull(entity.getParentId());
  }

  @Test
  void toEntity_shouldReturnNullWhenRequestIsNull() {
    // Act
    FoodCategory entity = mapper.toEntity(null);

    // Assert
    assertNull(entity);
  }

  @Test
  void mapperImplementation_shouldBeGenerated() {
    // Этот тест проверяет, что реализация маппера была сгенерирована
    assertNotNull(mapper);
    assertTrue(mapper instanceof FoodCategoryMapperImpl);
  }
}