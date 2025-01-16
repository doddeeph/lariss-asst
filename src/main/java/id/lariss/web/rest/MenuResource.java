package id.lariss.web.rest;

import id.lariss.service.MenuService;
import id.lariss.service.dto.MenuDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/menu")
public class MenuResource {

    private final MenuService menuService;

    public MenuResource(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/by-number")
    public MenuDTO getMenuByNumber(@RequestParam(required = false) Long number) {
        return menuService.getMenuByProductNumber(number);
    }

    @GetMapping("/by-name")
    public MenuDTO getMenuByName(@RequestParam(required = false) String name) {
        return menuService.getMenuByProductName(name);
    }
}
