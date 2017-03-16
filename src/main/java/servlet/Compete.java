// ------------------------------------------------------------------------------------ //
// Compete.java    by JA Solheim    14 OCT 2016
// This is the Compete servlet ...
// ------------------------------------------------------------------------------------ //

import  java.io.* ;
import  javax.servlet.* ;
import  javax.servlet.http.* ;
import  java.util.* ;
import  java.sql.* ;
import  javax.servlet.annotation.WebServlet ;

// ------------------------------------------------------------------------------------ //

@WebServlet ( name = "Compete", urlPatterns = { "/Compete" } )
public class Compete extends HttpServlet
  {

  // ------------------  method to service HTTP POST requests  --------------------- //
  @Override
  public void doPost ( HttpServletRequest request, HttpServletResponse response )
    throws ServletException, IOException
    {
	Connection connection     =  null ;
	Statement  statement      =  null ;
	final String DB_URL       =  System.getenv ( "JDBC_DATABASE_URL" ) ;
    response.setContentType ( "text/html" ) ;
    final PrintWriter  out              =  response.getWriter() ;
    HttpSession        session          =  request.getSession() ;
    String             sessionId        =  session.getId() ;
    String             userIdAttribute  =  ( "LOGGED_IN_USER_" + sessionId ) ;
    ServletContext     context          =  this.getServletContext() ;
    String             username         =  request.getParameter("username") ;
    String             password         =  request.getParameter("password") ;
    String             storedPassword   =  null ;

	try
		{
		connection = DriverManager.getConnection ( DB_URL ) ;
		statement = connection.createStatement() ;
		String queryString  =  null ;
	
    if ( username != null )
      {
      username     =  username.substring(0,1).toUpperCase() + username.substring(1).toLowerCase() ;
      queryString  = "select Password from Math.Competitor "
						+  "WHERE lower(Username) = lower('" + username + "') " ;
	System.out.println ( "-------------------------------------" ) ;
	System.out.println (queryString) ;
	System.out.println ( "-------------------------------------" ) ;
	  ResultSet resultSet = statement.executeQuery ( queryString ) ;
	  // two possibilities:
	  // 1. resultSet is empty (username entered by user was not found in database)
	  // 2. resultSet is non-empty (username was found in database)
	  if ( resultSet.next() )
		storedPassword  =  (String) resultSet.getObject("Password") ;		  
      }

    boolean  userExists      =   (storedPassword != null) ;
    boolean  passwordsMatch  =   ( (password != null) && (storedPassword != null)
                                && (password. equals (storedPassword)) ) ;

    out.print  (  "<!DOCTYPE html>\n"
             +    "<html>\n"
             +    "  <head>\n"
             +    "    <meta charset='UTF-8'>\n"
             +    "    <title>Compete</title>\n"
             +    "    <style>\n"
             +    "      table, th, td { border:1px solid black;\n"
             +    "                      margin:10px;\n"
             +    "                      padding:10px 30px 10px 30px; }\n"
             +    "      .inline_wide { display: inline-block; width:100%; }\n"
             +    "      body { text-align: center; }\n"
             +    "      body > * { text-align: left; }\n"
             +    "      form { display: inline-block; }\n"
             +    "    </style>\n"
+ "<script type='text/javascript' async\n"
+ " src='https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-MML-AM_CHTML'>\n"
+ "</script>\n"
             +    "  </head>\n"
             +    "  <body>\n"
             +    "    <hr>\n"
             +    "    <form method='POST'>\n"
             +    "      <table>\n"  ) ;


    if ( session.isNew() )
      {
      if ( userExists && passwordsMatch )
        {
        session.setAttribute ( userIdAttribute, username ) ;
        out.print  (  "        <tr style='font-size:x-large;'><td>Hello, " + username + ".</td></tr>\n" 
     /*            +    "        <tr style='font-size:x-large;'><td>The time is " + (new java.util.Date()) + ".</td></tr>\n"
                 +    "        <tr style='font-size:x-large;'><td>This is a new session.</td></tr>\n"
                 +    "        <tr style='font-size:x-large;'><td>The session ID is " + sessionId + ".</td></tr>\n"  ) ; */

                +	"	   <h1>Here is your challenge question ...</h1> "
            	+	"	   <hr>" 
            	+	"	   <h1>HERE IS WHERE THE QUESTION WILL BE</h1>" 
            	+	"	   <hr>" 
            	+	"    <form action='CheckAnswer' method='POST'>" 
            	+	"    	<table>" 
            	+	"       <tr>" 
            	+	"         <td><input type='radio' name='choice'  ></td><td>\\( " + "THIS IS WHERE THE CHOICE WILL BE" + " \\)</td>" 
            	+	"       </tr>" 
            	+	"       <tr>" 
            	+	"         <td><input type='radio' name='choice'  ></td><td>\\( " + "THIS IS WHERE THE CHOICE WILL BE" + " \\)</td>" 
            	+	"       </tr>" 
            	+	"       <tr>" 
            	+	"         <td><input type='radio' name='choice'  ></td><td>\\( " + "THIS IS WHERE THE CHOICE WILL BE" + " \\)</td>" 
            	+	"       </tr>" 
            	+	"       <tr>" 
            	+	"         <td><input type='radio' name='choice'  ></td><td>\\( " + "THIS IS WHERE THE CHOICE WILL BE" + " \\)</td>" 
            	+	"       </tr>" 
            	+	"    		<tr>" 
            	+	"    			<td colspan='2'>" 
            	+	"    			<input type='submit' value='Click This Button to Check Your Answer'>" 
            	+	"    			</td>" 
            	+	"    		</tr>" 
            	+	"    	</table>" );
        }
      else
        {
        session.invalidate() ;
        out.print  (  "        <tr style='font-size:x-large;'><td>No user is currently logged in.</td></tr>\n"
                 +    "        <tr><td><button type='submit' class='inline_wide' formaction='index.html'>Log In</button></td></tr>\n"
                 +    "      </table>\n"
                 +    "    </form>\n"
                 +    "    <hr>\n"
                 +    "  </body>\n"
                 +    "</html>\n"  ) ;
        out.close() ;
        return ;
        }
      }
    else // session is *not* new
      {
      username  =  (String) session.getAttribute ( userIdAttribute ) ;
      out.print  (  "        <tr style='font-size:x-large;'><td>Welcome back, " + username + ".</td></tr>\n"
               +    "        <tr style='font-size:x-large;'><td>The time is " + (new java.util.Date()) + ".</td></tr>\n"
               +    "        <tr style='font-size:x-large;'><td>This is a continuation of a previously established session.</td></tr>\n"
               +    "        <tr style='font-size:x-large;'><td>The session ID is " + sessionId + ".</td></tr>\n"  ) ;
      } // end else

    out.print ( "        <tr><td><button type='submit' class='inline_wide' formaction='Compete'>Update Time</button></td></tr>\n"
             +  "        <tr><td><button type='submit' class='inline_wide' formaction='LogOut' name='username' value='"
             + username + "'>Log Out</button></td></tr>\n" ) ;

    out.print  (  "      </table>\n"
             +    "    </form>\n"
             +    "    <hr>\n" ) ;

		queryString  =	   "SELECT	 * "
						+  "FROM	 Math.Question " ;
		ResultSet resultSet = statement.executeQuery ( queryString ) ;
out.print( ""
+    "<table style='margin-left:auto;margin-right:auto;font-size:small;'>\n"
+    "    <caption>QUESTION</caption>\n"
+    "    <tr>\n"
+    "        <th>ID<br><span style='font-size:smaller;'>SERIAL</span></th>\n"
+    "        <th>QUESTIONTEXT<br><span style='font-size:smaller;'>VARCHAR</span></th>\n"
+    "        <th>CORRECTANSWER_CHOICE_ID<br><span style='font-size:smaller;'>INT4</span></th>\n"
+    "        <th>FOIL1_CHOICE_ID<br><span style='font-size:smaller;'>INT4</span></th>\n"
+    "        <th>FOIL2_CHOICE_ID<br><span style='font-size:smaller;'>INT4</span></th>\n"
+    "        <th>FOIL3_CHOICE_ID<br><span style='font-size:smaller;'>INT4</span></th>\n"
+    "    </tr>\n"
) ;
		while ( resultSet.next() ) 
			{
out.print( ""
+    "    <tr>\n"
+    "        <td>" + resultSet.getObject("ID") + "</td>\n"
+    "        <td>" + resultSet.getObject("QuestionText") + "</td>\n"
+    "        <td>" + resultSet.getObject("CorrectAnswer_Choice_ID") + "</td>\n"
+    "        <td>" + resultSet.getObject("Foil1_Choice_ID") + "</td>\n"
+    "        <td>" + resultSet.getObject("Foil2_Choice_ID") + "</td>\n"
+    "        <td>" + resultSet.getObject("Foil3_Choice_ID") + "</td>\n"
+    "    </tr>\n"
) ;
			} // end while
out.print( ""
+    "</table>\n"
+    "<hr>\n"
) ;
		} // end try block
	catch ( SQLException sqlException ) 
		{
		System.out.println ( "Caught SQLException ..." ) ;
		sqlException.printStackTrace() ;
		System.exit ( 1 ) ;
		} // end catch
	finally
		{
		try
			{
			if ( statement  != null ) statement.close() ;
			if ( connection != null ) connection.close() ;
			} // end try
		catch ( Exception exception )
			{
			System.out.println ( "Caught Exception ..." ) ;
			exception.printStackTrace() ;
			System.exit( 1 ) ;
			} // end catch
		} // end finally

    out.print  (  "  </body>\n"
             +    "</html>\n"  ) ;


    out.close() ;
    return ;
    } // end doPost method

  // ------------------  method to service HTTP GET requests  --------------------- //
  @Override
  public void doGet ( HttpServletRequest request, HttpServletResponse response )
    throws ServletException, IOException
    {
    // HTTP GET requests are forwarded on to the doPost method
    // (i.e., toPost handles both HTTP GET and HTTP POST requests)
    doPost ( request, response ) ;
    } // end doGet method

  } // end Compete class

// ------------------------------------------------------------------------------------ //
