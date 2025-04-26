package com.egorov.lib_food_categories.dto.exception;

/**
 * DTO для представления ошибки валидации конкретного поля.
 * Содержит информацию о поле, сообщении об ошибке и отклоненном значении.
 *
 * @param field Название поля, в котором произошла ошибка валидации
 * @param message Сообщение об ошибке
 * @param rejectedValue Отклоненное значение, которое не прошло валидацию
 * @author i.egorov
 */
public record FieldError(
    String field,
    String message,
    Object rejectedValue
) {

}