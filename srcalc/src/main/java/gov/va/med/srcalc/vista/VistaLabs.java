package gov.va.med.srcalc.vista;

import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Enumerates all lab tests which this application can retrieve from VistA.
 */
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
    },
    WBC
    {
        @Override
        List<String> getPossibleLabNames()
        {
            return ImmutableList.of(
                    "WBC",
                    "WHITE BLOOD COUNT");
        }
    },
    PLATELETS
    {
        @Override
        List<String> getPossibleLabNames()
        {
            return ImmutableList.of("PLATELET COUNT");
        }
    },
    HEMATOCRIT
    {
        @Override
        List<String> getPossibleLabNames()
        {
            return ImmutableList.of("HCT");
        }
    },
    SODIUM
    {
        @Override
        List<String> getPossibleLabNames()
        {
            return ImmutableList.of(
                    "SODIUM",
                    "NA");
        }
    },
    INR
    {
        @Override
        List<String> getPossibleLabNames()
        {
            return ImmutableList.of("INR");
        }
    },
    BILIRUBIN
    {
        @Override
        List<String> getPossibleLabNames()
        {
            return ImmutableList.of("TOT. BILIRUBIN");
        }
    },
    PTT
    {
        @Override
        List<String> getPossibleLabNames()
        {
            return ImmutableList.of("PTT");
        }
    };
    
    /**
     * Returns a list of the possible names for this lab. Each lab has multiple names
     * because there are not enterprise standard titles for the labs, only local titles.
     */
    abstract List<String> getPossibleLabNames();
      
}
