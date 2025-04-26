package com.egorov.lib_food_categories.dto.exception;

import org.springframework.validation.BindingResult;
import java.util.List;

/**
 * DTO для передачи информации об ошибках валидации.
 * Содержит общее сообщение об ошибке и список конкретных ошибок полей.
 *
 * @author i.egorov
 * @param message общее сообщение об ошибке валидации
 * @param errors список ошибок для отдельных полей
 */
public record ValidationErrorResponse(
    String message,
    List<FieldError> errors
) {

  /**
   * Создает объект ValidationErrorResponse из результатов валидации Spring.
   *
   * @author i.egorov
   * @param bindingResult объект BindingResult с результатами валидации
   * @return новый объект ValidationErrorResponse с информацией об ошибках
   */
  public static ValidationErrorResponse fromBindingResult(BindingResult bindingResult) {
    List<FieldError> errors = bindingResult.getFieldErrors().stream()
        .map(fe -> new FieldError(
            fe.getField(),
            fe.getDefaultMessage(),
            fe.getRejectedValue()))
        .toList();

    return new ValidationErrorResponse("Validation failed", errors);
  }
}