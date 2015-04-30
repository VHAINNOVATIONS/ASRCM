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
 * <p>Entry-point methods in the Service Layer accept persistent object
 * identifiers, not the objects themselves. This allows the calling objects
 * (that is, the Presentation Layer) to contain just "glue code" and this layer
 * to contain all calls to load objects from persistence, etc.</p>
 * 
 * <p>Accordingly, entry-point methods that update objects take command objects
 * such as {@link gov.va.med.srcalc.service.EditVariable}. The advantage of
 * taking these command objects over simply allowing the calling code to freely
 * modify the domain objects is restricting the valid changes that may be made
 * as part of a particular business operation. For example, the aforementioned
 * {@link gov.va.med.srcalc.service.EditVariable} allows calling code to update
 * a variable's display text and help text but not its key, which should remain
 * static.</p>
 */
package gov.va.med.srcalc.service;