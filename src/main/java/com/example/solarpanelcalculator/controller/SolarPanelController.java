package com.example.solarpanelcalculator.controller;

import com.example.solarpanelcalculator.model.*;
import com.example.solarpanelcalculator.repository.*;
import com.example.solarpanelcalculator.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Controller
public class SolarPanelController {

    @Autowired
    private ApplianceRepository applianceRepository;

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AIService aiService;

    @GetMapping("/appliances")
    public String appliances(
        Model model,
        Authentication authentication,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        User user = userRepository.findByEmail(authentication.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<Appliance> appliancesPage = applianceRepository.findByUserId(user.getId(), pageable);

        model.addAttribute("appliancesPage", appliancesPage);
        model.addAttribute("appliance", new Appliance());
        return "appliances";
    }

    @PostMapping("/appliances")
    public String addAppliance(@ModelAttribute("appliance") @Valid Appliance appliance,
                               BindingResult bindingResult,
                               Authentication authentication,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return appliances(model, authentication, 0, 5);
        }
        User user = userRepository.findByEmail(authentication.getName());
        appliance.setUser(user);
        applianceRepository.save(appliance);
        return "redirect:/appliances";
    }

    @GetMapping("/appliance/edit/{id}")
    public String editAppliance(@PathVariable("id") Long id, Model model, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        Optional<Appliance> optionalAppliance = applianceRepository.findById(id);
        if (optionalAppliance.isPresent()) {
            Appliance appliance = optionalAppliance.get();
            if (appliance.getUser().getId().equals(user.getId())) {
                model.addAttribute("appliance", appliance);
                return "edit-appliance";
            }
        }
        return "redirect:/appliances";
    }

    @PostMapping("/appliance/update")
    public String updateAppliance(@ModelAttribute("appliance") @Valid Appliance appliance,
                                  BindingResult bindingResult,
                                  Authentication authentication,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            return "edit-appliance";
        }
        User user = userRepository.findByEmail(authentication.getName());
        Optional<Appliance> optionalAppliance = applianceRepository.findById(appliance.getId());
        if (optionalAppliance.isPresent()) {
            Appliance existingAppliance = optionalAppliance.get();
            if (existingAppliance.getUser().getId().equals(user.getId())) {
                existingAppliance.setApplianceName(appliance.getApplianceName());
                existingAppliance.setPowerConsumption(appliance.getPowerConsumption());
                applianceRepository.save(existingAppliance);
            }
        }
        return "redirect:/appliances";
    }

    @GetMapping("/appliance/delete/{id}")
    public String deleteAppliance(@PathVariable("id") Long id, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        Optional<Appliance> optionalAppliance = applianceRepository.findById(id);
        if (optionalAppliance.isPresent()) {
            Appliance appliance = optionalAppliance.get();
            if (appliance.getUser().getId().equals(user.getId())) {
                applianceRepository.delete(appliance);
            }
        }
        return "redirect:/appliances";
    }

    @GetMapping("/calculate")
    public String showCalculationForm(Model model, Authentication authentication) {
        model.addAttribute("analysis", new Analysis());
        return "calculate-form";
    }

    @PostMapping("/calculate")
    public String calculate(@ModelAttribute("analysis") @Valid Analysis analysis,
                            BindingResult bindingResult,
                            Model model,
                            Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        List<Appliance> appliances = applianceRepository.findByUserId(user.getId());

        if (appliances.isEmpty()) {
            model.addAttribute("error", "Você precisa adicionar eletrodomésticos antes de calcular.");
            return "calculate-form";
        }

        if (analysis.getSunlightHours() == 0) {
            analysis.setSunlightHours(5);
            model.addAttribute("warning", "Número de horas de sol não fornecido. Usando valor padrão de 5 horas.");
        } else if (analysis.getSunlightHours() > 24) {
            bindingResult.rejectValue("sunlightHours", "error.analysis", "O número máximo de horas é 24.");
        }

        if (bindingResult.hasErrors()) {
            return "calculate-form";
        }

        double totalConsumption = appliances.stream().mapToDouble(Appliance::getPowerConsumption).sum() / 1000;

        String result = aiService.calculateSolarPanels(totalConsumption, analysis.getSunlightHours());

        List<ApplianceData> appliancesData = appliances.stream()
            .map(appliance -> new ApplianceData(appliance.getApplianceName(), appliance.getPowerConsumption()))
            .toList();

        analysis.setTotalConsumption(totalConsumption);
        analysis.setResult(result);
        analysis.setUser(user);
        analysis.setAppliancesData(appliancesData);

        analysisRepository.save(analysis);

        model.addAttribute("result", result);
        model.addAttribute("totalConsumption", totalConsumption);
        model.addAttribute("sunlightHours", analysis.getSunlightHours());
        model.addAttribute("appliances", appliances);

        return "calculation-result";
    }

    @GetMapping("/analyses")
    public String viewAnalyses(
        Model model,
        Authentication authentication,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        User user = userRepository.findByEmail(authentication.getName());
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Analysis> analysesPage = analysisRepository.findByUserId(user.getId(), pageable);

        model.addAttribute("analysesPage", analysesPage);
        return "analyses";
    }

    @GetMapping("/analysis/delete/{id}")
    public String deleteAnalysis(@PathVariable("id") Long id, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        Optional<Analysis> optionalAnalysis = analysisRepository.findById(id);
        if (optionalAnalysis.isPresent()) {
            Analysis analysis = optionalAnalysis.get();
            if (analysis.getUser().getId().equals(user.getId())) {
                analysisRepository.delete(analysis);
            }
        }
        return "redirect:/analyses";
    }

    @GetMapping("/analysis/edit/{id}")
    public String editAnalysis(@PathVariable("id") Long id, Model model, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        Optional<Analysis> optionalAnalysis = analysisRepository.findById(id);
        if (optionalAnalysis.isPresent()) {
            Analysis analysis = optionalAnalysis.get();
            if (analysis.getUser().getId().equals(user.getId())) {
                model.addAttribute("analysis", analysis);
                return "edit-analysis";
            }
        }
        return "redirect:/analyses";
    }

    @PostMapping("/analysis/update")
    public String updateAnalysis(@ModelAttribute("analysis") @Valid Analysis analysis,
                                 BindingResult bindingResult,
                                 Authentication authentication,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "edit-analysis";
        }
        User user = userRepository.findByEmail(authentication.getName());
        Optional<Analysis> optionalAnalysis = analysisRepository.findById(analysis.getId());
        if (optionalAnalysis.isPresent()) {
            Analysis existingAnalysis = optionalAnalysis.get();
            if (existingAnalysis.getUser().getId().equals(user.getId())) {
                existingAnalysis.setSunlightHours(analysis.getSunlightHours());
                existingAnalysis.setResult(analysis.getResult());
                analysisRepository.save(existingAnalysis);
            }
        }
        return "redirect:/analyses";
    }
}
