<%@ page isELIgnored="false" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<html>

<body>
<h1> Formular </h1>

<form method="post" action="/form">
    Escriu un numero:
    <input type="number" name="var1">
    <br>
    Escriu un altre numero:
    <input type="number" name="var2">
    <br>
    Tria una operacio:
    <select name="operacio">
      <option value="suma">Suma</option>
      <option value="resta">Resta</option>
      <option value="multiplicacio">Multiplicacio</option>
      <option value="divisio">Divisio</option>
    </select>
    <input type="submit">
        <br>
</form>

</body>

</html>