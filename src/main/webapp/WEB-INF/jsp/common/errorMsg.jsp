<script type="text/javascript">
	<c:set var="errorKey" value="MSG"></c:set>
	<c:forEach begin="1" end="103" var="i">
		var <c:out value="${errorKey.concat(i)}"></c:out>= "<spring:message code="${errorKey.concat(i)}" />";
	</c:forEach>
</script>