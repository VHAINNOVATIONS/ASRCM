package gov.va.med.srcalc.vista;

import java.util.List;

import com.google.common.collect.ImmutableList;

enum VistaLabs
{
    ALBUMIN
    {
        @Override
        List<String> getPossibleLabNames()
        {
            return ImmutableList.of("ALBUMIN");
        }
    },
    CREATININE
    {
        @Override
        List<String> getPossibleLabNames()
        {
            return ImmutableList.of("CREATININE");
        }
    },
    ALKALINE_PHOSPHATASE
    {
        @Override
        List<String> getPossibleLabNames()
        {
            return ImmutableList.of("ALKALINE PHOSPHATASE");
        }
    },
    BUN
    {
        @Override
        List<String> getPossibleLabNames()
        {
            return ImmutableList.of(
                    "BUN",
                    "UREA NITROGEN",
                    "BLOOD UREA NITROGEN");
        }
    },
    SGOT
    {
        @Override
        List<String> getPossibleLabNames()
        {
            return ImmutableList.of(
                    "SGOT", 
                    "Transferase Aspartate SGOT",
                    "Aspartate Aminotransferase",
                    "AST");
        }
    };
    
    abstract List<String> getPossibleLabNames();
      
}
