package com.egorov.lib_food_categories.dto.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Глобальный обработчик исключений для REST API.
 * Обрабатывает различные типы исключений и возвращает соответствующие HTTP-статусы и сообщения об ошибках.
 *
 * @author i.egorov
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Обрабатывает исключения, связанные с отсутствием сущностей в базе данных.
   *
   * @param ex исключение типа EntityNotFoundException
   * @return объект ErrorResponse с сообщением об ошибке
   */
  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleEntityNotFound(EntityNotFoundException ex) {
    log.warn("Entity not found: {}", ex.getMessage());
    return new ErrorResponse(ex.getMessage());
  }

  /**
   * Обрабатывает исключения валидации входных данных.
   *
   * @param ex исключение типа MethodArgumentNotValidException
   * @return объект ValidationErrorResponse с деталями ошибок валидации
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ValidationErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
    log.warn("Validation error: {}", ex.getMessage());
    BindingResult bindingResult = ex.getBindingResult();
    return ValidationErrorResponse.fromBindingResult(bindingResult);
  }

  /**
   * Обрабатывает все непредвиденные исключения.
   *
   * @param ex исключение типа Exception
   * @return объект ErrorResponse с общим сообщением об ошибке
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleGlobalException(Exception ex) {
    log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
    return new ErrorResponse("Internal server error");
  }

  /**
   * Обрабатывает исключения, связанные с отсутствием категорий продуктов.
   *
   * @param ex исключение типа FoodCategoryNotFoundException
   * @return объект ErrorResponse с сообщением об ошибке
   */
  @ExceptionHandler(FoodCategoryNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleFoodCategoryNotFoundException(Exception ex) {
    log.warn("Food category not found: {}", ex.getMessage());
    return new ErrorResponse(ex.getMessage());
  }
}