package com.kys.mybatispj.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/ctweb")
public class CategoryWebController {
    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("")
    public String indexHome() {
        return "index";
    }

    @GetMapping("/oldhtml/category_old") //브라우저의 URL 주소
    public String category_old(Model model) {
        try {
            List<ICategory> allList = this.categoryService.getAllList();
            model.addAttribute("itemList", allList);
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        return "oldhtml/category_old"; //resources/templates 폴더안의 화면파일
    }

    @PostMapping("/oldhtml/category_old_act")
    public String categoryOldAct(@ModelAttribute CategoryDto dto, Model model) {
        try {
            if (dto == null || dto.getName() == null || dto.getName().isEmpty()) {
                return "redirect:category_old"; //브라우저 주소를 redirect 한다
            }
            this.categoryService.insert(dto);
            model.addAttribute("categoryDto", dto);
        } catch (Exception ex) {
            log.error(ex.toString());
            return "/oldhtml/category_old"; //resources/templates 폴더안의 화면파일
        }
        return "redirect:category_old"; //브라우저 주소를 redirect 한다
    }

    @GetMapping("/oldhtml/category_old_view") //브라우저의 URL 주소
    public String categoryOldView(@RequestParam Long id, Model model) {
        try {
            ICategory byId = this.categoryService.findById(id);
            if (byId == null) {
                return "redirect:category_old";
            }
            model.addAttribute("categoryDto", byId);
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        return "oldhtml/category_view"; //resources/templates 폴더안의 화면파일
    }

    @PostMapping("/oldhtml/category_old_update")
    public String categoryOldUpdate(@ModelAttribute CategoryDto dto, Model model) {
        try {
            if (dto == null || dto.getId() == null || dto.getName() == null || dto.getName().isEmpty()) {
                return "redirect:category_old"; //브라우저 주소를 redirect 한다
            }
            this.categoryService.update(dto.getId(), dto);
        } catch (Exception ex) {
            log.error(ex.toString());
            return "/oldhtml/category_old"; //resources/templates 폴더안의 화면파일
        }
        return "redirect:category_old"; //브라우저 주소를 redirect 한다
    }

    @GetMapping("/oldhtml/category_old_delete")
    public String categoryOldDelete(@RequestParam Long id, Model model) { //get 방식에 @RequestParam
        try {
            if (id == null) {
                return "redirect:category_old"; //브라우저 주소를 redirect 한다
            }
            this.categoryService.remove(id);
        } catch (Exception ex) {
            log.error(ex.toString());
            return "/oldhtml/category_old"; //resources/templates 폴더안의 화면파일
        }
        return "redirect:category_old"; //브라우저 주소를 redirect 한다
    }
}
