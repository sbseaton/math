// ------------------------------------------------------------------------------------ //
// Compete.java    by ZE Glass    14 OCT 2016
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
  public void doPost ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {
    Connection connection     =  null ;
    Statement  statement      =  null ;
    final String DB_URL       =  System.getenv ( "JDBC_DATABASE_URL" ) ;
    response.setContentType ( "text/html" ) ;
    final PrintWriter  out              =  response.getWriter() ;
    HttpSession        session          =  request.getSession() ;
    
    // session handling
    String             sessionId        =  session.getId() ;
    String             userIdAttribute  =  ( "LOGGED_IN_USER_" + sessionId ) ;
    
    // user entered data from client 
    ServletContext     context          =  this.getServletContext() ;
    String             username         =  request.getParameter("username") ;
    //Initialize Competitor_ID (will be set later)
    int currentCompetitorID = -1;

    int currentQuestionID;
    String  currentQuestionIDString     = request.getParameter ( "Q_ID" );
    if (currentQuestionIDString != null)
        currentQuestionID  = Integer.parseInt(currentQuestionIDString) ;
    else
        currentQuestionID = -1;// id of -1 indicates no question has been selected

    try
    {
        connection = DriverManager.getConnection ( DB_URL ) ;// connect to database
        statement = connection.createStatement() ;

        //------------ Update User Score if Last Choice is Correct ---------------------//

        boolean isPreviousChoiceCorrect = false;
        String  isPreviousChoiceCorrectString     = request.getParameter ( "isCorrect" );
        if (isPreviousChoiceCorrectString != null)
            isPreviousChoiceCorrect  = Boolean.parseBoolean(isPreviousChoiceCorrectString) ;

        
        int prevQuestionPointValue = 0;
        String prevQuestionPointValueString = request.getParameter ( "PointValue" );
        if (prevQuestionPointValueString != null){
            prevQuestionPointValue = Integer.parseInt( prevQuestionPointValueString );
        }

        if (isPreviousChoiceCorrect)
        {
            String updateScoreQuery = "UPDATE Math.Competitor SET score = score + " + prevQuestionPointValue + " WHERE lower(username) = lower('" + username + "')" ;
            int something = statement.executeUpdate(updateScoreQuery);// execute SQL update statement
        }// end if
        

        // print head of HTML
        out.print (  "<!DOCTYPE html>\n"
                +    "<html>\n"
                +    "  <head>\n"
                +    "    <meta charset='UTF-8'>\n"
                +    "    <title>Compete</title>\n"
                +    "    <style>\n"
                +    "     .dropdown:hover .dropdown-menu { display: block; }\n"
                +    "     .equal { display: flex; display: -webkit-flex; }\n"
                +    "     .tbodyScoreboard { display:block; height:300px; overflow:auto; }\n"
                +    "     .theadScoreboard .tbodyScoreboard .trScoreboard { display:table; width:100%; table-layout:fixed; }\n"
                +    "     .theadScoreboard { width: calc( 100% - 1em ) }\n"
                +    "    </style>\n"
                //---------------  Bootstrap Import ----------------------
                +    "    <script src='javascript/jquery-3.2.0.min.js'></script>\n"

                +    "    <!-- Latest compiled and minified CSS -->"
                +    "    <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css' integrity='sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u' crossorigin='anonymous'>\n"
                +    "    <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css' integrity='sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp' crossorigin='anonymous'>\n"
                +    "    <!-- Latest compiled and minified JavaScript -->\n"
                +    "    <script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js' integrity='sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa' crossorigin='anonymous'></script>\n"
                //--------------
                +    "  <script type='text/javascript' async\n"
                +    "   src='https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-MML-AM_CHTML'>\n"
                +    "  </script>\n"

                +    "<script>\n"
                +    "  $(document).on('change','input[type=radio]', function( event )\n"
                +    "    {\n"
                +    "    if ( $(event.target).hasClass('correct') )\n"
                +    "      $('#feedbackHeader').text( 'Correct!' ) ;\n"
                +    "    else if ( $(event.target).hasClass('incorrect') )\n"
                +    "      $('#feedbackHeader').text( 'Sorry, incorrect.' ) ;\n"
                +    "    $('#feedbackModal').modal ( 'show' ) ;\n"
                +    "    setTimeout ( function() { $('#feedbackModal').modal('hide'); }, 2000 ) ;\n"
                +    "    setTimeout ( function() { $(event.target).closest('form').submit() }, 2500 ) ;\n"
                +    "    } ) ;\n"
                +    "  $(document).on('click','#updateScoreboardButton', function( ) {$('#updateScoreboardButton').closest('form').submit();} ) ;\n"
                +    "</script>\n"

                +    "  </head>\n"
                +    "  <body>\n"
                +    "      <div class='container'>\n"
                +    "        <div class='modal fade' id='feedbackModal' role='dialog'>\n"
                +    "          <div class='modal-dialog modal-sm'>\n"
                +    "            <div class='modal-content'>\n"
                +    "              <div class='modal-header'>\n"
                +    "                <button type='button' class='close' data-dismiss='modal'>&times;</button>\n"
                +    "                <h4 class='modal-title' id='feedbackHeader'>TO BE SET BY SCRIPT</h4>\n"
                +    "              </div>\n"
                +    "            </div>\n"
                +    "          </div>\n"
                +    "        </div>\n"
                +    "      </div>\n");
        
        boolean usernameAvailable = false;

        //------------- Check if username is already taken --------------------
        String usernameExistsQuery = "SELECT * FROM Math.Competitor WHERE LOWER(username) = LOWER('"+ username +"')";
        ResultSet usernameExistsResultSet = statement.executeQuery ( usernameExistsQuery ) ;
        if ( ! usernameExistsResultSet.next() )
            usernameAvailable = true;
        //-----------------------------------------------------------------------

        boolean  isUserLoggedIn  = false;

        if ( session.isNew() )
        {
            // new login
            // user has submitted desired username from HTML index page
            if ( usernameAvailable )
            {
                session.setAttribute ( userIdAttribute, username ) ;

                //---------------- insert username into Math.Competitor table ---------------------//
                String insertUsernameQuery = "INSERT INTO Math.Competitor ( Username, Score ) VALUES ( '" + username + "', 0 )";
                int insertUsernameExecution = statement.executeUpdate ( insertUsernameQuery ) ;
                //---------------------------------------------------------------------------------//

                isUserLoggedIn = true;



                //------------- Insert the submission made into the Submission Table ------------------//

                // get Question_ID of Previous problem
                int prevQuestionID = -1;
                String  prevQuestionIDString     = request.getParameter ( "prevQuestionID" );
                if (prevQuestionIDString != null)
                    prevQuestionID  = Integer.parseInt(prevQuestionIDString) ;

                // get ID of the selected choice for Previous problem 
                int selectedChoiceID = -1;
                String  selectedChoiceIDString     = request.getParameter ( "selectedChoiceID" );
                if (selectedChoiceIDString != null)
                    selectedChoiceID  = Integer.parseInt(selectedChoiceIDString) ;

                String competitorIDQuery = "SELECT * FROM Math.Competitor WHERE lower(Username) = lower('" + username + "') ";
                
                ResultSet competitorIDResultSet = statement.executeQuery ( competitorIDQuery ) ;
                
                //out.println("competitorIDResultSet.next() = " + competitorIDResultSet.next() );

                if ( competitorIDResultSet.next() )
                {
                    currentCompetitorID = Integer.parseInt( "" + competitorIDResultSet.getObject("ID") );
                }

                if (prevQuestionIDString != null && selectedChoiceIDString != null) {
                    String insertQuery = "INSERT INTO Math.Submission ( Competitor_ID, Question_ID, AtTime, Selected_Choice_ID ) "
                                       + "VALUES( "+ currentCompetitorID +", "+ prevQuestionID +", '"+ ( new java.util.Date() ) +"', "+ selectedChoiceID +" )";
                    int insertExecution = statement.executeUpdate ( insertQuery ) ;
                }// end if

                //----------------------------------------------------------------------------//

            }// end if
            else 
            {
                // username is not available
                isUserLoggedIn = false;                         //---------------------- EDIT
                session.invalidate() ; // log the user out
                out.println("<meta http-equiv='refresh' content='0; url=/index.html' />");//---------------------- EDIT
                out.print  (  "    <hr>\n"
                         +    "    <form method='POST'>\n"
                         +    "      <table class='table table-bordered'>\n"  
                         +    "        <tr style='font-size:x-large;'><td>No user is currently logged in.</td></tr>\n"
                         +    "        <tr><td><button type='submit' class='inline_wide  btn btn-primary btn-lg' formaction='index.html'>Log In</button></td></tr>\n"
                         +    "      </table>\n"
                         +    "    </form>\n"
                         +    "    <hr>\n"
                         +    "  </body>\n"
                         +    "</html>\n"  ) ;
                out.close() ;
                return ;
            }// end if not successful login
        }// end if session is new
        else 
        {
            // session is *not* new, user is logged in
            // probably returning from answering a question servlet
            isUserLoggedIn = true;
            username  =  (String) session.getAttribute ( userIdAttribute ) ;


            //------------- Insert the submission made into the Submission Table ------------------//

            // get Question_ID of Previous problem
            int prevQuestionID = -1;
            String  prevQuestionIDString     = request.getParameter ( "prevQuestionID" );
            if (prevQuestionIDString != null)
                prevQuestionID  = Integer.parseInt(prevQuestionIDString) ;

            // get ID of the selected choice for Previous problem 
            int selectedChoiceID = -1;
            String  selectedChoiceIDString     = request.getParameter ( "selectedChoiceID" );
            if (selectedChoiceIDString != null)
                selectedChoiceID  = Integer.parseInt(selectedChoiceIDString) ;

            String competitorIDQuery = "SELECT * FROM Math.Competitor WHERE lower(Username) = lower('" + username + "') ";
            
            ResultSet competitorIDResultSet = statement.executeQuery ( competitorIDQuery ) ;
            

            if ( competitorIDResultSet.next() )
            {
                currentCompetitorID = Integer.parseInt( "" + competitorIDResultSet.getObject("ID") );
            }

            if (prevQuestionIDString != null && selectedChoiceIDString != null) {
                String insertQuery = "INSERT INTO Math.Submission ( Competitor_ID, Question_ID, AtTime, Selected_Choice_ID ) "
                                   + "VALUES( "+ currentCompetitorID +", "+ prevQuestionID +", '"+ ( new java.util.Date() ) +"', "+ selectedChoiceID +" )";
                int insertExecution = statement.executeUpdate ( insertQuery ) ;
            }// end if

            //----------------------------------------------------------------------------//



        } // end if else is session new or old

        // -------------------------- HTML if Logged in ------------------------------------- // 
        if (isUserLoggedIn)
        {
            //------------------ Begin Navbar --------------------//
            out.println ( "<div class='container'>");
            out.println ( "<nav class='navbar navbar-default'>");
            out.println ( "<div class='container-fluid'>");
            //out.println ( "    <div class='collapse navbar-collapse' id='bs-example-navbar-collapse-1'>");
            out.println ( "<ul class='nav navbar-nav'>");

            out.println ( "    <li class='dropdown'>");
            out.println ( "        <a href='#' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>Easy <span class='caret'></span></a>");
            out.println ( "        <ul class='dropdown-menu'>");

            //------------------------- Save user's answers in ArrayList ----------------------//

            ArrayList<Boolean> questionsAnswered = new ArrayList<Boolean>();// holds which quesiton currentCompetitor has answered and if correct or not
            // fill list with null values
            for (int i = 0; i < 40 ; i++ ) {
                questionsAnswered.add(null);
            }// end for

            String getAnswersQuery = "SELECT * FROM Math.Question LEFT JOIN Math.Submission ON (Question.ID = Submission.Question_ID) WHERE competitor_ID = " + currentCompetitorID + " ";
           
            ResultSet getAnswersRS = statement.executeQuery(getAnswersQuery);

            while ( getAnswersRS.next() )
            {
                int tempQuestionID  = Integer.parseInt("" + getAnswersRS.getObject("Question_ID") );
                int correctAnswerID = Integer.parseInt("" + getAnswersRS.getObject("CorrectAnswer_Choice_ID"));
                int usersAnswerID   = Integer.parseInt("" + getAnswersRS.getObject("Selected_Choice_ID") );

                if ( usersAnswerID == correctAnswerID )
                {
                    questionsAnswered.add(tempQuestionID, true);
                } else {
                    questionsAnswered.add(tempQuestionID, false);
                }// end if else

            }// end while

            //--------------------------------------------------------//

            String easyQuestionsQuery = "SELECT * FROM Math.Question WHERE PointValue = 2";
            ResultSet easyQuestionsResultSet = statement.executeQuery ( easyQuestionsQuery ) ;

            //output EASY questions to dropdown menu
            while ( easyQuestionsResultSet.next() ) 
            {

                String liClasses = "";
                Boolean isCorrect = questionsAnswered.get( Integer.parseInt( "" + easyQuestionsResultSet.getObject("ID") ) ) ;

                if (isCorrect == null) {
                    liClasses = "";
                } else {
                    if (isCorrect) {
                        liClasses = "bg-success disabled";
                    } else {
                        liClasses = "bg-danger disabled";
                    }// end inner if else
                }// end outer if-else
    
                String tempQuestionText = (String) easyQuestionsResultSet.getObject("QuestionText") ;   
                int    tempQuestionID   = Integer.parseInt ( "" + easyQuestionsResultSet.getObject("id") ) ;
                out.println ( "             <li class='" + liClasses + "'><a  class='not-active' data-target='/Compete' href='/Compete?Q_ID="+ tempQuestionID +"'>"+ tempQuestionText +"</a></li>");
            
            }// end while (next result set)

            out.println ( "             <li role='separator' class='divider'></li>");
            out.println ( "             <li class='dropdown-header disabled'>Legend</li>");
            out.println ( "             <li class='bg-success disabled'><a href='#'>Green = Answered Correctly</a></li>");
            out.println ( "             <li class='bg-danger disabled'><a href='#'>Red = Answered Incorrectly</a></li>");
            out.println ( "         </ul>");
            out.println ( "    </li>");

            out.println ( "    <li class='dropdown'>");
            out.println ( "        <a href='#' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>Medium <span class='caret'></span></a>");
            out.println ( "        <ul class='dropdown-menu'>");

            String mediumQuestionsQuery = "SELECT * FROM Math.Question WHERE PointValue = 3";
            ResultSet mediumQuestionsResultSet = statement.executeQuery ( mediumQuestionsQuery ) ;

            //output MEDIUM questions to dropdown menu
            while ( mediumQuestionsResultSet.next() ) 
            {
                String liClasses = "";
                Boolean isCorrect = questionsAnswered.get( Integer.parseInt( "" + mediumQuestionsResultSet.getObject("ID") ) ) ;

                if (isCorrect == null) {
                    liClasses = "";
                } else {
                    if (isCorrect) {
                        liClasses = "bg-success disabled";
                    } else {
                        liClasses = "bg-danger disabled";
                    }// end inner if else
                }// end outer if-else


                String tempQuestionText = (String) mediumQuestionsResultSet.getObject("QuestionText") ; 
                int    tempQuestionID   = Integer.parseInt ( "" + mediumQuestionsResultSet.getObject("id") ) ;
                out.println ( "             <li class='"+ liClasses +"'><a  class='not-active' data-target='/Compete' href='/Compete?Q_ID="+ tempQuestionID +"'>"+ tempQuestionText +"</a></li>\n");
            }// end while (next result set)

            out.println ( "             <li role='separator' class='divider'></li>");
            out.println ( "             <li class='dropdown-header disabled'>Legend</li>");
            out.println ( "             <li class='bg-success disabled'><a href='#'>Green = Answered Correctly</a></li>");
            out.println ( "             <li class='bg-danger disabled'><a href='#'>Red = Answered Incorrectly</a></li>");
            out.println ( "         </ul>");
            out.println ( "    </li>");

            out.println ( "    <li class='dropdown'>");
            out.println ( "        <a href='#' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>Hard <span class='caret'></span></a>");
            out.println ( "        <ul class='dropdown-menu'>");
            
            String hardQuestionsQuery = "SELECT * FROM Math.Question WHERE PointValue = 4";
            ResultSet hardQuestionsResultSet = statement.executeQuery ( hardQuestionsQuery ) ;

            //output HARD questions to dropdown menu
            while ( hardQuestionsResultSet.next() ) 
            {
                String liClasses = "";
                Boolean isCorrect = questionsAnswered.get( Integer.parseInt( "" + hardQuestionsResultSet.getObject("ID") ) ) ;

                if (isCorrect == null) {
                    liClasses = "";
                } else {
                    if (isCorrect) {
                        liClasses = "bg-success disabled";
                    } else {
                        liClasses = "bg-danger disabled";
                    }// end inner if else
                }// end outer if-else


                String tempQuestionText = (String) hardQuestionsResultSet.getObject("QuestionText") ;   
                int    tempQuestionID   = Integer.parseInt ( "" + hardQuestionsResultSet.getObject("id") ) ;
                out.println ( "             <li class='"+ liClasses +"'><a  class='not-active' data-target='/Compete' href='/Compete?Q_ID="+ tempQuestionID +"'>"+ tempQuestionText +"</a></li>");
            }// end while (next result set)

            out.println ( "             <li role='separator' class='divider'></li>");
            out.println ( "             <li class='dropdown-header disabled'>Legend</li>");
            out.println ( "             <li class='bg-success disabled'><a href='#'>Green = Answered Correctly</a></li>");
            out.println ( "             <li class='bg-danger disabled'><a href='#'>Red = Answered Incorrectly</a></li>");
            out.println ( "         </ul>");
            out.println ( "    </li>");
            out.println ( "</ul>");

            // Update Scores button
            //out.println ("<form class='navbar-form navbar-right'>");
            out.println ( " <form method='POST' action='/Compete' class='navbar-form navbar-right'>");
            out.println ( "     <button type='button' class='btn btn-primary'>");
            out.println ( "         Update Scores");
            out.println ( "     </button>");
            out.println ( " </form>");

            //out.println ( "     </div><!-- /.navbar-collapse -->");
            out.println ( "   </div><!-- /.container-fluid -->");
            out.println ( " </nav>");
            //---------------- End Navbar ---------------------//


            out.println(" <div class='container-fluid'>" ) ;
            out.println("        <div class='row equal'>" ) ;

            // print .col-md-8 (Into / Question & Options)
            out.println("          <div class='jumbotron col-md-8' style='margin:20px'>" ) ;
            if (currentQuestionID == -1) {
                out.println("            <h2 class='display-3'>Welcome to Zach's Mental Math Competition!</h2>" ) ;
                out.println("            <hr class='my-4'>" ) ;
                out.println("            <p class='lead'>Neither calculator nor scratchpaper are permitted.  You get one guess for each question.</p>" ) ;
                out.println("            <hr class='my-4'>" ) ;
                out.println("            <p>To begin, select a question from a drop-down menu above -- Easy, Medium, or Difficult.</p>" ) ;
                out.println("            <hr class='my-4'>" ) ;
            } else {

                // get current question text from database
                String currentQuestionQueryString = " SELECT * FROM Math.Question WHERE id = " + currentQuestionID + " ";
                ResultSet currentQuestionResultSet = statement.executeQuery ( currentQuestionQueryString ) ;
                

                String currentQuestionText = "";
                int correctAnswer_Choice_ID = -1;
                int currQuestionPointValue = 0;
                if ( currentQuestionResultSet.next() ) // if result set is not empty
                {
                    currentQuestionText  =  (String) currentQuestionResultSet.getObject("QuestionText") ;   
                    correctAnswer_Choice_ID = Integer.parseInt( "" +  currentQuestionResultSet.getObject("correctAnswer_Choice_ID") ) ;
                    currQuestionPointValue = Integer.parseInt( "" + currentQuestionResultSet.getObject("PointValue") );
                }
                
                out.println("   <h3>&nbsp; "+ currentQuestionText +" &nbsp;</h3>");
                
                // get the questions options from the database
                String choiceQueryString = "SELECT * FROM Math.Choice "
                            + "INNER JOIN Math.Question "
                            + " ON (Choice.ID = Question.Foil1_Choice_ID) "
                            + " OR (Choice.ID = Question.Foil2_Choice_ID) "
                            + " OR (Choice.ID = Question.Foil3_Choice_ID) "
                            + " OR (Choice.ID = Question.CorrectAnswer_Choice_ID) "
                            + " WHERE Question.ID = " + currentQuestionID + " ";
                ResultSet choiceResultSet = statement.executeQuery ( choiceQueryString ) ;


            //  out.println("   <form method='POST' action='/Compete?Q_ID="+ currentQuestionID +"'>") ;
                out.println("   <table class='table table-bordered table-hover'>");

                while ( choiceResultSet.next() )
                {
                    String currentChoiceText = (String) choiceResultSet.getObject("choicetext");
                    int currentChoiceID   = Integer.parseInt("" + choiceResultSet.getObject("id"));
                    out.println("      <tr>") ;
                    out.println("          <td>") ;
                    out.println("              <div class='form-check'>") ;
                    out.println("                <form method='POST' action='/Compete?Q_ID="+ (currentQuestionID+1) +"'>") ;
                    out.println("                  <label class='form-check-label'> &nbsp;") ;
                    if ( correctAnswer_Choice_ID == currentChoiceID )
                    {
                        out.println("                      <input type='radio' class='form-check-input correct' name='C_ID' value='"+ choiceResultSet.getObject("id") +"'>") ;
                        out.println("                      <input type='hidden' name='isCorrect' value='True'>") ;
                        
                    }
                    else
                    {
                        out.println("                      <input type='radio' class='form-check-input incorrect' name='C_ID' value='"+ choiceResultSet.getObject("id") +"'>") ;
                        out.println("                      <input type='hidden' name='isCorrect' value='False'>") ;
                    }
                    
                    //out.println("                          <input type='hidden' name='previousQ_ID' value='" + currentQuestionID + "'>") ;
                    out.println("                              &nbsp;" + currentChoiceText) ;
                    out.println("                      <input type='hidden' name='selectedChoiceID' value='" + currentChoiceID + "'>") ;
                    out.println("                   <input type='hidden' name='prevQuestionID' value='" + currentQuestionID + "'>") ;// used to insert into Submission table
                    out.println("                   <input type='hidden' name='PointValue' value='" + currQuestionPointValue + "'>") ;
                    out.println("                   <input type='hidden' name='username'   value='" + username + "'>") ;
                    out.println("                  </label>") ;
                    out.println("                </form>") ;
                    out.println("              </div>") ;
                    out.println("          </td>") ;
                    out.println("      </tr>") ;
                }// end while
                out.println("   </table>");
            }// end if else
            

            out.println("          </div><!--/.jumbotron.col-md-8 -->" ) ;

            // print .col-md-4 (Scoreboard)
            out.println("       <div class='jumbotron col-md-4' style='margin:20px'>");
            out.println("           <div class='panel panel-default'>");
            out.println("               <div class='panel-heading'>");
            out.println("                   <h4> Scoreboard </h4>");
            out.println("               </div>");
            out.println("               <table class='table tbodyScoreboard theadScoreboard trScoreboard'>");
            out.println("                   <tr><th class='col-lg-8'>Competitor</th><th class='col-lg-4'>Points</th></tr>");

            // Query Scores
            String retreiveScoreQuery = "SELECT * FROM Math.Competitor";
            ResultSet retreiveScoreResultSet = statement.executeQuery ( retreiveScoreQuery ) ;
            while ( retreiveScoreResultSet.next() )
            {
                String tempUsername = "" + retreiveScoreResultSet.getObject("Username");
                String tempScore    = "" + retreiveScoreResultSet.getObject("Score");
                out.println("                   <tr><td class='col-lg-8'>" + tempUsername + "</td><td class='col-lg-4'>" + tempScore + "</td></tr>");
            }// end while

            out.println("               </table>");
            out.println("           </div><!-- /.panel-heading -->");
            out.println("       </div>");
            out.println("   </div><!-- /.row.equal -->");
            out.println("</div> <!-- /.container-fluid -->");
        }// end if (isUserLoggedIn  )
        else
        {
            out.println("<meta http-equiv='refresh' content='0; url=/index.html' />"); //---------------------- EDIT
        }// end else (isUserLoggedIn)

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

    out.println ( "    </div> <!-- /.container-->");
    out.println ( "  </body>\n" +  "</html>\n"  ) ;

    out.close() ;
    return ;
  } // end doPost method


  // ------------------  method to service HTTP GET requests  --------------------- //
  @Override
  public void doGet ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {
    // HTTP GET requests are forwarded on to the doPost method
    // (i.e., toPost handles both HTTP GET and HTTP POST requests)
    doPost ( request, response ) ;
  } // end doGet method
  // ------------------------------------------------------------------------------------ //


} // end Compete class
