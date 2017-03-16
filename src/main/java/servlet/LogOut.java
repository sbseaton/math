// ------------------------------------------------------------------------------------ //
// LogOut.java    by JA Solheim    14 OCT 2016
// This is the LogOut servlet of the Time web app.  It displays logout information.
// ------------------------------------------------------------------------------------ //

import  java.io.* ;
import  javax.servlet.* ;
import  javax.servlet.http.* ;
import  java.util.* ;
import  javax.servlet.annotation.WebServlet ;

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
             +    "      table, th, td { border:1px solid black;\n"
             +    "                      margin:10px;\n"
             +    "                      padding:10px 30px 10px 30px; }\n"
             +    "      .inline_wide { display: inline-block; width:100%; }\n"
             +    "      body { text-align: center; }\n"
             +    "      body > * { text-align: left; }\n"
             +    "      form { display: inline-block; }\n"
             +    "    </style>\n"
             +    "  </head>\n"
             +    "  <body>\n"
             +    "    <hr>\n"
             +    "    <form method='POST'>\n"
             +    "      <table>\n"
             +    "        <tr style='font-size:x-large;'><td>" + username + " has logged out.</td></tr>\n"
             +    "        <tr style='font-size:x-large;'><td>Logged out at " + (new java.util.Date()) + ".</td></tr>\n"
             +    "        <tr style='font-size:x-large;'><td>Session " + session.getId() + " has ended.</td></tr>\n"
             +    "        <tr><td><button type='submit' class='inline_wide' formaction='index.html'>Log In</button></td></tr>\n"
             +    "      </table>\n"
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
