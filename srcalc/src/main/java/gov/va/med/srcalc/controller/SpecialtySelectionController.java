package gov.va.med.srcalc.controller;

import java.util.List;

import gov.va.med.srcalc.domain.Specialty;
import gov.va.med.srcalc.service.DefaultSpecialtyService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpecialtySelectionController
{
    private final DefaultSpecialtyService fSpecialtyService;
    
    public SpecialtySelectionController(DefaultSpecialtyService specialtyService)
    {
        fSpecialtyService = specialtyService;
    }

    @RequestMapping("/")
    public String presentSelection(final Model model)
    {
        final List<Specialty> specialties = fSpecialtyService.getAllSpecialties();
	model.addAttribute("specialties", specialties);
	return specialties.toString();
    }
}
