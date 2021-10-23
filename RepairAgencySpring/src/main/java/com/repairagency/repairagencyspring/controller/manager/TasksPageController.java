package com.repairagency.repairagencyspring.controller.manager;

import com.repairagency.repairagencyspring.DAO.manager.TasksPageService;
import com.repairagency.repairagencyspring.dto.FilterDataDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.HashMap;

@Log4j2
@Controller
@PreAuthorize("hasAuthority('perm:manager')")
@RequestMapping(value = "/account/manager/tasks")
public class TasksPageController {

    final LocaleResolver localeResolver;
    private final TasksPageService tasksPageService;

    public TasksPageController(LocaleResolver localeResolver, TasksPageService tasksPageService) {
        this.localeResolver = localeResolver;
        this.tasksPageService = tasksPageService;
    }

    @GetMapping("/new")
    public String newTasksPage(
            Model model,
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "dateCreate", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            })
                Pageable pageable,
                FilterDataDTO filterData
            )
    {
        model.addAttribute("page",tasksPageService.getPageFilterDto(pageable,filterData));
        model.addAttribute("repairers",tasksPageService.getAllRepairers());
        model.addAttribute("url","new");
        model.addAttribute("filter",filterData);
        return "account/manager/managernewtasks";
    }

    @PostMapping("/setprice/{id}")
    @ResponseBody
    public HashMap<String, Object> setPrice(@RequestParam(value = "money") @NotNull String money, @PathVariable(value = "id") @NotNull Long taskId, HttpServletRequest request){
        return tasksPageService.setPriceForTaskId(money, taskId, localeResolver.resolveLocale(request));
    }

    @PostMapping("/cancel/{id}")
    @ResponseBody
    public HashMap<String, Object> setCanceled(@PathVariable(value = "id") @NotNull Long taskId, HttpServletRequest request){
        return tasksPageService.cancelTask(taskId,localeResolver.resolveLocale(request));
    }

    @PostMapping("/acceptpay/{id}")
    @ResponseBody
    public HashMap<String, Object> setAcceptPay(@PathVariable(value = "id") Long taskId, HttpServletRequest request){
        return tasksPageService.acceptPay(taskId,localeResolver.resolveLocale(request));
    }

    @PostMapping("/setrepairer/{id}/{idrep}")
    @ResponseBody
    public HashMap<String, Object> setRepairer(@PathVariable(value = "id") Long taskId, @PathVariable(value = "idrep") Long userId){
        return tasksPageService.setRepairer(taskId,userId);
    }

}



