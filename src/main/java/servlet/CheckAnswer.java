
import java.io.IOException ;
import java.io.PrintWriter ;

/*import javax.servlet.ServletException ;
import javax.servlet.http.HttpServlet ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;
import javax.servlet.http.HttpSession ;
import java.util.Random ;
*/
import  java.io.* ;
import  javax.servlet.* ;
import  javax.servlet.http.* ;
import  java.util.* ;
import  java.sql.* ;
import  javax.servlet.annotation.WebServlet ;

public class CheckAnswer extends HttpServlet
	{

	@Override
	protected void doPost (	HttpServletRequest  request,
							HttpServletResponse response )
		throws ServletException, IOException
		{

		 Connection connection     =  null ;	// added
 		 Statement  statement      =  null ;	// added
 		 HttpSession        session          =  request.getSession() ;
 		 int competitor_ID = -1; // declare competitor_ID

		final String DB_URL       =  System.getenv ( "JDBC_DATABASE_URL" ) ;
		
		response.setContentType ( "text/html" ) ;
		PrintWriter out = response.getWriter() ;
		String             username         =  request.getParameter("username") ;
		// find session ID and user ID

        /*
        session.setAttribute ("LOGGED_IN_USER", username);
        String loggedInUser = (String) session.getAttribute("LOGGED_IN_USER");
        */

        try
            {
            connection = DriverManager.getConnection ( DB_URL ) ;
    		statement = connection.createStatement() ;
            int questionNumber = Integer.parseInt(request.getParameter( "questionNumber" ));
			String usersChoiceID   = request.getParameter ( "choice" ) ;
			String usersChoiceText = "";
			String correctChoiceID = "" ; 
			String correctChoiceText = "";
			int largestQuestionID = 0;

			boolean addToScore = false;



			// Get the competitor ID for the insert query for submission
			String competitor_IDQuery = "SELECT * FROM Math.Competitor WHERE Username = '" + username + "' ";
			ResultSet competitor_IDRS = statement.executeQuery (competitor_IDQuery );
			if ( competitor_IDRS.next() )
				competitor_ID = Integer.parseInt ("" + competitor_IDRS.getObject("ID"));



			String submissionQuery = "UPDATE Math.submission "
									+"SET competitor_ID = "+ competitor_ID + ", Question_ID =" + questionNumber + ", AtTime = '" + (new java.util.Date() ) + "', Selected_Choice_ID = " + Integer.parseInt( usersChoiceID ) + " "
									+"WHERE competitor_ID = "+ competitor_ID + " AND Question_ID = " + questionNumber + "; ";

			int insertSubmission = statement.executeUpdate ( submissionQuery );

			// get the user choice to display ------------------------------------------------------------------------------
			String usersChoiceQuery = "select * "	
								+ "from math.choice "
								+ "inner join math.question "
								+ "on (choice.id = " + usersChoiceID +") "
								+ "where question.id =" + questionNumber + " " ;

			ResultSet usersChoice = statement.executeQuery ( usersChoiceQuery ) ;

			if (usersChoice.next() )
			{
				usersChoiceID = "" + usersChoice.getObject("ID");
				usersChoiceText = "" + usersChoice.getObject("ChoiceText"); 
			}
			//------------------------------------------------------------------------------

			String correctChoiceQuery = "select * "	
								+ "from math.choice "
								+ "inner join math.question "
								+ "on (choice.id = question.correctanswer_choice_id)  "
								+ "where question.id =" + questionNumber + " " ;

			ResultSet correctChoice = statement.executeQuery ( correctChoiceQuery ) ;

			if (correctChoice.next() )
			{
				correctChoiceID = "" + correctChoice.getObject("ID");
				correctChoiceText = "" + correctChoice.getObject("ChoiceText"); 
			}

            connection = DriverManager.getConnection ( DB_URL ) ;	// added
    		statement = connection.createStatement() ;				// added

            out.println ( "<!DOCTYPE html>" ) ;
            out.println ( "<html>" ) ;
            out.println ( "  <head> " ) ;
            out.println ( "    <meta charset = 'utf-8'>" ) ;
            out.println ( "    <title>Check Answer</title>" ) ;
            out.println ( "    <script type='text/javascript' async" ) ;
            out.println ( "      src='https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-MML-AM_CHTML'>" ) ;
            out.println ( "    </script>" ) ;
            out.println ( "  </head>" ) ;
            out.println ( "  <body>" ) ;
            out.println ( "    <hr>" ) ;
            
            String largestQuestionQuery = "select * from math.question ";
			ResultSet largestQuestionIDRS = statement.executeQuery (largestQuestionQuery);
			
			// -------------------------------------------------------------------------------
			// finds the largest question ID 
			while (largestQuestionIDRS.next() )
			{
				int currentQuestionID = Integer.parseInt( "" + largestQuestionIDRS.getObject("ID") );
				if (currentQuestionID > largestQuestionID )
					largestQuestionID = currentQuestionID ; 
			}


			//--------------------------------------------------------------------------------
            // find if the user is on the last question. if so send them to a logout screen
           if ( questionNumber  >= largestQuestionID )
           {
           		
           		out.println ( " <p> This is what the user chose: " + usersChoiceText + " <p> ");
	            out.println ( " <p> This is what is expected: " + correctChoiceText + " <p> ");
				if ( usersChoiceText .equals (correctChoiceText) )
				{
					out.println ( "	   <h1>Your answer was correct.</h1>" ) ;
					addToScore = true;
				}
				else
				{
					out.println ( "	   <h1>Your answer was not correct.</h1>" ) ;
					addToScore = false; 
				}

				out.println( "<form action = 'LogOut' method='POST'> " 
						+ 	 "<table>"
						+	 "<tr>	"
						+ "   <td><input type='hidden' name= 'username' value='" + username + "'></td> "
						+	"<td><input type='hidden'  name= 'questionNumber' value='" + (questionNumber+1) + "'></td> " 
						  + "<td><input type='submit' value='Log Out'>"
					    // + " <button type='submit' class='inline_wide' formaction='LogOut' name='username' value='" + username + "'>Log Out</button> "
	     				+	 "</tr>"
	     				+	 "</table>" 
	     				+	 "</form>" );
           }

           else
           { 
	            out.println ( " <p> This is what the user chose: " + usersChoiceText + " <p> ");
	            out.println ( " <p> This is what is expected: " + correctChoiceText + " <p> ");
				if ( usersChoiceText .equals (correctChoiceText) )
				{
					out.println ( "	   <h1>Your answer was correct.</h1>" ) ;
					addToScore = true;
				}
				else
				{
					out.println ( "	   <h1>Your answer was not correct.</h1>" ) ;
					addToScore = false ;
				}

				out.println( "<form action = 'Compete' method='POST'> " 
						+ 	 "<table>"
						+	 "<tr>	"
						+		"<td><input type='hidden' name= 'questionNumber' value='" + (questionNumber+1) + "'> " 
	     				+ 		"<td><input type='submit' value='View the Next Question'>"
	     				+	 "</tr>"
	     				+	 "</table>" 
	     				+	 "</form>" );

			 }

			 // ---------------------------------------------------------------------------

			 // UPDATE totals 
			 //		SET total = total + 1
			 // WHERE name = 'bill';
			if (addToScore == true)
			{	
				 String scoreQuery =  "UPDATE Math.competitor "
				 					+ "SET score = score + 1 "
				 					+ "WHERE lower(Username) = lower('" + username + "') " ; 
				 int scoreIncrements = statement.executeUpdate (scoreQuery);
			}
			 

			 	out.println( "<table>"); // open the table
			 	out.println( "<tr> "
			 				+ "	<td><strong>Username</strong></td> "
			 				+ " <td><strong>Score</strong> "
			 				+ " </tr> " ) ;
			 	
			String tableQuery = "select * from math.competitor ";
			ResultSet competitorNames = statement.executeQuery (tableQuery);

			while (competitorNames.next())
			{
				out.println("<tr> "
					+ " 	<td>" + competitorNames.getObject("username") + "</td> "
					+ "		<td>" + competitorNames.getObject("score") + "</td>" 
					+ "	 	</tr>" ) ;
			}
				
			

				out.println("</table>"); // close the table




	            out.println ( "	   <hr>" ) ;
	            out.println ( "  </body>" ) ;
	            out.println ( "</html>" ) ;
        	
			} // end try block

		catch ( SQLException sqlException ) 
    	{
    		System.out.println ( "Caught SQLException ..." ) ;
    		sqlException.printStackTrace() ;
    		System.exit ( 1 ) ;
    	} // end catch
		finally	{ out.close() ; }
		} // end doPost method

	} // CheckAnswer class
