package com.kys.mybatispj.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/catweb")
public class CatWebController {
    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("")
    public String indexHome() {
        return "index";
    }

    @GetMapping("/category_list")    // 브라우저의 URL 주소
    public String categoryList(Model model, @RequestParam String name, @RequestParam int page) {
        try {
            if (name == null) {
                name = "";
            }
//            List<ICategory> catList = this.categoryService.getAllList();
            SearchCategoryDto searchCategoryDto = SearchCategoryDto.builder()
                    .name(name).page(page).build();
            int total = this.categoryService.countAllByNameContains(searchCategoryDto);
            searchCategoryDto.setTotal(total);
            List<ICategory> catList = this.categoryService.findAllByNameContains(searchCategoryDto);
            model.addAttribute("catList", catList);
            model.addAttribute("searchCategoryDto", searchCategoryDto);
            // java 에서 html 문자를 만드는 고전적인 방법은 매우 안 좋다.
            String sPages = this.getHtmlPageString(searchCategoryDto);
            model.addAttribute("pageHtml", sPages);
        } catch (Exception ex) {
            log.error(ex.toString());
            model.addAttribute("error_message", "오류가 발생했습니다. 관리자에게 문의하세요.");
            return "error/error_save";  // resources/templates 폴더안의 화면파일
        }
        return "catweb/category_list";  // resources/templates 폴더안의 화면파일
    }

    private String getHtmlPageString(SearchCategoryDto searchCategoryDto) {
        StringBuilder sResult = new StringBuilder();
        int tPage = (searchCategoryDto.getTotal() + 9) / 10;
        sResult.append("<div>");
        for ( int i = 0; i < tPage; i++ ) {
            sResult.append(" <a href='category_list?page=" + (i+1) +
                    "&name=" + searchCategoryDto.getName() + "'>");
            sResult.append(i+1);
            sResult.append("</a> ");
        }
        sResult.append("</div>");
        return sResult.toString();
    }

    @PostMapping("/category_add")
    public String categoryListInsert(@ModelAttribute CategoryDto dto, Model model) {
        try {
            if (dto == null || dto.getName() == null || dto.getName().isEmpty()) {
                model.addAttribute("error_message", "이름이 비었습니다.");
                return "error/error_bad"; //브라우저 주소를 redirect 한다
            }
            this.categoryService.insert(dto);
        } catch (Exception ex) {
            log.error(ex.toString());
            model.addAttribute("error_message", dto.getName() + "중복입니다.");
            return "error/error_save";  // resources/templates 폴더안의 화면파일
        }
        return "redirect:category_list?page=1&name=";  // 브라우저 주소를 redirect 한다.
    }

    @GetMapping("/category_view") //브라우저의 URL 주소
    public String categoryListView(@RequestParam Long id, Model model) {
        try {
            if ( id == null || id <= 0 ) {
                model.addAttribute("error_message", "ID는 1보다 커야 합니다.");
                return "error/error_bad";  // resources/templates 폴더안의 화면파일
            }
            ICategory find = this.categoryService.findById(id);
            if ( find == null ) {
                model.addAttribute("error_message", id + " 데이터가 없습니다.");
                return "error/error_find";
            }
            model.addAttribute("catList", find);
        } catch (Exception ex) {
            log.error(ex.toString());
            model.addAttribute("error_message", "서버 에러입니다. 관리자에게 문의 하세요.");
            return "error/error_save";  // resources/templates 폴더안의 화면파일
        }
        return "catweb/category_insert"; //resources/templates 폴더안의 화면파일
    }

    @PostMapping("/category_update")
    public String categoryListUpdate(@ModelAttribute CategoryDto dto, Model model) {
        try {
            if (dto == null || dto.getId() <= 0 || dto.getName().isEmpty()) {
                model.addAttribute("error_message", "id 는 1보다 커야하고 name 이 있어야합니다");
                return "error/error_bad";
            }
            ICategory find = this.categoryService.findById(dto.getId());
            if(find == null) {
                model.addAttribute("error_message", dto.getId() + "데이터가 없습니다");
                return "error/error_find";
            }
            this.categoryService.update(dto.getId(), dto);
        } catch (Exception ex) {
            log.error(ex.toString());
            model.addAttribute("error_message", dto.getName() + " 중복입니다.");
            return "error/error_save";  // resources/templates 폴더안의 화면파일
        }
        return "redirect:category_list?page=1&name=";
    }

    @GetMapping("/category_ delete")
    public String categoryListDelete(@RequestParam Long id, Model model) { //get 방식에 @RequestParam
        try {
            if (id == null || id <= 0) {
                model.addAttribute("error_message", "id는 1보다 커야 합니다.");
                return "error/error_bad";  // resources/templates 폴더안의 화면파일
            }
            ICategory find = this.categoryService.findById(id);
            if (find == null) {
                model.addAttribute("error_message", id + " 데이터가 없습니다.");
                return "error/error_find";
            }
            this.categoryService.delete(id);
        } catch (Exception ex) {
            log.error(ex.toString());
            model.addAttribute("error_message", "서버 에러입니다. 관리자에게 문의 하세요.");
            return "error/error_save";  // resources/templates 폴더안의 화면파일
        }
        return "redirect:category_list?page=1&name=";
    }
}
