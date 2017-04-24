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
//      String             storedPassword   =  null ;
        String      userChoice = null;


        // -----------------------------------------------------------------------
        String username = request.getParameter("username");



        // -----------------------------------------------------------------------

      

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
            String questionAnswersString = null ;   // question one answer string]

        	boolean userLoggedIn = false;

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
            System.out.println("<h1>" + username + "</h1>");

            // ---------------------------------------------------------

        	boolean userIsAvailable = true ;				// change this back to false

/*
        	String submissionQuery = "UPDATE Math.submission "
									+"SET competitor_ID = "+ competitor_ID + ", Question_ID =" + questionNumber + ", AtTime = '" + (new java.util.Date() ) + "', Selected_Choice_ID = " + Integer.parseInt( usersChoiceID ) + " "
									+"WHERE competitor_ID = "+ competitor_ID + " AND Question_ID = " + questionNumber + "; ";
*/

        	// ---------------------------------------------------------


       //     String questionNumberString = null ; 
        /*    if ( username != null )
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
                

               // if ( resultSet.next() )
                 //   storedPassword  =  (String) resultSet.getObject("Password") ;     

            } // end if
            */

         //   boolean  userExists      =   (storedPassword != null) ;
         //   boolean  passwordsMatch  =   ( (password != null) && (storedPassword != null) && (password. equals (storedPassword)) ) ;
          

        	String getUserQuery = "SELECT * FROM MATH.Competitor WHERE username = lower('" + username + "') ";
        	ResultSet usernameRS = statement.executeQuery ( getUserQuery ) ;
        	
        	// find if the username is available
        	// changed this from if to while
        	while (usernameRS.next() )
        	{
        		if (usernameRS.getObject("username") == null )
        		{
        			userIsAvailable = true; 	// set the available to true if the result set can't find the username
        		}
        	}

            
            if ( session.isNew() )
            {

                // if the user exists and the password matches, display the first webpage
               if ( userIsAvailable == true  ) // if the userIs available, update the competitor table 
                {

                	// insert the new user into the competitor table 
				//	String insertUserQuery = 	"INSERT into Math.Competitor ( Username, Score ) "
											+	"VALUES  ( '" + username + "', 0 )" ;

				//	int insertUser = statement.executeUpdate( insertUserQuery ); // execute the query



                    session.setAttribute ( userIdAttribute, username ) ;
                    userLoggedIn = true; 

               /*     // set score back to zero when there is new session
                    String setScoreQuery =  "UPDATE Math.competitor "
				 					+ "SET score = 0 "
				 					+ "WHERE lower(Username) = lower('" + username + "') " ; 
				 	int scoreZero = statement.executeUpdate (setScoreQuery);
				*/

                }
                //  if the username and password don't match then display no user is logged in page ---------------------------------------------------------
                else    
                { 
                    session.invalidate(); 
                    out.println ( "    <form action='LogOut' method='POST'>" ) ;
                    out.print  (  ""
                        +    "      <table>\n"
                        +    "   <tr style='font-size:x-large;'> "
                        +    "        <tr style='font-size:x-large;'><td>No user is currently logged in.</td></tr>\n"
                        +    "        <tr><td><button type='submit' class='inline_wide' formaction='index.html'>Log In</button></td></tr>\n"
                        +    "      </table>\n"
                        +    "    </form>\n"
                        +    "    <hr>\n"
                        +    "  </body>\n"
                        +    "</html>\n"  ) ;
                    out.close() ;
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
                + "FROM   Math.Question "
                + "WHERE  ID =" + questionNumber + " ";

                ResultSet questionRS = statement.executeQuery ( questionString ) ;
                String questionText = "";
                // display the question text 
                if (questionRS.next() )
                    questionText = (String) questionRS.getObject("QuestionText") ;

                // gets the answer text from data-------------------------------------------------------
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
                    +    "      <br><br> \n " 
                    +   "      <table>\n"
                    +   " <tr>     "
                    +       "     <td> &nbsp;&ensp;&ensp;" + questionText + " = </td>      "
                    +       "   </tr>      "
                    +       "</table>     "
                    +       "<br>     " ); 

        
                // print each answer with a radio button for user selection, assign the value to the id of the answer
                
                out.println ( "    <form action='CheckAnswer' method='POST'>" ) ;
                
                while (questionAnswers.next() )
                {
                    out.println ( " <input type=\"radio\" name=\"choice\" value="+ questionAnswers.getObject("id") + " >&nbsp;&ensp;&ensp;" + questionAnswers.getObject("choicetext") + "<br><br> \n " );
                }

                out.println(""  
                    + " <table style='float:right'> "
                    + " <tr>  "
                    + "   <td><input type='hidden' name= 'username' value='" + username + "'> </td>"
                    + "   <td><input type='hidden' name= 'questionNumber' value='" + questionNumber + "'> </td> "
                    + "   <td><input type='submit' value='Click This Button to Check Your Answer'></td> "
                        //  + "   <td><input type='submit' value='Submit'></td>"
                    + "  </tr>"
                    + " </table>"
                    +   "   <br> " );

                out.println ( " </form> ");

            } // end if the userLoggedIn
            
      
            out.print ( ""        
                +   "    <form method='POST'>\n"
                +   "   <table> "
                + "     <tr> "
                + "     <td> "
                + "     <button type='submit' class='inline_wide' formaction='LogOut' name='username' value='" + username + "'>Log Out</button> "
                + "     </td>"
                + "     </tr>\n" ) ;

            out.print  (  "     </table>\n"
                +    "    </form>\n"
                +    "    <hr>\n" ) ;

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
