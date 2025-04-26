package com.egorov.lib_food_categories.repository;

import com.egorov.lib_food_categories.model.FoodCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {

  List<FoodCategory> findByParentIdIsNull();

  List<FoodCategory> findAllByParentId(Long parentId);
}