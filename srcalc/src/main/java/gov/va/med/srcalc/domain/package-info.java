/**
 * <p>The Domain Model. Represents the application entities (calculations,
 * variables, terms, etc.) as Java objects, incorporating both behavior and
 * data. These objects are "plain old Java Objects" (POJOs) and, as such, are
 * not bound by any particular Java frameworks such as Enterprise Java Beans.
 * They do, however, follow the Object-Oriented Design (OOD) principles of
 * <i>encapsulation</i>, <i>information and implementation hiding</i>,
 * <i>polymorphism</i>, etc.</p>
 * 
 * <p>An explanation of OOD principles is outside the scope of this
 * documentation, but one consequence of following these principles is to
 * include as much relevant behavior in the objects as possible. For example,
 * there is a Calculation class representing a risk calculation performed by a
 * user. Although the logic to calculate the risk outcomes could live in one of
 * the other layers, the logic lives in the Calculation objects itself,
 * encapsulating both the data (the calculation input values) and the behavior
 * (the algorithm to calculate the outcomes).</p>
 * 
 * <p>One advantage of the above-described encapsulation is isolating the vast
 * majority of business logic in objects that do not have dependencies on
 * external systems. This isolation facilitates automated testing of the
 * business logic.</p>
 * 
 * <p>There is one exception, however, to these classes being isolated from
 * other layers and external systems: many of these classes bear JPA (Java
 * Persistence API) annotations. These annotations are actually components of
 * the Data Layer, not the Domain Model, but the development team decided to use
 * the annotations because they greatly facilitate Object-Relational Mapping
 * (ORM). Note that these annotations do not actually make the objects'
 * interfaces dependent on any ORM technology. In other words, even though the
 * JPA annotations are a compile-time dependency of these classes, consumers of
 * these objects need not be aware of JPA at all.</p>
 */
package gov.va.med.srcalc.domain;
