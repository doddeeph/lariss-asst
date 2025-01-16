package id.lariss.service;

import id.lariss.service.dto.MenuDTO;

public interface MenuService {
    MenuDTO getMenuByProductNumber(Long productNumber);

    MenuDTO getMenuByProductName(String productName);
}
