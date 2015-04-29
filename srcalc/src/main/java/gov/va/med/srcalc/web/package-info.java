/**
 * <p>The Web Presentation Layer of the application. These classes present the
 * application data to users (whether humans or machines) and, as appropriate,
 * allow those users to modify the data.</p>
 * 
 * <p>This layer uses a Model-View-Controller design and, specifically, <a
 * href="http://docs.spring.io/spring/docs/4.0.x/spring-framework-reference/html/mvc.html">the
 * Spring Web MVC Framework</a>.</p>
 * 
 * <p>The <em>Model</em> objects are of two categories:</p>
 * 
 * <ol>
 * <li>the direct objects returned by the Service Layer (e.g.,
 * {@link gov.va.med.srcalc.domain.Calculation} or
 * {@link gov.va.med.srcalc.service.EditVariable}) when they already contain all
 * the properties that the view requires; or</li>
 * <li>operation-specific wrappers (e.g.,
 * {@link gov.va.med.srcalc.web.view.VariableSummary}) when the view requires
 * extra properties.</li>
 * </ol>
 * 
 * <p>The <em>Views</em> are standard JSPs with a custom tag library. The custom
 * tag library provides the master page layout (via the <code>basePage</code>
 * and <code>adminPage</code> tags) and utility tags such as the
 * <code>variableSpecific</code> tag.</p>
 * 
 * <p>The <em>Controllers</em> are classes annotated with Spring's {@link
 * org.springframework.stereotype.Controller @Controller}.</p>
 * 
 * @see gov.va.med.srcalc.web.controller
 */
package gov.va.med.srcalc.web;