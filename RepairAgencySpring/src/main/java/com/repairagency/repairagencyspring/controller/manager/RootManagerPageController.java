package com.repairagency.repairagencyspring.controller.manager;

import com.repairagency.repairagencyspring.DAO.manager.RootManagerPageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Log4j2
@Controller
@PreAuthorize("hasAuthority('perm:manager')")
@RequestMapping(value = "/account/manager")
public class RootManagerPageController {

    private final RootManagerPageService rootManagerPageService;

    public RootManagerPageController(RootManagerPageService rootManagerPageService) {
        this.rootManagerPageService = rootManagerPageService;
    }

    @GetMapping("")
    public String managerPage()
    {
        return "redirect:/account/manager/tasks/new";
    }

    @GetMapping("/userslist")
    public String usersPage(
            Model model,
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "login", direction = Sort.Direction.ASC),
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            })
                    Pageable pageable
    )
    {
        model.addAttribute("page", rootManagerPageService.getPageUserDTO(pageable));
        model.addAttribute("url","userslist");
        return "account/manager/manageruserslistpage";
    }


    @PostMapping("/addmoney")
    @ResponseBody
    public HashMap<String, Object> addMoneyPage(@RequestParam(value = "money") String money, @RequestParam(value = "user") String userName, HttpServletRequest request)
    {
        return rootManagerPageService.addMoneyManagerToUser(money, userName, request);
    }

    @PostMapping("/lock")
    @ResponseBody
    public HashMap<String, Object> lockUser(@RequestParam(value = "locked") boolean locked, @RequestParam(value = "user") String userName)
    {
        return rootManagerPageService.lockUser(locked,userName);
    }

}
