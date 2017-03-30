// ------------------------------------------------------------------------------------ //
// Compete.java    by Samuel Seaton    14 OCT 2016

// This is the Compete servlet ...

/* 
here is how you display the answer text : 

	select *
	from math.choice
	inner join math.question 
	on (choice.id = question.foil1_choice_id) 
	or (choice.id = question.foil2_choice_id) 
	or (choice.id = question.foil3_choice_id) 
	or (choice.id = question.correctanswer_choice_id)   
	where question.id = 301 ; */

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
    // find session ID and user ID
    String             sessionId        =  session.getId() ;
    String             userIdAttribute  =  ( "LOGGED_IN_USER_" + sessionId ) ;

    ServletContext     context          =  this.getServletContext() ;
    String             username         =  request.getParameter("username") ;
    String             password         =  request.getParameter("password") ;
    String             storedPassword   =  null ;
	  String 			userChoice = null;

    boolean userLoggedIn ; 

    int questionNumber; 
    String currentQuestionNumberString = request.getParameter( "questionNumber" );

    // find if this is the first question or not ---------------------------------------
    if ( currentQuestionNumberString != null )
        questionNumber = Integer.parseInt(currentQuestionNumberString);
    else
        questionNumber = 301;
    // ----------------------------------------------------------------------------------


  try
    {
    connection = DriverManager.getConnection ( DB_URL ) ;
    statement = connection.createStatement() ;
    String queryString  =  null ;
	  String questionString = null;
	  String questionAnswersString = null ; 	// question one answer string]

   
	  String questionNumberString = null ; 
  
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

	  } // end if

    boolean  userExists      =   (storedPassword != null) ;
    boolean  passwordsMatch  =   ( (password != null) && (storedPassword != null)
                                && (password. equals (storedPassword)) ) ;

    // html display ---------------------------------------------------------------

    out.print  (  "<!DOCTYPE html>\n"
				+    "<html>\n"
				+    "  <head>\n"
				+    "    <meta charset='UTF-8'>\n"
				+    "    <title>Compete</title>\n"
				+    "    <style> "
				+    "    </style>\n"
				+ "<script type='text/javascript' async\n"
				+ " src='https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-MML-AM_CHTML'>\n"
				+ "</script>\n"
				+    "  </head>\n"
				+    "  <body>\n" );
    /*
				+    "    <hr>\n"
				// +    "    <form method='POST'>\n"

				+    "     <h2 style='text-align:center'> Mental Math Game </h2> \n"
    
				+    "   <h3> Username: "+ username +" </h3>  \n "
				+    "   <p style=\"text-align:center\">Time Remaining 00&#58;50&#58;00 </p> \n"
				+    "    <br>" 
				+    "    <hr style='border: 2px solid #FFD700'> \n  "
				+    "      <h4> Question (put questionNumberHere) </h4>   \n "
				+    "      <p> &#40;101 points possible&#41; </p> "
				
				// add the current score keeping right here --------------------------------------------------------
				+    "      <p> 0 points total</p> "
				+    "     <hr style='border: 2px solid #FFD700'> \n   "
				+    "     <label> What is the answer to the following problem? </label> \n  "
				+    "      <br><br> \n " ); 
				// +    "      <table>\n"	);
        */
						
    if ( session.isNew() )
    {

	
		  // if the user exists and the password matches, display the first webpage
		  if ( userExists && passwordsMatch )
		  {
			 session.setAttribute ( userIdAttribute, username ) ;
		   userLoggedIn = true; 
      }
      //  if the username and password don't match then display no user is logged in page ---------------------------------------------------------
      else    
      { 
        session.invalidate(); 
        out.print  (  ""
        +    "      <table>\n"
        +   "   <tr style='font-size:x-large;'> "
        +    "    <td>No user is currently logged in. </td> "
        +   "   </tr>\n"
                +    "     <tr><td><button type='submit' class='inline_wide' formaction='index.html'>Log In</button></td></tr>\n"
                +    "      </table>\n"
                +    "    </form>\n"
                +    "    <hr>\n"
                +    "  </body>\n"
                +    "</html>\n"  ) ;
        out.close() ;
        return ;
        }
      //-------------------------------------------------------------------------------------------------------------------------------------------
		} // end if session.isNew() 

    else 
    {
        // the session is not new, but the user is logged in 
        // and is returning from checkAnswer servlet for a new problem
        userLoggedIn = true;

        username = (String) session.getAttribute (userIdAttribute) ;
    }   // end else
      
		
		// query for question 1 ---------------------------------------------------------------------------------------------------------------------

    if (userLoggedIn == true )
    {
			questionString = "SELECT * "
						+ "FROM 	Math.Question "
						+ "WHERE 	ID =" + questionNumber + " ";
				
			ResultSet questionRS = statement.executeQuery ( questionString ) ;
      String questionText = "";
      // display the question text 
			if (questionRS.next() )
        questionText = (String) questionRS.getObject("QuestionText") ;


        // gets the answer text from database-------------------------------------------------------
        questionAnswersString = "select * " 
                + "from math.choice "
                + "inner join math.question "
                + "on (choice.id = question.foil1_choice_id) "
                + "or (choice.id = question.foil2_choice_id) "
                + "or (choice.id = question.foil3_choice_id) "
                + "or (choice.id = question.correctanswer_choice_id)  "
                + "where question.id =" + questionNumber + " " ;
        
        
        // create a result set for the question answers
        ResultSet questionAnswers = statement.executeQuery ( questionAnswersString ) ;
        

			out.println  ( "" 
        +    "    <hr>\n"
        +    "     <h2 style='text-align:center'> Mental Math Game </h2> \n"
        +    "   <h3> Username: "+ username +" </h3>  \n "
        +    "   <p style=\"text-align:center\">Time Remaining 00&#58;50&#58;00 </p> \n"
        +    "    <br>" 
        +    "    <hr style='border: 2px solid #FFD700'> \n  "
        +    "      <h4> Question "+ questionNumber +" </h4>   \n "
        +    "      <p> &#40;101 points possible&#41; </p> "
        // add the current score keeping right here --------------------------------------------------------
        +    "      <p> 0 points total</p> "
        +    "     <hr style='border: 2px solid #FFD700'> \n   "
        +    "     <label> What is the answer to the following problem? </label> \n  "
        +    "      <br><br> \n " ); 
				+   "      <table>\n"
				+		"	<tr>     "
				+     	"   	<td> &nbsp;&ensp;&ensp;" + questionText + " = </td>      "
				+     	"  	</tr>      "
				+     	"</table>     "
				+     	"<br>     " ); 

			
				
				// print each answer with a radio button for user selection, assign the value to the id of the answer

      out.println ( "    <form action='CheckAnswer' method='POST'>" ) ;


				while (questionAnswers.next() )
				{
					out.println ( "	<input type=\"radio\" name=\"choice\" value="+ questionAnswers.getObject("id") + " >&nbsp;&ensp;&ensp;" + questionAnswers.getObject("choicetext") + "<br><br> \n " );
				}

				
			out.println(""	
			+	"	<table style='float:right'> "
			+	"	<tr>	"
      + "   <td><input type='hidden' name= 'questionNumber' value='" + questionNumber + "'> "
      + "   <td><input type='submit' value='Click This Button to Check Your Answer'>"
		//	+	"		<td><input type='submit' value='Submit'></td>"
			+	"	 </tr>"
			+	"	</table>"
			+  	"   <br> " );

      out.println ( " </form> ");

    } // end if the userLoggedIn
		// finds the database information for table on screen ----------------------------------------------------------------------------------------------------------------
			/*
				queryString  =     "SELECT   * "
				+  "FROM   Math.Question " ;
				ResultSet resultSet = statement.executeQuery ( queryString ) ;
				out.print(  "<table style='margin-left:auto;margin-right:auto;font-size:small;'>\n"
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
			{	out.print("    <tr>\n"
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
			*/

		// -------------------------------------------------------------------------------------------------------------------------------------------



			 /* out.print  (
                     "        <tr style='font-size:x-large;'><td>Hello, " + username + ".</td></tr>\n"
                 +    "        <tr style='font-size:x-large;'><td>The time is " + (new java.util.Date()) + ".</td></tr>\n"
                 +    "        <tr style='font-size:x-large;'><td>This is a new session.</td></tr>\n"
                 +    "        <tr style='font-size:x-large;'><td>The session ID is " + sessionId + ".</td></tr>\n"  ) ;
                 */
    // }--------------------------------------------

		
	

	

      
    out.print ( ""      	
			+   "    <form method='POST'>\n"
			+ 	"		<table> "
			+	"			<tr> "
			+	"			<td> "
			+	"			<button type='submit' class='inline_wide' formaction='LogOut' name='username' value='" + username + "'>Log Out</button> "
			+	"			</td>"
			+	"			</tr>\n" ) ;

    out.print  (  "     </table>\n"
             +    "    </form>\n"
             +    "    <hr>\n" ) ;


out.println();
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
