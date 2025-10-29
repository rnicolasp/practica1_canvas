<html>
<body>
<h2>Hello World!</h2>

 <table border="1">
   <tr>
     <th>Id</th>
     <th>N. primer</th>
   </tr>
<%

    int contadorVegades = 20;
    int temp = 1;
    while (contadorVegades>0) {
        boolean primer = true;
        for(int i=2; i<temp;i++){
            if (temp % i == 0) {
                primer = false;
            }
        }
        if (primer) {
            out.println("<tr><td>");
            out.println(20-contadorVegades);
            out.println("</td>");
            out.println("<td>");
            out.println(temp);
            out.println("</td></tr>");
            contadorVegades--;
        }
        temp++;
    }
%>
</table>

</body>
</html>
