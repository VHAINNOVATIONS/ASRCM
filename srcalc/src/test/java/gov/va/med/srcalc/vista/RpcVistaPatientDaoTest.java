package gov.va.med.srcalc.vista;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import gov.va.med.srcalc.domain.*;
import gov.va.med.srcalc.domain.calculation.RetrievedValue;
import gov.va.med.srcalc.vista.VistaPatientDao.SaveNoteCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * Tests the {@link RpcVistaPatientDao} class.
 */
public class RpcVistaPatientDaoTest
{
    private final static String RADIOLOGIST_DUZ = "11716";
    private final static String PATIENT_RPC_RETURN = "TESTPATIENT^50^M";
    private final static String INVALID_SIGNATURE_RETURN = "0^Incorrect Electronic Signature Code Entered.";
    private final static String ELECTRONIC_SIGNATURE = "TESTSIG";
    private final static String DUMMY_BODY = "Note Body";
    private final static String ALBUMIN_SUCCESS = "ALBUMIN^3.0^02/02/2015@14:35:12^g/dl";
    private final static String INVALID_LAB = "This is invalid. ~@!#$";
    private final static String VALID_LAB_NO_UNITS = "ALBUMIN^3.0^02/02/2015@14:35:12^";
    private final static List<String> VALID_HEALTH_FACTORS = ImmutableList.of(
            "08/25/2014^REFUSED INFLUENZA IMMUNIZATION",
            "08/22/2014^DEPRESSION ASSESS POSITIVE (MDD)",
            "08/22/2014^REFUSED INFLUENZA IMMUNIZATION",
            "08/20/2014^ALCOHOL - TREATMENT REFERRAL",
            "08/08/2014^CURRENT SMOKER",
            "07/30/2014^GEC HOMELESS");
    private final static List<String> VALID_ACTIVE_MEDICATIONS = ImmutableList.of(
            "403962R;O^METOPROLOL TARTRATE 50MG TAB^3110228^^^3",
            "404062R;O^SIMVASTATIN 40MG TAB^3110228^^^3");
    private static final String ADL_ENTERPRISE_TITLE = "NURSING ADMISSION EVALUATION NOTE";
    private final static List<String> VALID_ADL_NOTES = ImmutableList.of(
            "<notes>",
            "<note localTitle='AUDIOLOGY - HEARING LOSS CONSULT' signDate='04/01/2004 22:24'>",
            "<body>",
            "<![CDATA[HX:  Patient was seen for hearing aid fitting and orientation.]]>",
            "<![CDATA[The batteries supplied for this hearing aid were: za312.]]>",
            "</body>",
            "</note>",
            "</notes>");
    private final static String VALID_NOTE_BODY =
            "\nHX:  Patient was seen for hearing aid fitting and orientation.\n" +
            "The batteries supplied for this hearing aid were: za312.\n";
    private final static List<String> VALID_DNR_NOTES = ImmutableList.of(
            "<notes>",
            "<note localTitle='GENERIC DNR TITLE' signDate='04/01/2004 22:24'>",
            "<body>",
            "<![CDATA[HX:  Patient was seen for hearing aid fitting and orientation.]]>",
            "<![CDATA[The batteries supplied for this hearing aid were: za312.]]>",
            "</body>",
            "</note>",
            "</notes>");
    private final static List<String> FULL_VITALS = ImmutableList.of(
            "Temp.:       (03/05/10@09:00)  98.5 F  (36.9 C)  _NURSE,ONE_Vitals",
            "Pulse:       (03/05/10@09:00)  74  _NURSE,ONE_Vitals",
            "Resp.:       (08/24/09@14:00)  18  _NURSE,ONE_Vitals",
            "Pulse Ox:    (12/01/09@08:53)  98%   _NURSE,ONE_Vitals",
            "B/P:         (03/05/10@09:00)  134/81  _NURSE,ONE_Vitals",
            "Ht.:         (08/24/09@14:00)  5 ft 11 in (180.34 cm)  _NURSE,ONE_Vitals",
            "Wt.:         (03/05/10@09:00)  178 lb  (80.74 kg)  _NURSE,ONE",
            "Body Mass Index:             24.88  _Vitals",
            "Pain:        (03/05/10@09:00)  1  _NURSE,ONE_Vitals");
    private final static List<String> PARTIAL_VITALS = ImmutableList.of(
            "Ht.:         (01/02/02@08:00)  6 ft  (182.88 cm)  _LABTECH,FIFTYNINE_Vitals",
            "Wt.:         (03/21/10@08:00)  208 lb  (94.35 kg)  _LABTECH,FIFTYNINE",
            "Body Mass Index:             28.27*  _Vitals");
    private final static List<String> NO_VITALS = ImmutableList.of(
            "There are no results to report");
    
