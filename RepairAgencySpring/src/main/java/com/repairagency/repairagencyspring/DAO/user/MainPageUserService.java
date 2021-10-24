package com.repairagency.repairagencyspring.DAO.user;

import com.repairagency.repairagencyspring.DAO.BalanceDAO;
import com.repairagency.repairagencyspring.DAO.Exceptions.CommentCreateException;
import com.repairagency.repairagencyspring.DAO.Exceptions.TaskNotFoundException;
import com.repairagency.repairagencyspring.DAO.Exceptions.UserNotFoundException;
import com.repairagency.repairagencyspring.DAO.RepoRedirectService;
import com.repairagency.repairagencyspring.dto.RepairTaskDTO;
import com.repairagency.repairagencyspring.entity.*;
import com.repairagency.repairagencyspring.repos.RepairTaskRepository;
import com.repairagency.repairagencyspring.repos.ServiceNameRepository;
import com.repairagency.repairagencyspring.repos.UserRepository;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;

@Service
public class MainPageUserService {
    private final UserRepository userRepository;
    private final RepairTaskRepository repairTaskRepository;
    private final ServiceNameRepository serviceNameRepository;
    private final BalanceDAO balanceDAO;
    private final ResourceBundleMessageSource resourceBundleMessageSource;


    public MainPageUserService(UserRepository userRepository,
                               RepairTaskRepository repairTaskRepository,
                               ServiceNameRepository serviceNameRepository, BalanceDAO balanceDAO, ResourceBundleMessageSource resourceBundleMessageSource) {
        this.userRepository = userRepository;
        this.repairTaskRepository = repairTaskRepository;
        this.serviceNameRepository = serviceNameRepository;
        this.balanceDAO = balanceDAO;
        this.resourceBundleMessageSource = resourceBundleMessageSource;
    }

    public long getCentsForExistingLogin(String login){
        return userRepository.findByLogin(login).get().getAccount().getAmount();
    }

    public Page<RepairTaskDTO> getPageOwnerLogin(String login, Pageable pageable){
        return repairTaskRepository.findByOwner_Login(login,pageable);
    }

    public Iterable<ServiceName> getServicesOrderByName(){
        return serviceNameRepository.findAll(Sort.by(Sort.Order.asc("name")));
    }

    @Transactional
    public void saveRepairTask(Long serviceId, String login, RepairTaskDTO repairTaskDTO){
        repairTaskDTO.setServiceName(serviceNameRepository.findById(serviceId).orElseThrow(RuntimeException::new));
        repairTaskDTO.setOwner(userRepository.findByLogin(login).get()); //User has already authenticated, so anyway, he is present in db
        repairTaskRepository.save(new RepairTask(repairTaskDTO));
    }

    public String createAndSaveRepairTask(Long serviceId,
                                         HttpServletRequest request,
                                         RedirectAttributes redirectAttributes,
                                         String login){
        RepairTaskDTO repairTaskDTO = new RepairTaskDTO();
        repairTaskDTO.setWorkStatus(WorkStatus.FREE);
        repairTaskDTO.setPayStatus(PayStatus.FREE);
        repairTaskDTO.setDateCreate(LocalDateTime.now());
        try {
            saveRepairTask(serviceId, login, repairTaskDTO);
        } catch (RuntimeException ex){
            redirectAttributes.addFlashAttribute("saveDbError",ex.getMessage());
        }
        return "redirect:/account/user?"+RepoRedirectService.parametersFromHttpRequest(request);
    }

    public String addUserMoney(String login, String money, Locale locale){
        try {
            final Number moneyValue = NumberFormat.getNumberInstance(locale).parse(money);
            balanceDAO.addAmount(login, (long) (moneyValue.floatValue() * 100));
            return "redirect:../user";
        } catch (Exception ignore){
            return "redirect:../user?errorValue="+money;
        }
    }

    @Transactional
    public void saveFeedBack(String feedBack, Long id, String name) {
            RepairTask repairTask = repairTaskRepository.findByOwner_LoginAndIdAndWorkStatus(name, id, WorkStatus.DONE).orElseThrow(CommentCreateException::new);
            repairTask.setFeedBackMessage(feedBack);
            repairTaskRepository.save(repairTask);
    }

    public HashMap<String, Object> attemptToPayForTheService(Long taskId, String login, Locale locale) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("status","ok");
        result.put("id",taskId);
        try {
            RepairTask repairTask = repairTaskRepository.findByOwner_LoginAndIdAndPayStatus(login, taskId, PayStatus.WAIT).orElseThrow(TaskNotFoundException::new);
            UserAccount account = repairTask.getOwner().getAccount();
            Long price = repairTask.getPrice();
            Long amount=account.getAmount();
            if(price>amount){
                result.put("status","error");
                result.put("message",resourceBundleMessageSource.getMessage("user.money.not.enough",null,locale));
            } else {
                account.setAmount(account.getAmount()-price);
                repairTask.setPayStatus(PayStatus.PAID);
                repairTaskRepository.save(repairTask);
                result.put("message",resourceBundleMessageSource.getMessage(PayStatus.PAID.getMessageId(),null,locale));
            }
        } catch (TaskNotFoundException ignore){
            result.put("status","error");
            result.put("message","");
        }
        return result;
    }

    public String getFeedbackMessage(Long id) {
        try {
            return repairTaskRepository.findById(id).orElseThrow(UserNotFoundException::new).getFeedBack().getMessage();
        } catch (Exception ignore){}
        return "";
    }
}
