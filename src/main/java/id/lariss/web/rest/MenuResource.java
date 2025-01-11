package id.lariss.web.rest;

import id.lariss.service.MenuService;
import id.lariss.service.dto.MenuDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class MenuResource {

    @Autowired
    private MenuService menuService;

    @GetMapping("/menu-by-name")
    public MenuDTO getMenuByName(@RequestParam(required = false) String name) {
        return menuService.getMenuByName(name);
    }

    @GetMapping("/menu-by-number")
    public MenuDTO getMenuByNumber(@RequestParam(required = false) Long number) {
        return menuService.getMenuByNumber(number);
    }
}
