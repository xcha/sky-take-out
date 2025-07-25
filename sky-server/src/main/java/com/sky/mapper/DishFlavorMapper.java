package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

     void insertBatch(List<DishFlavor> flavors);

     @Delete("delete from dish_flavor where dish_id = #{DishId}")
     void deleteByDishId(Long DishId);

     void deleteByDishIds(List<Long> ids);

     @Select("select * from dish_flavor where dish_id = #{dishId}")
     List<DishFlavor> getByDishId(Long dishId);
}
