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
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger LOG = LoggerFactory.getLogger(MenuServiceImpl.class);

    private final CategoryService categoryService;

    private final ProductService productService;

    public MenuServiceImpl(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @Override
    public MenuDTO getMenuByProductNumber(Long productNumber) {
        LOG.info("Request to get Menu by number: {}", productNumber);
        return MenuDTO.builder()
            .items(Objects.isNull(productNumber) ? buildMenuItems(categoryService.findAll()) : buildMenuItems(productNumber))
            .build();
    }

    @Override
    public MenuDTO getMenuByProductName(String productName) {
        LOG.info("Request to get menu by product name: {}", productName);
        List<CategoryDTO> categories = categoryService.findAll();
        return MenuDTO.builder()
            .items(StringUtils.isBlank(productName) ? buildMenuItems(categories) : buildMenuItems(categories, productName))
            .build();
    }

    private List<MenuItemDTO> buildMenuItems(List<CategoryDTO> categories) {
        return categories
            .stream()
            .map(category -> MenuItemDTO.builder().productNumber(category.getId()).productName(category.getName()).build())
            .toList();
    }

    private List<MenuItemDTO> buildMenuItems(List<CategoryDTO> categories, String name) {
        Long categoryId = getCategoryId(categories, name);
        if (Objects.nonNull(categoryId)) {
            return productService
                .findAllProductByCategoryId(categoryId)
                .stream()
                .map(product -> MenuItemDTO.builder().productNumber(product.getId()).productName(product.getName()).build())
                .toList();
        }
        return new ArrayList<>();
    }

    private List<MenuItemDTO> buildMenuItems(Long number) {
        return productService
            .findAllProductByCategoryId(number)
            .stream()
            .map(product -> MenuItemDTO.builder().productNumber(product.getId()).productName(product.getName()).build())
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
