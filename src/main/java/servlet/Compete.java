// ------------------------------------------------------------------------------------ //
// Compete.java    by Samuel Seaton    14 OCT 2016

// This is the Compete servlet ...

/* 
here is how you display the answer text : 

Select * from Math.Question, Math.Submission WHERE question_id = id AND competitor_id = 3 AND question_id = 301 ;

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
        String      userChoice = null;

        String username = request.getParameter("username");
        int competitor_ID = -1; // declare competitor_ID


        int questionNumber;     // declare question number
        String currentQuestionNumberString = request.getParameter( "Q_ID" );

         // find the current question ID  ---------------------------------------
            if ( currentQuestionNumberString != null )
                questionNumber = Integer.parseInt(currentQuestionNumberString);
            else
                questionNumber = -1;    // welcome page
        // ----------------------------------------------------------------------------------

        
        

        try
        { 
            connection = DriverManager.getConnection ( DB_URL ) ;
            statement = connection.createStatement() ;

            // find if the last choice submission is correct or not ---------------------------------------------------------------

            String isPrevChoiceCorrectString = request.getParameter("isCorrect");
            boolean isPrevChoiceCorrect = false;
            if ( isPrevChoiceCorrectString != null )
                isPrevChoiceCorrect = Boolean.parseBoolean( isPrevChoiceCorrectString );

            // ----------------------------------------------------------------------------------------------------------------------


            // request the point value for the question
            String previousQuestionPointValueString = request.getParameter("pointValue");
            int previousQuestionPointValue = 0;

            if (previousQuestionPointValueString != null)
                previousQuestionPointValue = Integer.parseInt(previousQuestionPointValueString);
            
                
            String questionAnswersString = null ;   // question answer string

            boolean userLoggedIn = false;


        // incrementing score-----------------------------------------------------------------------------------------
            if (isPrevChoiceCorrect == true)
            {   
                 String scoreIncrementQuery =  "UPDATE Math.competitor "
                                          + "SET score = score + " + previousQuestionPointValue + " "   // increment score by the point value
                                          + "WHERE lower(Username) = lower('" + username + "') " ;
                //out.println("<h3> " + username + "</h3>"); 
                //out.println("<h3> " + previousQuestionPointValue + "</h3>"); 
                statement.executeUpdate (scoreIncrementQuery);
            }

            // end incrementing score------------------------------------------------------------------------------------------


            // html display ---------------------------------------------------------------
            out.println  (  ""   
                        +   "<!DOCTYPE html>\n"
                        +   "<html lang='en'>\n"
                        +   "<head>\n"
                        +   "<meta charset='utf-8'>\n"
                        +   "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
                        +   "<meta name='viewport' content='width=device-width, initial-scale=1'>\n"
                        +   "<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->\n"
                        +   "<title>FHSU 2017 Mental Math</title>\n"
                        +   "<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js'></script>\n"
                        +   "<!-- Bootstrap -->\n"
                        +   "<!-- Latest compiled and minified CSS -->\n"
                        +   "<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css' \n"
                        +   "      integrity='sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u' crossorigin='anonymous'>\n"
                        +   "<!-- Optional theme --> \n"
                        +   "<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css' \n"
                        +   "      integrity='sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp' crossorigin='anonymous'> \n"
                        +   "<!-- Latest compiled and minified JavaScript --> \n"
                        +   "<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js' \n"
                        +   "        integrity='sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa' crossorigin='anonymous'></script> \n"
                        +   "<script type='text/javascript' async \n"
                        +   "  src='https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-MML-AM_CHTML'> \n"
                        +   "</script> \n"
                    
                        +   "<!-- styles below to *FOLLOW* Bootstrap's CSS (which is above) -->\n"
                    
                        +   "<style>\n"
                        +   "  .dropdown:hover .dropdown-menu { display: block; }\n"
                        +   "  .equal { display: flex; display: -webkit-flex; }\n"
                        +   "  .tbodyScoreboard { display:block; height:300px; overflow:auto; }\n"
                        +   "  .theadScoreboard .tbodyScoreboard .trScoreboard { display:table; width:100%; table-layout:fixed; }\n"
                        +   "  .theadScoreboard { width: calc( 100% - 1em ) }\n"
                        +   "  .not-active { pointer-events: none; cursor: default; }\n"
                        +   "</style>\n"

                        +   "<script>\n"
                        +   "  $(document).on('change','input[type=radio]', function( event )\n"
                        +   "    { \n"
                        +   "    if ( $(event.target).hasClass('correct') )\n"
                        +   "      $('#feedbackHeader').text( 'Correct!' ) ;\n"
                        +   "    else if ( $(event.target).hasClass('incorrect') )\n"
                        +   "      $('#feedbackHeader').text( 'Sorry, incorrect.' ) ;\n"
                        +   "    $('#feedbackModal').modal ( 'show' ) ;\n"
                        +   "    setTimeout ( function() { $('#feedbackModal').modal('hide'); }, 2000 ) ;\n"
                        +   "    setTimeout ( function() { $(event.target).closest('form').submit() }, 2500 ) ;\n"
                        +   "    } ) ;\n"
                        +   "  $(document).on('click','#updateScoreboardButton', function( ) {$('#updateScoreboardButton').closest('form').submit();} ) ;\n"
                        +   "</script>\n"
                        +   "</head>\n"
                        +   "<body>\n" );



            // ---------------------------------------------------------

            boolean userIsAvailable = false ;               // change this back to false

/*
            String submissionQuery = "UPDATE Math.submission "
                                    +"SET competitor_ID = "+ competitor_ID + ", Question_ID =" + questionNumber + ", AtTime = '" + (new java.util.Date() ) + "', Selected_Choice_ID = " + Integer.parseInt( usersChoiceID ) + " "
                                    +"WHERE competitor_ID = "+ competitor_ID + " AND Question_ID = " + questionNumber + "; ";
*/

            // ---------------------------------------------------------


   

            String getUserQuery = "SELECT * FROM MATH.Competitor WHERE username = lower('" + username + "') ";
            ResultSet usernameRS = statement.executeQuery ( getUserQuery ) ;
            // find if the username is available
            // changed this from if to while
            if ( ! usernameRS.next() )
            {
                userIsAvailable = true;     // set the available to true if the result set can't find the username
            
            }

            
            if ( session.isNew() )
            {

                // if the user exists and the password matches, display the first webpage
               if ( userIsAvailable == true  ) // if the userIs available, update the competitor table 
                {
                    session.setAttribute ( userIdAttribute, username ) ;
                    // insert the new user into the competitor table 
                    String insertUserQuery =    "INSERT into Math.Competitor ( Username, Score ) "
                                            +   "VALUES  ( '" + username + "', 0 )" ;
                    int insertUser = statement.executeUpdate( insertUserQuery ); // execute the query


                    userLoggedIn = true; // set logged in = true

                // ----------------- Insert the submission into the submission table -------------------------------


                     // get the question id of the previous problem
                    String previousQuestionNumberString = request.getParameter("previousQuestionNumber");   // request previous user's question they answered 
                    int previousQuestionNumber = -1;
                    if (previousQuestionNumberString != null)
                        previousQuestionNumber = Integer.parseInt(previousQuestionNumberString);    // convert the previous question number to an int



                    // request the previous answer ID for submission query
                    String previousAnswerIDString = request.getParameter("Selected_Choice_ID"); 
                    int previousAnswerID = -1;
                    if (previousAnswerIDString != null )
                        previousAnswerID = Integer.parseInt(previousAnswerIDString);


                   
                    // Get the competitor ID for the insert query for submission
                    String competitor_IDQuery = "SELECT * FROM Math.Competitor WHERE lower( Username ) = lower('" + username + "') ";
                    ResultSet competitor_IDRS = statement.executeQuery (competitor_IDQuery );

                   // out.println("<h1>" + competitor_IDRS.next() + "</h1> " );

                    if ( competitor_IDRS.next() )
                        competitor_ID = Integer.parseInt ("" + competitor_IDRS.getObject("ID"));


                    if (previousAnswerIDString != null && previousAnswerIDString != null )
                    {
                        // query the submission made previously
                        String submissionInsertQuery = "INSERT INTO Math.Submission ( Competitor_ID, Question_ID, AtTime, Selected_Choice_ID ) " 
                                                + "VALUES ( " + competitor_ID + " , " + previousQuestionNumber + " , '" + (new java.util.Date() ) + "' , " + previousAnswerID + " ) " ;

                        int submissionInsert = statement.executeUpdate( submissionInsertQuery );
                    }
                    

                }
            
                else
                //  if the username and password don't match then display no user is logged in page -----------------
                 { 
                    userLoggedIn = false;
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


                // get the question id of the previous problem
                String previousQuestionNumberString = request.getParameter("previousQuestionNumber");   // request previous user's question they answered 
                int previousQuestionNumber = -1;
                if (previousQuestionNumberString != null)
                    previousQuestionNumber = Integer.parseInt(previousQuestionNumberString);    // convert the previous question number to an int



                // request the previous answer ID for submission query
                String previousAnswerIDString = request.getParameter("Selected_Choice_ID"); 
                int previousAnswerID = -1;
                if (previousAnswerIDString != null )
                    previousAnswerID = Integer.parseInt(previousAnswerIDString);


                // Get the competitor ID for the insert query for submission
                String competitor_IDQuery = "SELECT * FROM Math.Competitor WHERE lower( Username ) = lower('" + username + "') ";
                ResultSet competitor_IDRS = statement.executeQuery (competitor_IDQuery );

               // out.println("<h1>" + competitor_IDRS.next() + "</h1> " );

                if ( competitor_IDRS.next() )
                    competitor_ID = Integer.parseInt ("" + competitor_IDRS.getObject("ID"));


                if (previousQuestionNumberString != null && previousAnswerIDString != null )
                {
                    // INSERT the submission made previously
                    String submissionQuery = "INSERT INTO Math.Submission ( Competitor_ID, Question_ID, AtTime, Selected_Choice_ID ) " 
                                            + "VALUES ( " + competitor_ID + " , " + previousQuestionNumber + " , '" + (new java.util.Date() ) + "' , " + previousAnswerID + " ) " ;

                    int submission = statement.executeUpdate( submissionQuery );
                }
                
            }   // end else

            // query for question 1 ---------------------------------------------------------------------------------------------------------------------
            if (userLoggedIn == true )
            {

               out.println  ( "" 
                +   "<div class='container'>\n"
                +   "<div class='modal fade' id='feedbackModal' role='dialog'>\n"
                +   "<div class='modal-dialog modal-sm'> \n"
                +   "<div class='modal-content'> \n"
                +   "<div class='modal-header'> \n"
                +   "<button type='button' class='close' data-dismiss='modal'>&times;</button> \n"
                +   "<h4 class='modal-title' id='feedbackHeader'>TO BE SET BY SCRIPT</h4> \n"
                +   "</div> \n"
                +   "</div> \n"
                +   " </div> \n"
                +   "</div> \n"
                +   "</div> \n"
                +   "<!-- Begin outermost container --> \n"
                +   "<div class='container'> \n"
               
                +   "<nav class='navbar navbar-default'> \n"
                +   "<div class='container-fluid'> \n"
             //   + "<div id='navbar' c \n"
                +   " <!-- Begin static navbalass='navbar-collapse collapse' --> \n"
                +   "<ul class='nav navbar-nav'> \n"
                +   "<li class='dropdown'> \n"
                +   "<a href='#' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>Easy <span class='caret'></span></a> \n"
                +   "<ul class='dropdown-menu'> \n" );

               // easy questions ----------------------------------------------------------------------------------

        // --------------------------------------------------------------------------------------------------------------------

            ArrayList<Boolean> questionsAnswered = new ArrayList<Boolean>();// holds which quesiton currentCompetitor has answered and if correct or not
            // fill list with null values
            for (int i = 0; i < 40 ; i++ ) {
                questionsAnswered.add(null);
            }// end for

            String getAnswersQuery = "SELECT * FROM Math.Question LEFT JOIN Math.Submission ON (Question.ID = Submission.Question_ID) WHERE competitor_ID = " + competitor_ID + " ";
           
            ResultSet getAnswersRS = statement.executeQuery(getAnswersQuery);

            while ( getAnswersRS.next() )
            {
                int tempQuestionID  = Integer.parseInt("" + getAnswersRS.getObject("Question_ID") );
                int correctAnswerID = Integer.parseInt("" + getAnswersRS.getObject("CorrectAnswer_Choice_ID"));
                int usersAnswerID   = Integer.parseInt("" + getAnswersRS.getObject("Selected_Choice_ID") );

                questionsAnswered.remove(tempQuestionID);
                if ( usersAnswerID == correctAnswerID )
                {
                    questionsAnswered.add(tempQuestionID, true);
                } else {
                    questionsAnswered.add(tempQuestionID, false);
                }// end if else

            }// end while

            //--------------------------------------------------------//

            String questionStringEasy = "SELECT * FROM Math.Question WHERE PointValue = 2";
            ResultSet questionEasyRS = statement.executeQuery ( questionStringEasy ) ;

            //output EASY questions to dropdown menu
            while ( questionEasyRS.next() ) 
            {

            	String questionTextEasy = (String) questionEasyRS.getObject("QuestionText") ;   
                int    questionIDEasy   = Integer.parseInt ( "" + questionEasyRS.getObject("id") ) ;

                Boolean isCorrect = questionsAnswered.get( Integer.parseInt( "" + questionEasyRS.getObject("ID") ) ) ;

                if (isCorrect == null)
                    out.println ( " <li><a data-target='/Compete' href='/Compete?Q_ID="+ questionIDEasy +"'> " + questionTextEasy + " </a></li> \n" );

                else if (isCorrect) 
                	out.println ( " <li class='bg-success disabled'><a class='not-active'> " + questionTextEasy + " </a></li> \n" );
                else
               		out.println ( " <li class='bg-danger disabled'><a class='not-active'> " + questionTextEasy + " </a></li> \n" );
                  	// end inner if else
                // end outer if-else
            }
    // --------------------------------------------------------------------------------------------------------------------


            //  end easy questions------------------------------------------------------------------------------------------------ 

                out.println ( ""
               //  +    "<li class='bg-danger disabled'><a  class='not-active' data-target='/Compete' href='/Compete?Q_ID=101'> HERE IS A QUESTION </a></li> \n"

                +   "<li role='separator' class='divider'></li> \n"
                +   "<li class='dropdown-header disabled'>Legend</li> \n"
                +   "<li class='bg-success disabled'><a href='#'>Green = Answered Correctly</a></li> \n"
                +   "<li class='bg-danger disabled'><a href='#'>Red = Answered Incorrectly</a></li> \n"
                +   "</ul> \n"
                +   " </li> \n"
                +   "<li class='dropdown'> \n"
                +   " <a href='#' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>Medium <span class='caret'></span></a> \n"
                +   " <ul class='dropdown-menu'> \n" ) ;

                // medium questions ---------------------------------------------------------------------------------------------
                 //--------------------------------------------------------//

            String questionStringMedium = "SELECT * FROM Math.Question WHERE PointValue = 3";
            ResultSet questionMediumRS = statement.executeQuery ( questionStringMedium ) ;

            //output medium questions to dropdown menu
            while ( questionMediumRS.next() ) 
            {

                String liClasses = "";
                Boolean isCorrect = questionsAnswered.get( Integer.parseInt( "" + questionMediumRS.getObject("ID") ) ) ;

                if (isCorrect == null)
                    liClasses = "";
                else if (isCorrect) 
                    liClasses = "bg-success disabled";
                else
                    liClasses = "bg-danger disabled";
                  	// end inner if else
                // end outer if-else
    
                String questionTextMedium = (String) questionMediumRS.getObject("QuestionText") ;   
                int    questionIDMedium   = Integer.parseInt ( "" + questionMediumRS.getObject("id") ) ;
                out.println ( " <li class='" + liClasses + "'><a data-target='/Compete' href='/Compete?Q_ID="+ questionIDMedium +"'> " + questionTextMedium + " </a></li> \n" );
            
 
                }
    // --------------------------------------------------------------------------------------------------------------------

            // ------------------------------------------------------------------------------------------------ 

            out.println( ""
             //    +    " <li><a data-target='/Compete' href='/Compete?Q_ID=111'> HERE IS A QUESTION </a></li> \n"

                +   "<li role='separator' class='divider'></li> \n"
                +   "<li class='dropdown-header disabled'>Legend</li> \n"
                +   "<li class='bg-success disabled'><a href='#'>Green = Answered Correctly</a></li> \n"
                +   " <li class='bg-danger disabled'><a href='#'>Red = Answered Incorrectly</a></li> \n"
                +   "</ul> \n"
                +   " </li> \n"
                +   " <li class='dropdown'> \n"
                +   " <a href='#' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>Difficult <span class='caret'></span></a> \n"
                +   "<ul class='dropdown-menu'> \n" ) ; 


             // Hard questions ---------------------------------------------------------------------------------------------
             // pull from database and display --------------------------------------
                  //--------------------------------------------------------//

	            String questionStringHard = "SELECT * FROM Math.Question WHERE PointValue = 4";
	            ResultSet questionHardRS = statement.executeQuery ( questionStringHard ) ;

	            //output EASY questions to dropdown menu
	            while ( questionHardRS.next() ) 
	            {

	                String liClasses = "";
	                Boolean isCorrect = questionsAnswered.get( Integer.parseInt( "" + questionHardRS.getObject("ID") ) ) ;

	                if (isCorrect == null)
	                    liClasses = "";
	                else if (isCorrect) 
	                    liClasses = "bg-success disabled";
	                else
	                    liClasses = "bg-danger disabled";
	                  	// end inner if else
	                // end outer if-else
	    
	                String questionTextHard = (String) questionHardRS.getObject("QuestionText") ;   
	                int    questionIDHard   = Integer.parseInt ( "" + questionHardRS.getObject("id") ) ;
	                out.println ( " <li class='" + liClasses + "'><a data-target='/Compete' href='/Compete?Q_ID="+ questionIDHard +"'> " + questionTextHard + " </a></li> \n" );
	            
	            }
    // --------------------------------------------------------------------------------------------------------------------

            // ------------------------------------------------------------------------------------------------ 


             //   + "<li><a data-target='/Compete' href='/Compete?Q_ID=121'>HERE IS A QUESTION</a></li> \n"

            out.println( ""
                +   "<li role='separator' class='divider'></li> \n"
                +   "<li class='dropdown-header disabled'>Legend</li> \n"
                +   "<li class='bg-success disabled'><a href='#'>Green = Answered Correctly</a></li> \n"
                +   "<li class='bg-danger disabled'><a href='#'>Red = Answered Incorrectly</a></li> \n"
                +   "</ul> \n"
                +   "</li> \n"
                +   "</ul> \n"
                +   "<form method='POST' action='/Compete' class='navbar-form navbar-right'> \n"
                +   "<!-- Button trigger modal --> \n"
                +   "<button id='updateScoreboardButton' type='button' class='btn btn-primary'> \n"
                +   " Update Scoreboard \n"
                +   "</button> \n"
                +   "</form> \n"
               //  +    "</div><!--/.nav-collapse --> \n"
                +   "</div><!--/.container-fluid --> \n"
                +   "</nav> \n"
                +   "<!-- End static navbar --> \n"
                +   "<!-- Begin container of two jumbotrons --> \n"
                +   "<div class='container-fluid'> \n"
                +   " <div class='row equal'> \n"
                +   "<div class='jumbotron col-md-8' style='margin:20px'> \n" );

         	
         	String numOfQuestionsCompletedQuery = " Select Count(question_id) as numOfQuestionsCompleted FROM math.submission where competitor_id =" + competitor_ID + " ";
            ResultSet numOfQuestionsCompletedRS = statement.executeQuery(numOfQuestionsCompletedQuery) ;

            int numOfQuestionsCompleted = 0 ;
			


			if (numOfQuestionsCompletedRS.next() )
				numOfQuestionsCompleted = Integer.parseInt ("" + numOfQuestionsCompletedRS.getObject("numOfQuestionsCompleted"));

			// out.println("<h2>number of questions completed: " + numOfQuestionsCompleted + "</h2>");
			

        	if ( questionNumber == -1 )
            {
                out.println( ""
                +   "<h2 class='display-3'>Welcome to Sam's Mental Math Competition!</h2> \n"
                +   "<hr class='my-4'> \n"
                +   "<p class='lead'>Neither calculator nor scratchpaper are permitted.  You get one guess for each question.</p> \n"
                +   "<hr class='my-4'> \n"
                +   "<p>To begin, select a question from a drop-down menu above -- Easy, Medium, or Difficult.</p> \n"
                +   "<hr class='my-4'> \n" );
            }

            // if all questions have been answered, display final page
            else if ( numOfQuestionsCompleted == 30 )
            {
            	out.println( ""
                +   "<h2 class='display-3'>There are no more questions available.</h2> \n"
                +   "<hr class='my-4'> \n"
                +   "<p class='lead'>Thank you for participating.</p> \n"
                +   "<hr class='my-4'> \n" );
            }
           
            else 
            {   

                String questionQuery = "Select * from math.question where ID =" + questionNumber + " " ;
                ResultSet questionRS = statement.executeQuery ( questionQuery );

                int pointValue = 0; // declare point value for the current question
                int correctChoiceID = -1;
                while (questionRS.next() )
                {
                    out.println ("<h3>" + questionRS.getObject("QuestionText") + "</h3>\n");
                    correctChoiceID = Integer.parseInt( "" + questionRS.getObject("correctanswer_choice_id"));
                    pointValue = Integer.parseInt( "" + questionRS.getObject("PointValue")); // find the value of the question

                } // end while

                //  find the next question available--------------------------------------------------------------------------
                

				// ------------------finds the next available question ------------------------------------------------

					int nextQuestionAvailable = questionNumber + 1;
	                while ( true ) 
	                {
	                	if (nextQuestionAvailable > 30)
                  			nextQuestionAvailable = 1;

	                   	String nextQuestionAvailableString = "Select * from math.submission where competitor_ID =" + competitor_ID + " AND question_id =" + nextQuestionAvailable ;
	                	ResultSet nextQuestionAvailableRS = statement.executeQuery (nextQuestionAvailableString);
	                	if (! nextQuestionAvailableRS.next())
	                		break;

	                	nextQuestionAvailable++;

	               	}// end while
	          
                // --------------------------------------------------------------------------------------------------------


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

                out.println ( "<table class='table table-bordered table-hover' > " );

                // declare array list to hold all of the answers -----------------------------------------
                ArrayList<String> questionChoices = new ArrayList<String>();
	           
                while (questionAnswers.next() )
                {
                    String currentChoiceHTML = "";


                    currentChoiceHTML +=           "<tr>";
                    currentChoiceHTML +=           "<td>";
                    currentChoiceHTML +=           "<form method='POST' action='/Compete?Q_ID=" + (nextQuestionAvailable) + "'>";
                    currentChoiceHTML +=           "<div class='form-check'>";
                    currentChoiceHTML +=           "<label class='form-check-label'> &nbsp; " ;

                    int choiceID = Integer.parseInt ( "" + questionAnswers.getObject("id"));

                    //correctAnswerQuery = "Select correctanswer_choice_id from math.question "
                    if ( correctChoiceID ==  choiceID )
                    {
                        currentChoiceHTML += "<input type='radio' class='form-check-input correct' name='C_ID' value='"+ questionAnswers.getObject("id") + "'> ";
                        currentChoiceHTML += "<input type='hidden' name='isCorrect' value='True'> ";
                        currentChoiceHTML += "<input type='hidden' name='username' value='" + username + "'>";

                    }

                    else
                    {
                        currentChoiceHTML += "<input type='radio' class='form-check-input incorrect' name='C_ID' value='"+ questionAnswers.getObject("id") + "'> " ;
                        currentChoiceHTML += "<input type='hidden' name='isCorrect' value='False'> ";
                        currentChoiceHTML +="<input type='hidden' name='username' value='" + username + "'>";
                    }

                    currentChoiceHTML += "<input type='hidden' name='previousQuestionNumber' value='" + questionNumber + "'>";     // pass the question number
                    currentChoiceHTML += "<input type='hidden' name='pointValue' value='" + pointValue + "'> "; // pass the value of the question 
                    
                    // pass the current answer ID for 
                    currentChoiceHTML += "<input type='hidden' name='Selected_Choice_ID' value='"+ choiceID +"'>";


                    currentChoiceHTML +=           "&nbsp;" + questionAnswers.getObject("choicetext") + " ";
                    currentChoiceHTML +=           "</label> ";
                    currentChoiceHTML +=           "</div> ";
                    currentChoiceHTML +=           "</form> ";
                    currentChoiceHTML +=           "</td> ";
                    currentChoiceHTML +=           "</tr> " ;
		
					questionChoices.add(currentChoiceHTML);

                 //    out.println ( " <input type=\"radio\" name=\"choice\" value="+ questionAnswers.getObject("id") + " >&nbsp;&ensp;&ensp;" + questionAnswers.getObject("choicetext") + "<br><br> \n " );
                }

                out.println(questionChoices);

                out.println ("</table>");
            
            }

      

            out.println( ""
                +   "</div> \n"
                +   "<div class='jumbotron col-md-4' style='margin:20px'> \n"
                +   "<div class='panel panel-default'> \n"
                +   "<div class='panel-heading'> \n"
                +   "<h4> Scoreboard </h4> \n"
                +   "</div> \n"
                +   "<table class='table tbodyScoreboard theadScoreboard trScoreboard'> \n"
                +   "<thead> \n"
                +   "<tr><th class='col-lg-8'>Competitor</th><th class='col-lg-4'>Points</th></tr> \n"
                +   "</thead> \n"
                +   "<tbody> \n" ); 

                String usernameQuery = "Select * from Math.Competitor " ;
                ResultSet usernames = statement.executeQuery(usernameQuery);

                while (usernames.next() )
                {
                    out.println("<tr><td class='col-lg-8'>" + usernames.getObject("Username") + "</td><td class='col-lg-4'>" + usernames.getObject("score") + "</td></tr> ");
                } // end while

                out.println( ""
                +   "</table> \n"
                +   "</div> \n"
                +   "</div> \n"
                +   "</div> \n"
                +   "</div> \n"
                +   "<!-- End container of two jumbotrons --> \n"
                +   "</div> <!-- /container --> \n"
                +   " <!-- End outermost container -->\n " );




         

            }   // end if userloggedin is true 

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