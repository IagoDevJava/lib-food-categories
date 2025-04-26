package com.egorov.lib_food_categories.dto.exception;

/**
 * DTO для передачи информации об ошибке в API.
 * Содержит сообщение об ошибке, которое может быть показано пользователю.
 *
 * @author i.egorov
 * @param message сообщение об ошибке
 */
public record ErrorResponse(String message) {

}