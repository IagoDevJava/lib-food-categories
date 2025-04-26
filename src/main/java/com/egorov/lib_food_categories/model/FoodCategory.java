package com.egorov.lib_food_categories.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Сущность категории продуктов питания.
 * Представляет иерархическую структуру категорий продуктов.
 *
 * @author i.egorov
 */
@Entity
@Table(name = "food_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "parentId")
public class FoodCategory {

  /**
   * Уникальный идентификатор категории
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Родительская категория. Может быть null для корневых категорий.
   * Используется для построения иерархии категорий.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Long parentId;

  /**
   * Название категории. Обязательное поле.
   * Максимальная длина - 100 символов.
   */
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /**
   * Описание категории. Необязательное поле.
   */
  @Column(name = "description")
  private String description;

  /**
   * Флаг, указывающий является ли категория конечной (не может иметь подкатегорий).
   * По умолчанию false.
   */
  @Column(name = "is_final", columnDefinition = "boolean default false")
  private Boolean isFinal;

  /**
   * Дата и время создания записи.
   * Заполняется автоматически при создании.
   */
  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private ZonedDateTime createdAt;

  /**
   * Дата и время последнего обновления записи.
   * Обновляется автоматически при изменении.
   */
  @UpdateTimestamp
  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

//  /**
//   * Устанавливает родительскую категорию с дополнительной проверкой.
//   * Если родительская категория помечена как конечная (isFinal=true),
//   * сбрасывает этот флаг, так как конечная категория не может иметь подкатегорий.
//   *
//   * @param parentId родительская категория для установки
//   */
//  public void setParentId(Long parentId) {
//    this.parentId = parentId;
//    if (parentId != null && !parentId.getIsFinal()) {
//      parentId.setIsFinal(false);
//    }
//  }
}