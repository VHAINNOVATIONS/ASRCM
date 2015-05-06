/**
 * <p>This is the Service Layer for the Surgical Risk Calculator, that is, the
 * Domain Model's primary interface to the outside world. Unlike the Domain
 * Model, which consists of many objects with many fine-grained interfaces,
 * these classes present fewer, higher-level business operations.</p>
 * 
 * <p>Beyond facilitating interaction with the Domain Model, these
 * business-operation-oriented methods present a natural transaction boundary
 * and therefore most of them represent one transaction.</p>
 * 
 * <p>Since entry-point methods in the Service Layer represent transactions,
 * many methods accept persistent object identifiers instead of objects
 * themselves in order to encapsulate database interaction within the
 * transaction. For example,
 * {@link CalculationService#setSpecialty(gov.va.med.srcalc.domain.Calculation, String)}
 * accepts a String identifying a specialty instead of the specialty object
 * itself. Other methods, such as
 * {@link AdminService#updateVariable(gov.va.med.srcalc.domain.variable.AbstractVariable)},
 * accept domain objects, but require that the caller provide an object that it
 * previously obtained from a different Service Layer method.</p>
 */
package gov.va.med.srcalc.service;