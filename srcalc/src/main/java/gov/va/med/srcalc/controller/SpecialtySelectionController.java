package gov.va.med.srcalc.controller;

import java.util.Set;

import gov.va.med.srcalc.domain.Specialty;
import gov.va.med.srcalc.service.SpecialtyService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpecialtySelectionController
{
    private final SpecialtyService fSpecialtyService;
    
    public SpecialtySelectionController(SpecialtyService specialtyService)
    {
        fSpecialtyService = specialtyService;
    }

    @RequestMapping("/")
    public String presentSelection(final Model model)
    {
        final Set<Specialty> specialties = fSpecialtyService.getAllSpecialties();
	model.addAttribute("specialties", specialties);
	return specialties.toString();
    }
}
