// ------------------------------------------------------------------------------------ //
// LogOut.java    by Samuel Seaton   14 OCT 2016
// This is the LogOut servlet of the Mental Math web app.  It displays logout information.
// ------------------------------------------------------------------------------------ //
 
import java.io.* ;
import javax.servlet.* ;
import javax.servlet.http.* ;
import java.util.* ;
import javax.servlet.annotation.WebServlet ;
 
// ------------------------------------------------------------------------------------ //
 
@WebServlet ( name = "LogOut", urlPatterns = { "/LogOut" } )
public class LogOut extends HttpServlet
  {
 
  // ------------------  method to service HTTP POST requests  --------------------- //
  @Override
  public void doPost ( HttpServletRequest request, HttpServletResponse response )
    throws ServletException, IOException
    {
 
    response.setContentType ( "text/html" ) ;
    final PrintWriter  out       =  response.getWriter() ;
    HttpSession        session   =  request.getSession() ;
    String             username  =  request.getParameter ( "username" ) ;
 
    out.print  (  "<!DOCTYPE html>\n"
             +    "<html>\n"
             +    "  <head>\n"
             +    "    <meta charset='UTF-8'>\n"
             +    "    <title>Log Out</title>\n"
             +    "    <style>\n"
             +    "      table, th, td { margin:10px;\n"
             +    "                      padding:10px 30px 10px 30px; }\n"

             +    "      .inline_wide { display: inline-block; width:100%; }\n"
             +    "      body { text-align: center; }\n"
             +    "      body > * { text-align: left; }\n"
             +    "      form { display: inline-block; }\n"

             +     " form { width: 350px; "
             +     " background: #eee; "
             +     " padding: 15px;"
             +     " border-radius: 5px; }"

             +      "h2 { text-align: center; margin-top: 10px; margin-bottom: 20px;}"


             +    "    </style>\n"
             //---------------  Bootstrap Import ----------------------
             +    "    <!-- Latest compiled and minified CSS -->"
             +    "    <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css' integrity='sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u' crossorigin='anonymous'>"
             +    "    <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css' integrity='sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp' crossorigin='anonymous'>"
             +    "    <!-- Latest compiled and minified JavaScript -->"
             +    "    <script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js' integrity='sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa' crossorigin='anonymous'></script>"
            //--------------
             +    "  </head>\n"
             +    "  <body>\n"
             +    "    <hr>\n"
             +    "    <form method='POST'>\n"
            
             +    "        <h2 style='font-size:x-large;'>" + username + " has logged out</h2>\n"
             //+    "        <tr style='font-size:x-large;'><td>Logged out at " + (new java.util.Date()) + ".</td></tr>\n"
             //+    "        <tr style='font-size:x-large;'><td>Session " + session.getId() + " has ended.</td></tr>\n"
             +    "        <button type='submit' class='inline_wide btn btn-primary btn-lg' formaction='index.html'>Log In</button>\n"
   
             +    "    </form>\n"
             +    "    <hr>\n"
             +    "  </body>\n"
             +    "</html>\n"  ) ;
 
    out.close() ;
    session.invalidate() ; // invalidate session *after* using it (above)
    return ;
    } // end doPost method
 
  // ------------------  method to service HTTP GET requests  --------------------- //
  @Override
  public void doGet ( HttpServletRequest request, HttpServletResponse response )
    throws ServletException, IOException
    {
    doPost ( request, response ) ;
    } // end doGet method
 
  } // end LogOut class
 
// ------------------------------------------------------------------------------------ //