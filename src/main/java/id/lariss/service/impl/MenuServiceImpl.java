package id.lariss.service.impl;

import id.lariss.service.CategoryService;
import id.lariss.service.MenuService;
import id.lariss.service.ProductService;
import id.lariss.service.dto.CategoryDTO;
import id.lariss.service.dto.MenuDTO;
import id.lariss.service.dto.MenuItemDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger LOG = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Override
    public MenuDTO getMenuByName(String name) {
        LOG.debug("Request to get Menu by name: {}", name);
        List<CategoryDTO> categories = categoryService.findAll();
        return MenuDTO.builder().items(StringUtils.isBlank(name) ? buildMenuItems(categories) : buildMenuItems(categories, name)).build();
    }

    @Override
    public MenuDTO getMenuByNumber(Long number) {
        LOG.debug("Request to get Menu by number: {}", number);
        return MenuDTO.builder().items(Objects.isNull(number) ? buildMenuItems(categoryService.findAll()) : buildMenuItems(number)).build();
    }

    private List<MenuItemDTO> buildMenuItems(List<CategoryDTO> categories) {
        return categories
            .stream()
            .map(category -> MenuItemDTO.builder().number(category.getId()).name(category.getName()).build())
            .toList();
    }

    private List<MenuItemDTO> buildMenuItems(List<CategoryDTO> categories, String name) {
        Long categoryId = getCategoryId(categories, name);
        if (Objects.nonNull(categoryId)) {
            return productService
                .findAllProductByCategoryId(categoryId)
                .stream()
                .map(product -> MenuItemDTO.builder().number(product.getId()).name(product.getName()).build())
                .toList();
        }
        return new ArrayList<>();
    }

    private List<MenuItemDTO> buildMenuItems(Long number) {
        return productService
            .findAllProductByCategoryId(number)
            .stream()
            .map(product -> MenuItemDTO.builder().number(product.getId()).name(product.getName()).build())
            .toList();
    }

    private Long getCategoryId(List<CategoryDTO> categories, String name) {
        return categories
            .stream()
            .filter(category -> name.equalsIgnoreCase(category.getName()))
            .findAny()
            .orElse(new CategoryDTO())
            .getId();
    }
}