    private final static int PATIENT_DFN = 500;

    /**
     * Creates a mock VistaProcedureCaller that returns the minimal information. Callers
     * may perform further mocking to expand the returned data.
     */
    private static VistaProcedureCaller mockVistaProcedureCaller()
    {
        try
        {
            final VistaProcedureCaller caller = mock(VistaProcedureCaller.class);
            // Anything besides a valid measurement is returned as an empty string
            when(caller.doRetrieveLabsCall(
                    eq(RADIOLOGIST_DUZ), eq(String.valueOf(PATIENT_DFN)), anyListOf(String.class)))
                .thenReturn("");
            // Setup the necessary actions for getting patient data.
            when(caller.doRpc(RADIOLOGIST_DUZ, RemoteProcedure.GET_PATIENT, String.valueOf(PATIENT_DFN)))
                .thenReturn(Arrays.asList(PATIENT_RPC_RETURN));
            // Return empty vitals
            when(caller.doRpc(
                    RADIOLOGIST_DUZ, RemoteProcedure.GET_RECENT_VITALS, String.valueOf(PATIENT_DFN)))
                .thenReturn(NO_VITALS);
            when(caller.doRpc(RADIOLOGIST_DUZ, RemoteProcedure.GET_VITAL, ""))
                .thenReturn(new ArrayList<String>());
            when(caller.doRpc(
                    RADIOLOGIST_DUZ,
                    RemoteProcedure.GET_HEALTH_FACTORS,
                    String.valueOf(PATIENT_DFN)))
                    .thenReturn(ImmutableList.of(""));
            return caller;
        }
        catch (final LoginException ex)
        {
            // The compiler sees a possible LoginException, but it is just an artifact
            // of Mockito's mocking API.
            throw new RuntimeException("Unexpected Exception.", ex);
        }
    }
    
    @Test
    public final void testLoadVistaPatientValid()
    {
        // Setup
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);

