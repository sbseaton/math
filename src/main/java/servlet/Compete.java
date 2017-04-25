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
        String currentQuestionNumberString = request.getParameter( "Q_ID" );

        // find if this is the first question or not ---------------------------------------
        if ( currentQuestionNumberString != null )
            questionNumber = Integer.parseInt(currentQuestionNumberString);
        else
            questionNumber = -1;
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
            out.println  ( 	""   
            			+	"<!DOCTYPE html>\n"
						+	"<html lang='en'>\n"
  						+	"<head>\n"
    					+	"<meta charset='utf-8'>\n"
    					+	"<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
    					+	"<meta name='viewport' content='width=device-width, initial-scale=1'>\n"
    					+	"<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->\n"
    					+	"<title>FHSU 2017 Mental Math</title>\n"
    					+	"<!-- ======================= jQuery (necessary for Bootstrap's JavaScript plugins) ======================= -->\n"
    					+	"<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js'></script>\n"
    					+	"<!-- Bootstrap -->\n"
    					+	"<!-- Latest compiled and minified CSS -->\n"
    					+	"<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css' \n"
    					+	"      integrity='sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u' crossorigin='anonymous'>\n"
    					+	"<!-- Optional theme --> \n"
    					+	"<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css' \n"
    					+	"      integrity='sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp' crossorigin='anonymous'> \n"
    					+	"<!-- Latest compiled and minified JavaScript --> \n"
    					+	"<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js' \n"
    					+	"        integrity='sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa' crossorigin='anonymous'></script> \n"
    					+	"<script type='text/javascript' async \n"
    					+	"  src='https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-MML-AM_CHTML'> \n"
    					+	"</script> \n"
    				
    					+	"<!-- styles below to *FOLLOW* Bootstrap's CSS (which is above) -->\n"
					
    					+	"<style>\n"
    					+	"  .dropdown:hover .dropdown-menu { display: block; }\n"
    					+	"  .equal { display: flex; display: -webkit-flex; }\n"
    					+	"  .tbodyScoreboard { display:block; height:300px; overflow:auto; }\n"
    					+	"  .theadScoreboard .tbodyScoreboard .trScoreboard { display:table; width:100%; table-layout:fixed; }\n"
    					+	"  .theadScoreboard { width: calc( 100% - 1em ) }\n"
    					+	"  .not-active { pointer-events: none; cursor: default; }\n"
    					+	"</style>\n"

    					+	"<script>\n"
    					+	"  $(document).on('change','input[type=radio]', function( event )\n"
    					+	"    { \n"
    					+	"    if ( $(event.target).hasClass('correct') )\n"
    					+	"      $('#feedbackHeader').text( 'Correct!' ) ;\n"
    					+	"    else if ( $(event.target).hasClass('incorrect') )\n"
    					+	"      $('#feedbackHeader').text( 'Sorry, incorrect.' ) ;\n"
    					+	"    $('#feedbackModal').modal ( 'show' ) ;\n"
    					+	"    setTimeout ( function() { $('#feedbackModal').modal('hide'); }, 2000 ) ;\n"
    					+	"    setTimeout ( function() { $(event.target).closest('form').submit() }, 2500 ) ;\n"
    					+	"    } ) ;\n"
    					+	"  $(document).on('click','#updateScoreboardButton', function( ) {$('#updateScoreboardButton').closest('form').submit();} ) ;\n"
    					+	"</script>\n"
						+	"</head>\n"
						+	"<body>\n" );

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
		/*			String insertUserQuery = 	"INSERT into Math.Competitor ( Username, Score ) "
											+	"VALUES  ( '" + username + "', 0 )" ;

					int insertUser = statement.executeUpdate( insertUserQuery ); // execute the query

		*/

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
              /*  
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

                while (questionAnswers.next() )
                {
                    out.println ( " <input type=\"radio\" name=\"choice\" value="+ questionAnswers.getObject("id") + " >&nbsp;&ensp;&ensp;" + questionAnswers.getObject("choicetext") + "<br><br> \n " );
                }

                */

               out.println  ( "" 
                +	"<div class='container'>\n"
              /*  +	"<div class='modal fade' id='feedbackModal' role='dialog'>\n"
                +	"<div class='modal-dialog modal-sm'> \n"
                +	"<div class='modal-content'> \n"
                +	"<div class='modal-header'> \n"
                +	"<button type='button' class='close' data-dismiss='modal'>&times;</button> \n"
                +	"<h4 class='modal-title' id='feedbackHeader'>TO BE SET BY SCRIPT</h4> \n"
                +	"</div> \n"
                +	"</div> \n"
                +	" </div> \n"
                +	"</div> \n"
                +	"</div> \n"
                +	"<!-- Begin outermost container --> \n"
                +	"<div class='container'> \n"
               */ 
                +	"<nav class='navbar navbar-default'> \n"
                +	"<div class='container-fluid'> \n"
             //   +	"<div id='navbar' c \n"
                +	" <!-- Begin static navbalass='navbar-collapse collapse' --> \n"
                +	"<ul class='nav navbar-nav'> \n"
                +	"<li class='dropdown'> \n"
                +	"<a href='#' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>Easy <span class='caret'></span></a> \n"
                +	"<ul class='dropdown-menu'> \n" );

			// easy questions ----------------------------------------------------------------------------------
               	String questionStringEasy = "SELECT * "
                + "FROM   Math.Question WHERE PointValue = 2 ";

                ResultSet questionEasyRS = statement.executeQuery ( questionStringEasy ) ;
                String questionTextEasy = "";
                String questionIDEasy = "";
                // display the question text 
                while (questionEasyRS.next() )
                {
                    questionTextEasy = (String) questionEasyRS.getObject("QuestionText") ;
                    questionIDEasy = "" + questionEasyRS.getObject("ID");
                    out.println ( " <li><a data-target='/Compete' href='/Compete?Q_ID="+ questionIDEasy +"'> " + questionTextEasy + " </a></li> \n" );
                }

            // ------------------------------------------------------------------------------------------------ 

                out.println	( ""
               //  +	"<li class='bg-danger disabled'><a  class='not-active' data-target='/Compete' href='/Compete?Q_ID=101'> HERE IS A QUESTION </a></li> \n"

                +	"<li role='separator' class='divider'></li> \n"
                +	"<li class='dropdown-header disabled'>Legend</li> \n"
                +	"<li class='bg-success disabled'><a href='#'>Green = Answered Correctly</a></li> \n"
                +	"<li class='bg-danger disabled'><a href='#'>Red = Answered Incorrectly</a></li> \n"
                +	"</ul> \n"
                +	" </li> \n"
                +	"<li class='dropdown'> \n"
                +	" <a href='#' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>Medium <span class='caret'></span></a> \n"
                +	" <ul class='dropdown-menu'> \n" ) ;

                // medium questions ---------------------------------------------------------------------------------------------

                String questionStringMedium = "SELECT * "
                + "FROM   Math.Question WHERE PointValue = 3 ";

                ResultSet questionMediumRS = statement.executeQuery ( questionStringMedium ) ;
                String questionTextMedium = "";
                String questionIDMedium = "";
                // display the question text 
                while (questionMediumRS.next() )
                {
                    questionTextMedium = (String) questionMediumRS.getObject("QuestionText") ;
                    questionIDMedium = "" + questionMediumRS.getObject("ID");
                    out.println ( " <li><a data-target='/Compete' href='/Compete?Q_ID="+ questionIDMedium +"'> " + questionTextMedium + " </a></li> \n" );
                }

            // ------------------------------------------------------------------------------------------------ 

            out.println( ""
             //    +	" <li><a data-target='/Compete' href='/Compete?Q_ID=111'> HERE IS A QUESTION </a></li> \n"

                +	"<li role='separator' class='divider'></li> \n"
                +	"<li class='dropdown-header disabled'>Legend</li> \n"
                +	"<li class='bg-success disabled'><a href='#'>Green = Answered Correctly</a></li> \n"
                +	" <li class='bg-danger disabled'><a href='#'>Red = Answered Incorrectly</a></li> \n"
                +	"</ul> \n"
                +	" </li> \n"
                +	" <li class='dropdown'> \n"
                +	" <a href='#' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>Difficult <span class='caret'></span></a> \n"
                +	"<ul class='dropdown-menu'> \n" ) ; 


             // Hard questions ---------------------------------------------------------------------------------------------

                String questionStringHard = "SELECT * "
                + "FROM   Math.Question WHERE PointValue = 4 ";

                ResultSet questionHardRS = statement.executeQuery ( questionStringHard ) ;
                String questionTextHard = "";
                String questionIDHard= "";
                // display the question text 
                while (questionHardRS.next() )
                {
                    questionTextHard = (String) questionHardRS.getObject("QuestionText") ;
                    questionIDHard = "" + questionHardRS.getObject("ID");
                    out.println ( " <li><a data-target='/Compete' href='/Compete?Q_ID="+ questionIDHard +"'> " + questionTextHard + " </a></li> \n" );
                }

            // ------------------------------------------------------------------------------------------------ 


             //   +	"<li><a data-target='/Compete' href='/Compete?Q_ID=121'>HERE IS A QUESTION</a></li> \n"

            out.println( ""
                +	"<li role='separator' class='divider'></li> \n"
                +	"<li class='dropdown-header disabled'>Legend</li> \n"
                +	"<li class='bg-success disabled'><a href='#'>Green = Answered Correctly</a></li> \n"
                +	"<li class='bg-danger disabled'><a href='#'>Red = Answered Incorrectly</a></li> \n"
                +	"</ul> \n"
                +	"</li> \n"
                +	"</ul> \n"
                +	"<form method='POST' action='/Compete' class='navbar-form navbar-right'> \n"
                +	"<!-- Button trigger modal --> \n"
                +	"<button id='updateScoreboardButton' type='button' class='btn btn-primary'> \n"
                +	" Update Scoreboard \n"
                +	"</button> \n"
                +	"</form> \n"
               //  +	"</div><!--/.nav-collapse --> \n"
                +	"</div><!--/.container-fluid --> \n"
                +	"</nav> \n"
                +	"<!-- End static navbar --> \n"
                +	"<!-- Begin container of two jumbotrons --> \n"
                +	"<div class='container-fluid'> \n"
                +	" <div class='row equal'> \n"
                +	"<div class='jumbotron col-md-8' style='margin:20px'> \n" );


            if ( questionNumber == -1 )
            {
            	out.println( ""
                +	"<h2 class='display-3'>Welcome to Sam's Mental Math Competition!</h2> \n"
                +	"<hr class='my-4'> \n"
                +	"<p class='lead'>Neither calculator nor scratchpaper are permitted.  You get one guess for each question.</p> \n"
                +	"<hr class='my-4'> \n"
                +	"<p>To begin, select a question from a drop-down menu above -- Easy, Medium, or Difficult.</p> \n"
                +	"<hr class='my-4'> \n" );
            }

            else 
            {	

            	String questionQuery = "Select * from math.question where ID =" + questionNumber + " " ;

            	ResultSet questionRS = statement.executeQuery ( questionQuery );

            	while (questionRS.next() )
            	{
            		out.println ("<p>" + questionRS.getObject("QuestionText") + "</p>\n");
            	} 


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

                while (questionAnswers.next() )
                {
                    out.println ( " <input type=\"radio\" name=\"choice\" value="+ questionAnswers.getObject("id") + " >&nbsp;&ensp;&ensp;" + questionAnswers.getObject("choicetext") + "<br><br> \n " );
                }

          
            }

            out.println( ""
                +	"</div> \n"
                +	"<div class='jumbotron col-md-4' style='margin:20px'> \n"
                +	"<div class='panel panel-default'> \n"
                +	"<div class='panel-heading'> \n"
                +	"<h4> Scoreboard </h4> \n"
                +	"</div> \n"
                +	"<table class='table tbodyScoreboard theadScoreboard trScoreboard'> \n"
                +	"<thead> \n"
                +	"<tr><th class='col-lg-8'>Competitor</th><th class='col-lg-4'>Points</th></tr> \n"
                +	"</thead> \n"
                +	"<tbody> \n"
                +	"<tr><td class='col-lg-8'>THIS IS WHERE USERNAME ENTRIES GO</td></tr>      </tbody> \n"
                +	"</table> \n"
                +	"</div> \n"
                +	"</div> \n"
                +	"</div> \n"
                +	"</div> \n"
                +	"<!-- End container of two jumbotrons --> \n"
                +	"</div> <!-- /container --> \n"
                +	" <!-- End outermost container -->\n " );



			}	// end if userloggedin is true



            

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
