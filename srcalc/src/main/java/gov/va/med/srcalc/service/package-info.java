/**
 * <p>This is the Service Layer for the Surgical Risk Calculator, that is, the
 * domain model's primary interface to the outside world. Unlike the domain
 * model, these classes are oriented around use cases, not objects.</p>
 * 
 * <p>Entry-point methods in the Service Layer accept persistent object
 * identifiers, not the objects themselves. This allows the calling objects
 * (that is, the Presentation Layer) to contain just "glue code" and this layer
 * to contain all calls to load objects from persistence, etc.</p>
 */
package gov.va.med.srcalc.service;