        // Behavior verification
        final Patient patient = dao.getPatient(PATIENT_DFN);
        assertEquals(PATIENT_DFN, patient.getDfn());
        assertEquals("TESTPATIENT", patient.getName());
        assertEquals(Patient.Gender.Male, patient.getGender());
        assertEquals(50, patient.getAge());
    }
    
    @Test
    public final void testSaveNoteInvalidSignature() throws Exception
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        when(caller.doSaveProgressNoteCall(anyString(), anyString(), anyString(), anyListOf(String.class)))
            .thenReturn(INVALID_SIGNATURE_RETURN);
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        
        // The note body being used here should not matter since the doRpc() call is being
        // mocked and is told what to return.
        assertEquals(SaveNoteCode.INVALID_SIGNATURE,
                dao.saveRiskCalculationNote(PATIENT_DFN, "BadSig", DUMMY_BODY));
        assertEquals(SaveNoteCode.INVALID_SIGNATURE,
                dao.saveRiskCalculationNote(PATIENT_DFN, "AlsoBad", DUMMY_BODY));
    }
    
    @Test
    public final void testSaveNoteSuccess() throws Exception
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        when(caller.doSaveProgressNoteCall(anyString(), anyString(), anyString(), anyListOf(String.class)))
            .thenReturn(RemoteProcedure.VALID_SIGNATURE_RETURN);
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        
        assertEquals(
                SaveNoteCode.SUCCESS,
                dao.saveRiskCalculationNote(PATIENT_DFN, ELECTRONIC_SIGNATURE, DUMMY_BODY));
    }
    
    @Test
    public final void testLabRetrievalFailure() throws Exception
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        // Behavior verification
        final Patient patient = dao.getPatient(PATIENT_DFN);
        assertEquals(new HashMap<String, RetrievedValue>(), patient.getLabs());
    }
    
    @Test
    public final void testLabRetrievalSuccess() throws Exception
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        when(caller.doRetrieveLabsCall(
                RADIOLOGIST_DUZ, 
                String.valueOf(PATIENT_DFN),
                VistaLabs.ALBUMIN.getPossibleLabNames()))
            .thenReturn(ALBUMIN_SUCCESS);
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        // Behavior verification
        final Patient patient = dao.getPatient(PATIENT_DFN);
        assertEquals(1, patient.getLabs().size());
        final RetrievedValue value = patient.getLabs().get(VistaLabs.ALBUMIN);
        assertEquals(3.0, value.getValue(), .0001);
        final DateTime expectedTime = new DateTime(2015, 2, 2, 14, 35, 12, 0);

        assertEquals(expectedTime.toDate(), value.getMeasureDate());
    }
    
    @Test
    public final void testInvalidLabResponse() throws Exception
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        when(caller.doRetrieveLabsCall(
                RADIOLOGIST_DUZ, 
                String.valueOf(PATIENT_DFN),
                VistaLabs.ALBUMIN.getPossibleLabNames()))
            .thenReturn(INVALID_LAB);
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        // Behavior verification
        final Patient patient = dao.getPatient(PATIENT_DFN);
        // Insure the lab was not added and no exceptions occurred.
        assertEquals(0, patient.getLabs().size());
        // Insure other information was still added.
        assertEquals(patient.getAge(), 50);
        assertEquals(patient.getName(), "TESTPATIENT");
    }
    
    @Test
    public final void testLabResponseNoUnits() throws Exception
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        when(caller.doRetrieveLabsCall(
                RADIOLOGIST_DUZ, 
                String.valueOf(PATIENT_DFN),
                VistaLabs.ALBUMIN.getPossibleLabNames()))
            .thenReturn(VALID_LAB_NO_UNITS);
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        // Behavior verification
        final Patient patient = dao.getPatient(PATIENT_DFN);
        // Insure the lab was not added and no exceptions occurred.
        assertEquals(1, patient.getLabs().size());
    }
    
    @Test
    public final void testHealthFactorsValid() throws Exception
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        when(caller.doRpc(
                RADIOLOGIST_DUZ,
                RemoteProcedure.GET_HEALTH_FACTORS,
                String.valueOf(PATIENT_DFN)))
                .thenReturn(VALID_HEALTH_FACTORS);
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        final Patient patient = dao.getPatient(PATIENT_DFN);
        final DateTimeFormatter format = DateTimeFormat.forPattern("MM/dd/yy");
        final List<HealthFactor> expectedFactors = ImmutableList.of(
                new HealthFactor(format.parseLocalDate("08/22/2014"),"DEPRESSION ASSESS POSITIVE (MDD)"),
                new HealthFactor(format.parseLocalDate("08/20/2014"), "ALCOHOL - TREATMENT REFERRAL"),
                new HealthFactor(format.parseLocalDate("07/30/2014"), "GEC HOMELESS"));
        
        assertEquals(expectedFactors, patient.getHealthFactors());
    }
    
    @Test
    public final void testNoHealthFactors()
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        final Patient patient = dao.getPatient(PATIENT_DFN);
        assertEquals(Collections.<HealthFactor>emptyList(), patient.getHealthFactors());
    }
    
    @Test
    public final void testInvalidHealthFactor() throws Exception
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        when(caller.doRpc(
                RADIOLOGIST_DUZ,
                RemoteProcedure.GET_HEALTH_FACTORS,
                String.valueOf(PATIENT_DFN)))
                .thenReturn(ImmutableList.of("Invalid health factors String."));
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        final Patient patient = dao.getPatient(PATIENT_DFN);
        assertEquals(Collections.<HealthFactor>emptyList(), patient.getHealthFactors());
    }
    
    @Test
    public final void testMedicationsValid() throws Exception
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        when(caller.doRpc(
                RADIOLOGIST_DUZ,
                RemoteProcedure.GET_ACTIVE_MEDICATIONS,
                String.valueOf(PATIENT_DFN)))
                .thenReturn(VALID_ACTIVE_MEDICATIONS);
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        final Patient patient = dao.getPatient(PATIENT_DFN);
        final List<String> expectedMedications = ImmutableList.of(
                "METOPROLOL TARTRATE 50MG TAB", "SIMVASTATIN 40MG TAB");
        assertEquals(expectedMedications, patient.getActiveMedications());
    }
    
    @Test
    public final void testNoMedications()
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        final Patient patient = dao.getPatient(PATIENT_DFN);
        assertEquals(Collections.<String>emptyList(), patient.getActiveMedications());
    }
    
    @Test
    public final void testInvalidMedications() throws Exception
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        when(caller.doRpc(
                RADIOLOGIST_DUZ,
                RemoteProcedure.GET_ACTIVE_MEDICATIONS,
                String.valueOf(PATIENT_DFN)))
                .thenReturn(ImmutableList.of("Invalid medications String."));
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        final Patient patient = dao.getPatient(PATIENT_DFN);
        assertEquals(Collections.<String>emptyList(), patient.getActiveMedications());
    }
    
    @Test
    public final void testValidAdlNotes() throws Exception
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        when(caller.doRpc(
                RADIOLOGIST_DUZ,
                RemoteProcedure.GET_ADL_STATUS,
                String.valueOf(PATIENT_DFN),
                ADL_ENTERPRISE_TITLE))
                .thenReturn(VALID_ADL_NOTES);
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        final Patient patient = dao.getPatient(PATIENT_DFN);
        assertEquals(1, patient.getAdlNotes().size());
        final String noteBody = patient.getAdlNotes().get(0).getNoteBody();
        assertEquals(VALID_NOTE_BODY, noteBody);
    }
    
    @Test
    public final void testInvalidAdlNotes() throws Exception
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        when(caller.doRpc(
                RADIOLOGIST_DUZ,
                RemoteProcedure.GET_ADL_STATUS,
                String.valueOf(PATIENT_DFN),
                ADL_ENTERPRISE_TITLE))
                .thenReturn(ImmutableList.of("Invalid XML"));
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        final Patient patient = dao.getPatient(PATIENT_DFN);
        // This would fail to retrieve notes and the notes would be empty.
        assertEquals(0, patient.getAdlNotes().size());
    }
    
    @Test
    public final void testValidDnrNotes() throws Exception
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        when(caller.doRpc(
                RADIOLOGIST_DUZ,
                RemoteProcedure.GET_NOTES_WITH_SUBSTRING,
                String.valueOf(PATIENT_DFN),
                "DNR"))
                .thenReturn(VALID_DNR_NOTES);
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        final Patient patient = dao.getPatient(PATIENT_DFN);
        assertEquals(1, patient.getDnrNotes().size());
        final String noteBody = patient.getDnrNotes().get(0).getNoteBody();
        assertEquals(VALID_NOTE_BODY, noteBody);
    }
    
    @Test
    public final void testInvalidDnrNotes() throws Exception
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        when(caller.doRpc(
                RADIOLOGIST_DUZ,
                RemoteProcedure.GET_NOTES_WITH_SUBSTRING,
                String.valueOf(PATIENT_DFN),
                "DNR"))
                .thenReturn(ImmutableList.of("Invalid XML"));
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        final Patient patient = dao.getPatient(PATIENT_DFN);
        // This would fail to retrieve DNR notes and the notes would be empty.
        assertEquals(0, patient.getDnrNotes().size());
    }
    
    @Test
    public final void testFullVitals() throws Exception
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        when(caller.doRpc(
                RADIOLOGIST_DUZ, RemoteProcedure.GET_RECENT_VITALS, String.valueOf(PATIENT_DFN)))
            .thenReturn(FULL_VITALS);
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        final Patient patient = dao.getPatient(PATIENT_DFN);
        final ImmutableList<Double> expectedValues = ImmutableList.of(178.0, 71.0, 24.88);
        assertEquals(expectedValues, ImmutableList.of(patient.getWeight().getValue(),
                patient.getHeight().getValue(), patient.getBmi().getValue()));
    }
    
    @Test
    public final void testPartialVitals() throws Exception
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        when(caller.doRpc(
                RADIOLOGIST_DUZ, RemoteProcedure.GET_RECENT_VITALS, String.valueOf(PATIENT_DFN)))
            .thenReturn(PARTIAL_VITALS);
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        final Patient patient = dao.getPatient(PATIENT_DFN);
        final ImmutableList<Double> expectedValues = ImmutableList.of(72.0, 208.0, 28.27);
        assertEquals(expectedValues, ImmutableList.of(patient.getHeight().getValue(),
                patient.getWeight().getValue(), patient.getBmi().getValue()));
    }
    
    @Test
    public final void testEmptyVitals()
    {
        final VistaProcedureCaller caller = mockVistaProcedureCaller();
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);
        final Patient patient = dao.getPatient(PATIENT_DFN);
        // Empty vitals means the vitals do not get filled with any values.
        assertEquals(null, patient.getWeight());
        assertEquals(null, patient.getHeight());
        assertEquals(null, patient.getBmi());
    }
}
