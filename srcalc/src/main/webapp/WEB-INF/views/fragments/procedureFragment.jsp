<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:hidden path="${displayItemParam.varPath}" cssClass="procedureHiddenInput" />
<div class="procedureSelectGroup dialog uninitialized" title="Select ${displayItemParam.displayName}">
	<span class="loadingText">Loading...</span>
	<!-- The table will be filled by Javascript. -->
	<table id="procedureTable">
	   <thead><tr><th>CPT Code</th><th>Description</th><th>RVU</th><th>Select</th></tr></thead>
	</table>
</div>
<!-- This text will be replaced by Javascript. -->
<span class="procedureDisplay"><span class="loadingText">Loading...</span></span>
<!-- Display any errors immediately following the input control. -->
<form:errors path="${displayItemParam.varPath}" cssClass="error" />