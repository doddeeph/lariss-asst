package id.lariss.service;

import id.lariss.service.dto.MenuDTO;

public interface MenuService {
    MenuDTO getMenuByName(String root);

    MenuDTO getMenuByNumber(Long number);
}
