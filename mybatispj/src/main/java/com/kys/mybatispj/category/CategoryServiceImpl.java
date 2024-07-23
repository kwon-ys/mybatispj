package com.kys.mybatispj.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMybatisMapper categoryMybatisMapper;

    @Override
    public ICategory findById(Long id) {
        if (id == null || id <= 0){
            return null;
        }
        Optional<CategoryEntity> find = this.categoryMybatisMapper.findById(id);
        return find.orElse(null);
    }

    @Override
    public ICategory findByName(String name) {
        if (name == null || name.isEmpty()){
            return null;
        }
        Optional<CategoryEntity> find = this.categoryMybatisMapper.findByName(name);
        return find.orElse(null);
    }

    @Override
    public List<ICategory> getAllList() {
        List<ICategory> list = this.getICategoryList(
                this.categoryMybatisMapper.findAll()
        );
        return list;
    }

    private List<ICategory> getICategoryList(List<CategoryEntity> list) {
        if (list == null || list.size() <= 0) {
            return new ArrayList<>();
        }
        List<ICategory> result = list.stream()
                .map(x -> (ICategory)x)
                .toList();
        return result;
    }


    @Override
    public ICategory insert(ICategory category) throws Exception {
        if (!isValidInsert(category)) {
            return null;
        }
        CategoryEntity entity = CategoryEntity.builder()
                .id(0L).name(category.getName()).build();
        CategoryEntity result = this.categoryMybatisMapper.saveAndFlush(entity);
        return result;
    }

    private boolean isValidInsert(ICategory category) {
        if (category == null) {
            return false;
        } else if (category.getName() == null || category.getName().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean remove(Long id) throws Exception {
        ICategory find = this.findById(id);
        if (find == null) {
            return false;
        }
        this.categoryMybatisMapper.deleteById(id);
        return true;
    }

    @Override
    public ICategory update(Long id, ICategory category) {
        ICategory find = this.findById(id);
        if (find == null) {
            return null;
        }
        find.copyFields(category);
        CategoryEntity result = this.categoryMybatisMapper.saveAndFlush((CategoryEntity) find);
        return result;
    }

    @Override
    public List<ICategory> findAllByNameContains(String name) {
        if (name == null || name.isEmpty()) {
            return new ArrayList<>();
        }
        List<ICategory> list = this.getICategoryList(
                this.categoryMybatisMapper.findAllByNameContains(name)
        );
        return list;
    }

}
