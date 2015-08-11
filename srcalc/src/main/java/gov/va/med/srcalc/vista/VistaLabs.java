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
    },
    POTASSIUM
    {
        @Override
        List<String> getPossibleLabNames()
        {
            return ImmutableList.of("POTASSIUM", "K");
        }
    },
    HGA1C
    {
        @Override
        List<String> getPossibleLabNames()
        {
            return ImmutableList.of("HGA1C", "HEMOGLOBIN A1C", "A1C");
        }
    },
    GLUCOSE
    {
        @Override
        List<String> getPossibleLabNames()
        {
            return ImmutableList.of("GLUCOSE", "SERUM GLUCOSE");
        }
    };
    
    abstract List<String> getPossibleLabNames();
      
}
