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
        if (id == null || id <= 0) {
            return null;
        }
        CategoryDto dto = this.categoryMybatisMapper.findById(id);
        return dto;
    }

    @Override
    public ICategory findByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        CategoryDto find = this.categoryMybatisMapper.findByName(name);
        return find;
    }

    @Override
    public List<ICategory> getAllList() {
        List<ICategory> list = this.getICategoryList(
                this.categoryMybatisMapper.findAll()
        );
        return list;
    }

    private List<ICategory> getICategoryList(List<CategoryDto> list) {
        if (list == null || list.size() <= 0) {
            return new ArrayList<>();
        }

        List<com.kys.mybatispj.category.ICategory> result = list.stream()
                .map(entity -> (com.kys.mybatispj.category.ICategory) entity)
                .toList();
        return result;
    }


    @Override
    public ICategory insert(ICategory category) throws Exception {
        if (!isValidInsert(category)) {
            return null;
        }
        CategoryDto dto = new CategoryDto();
        dto.copyFields(category);
        dto.setId(0L);
        this.categoryMybatisMapper.insert(dto);
        return dto;
    }

    private boolean isValidInsert(ICategory dto) {
        if (dto == null) {
            return false;
        } else if (dto.getName() == null || dto.getName().isEmpty()) {
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
    public ICategory update(Long id, ICategory category) throws Exception {
        ICategory find = this.findById(id);
        if (find == null) {
            return null;
        }
        find.copyFields(category);
        this.categoryMybatisMapper.update((CategoryDto) find);
        return find;
    }

    @Override
    public List<ICategory> findAllByNameContains(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        List<ICategory> list = this.getICategoryList(
                this.categoryMybatisMapper.findAllByNameContains(name)
        );
        return list;
    }

}
